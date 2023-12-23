package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import model.Board;
import model.Game;

public class MoveLogic {
	public MoveLogic() {
	}

	public static boolean isValidMove(Game var0, int var1, int var2) {
		return var0 == null ? false : isValidMove(var0.getBoard(), var0.isP1Turn(), var1, var2, var0.getSkipIndex());
	}

	public static boolean isValidMove(Board var0, boolean var1, int var2, int var3, int var4) {
		if (var0 != null && Board.isValidIndex(var2) && Board.isValidIndex(var3)) {
			if (var2 == var3) {
				return false;
			} else if (Board.isValidIndex(var4) && var4 != var2) {
				return false;
			} else if (!validateIDs(var0, var1, var2, var3)) {
				return false;
			} else {
				return validateDistance(var0, var1, var2, var3);
			}
		} else {
			return false;
		}
	}

	private static boolean validateIDs(Board var0, boolean var1, int var2, int var3) {
		if (var0.get(var3) != 0) {
			return false;
		} else {
			int var4 = var0.get(var2);
			if (var1 && var4 != 6 && var4 != 7 || !var1 && var4 != 4 && var4 != 5) {
				return false;
			} else {
				Point var5 = Board.middle(var2, var3);
				int var6 = var0.get(Board.toIndex(var5));
				return var6 == -1 || (var1 || var6 == 6 || var6 == 7) && (!var1 || var6 == 4 || var6 == 5);
			}
		}
	}

	private static boolean validateDistance(Board var0, boolean var1, int var2, int var3) {
		Point var4 = Board.toPoint(var2);
		Point var5 = Board.toPoint(var3);
		int var6 = var5.x - var4.x;
		int var7 = var5.y - var4.y;
		if (Math.abs(var6) == Math.abs(var7) && Math.abs(var6) <= 2 && var6 != 0) {
			int var8 = var0.get(var2);
			if (var8 == 4 && var7 > 0 || var8 == 6 && var7 < 0) {
				return false;
			} else {
				Point var9 = Board.middle(var2, var3);
				int var10 = var0.get(Board.toIndex(var9));
				if (var10 < 0) {
					List var11;
					if (var1) {
						var11 = var0.find(6);
						var11.addAll(var0.find(7));
					} else {
						var11 = var0.find(4);
						var11.addAll(var0.find(5));
					}

					Iterator var12 = var11.iterator();

					while(var12.hasNext()) {
						Point var13 = (Point)var12.next();
						int var14 = Board.toIndex(var13);
						if (!MoveGenerator.getSkips(var0, var14).isEmpty()) {
							return false;
						}
					}
				}

				return true;
			}
		} else {
			return false;
		}
	}

	public static boolean isSafe(Board var0, Point var1) {
		if (var0 != null && var1 != null) {
			int var2 = Board.toIndex(var1);
			if (var2 < 0) {
				return true;
			} else {
				int var3 = var0.get(var2);
				if (var3 == 0) {
					return true;
				} else {
					boolean var4 = var3 == 6 || var3 == 7;
					ArrayList var5 = new ArrayList();
					MoveGenerator.addPoints(var5, var1, 7, 1);
					Iterator var6 = var5.iterator();

					int var8;
					int var14;
					do {
						Point var7;
						boolean var10;
						boolean var11;
						int var12;
						int var13;
						do {
							int var9;
							do {
								do {
									do {
										if (!var6.hasNext()) {
											return true;
										}

										var7 = (Point)var6.next();
										var8 = Board.toIndex(var7);
										var9 = var0.get(var8);
									} while(var9 == 0);
								} while(var9 == -1);

								var10 = var9 == 4 || var9 == 5;
							} while(var4 && !var10);

							var11 = var9 == 7 || var9 == 7;
							var12 = (var1.x - var7.x) * 2;
							var13 = (var1.y - var7.y) * 2;
						} while(!var11 && var10 ^ var13 < 0);

						var14 = Board.toIndex(new Point(var7.x + var12, var7.y + var13));
					} while(!MoveGenerator.isValidSkip(var0, var8, var14));

					return false;
				}
			}
		} else {
			return true;
		}
	}
}
