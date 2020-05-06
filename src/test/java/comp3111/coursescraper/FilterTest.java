package comp3111.coursescraper;

import java.util.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FilterTest {
    private final String AM = "AM";
    private final String PM = "PM";
    private final String MON = "Monday";
    private final String TUE = "Tuesday";
    private final String WED = "Wednesday";
    private final String THU = "Thursday";
    private final String FRI = "Friday";
    private final String SAT = "Saturday";
    private final String CC = "Common Core";
    private final String NOEX = "No Exclusion";
    private final String LABTUT = "With Labs or Tutorial";

    private final List<String> FILTERS_NAME = Arrays.asList(AM, PM, MON, TUE, WED, THU, FRI, SAT, CC, NOEX, LABTUT);
    
    private Map<String, Boolean> filters;

    List<Course> result = new Vector<Course>();
    
    Vector<Course> courses = new Vector<Course>();

    // Get the latest states of all filters
    private void updateFilters() {
        filters = Filter.getFilters();
    }

    @Before
    public void setUp() {
        // Create special course
        Course c = new Course();
        c.setTitle("AAAA 0000 - Foobar (0 units)");
        c.setDescription("Foobar");
        c.setExclusion(null);
        c.setCC(true);

        // Create special sections
        Section le = new Section();
        le.setID(9999);
        le.setCode("L0");
        le.setInstructor("Foobar");

        Section la = new Section();
        la.setID(9999);
        la.setCode("LA0");
        la.setInstructor("Foobar");

        Section tu = new Section();
        tu.setID(9999);
        tu.setCode("T0");
        tu.setInstructor("Foobar");

        // Create special slots
        final String start = "11:00AM";
        final String end = "01:00PM";

        Slot mon = new Slot();
        mon.setDay(0);
        mon.setVenue("Foobar");

        Slot tue = new Slot();
        tue.setDay(1);
        tue.setVenue("Foobar");

        Slot wed = new Slot();
        wed.setDay(2);
        wed.setVenue("Foobar");

        // Variations for lec
        mon.setStart(start);
        mon.setEnd(start);
        tue.setStart(end);
        tue.setEnd(end);
        wed.setStart(start);
        wed.setEnd(end);

        le.addSlot(mon);
        le.addSlot(tue);
        le.addSlot(wed);

        // Variations for lab
        tue.setStart(start);
        tue.setEnd(end);
        wed.setStart(end);
        wed.setEnd(end);
        mon.setStart(start);
        mon.setEnd(end);

        la.addSlot(mon);
        la.addSlot(tue);
        la.addSlot(wed);

        // Varations for tut
        wed.setStart(start);
        wed.setEnd(end);
        mon.setStart(end);
        mon.setEnd(end);
        tue.setStart(start);
        tue.setEnd(end);

        tu.addSlot(mon);
        tu.addSlot(tue);
        tu.addSlot(wed);

        c.addSection(le);
        c.addSection(la);
        c.addSection(tu);

        courses.add(c);
        
        result.add(c);
    }

    @Test
    public void testToggleAll() {
        assertTrue(Filter.toggleAll());

        updateFilters();

        for (String entry : FILTERS_NAME) {
            assertTrue(filters.get(entry));
        }

        assertFalse(Filter.toggleAll());

        updateFilters();

        for (String entry : FILTERS_NAME) {
            assertFalse(filters.get(entry));
        }
    }

    @Test
    public void testCheck() {
        Random rnd = new Random();
        final int index = rnd.nextInt(FILTERS_NAME.size());
        final String entry = FILTERS_NAME.get(index);

        updateFilters();
        final boolean oldVal = filters.get(entry);

        Filter.check(entry);

        updateFilters();
        final boolean newVal = filters.get(entry);

        assertEquals(newVal, !oldVal);
    }

    @Test
    public void testAllFilters() {
         // All filters
         assertTrue(Filter.toggleAll());
         assertTrue(Filter.filterCourses(courses).isEmpty());
 
         // No filters
         assertFalse(Filter.toggleAll());
         assertTrue(result.equals(Filter.filterCourses(courses)));
    }

    @Test
    public void testSingleFilter() {
        Filter.check(CC);
        assertTrue(result.equals(Filter.filterCourses(courses)));
        Filter.check(CC);

        Filter.check(NOEX);
        assertTrue(result.equals(Filter.filterCourses(courses)));
        Filter.check(NOEX);

        Filter.check(LABTUT);
        assertTrue(result.equals(Filter.filterCourses(courses)));
        Filter.check(LABTUT);

        Filter.check(AM);
        assertTrue(result.equals(Filter.filterCourses(courses)));
        Filter.check(AM);

        Filter.check(PM);
        assertTrue(result.equals(Filter.filterCourses(courses)));
        Filter.check(PM);

        Filter.check(MON);
        assertTrue(result.equals(Filter.filterCourses(courses)));
        Filter.check(MON);

        Filter.check(TUE);
        assertTrue(result.equals(Filter.filterCourses(courses)));
        Filter.check(TUE);

        Filter.check(WED);
        assertTrue(result.equals(Filter.filterCourses(courses)));
        Filter.check(WED);

        Filter.check(THU);
        assertTrue(Filter.filterCourses(courses).isEmpty());
        Filter.check(THU);

        Filter.check(FRI);
        assertTrue(Filter.filterCourses(courses).isEmpty());
        Filter.check(FRI);
        
        Filter.check(SAT);
        assertTrue(Filter.filterCourses(courses).isEmpty());
        Filter.check(SAT);
    }

    @Test
    public void testMultiFilters() {
        Filter.check(AM);
        Filter.check(PM);
        assertTrue(result.equals(Filter.filterCourses(courses)));
    }
}
