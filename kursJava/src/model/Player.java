package model;

public abstract class Player {
	public Player() {
	}

	public abstract boolean isHuman();

	public abstract void updateGame(Game var1);

	public String toString() {
		String var10000 = this.getClass().getSimpleName();
		return var10000 + "[isHuman=" + this.isHuman() + "]";
	}
}
