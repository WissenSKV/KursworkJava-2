package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import model.Board;

public class MoveGenerator {
    public MoveGenerator() {
    }

    public static List<Point> getMoves(Board var0, Point var1) {
        return getMoves(var0, Board.toIndex(var1));
    }

    public static List<Point> getMoves(Board var0, int var1) {
        ArrayList var2 = new ArrayList();
        if (var0 != null && Board.isValidIndex(var1)) {
            int var3 = var0.get(var1);
            Point var4 = Board.toPoint(var1);
            addPoints(var2, var4, var3, 1);

            for(int var5 = 0; var5 < var2.size(); ++var5) {
                Point var6 = (Point)var2.get(var5);
                if (var0.get(var6.x, var6.y) != 0) {
                    var2.remove(var5--);
                }
            }

            return var2;
        } else {
            return var2;
        }
    }

    public static List<Point> getSkips(Board var0, Point var1) {
        return getSkips(var0, Board.toIndex(var1));
    }

    public static List<Point> getSkips(Board var0, int var1) {
        ArrayList var2 = new ArrayList();
        if (var0 != null && Board.isValidIndex(var1)) {
            int var3 = var0.get(var1);
            Point var4 = Board.toPoint(var1);
            addPoints(var2, var4, var3, 2);

            for(int var5 = 0; var5 < var2.size(); ++var5) {
                Point var6 = (Point)var2.get(var5);
                if (!isValidSkip(var0, var1, Board.toIndex(var6))) {
                    var2.remove(var5--);
                }
            }

            return var2;
        } else {
            return var2;
        }
    }

    public static boolean isValidSkip(Board var0, int var1, int var2) {
        if (var0 == null) {
            return false;
        } else if (var0.get(var2) != 0) {
            return false;
        } else {
            int var3 = var0.get(var1);
            int var4 = var0.get(Board.toIndex(Board.middle(var1, var2)));
            if (var3 != -1 && var3 != 0) {
                if (var4 != -1 && var4 != 0) {
                    return !((var4 == 6 || var4 == 7) ^ (var3 == 4 || var3 == 5));
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public static void addPoints(List<Point> var0, Point var1, int var2, int var3) {
        boolean var4 = var2 == 7 || var2 == 5;
        if (var4 || var2 == 6) {
            var0.add(new Point(var1.x + var3, var1.y + var3));
            var0.add(new Point(var1.x - var3, var1.y + var3));
        }

        if (var4 || var2 == 4) {
            var0.add(new Point(var1.x + var3, var1.y - var3));
            var0.add(new Point(var1.x - var3, var1.y - var3));
        }

    }
}
