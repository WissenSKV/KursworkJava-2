package model;

import logic.MoveGenerator;
import logic.MoveLogic;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;

public class Game {
	private Board board;
	private boolean isP1Turn;
	private int skipIndex;

	public Game() {
		this.restart();
	}

	public Game(String var1) {
		this.setGameState(var1);
	}

	public Game(Board var1, boolean var2, int var3) {
		this.board = var1 == null ? new Board() : var1;
		this.isP1Turn = var2;
		this.skipIndex = var3;
	}

	public Game copy() {
		Game var1 = new Game();
		var1.board = this.board.copy();
		var1.isP1Turn = this.isP1Turn;
		var1.skipIndex = this.skipIndex;
		return var1;
	}

	public void restart() {
		this.board = new Board();
		this.isP1Turn = true;
		this.skipIndex = -1;
	}

	public boolean move(Point var1, Point var2) {
		return var1 != null && var2 != null ? this.move(Board.toIndex(var1), Board.toIndex(var2)) : false;
	}

	public boolean move(int var1, int var2) {
		if (!MoveLogic.isValidMove(this, var1, var2)) {
			return false;
		} else {
			Point var3 = Board.middle(var1, var2);
			int var4 = Board.toIndex(var3);
			this.board.set(var2, this.board.get(var1));
			this.board.set(var4, 0);
			this.board.set(var1, 0);
			Point var5 = Board.toPoint(var2);
			int var6 = this.board.get(var2);
			boolean var7 = false;
			if (var5.y == 0 && var6 == 4) {
				this.board.set(var2, 5);
				var7 = true;
			} else if (var5.y == 7 && var6 == 6) {
				this.board.set(var2, 7);
				var7 = true;
			}

			boolean var8 = Board.isValidIndex(var4);
			if (var8) {
				this.skipIndex = var2;
			}

			if (!var8 || MoveGenerator.getSkips(this.board.copy(), var2).isEmpty()) {
				var7 = true;
			}

			if (var7) {
				this.isP1Turn = !this.isP1Turn;
				this.skipIndex = -1;
			}

			return true;
		}
	}

	public Board getBoard() {
		return this.board.copy();
	}

	public boolean isGameOver() {
		List var1 = this.board.find(6);
		var1.addAll(this.board.find(7));
		if (var1.isEmpty()) {
			return true;
		} else {
			List var2 = this.board.find(4);
			var2.addAll(this.board.find(5));
			if (var2.isEmpty()) {
				return true;
			} else {
				List var3 = this.isP1Turn ? var1 : var2;
				Iterator var4 = var3.iterator();

				int var6;
				do {
					if (!var4.hasNext()) {
						return true;
					}

					Point var5 = (Point)var4.next();
					var6 = Board.toIndex(var5);
				} while(MoveGenerator.getMoves(this.board, var6).isEmpty() && MoveGenerator.getSkips(this.board, var6).isEmpty());

				return false;
			}
		}
	}

	public boolean isP1Turn() {
		return this.isP1Turn;
	}

	public void setP1Turn(boolean var1) {
		this.isP1Turn = var1;
	}

	public int getSkipIndex() {
		return this.skipIndex;
	}

	public String getGameState() {
		String var1 = "";

		for(int var2 = 0; var2 < 32; ++var2) {
			var1 = var1 + this.board.get(var2);
		}

		var1 = var1 + (this.isP1Turn ? "1" : "0");
		var1 = var1 + this.skipIndex;
		return var1;
	}

	public void setGameState(String var1) {
		this.restart();
		if (var1 != null && !var1.isEmpty()) {
			int var2 = var1.length();

			for(int var3 = 0; var3 < 32 && var3 < var2; ++var3) {
				try {
					char var10000 = var1.charAt(var3);
					int var4 = Integer.parseInt("" + var10000);
					this.board.set(var3, var4);
				} catch (NumberFormatException var6) {
				}
			}

			if (var2 > 32) {
				this.isP1Turn = var1.charAt(32) == '1';
			}

			if (var2 > 33) {
				try {
					this.skipIndex = Integer.parseInt(var1.substring(33));
				} catch (NumberFormatException var5) {
					this.skipIndex = -1;
				}
			}

		}
	}
}
