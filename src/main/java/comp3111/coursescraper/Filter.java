package comp3111.coursescraper;

import java.util.*;

/**
 * <b>Filter helper class</b><br>
 * This class is used to handle the filtering only. <br>
 * 
 * @author Nathan
 */
class Filter {
    private static final String AM = "AM";
    private static final String PM = "PM";
    private static final String MON = "Monday";
    private static final String TUE = "Tuesday";
    private static final String WED = "Wednesday";
    private static final String THU = "Thursday";
    private static final String FRI = "Friday";
    private static final String SAT = "Saturday";
    private static final String CC = "Common Core";
    private static final String NOEX = "No Exclusion";
    private static final String LABTUT = "With Labs or Tutorial";

    private static final List<String> FILTERS_NAME = Arrays.asList(AM, PM, MON, TUE, WED, THU, FRI, SAT, CC, NOEX, LABTUT);

    private static Map<String, Boolean> filters = new HashMap<String, Boolean>();
    static {
        for (int i = 0; i < FILTERS_NAME.size(); ++i) {
            filters.put(FILTERS_NAME.get(i), false);
        }
    }

    private static boolean all = false;

    private static List<Course> unfilteredCourses = Collections.emptyList();
    private static List<Course> filteredCourses = Collections.emptyList();

    private static final String SLOT = "SLOT";

    /**
     * Toggle all checkboxes at once
     * 
     * @return the current state of all checkboxes, true == checked, false == unchecked
     */
    static boolean toggleAll() {
        all = !all;
        filters.replaceAll((key, bool)->bool = all);
        return all;
    }

    /**
     * Toggle a checkbox
     * 
     * @param filter the text of the checkbox
     */
    static void check(String filter) {
        filters.replace(filter, !filters.get(filter));
    }

    /**
     * Check if any filters are applied
     * 
     * @return true if contain any filters, false if contain 0 filters
     */
    private static boolean haveFilters() {
        return filters.containsValue(true);
    }

    /**
     * Check if any time filters are applied (am/pm)
     * 
     * @return true if contain any time filters, false if none
     */
    private static boolean haveTimeFilters() {
        return (filters.get(AM) || filters.get(PM));
    }

    /**
     * Check if any day filters are applied (mon/tue/etc)
     * 
     * @return true if contain any days filters, false if none
     */
    private static boolean haveDayFilters() {
        return (filters.get(MON) || filters.get(TUE) || filters.get(WED) || filters.get(THU) || filters.get(FRI) || filters.get(SAT));
    }

    private static void addMatchedFilters(List<String> matchedFilters, String filterString) {
        if (!matchedFilters.contains(filterString)) {

            // Stores the matched filters of any slots
            List<String> matchedFilters = Collections.emptyList();

            for (Slot slot : slots) {
                if (haveTimeFilters && matchTime(slot, matchedFilters)) {

            if (filtersMatched(matchedFilters, SLOT)) {

                    }

            if (!filteredSlots.isEmpty()) {
                section.setSlots(filteredSlots);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Not selecting any filter == not applying those filters "Display" sections
     * that have SLOTS that fulfills those filters (By display it means you should
     * modify Course, Course.sections and Course.sections.slots accordingly)
     */

    /**
     * 
     * @param courses a list of unfiltered courses
     * @return a list of filtered courses
     */
    public static List<Course> filterCourses(List<Course> courses) {
        unfilteredCourses = courses;

        /**
         * TODO: Filter courses Test1
         */

        return filteredCourses;
    }

    public static void reset() {
        unfilteredCourses = Collections.emptyList();
        filteredCourses = Collections.emptyList();
    }

    public static String getDebugMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        
        for (Map.Entry<String, Boolean> entry : filters.entrySet()) {
            stringBuilder.append(entry.getKey()).append(":\t\t").append(entry.getValue()).append("\n");
        }
        stringBuilder.append("haveFilters:\t\t").append(haveFilters()).append("\n");
        stringBuilder.append("haveTimeFilters:\t\t").append(haveTimeFilters()).append("\n");
        stringBuilder.append("haveDayFilters:\t\t").append(haveDayFilters()).append("\n");

        return stringBuilder.toString();
    }
}