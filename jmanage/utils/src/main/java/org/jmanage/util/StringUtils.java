/**
 * Copyright 2004-2005 jManage.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmanage.util;

import java.util.*;
import java.lang.reflect.Array;

/**
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
public class StringUtils {

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

    /**
     * Converts a String[] to comma delimited String
     */
    public static String stringArrayToCSV(String[] array){
        StringBuffer buff = new StringBuffer();
        for(int i=0; array != null && i<array.length; i++){
            buff.append(array[i]);
            if(i < array.length){
                buff.append(",");
            }
        }
        return buff.toString();
    }

    public static String[] listToStringArray(List list){
        String[] output = new String[list.size()];
        int i=0;
        for(Iterator it=list.iterator(); it.hasNext(); i++){
            output[i] = it.next().toString();
        }
        return output;
    }

    public static List csvToList(String csv) {
        if(csv == null){
            return null;
        }
        StringTokenizer tokenizer = new StringTokenizer(csv, ",");
        List list = new ArrayList(tokenizer.countTokens());
        while(tokenizer.hasMoreTokens()){
            list.add(tokenizer.nextToken().trim());
        }
        return list;
    }

    /**
     *
     * @param obj
     * @param listDelim delimiter to be used for lists, arrays, etc.
     * @return
     */
    public static String toString(Object obj, String listDelim){
        return toString(obj, listDelim, false);
    }

    /**
     *
     * @param obj
     * @param listDelim
     * @param htmlEscape    indicates if the string should be HTML escaped
     * @return
     */
    public static String toString(Object obj,
                                  String listDelim,
                                  boolean htmlEscape){
        if(obj == null){
            return "null";
        }
        if(obj.getClass().isArray()){
            return arrayToString(obj, listDelim);
        }
        String output = obj.toString();
        if(htmlEscape){
            return htmlEscape(output);
        }
        return output;
    }

    private static String arrayToString(Object array, String listDelim){
        assert array.getClass().isArray();
        int length = Array.getLength(array);
        StringBuffer buff = new StringBuffer();
        for(int i=0; i<length; i++){
            if(i>0){
                buff.append(listDelim);
            }
            buff.append(Array.get(array, i));
        }
        return buff.toString();
    }

    public static String htmlEscape(String str){
        StringBuffer buff = new StringBuffer(str.length());
        for(int i=0; i<str.length(); i++){
            final char ch = str.charAt(i);
            if(ch == '"'){
                buff.append("&quot;");
            }else if(ch == '<'){
                buff.append("&lt;");
            }else if(ch == '>'){
                buff.append("&gt;");
            }else{
                buff.append(ch);
            }
        }
        return buff.toString();
    }
}
