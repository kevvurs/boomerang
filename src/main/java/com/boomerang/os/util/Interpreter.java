package com.boomerang.os.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Interpreter {
	public static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd-MMM-yy");
	public static final String FAIL = "INVALID";
	
	// Flat interpretation.
	public static List<List<String>> delimit(List<String> rows) {
		return rows.stream()
                    .map(row -> {
                        String[] cells = row.split(",");
                        cells[0] = hashDate(cells[0]);
                        List<String> entry = Arrays.asList(cells);
                        return entry;
                    })
                    .filter(row -> !row.get(0).equals(FAIL))
                    .filter(row -> Integer.valueOf(row.get(0)) >= 0 || Integer.valueOf(row.get(0)) < 366)
                    .sorted(new RowComparator())
                    .collect(Collectors.toList());
	}
	
    private static String hashDate(String dateStr) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        // calendar.roll(Calendar.YEAR, false);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = calendar.getTime();
        Date date;
        try {
                date = FORMAT.parse(dateStr);
        } catch (ParseException e) {
                return FAIL;
        }
        long millis = today.getTime() - date.getTime();
        long serialDay = TimeUnit.DAYS.convert(millis, TimeUnit.MILLISECONDS);
        return String.valueOf((int)serialDay);
    }
    
    public static double recentQuote(List<String> table) {
        try {
            double q = Double.valueOf(table.get(0).split(",")[4]);
            return q;
        } catch (NumberFormatException e) {
            return 0d;
        }
    }
}
