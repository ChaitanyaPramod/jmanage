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
package org.jmanage.core.remote;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.Reader;
import java.io.StringReader;

/**
 *
 * date:  Jan 18, 2005
 * @author	Rakesh Kalra
 */
public class Unmarshaller {

    public static Object unmarshal(Class clazz, String str){
        try {
            return org.exolab.castor.xml.Unmarshaller.unmarshal(clazz,
                    new StringReader(str));
        } catch (Exception e) {
            throw new RuntimeException("Error while unmarshalling obj of type:" +
                    clazz.getName(), e);
        }
    }
}
