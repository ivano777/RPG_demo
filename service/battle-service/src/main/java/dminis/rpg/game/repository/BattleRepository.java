package dminis.rpg.game.repository;

import dminis.rpg.game.entity.battle.Battle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BattleRepository extends JpaRepository<Battle, Long> {
    Optional<Battle> findFirstByActiveTrueAndHeroId(Long aLong);
}
