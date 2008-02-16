/**
 * Copyright 2004-2005 jManage.org. All rights reserved.
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
package org.jmanage.webui.taglib;

import java.util.*;

/**
 *
 * <p>
 * Date:  Feb 4, 2006
 * @author	Rakesh Kalra
 */
public class HtmlElement {

    private final String name;
    private final Map<String, String> attributes = new HashMap<String, String>();
    private final List<HtmlElement> childElements = new LinkedList<HtmlElement>();

    public HtmlElement(String name){
        this.name = name;
    }

    public void addAttribute(String attr, Object value){
        attributes.put(attr, value.toString());
    }

    public void addAttribute(String attr, int value){
        addAttribute(attr, Integer.toString(value));
    }

    public void addAttribute(String attr, long value){
        addAttribute(attr, Long.toString(value));
    }

    public void addChildElement(HtmlElement element){
        childElements.add(element);
    }

    public String toString(){
        StringBuffer buff = new StringBuffer("<");
        buff.append(name);
        for(Iterator it=attributes.keySet().iterator(); it.hasNext();){
            String attr = (String)it.next();
            String value = (String)attributes.get(attr);
            buff.append(" ");
            buff.append(attr);
            buff.append("=\"");
            buff.append(escape(value));
            buff.append("\"");
        }
        buff.append(">");
        for(Iterator it=childElements.iterator(); it.hasNext();){
            buff.append("\n");
            buff.append(it.next());
        }
        buff.append("</");
        buff.append(name);
        buff.append(">");
        return buff.toString();
    }

    private String escape(String value){
        return value.replaceAll("\"", "&quot;");
    }
}
