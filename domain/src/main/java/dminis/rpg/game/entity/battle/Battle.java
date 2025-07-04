package dminis.rpg.game.entity.battle;


import dminis.rpg.game.entity.hero.Hero;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "hero_name")),
            @AttributeOverride(name = "level", column = @Column(name = "hero_level")),
            @AttributeOverride(name = "maxHp", column = @Column(name = "hero_max_hp")),
            @AttributeOverride(name = "atk.level", column = @Column(name = "hero_atk_level")),
            @AttributeOverride(name = "atk.flat", column = @Column(name = "hero_atk_flat")),
            @AttributeOverride(name = "def.level", column = @Column(name = "hero_def_level")),
            @AttributeOverride(name = "def.flat", column = @Column(name = "hero_def_flat")),
            @AttributeOverride(name = "lck.level", column = @Column(name = "hero_lck_level")),
            @AttributeOverride(name = "lck.flat", column = @Column(name = "hero_lck_flat"))
    })
    private CharacterSnapshot heroSnapshot;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "enemy_name")),
            @AttributeOverride(name = "level", column = @Column(name = "enemy_level")),
            @AttributeOverride(name = "maxHp", column = @Column(name = "enemy_max_hp")),
            @AttributeOverride(name = "atk.level", column = @Column(name = "enemy_atk_level")),
            @AttributeOverride(name = "atk.flat", column = @Column(name = "enemy_atk_flat")),
            @AttributeOverride(name = "def.level", column = @Column(name = "enemy_def_level")),
            @AttributeOverride(name = "def.flat", column = @Column(name = "enemy_def_flat")),
            @AttributeOverride(name = "lck.level", column = @Column(name = "enemy_lck_level")),
            @AttributeOverride(name = "lck.flat", column = @Column(name = "enemy_lck_flat"))
    })
    private CharacterSnapshot enemySnapshot;

    @Enumerated(EnumType.STRING)
    private BattleStatus status = BattleStatus.TO_START;

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

    @Column(nullable = false)
    private Turn.Actor startingPlayer;

    @ToString.Exclude
    @OneToMany(mappedBy = "battle", cascade = CascadeType.ALL, orphanRemoval = true, fetch =  FetchType.LAZY)
    @OrderBy("index ASC")
    private List<Turn> turns = new ArrayList<>();

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "hero_id", nullable = false)
    private Hero hero;

    @Embedded
    private ExpPack expPack;

     public enum BattleStatus {
        ONGOING, ENEMY_WIN, HERO_WIN, TO_START, ESCAPED
    }
}
