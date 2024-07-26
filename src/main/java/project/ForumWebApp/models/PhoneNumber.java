package project.ForumWebApp.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID of the phone number", example = "1")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "User associated with the phone number")
    private ApplicationUser user;

    @Column(name = "number")
    @Schema(description = "Phone number", example = "+1234567890", required = true)
    private String number;
}
