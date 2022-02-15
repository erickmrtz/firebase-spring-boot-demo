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
public class StorePostRequest implements Serializable {
    private static final long serialVersionUID = 2332296483487973583L;

    private String name;
    private Integer quantity;
}
