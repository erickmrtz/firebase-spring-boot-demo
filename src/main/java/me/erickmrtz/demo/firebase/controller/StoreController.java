package me.erickmrtz.demo.firebase.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.erickmrtz.demo.firebase.dto.Response;
import me.erickmrtz.demo.firebase.dto.StoreGetIdResponse;
import me.erickmrtz.demo.firebase.dto.StorePostRequest;
import me.erickmrtz.demo.firebase.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
@Api(value = "/store", tags = "Store")
public class StoreController {

    private final StoreService storeService;

    @ApiOperation(
            value = "Get Products",
            nickname = "Get Products",
            notes = "Obtain all products stored in database",
            response = StoreGetIdResponse.class,
            responseContainer = "list"
    )
    @GetMapping(value = "/products", produces = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<?> getAll() {
        Response response = new Response(HttpStatus.OK, "Success", storeService.getAllProducts());

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ApiOperation(
            value = "Get Product by id",
            nickname = "Get Product by id",
            notes = "Obtain a products stored in database",
            response = StoreGetIdResponse.class
    )
    @GetMapping(value = "/products/{productId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<?> getById(@PathVariable String productId) {
        Response response = new Response(HttpStatus.OK, "Success", storeService.getProductById(productId));

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ApiOperation(
            value = "Register a product",
            nickname = "Register a product",
            notes = "Allows to register a new product in store",
            response = StoreGetIdResponse.class
    )
    @PostMapping(value = "/products", produces = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<?> create(@RequestBody StorePostRequest request) {
        Response response = new Response(HttpStatus.CREATED, "Created", storeService.registerProduct(request));

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @ApiOperation(
            value = "Remove a product",
            nickname = "Remove a product",
            notes = "Allows to remove a product in store",
            response = StoreGetIdResponse.class
    )
    @DeleteMapping(value = "/products/{productId}", produces = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<?> delete(@PathVariable String productId) {
        Response response = new Response(HttpStatus.OK, "Success", storeService.removeProduct(productId));

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}
