public class Output {
    private static int month = 1;
    private static int day = 0;

    public static void updateTime() {
        if (month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12) {
            if (day == 31) {
                day = 1;
                month++;
            } else {
                day++;
            }
        } else if (month == 2) {
            if (day == 28) {
                day = 1;
                month++;
            } else {
                day++;
            }
        } else {
            if (day == 30) {
                day = 1;
                month++;
            } else {
                day++;
            }
        }
    }

    public static int getMonth() {
        return month;
    }

    public static int getDay() {
        return day;
    }
}

