package UI;
import javax.swing.*;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class CheckResultPanel extends JPanel {
/*	public static void main(String[] args) {
		CheckResultPanel temp = new CheckResultPanel();
		JFrame frame = new JFrame();
		frame.getContentPane().add(temp);
		frame.setSize(1000, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
*/	
	public JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		String lineSeparator = System.getProperty("line.separator");
		JTextArea text = new JTextArea();
		text.setBorder(new TitledBorder("Verification Result"));
		text.setBounds(0, 0, 900, 480);
		text.setText("Pleasing wait..." + lineSeparator);
		panel.add(text);
		
		return panel;
	}
}
