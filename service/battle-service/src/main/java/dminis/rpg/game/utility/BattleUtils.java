package dminis.rpg.game.utility;


import dminis.rpg.game.entity.battle.*;
import dminis.rpg.game.entity.hero.Hero;
import org.springframework.cglib.core.internal.Function;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static dminis.rpg.game.entity.battle.Battle.BattleStatus.HERO_WIN;

public class BattleUtils {

    private static final float V_RATEO = 1.5f;
    private static final float E_RATEO = 0.5f;


    public static CharacterSnapshot twist(CharacterSnapshot source){
        var target = new CharacterSnapshot();
        target.setLevel(randomInRange(0.3)*source.getLevel());
        target.setMaxHp(randomInRange(0.3)*source.getMaxHp());
        target.setAtk(randomInRange(0.5)*source.getAtk());
        target.setDef(randomInRange(0.5)*source.getDef());
        target.setLck(randomInRange(0.5)*source.getLck());
        return target;
    }

    /**
     *
     * @param delta
     * @return dato un input x (es. 0.3), generi un numero casuale nell’intervallo [1 - x, 1 + x].
     */
    public static int randomInRange(double delta) {
        return (int) Math.round(1.0 - delta + Math.random() * (2 * delta));
    }

    public static void handleRewards(Battle battle, Turn newTurn) {
        Set<Turn> sortedTurns = new TreeSet<>(Comparator.comparingInt(Turn::getIndex));
        sortedTurns.addAll(battle.getTurns());
        sortedTurns.add(newTurn);
        int atkExp = calculateActionExp(battle, sortedTurns, Action.ActionType.ATTACK);
        int defExp = calculateActionExp(battle, sortedTurns, Action.ActionType.DEFENCE);
        int lckExp = calculateLuckExp(battle, newTurn);
        int lvExp = calculateLevelExp(atkExp, defExp, lckExp, isVictory(battle));
        var expPack = new ExpPack(lvExp, lckExp, atkExp, defExp, false);
        battle.setExpPack(expPack);
    }


    private static int calculateActionExp(Battle battle, Set<Turn> sortedTurns, Action.ActionType type) {
        Set<Turn> heroTurns = sortedTurns.stream()
                .filter(t -> Turn.Actor.HERO.equals(t.getActor()))
                .collect(Collectors.toSet());

        CharacterSnapshot hero = battle.getHeroSnapshot();
        CharacterSnapshot enemy = battle.getEnemySnapshot();

        int heroLevel = hero.getLevel();
        int enemyDifficulty = enemy.getAtk() + enemy.getDef() + enemy.getLck();
        boolean victory = isVictory(battle);

        float exp = computeBaseExp(
                heroTurns,
                a -> a.getType().equals(type),
                heroLevel,
                enemyDifficulty,
                victory
        );

        return Math.round(exp);
    }

    private static int calculateLuckExp(Battle battle, Turn lastTurn) {
        CharacterSnapshot hero = battle.getHeroSnapshot();

        int maxHp = hero.getMaxHp();
        int remainingHp = Math.max(0, lastTurn.getCurrentHeroHp()); // o snapshot, se preferisci
        float hpRatio = (float) remainingHp / maxHp; // da 0.0 (morto) a 1.0 (full HP)

        float luckFactor = 1 - hpRatio; // più sei basso, più guadagni
        float baseLuck = 10f; // scalabile
        float victoryBonus = isVictory(battle) ? V_RATEO : E_RATEO;

        float exp = baseLuck * luckFactor * victoryBonus;
        return Math.round(exp);
    }

    private static int calculateLevelExp(int attackExp, int defenseExp, int luckExp, boolean victory) {
        int total = (attackExp + defenseExp + luckExp)/3;

        float victoryBonus = victory ? V_RATEO : E_RATEO; // 25% in più se ha vinto
        float scaled = total * victoryBonus;

        return Math.round(scaled);
    }


    private static boolean isVictory(Battle battle) {
        return HERO_WIN.equals(battle.getStatus());
    }

    private static float computeBaseExp(
            Collection<Turn> turns,
            Predicate<Action> actionFilter,
            int heroLevel,
            int enemyDifficulty,
            boolean victory
    ) {
        int totalActions = turns.size();
        if (totalActions == 0) return 0f;

        int relevantWeight = turns.stream()
                .map(Turn::getAction)
                .filter(actionFilter)
                .mapToInt(Action::getWeight)
                .sum();

        float baseExp = (float) relevantWeight / totalActions * enemyDifficulty * heroLevel;

        float levelFactor = 1 + (heroLevel * 0.05f);       // +5% per livello
        float difficultyFactor = 1 + (enemyDifficulty / 100f); // es. 120 => 2.2
        float victoryBonus = victory ? V_RATEO : E_RATEO;

        return baseExp * levelFactor * difficultyFactor * victoryBonus;
    }


    public static void handleExpPack(ExpPack expPack, Hero hero) {
        gainHeroExp(hero, Hero::getLevel, Hero::setLevel, Hero::getExp, Hero::setExp, expPack.getLvExp(), true);
        gainHeroExp(hero, h -> h.getAtk().getLevel(), (h,v) -> h.getAtk().setLevel(v),
                h -> h.getAtk().getExp(), (h,v) -> h.getAtk().setExp(v), expPack.getAtkExp(), false);
        gainHeroExp(hero, h -> h.getDef().getLevel(), (h,v) -> h.getDef().setLevel(v),
                h -> h.getDef().getExp(), (h,v) -> h.getDef().setExp(v), expPack.getDefExp(), false);
        gainHeroExp(hero, h -> h.getLck().getLevel(), (h,v) -> h.getLck().setLevel(v),
                h -> h.getLck().getExp(), (h,v) -> h.getLck().setExp(v), expPack.getLckExp(), false);
        expPack.setTaken(true);
    }

    public static boolean gainHeroExp(Hero hero, Function<Hero, Integer> lvGetter, BiConsumer<Hero, Integer> lvSetter, Function<Hero, Integer> expGetter,
                                      BiConsumer<Hero, Integer> expSetter,  int deltaXp, boolean lvUpHP) {
        var status = hero.getStatus();
        var exp = expGetter.apply(hero) + deltaXp;
        var lv = lvGetter.apply(hero);

        if (Hero.LifeStatus.DEAD.equals(status) || lv >= 12) return false;

        boolean leveled = false;

        // calcola quanta xp serve a ogni step e livella finché può
        while (lv < 12 && exp >= xpToNextLevel(lv)) {
            exp -= xpToNextLevel(lv);
            lvSetter.accept(hero,++lv);
            expSetter.accept(hero, exp);
            if(lvUpHP) lvUpHp(hero);
            leveled = true;
        }
        // se raggiungo il cap 12, azzero xp residua
        if (lv >= 12) {
            exp=0;
            lvSetter.accept(hero,12);
            expSetter.accept(hero, 0);
        }
        return leveled;
    }

    private static void lvUpHp(Hero hero){
        hero.setMaxHp(hero.getMaxHp() + 10*hero.getLevel());
    }

    private static int xpToNextLevel(int currentLV) {
        return 10 + currentLV * currentLV;
    }



}
