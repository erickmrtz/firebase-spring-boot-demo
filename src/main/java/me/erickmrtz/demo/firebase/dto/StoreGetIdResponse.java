package me.erickmrtz.demo.firebase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreGetIdResponse implements Serializable {
    private static final long serialVersionUID = 662604227785642175L;

    private String documentId;
    private String name;
    private Integer quantity;
    private String imageUrl;
}
