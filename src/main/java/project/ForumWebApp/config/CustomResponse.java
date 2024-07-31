package project.ForumWebApp.config;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Data
@Setter
@Getter
@AllArgsConstructor
public class CustomResponse {
    private Map<String, Object> data;
    private LocalDateTime timestamp;

    public CustomResponse() {
        this.data = new HashMap<String, Object>();
        this.timestamp = LocalDateTime.now();
    }



}
