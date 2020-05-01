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
            matchedFilters.add(filterString);
        }
    }


    /**
     * Check if the labTut filter exist, whether the given section is valid
     * @param section the section to be checked
     * @return true if section can satisfy the labTut filter if it is applied
     */
    private static boolean matchLabTut(Section section) {
        String sectionCode = section.getCode();
        char first = sectionCode.charAt(0);
        char second = sectionCode.charAt(1);

        if ((first == 'T') || ((first == 'L') && second == 'A')) {
            return true;
        }
        return false;

    }

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
     * Check if the given course contains sections that fulfills the filters
     * @param course the course to be checked
     * @return true if the course contains sections that fulfills the filters
     */
    private static boolean filterSections(Course course) {
        List<Section> filteredSections = Collections.emptyList();

        for (Section section : course.getSections()) {
            /**
             * TODO
             * Filters labs or tutorial
             */

            final boolean haveLabTutFilter = filters.get(LABTUT);
            final boolean matchLabTutFilter = matchLabTut(section);
            final boolean containsSlots = filterSlots(section);

            // CNF
            if ((!haveLabTutFilter || matchLabTutFilter) && containsSlots) {
                filteredSections.add(section);
            }
            
        }
        if (!filteredSections.isEmpty()) {
            course.setSections(filteredSections);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Filter the course according to the selected filters
     * @param courses a list of unfiltered courses
     * @return a list of filtered courses
     */
    public static List<Course> filterCourses(List<Course> courses) {
        unfilteredCourses = courses;

        if (!haveFilters()) {
            filteredCourses = unfilteredCourses;
        } else {
            /**
             * 3 layers filtering:
            *       1. Course
            *       2. Course.sections
            *       3. Course.sections.slots
            *  This is the layer of filtering out courses
            */

            for (Course course : unfilteredCourses) {
                // Filters exclusion and CC
                final boolean haveCCFilter = filters.get(CC);
                final boolean matchCCFilter = course.getCC();

                final boolean haveExFilter = filters.get(NOEX);
                final boolean matchExFilter = !(course.getExclusion().isEmpty());

                final boolean containsSections = filterSections(course);

                // CNF
                if ((!haveCCFilter || matchCCFilter) && (!haveExFilter || matchExFilter) && containsSections) {
                    filteredCourses.add(course);
                }
            }
        }
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