package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int INVALID = -1;
    public static final int EMPTY = 0;
    public static final int BLACK_CHECKER = 6;
    public static final int WHITE_CHECKER = 4;
    public static final int BLACK_KING = 7;
    public static final int WHITE_KING = 5;
    private int[] state;

    public Board() {
        this.reset();
    }

    public Board copy() {
        Board var1 = new Board();
        var1.state = (int[])this.state.clone();
        return var1;
    }

    public void reset() {
        this.state = new int[3];

        for(int var1 = 0; var1 < 12; ++var1) {
            this.set(var1, 6);
            this.set(31 - var1, 4);
        }

    }

    public List<Point> find(int var1) {
        ArrayList var2 = new ArrayList();

        for(int var3 = 0; var3 < 32; ++var3) {
            if (this.get(var3) == var1) {
                var2.add(toPoint(var3));
            }
        }

        return var2;
    }

    public void set(int var1, int var2, int var3) {
        this.set(toIndex(var1, var2), var3);
    }

    public void set(int var1, int var2) {
        if (isValidIndex(var1)) {
            if (var2 < 0) {
                var2 = 0;
            }

            for(int var3 = 0; var3 < this.state.length; ++var3) {
                boolean var4 = (1 << this.state.length - var3 - 1 & var2) != 0;
                this.state[var3] = setBit(this.state[var3], var1, var4);
            }

        }
    }

    public int get(int var1, int var2) {
        return this.get(toIndex(var1, var2));
    }

    public int get(int var1) {
        return !isValidIndex(var1) ? -1 : getBit(this.state[0], var1) * 4 + getBit(this.state[1], var1) * 2 + getBit(this.state[2], var1);
    }

    public static Point toPoint(int var0) {
        int var1 = var0 / 4;
        int var2 = 2 * (var0 % 4) + (var1 + 1) % 2;
        return !isValidIndex(var0) ? new Point(-1, -1) : new Point(var2, var1);
    }

    public static int toIndex(int var0, int var1) {
        return !isValidPoint(new Point(var0, var1)) ? -1 : var1 * 4 + var0 / 2;
    }

    public static int toIndex(Point var0) {
        return var0 == null ? -1 : toIndex(var0.x, var0.y);
    }

    public static int setBit(int var0, int var1, boolean var2) {
        if (var1 >= 0 && var1 <= 31) {
            if (var2) {
                var0 |= 1 << var1;
            } else {
                var0 &= ~(1 << var1);
            }

            return var0;
        } else {
            return var0;
        }
    }

    public static int getBit(int var0, int var1) {
        if (var1 >= 0 && var1 <= 31) {
            return (var0 & 1 << var1) != 0 ? 1 : 0;
        } else {
            return 0;
        }
    }

    public static Point middle(Point var0, Point var1) {
        return var0 != null && var1 != null ? middle(var0.x, var0.y, var1.x, var1.y) : new Point(-1, -1);
    }

    public static Point middle(int var0, int var1) {
        return middle(toPoint(var0), toPoint(var1));
    }

    public static Point middle(int var0, int var1, int var2, int var3) {
        int var4 = var2 - var0;
        int var5 = var3 - var1;
        if (var0 >= 0 && var1 >= 0 && var2 >= 0 && var3 >= 0 && var0 <= 7 && var1 <= 7 && var2 <= 7 && var3 <= 7) {
            if (var0 % 2 != var1 % 2 && var2 % 2 != var3 % 2) {
                return Math.abs(var4) == Math.abs(var5) && Math.abs(var4) == 2 ? new Point(var0 + var4 / 2, var1 + var5 / 2) : new Point(-1, -1);
            } else {
                return new Point(-1, -1);
            }
        } else {
            return new Point(-1, -1);
        }
    }

    public static boolean isValidIndex(int var0) {
        return var0 >= 0 && var0 < 32;
    }

    public static boolean isValidPoint(Point var0) {
        if (var0 == null) {
            return false;
        } else {
            int var1 = var0.x;
            int var2 = var0.y;
            if (var1 >= 0 && var1 <= 7 && var2 >= 0 && var2 <= 7) {
                return var1 % 2 != var2 % 2;
            } else {
                return false;
            }
        }
    }

    public String toString() {
        String var1 = this.getClass().getName() + "[";

        for(int var2 = 0; var2 < 31; ++var2) {
            var1 = var1 + this.get(var2) + ", ";
        }

        var1 = var1 + this.get(31);
        return var1 + "]";
    }
}
