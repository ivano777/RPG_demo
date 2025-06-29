package dminis.rpg.game.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "turn")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Turn {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "battle_id")
    private Battle battle;

    private int index; //todo da riempire con una query apposita

    @Enumerated(EnumType.STRING)
    private Actor actor;          // HERO | ENEMY

    private int heroHpAfter;
    private int enemyHpAfter;

    @Embedded
    private Action action;

    private int actionWeight;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Instant creationTime;


     public enum Actor {
        ENEMY, HERO
    }
}