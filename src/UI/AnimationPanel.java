package UI;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

@SuppressWarnings("serial")
public class AnimationPanel extends JPanel {
	static JButton startbutton;
	static JButton stopbutton;
	static JButton resetbutton;
	static JTextArea information = new JTextArea("", 100, 65);
	static JScrollPane scroller = new JScrollPane(information);
	
	public JPanel createPanel() {
		JPanel panel = new JPanel();
		Timer timer = new Timer(1000, new TimerListener());
		startbutton = new JButton("Start");
		stopbutton = new JButton("Stop ");
		resetbutton = new JButton("Reset");
		
		startbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.start();
			}
		});
		
		stopbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer.stop();
			}
		});
		
		resetbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Common.Data.furniturepanel.reset();
				timer.stop();
			}
		});
		
		
		startbutton.setBounds(10, 5, 100, 30);
		stopbutton.setBounds(10, 40, 100, 30);
		resetbutton.setBounds(10, 75, 100, 30);

		information.setBorder(BorderFactory.createTitledBorder("Devices State Informations"));
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		information.setEditable(false);
		scroller.setBounds(120, 5, 755, 100);
		
		panel.setLayout(null);
		panel.add(startbutton);
		panel.add(stopbutton);
		panel.add(resetbutton);
		panel.add(scroller);
		
		return panel;
	}
}

class TimerListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		Common.Data.furniturepanel.animationRefresh();
	}					
}
