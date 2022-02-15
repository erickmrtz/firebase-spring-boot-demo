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
public class StorePostResponse implements Serializable {
    private static final long serialVersionUID = -6840449512998475548L;

    private String documentId;
    private String name;
    private Integer quantity;
}
