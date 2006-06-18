/**
 * Copyright 2004-2006 jManage.org
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
package org.jmanage.webui.dashboard.components;

import org.jmanage.webui.dashboard.framework.DashboardComponent;
import org.jmanage.webui.dashboard.framework.DashboardContext;
import org.jmanage.core.management.*;
import org.jdom.Element;

import java.util.*;

/**
 * Date: Jun 10, 2006 8:17:55 PM
 *
 * @ author: Shashank Bellary
 */
public class MBeanAttributeTable implements DashboardComponent{
    private String id, name;
    private Map<String, List> mbeans = new HashMap<String, List>();
    private Map<String, List<String>> attributes =
            new HashMap<String, List<String>>();
    private Map<String, Map<String, String>> attributeDisplayNames =
            new HashMap<String, Map<String, String>>();

    protected final String HEADER = "header";
    protected final String NAME = "name";
    protected final String PARAM = "param";
    protected final String MBEAN = "mbean";
    protected final String ATTRIBUTE = "attribute";
    protected final String DISPLAY_NAME = "displayName";

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public void init(Element componentConfig) {
        id = componentConfig.getAttribute(ID).getValue();
        name = componentConfig.getAttribute(NAME).getValue();
        List<Element> headerElements = componentConfig.getChildren(HEADER);
        for(Element header: headerElements){
            String displayHeader = header.getAttribute(NAME).getValue();
            List<Element> paramElements = header.getChildren(PARAM);
            List<String> mbeansPerHeader = new ArrayList<String>();
            for(Element param: paramElements){
                String mbean = param.getAttribute(MBEAN).getValue();
                String attribute = param.getAttribute(ATTRIBUTE).getValue();
                String attribDisplayName = param.getAttribute(DISPLAY_NAME).getValue();
                String mbeansMapKey = mbean+"|"+displayHeader;

                if(!mbeansPerHeader.contains(mbean))
                    mbeansPerHeader.add(mbean);

                if(attributes.containsKey(mbeansMapKey))
                    attributes.get(mbeansMapKey).add(attribute);
                else{
                    List attribs = new ArrayList();
                    attribs.add(attribute);
                    attributes.put(mbeansMapKey, attribs);
                }

                if(attributeDisplayNames.containsKey(mbeansMapKey))
                    attributeDisplayNames.get(mbeansMapKey).put(attribute,
                            attribDisplayName);
                else{
                    Map attribDisplayNamesMap = new HashMap();
                    attribDisplayNamesMap.put(attribute, attribDisplayName);
                    attributeDisplayNames.put(mbeansMapKey, attribDisplayNamesMap);
                }
            }
            mbeans.put(displayHeader, mbeansPerHeader);
        }
    }

    @SuppressWarnings("unchecked")
    /**
     *
     * @param context
     * @return Component in HTML format.
     */
    public String draw(DashboardContext context) {
        StringBuffer htmlTable = new StringBuffer("<table class=\"plaintext\" style=\"border:1;border-style:solid;border-width:1px;border-color:#C0C0C0\">");
        htmlTable.append("<tr><td colspan=\"3\" style=\"text-align:center;\"><b>").append(getName()).append("</b></td></tr>");
        htmlTable.append("<tr><td colspan=\"3\">&nbsp;</td></tr>");
        ServerConnection connection = context.getWebContext().getServerConnection();
        Set<String> displayHeaders = mbeans.keySet();
        for(String header : displayHeaders){
            htmlTable.append("<tr><td colspan=\"3\" style=\"text-align:center;\"><b>").append(header).append("</b></td></tr>");
            List<String> mbeanList = mbeans.get(header);
            for(String mbeanName : mbeanList){
                ObjectName objectName = new ObjectName(mbeanName);
                String mbeansMapKey = mbeanName+"|"+header;
                List<String> mbean2AttribList = attributes.get(mbeansMapKey);
                Map<String, String> mbean2AttribDisplayNameMap =
                        attributeDisplayNames.get(mbeansMapKey);
                String[] attribs = new String[mbean2AttribList.size()];
                List attributeList = connection.getAttributes(objectName,
                        mbean2AttribList.toArray(attribs));
                for (Object anAttributeList : attributeList) {
                    ObjectAttribute currentAttribute = (ObjectAttribute) anAttributeList;
                    htmlTable.append("<tr><td>").append(mbean2AttribDisplayNameMap.get(currentAttribute.getName())).append("</td><td>&nbsp;</td>");
                    htmlTable.append("<td>").append(currentAttribute.getDisplayValue()).append("</td></tr>");
                }
            }
        }
        htmlTable.append("</table>");
        return htmlTable.toString();
    }
}