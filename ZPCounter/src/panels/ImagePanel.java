package panels;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;

import frames.ZPCounter;

public class ImagePanel extends SomePanel {

	public ImagePanel(JFrame frm, Statement s) {
		super(frm, s);
		isReadyQuestionLabel.setText("КТУ:");
	}

	@Override
	public void updateAll(int monthNumber, int yearNumber) throws SQLException {
		super.updateAll(monthNumber, yearNumber);
		ResultSet res = s.executeQuery("SELECT * FROM " + ZPCounter.db_name + ".ktu " + "WHERE YEAR(monthYear) = "
				+ yearNumber + " AND MONTH(monthYear) = " + monthNumber + ";");
		setReady(res.next());
		res.close();
	}
}
