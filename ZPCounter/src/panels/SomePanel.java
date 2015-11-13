package panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SomePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	protected JLabel panelNameLabel;
	protected JLabel isReadyLabel;
	protected JButton watchResults;
	protected JButton enterData;
	protected Statement s;

	static Color red = Color.red;
	static Color dark_green = new Color(0, 125, 0);
	protected String whenDoesntReady = "Нет", whenReady = "Есть";
	protected boolean ready;

	public SomePanel(JFrame frm, Statement s) {
		super();
		this.s = s; 
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
		frm.getContentPane().add(this);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		panelNameLabel = new JLabel("Something:");
		this.add(panelNameLabel);

		isReadyLabel = new JLabel();
		this.add(isReadyLabel);

		watchResults = new JButton("Посмотреть");
		watchResults.addActionListener(event -> watchResults());
		this.add(watchResults);

		enterData = new JButton("Ввести");
		enterData.addActionListener(event -> enterData());
		this.add(enterData);

		setReady(false);
	}

	protected void setReady(boolean ready) {
		this.ready = ready;
		if (!ready) {
			isReadyLabel.setText(whenDoesntReady);
			isReadyLabel.setForeground(red);

			watchResults.setEnabled(false);
		} else {
			isReadyLabel.setText(whenReady);
			isReadyLabel.setForeground(dark_green);

			watchResults.setEnabled(true);
		}
	}
	public boolean isReady()
	{
		return ready;
	}

	protected void watchResults() {
		// TODO Auto-generated method stub

	}

	protected void enterData() {
		// TODO Auto-generated method stub
	}

	public void updateAll(int monthNumber, int yearNumber) throws SQLException {
		// TODO Auto-generated method stub
	}

}
