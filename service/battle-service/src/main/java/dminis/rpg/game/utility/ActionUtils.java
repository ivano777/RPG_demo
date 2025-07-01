package dminis.rpg.game.utility;


import dminis.rpg.game.entity.battle.Action;
import dminis.rpg.game.entity.battle.Battle;
import dminis.rpg.game.entity.battle.CharacterSnapshot;
import dminis.rpg.game.entity.battle.Turn;

import java.util.Optional;

import static dminis.rpg.game.utility.DiceUtils.*;

public class ActionUtils {
    public static void calculateAttack(Turn newTurn, Battle battle) {
        int weight = 0;
        switch (newTurn.getActor()){
            case ENEMY -> {
                weight = calculateActionWeight(battle.getEnemySnapshot(), battle.getHeroSnapshot());
                newTurn.setCurrentHeroHp(Math.max(0, newTurn.getCurrentHeroHp() - weight));
            }
            case HERO -> {
                weight = calculateActionWeight(battle.getHeroSnapshot(), battle.getEnemySnapshot());
                newTurn.setCurrentEnemyHp(Math.max(0, newTurn.getCurrentEnemyHp() - weight));
            }
        }
        if(newTurn.getCurrentHeroHp() <= 0){
            battle.setActive(false);
            battle.setStatus(Battle.BattleStatus.ENEMY_WIN);
        }
        if(newTurn.getCurrentEnemyHp() <= 0){
            battle.setActive(false);
            battle.setStatus(Battle.BattleStatus.HERO_WIN);
        }

        var action = new Action(Action.ActionType.ATTACK, weight);
        newTurn.setAction(action);
    }

    public static int calculateActionWeight(CharacterSnapshot attacker, CharacterSnapshot defender){
        return Math.max(0, rollAtk(attacker) - rollReactDef(defender));
    }

    public static void calculateSkip(Turn newTurn) {
        newTurn.setAction(new Action(Action.ActionType.SKIP,0));
    }

    public static void computeAction(Turn newTurn, Battle battle, Optional<Turn> lastTurn, Action.ActionType actionTypeEnum) {
        int currentHeroHp = battle.getHeroSnapshot().getMaxHp();
        int currentEnemyHp = battle.getEnemySnapshot().getMaxHp();

        if(lastTurn.isPresent()){
            currentEnemyHp = lastTurn.get().getCurrentEnemyHp();
            currentHeroHp = lastTurn.get().getCurrentHeroHp();
        }
        newTurn.setCurrentHeroHp(currentHeroHp);
        newTurn.setCurrentEnemyHp(currentEnemyHp);
        if(Turn.Actor.ENEMY.equals(newTurn.getActor())){
            actionTypeEnum = getRandomAction();
        }
        switch (actionTypeEnum){
            case SKIP -> ActionUtils.calculateSkip(newTurn);
            case ATTACK -> ActionUtils.calculateAttack(newTurn, battle);
            default ->  ActionUtils.calculateAttack(newTurn, battle);
        }
    }

    public static Action.ActionType getRandomAction() {
        Action.ActionType[] values = Action.ActionType.values();
        return values[RNG.nextInt(values.length)];
    }

}

