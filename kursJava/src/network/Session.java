package network;

public class Session {
	private ConnectionListener listener;
	private String sid;
	private String destinationHost;
	private int destinationPort;

	public Session(ConnectionListener var1, String var2, String var3, int var4) {
		this.listener = var1;
		this.sid = var2;
		this.destinationHost = var3;
		this.destinationPort = var4;
	}

	public Session(String var1, int var2, String var3, int var4) {
		this.listener = new ConnectionListener(var2);
		this.sid = var1;
		this.destinationHost = var3;
		this.destinationPort = var4;
	}

	public ConnectionListener getListener() {
		return this.listener;
	}

	public void setListener(ConnectionListener var1) {
		this.listener = var1;
	}

	public String getSid() {
		return this.sid;
	}

	public void setSid(String var1) {
		this.sid = var1;
	}

	public String getDestinationHost() {
		return this.destinationHost;
	}

	public void setDestinationHost(String var1) {
		this.destinationHost = var1;
	}

	public int getDestinationPort() {
		return this.destinationPort;
	}

	public void setDestinationPort(int var1) {
		this.destinationPort = var1;
	}

	public int getSourcePort() {
		return this.listener == null ? -1 : this.listener.getPort();
	}

	public void setSourcePort(int var1) {
		if (this.listener != null) {
			this.listener.setPort(var1);
		}

	}
}
