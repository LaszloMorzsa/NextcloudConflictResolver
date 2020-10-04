package hu.morzsa.nextcloud.tools.conflictedcopy;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class FilterableFileTest {

    @Test
    void isConflictedFile() {
        FilterableFile filterableFile = new FilterableFile("c:\\Java\\Nextcloud-tools\\tstFolder\\65944372 (conflicted copy 2020-09-03 134240).db");
        assertTrue(filterableFile.isConflictedFile());
        filterableFile = new FilterableFile("c:\\Java\\Nextcloud-tools\\tstFolder\\cheque.cfg");
        assertFalse(filterableFile.isConflictedFile());
    }

    @Test
    void getDate() {
        FilterableFile filterableFile = new FilterableFile("c:\\Java\\Nextcloud-tools\\tstFolder\\65944372 (conflicted copy 2020-09-03 134240).db");
        assertEquals(LocalDate.parse("2020-09-03"), filterableFile.getDate());
    }

    @Test
    void getOriginalName() {
        FilterableFile filterableFile = new FilterableFile("c:\\Java\\Nextcloud-tools\\tstFolder\\65944372 (conflicted copy 2020-09-03 134240).db");
        assertEquals("65944372.db", filterableFile.getOriginalName());
    }

    @Test
    void getTime() {
        FilterableFile filterableFile = new FilterableFile("c:\\Java\\Nextcloud-tools\\tstFolder\\65944372 (conflicted copy 2020-09-03 134240).db");
        assertEquals(LocalTime.parse("134240", DateTimeFormatter.ofPattern("HHmmss")), filterableFile.getTime());
    }
}