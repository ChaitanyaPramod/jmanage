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
import org.apache.commons.beanutils.ConvertUtils;
import org.exolab.castor.xml.IDResolver;

import java.io.StringReader;
import java.util.logging.Logger;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 *
 * date:  Jan 18, 2005
 * @author	Rakesh Kalra
 */
public class Unmarshaller {

    private static final Logger logger = Loggers.getLogger(Unmarshaller.class);

    public static Object unmarshal(String className, String str){
        try {
            return unmarshal(Class.forName(className), str);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object unmarshal(Class clazz, String str){

        logger.fine("Unmarshalling: " + str);
        Object output = null;
        if(clazz.getName().equals("java.lang.Void") ||
                clazz.getName().equals("void")){
            return null;
        }else if(clazz.getName().startsWith("java.lang")){
            /* just convert the string representation */
            output = ConvertUtils.convert(str, clazz);
        }else {
            output = unmarshalFromXml(clazz, str);
            if(List.class.isAssignableFrom(clazz)){
                List list = (List)output;
                List outputList = new ArrayList(list.size());
                /* first element in the (non-empty) list is the object type */
                if(list.size() > 100000){
                    for(Iterator it=list.iterator();it.hasNext();){
                        Object obj = it.next();
                        System.out.println("list.get(). type=" + obj.getClass() + ", toString=" + obj.toString());
                    }


                    String elementType = (String)list.get(0);
                    logger.fine("elementType=" + elementType);
                    /*
                    The first element is enclosed in <string> XML element.
                    We need to take it out.
                    This is a hack (will need testing when we upgrade
                    castor version. */
                    int beginIndex = elementType.indexOf(">");
                    assert beginIndex != -1;
                    int endIndex = elementType.indexOf("<", beginIndex);
                    assert endIndex != -1;
                    elementType = elementType.substring(beginIndex, endIndex);
                    System.out.println("elementType=" + elementType);
                    logger.fine("After unmarshalling, elementType=" +
                            elementType);
                    for(Iterator it=list.iterator();it.hasNext();){
                        outputList.add(unmarshal(elementType,
                                (String)it.next()));
                    }
                    output = outputList;
                }
            }
        }
        logger.fine("Unmarshalled value: " + output);
        return output;
    }

    public static Object unmarshalFromXml(Class clazz, String str){
        try {

            org.exolab.castor.xml.Unmarshaller unmarshaller =
                    new org.exolab.castor.xml.Unmarshaller(clazz);
            //unmarshaller.setIDResolver(new MyIDResolver());
            unmarshaller.setMapping(DataMapping.getMapping());
            return unmarshaller.unmarshal(new StringReader(str));
            //return org.exolab.castor.xml.Unmarshaller.unmarshal(clazz,
             //       new StringReader(str));
        } catch (Exception e) {
            throw new RuntimeException("Error while unmarshalling obj of type:" +
                    clazz.getName() + ", xml=" + str, e);
        }
    }


    private static class MyIDResolver implements IDResolver{
        public Object resolve(String s) {
            System.out.println("IDResolver: " + s);
            return null;
        }
    }
}
