package UI;
import javax.swing.*;
import java.awt.event.*;

public class Menu {
	static final int WIDTH = 1000;
	static final int HEIGHT = 700;
	
	static final int PICWIDTH = 900;
	static final int PICHEIGHT = 516;
	static final int PICX = 0;
	static final int PICY = 100;
	
	static JFrame frame;
	static JPanel loginpanel;
	static JPanel mainpanel;
	
	static JPanel operatepanel;
	static JPanel buttonpanel;
	
	static Menu menu;
	public static void main(String[] args) {
		menu = new Menu();
	}
	
	public Menu() {
		frame = new JFrame();
		frame.setResizable(false);
		mainpanel = new JPanel();
		frame.setTitle("智能家居安全检验");
		frame.setIconImage(new ImageIcon("data/images/others/sun.png").getImage());
		frame.setLayout(null);
		
		LogMenu logpanel = new LogMenu();
		loginpanel = logpanel.createPanel();
		loginpanel.setBounds(0, 20, 1000, 600);
		frame.getContentPane().add(loginpanel);
		buttonpanel = new JPanel();
		//button:进入系统
		JButton enter = new JButton(new ImageIcon("data/images/buttons/enter.png"));
		enter.setBounds(450, 0, 90, 40);
		enter.addActionListener(new ButtonListener());
		
		buttonpanel.setLayout(null);
		buttonpanel.add(enter);
		buttonpanel.setBounds(0, 550, 1000, 150);
		frame.add(buttonpanel);
		
		Common.Data.checkresultpanel = new CheckResultPanel().createPanel();
		Common.Data.checkresultpanel.setBounds(50, 140, 900, 516);
		frame.add(Common.Data.checkresultpanel);
		Common.Data.checkresultpanel.setVisible(false);

		Common.Data.fixresultpanel = new FixResultPanel().createPanel();
		Common.Data.fixresultpanel.setBounds(50, 140, 900, 516);
		frame.add(Common.Data.fixresultpanel);
		Common.Data.fixresultpanel.setVisible(false);

		Common.Data.furniturepanel = new FurniturePanel();
		OperatePanel op = new OperatePanel();
		operatepanel = op.createPanel();		
		Common.Data.furniturepanel.setBounds(50, 150, 900, 516);
				
		operatepanel.setBounds(50, 0, 900, 150);
		operatepanel.setVisible(false);
		frame.add(operatepanel);
		
		MenuBar bar = new MenuBar();
		JMenuBar mb = bar.createMenuBar();
		frame.setJMenuBar(mb);
		
		frame.setSize(WIDTH,HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			//初始化家具列表
			model.Io.initDevice();
			Common.Data.devices = new String[Common.Info.FurList.size() + 1];
			Common.Data.jsonpaths = new String[Common.Info.FurList.size() + 1];
			Common.Data.devices[0] = "Default";
			Common.Data.jsonpaths[0] = "Default";
			
			int i = 1;
			for(Common.FurListObj obj:Common.Info.FurList) {
				Common.Data.devices[i] = obj.name;
				Common.Data.jsonpaths[i] = obj.jsonpath;
				i++;
			}
			
			frame.remove(loginpanel);
			frame.remove(buttonpanel);
			frame.repaint();
			frame.add(Common.Data.furniturepanel);
			operatepanel.setVisible(true);
			Common.Data.display = new Display();
			Common.Data.display.setVisible(false);
			frame.repaint();
		}
	}
}