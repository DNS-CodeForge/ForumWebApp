package project.ForumWebApp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "phone_numbers")
@Schema(description = "Entity representing a phone number")
public class PhoneNumber {

    @Id
    @Column(name = "user_id")
    @Schema(description = "User ID, which is also the primary key", example = "1")
    private Integer userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @MapsId
    @Schema(description = "User associated with the phone number")
    private ApplicationUser user;

    @Column(name = "number")
    @Schema(description = "Phone number", example = "+1234567890", required = true)
    private String number;
}
