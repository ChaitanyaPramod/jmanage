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

import org.jmanage.core.util.Loggers;

import java.util.Set;
import java.util.List;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * ServerConnectionProxy updates the context classloader before calling
 * any method on the wrapped ServerConnection object. It later sets the
 * classloader back to the original classloader.
 *
 * date:  Aug 19, 2004
 * @author	Rakesh Kalra
 */
public class ServerConnectionProxy implements ServerConnection{

    private static final Logger logger =
            Loggers.getLogger(ServerConnectionProxy.class);

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
    public Set queryNames(ObjectName objectName) {

        final ClassLoader contextClassLoader =
                        Thread.currentThread().getContextClassLoader();
        try {
            /* temporarily change the thread context classloader */
            Thread.currentThread().setContextClassLoader(classLoader);
            /* invoke the method on the wrapped ServerConnection */
            return connection.queryNames(objectName);
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
            /* some attribute values may not be serializable, hence may fail,
                hence we need to get one attribute at a time */
            List attributeList = new LinkedList();
            for(int i=0; i<attributeNames.length; i++){
                attributeList.add(getAttribute(objectName, attributeNames[i]));
            }
            return attributeList;
        } finally {
            /* change the thread context classloader back to the
                    original classloader*/
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }

    private ObjectAttribute getAttribute(ObjectName objectName,
                                         String attributeName){

        try {
            // TODO: It will be better to have a getAttribute method which
            // just works on a single attribute
            List attrList = connection.getAttributes(objectName,
                    new String[]{attributeName});
            if(attrList.size() > 0)
                return (ObjectAttribute)attrList.get(0);
        } catch (Exception e) {
            String msg = "Error retriving attribute=" +
                    attributeName + ", objectName=" + objectName;
            logger.log(Level.WARNING, msg);
            logger.log(Level.FINE, msg, e);
            return new ObjectAttribute(attributeName,
                    ObjectAttribute.STATUS_ERROR, e.getMessage());
        }
        return new ObjectAttribute(attributeName,
                ObjectAttribute.STATUS_NOT_FOUND, null);
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

    public void addNotificationListener(ObjectName objectName,
                                        ObjectNotificationListener listener,
                                        ObjectNotificationFilter filter,
                                        Object handback) {
        final ClassLoader contextClassLoader =
                        Thread.currentThread().getContextClassLoader();
        try {
            /* temporarily change the thread context classloader */
            Thread.currentThread().setContextClassLoader(classLoader);
            /* invoke the method on the wrapped ServerConnection */
            connection.addNotificationListener(objectName, listener,
                    filter, handback);
        } finally {
            /* change the thread context classloader back to the
                    original classloader*/
            Thread.currentThread().setContextClassLoader(contextClassLoader);
        }
    }
}
