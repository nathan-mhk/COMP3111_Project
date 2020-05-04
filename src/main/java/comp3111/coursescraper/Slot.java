package comp3111.coursescraper;

import java.util.Map;
import java.util.HashMap;
import java.time.LocalTime;
import java.util.Locale;
import java.time.format.DateTimeFormatter;

/**
 * <b>A Slot in a section</b><br>
 * Slot stores a time slot of a section, e.g.: Tu 0900AM - 1030AM. <br>
 * 
 */
public class Slot {
	private int day;
	private LocalTime start;
	private LocalTime end;
	private String venue;
	public static final String DAYS[] = {"Mo", "Tu", "We", "Th", "Fr", "Sa"};
	public static final Map<String, Integer> DAYS_MAP = new HashMap<String, Integer>();
	static {
		for (int i = 0; i < DAYS.length; i++)
			DAYS_MAP.put(DAYS[i], i);
	}

	/**
	 * This function duplicates a Slot object
	 * @return This return the cloned slot
	 */
	@Override
	public Slot clone() {
		Slot s = new Slot();
		s.day = this.day;
		s.start = this.start;
		s.end = this.end;
		s.venue = this.venue;
		return s;
	}
	
	/**
	 * This function return the details of a slot
	 * @return The details in a String
	 */
	public String toString() {
		return DAYS[day] + start.toString() + "-" + end.toString() + ":" + venue;
	}
	
	/**
	 * This function return the hour of the starting time
	 * @return Starting hour
	 */
	public int getStartHour() {
		return start.getHour();
	}
	
	/**
	 * This function return the minute of the starting time
	 * @return Starting minute
	 */
	public int getStartMinute() {
		return start.getMinute();
	}
	
	/**
	 * This function return the hour of the ending time
	 * @return Ending hour
	 */
	public int getEndHour() {
		return end.getHour();
	}
	
	/**
	 * This function return the minute of the ending time
	 * @return Ending minute
	 */
	public int getEndMinute() {
		return end.getMinute();
	}
	
	/**
	 * This function return the starting time
	 * @return the start
	 */
	public LocalTime getStart() {
		return start;
	}
	
	/**
	 * This funtion set the starting time
	 * @param start the start to set
	 */
	public void setStart(String start) {
		this.start = LocalTime.parse(start, DateTimeFormatter.ofPattern("hh:mma", Locale.US));
	}
	
	/**
	 * This function return the ending time
	 * @return the end
	 */
	public LocalTime getEnd() {
		return end;
	}
	
	/**
	 * This function set the ending time
	 * @param end the end to set
	 */
	public void setEnd(String end) {
		this.end = LocalTime.parse(end, DateTimeFormatter.ofPattern("hh:mma", Locale.US));
	}
	
	/**
	 * This function return the venue
	 * @return the venue
	 */
	public String getVenue() {
		return venue;
	}
	
	/**
	 * This function set the venue
	 * @param venue the venue to set
	 */
	public void setVenue(String venue) {
		this.venue = venue;
	}

	/**
	 * This function get the day
	 * @return the day
	 */
	public int getDay() {
		return day;
	}
	
	/**
	 * This function set the day
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}
}
