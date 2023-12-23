package model;

import java.awt.Point;

public class Move {
	public static final double WEIGHT_INVALID = Double.NEGATIVE_INFINITY;
	private byte startIndex;
	private byte endIndex;
	private double weight;

	public Move(int var1, int var2) {
		this.setStartIndex(var1);
		this.setEndIndex(var2);
	}

	public Move(Point var1, Point var2) {
		this.setStartIndex(Board.toIndex(var1));
		this.setEndIndex(Board.toIndex(var2));
	}

	public int getStartIndex() {
		return this.startIndex;
	}

	public void setStartIndex(int var1) {
		this.startIndex = (byte)var1;
	}

	public int getEndIndex() {
		return this.endIndex;
	}

	public void setEndIndex(int var1) {
		this.endIndex = (byte)var1;
	}

	public Point getStart() {
		return Board.toPoint(this.startIndex);
	}

	public void setStart(Point var1) {
		this.setStartIndex(Board.toIndex(var1));
	}

	public Point getEnd() {
		return Board.toPoint(this.endIndex);
	}

	public void setEnd(Point var1) {
		this.setEndIndex(Board.toIndex(var1));
	}

	public double getWeight() {
		return this.weight;
	}

	public void setWeight(double var1) {
		this.weight = var1;
	}

	public void changeWeight(double var1) {
		this.weight += var1;
	}

	public String toString() {
		String var10000 = this.getClass().getSimpleName();
		return var10000 + "[startIndex=" + this.startIndex + ", endIndex=" + this.endIndex + ", weight=" + this.weight + "]";
	}
}
