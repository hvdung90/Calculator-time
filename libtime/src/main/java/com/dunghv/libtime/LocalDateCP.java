package com.dunghv.libtime;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class LocalDateCP {
    /**
     * The year.
     */
    private final int year;
    /**
     * The month-of-year.
     */
    private final int month;
    /**
     * The day-of-month.
     */
    private final int day;
    /**
     * The minute-of-hour.
     */
    private final int minute;
    /**
     * The hour-of-day.
     */
    private final int hour;
    private static final int DAYS_PER_CYCLE = 146097;
    static final long DAYS_0000_TO_1970 = (DAYS_PER_CYCLE * 5L) - (30L * 365L + 7L);

    public static void main(String[] args) {
        long utc = Calendar.getInstance().getTimeInMillis();
        long timetemp = 1598947768000l;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:00", Locale.getDefault());
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            String start = formatter.format(utc);
            String end = formatter.format(timetemp);

            System.out.println("date start " + start);
            System.out.println("date end " + end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CountDate d = LocalDateCP.between(timetemp);
        System.out.println(String.format("Years : %d Years, \nMonths : %d Months, \nDays : %d Days, ",
                d.year, d.month, d.day)
        );

    }

    /*
     * Calculates the time from the selected date to the current date with timestamp
     * */
    public static CountDate between(long time) {
        Calendar calendarCurrent = Calendar.getInstance();
        calendarCurrent.setTimeZone(TimeZone.getTimeZone("GMT"));

        LocalDateCP start = new LocalDateCP(calendarCurrent.getTimeInMillis());
        LocalDateCP end = new LocalDateCP(time);
        return start.until(end);
    }

    public static LocalDateCP parse(String text, String formatter) {
        Objects.requireNonNull(formatter, "formatter");
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(formatter);
        dateTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        try {
            date = dateTimeFormatter.parse(text);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new LocalDateCP(date);
    }

    public LocalDateCP(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = 0;
        this.minute = 0;
    }

    public LocalDateCP(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public LocalDateCP(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
    }

    public LocalDateCP(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
    }

    public long toEpochDay() {
        long y = year;
        long m = month;
        long total = 0;
        total += 365 * y;
        if (y >= 0) {
            total += (y + 3) / 4 - (y + 99) / 100 + (y + 399) / 400;
        } else {
            total -= y / -4 - y / -100 + y / -400;
        }
        total += ((367 * m - 362) / 12);
        total += day - 1;
        if (m > 2) {
            total--;
            if (isLeapYear(this.year) == false) {
                total--;
            }
        }
        return total - DAYS_0000_TO_1970;
    }

    public boolean isLeapYear(long prolepticYear) {
        return ((prolepticYear & 3) == 0) && ((prolepticYear % 100) != 0 || (prolepticYear % 400) == 0);
    }

    private long getProlepticMonth() {
        return (year * 12L + month - 1);
    }


    public CountDate until(LocalDateCP end) {
        long totalMonths = end.getProlepticMonth() - this.getProlepticMonth();  // safe
        int days = end.day - this.day;
        if (totalMonths > 0 && days < 0) {
            totalMonths--;
            LocalDateCP calcDate = this.plusMonths(totalMonths);
            days = (int) (end.toEpochDay() - calcDate.toEpochDay());  // safe
        } else if (totalMonths < 0 && days > 0) {
            totalMonths++;
            days -= end.lengthOfMonth();
        }
        CountDate temp = caculatorTimeHourMinute(end.hour, end.minute);

        long years = totalMonths / 12;  // safe
        int months = (int) (totalMonths % 12);  // safe
        if (temp.day != 0) {
            if (years <= 0 && months <= 0 && days == 1) {
                //TODO meo lam gi
            } else {
                days--;
                if (days < 0) {
                    if (months > 0) {
                        months--;
                        if (months < 0) {
                            years--;
                            months = 11;
                        }
                    } else if (months == 0 && years > 0) {
                        years--;
                        months = 11;
                    }
                    LocalDateCP calcDate = this.plusMonths(months);
                    days = (int) (end.toEpochDay() - calcDate.toEpochDay()) - 1;
                }
            }
        }


        if (years < 0) {
            years = 0;
        }
        if (months < 0) {
            months = 0;
        }
        if (days < 0) {
            days = 0;
        }
        return new CountDate(temp.minute, temp.hour, days, months, (int) years);
    }

    public int lengthOfMonth() {
        switch (month) {
            case 2:
                return (year % 4 == 0 ? 29 : 28);
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }

    private static LocalDateCP resolvePreviousValid(int year, int month, int day) {
        switch (month) {
            case 2:
                day = Math.min(day, (year % 4 == 0 ? 29 : 28));
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                day = Math.min(day, 30);
                break;
        }
        return new LocalDateCP(year, month, day);
    }

    public LocalDateCP plusMonths(long monthsToAdd) {
        if (monthsToAdd == 0) {
            return this;
        }
        long monthCount = year * 12L + (month - 1);
        long calcMonths = monthCount + monthsToAdd;  // safe overflow
        int newYear = (int) floorDiv(calcMonths, (long) 12);
        int newMonth = (int) floorMod(calcMonths, 12) + 1;
        return resolvePreviousValid(newYear, newMonth, day);
    }

    public static long floorDiv(long x, long y) {
        long r = x / y;
        // if the signs are different and modulo not zero, round down
        if ((x ^ y) < 0 && (r * y != x)) {
            r--;
        }
        return r;
    }

    CountDate caculatorTimeHourMinute(int hour, int minute) {
        CountDate count = new CountDate();
        int minuteStar = 60 - this.minute;
        int minuteEnd = minuteStar + minute;
        count.minute = minuteEnd;
        int hourTemp = 0;

        if (minuteEnd < 60) {
            count.minute = minuteEnd;
            hourTemp = 0;
        } else {
            count.minute = 0;
            hourTemp = 1;
        }
        //   int hourStart = 24 - this.hour;
        int hourEnd = hour + hourTemp;
        if (hourTemp > 0 && hourEnd < 60) {
            count.hour = hourEnd;
        }
        if (hourEnd >= 24) {
            count.day = 0;
        } else {
            count.day = -1;
        }
        return count;
    }

    public static long floorMod(long x, long y) {
        return x - floorDiv(x, y) * y;
    }

    public class CountDate {
        public int minute;
        public int hour;
        public int day;
        public int month;
        public int year;

        public CountDate() {

        }

        public CountDate(int day, int month, int year) {
            this.day = day;
            this.month = month;
            this.year = year;
        }

        public CountDate(int minute, int hour, int day, int month, int year) {
            this.minute = minute;
            this.hour = hour;
            this.day = day;
            this.month = month;
            this.year = year;
        }
    }
}

