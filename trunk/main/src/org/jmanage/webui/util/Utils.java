package org.jmanage.webui.util;

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
}
