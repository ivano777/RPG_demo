package dminis.rpg.game.hero.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "heroes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hero implements Progressable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Min(1) @Max(12)
    private int level = 1;

    @Min(0)
    private int exp = 0;

    @NotBlank
    @Size(max = 40)
    @Column(nullable = false, unique = true)
    private String name;

    @Min(0)
    private int maxHp = 100;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LifeStatus status = LifeStatus.ALIVE;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="level", column=@Column(name="atk_lvl")),
            @AttributeOverride(name="exp",   column=@Column(name="atk_exp"))
    })
    private StatProgress atk = new StatProgress();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="level", column=@Column(name="def_lvl")),
            @AttributeOverride(name="exp",   column=@Column(name="def_exp"))
    })
    private StatProgress def = new StatProgress();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="level", column=@Column(name="lck_lvl")),
            @AttributeOverride(name="exp",   column=@Column(name="lck_exp"))
    })
    private StatProgress lck = new StatProgress();

    @Min(0)
    private int gold;

    public enum LifeStatus {
        ALIVE,
        DEAD
    }
// todo spostare in service
//    public boolean gainHeroExp(int deltaXp) {
//        if (status == LifeStatus.DEAD || level >= 12) return false;
//
//        exp += deltaXp;
//        boolean leveled = false;
//
//        // calcola quanta xp serve a ogni step e livella finché può
//        while (level < 12 && exp >= xpToNextLevel()) {
//            exp -= xpToNextLevel();
//            leveled = true;
//        }
//        // se raggiungo il cap 12, azzero xp residua
//        if (level >= 12) {
//            level = 12;
//            exp = 0;
//        }
//        return leveled;
//    }
//
//    private void lvUp(){
//        hp+=10*level;
//        level++;
//    }
//
//    private int xpToNextLevel() {
//        return 10 + level * level;
//    }
//
//    public void applyDamage(int dmg) {
//        if (status == LifeStatus.DEAD) return;   //todo forse aggiungere exception
//
//        hp = Math.max(hp - dmg, 0);
//        if (hp == 0) {
//            status = LifeStatus.DEAD;
//        }
//    }

}
