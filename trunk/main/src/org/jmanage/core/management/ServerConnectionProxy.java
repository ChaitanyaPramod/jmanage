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

import java.util.Set;
import java.util.List;

/**
 * ServerConnectionProxy updates the context classloader before calling
 * any method on the wrapped ServerConnection object. It later sets the
 * classloader back to the original classloader.
 *
 * date:  Aug 19, 2004
 * @author	Rakesh Kalra
 */
public class ServerConnectionProxy implements ServerConnection{

    private ServerConnection connection;
    private ClassLoader classLoader;

    public ServerConnectionProxy(ServerConnection connection,
                                 ClassLoader classLoader){
        this.connection = connection;
        this.classLoader = classLoader;
    }

    /**
     * Queries the management objects based on the given object name, containing
     * the search criteria.
     *
     * @param objectName
     * @return
     */
    public Set queryObjects(ObjectName objectName) {

        final ClassLoader contextClassLoader =
                        Thread.currentThread().getContextClassLoader();
        try {
            /* temporarily change the thread context classloader */
            Thread.currentThread().setContextClassLoader(classLoader);
            /* invoke the method on the wrapped ServerConnection */
            return connection.queryObjects(objectName);
        } finally {
            /* change the thread context classloader back to the
                    original classloader*/
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    /**
     * Invokes the given "operationName" on the object identified by
     * "objectName".
     *
     * @param objectName
     * @param operationName
     * @param params
     * @param signature
     * @return
     */
    public Object invoke(ObjectName objectName,
                         String operationName,
                         Object[] params,
                         String[] signature) {

        final ClassLoader contextClassLoader =
                        Thread.currentThread().getContextClassLoader();
        try {
            /* temporarily change the thread context classloader */
            Thread.currentThread().setContextClassLoader(classLoader);
            /* invoke the method on the wrapped ServerConnection */
            return connection.invoke(objectName, operationName,
                    params, signature);
        } finally {
            /* change the thread context classloader back to the
                    original classloader*/
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    /**
     * Returns the information about the given objectName.
     *
     * @param objectName
     * @return
     */
    public ObjectInfo getObjectInfo(ObjectName objectName) {

        final ClassLoader contextClassLoader =
                        Thread.currentThread().getContextClassLoader();
        try {
            /* temporarily change the thread context classloader */
            Thread.currentThread().setContextClassLoader(classLoader);
            /* invoke the method on the wrapped ServerConnection */
            return connection.getObjectInfo(objectName);
        } finally {
            /* change the thread context classloader back to the
                    original classloader*/
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    /**
     * Returns a list of ObjectAttribute objects containing attribute names
     * and values for the given attributeNames
     *
     * @param objectName
     * @param attributeNames
     * @return
     */
    public List getAttributes(ObjectName objectName, String[] attributeNames) {

        final ClassLoader contextClassLoader =
                        Thread.currentThread().getContextClassLoader();
        try {
            /* temporarily change the thread context classloader */
            Thread.currentThread().setContextClassLoader(classLoader);
            /* invoke the method on the wrapped ServerConnection */
            return connection.getAttributes(objectName, attributeNames);
        } finally {
            /* change the thread context classloader back to the
                    original classloader*/
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    /**
     * Saves the attribute values.
     *
     * @param objectName
     * @param attributeList list of ObjectAttribute objects
     */
    public List setAttributes(ObjectName objectName, List attributeList) {
        final ClassLoader contextClassLoader =
                        Thread.currentThread().getContextClassLoader();
        try {
            /* temporarily change the thread context classloader */
            Thread.currentThread().setContextClassLoader(classLoader);
            /* invoke the method on the wrapped ServerConnection */
            return connection.setAttributes(objectName, attributeList);
        } finally {
            /* change the thread context classloader back to the
                    original classloader*/
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }
}
