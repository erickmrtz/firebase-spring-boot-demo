package me.erickmrtz.demo.firebase.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.erickmrtz.demo.firebase.collection.Product;
import me.erickmrtz.demo.firebase.dto.StoreDeleteResponse;
import me.erickmrtz.demo.firebase.dto.StoreGetIdResponse;
import me.erickmrtz.demo.firebase.dto.StorePatchRequest;
import me.erickmrtz.demo.firebase.dto.StorePostRequest;
import me.erickmrtz.demo.firebase.dto.StorePostResponse;
import me.erickmrtz.demo.firebase.exception.BadRequestException;
import me.erickmrtz.demo.firebase.exception.FirebaseException;
import me.erickmrtz.demo.firebase.exception.NotFoundException;
import me.erickmrtz.demo.firebase.mapper.StoreMapperService;
import me.erickmrtz.demo.firebase.service.StoreService;
import me.erickmrtz.demo.firebase.util.Constant;
import me.erickmrtz.demo.firebase.util.Functions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private static final Firestore FIRESTORE = FirestoreClient.getFirestore();
    private final StoreMapperService storeMapperService;

    @Resource(name = "firebaseStorage")
    private StorageOptions storageOptions;

    @Resource(name = "bucketStorage")
    private String storageBucket;

    @Override
    public List<StoreGetIdResponse> getAllProducts() {
        List<StoreGetIdResponse> productList = new ArrayList<>();
         for(DocumentReference documentReference : FIRESTORE.collection(Constant.PRODUCT_COLLECTION).listDocuments()) {
             productList.add(storeMapperService.mapToStoreGetIdResponse(getProduct(documentReference)));
         }

        return productList;
    }

    @Override
    public StoreGetIdResponse getProductById(String documentId) {
        DocumentReference documentReference = FIRESTORE
                .collection(Constant.PRODUCT_COLLECTION)
                .document(documentId);

        return storeMapperService.mapToStoreGetIdResponse(getProduct(documentReference));
    }

    @Override
    public StorePostResponse registerProduct(StorePostRequest request) {
        Product product = storeMapperService.mapToProduct(request);

        ApiFuture<WriteResult> collectionsApiFuture = FIRESTORE
                .collection(Constant.PRODUCT_COLLECTION)
                .document(product.getDocumentId())
                .set(product);

        if(collectionsApiFuture.isCancelled()) {
            throw new FirebaseException(Constant.FIREBASE_EXCEPTION_ERROR);
        }

        return storeMapperService.mapToStorePostResponse(product);
    }

    @Override
    public StoreDeleteResponse removeProduct(String documentId) {
        ApiFuture<WriteResult> collectionsApiFuture = FIRESTORE
                .collection(Constant.PRODUCT_COLLECTION)
                .document(documentId)
                .delete();

        if(collectionsApiFuture.isCancelled()) {
            throw new FirebaseException(Constant.FIREBASE_EXCEPTION_ERROR);
        }

        return StoreDeleteResponse.builder().documentId(documentId).build();
    }

    @Override
    public void uploadProductPhoto(String documentId, MultipartFile file) {
        DocumentReference documentReference = FIRESTORE
                .collection(Constant.PRODUCT_COLLECTION)
                .document(documentId);

        Product product = getProduct(documentReference);
        String fileName = Functions.getFileName(file.getOriginalFilename());

        File tempFile = new File(fileName);

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());

            String fileLocation = Functions.getFileLocation(fileName);
            String fileSignedUrl = uploadFile(fileLocation, tempFile, file.getContentType());

            product.setImageUrl(fileSignedUrl);
            product.setModificationDate(new Date());

            FIRESTORE
                    .collection(Constant.PRODUCT_COLLECTION)
                    .document(product.getDocumentId())
                    .set(product);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new BadRequestException(Constant.FILE_EXCEPTION_ERROR);
        }

        if(!tempFile.delete()) {
            log.info("Failed deleting file");
        }
    }

    @Override
    public void updateProductQuantity(String documentId, StorePatchRequest request) {
        DocumentReference documentReference = FIRESTORE
                .collection(Constant.PRODUCT_COLLECTION)
                .document(documentId);

        Product product = getProduct(documentReference);
        product.setQuantity(request.getQuantity());
        product.setModificationDate(new Date());

        FIRESTORE
                .collection(Constant.PRODUCT_COLLECTION)
                .document(documentId)
                .set(product);
    }

    private Product getProduct(DocumentReference documentReference) {
        try {
            ApiFuture<DocumentSnapshot> documentFuture = documentReference.get();
            DocumentSnapshot document = documentFuture.get();

            if(!document.exists()) throw new NotFoundException(Constant.PRODUCT_EXCEPTION_ERROR);
            return document.toObject(Product.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new FirebaseException(Constant.FIREBASE_EXCEPTION_ERROR);
        }
    }

    private String uploadFile(String fileLocation, File file, String contentType) throws IOException {
        BlobId blobId = BlobId.of(storageBucket, fileLocation);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();

        Storage storage = storageOptions.getService();
        Blob blob = storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        URL url = blob.signUrl(Constant.URL_MAX_DAYS, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature());
        return url.toString();
    }
}
