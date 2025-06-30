package dminis.rpg.game.entity.battle;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "turn")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Turn {

    @Id
    @GeneratedValue
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "battle_id")
    private Battle battle;

    private int index; //todo da riempire con una query apposita

    @Enumerated(EnumType.STRING)
    private Actor actor;          // HERO | ENEMY

    private int currentHeroHp;
    private int currentEnemyHp;

    @Embedded
    private Action action;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant creationTime;


     public enum Actor {
        ENEMY, HERO
    }
}