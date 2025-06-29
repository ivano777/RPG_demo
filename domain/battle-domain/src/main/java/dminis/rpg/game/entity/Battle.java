package dminis.rpg.game.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "battle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long heroId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "hero_name")),
            @AttributeOverride(name = "level", column = @Column(name = "hero_level")),
            @AttributeOverride(name = "maxHp", column = @Column(name = "hero_max_hp")),
            @AttributeOverride(name = "atk", column = @Column(name = "hero_atk")),
            @AttributeOverride(name = "def", column = @Column(name = "hero_def")),
            @AttributeOverride(name = "lck", column = @Column(name = "hero_lck"))
    })    private CharacterSnapshot heroSnapshot;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "enemy_name")),
            @AttributeOverride(name = "level", column = @Column(name = "enemy_level")),
            @AttributeOverride(name = "maxHp", column = @Column(name = "enemy_max_hp")),
            @AttributeOverride(name = "atk", column = @Column(name = "enemy_atk")),
            @AttributeOverride(name = "def", column = @Column(name = "enemy_def")),
            @AttributeOverride(name = "lck", column = @Column(name = "enemy_lck"))
    })    private CharacterSnapshot enemySnapshot;

    @Enumerated(EnumType.STRING)
    private BattleWinner status = BattleWinner.ONGOING;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant creationTime;

    @Column(nullable = false)
    @UpdateTimestamp
    private Instant lastUpdateTime;

    @Column(nullable = false)
    boolean active = true;

    @Version                      // << optimistic-lock
    private int version;

    @OneToMany(mappedBy = "battle", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("index ASC")
    private List<Turn> turns = new ArrayList<>();

     public enum BattleWinner {
        ONGOING, ENEMY, HERO
    }
}
