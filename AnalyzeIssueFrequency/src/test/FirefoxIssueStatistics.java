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
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class FirefoxIssueStatistics {
	
	private static HashMap<Integer, ArrayList<GregorianCalendar>> release_dates = new HashMap<Integer, ArrayList<GregorianCalendar>>();
	private static final String FIREFOX_ISSUES = "Issues/MozillaDataset2008_2013.xlsx";
	private static final String ISSUE_CLOSED = "Modified";
	private static final String ISSUE_STATUS = "Status";
	private static final String ISSUE_REPORTED = "Reported";
	private static final String ISSUE_RESOLVED = "RESOLVED";
	private static final String ISSUE_VERIFIED = "VERIFIED";
	
	static int matching =0;
	private static XSSFWorkbook workbook;
	
	private static void initializeReleaseMap(){
		
		ArrayList<GregorianCalendar> release4_list = new ArrayList<GregorianCalendar>();
		release4_list.add(new GregorianCalendar(2011, 3, 22));
		release4_list.add(new GregorianCalendar(2011, 4, 28));
		release_dates.put(4,release4_list);
		
		ArrayList<GregorianCalendar> release5_list = new ArrayList<GregorianCalendar>();
		release5_list.add(new GregorianCalendar(2011, 4, 12));
		release5_list.add(new GregorianCalendar(2011, 6, 21));
		release_dates.put(5,release5_list);
		
		ArrayList<GregorianCalendar> release6_list = new ArrayList<GregorianCalendar>();
		release6_list.add(new GregorianCalendar(2011, 4, 12));
		release6_list.add(new GregorianCalendar(2011, 8, 16));
		release_dates.put(6,release6_list);
		
		ArrayList<GregorianCalendar> release7_list = new ArrayList<GregorianCalendar>();
		release7_list.add(new GregorianCalendar(2011, 5, 24));
		release7_list.add(new GregorianCalendar(2011, 9, 27));
		release_dates.put(7,release7_list);
		
		ArrayList<GregorianCalendar> release8_list = new ArrayList<GregorianCalendar>();
		release8_list.add(new GregorianCalendar(2011, 7, 5));
		release8_list.add(new GregorianCalendar(2011, 11, 8));
		release_dates.put(8,release8_list);
		
		ArrayList<GregorianCalendar> release9_list = new ArrayList<GregorianCalendar>();
		release9_list.add(new GregorianCalendar(2011, 8, 16));
		release9_list.add(new GregorianCalendar(2011, 12, 20));
		release_dates.put(9,release9_list);
		
		
		for(ArrayList<GregorianCalendar> dates : release_dates.values()){
			for(GregorianCalendar date: dates){
				date.set(Calendar.MONTH, date.get(Calendar.MONTH)-1 );
			}
		}
	}
	
	private static void initializeDataSet(){
		FileInputStream mozillaIssuesFile = null;
		try {
			mozillaIssuesFile = new FileInputStream(new File(FIREFOX_ISSUES));
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
				if(header.equals(ISSUE_STATUS))   { issue_status_col =  cell.getColumnIndex();}
				if(header.equals(ISSUE_REPORTED)) { issue_reported_date_col = cell.getColumnIndex();} 
				
			}
		
		
		for(Row row:sheet){
			
			if(row.getRowNum()==0) {continue;}
			Cell issuse_closed_date_cell = row.getCell(issue_closed_date_col);
			Cell issue_Reported_date_cell = row.getCell(issue_reported_date_col);
			
			int reported_year = getIssueDate(issue_Reported_date_cell).get(Calendar.YEAR);
			int reported_month = getIssueDate(issue_Reported_date_cell).get(Calendar.MONTH);
			int reported_day = getIssueDate(issue_Reported_date_cell).get(Calendar.DAY_OF_MONTH);
			
			Cell issue_status_cell = row.getCell(issue_status_col);
			
			//System.out.println(issue_status_cell.getStringCellValue());
			if(reported_year == 2011 && reported_month >= 2 && (issue_status_cell.getStringCellValue().contains(ISSUE_RESOLVED) || 
					issue_status_cell.getStringCellValue().contains(ISSUE_VERIFIED))){
				closed_issue_counter++;
				Calendar closed_date = getIssueDate(issuse_closed_date_cell);
				Calendar opened_date = getIssueDate(issue_Reported_date_cell);
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

	private static Calendar getIssueDate(Cell cell){
		String issue_text = cell.getStringCellValue();
		String[] issue_text_elements = issue_text.split(" ");
		String date = issue_text_elements[0];
		String[] date_elements = date.split("-");
		
		int year = Integer.parseInt(date_elements[0]);
		int month = Integer.parseInt(date_elements[1]);
		int day = Integer.parseInt(date_elements[2]);
		Calendar issue_date = new GregorianCalendar(year,month,day);
		return issue_date;
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
		int year_start,month_start, day_start;
		
		initializeReleaseMap();
		Set<Entry<Integer, ArrayList<GregorianCalendar>>> release_pairs =  release_dates.entrySet();
		
		for(Map.Entry<Integer, ArrayList<GregorianCalendar>> release_pair : release_pairs){
				release_start = release_pair.getValue().get(RELEASE_START_DATE_INDEX);
				release_end = release_pair.getValue().get(RELEASE_END_DATE_INDEX);
				
				if(opened.equals(release_start) || opened.equals(release_end) || (opened.after(release_start) && opened.before(release_end))){
					matching++;
					//System.out.println("Matching : " + matching);
					return release_pair.getKey();
				}
			}
		
		return 0;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		initializeDataSet();
		getAverageDurationToFix();

	}

}
