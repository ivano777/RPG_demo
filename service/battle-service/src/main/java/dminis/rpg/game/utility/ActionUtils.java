package dminis.rpg.game.utility;


import dminis.rpg.game.entity.battle.Action;
import dminis.rpg.game.entity.battle.Battle;
import dminis.rpg.game.entity.battle.CharacterSnapshot;
import dminis.rpg.game.entity.battle.Turn;

import java.util.Arrays;
import java.util.Optional;

import static dminis.rpg.game.entity.battle.Turn.Actor.ENEMY;
import static dminis.rpg.game.utility.DiceUtils.*;

public class ActionUtils {
    public static void calculateAttack(Turn newTurn, Battle battle) {
        int weight = 0;
        switch (newTurn.getActor()){
            case ENEMY -> {
                weight = Math.max(0, rollAtk(battle.getEnemySnapshot()) - rollReactDef(battle.getHeroSnapshot()));
                newTurn.setCurrentHeroHp(Math.max(0, newTurn.getCurrentHeroHp() - weight));
            }
            case HERO -> {
                weight = Math.max(0, rollAtk(battle.getHeroSnapshot()) - rollReactDef(battle.getEnemySnapshot()));
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

    private static void calculateEscape(Turn newTurn, Battle battle) {
        var weight = Math.max(0, rollLck(battle.getHeroSnapshot()) - rollLck(battle.getEnemySnapshot()));
        if(weight > 0){
            battle.setActive(false);
            battle.setStatus(Battle.BattleStatus.ESCAPED);
        }
        var action = new Action(Action.ActionType.ESCAPE, weight);
        newTurn.setAction(action);
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
        if(ENEMY.equals(newTurn.getActor())){
            actionTypeEnum = getRandomEnemyAction();
        }
        switch (actionTypeEnum){
            case SKIP -> ActionUtils.calculateSkip(newTurn);
            case ATTACK -> ActionUtils.calculateAttack(newTurn, battle);
            case DEFENCE -> ActionUtils.calculateSkip(newTurn);
            case ESCAPE -> ActionUtils.calculateEscape(newTurn, battle);
            default ->  ActionUtils.calculateSkip(newTurn);
        }
    }

    public static Action.ActionType getRandomEnemyAction() {
        var actions = Arrays.stream(Action.ActionType.values())
                .filter(a -> a != Action.ActionType.ESCAPE)
                .toList();
        return actions.get(RNG.nextInt(actions.size()));
    }
}

