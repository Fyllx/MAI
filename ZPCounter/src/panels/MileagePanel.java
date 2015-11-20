package panels;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import frames.ZPCounter;

public class MileagePanel extends SomePanel {

	private static final long serialVersionUID = 1L;

	public MileagePanel(JFrame frm, Statement s) {
		super(frm, s);
		panelNameLabel.setText("Километраж:");
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
		if (monthNumber == 0 || yearNumber == 2012) {
			setReady(false);
			return;
		}
		ResultSet res = s.executeQuery("SELECT * FROM " + ZPCounter.db_name + ".km " + "WHERE YEAR(monthYear) = "
				+ yearNumber + " AND MONTH(monthYear) = " + monthNumber + ";");
		setReady(res.next());
		res.close();
	}

	//read from XML and write into database KTU data. If in this month data already exist, 
	// it will be rewritten.
	protected boolean enterData() {
		if ( ! super.enterData() ) return false;
		try{
			
			if (!tabNode.tableName.startsWith("schedule"))
			{
				throw new Exception("Выбран неверный файл для чтения километража: " + tabNode.tableName);
			}
			if (getMonthFromDate(tabNode.finishDate) != this.monthNumber || getYearFromDate(tabNode.finishDate) != this.yearNumber)
			{
				throw new Exception("Выбран файл с данными неправильного месяца. Нужен : " + this.monthNumber + "-" + this.yearNumber);
			}
			for (int i=0; i < tabNode.users.size(); i++){
				String fio = tabNode.users.get(i).fio;
				Double sumKm = 0.0;
				String monthYear;
				if (this.monthNumber < 10)
				{
					monthYear = String.valueOf(this.yearNumber).substring(2) + "-0" + this.monthNumber + "-01" ;
				}
				else
				{
					monthYear =  String.valueOf(this.yearNumber).substring(2) + "-" + this.monthNumber + "-01" ;
				}
				for (int j=0; j < tabNode.users.get(i).days.size(); j++){
					sumKm += tabNode.users.get(i).days.get(j).distance;
				}
				
				ResultSet queryRes = s.executeQuery("SELECT (workerID) FROM " + ZPCounter.db_name + ".worker_general_data "
						+ "where name='" + fio + "'");
				if(!queryRes.first()) {
					JOptionPane.showMessageDialog(this, "Name " + fio + " is absent in worker_general_data table. "
							+ "So it can't be written into KM table.", "Предупреждение", JOptionPane.WARNING_MESSAGE);
			    }
			    else if (queryRes.isLast())
			    {
			    	String workerId = queryRes.getString(1);
			    	s.executeUpdate("DELETE FROM " + ZPCounter.db_name + ".km WHERE workerID=" + workerId + " AND monthYear='" + monthYear + "'");
			    	s.executeUpdate("INSERT INTO " + ZPCounter.db_name + ".km  SET workerID="+workerId+", "
			    			+ "monthYear='" + monthYear + "', monthSumKm=" + sumKm);
			    }
			    else
			    {
			    	throw new Exception("Name " + fio + " is not unique in worker_general_data table.");
			    }
				queryRes.close();
			}
			updateAll( monthNumber, yearNumber);
		}
		catch (Exception e){
			JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
