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

    /**
     * Get a list of applied day fitlers 
     * @return a list of applied day filters
     */
    private static Vector<String> getDayFilters() {
        Vector<String> dayFilters = new Vector<String>();

        if (filters.get(MON)) {
            dayFilters.add(MON);
        }
        if (filters.get(TUE)) {
            dayFilters.add(TUE);
        }
        if (filters.get(WED)) {
            dayFilters.add(WED);
        }
        if (filters.get(THU)) {
            dayFilters.add(THU);
        }
        if (filters.get(FRI)) {
            dayFilters.add(FRI);
        }
        if (filters.get(SAT)) {
            dayFilters.add(SAT);
        }

        return dayFilters;
    }

    private static boolean matchTime(Vector<Slot> slots) {
        // If the given slots contains slots such that all filters are matched
        // Remove those slots that can't
        
        final boolean haveAM = filters.get(AM);
        final boolean havePM = filters.get(PM);

        for (Iterator<Slot> itr = slots.iterator(); itr.hasNext();) {
            Slot slot = itr.next();

            final int middle = 12;
            final int startTime = slot.getStartHour();
            // final int endTime = slot.getEndHour();

            final boolean matchAM = (startTime < middle);
            final boolean matchPM = (startTime >= middle);

            // CNF
            if ((!haveAM || !matchAM) && (!havePM || !matchPM)) {
                itr.remove();
            }
        }
        return (slots.size() != 0);
    }

    private static boolean matchDay(Vector<Slot> slots) {
        // If the given slots contains slots that can fulfill all
        // Remove the slot that can't

        List<String> dayFilters = getDayFilters();

        final int DAYS_OFFSET_START = FILTERS_NAME.indexOf(MON);
        final int DAYS_OFFSET_END = FILTERS_NAME.indexOf(SAT);

        for (Iterator<Slot> itr = slots.iterator(); itr.hasNext();) {
            Slot slot = itr.next();

            final int index = slot.getDay() + DAYS_OFFSET_START;

            // In case the value get from slot.getDay() is out of bounds
            if ((index >= DAYS_OFFSET_START) && (index <= DAYS_OFFSET_END)) {

                String day = FILTERS_NAME.get(index);
                if (dayFilters.contains(day)) {
                    dayFilters.remove(day);
                } else {
                    itr.remove();
                }
            }
        }
        return ((dayFilters.size() == 0) && (slots.size() != 0));
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
     * Not selecting any filter == not applying those filters 
     * "Display" sections that have ***SLOTS*** that fulfills those filters
     * (By display it means you should modify Course, Course.sections and Course.sections.slots accordingly)
     * Section::sectionCode (String, Lx = lecture, LAx = lab, Tx = tutorial)
     */

    /**
     * Check if the given section contains valid slots. <br><br>
     * A slot is valid if it: <br>
     * 1. Matches time filters if they are applied <br>
     * 2. Matches day filters if they are applied <br>
     * 
     * @param section the section to be checked
     * @return true if section contains valid sections
     */
    private static boolean filterSlots(Section section) {
        final boolean haveTimeFilters = haveTimeFilters();
        final boolean haveDayFilters = haveDayFilters();

        if (!haveTimeFilters && !haveDayFilters) {
            // No time and day filters are applied
            return true;
        } else {
            Vector<Slot> slots = new Vector<Slot>();

            for (int i = 0; i < section.getNumSlots(); ++i) {
                slots.add(section.getSlot(i));
            }

            /**
             * CNF, added a duplicated case which will never happened, 
             * as checked already, but simplifies the expression
             */
            if ((!haveTimeFilters || matchTime(slots)) && (!haveDayFilters || matchDay(slots))) {
                section.setSlots(slots);
                section.setNumSlots(slots.size());
                return true;
            }
            return false;
        }
    }

    /**
     * Check if the given course contains valid sections. <br><br>
     * A section is considered valid only if it: <br>
     * 1. Matches lab & tut filter if it is applied <br>
     * 2. Contains valid slots <br>
     * 
     * @param course the course to be checked
     * @return true if course contains valid sections
     */
    private static boolean filterSections(Course course) {
        Vector<Section> filteredSections = new Vector<Section>();

        for (int i = 0; i < course.getNumSections(); i++) {
            Section section = course.getSection(i);

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
            course.setNumSections(filteredSections.size());
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
        if (!haveFilters()) {
            return courses;
        } else {
            Vector<Course> unfilteredCourses = new Vector<Course>();
            Vector<Course> filteredCourses = new Vector<Course>();

            // Clone the whole given list of course (will also clone sections and slots)
            for (Course course : courses) {
                unfilteredCourses.add(course.clone());
            }
            /**
             * 3 layers filtering:
             * 1. Course
             * 2. Course.sections
             * 3. Course.sections.slots
             * 
             * This is the layer of filtering out courses
             * A course is considered valid only if it:
             * 1. Matches CC filter if it is applied
             * 2. Matches no exclusion filter if it is applied
             * 3. Contains valid sections
             */

            for (Course course : unfilteredCourses) {
                // Filters CC and no exclusion
                final boolean haveCCFilter = filters.get(CC);
                final boolean matchCCFilter = course.getCC();

                final boolean haveExFilter = filters.get(NOEX);
                final boolean matchExFilter = (course.getExclusion() != null);

                final boolean containsSections = filterSections(course);

                // CNF
                if ((!haveCCFilter || matchCCFilter) && (!haveExFilter || matchExFilter) && containsSections) {
                    filteredCourses.add(course);
                }
            }
            return filteredCourses;
        }
    }

    /**
     * Get a debug message which contains the information of all filterse
     * @return information of all filters
     */
    public static String getDebugMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        
        for (Map.Entry<String, Boolean> entry : filters.entrySet()) {
            stringBuilder.append(entry.getKey()).append(":\t\t").append(entry.getValue()).append("\n");
        }
        return stringBuilder.toString();
    }
}