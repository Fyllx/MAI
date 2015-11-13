package panels;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;

import frames.ZPCounter;

public class TimesheetPanel extends SomePanel {

	private static final long serialVersionUID = 1L;

	public TimesheetPanel(JFrame frm, Statement s) {
		super(frm, s);
		panelNameLabel.setText("Табель:");
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
