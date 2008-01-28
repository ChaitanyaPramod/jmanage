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
package org.jmanage.testapp.mbeans;

import javax.management.ObjectName;
import javax.management.MalformedObjectNameException;

/**
 *
 * Date:  Jul 31, 2005
 * @author	Rakesh Kalra
 */
public class ObjectNameDataTypeTest implements ObjectNameDataTypeTestMBean {

    private ObjectName objectName;

    public ObjectNameDataTypeTest(){
        try {
            objectName = new ObjectName("domain:name=abc");
        } catch (MalformedObjectNameException e) {
            assert false;
        }
    }

    public ObjectName getObjectName() {
        return objectName;
    }

    public void setObjectName(ObjectName objectName) {
        this.objectName = objectName;
    }

    public String objectNameToString(ObjectName objectName) {
        return objectName.toString();
    }
}
