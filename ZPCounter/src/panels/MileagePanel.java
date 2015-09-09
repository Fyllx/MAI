package panels;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;

import frames.ZPCounter;

public class MileagePanel extends SomePanel {

	public MileagePanel(JFrame frm, Statement s) {
		super(frm, s);
		isReadyQuestionLabel.setText("Километраж:");
	}

	/*
	 * Values can be retrieved using either the index number of the column or
	 * the name of the column. In general, using the column index will be more
	 * efficient. Columns are numbered from 1. For maximum portability, result
	 * set columns within each row should be read in left-to-right order, and
	 * each column should be read only once. (non-Javadoc)
	 * 
	 * @see SomePanel#updateAll(int, int, java.sql.Statement)
	 */

	@Override
	public void updateAll(int monthNumber, int yearNumber) throws SQLException {
		super.updateAll(monthNumber, yearNumber);
		ResultSet res = s.executeQuery("SELECT * FROM " + ZPCounter.db_name + ".km " + "WHERE YEAR(monthYear) = "
				+ yearNumber + " AND MONTH(monthYear) = " + monthNumber + ";");
		setReady(res.next());
		res.close();
	}
}
