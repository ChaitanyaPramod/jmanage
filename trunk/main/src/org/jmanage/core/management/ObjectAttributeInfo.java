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

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ObjectAttributeInfo extends ObjectFeatureInfo {

    private String attributeType;
    private boolean isWrite;
    private boolean isRead;
    private boolean isIs;

    public ObjectAttributeInfo(String name,
                               String description,
                               String attributeType,
                               boolean isWrite,
                               boolean isRead,
                               boolean isIs) {
        super(name, description);
        this.attributeType = attributeType;
        this.isWrite = isWrite;
        this.isRead = isRead;
        this.isIs = isIs;
    }

    public String getType() {
        return attributeType;
    }

    public boolean isIs() {
        return isIs;
    }

    public boolean isReadable() {
        return isRead;
    }

    public boolean isWritable() {
        return isWrite;
    }
}