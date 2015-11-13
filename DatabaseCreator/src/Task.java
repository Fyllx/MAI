
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Task
{
	public static Connection con;
	public static String db_name = "zp_data";
	public static String general_tab_name = "worker_general_data";

	public static void main(String[] args)
	{
		
		try
		{
			con = DriverManager.getConnection("jdbc:mysql://localhost/?" + "user=root&password=1288"
		+"&characterEncoding=utf8");//"user=program&password=atms2015zp");
			// do we need "collation utf-8"?
			Statement s = con.createStatement();
			s.executeUpdate("DROP DATABASE IF EXISTS "+db_name);
			s.executeUpdate("CREATE DATABASE "+ db_name + " DEFAULT CHARACTER SET utf8");
			s.executeUpdate("USE "+ db_name);
			createTabWorkerGeneralData(s);
			createTabWorkerDefaultValues(s);
			createTabWorkerActualValues(s);
			createTabKm(s);
			createTabKTU(s);
			createTabTabel(s);
			s.close();
			con.close();
		} catch (SQLException e)
		{
			System.out.println("Oooops!! MySql exception was thrown: "+e.getMessage());
			//e.printStackTrace();
		}
		System.out.println("Successfuly finished!");
	}
	
	public static void createTabWorkerGeneralData(Statement s) throws SQLException
	{
		s.executeUpdate("CREATE TABLE IF NOT EXISTS " + general_tab_name +
				" (workerID MEDIUMINT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
				+ "name VARCHAR(50) NOT NULL, "
				+ "region ENUM('omsk_reg', 'nsk_reg', "
							+ "'kem_reg', 'altai_reg', "
							+ "'tomsk_reg', 'tumen_reg'), "
				+ "city VARCHAR(20), "//can we do ENUM and than change it often
				+ "isProbationFinished BOOLEAN DEFAULT FALSE, "// may be it can be counted from beginWork
				+ "isAutoExist BOOLEAN DEFAULT FALSE, "
				+ "isKmFixed BOOLEAN DEFAULT FALSE, "
				+ "isKtuFixed BOOLEAN DEFAULT FALSE, "
				+ "isWorkingNow BOOLEAN DEFAULT TRUE, "// do we need this?
				+ "birthDate DATE, "
				+ "beginWork DATE, "
				+ "finishWork DATE DEFAULT NULL, "
				+ "beginContract DATE) "
				+ "ENGINE=INNODB");
		s.executeUpdate("INSERT INTO " + general_tab_name + "(name, region, city, isAutoExist, birthDate, beginWork, beginContract) "
				+ "VALUES ('Ivanov Ivan Ivanovich', 'omsk_reg', 'Omsk', true, '1983-06-23', '2015-01-21', '2015-01-22')");
		
	}
	
	public static void createTabWorkerDefaultValues(Statement s) throws SQLException
	{
		String tab_name = "worker_default_values";
		s.executeUpdate("CREATE TABLE IF NOT EXISTS " + tab_name +
				" (workerID MEDIUMINT NOT NULL PRIMARY KEY, "
				+ "salary DECIMAL(7,0) DEFAULT 8000, "// оклад
				+ "prepayment DECIMAL(7,0) DEFAULT 4000, "// аванс
				+ "zp DECIMAL(7,0) DEFAULT 15000, "// ЗП do we need this?
				+ "carRent DECIMAL(5,0) DEFAULT 2000, "// аренда авто
				+ "ktuStartMoney DECIMAL(6,0) DEFAULT 6000, "//КТУ деньги
				+ "kmStartMoney DECIMAL(6,0) DEFAULT 4000, "//км деньги
				+ "dinner DECIMAL(5,0) DEFAULT 3000, "//обеды
				+ "zoch DECIMAL(5,0) DEFAULT 0, "//ЗОЖ
				+ "fixedPremium DECIMAL(7,0) DEFAULT 0, "// фиксированные надбавки
				+ "compTransactions DECIMAL(5,0) DEFAULT 0, "//	компенсация транзакций
				+ "FOREIGN KEY (workerID) "
				+ "REFERENCES " + general_tab_name + " (workerID) "
				+ "ON DELETE CASCADE) "
				+ "ENGINE=INNODB");
		s.executeUpdate("INSERT INTO " + tab_name + "(workerID) VALUES (1)");
	}
	
	public static void createTabWorkerActualValues(Statement s) throws SQLException
	{
		String tab_name = "worker_actual_values";
		s.executeUpdate("CREATE TABLE IF NOT EXISTS " + tab_name +
				" (workerID MEDIUMINT NOT NULL PRIMARY KEY, "
				+ "salary DECIMAL(7,0), "// оклад
				+ "prepayment DECIMAL(7,0), "// аванс
				+ "zp DECIMAL(7,0), "// ЗП do we need this?
				+ "carRent DECIMAL(5,0), "// аренда авто
				+ "ktuStartMoney DECIMAL(6,0), "//КТУ деньги
				+ "kmStartMoney DECIMAL(6,0), "//км деньги
				+ "dinner DECIMAL(5,0), "//обеды
				+ "zoch DECIMAL(5,0), "//ЗОЖ
				+ "fixedPremium DECIMAL(7,0), "// фиксированные надбавки
				+ "compTransactions DECIMAL(5,0), "//	компенсация транзакций
				+ "isVacation BOOLEAN DEFAULT FALSE, "// был ли в этом месяце отпуск
				+ "FOREIGN KEY (workerID) "
				+ "REFERENCES " + general_tab_name + " (workerID) "
				+ "ON DELETE CASCADE) "
				+ "ENGINE=INNODB");
		s.executeUpdate("INSERT INTO " + tab_name + "(workerID) VALUES (1)");
	}
	
	public static void createTabKm(Statement s) throws SQLException
	{
		String tab_name = "km";
		s.executeUpdate("CREATE TABLE IF NOT EXISTS " + tab_name +
				" (kmID MEDIUMINT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
				+ "workerID MEDIUMINT NOT NULL, "
				+ "monthYear DATE, "	
				+ "monthSumKm DECIMAL(10,2), "
				+ "countedResRub DECIMAL(6,0), " // do we need this?
				+ "FOREIGN KEY (workerID) "
				+ "REFERENCES " + general_tab_name + " (workerID) "
				+ "ON DELETE CASCADE) "
				+ "ENGINE=INNODB");
		s.executeUpdate("INSERT INTO " + tab_name + "(workerID, monthYear, monthSumKm) VALUES (1, '2015-05-01', 5440)");
	}
	
	public static void createTabKTU(Statement s) throws SQLException
	{
		String tab_name = "ktu";
		s.executeUpdate("CREATE TABLE IF NOT EXISTS " + tab_name +
				" (ktuID MEDIUMINT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
				+ "workerID MEDIUMINT NOT NULL, "
				+ "monthYear DATE, "	
				+ "monthSumPoints DECIMAL(7,0), "
				+ "countedResRub DECIMAL(6,0), "// do we need this?
				+ "FOREIGN KEY (workerID) "
				+ "REFERENCES " + general_tab_name + " (workerID) "
				+ "ON DELETE CASCADE) "
				+ "ENGINE=INNODB");
		s.executeUpdate("INSERT INTO " + tab_name + "(workerID, monthYear, monthSumPoints) VALUES (1, '2015-05-01', 12677)");
	}
	
	public static void createTabTabel(Statement s) throws SQLException
	{
		String tab_name = "tabel";
		s.executeUpdate("CREATE TABLE IF NOT EXISTS " + tab_name +
				" (tabelID MEDIUMINT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
				+ "workerID MEDIUMINT NOT NULL, "
				+ "dayMonthYear DATE, "
				+ "workedHours DECIMAL(5,2), "
				+ "isVacation BOOLEAN DEFAULT FALSE, "
				+ "isIll BOOLEAN DEFAULT FALSE, "
				+ "isAutoDay BOOLEAN DEFAULT FALSE, "
				+ "isBusinessTrip BOOLEAN DEFAULT FALSE, "//командировка
				+ "isHoliday BOOLEAN DEFAULT FALSE, "
				+ "duty ENUM('notDuty', 'workingHoliday', 'reserve1', 'reserve2', 'gph'), "// дежурство
				+ "note VARCHAR(70), "
				+ "FOREIGN KEY (workerID) "
				+ "REFERENCES " + general_tab_name + " (workerID) "
				+ "ON DELETE CASCADE) "
				+ "ENGINE=INNODB");
		s.executeUpdate("INSERT INTO " + tab_name + "(workerID, dayMonthYear, workedHours, duty) VALUES (1, '2015-05-01', 8.5, 'workingHoliday')");
	}
}
