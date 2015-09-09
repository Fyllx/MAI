package panels;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;

import frames.ZPCounter;

public class TimesheetPanel extends SomePanel {

	public TimesheetPanel(JFrame frm, Statement s) {
		super(frm, s);
		isReadyQuestionLabel.setText("Табель:");
	}
	
	@Override
	public void updateAll(int monthNumber, int yearNumber) throws SQLException {
		super.updateAll(monthNumber, yearNumber);
		ResultSet res = s.executeQuery("SELECT * FROM " + ZPCounter.db_name + ".tabel " + "WHERE YEAR(dayMonthYear) = "
				+ yearNumber + " AND MONTH(dayMonthYear) = " + monthNumber + ";");
		setReady(res.next());
		res.close();
	}
}
