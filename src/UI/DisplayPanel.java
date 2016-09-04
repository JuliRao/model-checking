package UI;
import javax.swing.*;

public class DisplayPanel {
	public JPanel createPanel() {
		JPanel display = new JPanel();
		
		RuleList rl = new RuleList();
		JPanel rldisplay = rl.createPanel();
		
		SpeciList sl = new SpeciList();
		JPanel sldisplay = sl.createPanel();
		
		display.setLayout(null);
		rldisplay.setBounds(10, 0, 480, 140);
		sldisplay.setBounds(500, 0, 480, 140);
		display.add(rldisplay);
		display.add(sldisplay);
		
		return display;
	}

}
