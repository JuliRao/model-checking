package UI;
import java.awt.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class LogMenu extends JPanel {
	public void paintComponent(Graphics g) {
		Image image = new ImageIcon("data/images/background/MainBg2.jpg").getImage();
		g.drawImage(image, 130, 50, 720, 470, this);
	}
	
	public JPanel createPanel() {
		LogMenu panel = new LogMenu();
		panel.setBounds(0, 0, 1000, 500);
		panel.setLayout(null);
		//label:欢迎来到智能家居安全验证系统
		JLabel label = new JLabel(new ImageIcon("data/images/labels/welcome.png"));
		label.setBounds(250, 0, 500, 50);
		panel.add(label);

		return panel;
	}
}
