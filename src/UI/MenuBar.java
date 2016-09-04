package UI;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
//import javax.swing.text.SimpleAttributeSet;
//import javax.swing.text.StyleConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MenuBar {
	static final int WIDTH=1000;
	static final int HEIGHT=600;
	
	static JFrame frame;
	public JMenuBar createMenuBar() {
		JMenuBar menubar = new JMenuBar();
		
		//Set File
		JMenu filemenu=new JMenu("File");
		filemenu.setMnemonic('F');
		JMenuItem newitem=new JMenuItem("New");
		newitem.addActionListener(new newListener());
		newitem.setAccelerator(KeyStroke.getKeyStroke('N',java.awt.Event.CTRL_MASK,false));
		JMenuItem openitem=new JMenuItem("Open");
		openitem.addActionListener(new openListener());
		openitem.setAccelerator(KeyStroke.getKeyStroke('O',java.awt.Event.CTRL_MASK,false));
		JMenuItem saveitem=new JMenuItem("Save");
		saveitem.addActionListener(new saveListener());
		saveitem.setAccelerator(KeyStroke.getKeyStroke('S',java.awt.Event.CTRL_MASK,false));
		JMenuItem closeitem=new JMenuItem("Close");
		closeitem.addActionListener(new closeListener());
		closeitem.setAccelerator(KeyStroke.getKeyStroke('Q',java.awt.Event.CTRL_MASK,false));
		
//		filemenu.add(newitem);
//		filemenu.addSeparator();
		filemenu.add(openitem);
		filemenu.addSeparator();
		filemenu.add(saveitem);
		filemenu.addSeparator();
		filemenu.add(closeitem);
		menubar.add(filemenu);

		//Set Window
		JMenu windowmenu=new JMenu("Window");
		windowmenu.setMnemonic('W');
		JMenuItem displayitem =new JMenuItem("Display");
		displayitem.setAccelerator(KeyStroke.getKeyStroke('D',java.awt.Event.CTRL_MASK,false));
		displayitem.addActionListener(new displayListener());
		windowmenu.add(displayitem);
		menubar.add(windowmenu);
		
		//Set Preferences
		JMenu preferencesmenu=new JMenu("Preferences");
		JMenuItem modeitem = new JMenuItem("Mode");
		modeitem.addActionListener(new modeListener());
		modeitem.setAccelerator(KeyStroke.getKeyStroke('M',java.awt.Event.CTRL_MASK,false));
		preferencesmenu.add(modeitem);
		menubar.add(preferencesmenu);
		
		//Set Help
		JMenu helpmenu=new JMenu("Help");
		helpmenu.setMnemonic('H');
		JMenuItem aboutitem=new JMenuItem("About");
		aboutitem.setAccelerator(KeyStroke.getKeyStroke('A',java.awt.Event.CTRL_MASK,false));
		aboutitem.addActionListener(new aboutListener());
		helpmenu.add(aboutitem);
		menubar.add(helpmenu);

		return menubar;
	}
	
	public static JFrame modeFrame;
	class modeListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			modeFrame = new JFrame("Preferences"); //Choose Module of Verification
			modeFrame.setLayout(null);
			
			JPanel panel = new JPanel();
			panel.setLayout(null);
			modeFrame.add(panel);
			panel.setBounds(10, 10, 275, 100);
			panel.setBorder(new TitledBorder("Choose Mode"));
			
			JRadioButton fsm = new JRadioButton("Fsm");
			fsm.addActionListener(new fsmListener());
			JRadioButton lha = new JRadioButton("Lha");
			lha.addActionListener(new lhaListener());
			ButtonGroup Model = new ButtonGroup();
			Model.add(fsm);
			Model.add(lha);			
			if(Common.Info.getMode() == Common.CheckType.Fsm)
				Model.setSelected(fsm.getModel(), true);
			else
				Model.setSelected(lha.getModel(), true);
			
			panel.add(fsm);
			panel.add(lha);
			fsm.setBounds(10, 25, 100, 30);
			lha.setBounds(10, 55, 100, 30);			
			
			JButton jbOK = new JButton("OK");
			jbOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					Common.Info.setMode(Common.CheckType.getCheckType(Common.Info.tempMode.getStr()));
					modeFrame.dispose();
				}		
			});
			modeFrame.add(jbOK);
			jbOK.setBounds(110, 120, 80, 25);
			
			JButton jbCancel = new JButton("Cancel");
			jbCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					modeFrame.dispose();
				}		
			});
			modeFrame.add(jbCancel);
			jbCancel.setBounds(205, 120, 80, 25);
			
			modeFrame.setSize(300, 180);
			modeFrame.setResizable(false);
			modeFrame.setLocationRelativeTo(null);
			modeFrame.setVisible(true);
		}
	}
	
	class newListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
//			Menu newmenu = new Menu();
		}
	}
	
	class openListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFileChooser jfc=new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );
			jfc.setCurrentDirectory(new File("data/records"));
			jfc.showDialog(new JLabel(), "Open");

			String filename = "record.xml";
			try {
				filename = jfc.getSelectedFile().getName();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			System.out.println("filename : " + filename);
			model.Io.initFromFile(filename);
		}
	}
	
	class fsmListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Common.Info.tempMode = Common.CheckType.Fsm;
		}		
	}

	class lhaListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Common.Info.tempMode = Common.CheckType.Lha;
		}		
	}
	
	class closeListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			//添加关闭前应做的准备工作
//			JFrame frame = new JFrame("Close");
			String tip = "Do you want to quit without saving?";
			Object[] options = {"Cancel", "No", "Yes"};
			int index = JOptionPane.showOptionDialog(null, tip, null, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			System.out.println("index : " + index);
			if (index == 0)
				;
			else if (index == 2) {
				System.exit(0);
			}
			else {
				JFileChooser jfc=new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
				jfc.setCurrentDirectory(new File("data/records"));
				jfc.showDialog(new JLabel(), "Save");
				jfc.setSelectedFile(null);
				String saveType = "xml";
				jfc.setFileFilter(new FileNameExtensionFilter("XML", saveType));
				String filename = "record.xml";
				try {
					filename = jfc.getSelectedFile().getName();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				System.out.println("filename : " + filename);
				model.Io.saveIntoFile(filename);
			}
		}
	}
	
	class saveListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
/*			String location = "Please Input the filename you want to save as :\n";
			Object res = JOptionPane.showInputDialog(null, location, null, JOptionPane.PLAIN_MESSAGE, null, null, null);
			String filename = res.toString();
			System.out.println("filename : " + filename);
*/
			JFileChooser jfc=new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
			jfc.setCurrentDirectory(new File("data/records"));
			jfc.showDialog(new JLabel(), "Save");
			jfc.setSelectedFile(null);
			String saveType = "xml";
			jfc.setFileFilter(new FileNameExtensionFilter("XML", saveType));
			String filename = "record.xml";
			try {
				filename = jfc.getSelectedFile().getName();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			System.out.println("filename : " + filename);
			model.Io.saveIntoFile(filename);
		}
	}
	
	class displayListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(Common.Data.display != null) {
				Common.Data.display.refresh_devicelist();
				Common.Data.display.refresh_rulelist();
				Common.Data.display.refresh_specilist();
				Common.Data.display.setVisible(true);
			}
		}
	}

	class aboutListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFrame acFrame = new JFrame("About");

			JPanel panel = new JPanel();
			acFrame.add(panel);
			
			JTextArea text = new JTextArea();
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			text.setOpaque(true);
			
			String lineSeparator = System.getProperty("line.separator");
			String actxt ="智能家居安全验证系统" + lineSeparator + lineSeparator +
			"Thank for professor Bu Lei, Xiong Wen" + lineSeparator + 
			"Group of Wang Xizao, Wang Ya'nan, Ma Rao, Shen Siyuan." + lineSeparator + lineSeparator +
			"Copyright @2015-2017"
			;
			
			text.setText(actxt);			
			panel.setLayout(null);
			text.setBounds(5, 5, 285, 130);
			text.setBorder(new LineBorder(Color.RED, 1));
			text.setEditable(false);
			panel.add(text);
			
			JButton jbOK = new JButton("OK");
			jbOK.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event) {
					acFrame.dispose();
				}
			});
			panel.add(jbOK);
			jbOK.setBounds(210, 140, 80, 25);

			
/*			String lineSeparator = System.getProperty("line.separator");
			String actxt ="智能家居安全验证系统" + lineSeparator +  lineSeparator +
			"Thank for professor Bu Lei, Xiong Wen" + lineSeparator + 
			"Group of Wangxiz, Wanyanan, Marao, Shensiyuan." + lineSeparator + lineSeparator +
			"Copyright @2015-2017"
			;
			
			JTextPane jtpane = new JTextPane();
			JScrollPane scroller = new JScrollPane(jtpane);
			acFrame.add(scroller);
			jtpane.setText(actxt);
			jtpane.setEditable(false);
			SimpleAttributeSet attr=new SimpleAttributeSet();
			StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);
			jtpane.setCharacterAttributes(attr,false); */
			
			acFrame.setSize(300, 200);
			acFrame.setResizable(false);
			acFrame.setLocationRelativeTo(null);
			acFrame.setVisible(true);
		}
	}	
}
