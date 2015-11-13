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
	public Connection con;
	public String db_name;
	public String [] fio;
	public Double [] [] dataTable;
	
	public ExcelParser(Connection connect, String database_name)
	{
		con = connect;
		db_name = database_name;
	}

	public boolean parseWorkerGeneralDataTable(String fileName){			
		try
		{
			Sheet sheet=null;
			File excelKm = new File (fileName);
			Workbook workbook;
			workbook = Workbook.getWorkbook(excelKm);
			sheet = workbook.getSheet(0);
			
			// in first raw in table ((0,0) and (1,0)cells) should be written number of columns
			// and number of raws
			int col_len = Integer.parseInt(sheet.getCell(0,0).getContents())+1;
			int row_len = Integer.parseInt(sheet.getCell(1,0).getContents());
			String name;
			String [][] table = new String[col_len][row_len];
			Statement s = con.createStatement();
			s.executeUpdate("USE "+this.db_name);
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
			for (int i=1; i<col_len; i++)
			{
				String req = "worker_general_data SET";
				for (int j=0; j<row_len; j++)
				{
					if (table[i][j]==null) continue;
					if (table[0][j].contentEquals("name") || table[0][j].contentEquals("region") || table[0][j].contentEquals("city")){
						req += " " + table[0][j] + "='" + table[i][j] + "',";
					}
					else if (table[0][j].contentEquals("birthDate") || table[0][j].contentEquals("beginWork") 
							|| table[0][j].contentEquals("finishWork") || table[0][j].contentEquals("beginContract")){
						req += " " + table[0][j] + "='" + convertData(table[i][j]) + "',";
					}
					else {
						req += " " + table[0][j] + "=" + table[i][j] + ",";
					}
				}
				req = req.substring(0, req.length()-1);

				 //пишем в базу проверяем по имени соответствие. если не совпадает, то выводим на экран ошибку
			    ResultSet queryRes = s.executeQuery("SELECT (workerID) FROM worker_general_data where name='" + table[i][0] + "'");
			    System.out.println(req);
			    if (queryRes.next() && queryRes.isLast())
			    {
			    	s.executeUpdate( "REPLACE " + req + ", workerID="+queryRes.getString(1));
			    }
			    else if(!queryRes.first()) {
			    	s.executeUpdate( "INSERT INTO " + req);
			    }
			    else
			    {
			    	System.out.println("Name " + table[i][0] + " is not unique.");
					return false;
			    }	
			}
		}
		catch(IOException ex){
			System.out.println("Ops, IOException: "+ex.getMessage()+" Sorry...");
			return false;
		}
		catch(BiffException bex){
			System.out.println("Ops, BiffException: "+bex.getMessage()+" Sorry...");
			return false;
		}
		catch(java.lang.ArrayIndexOutOfBoundsException arrex){
			System.out.println("Ops, ArrayIndexOutOfBoundsException: "+arrex.getMessage()+" Sorry...");
			arrex.printStackTrace();
			return false;
		} catch (SQLException e)
		{
			System.out.println("Ops, SQLException: "+e.getMessage()+" Sorry...");
			return false;
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
	
//	public boolean parseNamesTable(String fileName){			
//		try
//		{
//			Sheet sheet=null;
//			File excelNames = new File (fileName);
//			Workbook workbook;
//			workbook = Workbook.getWorkbook(excelNames);
//			sheet = workbook.getSheet(0);
//			
//			int col_len = sheet.getColumn(0).length-17;
//			String name;
//			Statement s = con.createStatement();
//			s.executeUpdate("USE "+this.db_name);
//			for (int i=3; i<col_len; i++)
//			{
//				name = gapRemove(sheet.getCell(1,i).getContents());
//				
//				if (name=="") continue;
//			    ResultSet queryRes = s.executeQuery("SELECT (workerID) FROM worker_general_data where name='"+name+"'");
//			    if (!queryRes.first()){
//			    	s.executeUpdate("INSERT INTO worker_general_data (name) "
//			    			+ "VALUES ('" + name + "')");
//			    }
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
	
	public String gapRemove(String oldName){
		String convertedName="";
		String []a = oldName.trim().split("\\s+");
		for (int i=0; i<a.length-1; i++)
		{
			convertedName+=a[i]+" ";
		}
		convertedName+=a[a.length-1];
		return convertedName;
	}
	
	public String convertData(String oldData){
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
