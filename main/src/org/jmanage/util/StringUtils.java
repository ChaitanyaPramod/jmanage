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

import java.util.StringTokenizer;

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
}
