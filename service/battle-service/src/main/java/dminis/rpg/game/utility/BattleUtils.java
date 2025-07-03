package dminis.rpg.game.utility;


import dminis.rpg.game.entity.battle.*;
import dminis.rpg.game.entity.hero.Hero;
import org.springframework.cglib.core.internal.Function;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static dminis.rpg.game.entity.battle.Battle.BattleStatus.HERO_WIN;
import static dminis.rpg.game.utility.RNGProvider.nextDouble;

public class BattleUtils {

    private static final float V_RATEO = 1.5f;
    private static final float E_RATEO = 0.5f;

    private static final float RANGE = 0.3f; //30%


    public static CharacterSnapshot twist(CharacterSnapshot source){
        int heroLv = source.getLevel();
        int atk = randomLvInRange(RANGE,heroLv);
        int def = randomLvInRange(RANGE,heroLv);
        int lck = randomLvInRange(RANGE,heroLv);
        int lv = randomLvInRange(RANGE/2, Math.max(lck, Math.max(atk, def)), 1);

        var target = new CharacterSnapshot();
        target.setMaxHp(randomLvInRange(RANGE, source.getMaxHp()));
        target.getAtk().setLevel(atk);
        target.getDef().setLevel(def);
        target.getLck().setLevel(lck);
        target.setLevel(lv);

        return target;
    }

    /**
     * Genera un valore casuale nell'intervallo [1 - delta, 1 + delta] * lv.
     * Se il parametro direction è:
     *   > 0: solo incremento [1, 1 + delta] * lv
     *   < 0: solo decremento [1 - delta, 1] * lv
     *   = 0: intervallo completo [1 - delta, 1 + delta] * lv
     *
     * @param delta     ampiezza della variazione
     * @param lv        valore di base da scalare
     * @param direction direzione della variazione
     * @return valore intero casuale scalato
     */
    public static int randomLvInRange(double delta, int lv, int direction) {
        double factor;
        if (direction > 0) {
            factor = 1.0 + nextDouble() * delta;
        } else if (direction < 0) {
            factor = 1.0 - delta + nextDouble() * delta;
        } else {
            factor = 1.0 - delta + nextDouble() * (2 * delta);
        }
        return (int) Math.round(factor * lv);
    }

    public static int randomLvInRange(double delta, int lv) {
        return randomLvInRange(delta, lv, 0);
    }

    public static void handleRewards(Battle battle, Turn newTurn) {
        Set<Turn> sortedTurns = new TreeSet<>(Comparator.comparingInt(Turn::getIndex));
        sortedTurns.addAll(battle.getTurns());
        sortedTurns.add(newTurn);
        int atkExp = calculateActionExp(battle, sortedTurns, Action.ActionType.ATTACK);
        int defExp = Math.round(calculateActionExp(battle, sortedTurns, Action.ActionType.DEFENCE) * 1.5f);
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
        int enemyDifficulty = enemy.getAtk().getLevel() + enemy.getDef().getLevel() + enemy.getLck().getLevel();
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
        float baseLuck = 5f ; // scalabile
        float victoryBonus = isVictory(battle) ? V_RATEO : (V_RATEO + 0.5f);

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

        double relevantWeight = turns.stream()
                .map(Turn::getAction)
                .filter(actionFilter)
                .mapToInt(Action::getWeight)
                .mapToDouble(v -> v <= 0.0 ? 0.5 : (double) v)
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

    public static void gainHeroExp(Hero hero, Function<Hero, Integer> lvGetter, BiConsumer<Hero, Integer> lvSetter, Function<Hero, Integer> expGetter,
                                   BiConsumer<Hero, Integer> expSetter, int deltaXp, boolean lvUpHP) {

        // 1) early-exit
        if (hero.getStatus() == Hero.LifeStatus.DEAD) return;

        int lv  = lvGetter.apply(hero);
        if (lv >= 12) return;                   // già al cap

        // 2) aggiorno l’EXP grezza
        int exp = expGetter.apply(hero) + deltaXp;

    /* 3) livello massimo raggiungibile in questa chiamata:
          - HP: può salire fino a 12
          - altre stat: non può superare il livello dell’eroe              */
        int maxLv = lvUpHP ? 12 : hero.getLevel();

        // 4) loop di crescita
        while (lv < maxLv) {
            int xpNeeded = xpToNextLevel(lv);
            if (exp < xpNeeded) break;

            exp -= xpNeeded;
            lvSetter.accept(hero, ++lv);

            if (lvUpHP) lvUpHp(hero);
        }

    /* 5)se ho raggiunto il limite di crescita per questa
          chiamata, non lascio che l’EXP residua superi il “tetto”
          necessario a salire di livello la prossima volta           */
        if (lv == maxLv) {
            int xpCap = xpToNextLevel(lv) - 1;   // es. servono 100 → tengo max 99
            if (exp > xpCap) exp = xpCap;
        }

        // 6) se per qualunque ragione tocco il cap assoluto 12
        if (lv >= 12) {
            lv = 12;
            exp = 0;
        }

        // 7) salvo i valori finali (EXP una sola volta)
        lvSetter.accept(hero, lv);
        expSetter.accept(hero, exp);
    }

    private static void lvUpHp(Hero hero){
        hero.setMaxHp(hero.getMaxHp() + 5*hero.getLevel());
    }

    private static int xpToNextLevel(int currentLV) {
        return (int) Math.round(10 * Math.pow(currentLV, 2));
    }



}
