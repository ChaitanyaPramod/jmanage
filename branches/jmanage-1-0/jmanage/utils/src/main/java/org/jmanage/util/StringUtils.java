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
import java.net.URL;

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

    public static String toString(URL[] urls){
        StringBuffer buff = new StringBuffer();
        for(int i=0; i<urls.length; i++){
            buff.append(urls[i].toString());
            buff.append(";");
        }
        return buff.toString();
    }

    public static String padRight(String str, int totalLength) {
        int strLen = str.length();
        StringBuffer buff = new StringBuffer(str);
        for(int i=0;i<totalLength - strLen; i++){
            buff.append(' ');
        }
        return buff.toString();
    }

    public static String getCharSeries(char ch, int length) {
        char[] series = new char[length];
        Arrays.fill(series, ch);
        return new String(series);
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
