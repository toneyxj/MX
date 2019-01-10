package com.mx.teacher.util;

/**
 * Created by Archer on 16/9/20.
 */
public class StringUtils {
    public static String insertString(String srcStr, String descStr, int off) {
        StringBuilder sb = new StringBuilder();
        sb.append(descStr.substring(0, off));
        sb.append(srcStr);
        sb.append(descStr.substring(off));
        return sb.toString();
    }
}
