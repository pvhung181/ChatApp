package vn.pvhung.appchat.helpers;

public class DateHelper {
    public final static int DD_MM_YYYY = 1;

    /**
     * s: date string
     * regex : separator of date in s string
     * separator : expected separator in format date
     * format : Constant format value
     * **/
    public static String formatDate(String s, String regex, String separator ,int format) {
        if(format == 1) {
            String[] v = s.split(regex);
            return paddingStartZero(v[0]) + separator + paddingStartZero(v[1]) + separator + paddingStartZero(v[2]);
        }
        return s;
    }

    private static String paddingStartZero(String v) {
        return v.length() == 1 ? "0" + v : v;
    }
}
