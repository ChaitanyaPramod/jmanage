package org.jmanage.core.management;

/**
 *
 * date:  Aug 13, 2004
 * @author	Rakesh Kalra
 */
public class ObjectOperationInfo extends ObjectFeatureInfo {

    public static final int INFO = 0;
    public static final int ACTION = 1;
    public static final int ACTION_INFO = 2;
    public static final int UNKNOWN = 3;

    private String type;
    private ObjectParameterInfo[] signature;
    private int impact;

    public ObjectOperationInfo(String name,
                               String description,
                               ObjectParameterInfo[] signature,
                               String returnType,
                               int impact) {
        super(name, description);
        this.signature = signature;
        this.type = returnType;
        this.impact = impact;
    }

    public int getImpact() {
        return impact;
    }

    public String getReturnType() {
        return type;
    }

    public ObjectParameterInfo[] getSignature() {
        return signature;
    }
}
