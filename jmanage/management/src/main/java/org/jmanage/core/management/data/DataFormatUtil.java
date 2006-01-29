/**
* Copyright (c) 2004-2005 jManage.org
*
* This is a free software; you can redistribute it and/or
* modify it under the terms of the license at
* http://www.jmanage.org.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.jmanage.core.management.data;

import org.jmanage.core.util.Loggers;
import org.jmanage.util.StringUtils;

import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Utility class to provide data formatting.
 *
 * <p>
 * Date:  Sep 27, 2005
 * @author	Rakesh Kalra
 */
public class DataFormatUtil {

    private static final Logger logger = Loggers.getLogger(DataFormatUtil.class);

    private static final String FORMAT_PREFIX = "format.";
    private static final String LIST_DELIMITER = "list.delimiter";
    private static final String ESCAPE_HTML = "escape.html";
    private static final String NULL_VALUE = "null.value";

    private static Object[][] classToFormatMapping = new Object[0][2];
    // properties
    private static String listDelimiter = System.getProperty("line.separator");
    private static boolean escapeHtml = true;
    private static String nullValue = "";
    private static int formatCount = 0;

    static{
        String formatFile =
                System.getProperty("org.jmanage.core.management.data.formatConfig");
        if(formatFile != null){
            try {
                InputStream is = new FileInputStream(formatFile);
                Properties props = new Properties();
                props.load(is);
                is.close();
                // note that we are array is bigger than number of formatters
                classToFormatMapping = new Object[props.keySet().size()][2];
                for(Iterator it=props.keySet().iterator(); it.hasNext();){
                    String property = (String)it.next();
                    if(property.startsWith(FORMAT_PREFIX)){
                        String className = property.substring(FORMAT_PREFIX.length());
                        // todo: it will be better to load the data class using the application classloader
                        // the data format class can be loaded in the base classloader - rk
                        Class clazz = Class.forName(className);
                        classToFormatMapping[formatCount][0] = clazz;
                        classToFormatMapping[formatCount][1] =
                                Class.forName(props.getProperty(property)).newInstance();
                        formatCount ++;
                    }else if(property.equals(LIST_DELIMITER)){
                        listDelimiter = props.getProperty(property);
                    }else if(property.equals(ESCAPE_HTML)){
                        escapeHtml =
                                Boolean.valueOf(props.getProperty(property)).booleanValue();
                    }else if(property.equals(NULL_VALUE)){
                        nullValue = props.getProperty(property);
                    }else{
                        logger.warning("Unrecognized property=" + property);
                    }
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error reading data format config file:" +
                        formatFile + ". DataFormatUtil is not initialized.", e);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Config file:" + formatFile +
                        ". DataFormatUtil is not initialized.", e);
            }
        }

    }

    public static String format(Object data){
        return format(data, listDelimiter, escapeHtml);
    }

    private static String format(Object data, String listDelim, boolean htmlEscape){
        if(data == null) return nullValue;
        DataFormat formatter = findDataFormat(data);
        if(formatter != null){
            return formatter.format(data);
        }
        return StringUtils.toString(data, listDelim, htmlEscape);
    }

    private static DataFormat findDataFormat(Object data){
        for(int i=0; i<formatCount; i++){
            Class clazz = (Class)classToFormatMapping[i][0];
            if(clazz.isInstance(data)){
                return (DataFormat)classToFormatMapping[i][1];
            }
        }
        return null;
    }
}
