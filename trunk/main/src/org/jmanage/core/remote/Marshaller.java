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

import org.jmanage.core.util.Loggers;
import org.exolab.castor.mapping.Mapping;
import org.xml.sax.InputSource;

import java.io.StringWriter;
import java.io.StringReader;
import java.util.logging.Logger;
import java.util.List;

/**
 *
 * date:  Jan 18, 2005
 * @author	Rakesh Kalra
 */
public class Marshaller {

    private static final Logger logger = Loggers.getLogger(Marshaller.class);

    public static String marshal(Object obj){

        String output;
        if(obj == null){
            output = "";
        }else if(obj.getClass().getName().startsWith("java.lang")){
            /* just write out the string representation */
            output = obj.toString();
        }else{
            output = marshalToXml(obj);
        }
        logger.fine("Marshalled value: " + output);
        return output;
    }

    private static String marshalToXml(Object obj){

        /*if(obj instanceof java.util.List){
            List list = (List)obj;
            if(list.size() > 0){
                list.add(0, list.get(0).getClass().getName());
            }
        } */

        StringWriter writer = new StringWriter();
        try {
            org.exolab.castor.xml.Marshaller marshaller =
                    new org.exolab.castor.xml.Marshaller(writer);
            marshaller.setRootElement("marshalledObject");
            marshaller.setMarshalAsDocument(false);
            marshaller.setSuppressXSIType(false);
            marshaller.setMapping(DataMapping.getMapping());
            marshaller.marshal(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error while marshalling obj of type:" +
                    obj.getClass().getName(), e);
        }
        return writer.toString();
    }
}
