package panels;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
		if (monthNumber == 0 || yearNumber == 2012) {
			setReady(false);
			return;
		}
		ResultSet res = s.executeQuery("SELECT * FROM " + ZPCounter.db_name + ".tabel " + "WHERE YEAR(dayMonthYear) = "
				+ yearNumber + " AND MONTH(dayMonthYear) = " + monthNumber + ";");
		setReady(res.next());
		res.close();
	}

	//read from XML and write into database schedule data. If in this month data already exist, 
	// it will be rewritten.
	protected boolean enterData() {
		if ( ! super.enterData() ) return false;
		try{
			
			if (!tabNode.tableName.startsWith("schedule"))
			{
				throw new Exception("Выбран неверный файл для чтения табеля: " + tabNode.tableName);
			}
			if (getMonthFromDate(tabNode.finishDate) != this.monthNumber || getYearFromDate(tabNode.finishDate) != this.yearNumber)
			{
				throw new Exception("Выбран файл с данными неправильного месяца. Нужен : " + this.monthNumber + "-" + this.yearNumber);
			}
			for (int i=0; i < tabNode.users.size(); i++){
				String fio = tabNode.users.get(i).fio;

				ResultSet queryRes = s.executeQuery("SELECT (workerID) FROM " + ZPCounter.db_name + ".worker_general_data "
						+ "where name='" + fio + "'");
				if(!queryRes.first()) {
					JOptionPane.showMessageDialog(this, "Name " + fio + " is absent in worker_general_data table. "
							+ "So it can't be written into tabel table.", "Предупреждение", JOptionPane.WARNING_MESSAGE);
			    }
			    else if (queryRes.isLast())
			    {
			    	String workerId = queryRes.getString(1);
			    	for (int j=0; j < tabNode.users.get(i).days.size(); j++){
			    		String day = convertData(tabNode.users.get(i).days.get(j).date);
						s.executeUpdate("DELETE FROM " + ZPCounter.db_name + ".tabel WHERE workerID=" + workerId 
								+ " AND dayMonthYear='" + day + "'");

						String duty = "notDuty";
						if (tabNode.users.get(i).days.get(j).duty8h == 1) duty = "workingHoliday";
						else if (tabNode.users.get(i).days.get(j).dutyGPH == 1) duty = "gph";
						else if (tabNode.users.get(i).days.get(j).duty500 == 1) duty = "reserve1";
						else if (tabNode.users.get(i).days.get(j).duty250 == 1) duty = "reserve2";
						
						s.executeUpdate("INSERT INTO " + ZPCounter.db_name + ".tabel  SET workerID="+workerId
								+", dayMonthYear='" + day 
				    			+ "', workedHours=" + tabNode.users.get(i).days.get(j).workedHours
				    			+ ", isVacation=" + tabNode.users.get(i).days.get(j).vacation
				    			+ ", isIll=" + tabNode.users.get(i).days.get(j).sick
				    			+ ", isAutoDay=" + tabNode.users.get(i).days.get(j).avtoDay
				    			+ ", isBusinessTrip=" + tabNode.users.get(i).days.get(j).businessTrip
				    			+ ", isHoliday=" + tabNode.users.get(i).days.get(j).dayOff
				    			+ ", duty='" + duty
				    			+ "', isVacationWoPay=" + tabNode.users.get(i).days.get(j).vacationWoPay
				    			+ ", isVacationStudent=" + tabNode.users.get(i).days.get(j).vacationStudent);
					}
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
