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
