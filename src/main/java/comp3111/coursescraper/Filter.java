package comp3111.coursescraper;
/*
 * Filter class is used to handle the filtering ONLY
 * Should be singleton but I prefer all static instead
 * 
 * TODO:
 * Add a reference to a list of unfiltered class
 * Add a reference to a list of filtered class
 */
class Filter {
	private static boolean all = false;
	
	private static boolean am = false;
	private static boolean pm = false;
	
	private static boolean mon = false;
	private static boolean tue = false;
	private static boolean wed = false;
	private static boolean thu = false;
	private static boolean fri = false;
	private static boolean sat = false;
	
	private static boolean cc = false;
	private static boolean noEx = false;
	
	private static boolean labTut = false;
	
	// Singleton part, optional
	private static Filter filter = null;
	
	private Filter() {};
	
	public static Filter getInstance() {
		if (filter == null) {
			filter = new Filter();
		}
		return filter;
	}
	
	//*** Basic checkbox actions
	
	/* Toggle all checkboxes
	 * @return Return the current state of all checkboxes, true == checked, false == unchecked
	 */
	static boolean toggleAll() {
		all = !all;
		
		am = all;
		pm = all;
		
		mon = all;
		tue = all;
		wed = all;
		thu = all;
		fri = all;
		sat = all;
		
		cc = all;
		noEx = all;
		
		labTut = all;
		
		return all;
	}
	
	static void check(String filter) {
		boolean value = false;
		
		switch (filter) {
			case "AM":
				am = !am;
				value = am;
				break;
			case "PM":
				pm = !pm;
				value = pm;
				break;
			case "Monday":
				mon = !mon;
				value = mon;
				break;
			case "Tuesday":
				tue = !tue;
				value = tue;
				break;
			case "Wednesday":
				wed = !wed;
				value = wed;
				break;
			case "Thusday":
				thu = !thu;
				value = thu;
				break;
			case "Friday":
				fri = !fri;
				value = fri;
				break;
			case "Saturday":
				sat = !sat;
				value = sat;
				break;
			case "Common Core":
				cc = !cc;
				value = cc;
				break;
			case "No Exclusion":
				noEx = !noEx;
				value = noEx;
				break;
			case "With Labs or Tutorial":
				labTut = !labTut;
				value = labTut;
				break;
			default :
				break;
		}
	}
	//*** Basic checkbox actions end
}
