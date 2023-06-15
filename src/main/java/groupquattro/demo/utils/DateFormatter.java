package groupquattro.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy, HH:mm");

    public static Date formatDatePersonalized(Date d){
        String dateS = sdf.format(d);
        Date formattedDate = d;
        try {
            formattedDate = sdf.parse(dateS);
        } catch (ParseException e) {
            throw new RuntimeException();
        }
        return formattedDate;
    }
}
