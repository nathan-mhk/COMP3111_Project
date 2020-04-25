package comp3111.coursescraper;

class Filter {
	static private boolean am = false;
	static private boolean pm = false;
	
	static private boolean mon = false;
	static private boolean tue = false;
	static private boolean wed = false;
	static private boolean thu = false;
	static private boolean fri = false;
	static private boolean sat = false;
	static private boolean sun = false;
	
	static private boolean cc = false;
	static private boolean noEx = false;
	
	static private boolean labTut = false;
	
	//*** Basic checkbox actions
	static void selectAll() {
		am = true;
		pm = true;
		
		mon = true;
		tue = true;
		wed = true;
		thu = true;
		fri = true;
		sat = true;
		sun = true;
		
		cc = true;
		noEx = true;
		
		labTut = true;
	}
	
	static void checkAM() {
		am = !am;
	}
	
	static void checkPM() {
		pm = !pm;
	}
	
	static void checkMon() {
		mon = !mon;
	}
	
	static void checkTue() {
		tue = !tue;
	}
	
	static void checkWed() {
		wed = !wed;
	}
	
	static void checkThu() {
		thu = !thu;
	}
	
	static void checkFri() {
		fri = !fri;
	}
	
	static void checkSat() {
		sat = !sat;
	}
	
	static void checkSun() {
		sun = !sun;
	}
	
	static void checkCC() {
		cc = !cc;
	}
	
	static void checkNoExclusion() {
		noEx = !noEx;
	}
	
	static void checkLabTutorial() {
		labTut = !labTut;
	}
	//*** Basic checkbox actions end
}
