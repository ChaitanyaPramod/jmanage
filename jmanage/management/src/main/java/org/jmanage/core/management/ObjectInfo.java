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

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ObjectInfo implements java.io.Serializable {

	private static final long serialVersionUID = 5740245125717325823L;
	private ObjectName objectName;
    private String description;
    private String className;
    private ObjectAttributeInfo[] attributes;
    private ObjectOperationInfo[] operations;
    private ObjectConstructorInfo[] constructors;
    private ObjectNotificationInfo[] notifications;

    public ObjectInfo(ObjectName objectName,
                      String className,
                      String description,
                      ObjectAttributeInfo[] attributes,
                      ObjectConstructorInfo[] constructors,
                      ObjectOperationInfo[] operations,
                      ObjectNotificationInfo[] notifications) {
        this.objectName = objectName;
        this.className = className;
        this.description = description;
        this.attributes = attributes;
        this.constructors = constructors;
        this.operations = operations;
        this.notifications = notifications;
        /* alphabetically sort the attribute list */
        Arrays.sort(this.attributes, new Comparator<ObjectAttributeInfo>(){
            public int compare(ObjectAttributeInfo attrInfo1, ObjectAttributeInfo attrInfo2) {
                return attrInfo1.getName().compareToIgnoreCase(attrInfo2.getName());
            }
        });
    }

    public ObjectAttributeInfo[] getAttributes() {
        return attributes;
    }

    public String getClassName() {
        return className;
    }

    public ObjectConstructorInfo[] getConstructors() {
        return constructors;
    }

    public String getDescription() {
        return description;
    }

    public ObjectNotificationInfo[] getNotifications() {
        return notifications;
    }

    public ObjectOperationInfo[] getOperations() {
        return operations;
    }

    public ObjectName getObjectName() {
        return objectName;
    }
}
