package org.skunion.smallru8.wynnkwx.ctrlpanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.skunion.smallru8.wynnkwx.WynnKWX;

@SuppressWarnings("serial")
public class KWXJPanel extends JPanel{
	
	/**
	 * Add this to menu
	 */
	public JMenuItem showThisPanel;
	public JPanel self;
	
	public KWXJPanel(String panelName) {
		super();
		self = this;
		setBounds(10, 33, 614, 321);
		showThisPanel = new JMenuItem(panelName);
		showThisPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				WynnKWX.KWXMainPanel.hideAllPanel();
				self.setVisible(true);
			}
		});
	}
	
}
