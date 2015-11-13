import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import java.sql.Statement;

import parser.ExcelParser;

public class Task {
	
	public static Connection con;
	public static String db_name = "zp_data";	

	public static void main(String[] args) {
		try
		{
			con = DriverManager.getConnection("jdbc:mysql://localhost/?" + "user=root&password=1288"
		+"&characterEncoding=utf8");
			ExcelParser pars = new ExcelParser(con, db_name);
	//		if (pars.parseNamesTable("../K_02_15.xls"))
	//		if (pars.parseKmTable("../K_02_15.xls","2015-02-03")) // name of the file take from frontEnd
			if (pars.parseWorkerGeneralDataTable("general_data.xls"))
			{
				System.out.println("Excel file successfuly parsed and writen into database!");
			}
			con.close();
		} catch (SQLException e)
		{
			System.out.println("Oooops!! MySql exception was thrown: "+e.getMessage());
			return;
		}
		System.out.println("Successfuly finished!");

	}

}
