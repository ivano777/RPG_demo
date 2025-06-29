package dminis.rpg.game.service;

import dminis.rpg.game.dto.BattleDTO;
import dminis.rpg.game.enemy.repository.EnemyRepository;
import dminis.rpg.game.hero.repository.HeroRepository;
import dminis.rpg.game.mapper.BattleMapper;
import dminis.rpg.game.repository.BattleRepository;
import dminis.rpg.game.entity.Battle;
import dminis.rpg.game.entity.CharacterSnapshot;
import dminis.rpg.game.entity.Turn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static dminis.rpg.game.utility.BattleUtility.twist;

@Service
@RequiredArgsConstructor
public class BattleService {
    private final EnemyRepository enemyRepository;
    private final HeroRepository heroRepository;
    private final BattleRepository battleRepository;
    private final BattleMapper mapper;

    public BattleDTO resumeStartBattle(Long heroId){
        var resume = battleRepository.findFirstByActiveTrueAndHeroId(heroId);
        if(resume.isPresent()){
            return mapper.toDTO(resume.get());
        }
        var heroSnap = heroRepository.findById(heroId)
                .map(mapper::toSnap)
                .orElseThrow();
        var enemySnap = calculateEnemySnap(heroSnap);
        var enemyName = enemyRepository.findRandom()
                .orElseThrow()
                .getName();
        enemySnap.setName(enemyName);

        var battleToSave = new Battle();
        battleToSave.setHeroId(heroId);
        battleToSave.setHeroSnapshot(heroSnap);
        battleToSave.setEnemySnapshot(enemySnap);
        var savedBattle = battleRepository.save(battleToSave);
        return mapper.toDTO(savedBattle);
    }

    private CharacterSnapshot calculateEnemySnap(CharacterSnapshot heroSnap) {
        if(heroSnap.getLevel() <= 1){
            return heroSnap;
        }
        return twist(heroSnap);
    }

    private int findMaxTurnIndex(Long battleId) {
        return battleRepository.findById(battleId)
                .orElseThrow()
                .getTurns()
                .stream()
                .mapToInt(Turn::getIndex)
                .max()
                .orElse(-1);
    }


}
