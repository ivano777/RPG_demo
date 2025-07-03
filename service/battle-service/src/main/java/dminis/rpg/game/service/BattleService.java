package dminis.rpg.game.service;

import dminis.rpg.game.dto.BattleDTO;
import dminis.rpg.game.dto.RewardDTO;
import dminis.rpg.game.dto.TurnDTO;
import dminis.rpg.game.enemy.repository.EnemyRepository;
import dminis.rpg.game.entity.battle.Action;
import dminis.rpg.game.entity.battle.Battle;
import dminis.rpg.game.entity.battle.CharacterSnapshot;
import dminis.rpg.game.entity.battle.Turn;
import dminis.rpg.game.entity.enemy.Enemy;
import dminis.rpg.game.entity.hero.Hero;
import dminis.rpg.game.hero.repository.HeroRepository;
import dminis.rpg.game.mapper.BattleMapper;
import dminis.rpg.game.repository.BattleRepository;
import dminis.rpg.game.repository.TurnRepository;
import dminis.rpg.game.utility.ActionUtils;
import dminis.rpg.game.utility.BattleUtils;
import dminis.rpg.game.utility.DiceUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static dminis.rpg.game.entity.battle.Battle.BattleStatus.*;
import static dminis.rpg.game.utility.BattleUtils.twist;

@Service
@RequiredArgsConstructor
public class BattleService {
    private final EnemyRepository enemyRepository;
    private final HeroRepository heroRepository;
    private final BattleRepository battleRepository;
    private final TurnRepository turnRepository;
    private final BattleMapper mapper;
    private final RewardApplier rewardApplier;

    public BattleDTO resumeStartBattle(Long heroId){
        var resume = battleRepository.findFirstByActiveTrueAndHeroId(heroId);
        if(resume.isPresent()){
            return mapper.toDTO(resume.get());
        }
        var hero = heroRepository.findById(heroId).orElseThrow();
        if(Hero.LifeStatus.DEAD.equals(hero.getStatus())){
            throw new IllegalStateException("Questo eroe è morto!");
        }
        var heroSnap = mapper.toSnap(hero);
        var enemySnap = calculateEnemySnap(heroSnap);
        var enemyName = enemyRepository.findRandom()
                .map(Enemy::getName)
                .orElse("EnemyDummy");

        enemySnap.setName(enemyName);

        var battleToSave = new Battle();
        battleToSave.setHero(hero);
        battleToSave.setHeroSnapshot(heroSnap);
        battleToSave.setEnemySnapshot(enemySnap);
        battleToSave.setStartingPlayer(calculateInitiative(heroSnap,enemySnap));
        var savedBattle = battleRepository.save(battleToSave);
        return mapper.toDTO(savedBattle);
    }

    @Transactional
    public RewardDTO applyReward(long battleId) {
        var battle = battleRepository.findByIdAndActiveFalseAndExpPack_TakenFalse(battleId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Nessuna battaglia trovata con id %d e ricompense non riscattate.", battleId)));
        return rewardApplier.applyReward(battle);
    }

    @Transactional
    public void applyAllRewards(){
        battleRepository.findByActiveFalseAndExpPack_TakenFalse()
                .forEach(rewardApplier::applyReward);
    }

    @Transactional
    public TurnDTO playTurn(long battleId, String actionType, String actor) {
        var battle = battleRepository.findById(battleId).orElseThrow();
        var lastTurn = turnRepository.findTopByBattleIdOrderByIndexDesc(battleId);
        if(TO_START.equals(battle.getStatus())){
            battle.setStatus(ONGOING);
        }
        Turn.Actor actorEnum = Turn.Actor.valueOf(actor);
        Action.ActionType actionTypeEnum = Action.ActionType.valueOf(actionType);

        validate(battle, lastTurn, actorEnum, actionTypeEnum);

        var idx = lastTurn.map(Turn::getIndex).orElse(0) +1;

        var newTurn = Turn.builder()
                .battle(battle)
                .index(idx)
                .actor(actorEnum)
                .build();
        ActionUtils.computeAction(newTurn, battle, lastTurn, actionTypeEnum);

        if(newTurn.getCurrentHeroHp() <= 0 ){
            var hero = battle.getHero();
            hero.setStatus(Hero.LifeStatus.DEAD);
            heroRepository.save(hero);
        }
        var res = mapper.toDTO(turnRepository.save(newTurn));
        if(!battle.isActive()){
            var hero = battle.getHero();
            if(Hero.LifeStatus.ALIVE.equals(hero.getStatus())) {
                BattleUtils.handleRewards(battle, newTurn);
            }
        }
        battleRepository.save(battle);
        return res;
    }

    private static void validate(Battle battle, Optional<Turn> lastTurn, Turn.Actor actor, Action.ActionType action) {
        if(!battle.isActive()){
            throw new IllegalStateException("Battaglia conclusa!");
        }
        if(ENEMY_WIN.equals(battle.getStatus()) || HERO_WIN.equals(battle.getStatus())){
            throw new IllegalStateException("Battaglia conclusa! ->"+battle.getStatus());
        }
        if(Turn.Actor.ENEMY.equals(actor) && Action.ActionType.ESCAPE.equals(action)){
            throw new IllegalStateException(Turn.Actor.ENEMY.name() + "cannot use " + Action.ActionType.ESCAPE.name());

        }
        if(lastTurn.isEmpty()){
            if(!battle.getStartingPlayer().equals(actor))
                throw new IllegalArgumentException("It's not "+actor+" turn!");
        }else {
            if(lastTurn.get().getActor().equals(actor)){
                throw new DataIntegrityViolationException(actor + " cannot play two turns in a row!");
            }
            if(lastTurn.get().getCurrentEnemyHp() < 0 || lastTurn.get().getCurrentHeroHp() < 0){
                throw new IllegalStateException("HP negativi rilevati: stato del turno non valido");
            }
        }
    }

    private Turn.Actor calculateInitiative(CharacterSnapshot hero, CharacterSnapshot enemy){
        var heroInitiative = DiceUtils.rollLck(hero);
        var enemyInitiative = DiceUtils.rollLck(enemy);
        return heroInitiative >= enemyInitiative ? Turn.Actor.HERO : Turn.Actor.ENEMY;

    }

    private CharacterSnapshot calculateEnemySnap(CharacterSnapshot heroSnap) {
        var enemy = heroSnap.toBuilder().build();
        if(enemy.getLevel() <= 1){
            return enemy;
        }
        return twist(enemy);
    }

    public List<BattleDTO> getAllBattles() {
        return mapper.toDTOList(battleRepository.findAll());
    }
}
