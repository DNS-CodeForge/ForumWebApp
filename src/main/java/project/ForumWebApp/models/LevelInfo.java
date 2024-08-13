package project.ForumWebApp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "level_info")
public class LevelInfo {
    @Id
    private int userId;

    private int currentLevel = 1;
    private int currentExp = 0;
    private int expToNextLevel = 50;

    @OneToOne
    @JoinColumn(name = "user_id")
    private ApplicationUser user;
}
