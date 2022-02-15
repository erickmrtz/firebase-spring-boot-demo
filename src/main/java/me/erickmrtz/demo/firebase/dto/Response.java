package me.erickmrtz.demo.firebase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.erickmrtz.demo.firebase.util.Constant;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response implements Serializable {
    private static final long serialVersionUID = 8476108220780829222L;

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constant.FORMAT_DATE)
    private Date timestamp;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object payload;

    public Response(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }

    public Response(HttpStatus status, String message, Object payload) {
        this(status, message);
        this.payload = payload;
    }
}
