package org.jmanage.webui.util;

import java.util.StringTokenizer;

/**
 *
 * date:  Jun 20, 2004
 * @author	Rakesh Kalra
 */
public class Utils {

    /**
     * Appends param and value to the given url.
     *
     * @param url
     * @param param
     * @param value
     * @return
     */
    public static String appendURLParam(String url, String param, String value){

        StringBuffer urlString = new StringBuffer(url);
        if (url.indexOf("?") == -1) {
            urlString.append("?");
        }else if(url.endsWith("&") == false) {
            urlString.append("&");
        }
        urlString.append(param);
        urlString.append("=");
        urlString.append(value);
        return urlString.toString();
    }

    /**
     * Converts a csv to String[]
     *
     * @param csv
     * @return
     */
    public static String[] csvToStringArray(String csv){

        if(csv == null){
            return null;
        }
        StringTokenizer tokenizer = new StringTokenizer(csv, ",");
        String[] array = new String[tokenizer.countTokens()];
        int index = 0;
        while(tokenizer.hasMoreTokens()){
            array[index ++] = tokenizer.nextToken().trim();
        }
        return array;
    }
}
