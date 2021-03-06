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
package org.jmanage.webui.dashboard.framework;

import org.jdom.Element;

/**
 * Date: Apr 23, 2006 12:40:24 PM
 * @author Shashank Bellary
 */
public interface DashboardComponent {

    public static String COMPONENT = "component";
    public static String MBEAN = "mbean";
    public static String MBEAN_PATTERN = "mbeanPattern";
    public static String ATTRIBUTE = "attribute";
    public static String ATTRIBUTES = "attributes";
    public static String OPERATION = "operation";
    public static String DISPLAY_NAME = "displayName";
    public static String DISPLAY_NAMES = "displayNames";
    public static String OBJECT_NAME_FILTER = "objectNameFilter";

    public static String ID = "id";
    public static String NAME = "name";


    // TODO: It will be good to have an implementation of getId() here.
    public String getId();
    public void init(Element componentConfig);
    public String draw(DashboardContext context);
}
