package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ChromiumIssueStatistics {
	
	private static HashMap<Integer, ArrayList<GregorianCalendar>> release_dates = new HashMap<Integer, ArrayList<GregorianCalendar>>();
	private static final String CHROMIUM_ISSUES = "Issues/ChromiumBugs.xlsx";
	private static final String ISSUE_CLOSED = "Modified";
	private static final String ISSUE_STATUS = "Status";
	private static final String ISSUE_REPORTED = "Opened";
	private static final String ISSUE_ASSIGNED = "Assigned";
	//private static final String ISSUE_VERIFIED = "VERIFIED";
	
	private static XSSFWorkbook workbook;
	
	private static void initializeReleaseMap(){
		
		ArrayList<GregorianCalendar> release2_list = new ArrayList<GregorianCalendar>();
		release2_list.add(new GregorianCalendar(2008, 12, 11));
		release2_list.add(new GregorianCalendar(2009, 5, 24));
		release_dates.put(2,release2_list);
		
		ArrayList<GregorianCalendar> release3_list = new ArrayList<GregorianCalendar>();
		release3_list.add(new GregorianCalendar(2009, 5, 24));
		release3_list.add(new GregorianCalendar(2009, 10, 12));
		release_dates.put(3,release3_list);
		
		ArrayList<GregorianCalendar> release4_list = new ArrayList<GregorianCalendar>();
		release4_list.add(new GregorianCalendar(2009, 10, 12));
		release4_list.add(new GregorianCalendar(2010, 3, 17));
		release_dates.put(4,release4_list);
		
		ArrayList<GregorianCalendar> release5_list = new ArrayList<GregorianCalendar>();
		release5_list.add(new GregorianCalendar(2010, 3, 17));
		release5_list.add(new GregorianCalendar(2010, 5, 21));
		release_dates.put(5,release5_list);
		
		ArrayList<GregorianCalendar> release6_list = new ArrayList<GregorianCalendar>();
		release6_list.add(new GregorianCalendar(2010, 5, 21));
		release6_list.add(new GregorianCalendar(2010, 9, 2));
		release_dates.put(6,release6_list);
		
		ArrayList<GregorianCalendar> release7_list = new ArrayList<GregorianCalendar>();
		release7_list.add(new GregorianCalendar(2010, 9, 2));
		release7_list.add(new GregorianCalendar(2010, 10, 21));
		release_dates.put(7,release7_list);
		
		ArrayList<GregorianCalendar> release8_list = new ArrayList<GregorianCalendar>();
		release8_list.add(new GregorianCalendar(2010, 10, 21));
		release8_list.add( new GregorianCalendar(2010, 12, 2));
		release_dates.put(8,release8_list);
		
		for(ArrayList<GregorianCalendar> dates : release_dates.values()){
			for(GregorianCalendar date: dates){
				date.set(Calendar.MONTH, date.get(Calendar.MONTH)-1);
			}
		}
	}
	
	private static void initilizeDataSet(){
		FileInputStream mozillaIssuesFile = null;
		try {
			mozillaIssuesFile = new FileInputStream(new File(CHROMIUM_ISSUES));
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
	}
	
	public static void getAverageDurationToFix(){
		XSSFSheet sheet = workbook.getSheetAt(0);
		DataFormatter formatter = new DataFormatter();
		final int CLOSED_ISSUE_COUNT_INDEX = 0;
		final int ISSUE_FIX_DURATION_INDEX = 1;
		int closed_issue_counter =0;
		
		int issue_closed_date_col = 0;
		int issue_status_col = 0;
		int issue_reported_date_col = 0;
		int duration_to_fix =0 ;
		int release = 0;
		
		HashMap<Integer, List<Integer>> release_info = new HashMap<Integer, List<Integer>>(10);
	
			for(Cell cell:sheet.getRow(0)){
				
				String header = formatter.formatCellValue(cell);
				//System.out.println(header);
				if(header.equals(ISSUE_CLOSED))   { issue_closed_date_col = cell.getColumnIndex();}
				if(header.equals(ISSUE_STATUS))   { issue_status_col =  cell.getColumnIndex(); System.out.println(issue_status_col);}
				if(header.equals(ISSUE_REPORTED)) { issue_reported_date_col = cell.getColumnIndex();} 
				
			}
		
		
		for(Row row:sheet){
			
			if(row.getRowNum()==0) {continue;}
			
			Cell issuse_closed_date_cell = row.getCell(issue_closed_date_col);
			Cell issue_Reported_date_cell = row.getCell(issue_reported_date_col);
			Cell issue_status_cell = row.getCell(issue_status_col);
			System.out.println(issue_status_cell.getStringCellValue());
			
			Calendar closed_date = getModifiedIssueDate(issuse_closed_date_cell);
			Calendar opened_date = getOpenedIssueDate(issue_Reported_date_cell);
			String issue_status = issue_status_cell.getStringCellValue();
		
			System.out.println(issue_status_cell.getStringCellValue());
			if(issue_status.contains(ISSUE_ASSIGNED)){
				closed_issue_counter++;	
				release = getReleaseNumberForIssue(opened_date);
				//System.out.println("Release number:"+release);
				duration_to_fix = getDurationToFix(opened_date,closed_date);
				
				
				if(release_info.containsKey(release)){
					List<Integer> stats = new ArrayList<Integer>();
					//first value in stats is the number of closed issues
					stats.add(release_info.get(release).get(CLOSED_ISSUE_COUNT_INDEX)+1);
					//second value is the total number of minutes required to fix issues
					stats.add(release_info.get(release).get(ISSUE_FIX_DURATION_INDEX)+duration_to_fix);
					release_info.put(release, stats);
				}else{
					List<Integer> stats = new ArrayList<Integer>();
					//first value in stats is the number of closed issues
					stats.add(0);
					//second value is the total number of minutes required to fix issues
					stats.add(0);
					release_info.put(release, stats);
				}
			}
	
		}
		
		System.out.println(closed_issue_counter);
		System.out.println(release_info);
		
		Set<Entry<Integer, List<Integer>>> stats_by_release =  release_info.entrySet();
		
		for(Map.Entry<Integer, List<Integer>> release_stat : stats_by_release){
			int issue_count = release_stat.getValue().get(CLOSED_ISSUE_COUNT_INDEX);
			int total_duration = release_stat.getValue().get(ISSUE_FIX_DURATION_INDEX);
			int avg_duration_to_fix = (int)(total_duration/issue_count);
			List<Integer> avg_stats = new ArrayList<Integer>();
			avg_stats.add(issue_count);
			avg_stats.add(avg_duration_to_fix);
			release_info.put(release_stat.getKey(),avg_stats);
		}		
		System.out.println(release_info);
	}
	
	private static Calendar getModifiedIssueDate(Cell cell){
		DataFormatter formatter = new DataFormatter();
		String issue_text = formatter.formatCellValue(cell);
		//String issue_text = cell.getStringCellValue();
		String[] issue_text_elements = issue_text.split(" ");
		String date = issue_text_elements[0];
		String[] date_elements = date.split("/");
		
		int year = Integer.parseInt("20".concat(date_elements[2]));
		int month = Integer.parseInt(date_elements[0]);
		int day = Integer.parseInt(date_elements[1]);
		Calendar issue_date = new GregorianCalendar(year,month,day);
		//System.out.println(issue_date.toString());
		return issue_date;
	}
	
	private static Calendar getOpenedIssueDate(Cell cell) {
		DataFormatter formatter = new DataFormatter();
		String issue_date = formatter.formatCellValue(cell);
		String[] date_elements = issue_date.split("/");
	
		int year = Integer.parseInt("20".concat(date_elements[2]));
		int month = Integer.parseInt(date_elements[0]);
		int day = Integer.parseInt(date_elements[1]);
		Calendar date = new GregorianCalendar(year,month,day);
		//System.out.println(issue_date.toString());
		return date;
	}
	
	private static int getDurationToFix(Calendar opened, Calendar closed){
		long time_to_fix = closed.getTimeInMillis() - opened.getTimeInMillis();
		long mills_in_a_day = 1000*24*60*60;
		int days = (int) (time_to_fix/mills_in_a_day);
		return days;
	}
	
	private static int getReleaseNumberForIssue(Calendar opened){
		
		final int RELEASE_START_DATE_INDEX = 0;
		final int RELEASE_END_DATE_INDEX = 1;
		GregorianCalendar release_start; 
		GregorianCalendar release_end; 
		//int year_start,month_start, day_start;
		
		initializeReleaseMap();
		Set<Entry<Integer, ArrayList<GregorianCalendar>>> release_pairs =  release_dates.entrySet();
		
		for(Map.Entry<Integer, ArrayList<GregorianCalendar>> release_pair : release_pairs){
				release_start = release_pair.getValue().get(RELEASE_START_DATE_INDEX);
				release_end = release_pair.getValue().get(RELEASE_END_DATE_INDEX);
				
				if(opened.after(release_start) && opened.before(release_end)){
					//System.out.println(opened.get(Calendar.YEAR) + " " + opened.get(Calendar.MONTH) + " " + opened.get(Calendar.DAY_OF_MONTH));		
					return release_pair.getKey();
				}else{
					//System.out.println(opened.get(Calendar.YEAR) + " " + opened.get(Calendar.MONTH) + " " + opened.get(Calendar.DAY_OF_MONTH));
					
				}
			}
		
		return 0;
	}
	
	public static void main(String[] args) {
		
		initilizeDataSet();
		getAverageDurationToFix();

	}

}
