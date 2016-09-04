package UI;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class FixResultPanel extends JPanel {
	public JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		String lineSeparator = System.getProperty("line.separator");
		JTextArea text = new JTextArea();
		text.setBorder(new TitledBorder("Fixing Suggestion"));
		text.setBounds(0, 0, 900, 400);
		text.setText("Fixing..." + lineSeparator + "Pleasing wait" + lineSeparator);
		panel.add(text);
		
		JButton jbOK = new JButton("OK");
		jbOK.setBounds(710, 420, 70, 30);
		JButton jbRefix = new JButton("Refix");
		jbRefix.setBounds(830, 420, 70, 30);
		panel.add(jbOK);
		panel.add(jbRefix);
		
		return panel;
	}
}
