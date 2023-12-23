//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.Player;
import network.CheckersNetworkHandler;
import network.ConnectionListener;
import network.Session;

public class CheckersWindow extends JFrame {
    private static final long serialVersionUID = 8782122389400590079L;
    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 600;
    public static final String DEFAULT_TITLE = "Java Checkers";
    private CheckerBoard board;
    private OptionPanel opts;
    private Session session1;
    private Session session2;

    public CheckersWindow() {
        this(500, 600, "Java Checkers");
    }

    public CheckersWindow(Player var1, Player var2) {
        this();
        this.setPlayer1(var1);
        this.setPlayer2(var2);
    }

    public CheckersWindow(int var1, int var2, String var3) {
        super(var3);
        super.setSize(var1, var2);
        super.setLocationByPlatform(true);
        JPanel var4 = new JPanel(new BorderLayout());
        this.board = new CheckerBoard(this);
        this.opts = new OptionPanel(this);
        var4.add(this.board, "Center");
        var4.add(this.opts, "South");
        this.add(var4);
        CheckersNetworkHandler var5 = new CheckersNetworkHandler(true, this, this.board, this.opts);
        CheckersNetworkHandler var6 = new CheckersNetworkHandler(false, this, this.board, this.opts);
        this.session1 = new Session(new ConnectionListener(0, var5), (String)null, (String)null, -1);
        this.session2 = new Session(new ConnectionListener(0, var6), (String)null, (String)null, -1);
    }

    public CheckerBoard getBoard() {
        return this.board;
    }

    public void setPlayer1(Player var1) {
        this.board.setPlayer1(var1);
        this.board.update();
    }

    public void setPlayer2(Player var1) {
        this.board.setPlayer2(var1);
        this.board.update();
    }

    public void restart() {
        this.board.getGame().restart();
        this.board.update();
    }

    public void setGameState(String var1) {
        this.board.getGame().setGameState(var1);
    }

    public Session getSession1() {
        return this.session1;
    }

    public Session getSession2() {
        return this.session2;
    }
}
