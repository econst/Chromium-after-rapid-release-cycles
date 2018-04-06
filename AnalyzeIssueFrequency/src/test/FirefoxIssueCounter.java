package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
/**
 * @author Heena
 *
 */
public class FirefoxIssueCounter {

	/**
	 * @param args
	 */
	private static final String FILE_WITH_ISSUES = "Issues/MozillaDataset2008_2013.xlsx";
	
	
	public static void main(String[] args) {
		XSSFWorkbook workbook = null;
		int year_col_num = 35;
		int month_col_num = 36;
		int day_col_num = 37;
		int issue_year;
		int issue_month;
		int issue_count_before_switch = 0;
		int issue_count_on_switch = 0;
		int issue_count_after_switch = 0 ;
		int issue_count_little_after_switch = 0;
		int issue_count_well_after_switch = 0;
		int issue_count_release_9 = 0;
		
		HashMap<String, GregorianCalendar> release_dates = new HashMap<String, GregorianCalendar>();
		release_dates.put("release4_start", new GregorianCalendar(2011, 3, 22));
		release_dates.put("release4_end", new GregorianCalendar(2011, 4, 28));
		release_dates.put("release5_start", new GregorianCalendar(2011, 4, 12));
		release_dates.put("release5_end", new GregorianCalendar(2011, 6, 21));
		release_dates.put("release6_start", new GregorianCalendar(2011, 4, 12));
		release_dates.put("release6_end", new GregorianCalendar(2011, 8, 16));
		release_dates.put("release7_start", new GregorianCalendar(2011, 5, 24));
		release_dates.put("release7_end", new GregorianCalendar(2011, 9, 27));
		release_dates.put("release8_start", new GregorianCalendar(2011, 7, 5));
		release_dates.put("release8_end", new GregorianCalendar(2011, 11, 8));
		release_dates.put("release9_start", new GregorianCalendar(2011, 8, 16));
		release_dates.put("release9_end", new GregorianCalendar(2011, 12, 20));
		
		for(GregorianCalendar date : release_dates.values()){
				date.set(Calendar.MONTH, date.get(Calendar.MONTH)-1);
		}

		// TODO Auto-generated method stub
		FileInputStream mozillaIssuesFile = null;
		try {
			mozillaIssuesFile = new FileInputStream(new File(FILE_WITH_ISSUES));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			 workbook = new XSSFWorkbook(mozillaIssuesFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter();
		for(Row row:sheet){
			for(Cell cell:row){
				CellReference cellref = new CellReference(row.getRowNum(),cell.getColumnIndex());
				//System.out.print(cellref.formatAsString());
				String header = formatter.formatCellValue(cell);
				//System.out.println(header);
				if(header.equals("X1.1")){
					System.out.println(cell.getColumnIndex());
					year_col_num = cell.getColumnIndex();
					month_col_num = year_col_num + 1;
					day_col_num = year_col_num+2;
					break;
				}
			}
			break;
		}
		
		for(Row row:sheet){
			//CellReference cellref = new CellReference(row.getRowNum(),year_col_num);
			if(row.getRowNum()==0) {continue;}
			//System.out.println(year_col_num);
			Cell year_cell = row.getCell(year_col_num);
			Cell month_cell = row.getCell(month_col_num);
			Cell day_cell = row.getCell(day_col_num);
			int year = (int) year_cell.getNumericCellValue();
			int month = (int) month_cell.getNumericCellValue();
			int day = (int) day_cell.getNumericCellValue();
			Calendar calendar = new GregorianCalendar(year,month,day);
			
			if(calendar.before(release_dates.get("release4_end")) && calendar.after(release_dates.get("release4_start"))){
				issue_count_before_switch++;
			}
			
			if(calendar.before(release_dates.get("release5_end")) && calendar.after(release_dates.get("release5_start"))){
				issue_count_on_switch++;
			}
			
			if(calendar.before(release_dates.get("release6_end")) && calendar.after(release_dates.get("release6_start"))){
				issue_count_after_switch++;
			}
			
			if(calendar.before(release_dates.get("release7_end")) && calendar.after(release_dates.get("release7_start"))){
				issue_count_little_after_switch++;
			}
			
			if(calendar.before(release_dates.get("release8_end")) && calendar.after(release_dates.get("release8_start"))){
				issue_count_well_after_switch++;
			}
			
			if(calendar.before(release_dates.get("release9_end")) && calendar.after(release_dates.get("release9_start"))){
				issue_count_release_9++;
			}
		}
		
		System.out.println("The number of issues reported in for the duration of release 4 : " + issue_count_before_switch);
		System.out.println("The number of issues reported in for the duration of release 5 : " + issue_count_on_switch);
		System.out.println("The number of issues reported in for the duration of release 6 : " + issue_count_after_switch);
		System.out.println("The number of issues reported in for the duration of release 7 : " + issue_count_little_after_switch);
		System.out.println("The number of issues reported in for the duration of release 8 : " + issue_count_well_after_switch);
		System.out.println("The number of issues reported in for the duration of release 9 : " + issue_count_release_9);
		
		
	}
	
	/*
	
     	The number of issues reported in for the duration of release 4 : 1733
		The number of issues reported in for the duration of release 5 : 3422
		The number of issues reported in for the duration of release 6 : 4801
		The number of issues reported in for the duration of release 7 : 3321
		The number of issues reported in for the duration of release 8 : 2679
		The number of issues reported in for the duration of release 9 : 2692
		
	 */

}