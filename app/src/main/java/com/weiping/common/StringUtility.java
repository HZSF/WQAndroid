package com.weiping.common;

public class StringUtility {
    public static boolean isEmptyString(String string){
        boolean isEmpty = false;
        if(string == null || "".equalsIgnoreCase(string)){
            isEmpty = true;
        }
        return isEmpty;
    }

    public static String cleanEmTagHtml(String string){
        if(string.contains("<em>")){
            string = string.replaceAll("<em>", "");
        }
        if(string.contains("</em>")){
            string = string.replaceAll("</em>","");
        }
        return string;
    }
}
