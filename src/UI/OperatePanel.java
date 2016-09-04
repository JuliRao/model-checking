package UI;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;

@SuppressWarnings("serial")
public class OperatePanel extends JPanel{
	public JTabbedPane pane;
	public JPanel createPanel() {
		JPanel panel = new JPanel();
		
		pane = new JTabbedPane();
		AddPanel ap = new AddPanel();
		DemoPanel dp = new DemoPanel();
		FixPanel fp = new FixPanel();
		AnimationPanel anp = new AnimationPanel();
		
		JPanel addpanel = ap.createPanel();
		JPanel demopanel = dp.createPanel();
		JPanel fixpanel = fp.createPanel();
		JPanel animationpanel = anp.createPanel();
		
		pane.addChangeListener(new PaneListener());
		
		pane.addTab("Add", addpanel);
		pane.setEnabledAt(0, true);
		pane.addTab("Check", demopanel);
		pane.setEnabledAt(1, true);
		pane.addTab("Fix", fixpanel);
		pane.setEnabledAt(2, true);
		pane.addTab("Demo", animationpanel);
		pane.setEnabledAt(3, true);
		
		pane.setPreferredSize(new Dimension(900, 100));
		pane.setTabPlacement(JTabbedPane.TOP);
		pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		panel.add(pane);
		return panel;
	}
	
	class PaneListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() == pane) {
				if (pane.getSelectedIndex() == 0) {
					Common.Data.furniturepanel.setBounds(50, 150, 900, 516);
					Common.Data.furniturepanel.setVisible(true);
					pane.setPreferredSize(new Dimension(900, 100));
					setBounds(50, 0, 900, 100);
					Common.Data.checkresultpanel.setVisible(false);
					Common.Data.fixresultpanel.setVisible(false);
				}
				else if(pane.getSelectedIndex() == 1) {
					pane.setPreferredSize(new Dimension(900, 100));
					Common.Data.furniturepanel.setVisible(false);
					Common.Data.checkresultpanel.setVisible(true);
					Common.Data.fixresultpanel.setVisible(false);
				}
				else if(pane.getSelectedIndex() == 2) {
					pane.setPreferredSize(new Dimension(900, 100));
					Common.Data.furniturepanel.setVisible(false);
					Common.Data.checkresultpanel.setVisible(false);
					Common.Data.fixresultpanel.setVisible(true);
				}
				else {
					Common.Data.furniturepanel.setBounds(50, 170, 900, 516);
					Common.Data.furniturepanel.setVisible(true);
					pane.setPreferredSize(new Dimension(900, 140));
					setBounds(50, 0, 900, 140);
					Common.Data.checkresultpanel.setVisible(false);
					Common.Data.fixresultpanel.setVisible(false);
				}
			}
		}
	}
}
