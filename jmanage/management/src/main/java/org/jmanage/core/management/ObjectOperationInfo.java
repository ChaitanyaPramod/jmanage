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

    public String[] getParameters(){
        String[] parameters = new String[signature.length];
        for(int i=0; i<signature.length; i++){
            parameters[i] = signature[i].getType();
        }
        return parameters;
    }
}
