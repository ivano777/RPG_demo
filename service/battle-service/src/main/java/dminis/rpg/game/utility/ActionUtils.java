package dminis.rpg.game.utility;


import dminis.rpg.game.entity.battle.Action;
import dminis.rpg.game.entity.battle.Battle;
import dminis.rpg.game.entity.battle.Turn;

import java.util.Arrays;
import java.util.Optional;

import static dminis.rpg.game.entity.battle.Turn.Actor.ENEMY;
import static dminis.rpg.game.utility.DiceUtils.*;

public class ActionUtils {
    public static void calculateAttack(Turn newTurn, Battle battle) {
        int weight = 0;
        float critMod = 1f;
        float perfMod = 1f;
        switch (newTurn.getActor()){
            case ENEMY -> {
                int lv = battle.getEnemySnapshot().getLevel();
                if(chance(lv)) critMod = 3;
                if(chance(lv)) perfMod = 0;
                int dmg = Math.round(rollAtk(battle.getEnemySnapshot()) * critMod);
                int def = Math.round(rollReactDef(battle.getHeroSnapshot()) * perfMod);
                weight = Math.max(0, dmg - def);
                newTurn.setCurrentHeroHp(Math.max(0, newTurn.getCurrentHeroHp() - weight));
            }
            case HERO -> {
                int lv = battle.getHeroSnapshot().getLevel();
                if(chance(lv)) critMod = 3;
                if(chance(lv)) perfMod = 0;
                int dmg = Math.round(rollAtk(battle.getHeroSnapshot()) * critMod);
                int def = Math.round(rollReactDef(battle.getEnemySnapshot()) * perfMod);
                weight = Math.max(0, dmg - def);
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

    private static void calculateDefence(Turn newTurn, Battle battle) {
        int weight = 0;
        switch (newTurn.getActor()){
            case ENEMY -> {
                weight = Math.max(0, rollDef(battle.getEnemySnapshot()));
                var flatDef = battle.getEnemySnapshot().getDef().getFlat() + weight;
                battle.getEnemySnapshot().getDef().setFlat(flatDef);
            }
            case HERO -> {
                weight = Math.max(0, rollDef(battle.getHeroSnapshot()));
                var flatDef = battle.getHeroSnapshot().getDef().getFlat() + weight;
                battle.getHeroSnapshot().getDef().setFlat(flatDef);
            }
        }
        var action = new Action(Action.ActionType.DEFENCE, weight);
        newTurn.setAction(action);
    }

    private static void calculateEscape(Turn newTurn, Battle battle) {
        var weight = Math.max(0, rollLck(battle.getHeroSnapshot()) - rollLck(battle.getEnemySnapshot()));
        var lvChance = chance(battle.getHeroSnapshot().getLevel());
        if(weight > 0 || lvChance){
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
            case DEFENCE -> ActionUtils.calculateDefence(newTurn, battle);
            case ESCAPE -> ActionUtils.calculateEscape(newTurn, battle);
            default ->  ActionUtils.calculateSkip(newTurn);
        }
    }

    private static Action.ActionType getRandomEnemyAction() {
        var actions = Arrays.stream(Action.ActionType.values())
                .filter(a -> a != Action.ActionType.ESCAPE)
                .toList();
        return actions.get(RNG.nextInt(actions.size()));
    }

    private static boolean chance(int chance){
        return RNG.nextInt(100) < Math.min(99, chance);
    }
}

