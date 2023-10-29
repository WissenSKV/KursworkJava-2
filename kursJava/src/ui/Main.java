package ui;

import javax.swing.UIManager;

public class Main {
	public Main() {
	}

	public static void main(String[] var0) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception var2) {
			var2.printStackTrace();
		}

		CheckersWindow var1 = new CheckersWindow();
		var1.setDefaultCloseOperation(3);
		var1.setVisible(true);
	}
}
