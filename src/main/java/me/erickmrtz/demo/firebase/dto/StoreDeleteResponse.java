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
public class StoreDeleteResponse implements Serializable {
    private static final long serialVersionUID = -2631646496286526930L;

    private String documentId;
}
