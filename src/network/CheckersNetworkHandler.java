//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import model.NetworkPlayer;
import ui.CheckerBoard;
import ui.CheckersWindow;
import ui.NetworkWindow;
import ui.OptionPanel;

public class CheckersNetworkHandler implements ActionListener {
    private static final int MIN_SID_LENGTH = 16;
    private static final int MAX_SID_LENGTH = 64;
    public static final String RESPONSE_ACCEPTED = "ACCEPTED";
    public static final String RESPONSE_DENIED = "DENIED";
    private boolean isPlayer1;
    private CheckersWindow window;
    private CheckerBoard board;
    private OptionPanel opts;

    public CheckersNetworkHandler(boolean var1, CheckersWindow var2, CheckerBoard var3, OptionPanel var4) {
        this.isPlayer1 = var1;
        this.window = var2;
        this.board = var3;
        this.opts = var4;
    }

    public void actionPerformed(ActionEvent var1) {
        if (var1 != null && var1.getSource() instanceof ConnectionHandler) {
            ConnectionHandler var2 = (ConnectionHandler)var1.getSource();
            String var3 = ConnectionListener.read(var2.getSocket());
            var3 = var3.replace("\r\n", "\n");
            if (this.window != null && this.board != null && this.opts != null) {
                Session var4 = this.window.getSession1();
                Session var5 = this.window.getSession2();
                String[] var6 = var3.split("\n");
                String var7 = var6[0].split(" ")[0].toUpperCase();
                String var8 = var6.length > 1 ? var6[1] : "";
                String var9 = "";
                boolean var10 = false;
                if (this.isPlayer1) {
                    var10 = var8.equals(var4.getSid());
                } else {
                    var10 = var8.equals(var5.getSid());
                }

                if (!var7.equals("UPDATE")) {
                    if (var7.equals("CONNECT")) {
                        int var15 = -1;

                        try {
                            var15 = Integer.parseInt(var8);
                        } catch (NumberFormatException var14) {
                        }

                        String var12 = var6.length > 2 ? var6[2] : "";
                        boolean var13 = var12.startsWith("1");
                        var9 = this.handleConnect(var2.getSocket(), var15, var13);
                    } else if (var7.equals("GET-STATE")) {
                        if (var10) {
                            var9 = "ACCEPTED\n" + this.board.getGame().getGameState();
                        } else {
                            var9 = "DENIED";
                        }
                    } else if (var7.equals("DISCONNECT")) {
                        if (var10) {
                            var9 = "ACCEPTED\nClient has been disconnected.";
                            if (this.isPlayer1) {
                                var4.setSid((String)null);
                                this.opts.getNetworkWindow1().setCanUpdateConnect(true);
                            } else {
                                var5.setSid((String)null);
                                this.opts.getNetworkWindow2().setCanUpdateConnect(true);
                            }
                        } else {
                            var9 = "DENIED\nError: cannot disconnect if not connected.";
                        }
                    } else {
                        var9 = "DENIED\nJava Checkers - unknown command '" + var7 + "'";
                    }
                } else {
                    String var11 = var10 && var6.length > 2 ? var6[2] : "";
                    var9 = this.handleUpdate(var11);
                }

                sendResponse(var2, var9);
            } else {
                sendResponse(var2, "Client error: invalid network handler.");
            }
        }

    }

    private String handleUpdate(String var1) {
        if (var1.isEmpty()) {
            return "DENIED";
        } else {
            this.board.setGameState(false, var1, (String)null);
            if (!this.board.getCurrentPlayer().isHuman()) {
                this.board.update();
            }

            if (this.isPlayer1 && this.board.getPlayer2() instanceof NetworkPlayer) {
                this.board.sendGameState(this.window.getSession2());
            } else if (!this.isPlayer1 && this.board.getPlayer1() instanceof NetworkPlayer) {
                this.board.sendGameState(this.window.getSession1());
            }

            return "ACCEPTED";
        }
    }

    private String handleConnect(Socket var1, int var2, boolean var3) {
        Session var4 = this.window.getSession1();
        Session var5 = this.window.getSession2();
        String var6 = var4.getSid();
        String var7 = var5.getSid();
        if ((!this.isPlayer1 || var6 == null || var6.isEmpty()) && (this.isPlayer1 || var7 == null || var7.isEmpty())) {
            if (!(this.isPlayer1 ^ var3)) {
                return "DENIED\nError: the other client is already player " + (var3 ? "1." : "2.");
            } else {
                String var8 = var1.getInetAddress().getHostAddress();
                if (!var8.equals("127.0.0.1") || (!this.isPlayer1 || var2 != var5.getSourcePort()) && (this.isPlayer1 || var2 != var4.getSourcePort())) {
                    String var9 = generateSessionID();
                    Session var10 = this.isPlayer1 ? var4 : var5;
                    NetworkWindow var11 = this.isPlayer1 ? this.opts.getNetworkWindow1() : this.opts.getNetworkWindow2();
                    var10.setSid(var9);
                    var10.setDestinationHost(var8);
                    var10.setDestinationPort(var2);
                    var11.setDestinationHost(var8);
                    var11.setDestinationPort(var2);
                    var11.setCanUpdateConnect(false);
                    var11.setMessage("  Connected to " + var8 + ":" + var2 + ".");
                    return "ACCEPTED\n" + var9 + "\nSuccessfully connected.";
                } else {
                    return "DENIED\nError: the client cannot connect to itself.";
                }
            }
        } else {
            return "DENIED\nError: user already connected.";
        }
    }

    private static void sendResponse(ConnectionHandler var0, String var1) {
        if (var0 != null) {
            Socket var2 = var0.getSocket();
            if (var2 != null && !var2.isClosed()) {
                if (var1 == null) {
                    var1 = "";
                }

                try {
                    OutputStream var3 = var2.getOutputStream();

                    try {
                        var3.write(var1.getBytes());
                        var3.flush();
                    } catch (Throwable var17) {
                        if (var3 != null) {
                            try {
                                var3.close();
                            } catch (Throwable var16) {
                                var17.addSuppressed(var16);
                            }
                        }

                        throw var17;
                    }

                    if (var3 != null) {
                        var3.close();
                    }
                } catch (IOException var18) {
                    var18.printStackTrace();
                } finally {
                    try {
                        var2.close();
                    } catch (IOException var15) {
                        var15.printStackTrace();
                    }

                }
            }
        }

    }

    private static String generateSessionID() {
        String var0 = "";
        int var1 = (int)(48.0 * Math.random()) + 16;

        for(int var2 = 0; var2 < var1; ++var2) {
            int var3 = (int)(4.0 * Math.random());
            byte var4 = 32;
            byte var5 = 48;
            if (var3 == 1) {
                var4 = 48;
                var5 = 65;
            } else if (var3 == 2) {
                var4 = 65;
                var5 = 97;
            } else if (var3 == 3) {
                var4 = 97;
                var5 = 125;
            }

            char var6 = (char)((int)(Math.random() * (double)(var5 - var4) + (double)var4));
            var0 = var0 + var6;
        }

        return var0;
    }
}
