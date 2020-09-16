package com.queryflow.key.sequence;

import java.util.Calendar;

@Deprecated
public class SimpleSequence implements Sequence<String> {

    private Calendar latest;
    private int serial = 0;

    @Override
    public synchronized String next() {
        Calendar now;
        while (true) {
            now = Calendar.getInstance();
            if (!now.equals(this.latest)) {
                serial = 0;
                latest = now;
                break;
            } else {
                if (serial >= 255) {
                    continue;
                }
                serial++;
            }
        }
        long year = ((long) now.get(Calendar.YEAR)) << 36;
        long month = ((long) now.get(Calendar.MONTH) + 1) << 32;
        long day = ((long) now.get(Calendar.DATE)) << 27;
        long hour = ((long) now.get(Calendar.HOUR_OF_DAY)) << 22;
        long minute = ((long) now.get(Calendar.MINUTE)) << 16;
        long second = ((long) now.get(Calendar.SECOND)) << 10;
        long ms = now.get(Calendar.MILLISECOND);
        long temp = year | month | day | hour | minute | second | ms;
        String serialHex = Integer.toHexString(serial);
        serialHex = stringPad(serialHex, 2);
        String id = Long.toHexString(temp) + serialHex;
        id = stringPad(id, 14);
        return id;
    }

    private String stringPad(String src, int len) {
        int length = src.length();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < len - length; i++) {
            result.append("0");
        }
        result.append(src);
        return result.toString();
    }

}
