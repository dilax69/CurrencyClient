package pl.parser.nbp.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author krzykrucz.
 *         <p>
 *         <p>
 *         TableFileTemplate format: c005z150109.xml
 */

public class TableFileTemplate {

    private String fullName;
    private String name;
    private String ext;
    private LocalDate date;
    private char type;

    public TableFileTemplate(String name, String ext) {
        this.name = name;
        this.ext = ext;
        this.fullName = name + "." + ext;
        this.date = parseDateFromName();
        this.type = parseTypeFromName();
    }

    public String getFullName() {
        return fullName;
    }

    public String getName() {
        return name;
    }

    public String getExt() {
        return ext;
    }

    public LocalDate getDate() {
        return date;
    }

    public char getType() {
        return type;
    }

    private LocalDate parseDateFromName() {
        final int dateLength = 6;
        final int length = name.length();
        String date = name
                .substring(length - dateLength);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        return LocalDate.parse(date, formatter);
    }

    private char parseTypeFromName() {
        return name.charAt(0);
    }

}
