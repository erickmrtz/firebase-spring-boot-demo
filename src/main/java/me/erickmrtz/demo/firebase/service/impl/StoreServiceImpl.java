package me.erickmrtz.demo.firebase.service.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import me.erickmrtz.demo.firebase.collection.Product;
import me.erickmrtz.demo.firebase.dto.StoreDeleteResponse;
import me.erickmrtz.demo.firebase.dto.StoreGetIdResponse;
import me.erickmrtz.demo.firebase.dto.StorePostRequest;
import me.erickmrtz.demo.firebase.dto.StorePostResponse;
import me.erickmrtz.demo.firebase.exception.FirebaseException;
import me.erickmrtz.demo.firebase.exception.NotFoundException;
import me.erickmrtz.demo.firebase.mapper.StoreMapperService;
import me.erickmrtz.demo.firebase.service.StoreService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreMapperService storeMapperService;
    private final String PRODUCT_COLLECTION = "Products";
    private static final Firestore FIRESTORE = FirestoreClient.getFirestore();

    @Override
    public List<StoreGetIdResponse> getAllProducts() {
        List<StoreGetIdResponse> productList = new ArrayList<>();
         for(DocumentReference documentReference :FIRESTORE.collection(PRODUCT_COLLECTION).listDocuments()) {
             productList.add(getStoreGetResponse(documentReference));
         }

        return productList;
    }

    @Override
    public StoreGetIdResponse getProductById(String documentId) {
        DocumentReference documentReference = FIRESTORE
                .collection(PRODUCT_COLLECTION)
                .document(documentId);

        return getStoreGetResponse(documentReference);
    }

    @Override
    public StorePostResponse registerProduct(StorePostRequest request) {
        Product product = storeMapperService.mapToProduct(request);

        ApiFuture<WriteResult> collectionsApiFuture = FIRESTORE
                .collection(PRODUCT_COLLECTION)
                .document(product.getDocumentId())
                .set(product);

        if(collectionsApiFuture.isCancelled()) {
            throw new FirebaseException("Error connecting with database");
        }

        return storeMapperService.mapToStorePostResponse(product);
    }

    @Override
    public StoreDeleteResponse removeProduct(String documentId) {
        return null;
    }

    private StoreGetIdResponse getStoreGetResponse(DocumentReference documentReference) {
        try {
            ApiFuture<DocumentSnapshot> documentFuture = documentReference.get();
            DocumentSnapshot document = documentFuture.get();

            if(!document.exists()) throw new NotFoundException("Product not found");
            Product product = document.toObject(Product.class);

            return storeMapperService.mapToStoreGetIdResponse(product);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new FirebaseException("Error connecting with database");
        }
    }
}
