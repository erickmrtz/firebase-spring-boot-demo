package me.erickmrtz.demo.firebase.collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    private String documentId;
    private String name;
    private Integer quantity;
    private String imageUrl;
    private Date creationDate;
    private Date modificationDate;
}
