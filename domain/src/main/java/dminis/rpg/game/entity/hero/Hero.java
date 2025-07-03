package dminis.rpg.game.entity.hero;

import dminis.rpg.game.entity.battle.Battle;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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
    private int maxHp = 5;

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

    @OneToMany(mappedBy = "hero", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Battle> battles = new ArrayList<>();

    public enum LifeStatus {
        ALIVE,
        DEAD
    }

}
