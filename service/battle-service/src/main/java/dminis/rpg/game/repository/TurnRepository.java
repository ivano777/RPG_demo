package dminis.rpg.game.repository;

import dminis.rpg.game.entity.battle.Turn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TurnRepository extends JpaRepository<Turn, Long> {
    Optional<Turn> findTopByBattleIdOrderByIndexDesc(Long battleId);
}
