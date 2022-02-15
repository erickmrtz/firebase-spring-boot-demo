package me.erickmrtz.demo.firebase.mapper;

import me.erickmrtz.demo.firebase.collection.Product;
import me.erickmrtz.demo.firebase.dto.StoreGetIdResponse;
import me.erickmrtz.demo.firebase.dto.StorePostRequest;
import me.erickmrtz.demo.firebase.dto.StorePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StoreMapperService {

    StoreMapperService INSTANCE = Mappers.getMapper(StoreMapperService.class);

    @Mapping(target = "documentId", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "creationDate", expression = "java(new java.util.Date())")
    Product mapToProduct(StorePostRequest request);

    StorePostResponse mapToStorePostResponse(Product product);

    StoreGetIdResponse mapToStoreGetIdResponse(Product product);
}
