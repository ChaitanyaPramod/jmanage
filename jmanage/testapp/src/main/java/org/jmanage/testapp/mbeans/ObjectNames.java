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

/**
 *
 * date:  Dec 19, 2004
 * @author	Rakesh Kalra
 */
public interface ObjectNames {
    String CONFIGURATION = "jmanage:name=Configuration,type=test";
    String CALCULATOR = "jmanage:name=\"Calculator/test\",type=\"test\"";
    String PRIMITIVE_DATA_TYPE_TEST = "jmanage:name=PrimitiveDataTypeTest,type=test";
    String DATA_TYPE_TEST = "jmanage:type=test,name=DataTypeTest";
    String BIG_DATA_TYPE_TEST = "jmanage:type=test,name=BigDataTypeTest";
    String OBJECT_NAME_DATA_TYPE_TEST = "jmanage:name=ObjectNameDataTypeTest,type=test";
    String TIME_NOTIFICATION_BROADCASTER = "jmanage:type=test,name=TimeNotificationBroadcaster";
    String DATA_FORMAT = "jmanage:type=test,name=DataFormat";
    String OPEN_MBEAN_DATA_TYPE_TEST = "jmanage:type=test,name=OpenMBeanDataTypeTest";
}
