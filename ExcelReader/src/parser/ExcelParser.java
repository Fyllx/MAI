package parser;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import jxl.*;
import jxl.read.biff.BiffException; 

public class ExcelParser
{
	Statement s;
	String [][] table;
	int col_len = 0;
	int row_len = 0;
	Connection con;
	String database_name;
	//DB field's names for easier renaming if it needs  
	String worker_general_data = "worker_general_data";
	String worker_default_values = "worker_default_values";
	String name = "name";
	String region = "region";
	String city = "city";
	String birthDate = "birthDate";
	String beginWork = "beginWork";
	String finishWork = "finishWork";
	String beginContract = "beginContract";
	
//	public String [] fio;
//	public Double [] [] dataTable;
	
	public ExcelParser(Connection connect, String database_name) throws SQLException
	{
		con=connect;
		this.database_name=database_name;
	}

	public boolean parseAll(String fileName, int tabNum)
	{
		try
		{
			// общее чтение из Excel для всех таблиц
			Sheet sheet=null;
			File excelFile = new File (fileName);
			Workbook workbook;
			workbook = Workbook.getWorkbook(excelFile);
			sheet = workbook.getSheet(0);
			
			// in first raw in table ((0,0) and (1,0)cells) should be written number of columns
			// and number of raws
			col_len = Integer.parseInt(sheet.getCell(0,0).getContents())+1;
			row_len = Integer.parseInt(sheet.getCell(1,0).getContents());
			String name;
			table = new String[col_len][row_len];
			
			s = con.createStatement();
			s.executeUpdate("USE "+database_name);
			
			//reading from Excel file
			for (int i=1; i<=col_len; i++)
			{
				name = gapRemove(sheet.getCell(1,i).getContents());

				if (name=="") continue;
				table[i-1][0] = name;
				for (int j=2; j<=row_len; j++ )
				{	
					Cell a = sheet.getCell(j,i);
					if (a.getType() == CellType.NUMBER || a.getType() == CellType.BOOLEAN || a.getType() == CellType.DATE || 
							a.getType() == CellType.LABEL) 
					{ 
						table[i-1][j-1] = a.getContents().trim();
					}
				}				
			}

			//запись в БД для каждой таблицы своя
			if (tabNum==1){
				return parseWorkerGeneralDataTable();
			}
			else if (tabNum==2){
				return parseDefaultValuesTable();
			}
			else {
				System.out.println("Not correct number of operation! tabNum=" + tabNum);
				return false;
			}
		}
		catch(IOException ex){
			System.out.println("Ops, IOException: "+ex.getMessage()+" Sorry...");
			ex.printStackTrace();
			return false;
		}
		catch(BiffException bex){
			System.out.println("Ops, BiffException: "+bex.getMessage()+" Sorry...");
			bex.printStackTrace();
			return false;
		}
		catch(java.lang.ArrayIndexOutOfBoundsException arrex){
			System.out.println("Ops, ArrayIndexOutOfBoundsException: "+arrex.getMessage()+" Sorry...");
			arrex.printStackTrace();
			return false;
		} catch (SQLException e)
		{
			System.out.println("Ops, SQLException: "+e.getMessage()+" Sorry...");
			e.printStackTrace();
			return false;
		}
	}

	public boolean parseWorkerGeneralDataTable() throws SQLException
	{
		//writing into DB
		for (int i=1; i<col_len; i++)
		{
			//generation of SQL request 
			String req = worker_general_data + " SET";
			for (int j=0; j<row_len; j++)
			{
				if (table[i][j]==null) continue;
				if (table[0][j].contentEquals(name) || table[0][j].contentEquals(region) || table[0][j].contentEquals(city)){
					req += " " + table[0][j] + "='" + table[i][j] + "',";
				}
				else if (table[0][j].contentEquals(birthDate) || table[0][j].contentEquals(beginWork) 
						|| table[0][j].contentEquals(finishWork) || table[0][j].contentEquals(beginContract)){
					req += " " + table[0][j] + "='" + convertData(table[i][j]) + "',";
				}
				else {
					req += " " + table[0][j] + "=" + table[i][j] + ",";
				}
			}
			req = req.substring(0, req.length()-1);
			
			//Checks if such name already exist in DB, then call "REPLACE" request. Otherwise call "INSERT" request.
		    ResultSet queryRes = s.executeQuery("SELECT (workerID) FROM " + worker_general_data + " where name='" + table[i][0] + "'");
		    System.out.println(req);
		    if(!queryRes.first()) {
		    	s.executeUpdate( "INSERT INTO " + req);
		    }
		    else if (queryRes.isLast())
		    {
		    	s.executeUpdate( "REPLACE " + req + ", workerID="+queryRes.getString(1));
		    }
		    else
		    {
		    	throw new Error("Name " + table[i][0] + " is not unique.");
		    }
		}
		return true;
	}

	public boolean parseDefaultValuesTable() throws SQLException
	{
		//writing into DB default_values table
		for (int i=1; i<col_len; i++)
		{
			//generation of SQL request 
			String req = worker_default_values + " SET";
			for (int j=1; j<row_len; j++)
			{
				if (table[i][j]==null) continue;
				String []a = table[i][j].trim().split("\\,");
				String number = a[0];
				if (a.length>1) number += "." + a[1];
				Double num = Double.parseDouble(number);
				num = num*1000;
				req += " " + table[0][j] + "=" + num.intValue() + ",";
			}
			req = req.substring(0, req.length()-1);
			
			//Checks if such name already exist in DB, then call "REPLACE" request. Otherwise call "INSERT" request.
		    ResultSet queryRes = s.executeQuery("SELECT (workerID) FROM " + worker_general_data + " where name='" + table[i][0] + "'");
		    System.out.println(req);
		    if(!queryRes.first()) {
		    	throw new Error("There is no worker with name: " + table[i][0] 
		    			+ " in worker_general_data table, so it couldn't be writen into table worker_default_values.");
		    }
		    else if (queryRes.isLast())
		    {
		    	String workerId = queryRes.getString(1);
		    	ResultSet queryRes2 = s.executeQuery("SELECT * FROM " + worker_default_values + " where workerID='" + workerId + "'");
		    	 if(!queryRes2.first()) {
			    	s.executeUpdate( "INSERT INTO " + req + ", workerID="+workerId);
			    }
			    else if (queryRes2.isLast())
			    {
			    	s.executeUpdate( "REPLACE " + req + ", workerID="+workerId);
			    }
			    else
			    {
			    	throw new Error("Name " + table[i][0] + " is not unique in worker_default_values table.");
			    }
		    }
		    else
		    {
		    	throw new Error("Name " + table[i][0] + " is not unique in worker_general_data table.");
		    }
		}
		return true;
	}

//	public boolean parseKmTable(String fileName, String monthYear){			
//		try
//		{
//			Sheet sheet=null;
//			File excelKm = new File (fileName);
//			Workbook workbook;
//			workbook = Workbook.getWorkbook(excelKm);
//			sheet = workbook.getSheet(0);
//			
//			// in first raw in table ((0,0) and (0,1)cells) should be written number of columns
//			// and number of raws
//			int col_len = (int)((NumberCell) sheet.getCell(0,0)).getValue();
//			int row_len = (int)((NumberCell) sheet.getCell(1,0)).getValue();
//			String name;
//			Double sum_km = 0.0;
//			Statement s = con.createStatement();
//			s.executeUpdate("USE "+this.db_name);
//			for (int i=3; i<col_len; i++)
//			{
//				name = gapRemove(sheet.getCell(1,i).getContents());
//				
//				if (name=="") continue;
//				for (int j=2; j<row_len; j++ )
//				{	
//					Cell a = sheet.getCell(j,i);
//					if (a.getType() == CellType.NUMBER) 
//					{ 
//						 NumberCell nc = (NumberCell) a; 
//						 sum_km+= nc.getValue();
//					}
//				}
//			    //пишем в базу проверяем по имени соответствие. если не совпадает, то выводим на экран ошибку
//			    ResultSet queryRes = s.executeQuery("SELECT (workerID) FROM worker_general_data where name='"+name+"'");
//			    if (queryRes.next() && queryRes.isLast())
//			    {
//			    	s.executeUpdate("INSERT INTO km (workerID, monthYear, monthSumKm) "
//			    			+ "VALUES (" + queryRes.getString(0) + ", " + monthYear + ", " + sum_km + ")");
//			    }
//			    else
//			    {
//			    	System.out.println("Name " + name + " is not unique or epsent in general_worker_data table");
//					return false;
//			    }
//			    sum_km = 0.0;
//			}
//			workbook.close();
//		}
//		catch(IOException ex){
//			System.out.println("Ops, IOException: "+ex.getMessage()+" Sorry...");
//			return false;
//		}
//		catch(BiffException bex){
//			System.out.println("Ops, BiffException: "+bex.getMessage()+" Sorry...");
//			return false;
//		}
//		catch(java.lang.ArrayIndexOutOfBoundsException arrex){
//			System.out.println("Ops, ArrayIndexOutOfBoundsException: "+arrex.getMessage()+" Sorry...");
//			return false;
//		} catch (SQLException e)
//		{
//			System.out.println("Ops, SQLException: "+e.getMessage()+" Sorry...");
//			return false;
//		}
//		return true;
//	}
	
//	public boolean parseKtuTable(){
//		
//		return true;
//	}

	private String gapRemove(String oldName){
		String convertedName="";
		String []a = oldName.trim().split("\\s+");
		for (int i=0; i<a.length-1; i++)
		{
			convertedName+=a[i]+" ";
		}
		convertedName+=a[a.length-1];
		return convertedName;
	}

	private String convertData(String oldData){
		String convertedData="";
		if (oldData==null) return "";
		String []a = oldData.trim().split("\\.");
		int len = a.length-1;
		for (int i=0; i<len; i++)
		{
			convertedData+=a[len-i]+"-";
		}
		convertedData+=a[0];
		return convertedData;
	}

}
