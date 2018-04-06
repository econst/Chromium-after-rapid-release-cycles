package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ChromiumIssueCounter {

	private static final String CHROMIUM_ISSUES = "Issues/ChromiumBugs.xlsx";
	private static XSSFWorkbook workbook = null;
	private static HashMap<String, GregorianCalendar> release_dates = new HashMap<String, GregorianCalendar>();
	
	
	
	private static void getIssueFrequency(){
		
		int dateCol = 0;
		int issue_count_release_2 = 0;
		int issue_count_release_3 = 0;
		int issue_count_release_4 = 0;
		int issue_count_release_5 = 0;
		int issue_count_release_6 = 0;
		int issue_count_release_7 = 0;
		int issue_count_release_8 = 0;
		DataFormatter formatter = new DataFormatter();
		
		// TODO Auto-generated method stub
		FileInputStream chromiumIssuesFile = null;
		try {
			chromiumIssuesFile = new FileInputStream(new File(CHROMIUM_ISSUES));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			workbook = new XSSFWorkbook(chromiumIssuesFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		XSSFSheet sheet = workbook.getSheetAt(0);
		
		for(Row row:sheet){
			for(Cell cell:row){
				CellReference cellref = new CellReference(row.getRowNum(),cell.getColumnIndex());
				//System.out.print(cellref.formatAsString());
				String header = formatter.formatCellValue(cell);
				System.out.println(header);
				if(header.equals("Opened")){
					//System.out.println(cell.getColumnIndex());
					dateCol = cell.getColumnIndex();
					break;
				}
			}
			break;
		}
		for(Row row:sheet){
			
			if(row.getRowNum()==0) {continue;}
			//System.out.println(year_col_num);
			Cell year_cell = row.getCell(dateCol);
			//DataFormatter formatter = new DataFormatter();
			String date = formatter.formatCellValue(year_cell);
			//System.out.println(date);
			String[] date_elements = date.split("/");
			
			int month = Integer.parseInt(date_elements[0]);
			int day = Integer.parseInt(date_elements[1]);
			int year = Integer.parseInt("20".concat(date_elements[2]));
			//System.out.println(year);
			Calendar calendar = new GregorianCalendar(year,month,day);
			
			if(calendar.before(release_dates.get("release2_end")) && calendar.after(release_dates.get("release2_start"))){
				issue_count_release_2+=1;
			}
			
			if(calendar.before(release_dates.get("release3_end")) && calendar.after(release_dates.get("release3_start"))){
				issue_count_release_3+=1;
			}
		
			if(calendar.before(release_dates.get("release4_end")) && calendar.after(release_dates.get("release4_start"))){
				issue_count_release_4+=1;
			}
			
			if(calendar.before(release_dates.get("release5_end")) && calendar.after(release_dates.get("release5_start"))){
				issue_count_release_5+=1;
			}
			
			if(calendar.before(release_dates.get("release6_end")) && calendar.after(release_dates.get("release6_start"))){
				issue_count_release_6+=1;
			}
			
			if(calendar.before(release_dates.get("release7_end")) && calendar.after(release_dates.get("release7_start"))){
				issue_count_release_7+=1;
			}
			
			if(calendar.before(release_dates.get("release8_end")) && calendar.after(release_dates.get("release8_start"))){
				issue_count_release_8+=1;
			}
			
			
			
		}
		
		System.out.println("The number of issues reported in for the duration of release 2 : " + issue_count_release_2);
		System.out.println("The number of issues reported in for the duration of release 3 : " + issue_count_release_3);
		System.out.println("The number of issues reported in for the duration of release 4 : " + issue_count_release_4);
		System.out.println("The number of issues reported in for the duration of release 5 : " + issue_count_release_5);
		System.out.println("The number of issues reported in for the duration of release 6 : " + issue_count_release_6);
		System.out.println("The number of issues reported in for the duration of release 7 : " + issue_count_release_7);
		System.out.println("The number of issues reported in for the duration of release 8 : " + issue_count_release_8);

	}

	public static void main(String[] args) {

		release_dates.put("release2_start", new GregorianCalendar(2008, 12, 11));
		release_dates.put("release2_end", new GregorianCalendar(2009, 5, 24));
		release_dates.put("release3_start", new GregorianCalendar(2009, 5, 24));
		release_dates.put("release3_end", new GregorianCalendar(2009, 10, 12));
		release_dates.put("release4_start", new GregorianCalendar(2009, 10, 12));
		release_dates.put("release4_end", new GregorianCalendar(2010, 3, 17));
		release_dates.put("release5_start", new GregorianCalendar(2010, 3, 17));
		release_dates.put("release5_end", new GregorianCalendar(2010, 5, 21));
		release_dates.put("release6_start", new GregorianCalendar(2010, 5, 21));
		release_dates.put("release6_end", new GregorianCalendar(2010, 9, 2));
		release_dates.put("release7_start", new GregorianCalendar(2010, 9, 2));
		release_dates.put("release7_end", new GregorianCalendar(2010, 10, 21));
		release_dates.put("release8_start", new GregorianCalendar(2010, 10, 21));
		release_dates.put("release8_end", new GregorianCalendar(2010, 12, 2));
		
		for(GregorianCalendar date : release_dates.values()){	
				date.set(Calendar.MONTH, date.get(Calendar.MONTH)-1);
		}
		
		getIssueFrequency();

	}

}



/*
	The number of issues reported in for the duration of release 2 : 189
	The number of issues reported in for the duration of release 3 : 238
	The number of issues reported in for the duration of release 4 : 469
	The number of issues reported in for the duration of release 5 : 206
	The number of issues reported in for the duration of release 6 : 267
	The number of issues reported in for the duration of release 7 : 154
	The number of issues reported in for the duration of release 8 : 183

 */