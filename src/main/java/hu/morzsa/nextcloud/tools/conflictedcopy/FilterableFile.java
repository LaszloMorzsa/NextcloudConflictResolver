package hu.morzsa.nextcloud.tools.conflictedcopy;

import java.io.File;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FilterableFile extends File {

    private boolean isConflictedFile = false;
    private LocalDate date = null;
    private LocalTime time = null;
    private LocalDateTime dateTime = null;
    private String originalName = null;

    public FilterableFile(String pathname) {
        super(pathname);
        isConflictedFile = detectHasConflictInName();
        parseConflictData();
    }

    public FilterableFile(String parent, String child) {
        super(parent, child);
        isConflictedFile = detectHasConflictInName();
        parseConflictData();
    }

    public FilterableFile(File parent, String child) {
        super(parent, child);
        isConflictedFile = detectHasConflictInName();
        parseConflictData();
    }

    public FilterableFile(URI uri) {
        super(uri);
        isConflictedFile = detectHasConflictInName();
        parseConflictData();
    }

    public boolean isConflictedFile() {
        return isConflictedFile;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getOriginalName() {
        return originalName;
    }

    public LocalTime getTime() {
        return time;
    }

    private boolean detectHasConflictInName(){
        return getName().matches(".+ \\(conflicted copy [0-9]{4}\\-[0-9]{2}\\-[0-9]{2} [0-9]{5,7}\\).*");
    }

    public void parseConflictData(){
        if (isConflictedFile) {
            String startText = "(conflicted copy ";
            int start = getName().lastIndexOf(startText) + startText.length();
            int end = getName().lastIndexOf(")");
            String value = getName().substring(start, end);
            if (value.contains(" ")){
                String[] parts = value.split(" ");
                try {
                    date = LocalDate.parse(parts[0]);
                    time = LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("HHmmss"));
                    dateTime = date.atTime(time);
                }catch (Exception e){
                    System.out.println("Can't parse date and time from conflicted file's name: "+getName());
                }
            }
            originalName = getName().substring(0, start-(startText.length()+1));
            if (getName().length() > end+1){
                originalName += getName().substring(end+1);
            }
        }else{
            originalName = getName();
        }
    }


}
