package org.jmanage.core.remote;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.StringWriter;
import java.io.IOException;

/**
 *
 * date:  Jan 18, 2005
 * @author	Rakesh Kalra
 */
public class Marshaller {

    public static String marshal(Object obj){

        StringWriter writer = new StringWriter();
        try {
            org.exolab.castor.xml.Marshaller marshaller =
                    new org.exolab.castor.xml.Marshaller(writer);
            marshaller.setRootElement("marshalledObject");
            marshaller.setMarshalAsDocument(false);
            marshaller.setSuppressXSIType(true);
            marshaller.marshal(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error while marshalling obj of type:" +
                    obj.getClass().getName(), e);
        }
        return writer.toString();
    }
}
