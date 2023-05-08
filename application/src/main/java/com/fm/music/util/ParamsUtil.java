package com.fm.music.util;

public class ParamsUtil {

    public static Object updateEntityValue(Object oldValue, Object newValue) {
        return newValue != null ? newValue : oldValue;
    }
}
