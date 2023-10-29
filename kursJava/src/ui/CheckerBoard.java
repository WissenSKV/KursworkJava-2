package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.Timer;

import model.Board;
import model.Game;
import model.HumanPlayer;
import model.NetworkPlayer;
import model.Player;
import network.Command;
import network.Session;

public class CheckerBoard extends JButton {
	private static final long serialVersionUID = -6014690893709316364L;
	private static final int TIMER_DELAY = 1000;
	private static final int PADDING = 16;
	private Game game;
	private CheckersWindow window;
	private Player player1;
	private Player player2;
	private Point selected;
	private boolean selectionValid;
	private Color lightTile;
	private Color darkTile;
	private boolean isGameOver;
	private Timer timer;

	public CheckerBoard(CheckersWindow var1) {
		this(var1, new Game(), (Player)null, (Player)null);
	}

	public CheckerBoard(CheckersWindow var1, Game var2, Player var3, Player var4) {
		super.setBorderPainted(false);
		super.setFocusPainted(false);
		super.setContentAreaFilled(false);
		super.setBackground(Color.LIGHT_GRAY);
		this.addActionListener(new ClickListener());
		this.game = var2 == null ? new Game() : var2;
		this.lightTile = Color.WHITE;
		this.darkTile = Color.BLACK;
		this.window = var1;
		this.setPlayer1(var3);
		this.setPlayer2(var4);
	}

	public void update() {
		this.runPlayer();
		this.isGameOver = this.game.isGameOver();
		this.repaint();
	}

	private void runPlayer() {
		Player var1 = this.getCurrentPlayer();
		if (var1 != null && !var1.isHuman() && !(var1 instanceof NetworkPlayer)) {
			this.timer = new Timer(1000, new ActionListener() {
				public void actionPerformed(ActionEvent var1) {
					CheckerBoard.this.getCurrentPlayer().updateGame(CheckerBoard.this.game);
					CheckerBoard.this.timer.stop();
					CheckerBoard.this.updateNetwork();
					CheckerBoard.this.update();
				}
			});
			this.timer.start();
		}
	}

	public void updateNetwork() {
		ArrayList var1 = new ArrayList();
		if (this.player1 instanceof NetworkPlayer) {
			var1.add(this.window.getSession1());
		}

		if (this.player2 instanceof NetworkPlayer) {
			var1.add(this.window.getSession2());
		}

		Iterator var2 = var1.iterator();

		while(var2.hasNext()) {
			Session var3 = (Session)var2.next();
			this.sendGameState(var3);
		}

	}

	public synchronized boolean setGameState(boolean var1, String var2, String var3) {
		if (var1 && !this.game.getGameState().equals(var3)) {
			return false;
		} else {
			this.game.setGameState(var2);
			this.repaint();
			return true;
		}
	}

	public void sendGameState(Session var1) {
		if (var1 != null) {
			Command var2 = new Command("UPDATE", new String[]{var1.getSid(), this.game.getGameState()});
			String var3 = var1.getDestinationHost();
			int var4 = var1.getDestinationPort();
			var2.send(var3, var4);
		}
	}

	public void paint(Graphics var1) {
		super.paint(var1);
		Graphics2D var2 = (Graphics2D)var1;
		var2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Game var3 = this.game.copy();
		int var4 = this.getWidth();
		int var5 = this.getHeight();
		int var6 = var4 < var5 ? var4 : var5;
		int var7 = (var6 - 32) / 8;
		int var8 = (var4 - var7 * 8) / 2;
		int var9 = (var5 - var7 * 8) / 2;
		int var10 = Math.max(0, var7 - 8);
		var1.setColor(Color.BLACK);
		var1.drawRect(var8 - 1, var9 - 1, var7 * 8 + 1, var7 * 8 + 1);
		var1.setColor(this.lightTile);
		var1.fillRect(var8, var9, var7 * 8, var7 * 8);
		var1.setColor(this.darkTile);

		int var12;
		for(int var11 = 0; var11 < 8; ++var11) {
			for(var12 = (var11 + 1) % 2; var12 < 8; var12 += 2) {
				var1.fillRect(var8 + var12 * var7, var9 + var11 * var7, var7, var7);
			}
		}

		if (Board.isValidPoint(this.selected)) {
			var1.setColor(this.selectionValid ? Color.GREEN : Color.RED);
			var1.fillRect(var8 + this.selected.x * var7, var9 + this.selected.y * var7, var7, var7);
		}

		Board var17 = var3.getBoard();

		int var13;
		for(var12 = 0; var12 < 8; ++var12) {
			var13 = var9 + var12 * var7 + 4;

			for(int var14 = (var12 + 1) % 2; var14 < 8; var14 += 2) {
				int var15 = var17.get(var14, var12);
				if (var15 != 0) {
					int var16 = var8 + var14 * var7 + 4;
					if (var15 == 6) {
						var1.setColor(Color.DARK_GRAY);
						var1.fillOval(var16 + 1, var13 + 2, var10, var10);
						var1.setColor(Color.LIGHT_GRAY);
						var1.drawOval(var16 + 1, var13 + 2, var10, var10);
						var1.setColor(Color.BLACK);
						var1.fillOval(var16, var13, var10, var10);
						var1.setColor(Color.LIGHT_GRAY);
						var1.drawOval(var16, var13, var10, var10);
					} else if (var15 == 7) {
						var1.setColor(Color.DARK_GRAY);
						var1.fillOval(var16 + 1, var13 + 2, var10, var10);
						var1.setColor(Color.LIGHT_GRAY);
						var1.drawOval(var16 + 1, var13 + 2, var10, var10);
						var1.setColor(Color.DARK_GRAY);
						var1.fillOval(var16, var13, var10, var10);
						var1.setColor(Color.LIGHT_GRAY);
						var1.drawOval(var16, var13, var10, var10);
						var1.setColor(Color.BLACK);
						var1.fillOval(var16 - 1, var13 - 2, var10, var10);
					} else if (var15 == 4) {
						var1.setColor(Color.LIGHT_GRAY);
						var1.fillOval(var16 + 1, var13 + 2, var10, var10);
						var1.setColor(Color.DARK_GRAY);
						var1.drawOval(var16 + 1, var13 + 2, var10, var10);
						var1.setColor(Color.WHITE);
						var1.fillOval(var16, var13, var10, var10);
						var1.setColor(Color.DARK_GRAY);
						var1.drawOval(var16, var13, var10, var10);
					} else if (var15 == 5) {
						var1.setColor(Color.LIGHT_GRAY);
						var1.fillOval(var16 + 1, var13 + 2, var10, var10);
						var1.setColor(Color.DARK_GRAY);
						var1.drawOval(var16 + 1, var13 + 2, var10, var10);
						var1.setColor(Color.LIGHT_GRAY);
						var1.fillOval(var16, var13, var10, var10);
						var1.setColor(Color.DARK_GRAY);
						var1.drawOval(var16, var13, var10, var10);
						var1.setColor(Color.WHITE);
						var1.fillOval(var16 - 1, var13 - 2, var10, var10);
					}

					if (var15 == 7 || var15 == 5) {
						var1.setColor(new Color(255, 240, 0));
						var1.drawOval(var16 - 1, var13 - 2, var10, var10);
						var1.drawOval(var16 + 1, var13, var10 - 4, var10 - 4);
					}
				}
			}
		}

		String var18 = var3.isP1Turn() ? "Player 1's turn" : "Player 2's turn";
		var13 = var1.getFontMetrics().stringWidth(var18);
		Color var19 = var3.isP1Turn() ? Color.BLACK : Color.WHITE;
		Color var20 = var3.isP1Turn() ? Color.WHITE : Color.BLACK;
		var1.setColor(var19);
		var1.fillRect(var4 / 2 - var13 / 2 - 5, var9 + 8 * var7 + 2, var13 + 10, 15);
		var1.setColor(var20);
		var1.drawString(var18, var4 / 2 - var13 / 2, var9 + 8 * var7 + 2 + 11);
		if (this.isGameOver) {
			var1.setFont(new Font("Arial", 1, 20));
			var18 = "Game Over!";
			var13 = var1.getFontMetrics().stringWidth(var18);
			var1.setColor(new Color(240, 240, 255));
			var1.fillRoundRect(var4 / 2 - var13 / 2 - 5, var9 + var7 * 4 - 16, var13 + 10, 30, 10, 10);
			var1.setColor(Color.RED);
			var1.drawString(var18, var4 / 2 - var13 / 2, var9 + var7 * 4 + 7);
		}

	}

	public Game getGame() {
		return this.game;
	}

	public void setGame(Game var1) {
		this.game = var1 == null ? new Game() : var1;
	}

	public CheckersWindow getWindow() {
		return this.window;
	}

	public void setWindow(CheckersWindow var1) {
		this.window = var1;
	}

	public Player getPlayer1() {
		return this.player1;
	}

	public void setPlayer1(Player var1) {
		this.player1 = (Player)(var1 == null ? new HumanPlayer() : var1);
		if (this.game.isP1Turn() && !this.player1.isHuman()) {
			this.selected = null;
		}

	}

	public Player getPlayer2() {
		return this.player2;
	}

	public void setPlayer2(Player var1) {
		this.player2 = (Player)(var1 == null ? new HumanPlayer() : var1);
		if (!this.game.isP1Turn() && !this.player2.isHuman()) {
			this.selected = null;
		}

	}

	public Player getCurrentPlayer() {
		return this.game.isP1Turn() ? this.player1 : this.player2;
	}

	public Color getLightTile() {
		return this.lightTile;
	}

	public void setLightTile(Color var1) {
		this.lightTile = var1 == null ? Color.WHITE : var1;
	}

	public Color getDarkTile() {
		return this.darkTile;
	}

	public void setDarkTile(Color var1) {
		this.darkTile = var1 == null ? Color.BLACK : var1;
	}

	private void handleClick(int var1, int var2) {
		if (!this.isGameOver && this.getCurrentPlayer().isHuman()) {
			Game var3 = this.game.copy();
			int var4 = this.getWidth();
			int var5 = this.getHeight();
			int var6 = var4 < var5 ? var4 : var5;
			int var7 = (var6 - 32) / 8;
			int var8 = (var4 - var7 * 8) / 2;
			int var9 = (var5 - var7 * 8) / 2;
			var1 = (var1 - var8) / var7;
			var2 = (var2 - var9) / var7;
			Point var10 = new Point(var1, var2);
			if (Board.isValidPoint(var10) && Board.isValidPoint(this.selected)) {
				boolean var11 = var3.isP1Turn();
				String var12 = var3.getGameState();
				boolean var13 = var3.move(this.selected, var10);
				boolean var14 = var13 ? this.setGameState(true, var3.getGameState(), var12) : false;
				if (var14) {
					this.updateNetwork();
				}

				var11 = var3.isP1Turn() != var11;
				this.selected = var11 ? null : var10;
			} else {
				this.selected = var10;
			}

			this.selectionValid = this.isValidSelection(var3.getBoard(), var3.isP1Turn(), this.selected);
			this.update();
		}
	}

	private boolean isValidSelection(Board var1, boolean var2, Point var3) {
		int var4 = Board.toIndex(var3);
		int var5 = var1.get(var4);
		if (var5 != 0 && var5 != -1) {
			if (var2 ^ (var5 == 6 || var5 == 7)) {
				return false;
			} else if (!MoveGenerator.getSkips(var1, var4).isEmpty()) {
				return true;
			} else if (MoveGenerator.getMoves(var1, var4).isEmpty()) {
				return false;
			} else {
				List var6 = var1.find(var2 ? 6 : 4);
				var6.addAll(var1.find(var2 ? 7 : 5));
				Iterator var7 = var6.iterator();

				int var9;
				do {
					if (!var7.hasNext()) {
						return true;
					}

					Point var8 = (Point)var7.next();
					var9 = Board.toIndex(var8);
				} while(var9 == var4 || MoveGenerator.getSkips(var1, var9).isEmpty());

				return false;
			}
		} else {
			return false;
		}
	}

	private class ClickListener implements ActionListener {
		private ClickListener() {
		}

		public void actionPerformed(ActionEvent var1) {
			Point var2 = CheckerBoard.this.getMousePosition();
			if (var2 != null) {
				CheckerBoard.this.handleClick(var2.x, var2.y);
			}

		}
	}
}
