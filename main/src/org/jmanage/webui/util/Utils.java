package org.jmanage.webui.util;

import org.apache.commons.beanutils.ConvertUtils;

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

    public static Object getTypedValue(String value, String type){


        if(type.equals("int")){
            type = "java.lang.Integer";
        }else if(type.equals("long")){
            type = "java.lang.Long";
        }else if(type.equals("short")){
            type = "java.lang.Short";
        }else if(type.equals("float")){
            type = "java.lang.Float";
        }else if(type.equals("double")){
            type = "java.lang.Double";
        }else if(type.equals("char")){
            type = "java.lang.Character";
        }else if(type.equals("boolean")){
            type = "java.lang.Boolean";
        }else if(type.equals("byte")){
            type = "java.lang.Byte";
        }

        try {
            return ConvertUtils.convert(value, Class.forName(type));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
