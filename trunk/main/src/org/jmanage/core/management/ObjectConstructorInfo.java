package org.jmanage.core.management;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ObjectConstructorInfo extends ObjectFeatureInfo {

    private ObjectParameterInfo[] signature;

    public ObjectConstructorInfo(String name,
                                 String description,
                                 ObjectParameterInfo[] signature) {
        super(name, description);
        this.signature = signature;
    }

    public ObjectParameterInfo[] getSignature() {
        return signature;
    }
}
