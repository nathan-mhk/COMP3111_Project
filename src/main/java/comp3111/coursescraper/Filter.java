package comp3111.coursescraper;

import java.util.*;

/**
 * <b>Filter helper class</b><br>
 * This class is used to handle filtering only. <br>
 * Tracks the status of checked filters
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
     * Get the map of filters
     * @return The map of filters
     */
    public static Map<String, Boolean> getFilters() {
        return filters;
    }

    /**
     * Toggle all checkboxes at once
     * 
     * @return The current state of all checkboxes, true == checked, false == unchecked
     */
    static boolean toggleAll() {
        all = !all;
        filters.replaceAll((key, bool)->bool = all);
        return all;
    }

    /**
     * Toggle a checkbox
     * 
     * @param filter The text of the checkbox selected
     */
    static void check(String filter) {
        filters.replace(filter, !filters.get(filter));
    }

    /**
     * Check if any filters are applied
     * 
     * @return True if have any filters, false if none
     */
    private static boolean haveFilters() {
        return filters.containsValue(true);
    }

    /**
     * Check if any time filters are applied (am/pm)
     * 
     * @return True if contain any time filters, false if none
     */
    private static boolean haveTimeFilters() {
        return (filters.get(AM) || filters.get(PM));
    }

    /**
     * Check if any day filters are applied (mon/tue/etc)
     * 
     * @return True if contain any days filters, false if none
     */
    private static boolean haveDayFilters() {
        return (filters.get(MON) || filters.get(TUE) || filters.get(WED) || filters.get(THU) || filters.get(FRI) || filters.get(SAT));
    }

    /**
     * Get a list of applied day fitlers 
     * 
     * @return A list of applied day filters
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

    /**
     * Check if the given slots can fulfill all applied time filters.
     * 
     * @param slots Slots to be checked
     * 
     * @return True if there are slots inside the given list that 
     * together can fulfill all applied time filters
     */
    private static boolean matchTime(Vector<Slot> slots) {
        final boolean haveAM = filters.get(AM);
        final boolean havePM = filters.get(PM);

        boolean matchAM = false;
        boolean matchPM = false;

        final int middle = 12;

        for (Slot slot : slots) {
            final int startTime = slot.getStartHour();
            final int endTime = slot.getEndHour();

            if (!matchAM) {
                matchAM = (startTime < middle);
            }
            if (!matchPM) {
                matchPM = (endTime >= middle);
            }
            if (matchAM && matchPM) {
                break;
            }
        }
        // CNF: (!A+B)(A+C)(!C+D)
        return ((!haveAM || matchAM) && (haveAM || havePM) && (!havePM || matchPM));
    }

    /**
     * Check if the given slots can fulfill all applied day filters.
     * 
     * @param slots Slots to be checked
     * 
     * @return True if there are slots inside the given list that 
     * together can fulfill all applied day filters
     */
    private static boolean matchDay(Vector<Slot> slots) {
        List<String> dayFilters = getDayFilters();
        List<String> unmatchedFilters = getDayFilters();

        final int offsetStart = FILTERS_NAME.indexOf(MON);
        final int offsetEnd = FILTERS_NAME.indexOf(SAT);

        for (Slot slot : slots) {
            final int index = slot.getDay() + offsetStart;

            // In case the value get from slot.getDay() is out of bounds
            if ((index >= offsetStart) && (index <= offsetEnd)) {
                String day = FILTERS_NAME.get(index);

                if (dayFilters.contains(day)) {
                    unmatchedFilters.remove(day);
                }
            }
        }
        return unmatchedFilters.isEmpty();
    }

    /**
     * Check if the given section is a lab or tutorial
     * 
     * @param section The section to be checked
     * 
     * @return True if section is a lab or tutorial
     */
    private static boolean matchLabTut(Section section) {
        String sectionCode = section.getCode();
        char first = sectionCode.charAt(0);
        char second = sectionCode.charAt(1);

        return ((first == 'T') || ((first == 'L') && second == 'A'));
    }

    /**
     * Check if the given section contains valid slots. <br><br>
     * All slots are considered valid if they: <br>
     * 1. Contain slots that matche time filters if they are applied <br>
     * 2. Contains slots that match day filters if they are applied <br>
     * 
     * @param section The section to be checked
     * 
     * @return True if section contains valid slots or 
     * no time and day filters are applied
     */
    private static boolean filterSlots(Section section) {
        final boolean haveTimeFilters = haveTimeFilters();
        final boolean haveDayFilters = haveDayFilters();

        if (!haveTimeFilters && !haveDayFilters) {
            return true;
        } else {

            Vector<Slot> slots = new Vector<Slot>();

            for (int i = 0; i < section.getNumSlots(); ++i) {
                slots.add(section.getSlot(i));
            }

            /**
             * CNF: (!A+B)(!C+D) 
             * Added (!A!C), which is impossible as checked already, but can simplifies the expression
             */
            return ((!haveTimeFilters || matchTime(slots)) && (!haveDayFilters || matchDay(slots)));
        }
    }

    /**
     * Check if the given course contains valid sections. <br><br>
     * All sections are considered valid only if: <br>
     * 1. One of the section matches lab & tut filter if it is applied <br>
     * 2. One of the section contains valid slots <br>
     * 
     * @param course The course to be checked
     * 
     * @return True if course contains valid sections
     */
    private static boolean filterSections(Course course) {
        final boolean haveLabTutFilters = filters.get(LABTUT);

        // If no lab/tut, time and day filters
        if (!haveLabTutFilters && !haveTimeFilters() && !haveDayFilters()) {
            return true;
        } else {

            // Check if there exist a section that fulfills LT filter
            boolean matchLabTutFilters = false;

            for (int i = 0; i < course.getNumSections(); i++) {
                Section section = course.getSection(i);

                if (!matchLabTutFilters) {
                    matchLabTutFilters = matchLabTut(section);
                } else {
                    break;
                }
            }

            /**
             * Remove sections if it cannot fulfils time/day filters, 
             * even it fulfills LT filter (Required AND logic)
             */
            Vector<Section> filteredSections = new Vector<Section>();

            for (int i = 0; i < course.getNumSections(); i++) {
                Section section = course.getSection(i);

                // CNF: (!A+B)(C)
                if ((!haveLabTutFilters || matchLabTutFilters) && filterSlots(section)) {
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
    }

    /**
     * Filter the course according to the selected filters
     * 
     * @param courses A list of unfiltered courses
     * 
     * @return A list of filtered courses
     */
    public static List<Course> filterCourses(List<Course> courses) {
        if (!haveFilters()) {
            return courses;
        } else {
            Vector<Course> unfilteredCourses = new Vector<Course>();

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

            Vector<Course> filteredCourses = new Vector<Course>();
            
            for (Course course : unfilteredCourses) {
                // CNF: (!A+B)(!C+D)(E)
                if ((!filters.get(CC) || course.getCC()) && (!filters.get(NOEX) || (course.getExclusion() == null)) && filterSections(course)) {
                    filteredCourses.add(course);
                }
            }
            return filteredCourses;
        }
    }
}