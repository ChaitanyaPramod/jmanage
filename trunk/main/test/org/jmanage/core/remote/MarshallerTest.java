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

import junit.framework.TestCase;

import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.jmanage.core.util.Loggers;

/**
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
public class MarshallerTest extends TestCase {

    private static final Logger logger = Loggers.getLogger(MarshallerTest.class);

    public MarshallerTest(String name){
        super(name);
    }

    public void testString(){
        String str1 = "testString";
        String xml = Marshaller.marshal(str1);
        logger.info("marshalled xml=" + xml);
        String str2 = (String)Unmarshaller.unmarshal(String.class, xml);
        assertEquals(str1, str2);
    }

    public void testInteger(){
        Integer int1 = new Integer(10);
        String xml = Marshaller.marshal(int1);
        logger.info("marshalled xml=" + xml);
        Integer int2 = (Integer)Unmarshaller.unmarshal(Integer.class, xml);
        assertEquals(int1, int2);
    }

    public void testJavaBean(){
        TestBean bean1 = new TestBean("test", new Integer(10));
        String xml = Marshaller.marshal(bean1);
        logger.info("marshalled xml=" + xml);
        TestBean bean2 = (TestBean)Unmarshaller.unmarshal(TestBean.class, xml);
        assertEquals(bean1, bean2);
    }

    public void testList(){
        List list1 = new ArrayList();
        list1.add(new TestBean("test1", new Integer(10)));
        list1.add(new TestBean("test2", new Integer(20)));
        String xml = Marshaller.marshal(list1);
        logger.info("marshalled xml=" + xml);
        List list2 = (List)Unmarshaller.unmarshal(ArrayList.class, xml);
        assertEquals(list1.size(), list2.size());
        Iterator it1 = list1.iterator();
        for(Iterator it2=list2.iterator(); it2.hasNext(); ){
            assertEquals(it1.next(), it2.next());
        }
    }
}
