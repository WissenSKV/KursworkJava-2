package ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NetworkWindow extends JFrame {
	private static final long serialVersionUID = -3680869784531557351L;
	public static final int DEFAULT_WIDTH = 400;
	public static final int DEFAULT_HEIGHT = 140;
	public static final String DEFAULT_TITLE = "Configure Network";
	public static final int CONNECT_BUTTON = 0;
	public static final int LISTEN_BUTTON = 1;
	private JTextField srcPort;
	private JTextField destHost;
	private JTextField destPort;
	private JButton listen;
	private JButton connect;
	private JPanel src;
	private JPanel dest;
	private JLabel msg;
	private ActionListener actionListener;

	public NetworkWindow() {
		super("Configure Network");
		super.setSize(400, 140);
		super.setLocationByPlatform(true);
		this.init();
	}

	public NetworkWindow(ActionListener var1) {
		this();
		this.actionListener = var1;
	}

	public NetworkWindow(ActionListener var1, int var2, String var3, int var4) {
		this();
		this.actionListener = var1;
		this.setSourcePort(var2);
		this.setDestinationHost(var3);
		this.setDestinationPort(var4);
	}

	private void init() {
		this.getContentPane().setLayout(new GridLayout(3, 1));
		this.srcPort = new JTextField(4);
		this.destHost = new JTextField(11);
		this.destHost.setText("127.0.0.1");
		this.destPort = new JTextField(4);
		this.listen = new JButton("Listen");
		this.listen.addActionListener(new ButtonListener());
		this.connect = new JButton("Connect");
		this.connect.addActionListener(new ButtonListener());
		this.src = new JPanel(new FlowLayout(0));
		this.dest = new JPanel(new FlowLayout(0));
		this.msg = new JLabel();
		this.src.add(new JLabel("Source port:"));
		this.src.add(this.srcPort);
		this.src.add(this.listen);
		this.dest.add(new JLabel("Destination host/port:"));
		this.dest.add(this.destHost);
		this.dest.add(this.destPort);
		this.dest.add(this.connect);
		this.setCanUpdateConnect(false);
		this.srcPort.setToolTipText("Source port to listen for updates (1025 - 65535)");
		this.destPort.setToolTipText("Destination port to listen for updates (1025 - 65535)");
		this.destHost.setToolTipText("The destination host to send updates to (e.g. localhost)");
		this.createLayout((String)null);
	}

	private void createLayout(String var1) {
		this.getContentPane().removeAll();
		this.getContentPane().add(this.src);
		this.getContentPane().add(this.dest);
		this.msg.setText(var1);
		this.getContentPane().add(this.msg);
		this.msg.setVisible(false);
		this.msg.setVisible(true);
	}

	public void setCanUpdateListen(boolean var1) {
		this.srcPort.setEnabled(var1);
		this.listen.setEnabled(var1);
	}

	public void setCanUpdateConnect(boolean var1) {
		this.destHost.setEnabled(var1);
		this.destPort.setEnabled(var1);
		this.connect.setEnabled(var1);
	}

	public ActionListener getActionListener() {
		return this.actionListener;
	}

	public void setActionListener(ActionListener var1) {
		this.actionListener = var1;
	}

	public int getSourcePort() {
		return parseField(this.srcPort);
	}

	public void setSourcePort(int var1) {
		this.srcPort.setText("" + var1);
	}

	public String getDestinationHost() {
		return this.destHost.getText();
	}

	public void setDestinationHost(String var1) {
		this.destHost.setText(var1);
	}

	public int getDestinationPort() {
		return parseField(this.destPort);
	}

	public void setDestinationPort(int var1) {
		this.destPort.setText("" + var1);
	}

	public String getMessage() {
		return this.msg.getText();
	}

	public void setMessage(String var1) {
		this.createLayout(var1);
	}

	private static int parseField(JTextField var0) {
		if (var0 == null) {
			return 0;
		} else {
			int var1 = 0;

			try {
				var1 = Integer.parseInt(var0.getText());
			} catch (NumberFormatException var3) {
			}

			return var1;
		}
	}

	private class ButtonListener implements ActionListener {
		private ButtonListener() {
		}

		public void actionPerformed(ActionEvent var1) {
			if (NetworkWindow.this.actionListener != null) {
				JButton var2 = (JButton)var1.getSource();
				ActionEvent var3 = null;
				if (var2 == NetworkWindow.this.listen) {
					var3 = new ActionEvent(NetworkWindow.this, 1, (String)null);
				} else {
					var3 = new ActionEvent(NetworkWindow.this, 0, (String)null);
				}

				NetworkWindow.this.actionListener.actionPerformed(var3);
			}

		}
	}
}
