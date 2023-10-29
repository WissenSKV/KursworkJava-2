package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ComputerPlayer extends Player {
    private static final double WEIGHT_SKIP = 25.0;
    private static final double SKIP_ON_NEXT = 20.0;
    private static final double SAFE_SAFE = 5.0;
    private static final double SAFE_UNSAFE = -40.0;
    private static final double UNSAFE_SAFE = 40.0;
    private static final double UNSAFE_UNSAFE = -40.0;
    private static final double SAFE = 3.0;
    private static final double UNSAFE = -5.0;
    private static final double KING_FACTOR = 2.0;

    public ComputerPlayer() {
    }

    public boolean isHuman() {
        return false;
    }

    public void updateGame(Game var1) {
        if (var1 != null && !var1.isGameOver()) {
            Game var2 = var1.copy();
            List var3 = this.getMoves(var2);
            int var4 = var3.size();
            int var5 = 1;
            double var6 = Double.NEGATIVE_INFINITY;

            int var8;
            for(var8 = 0; var8 < var4; ++var8) {
                Move var9 = (Move)var3.get(var8);
                this.getMoveWeight(var2.copy(), var9);
                if (var9.getWeight() > var6) {
                    var5 = 1;
                    var6 = var9.getWeight();
                } else if (var9.getWeight() == var6) {
                    ++var5;
                }
            }

            var8 = (int)(Math.random() * (double)var5) % var5;

            for(int var11 = 0; var11 < var4; ++var11) {
                Move var10 = (Move)var3.get(var11);
                if (var6 == var10.getWeight()) {
                    if (var8 == 0) {
                        var1.move(var10.getStartIndex(), var10.getEndIndex());
                    } else {
                        --var8;
                    }
                }
            }

        }
    }

    private List<Move> getMoves(Game var1) {
        ArrayList var2;
        if (var1.getSkipIndex() >= 0) {
            var2 = new ArrayList();
            List var12 = MoveGenerator.getSkips(var1.getBoard(), var1.getSkipIndex());
            Iterator var13 = var12.iterator();

            while(var13.hasNext()) {
                Point var14 = (Point)var13.next();
                var2.add(new Move(var1.getSkipIndex(), Board.toIndex(var14)));
            }

            return var2;
        } else {
            var2 = new ArrayList();
            Board var3 = var1.getBoard();
            if (var1.isP1Turn()) {
                var2.addAll(var3.find(6));
                var2.addAll(var3.find(7));
            } else {
                var2.addAll(var3.find(4));
                var2.addAll(var3.find(5));
            }

            ArrayList var4 = new ArrayList();
            Iterator var5 = var2.iterator();

            Point var6;
            int var7;
            List var8;
            Iterator var9;
            Point var10;
            while(var5.hasNext()) {
                var6 = (Point)var5.next();
                var7 = Board.toIndex(var6);
                var8 = MoveGenerator.getSkips(var3, var7);
                var9 = var8.iterator();

                while(var9.hasNext()) {
                    var10 = (Point)var9.next();
                    Move var11 = new Move(var7, Board.toIndex(var10));
                    var11.changeWeight(25.0);
                    var4.add(var11);
                }
            }

            if (var4.isEmpty()) {
                var5 = var2.iterator();

                while(var5.hasNext()) {
                    var6 = (Point)var5.next();
                    var7 = Board.toIndex(var6);
                    var8 = MoveGenerator.getMoves(var3, var7);
                    var9 = var8.iterator();

                    while(var9.hasNext()) {
                        var10 = (Point)var9.next();
                        var4.add(new Move(var7, Board.toIndex(var10)));
                    }
                }
            }

            return var4;
        }
    }

    private int getSkipDepth(Game var1, int var2, boolean var3) {
        if (var3 != var1.isP1Turn()) {
            return 0;
        } else {
            List var4 = MoveGenerator.getSkips(var1.getBoard(), var2);
            int var5 = 0;
            Iterator var6 = var4.iterator();

            while(var6.hasNext()) {
                Point var7 = (Point)var6.next();
                int var8 = Board.toIndex(var7);
                var1.move(var2, var8);
                int var9 = this.getSkipDepth(var1, var8, var3);
                if (var9 > var5) {
                    var5 = var9;
                }
            }

            return var5 + (var4.isEmpty() ? 0 : 1);
        }
    }

    private void getMoveWeight(Game var1, Move var2) {
        Point var3 = var2.getStart();
        Point var4 = var2.getEnd();
        int var5 = Board.toIndex(var3);
        int var6 = Board.toIndex(var4);
        Board var7 = var1.getBoard();
        boolean var8 = var1.isP1Turn();
        boolean var9 = MoveLogic.isSafe(var7, var3);
        int var10 = var7.get(var5);
        boolean var10000;
        if (var10 != 7 && var10 != 5) {
            var10000 = false;
        } else {
            var10000 = true;
        }

        var2.changeWeight(this.getSafetyWeight(var7, var1.isP1Turn()));
        if (!var1.move(var2.getStartIndex(), var2.getEndIndex())) {
            var2.setWeight(Double.NEGATIVE_INFINITY);
        } else {
            var7 = var1.getBoard();
            var8 = var8 != var1.isP1Turn();
            var10 = var7.get(var6);
            boolean var11 = var10 == 7 || var10 == 5;
            boolean var12 = true;
            int var13;
            if (var8) {
                var12 = MoveLogic.isSafe(var7, var4);
                var13 = this.getSkipDepth(var1, var6, !var1.isP1Turn());
                if (var12) {
                    var2.changeWeight(20.0 * (double)var13 * (double)var13);
                } else {
                    var2.changeWeight(20.0);
                }
            } else {
                var13 = this.getSkipDepth(var1, var5, var1.isP1Turn());
                var2.changeWeight(25.0 * (double)var13 * (double)var13);
            }

            if (var9 && var12) {
                var2.changeWeight(5.0);
            } else if (!var9 && var12) {
                var2.changeWeight(40.0);
            } else if (var9 && !var12) {
                var2.changeWeight(-40.0 * (var11 ? 2.0 : 1.0));
            } else {
                var2.changeWeight(-40.0);
            }

            var2.changeWeight(this.getSafetyWeight(var7, var8 ? !var1.isP1Turn() : var1.isP1Turn()));
        }
    }

    private double getSafetyWeight(Board var1, boolean var2) {
        double var3 = 0.0;
        ArrayList var5 = new ArrayList();
        if (var2) {
            var5.addAll(var1.find(6));
            var5.addAll(var1.find(7));
        } else {
            var5.addAll(var1.find(4));
            var5.addAll(var1.find(5));
        }

        Iterator var6 = var5.iterator();

        while(var6.hasNext()) {
            Point var7 = (Point)var6.next();
            int var8 = Board.toIndex(var7);
            int var9 = var1.get(var8);
            boolean var10 = var9 == 7 || var9 == 5;
            if (MoveLogic.isSafe(var1, var7)) {
                var3 += 3.0;
            } else {
                var3 += -5.0 * (var10 ? 2.0 : 1.0);
            }
        }

        return var3;
    }
}
