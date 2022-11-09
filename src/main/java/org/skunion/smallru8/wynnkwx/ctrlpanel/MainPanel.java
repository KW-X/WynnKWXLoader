package org.skunion.smallru8.wynnkwx.ctrlpanel;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JPanel;

public class MainPanel {

	
	public JFrame frmKwxpanel;
	public JMenu KWXLoaderMainMenu;
	public JMenu KWXLoaderSecondMenu;
	public ArrayList<JPanel> panels;
	
	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainPanel window = new MainPanel();
					window.frmKwxpanel.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	 */
	/**
	 * Create the application.
	 */
	public MainPanel() {
		//Load gui style
		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
		initialize();
	}

	public void hideAllPanel() {
		panels.forEach(p -> {
			p.setVisible(false);
		});
	}
	
	public void addKWXJPanel(KWXJPanel kwxP) {
		frmKwxpanel.getContentPane().add(kwxP);
		panels.add(kwxP);
		KWXLoaderMainMenu.add(kwxP.showThisPanel);
	}
	
	public void removeKWXJPanel(KWXJPanel kwxP) {
		frmKwxpanel.getContentPane().remove(kwxP);
		panels.remove(kwxP);
		KWXLoaderMainMenu.remove(kwxP.showThisPanel);
		kwxP.setVisible(false);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		panels = new ArrayList<JPanel>();
		frmKwxpanel = new JFrame();
		frmKwxpanel.setTitle("KWXPanel");
		frmKwxpanel.setBounds(100, 100, 650, 403);
		frmKwxpanel.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmKwxpanel.getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 634, 23);
		frmKwxpanel.getContentPane().add(menuBar);
		
		KWXLoaderMainMenu = new JMenu("Module");
		menuBar.add(KWXLoaderMainMenu);
		
		KWXLoaderSecondMenu = new JMenu("Others");
		menuBar.add(KWXLoaderSecondMenu);
		
	}
}
