package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Command {
	public static final String COMMAND_UPDATE = "UPDATE";
	public static final String COMMAND_CONNECT = "CONNECT";
	public static final String COMMAND_DISCONNECT = "DISCONNECT";
	public static final String COMMAND_GET = "GET-STATE";
	private String command;
	private String[] data;

	public Command(String var1, String... var2) {
		this.command = var1;
		this.data = var2;
	}

	public String send(String var1, int var2) {
		String var3 = this.getOutput();
		String var4 = "";

		try {
			Socket var5 = new Socket(var1, var2);
			PrintWriter var6 = new PrintWriter(var5.getOutputStream());
			var6.println(var3);
			var6.flush();
			BufferedReader var7 = new BufferedReader(new InputStreamReader(var5.getInputStream()));

			for(String var8 = null; (var8 = var7.readLine()) != null; var4 = var4 + var8 + "\n") {
			}

			if (!var4.isEmpty()) {
				var4 = var4.substring(0, var4.length() - 1);
			}

			var5.close();
		} catch (UnknownHostException var9) {
			var9.printStackTrace();
		} catch (IOException var10) {
			var10.printStackTrace();
		}

		return var4;
	}

	public String getOutput() {
		String var1 = this.command;
		int var2 = this.data == null ? 0 : this.data.length;

		for(int var3 = 0; var3 < var2 && this.data[var3] != null; ++var3) {
			var1 = var1 + "\n" + this.data[var3];
		}

		return var1;
	}

	public String getCommand() {
		return this.command;
	}

	public void setCommand(String var1) {
		this.command = var1;
	}

	public String[] getData() {
		return this.data;
	}

	public void setData(String[] var1) {
		this.data = var1;
	}
}
