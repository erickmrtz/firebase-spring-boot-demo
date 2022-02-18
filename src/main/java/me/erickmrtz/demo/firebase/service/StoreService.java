package me.erickmrtz.demo.firebase.service;

import me.erickmrtz.demo.firebase.dto.StoreDeleteResponse;
import me.erickmrtz.demo.firebase.dto.StoreGetIdResponse;
import me.erickmrtz.demo.firebase.dto.StorePatchRequest;
import me.erickmrtz.demo.firebase.dto.StorePostRequest;
import me.erickmrtz.demo.firebase.dto.StorePostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoreService {

    List<StoreGetIdResponse> getAllProducts();
    StoreGetIdResponse getProductById(String documentId);
    StorePostResponse registerProduct(StorePostRequest request);
    StoreDeleteResponse removeProduct(String documentId);
    void uploadProductPhoto(String documentId, MultipartFile file);
    void updateProductQuantity(String documentId, StorePatchRequest request);
}
