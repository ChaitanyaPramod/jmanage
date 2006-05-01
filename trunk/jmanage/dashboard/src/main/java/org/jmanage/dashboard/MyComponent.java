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

package org.jmanage.dashboard;

import org.jmanage.core.config.DashboardComponent;
import org.jdom.Element;

import java.util.List;
import java.util.ArrayList;

/**
 * Date: Apr 23, 2006 5:00:21 PM
 * @author Shashank Bellary
 */
public class MyComponent implements DashboardComponent {

    private String id = null;
    private String name = null;
    private List<String> mbeans = new ArrayList<String>();
    private List<String> attributes = new ArrayList<String>();
    private List<String> operations = new ArrayList<String>();

    public void init(Element componentConfig) {
        id = "test";
        name = "test";
    }

    public String draw() {
        return null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getMbeans() {
        return mbeans;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public List<String> getOperations() {
        return operations;
    }
}