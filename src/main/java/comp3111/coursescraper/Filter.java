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
     * Check if the given slot matches the time filters. 
     * Add the matching filters to machedFilters
     * 
     * @param slot the slot to be matched
     * @param matchedFilters a map that contains matched filters
     * @return true if the slot matches the time filters
     */
    private static boolean matchTime(Slot slot, List<String> matchedFilters) {
        final boolean am = filters.get(AM);
        final boolean pm = filters.get(PM);

        final int middle = 12;
        final int startTime = slot.getStartHour();
        final int endTime = slot.getEndHour();

        if ((am && pm) && ((startTime < middle) && (endTime >= middle))) {
            /**
             * Special case, one slot fulfilling 2 filters simultaneously
             */
            addMatchedFilters(matchedFilters, AM);
            addMatchedFilters(matchedFilters, PM);
            return true;
        }
        if (am && (startTime < middle)) {
            addMatchedFilters(matchedFilters, AM);
            return true;
        }
        if (pm && (startTime >= middle)) {
            addMatchedFilters(matchedFilters, PM);
            return true;
        }
        return false;        
    }

    /**
     * Check if the given slot matches the day filters. 
     * Add the matching filters to machedFilters
     * 
     * @param slot the slot to be matched
     * @param matchedFilters a map that contains matched filters
     * @return true if the slot matches the day filters
     */
    private static boolean matchDay(Slot slot, List<String> matchedFilters) {
        final int DAYS_OFFSET_START = FILTERS_NAME.indexOf(MON);
        final int DAYS_OFFSET_END = FILTERS_NAME.indexOf(SAT);

        final int index = slot.getDay() + DAYS_OFFSET_START;

        // In case the value get from slot.getDay() is out of bounds
        if ((index >= DAYS_OFFSET_START) && (index <= DAYS_OFFSET_END)) {
            String filterString = FILTERS_NAME.get(index);
            boolean matched = filters.get(filterString);

            if (matched) {
                 addMatchedFilters(matchedFilters, filterString);
                 return true;
            }
        }
        return false;
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

    /**
     * Get a map of slot fitlers (time and day filters)
     * @return a map of slot filters
     */
    private static Map<String, Boolean> getSlotFilters() {
        Map<String, Boolean> slotFilters = new HashMap<String, Boolean>();

        slotFilters.put(AM, filters.get(AM));
        slotFilters.put(PM, filters.get(PM));

        slotFilters.put(MON, filters.get(MON));
        slotFilters.put(TUE, filters.get(TUE));
        slotFilters.put(WED, filters.get(WED));
        slotFilters.put(THU, filters.get(THU));
        slotFilters.put(FRI, filters.get(FRI));
        slotFilters.put(SAT, filters.get(SAT));

        return slotFilters;
    }

    /**
     * Check if the matched filters can fulfill the corresponding type of filters set
     * @param matchedFilters A list of matched filters
     * @param type The type of the list of matched filters
     * @return true of the list of matched filters fulfills the list of applied filters of the same type
     */
    private static boolean filtersMatched(List<String> matchedFilters) {
        for (Map.Entry<String, Boolean> entry : getSlotFilters().entrySet()) {
            // Iterate until the value of current key is true, i.e. the current filter is applied
            if (entry.getValue()) {
                // If the current filter is inside matchedFilters, pop the one in matchedFilters
                if (!matchedFilters.contains(entry.getKey())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Not selecting any filter == not applying those filters 
     * "Display" sections that have ***SLOTS*** that fulfills those filters
     * (By display it means you should modify Course, Course.sections and Course.sections.slots accordingly)
     * Section::sectionCode (String, Lx = lecture, LAx = lab, Tx = tutorial)
     */

    /**
     * Check if the slots in the given section matches the applied slot filters (if any). 
     * Add the slots that fulfilled the filters to the given section
     * @param section the section to be checked
     * @return true if the given section have slots that fulfills the slot filters
     * and have been successfully added to the given section. Or there are no slot filters applied
     */
    private static boolean filterSlots(Section section) {
        List<Slot> slots = section.getSlots();

        boolean haveTimeFilters = haveTimeFilters();
        boolean haveDayFilters = haveDayFilters();

        if (!haveTimeFilters && !haveDayFilters) {
            // No time and day filters are applied
            return true;
        } else {
            List<Slot> filteredSlots = Collections.emptyList();

            // Stores slots that fulfills time/day filters
            List<Slot> tempTimeList = Collections.emptyList();
            List<Slot> tempDayList = Collections.emptyList();

            // Stores the matched filters of any slots
            List<String> matchedFilters = Collections.emptyList();

            for (Slot slot : slots) {
                if (haveTimeFilters && matchTime(slot, matchedFilters)) {
                    tempTimeList.add(slot);
                }
                if (haveDayFilters && matchDay(slot, matchedFilters)) {
                   tempDayList.add(slot);
                }
            }

            if (filtersMatched(matchedFilters)) {
                if (haveTimeFilters && haveDayFilters && (tempTimeList.size() != 0) && (tempDayList.size() != 0)) {
                    for (Slot slot : tempTimeList) {
                        if (!filteredSlots.contains(slot)) {
                            filteredSlots.add(slot);
                        }
                    }
                    for (Slot slot : tempDayList) {
                        if (!filteredSlots.contains(slot)) {
                            filteredSlots.add(slot);
                        }
                    }
                } else if (haveTimeFilters && !haveDayFilters && (tempTimeList.size() != 0)) {
                    for (Slot slot : tempTimeList) {
                        filteredSlots.add(slot);

                    }
                } else if (!haveTimeFilters && haveDayFilters && (tempDayList.size() != 0)) {
                    for (Slot slot : tempDayList) {
                        filteredSlots.add(slot);
                    }
                }
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