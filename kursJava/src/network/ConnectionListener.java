package network;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionListener extends Thread {
	private ServerSocket serverSocket;
	private ActionListener connectionHandler;

	public ConnectionListener() {
		this(0);
	}

	public ConnectionListener(int var1) {
		this.setPort(var1);
	}

	public ConnectionListener(int var1, ActionListener var2) {
		this.setPort(var1);
		this.connectionHandler = var2;
	}

	public void listen() {
		this.start();
	}

	public void run() {
		if (this.serverSocket != null) {
			if (this.serverSocket.isClosed()) {
				try {
					this.serverSocket = new ServerSocket(this.serverSocket.getLocalPort());
				} catch (IOException var4) {
					var4.printStackTrace();
				}
			}

			while(!this.serverSocket.isClosed()) {
				try {
					ConnectionHandler var1 = new ConnectionHandler(this, this.serverSocket.accept());
					var1.start();
				} catch (IOException var2) {
					var2.printStackTrace();
				} catch (Exception var3) {
					var3.printStackTrace();
				}
			}

		}
	}

	public boolean stopListening() {
		if (this.serverSocket != null && !this.serverSocket.isClosed()) {
			boolean var1 = false;

			try {
				this.serverSocket.close();
			} catch (IOException var3) {
				var3.printStackTrace();
				var1 = true;
			}

			return !var1;
		} else {
			return true;
		}
	}

	public int getPort() {
		return this.serverSocket.getLocalPort();
	}

	public void setPort(int var1) {
		this.stopListening();

		try {
			if (var1 < 0) {
				this.serverSocket = new ServerSocket(0);
			} else {
				this.serverSocket = new ServerSocket(var1);
			}
		} catch (IOException var3) {
			var3.printStackTrace();
		}

	}

	public ServerSocket getServerSocket() {
		return this.serverSocket;
	}

	public void setServerSocket(ServerSocket var1) {
		this.serverSocket = var1;
	}

	public ActionListener getConnectionHandler() {
		return this.connectionHandler;
	}

	public void setConnectionHandler(ActionListener var1) {
		this.connectionHandler = var1;
	}

	public static String read(Socket var0) {
		if (var0 == null) {
			return "";
		} else {
			String var1 = "";

			try {
				InputStream var2 = var0.getInputStream();
				BufferedReader var3 = new BufferedReader(new InputStreamReader(var2));
				String var4 = null;

				while((var4 = var3.readLine()) != null) {
					var1 = var1 + var4 + "\n";
					if (!var3.ready()) {
						break;
					}
				}

				if (!var1.isEmpty()) {
					var1 = var1.substring(0, var1.length() - 1);
				}
			} catch (IOException var5) {
				var5.printStackTrace();
			}

			return var1;
		}
	}

	public static boolean available(int var0) {
		if (var0 >= 0 && var0 <= 65535) {
			ServerSocket var1 = null;
			DatagramSocket var2 = null;

			try {
				var1 = new ServerSocket(var0);
				var1.setReuseAddress(true);
				var2 = new DatagramSocket(var0);
				var2.setReuseAddress(true);
				boolean var3 = true;
				return var3;
			} catch (IOException var13) {
			} finally {
				if (var2 != null) {
					var2.close();
				}

				if (var1 != null) {
					try {
						var1.close();
					} catch (IOException var12) {
					}
				}

			}

			return false;
		} else {
			return false;
		}
	}
}
