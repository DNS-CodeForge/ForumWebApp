package project.ForumWebApp.config;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse {
    private String message;
    private int statusCode;
    private Map<String, Object> data;
    private LocalDateTime timestamp;

    public CustomResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = new HashMap<String, Object>();
        this.timestamp = LocalDateTime.now();
    }



}
