package dminis.rpg.game.hero.entity;

public interface Progressable {

    int getLevel();
    void setLevel(int level);

    int getExp();
    void setExp(int exp);

//    default int xpToNextLevel() {
//        return 10 + getLevel() * getLevel();
//    }
//
//    /** ritorna true se sale di livello */
//    default boolean gainExp(int deltaXp, int maxLevel) {
//        if (getLevel() >= maxLevel) return false;
//
//        setExp(getExp() + deltaXp);
//        boolean leveled = false;
//
//        while (getLevel() < maxLevel && getExp() >= xpToNextLevel()) {
//            setExp(getExp() - xpToNextLevel());
//            setLevel(getLevel() + 1);
//            leveled = true;
//        }
//        if (getLevel() >= maxLevel) setExp(0);
//        return leveled;
//    } todo delete
}
