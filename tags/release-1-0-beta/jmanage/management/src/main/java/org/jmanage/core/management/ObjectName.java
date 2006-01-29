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

import org.jmanage.core.util.JManageProperties;

import javax.management.MalformedObjectNameException;

/**
 *
 * date:  Aug 12, 2004
 * @author	Rakesh Kalra
 */
public class ObjectName implements java.io.Serializable {

    private final String objectName;
    private final String canonicalName;

    public ObjectName(String objectName){
        this.objectName = objectName;
        try {
            this.canonicalName =
                    new javax.management.ObjectName(objectName).getCanonicalName();
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public ObjectName(String objectName, String canonicalName){
        this.objectName = objectName;
        this.canonicalName = canonicalName;
    }

    public String getCanonicalName(){
        return canonicalName;
    }

    public String toString(){
        return objectName;
    }

    public String getDisplayName(){
        return JManageProperties.isDisplayCanonicalName()?
                canonicalName : objectName;
    }

    public boolean equals(Object obj){
        if(obj instanceof ObjectName){
            ObjectName on = (ObjectName)obj;
            return on.objectName.equals(this.objectName);
        }
        return false;
    }
}
