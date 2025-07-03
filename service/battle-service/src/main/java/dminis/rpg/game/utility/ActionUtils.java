package dminis.rpg.game.utility;


import dminis.rpg.game.entity.battle.Action;
import dminis.rpg.game.entity.battle.Battle;
import dminis.rpg.game.entity.battle.Turn;

import java.util.Arrays;
import java.util.Optional;

import static dminis.rpg.game.entity.battle.Turn.Actor.ENEMY;
import static dminis.rpg.game.utility.DiceUtils.*;
import static dminis.rpg.game.utility.RNGProvider.nextInt;

public class ActionUtils {

    public static void calculateAttack(Turn turn, Battle battle) {

        // 1. Scegli attaccante e difensore in base all’attore del turno
        var attacker = turn.getActor() == Turn.Actor.ENEMY
                ? battle.getEnemySnapshot()
                : battle.getHeroSnapshot();

        var defender = turn.getActor() == Turn.Actor.ENEMY
                ? battle.getHeroSnapshot()
                : battle.getEnemySnapshot();

        // 2. Calcola modificatori critico e perfetto una sola volta
        int lckLevel = attacker.getLck().getLevel();
        float critMod = chance(lckLevel) ? 3f : 1f;
        float perfMod = chance(lckLevel) ? 0f : 1f;

        // 3. Danno finale
        int dmg = Math.round(rollAtk(attacker) * critMod);
        int block = Math.round(rollReactDef(defender) * perfMod);
        //3.1 La difesa passiva scende del 33% ogni volta che viene usata in modo da evitare stalli
        int staticDef = defender.getDef().getFlat();
        if(staticDef == 1){
            if(chance(100 - 33)) staticDef = 0;   //todo mettere questo (e gli altri parametri configurabili)
        }else {
            staticDef = Math.round( staticDef * (1 - 0.33f));
        }
        defender.getDef().setFlat(staticDef);
        int weight = Math.max(0, dmg - block);

        // 4. Aggiorna gli HP corretti
        if (turn.getActor() == Turn.Actor.ENEMY) {
            turn.setCurrentHeroHp(Math.max(0, turn.getCurrentHeroHp() - weight));
        } else {
            turn.setCurrentEnemyHp(Math.max(0, turn.getCurrentEnemyHp() - weight));
        }

        // 5. Controllo vittoria
        if (turn.getCurrentHeroHp() <= 0 || turn.getCurrentEnemyHp() <= 0) {
            battle.setActive(false);
            battle.setStatus(turn.getCurrentHeroHp() <= 0
                    ? Battle.BattleStatus.ENEMY_WIN
                    : Battle.BattleStatus.HERO_WIN);
        }

        // 6. Registra l’azione
        turn.setAction(new Action(Action.ActionType.ATTACK, weight));
    }

    private static void calculateDefence(Turn turn, Battle battle) {
        // 1. Scegli lo snapshot corretto in base all’attore
        var defender = (turn.getActor() == Turn.Actor.ENEMY)
                ? battle.getEnemySnapshot()
                : battle.getHeroSnapshot();


        // 2. Calcola il bonus di difesa
        int bonus = Math.max(0, rollDef(defender));

        // 3. Aggiorna la difesa “flat” dello snapshot, deve essere minore del danno massimo dell'avversario
        //    (meglio sarebbe avere un metodo addFlat(bonus) dentro la classe Defence)
        var def = defender.getDef();
        int newFlat = def.getFlat() + bonus;
        int weight = newFlat - def.getFlat();
        def.setFlat(newFlat);

        // 4. Registra l’azione nel turno
        turn.setAction(new Action(Action.ActionType.DEFENCE, weight));
    }

    private static void calculateEscape(Turn newTurn, Battle battle) {
        var weight = Math.max(0, rollLck(battle.getHeroSnapshot()) - rollLck(battle.getEnemySnapshot()));
        var lvChance = chance(battle.getHeroSnapshot().getLck().getLevel());
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
        return actions.get(nextInt(actions.size()));
    }

    private static boolean chance(int chance){
        return nextInt(100) < Math.min(99, chance);
    }
}

