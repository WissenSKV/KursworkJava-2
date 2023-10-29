package ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.ComputerPlayer;
import model.HumanPlayer;
import model.NetworkPlayer;
import model.Player;
import network.Command;
import network.ConnectionListener;
import network.Session;

public class OptionPanel extends JPanel {
	private static final long serialVersionUID = -4763875452164030755L;
	private CheckersWindow window;
	private JButton restartBtn;
	private JComboBox<String> player1Opts;
	private NetworkWindow player1Net;
	private JButton player1Btn;
	private JComboBox<String> player2Opts;
	private NetworkWindow player2Net;
	private JButton player2Btn;

	public OptionPanel(CheckersWindow var1) {
		super(new GridLayout(0, 1));
		this.window = var1;
		OptionListener var2 = new OptionListener();
		String[] var3 = new String[]{"Human", "Computer", "Network"};
		this.restartBtn = new JButton("Restart");
		this.player1Opts = new JComboBox(var3);
		this.player2Opts = new JComboBox(var3);
		this.restartBtn.addActionListener(var2);
		this.player1Opts.addActionListener(var2);
		this.player2Opts.addActionListener(var2);
		JPanel var4 = new JPanel(new FlowLayout(1));
		JPanel var5 = new JPanel(new FlowLayout(0));
		JPanel var6 = new JPanel(new FlowLayout(0));
		this.player1Net = new NetworkWindow(var2);
		this.player1Net.setTitle("Player 1 - Configure Network");
		this.player2Net = new NetworkWindow(var2);
		this.player2Net.setTitle("Player 2 - Configure Network");
		this.player1Btn = new JButton("Set Connection");
		this.player1Btn.addActionListener(var2);
		this.player1Btn.setVisible(false);
		this.player2Btn = new JButton("Set Connection");
		this.player2Btn.addActionListener(var2);
		this.player2Btn.setVisible(false);
		var4.add(this.restartBtn);
		var5.add(new JLabel("(black) Player 1: "));
		var5.add(this.player1Opts);
		var5.add(this.player1Btn);
		var6.add(new JLabel("(white) Player 2: "));
		var6.add(this.player2Opts);
		var6.add(this.player2Btn);
		this.add(var4);
		this.add(var5);
		this.add(var6);
	}

	public CheckersWindow getWindow() {
		return this.window;
	}

	public void setWindow(CheckersWindow var1) {
		this.window = var1;
	}

	public void setNetworkWindowMessage(boolean var1, String var2) {
		if (var1) {
			this.player1Net.setMessage(var2);
		} else {
			this.player2Net.setMessage(var2);
		}

	}

	public NetworkWindow getNetworkWindow1() {
		return this.player1Net;
	}

	public NetworkWindow getNetworkWindow2() {
		return this.player2Net;
	}

	private void handleNetworkUpdate(NetworkWindow var1, ActionEvent var2) {
		if (var1 != null && this.window != null && var2 != null) {
			int var3 = var1.getSourcePort();
			int var4 = var1.getDestinationPort();
			String var5 = var1.getDestinationHost();
			boolean var6 = var1 == this.player1Net;
			Session var7 = var6 ? this.window.getSession1() : this.window.getSession2();
			if (var2.getID() == 1) {
				if (var3 < 1025 || var3 > 65535) {
					var1.setMessage("  Error: source port must be between 1025 and 65535. ");
					return;
				}

				if (!ConnectionListener.available(var3)) {
					var1.setMessage("  Error: source port " + var3 + " is not available.");
					return;
				}

				if (var7.getListener().getPort() != var3) {
					var7.getListener().stopListening();
				}

				var7.getListener().setPort(var3);
				var7.getListener().listen();
				var1.setMessage("  This client is listening on port " + var3);
				var1.setCanUpdateListen(false);
				var1.setCanUpdateConnect(true);
			} else if (var2.getID() == 0) {
				if (var4 < 1025 || var4 > 65535) {
					var1.setMessage("  Error: destination port must be between 1025 and 65535. ");
					return;
				}

				if (var5 == null || var5.isEmpty()) {
					var5 = "127.0.0.1";
				}

				String[] var10003 = new String[2];
				int var10006 = var1.getSourcePort();
				var10003[0] = "" + var10006;
				var10003[1] = var6 ? "1" : "0";
				Command var8 = new Command("CONNECT", var10003);
				String var9 = var8.send(var5, var4);
				if (var9.isEmpty()) {
					var1.setMessage("  Error: could not connect to " + var5 + ":" + var4 + ".");
				} else {
					String[] var10;
					String var11;
					if (var9.startsWith("DENIED")) {
						var10 = var9.split("\n");
						var11 = var10.length > 1 ? var10[1] : "";
						if (var11.isEmpty()) {
							var1.setMessage("  Error: the other client refused to connect.");
						} else {
							var1.setMessage("  " + var11);
						}
					} else if (var9.startsWith("ACCEPTED")) {
						var7.setDestinationHost(var5);
						var7.setDestinationPort(var4);
						var1.setMessage("  Successfully started a session with " + var5 + ":" + var4 + ".");
						var1.setCanUpdateConnect(false);
						var10 = var9.split("\n");
						var11 = var10.length > 1 ? var10[1] : "";
						var7.setSid(var11);
						Command var12 = new Command("GET-STATE", new String[]{var11, null});
						var9 = var12.send(var5, var4);
						var10 = var9.split("\n");
						String var13 = var10.length > 1 ? var10[1] : "";
						this.window.setGameState(var13);
					} else {
						var1.setMessage("  Error: you tried to connect to a host and port that isn't running a checkers client.");
					}
				}
			}

		}
	}

	private static Player getPlayer(JComboBox<String> var0) {
		Object var1 = new HumanPlayer();
		if (var0 == null) {
			return (Player)var1;
		} else {
			String var2 = "" + String.valueOf(var0.getSelectedItem());
			if (var2.equals("Computer")) {
				var1 = new ComputerPlayer();
			} else if (var2.equals("Network")) {
				var1 = new NetworkPlayer();
			}

			return (Player)var1;
		}
	}

	private class OptionListener implements ActionListener {
		private OptionListener() {
		}

		public void actionPerformed(ActionEvent var1) {
			if (OptionPanel.this.window != null) {
				Object var2 = var1.getSource();
				JButton var3 = null;
				boolean var4 = false;
				boolean var5 = true;
				Session var6 = null;
				if (var2 == OptionPanel.this.restartBtn) {
					OptionPanel.this.window.restart();
					OptionPanel.this.window.getBoard().updateNetwork();
				} else {
					Player var7;
					if (var2 == OptionPanel.this.player1Opts) {
						var7 = OptionPanel.getPlayer(OptionPanel.this.player1Opts);
						OptionPanel.this.window.setPlayer1(var7);
						var4 = var7 instanceof NetworkPlayer;
						var3 = OptionPanel.this.player1Btn;
						var6 = OptionPanel.this.window.getSession1();
					} else if (var2 == OptionPanel.this.player2Opts) {
						var7 = OptionPanel.getPlayer(OptionPanel.this.player2Opts);
						OptionPanel.this.window.setPlayer2(var7);
						var4 = var7 instanceof NetworkPlayer;
						var3 = OptionPanel.this.player2Btn;
						var6 = OptionPanel.this.window.getSession2();
						var5 = false;
					} else if (var2 == OptionPanel.this.player1Btn) {
						OptionPanel.this.player1Net.setVisible(true);
					} else if (var2 == OptionPanel.this.player2Btn) {
						OptionPanel.this.player2Net.setVisible(true);
					} else if (var2 == OptionPanel.this.player1Net || var2 == OptionPanel.this.player2Net) {
						OptionPanel.this.handleNetworkUpdate((NetworkWindow)var2, var1);
					}
				}

				if (var3 != null) {
					String var10 = var6.getSid();
					if (!var4 && var3.isVisible() && var10 != null && !var10.isEmpty()) {
						Command var8 = new Command("DISCONNECT", new String[]{var10});
						var8.send(var6.getDestinationHost(), var6.getDestinationPort());
						var6.setSid((String)null);
						NetworkWindow var9 = var5 ? OptionPanel.this.player1Net : OptionPanel.this.player2Net;
						var9.setCanUpdateConnect(true);
					}

					var3.setVisible(var4);
					var3.repaint();
				}

			}
		}
	}
}
