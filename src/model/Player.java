//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package model;

public abstract class Player {
    public Player() {
    }

    public abstract boolean isHuman();

    public abstract void updateGame(Game var1);

    public String toString() {
        String var1 = this.getClass().getSimpleName();
        return var1 + "[isHuman=" + this.isHuman() + "]";
    }
}
