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
package org.jmanage.core.management;

import java.io.Serializable;

/**
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra
 */
public class ObjectInstance implements Serializable {

    private ObjectName name;
    private String className;

    public ObjectInstance(String objName, String className) {
        this.name = new ObjectName(objName);
        this.className = className;
    }

    public ObjectInstance(ObjectName objectName, String className) {
        this.name = objectName;
        this.className = className;
    }

    public boolean equals(Object o) {
        if(o instanceof ObjectInstance){
            ObjectInstance oi = (ObjectInstance)o;
            if(oi.name.equals(this.name)
                    && oi.className.equals(this.className)){
                return true;
            }
        }
        return false;
    }

    public String getClassName() {
        return className;
    }

    public ObjectName getObjectName() {
        return name;
    }
}

