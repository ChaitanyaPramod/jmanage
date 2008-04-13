/**
 * Copyright 2004-2006 jManage.org
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
package org.jmanage.core.management.metadata;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jmanage.core.config.ApplicationConfig;
import org.jmanage.core.config.ApplicationConfigFactory;
import org.jmanage.core.config.ApplicationTypes;
import org.jmanage.core.management.ObjectAttributeInfo;
import org.jmanage.core.management.ObjectInfo;
import org.jmanage.core.management.ObjectName;
import org.jmanage.core.management.ObjectOperationInfo;
import org.jmanage.core.management.ObjectParameterInfo;
import org.jmanage.core.management.ServerConnection;
import org.jmanage.core.management.ServerConnector;
import org.jmanage.util.StringUtils;

/**
 * Used to take dump of the MBean metadata from the server.
 * 
 * @author Rakesh Kalra
 */
public class MetaDataWriter {

	public static void main(String[] args) throws IOException {

		ApplicationConfig appConfig = ApplicationConfigFactory.create("1", "test", "local",
				null, null, null, null, null, null, null);
		ServerConnection connection = ServerConnector.getServerConnection(appConfig);
		Set<ObjectName> objectNames = connection.queryNames(new ObjectName("*:*"));
		// Set<ObjectName> sortedObjectNames = new TreeSet<ObjectName>();
		// sortedObjectNames.addAll(objectNames);
		Document document = getDocument(connection, objectNames);
		/* write to the disc */
		XMLOutputter writer = new XMLOutputter();
		writer.output(document, new FileWriter("c:\\PlatformMBeans2.xml"));
		connection.close();
	}

	private static Document getDocument(ServerConnection connection,
			Set<ObjectName> objectNames) {
		Document doc = new Document();
		Element rootElement = new Element("mbeans");
		doc.setRootElement(rootElement);
		/* mbeans */
		for (ObjectName objName : objectNames) {
			ObjectInfo objInfo = connection.getObjectInfo(objName);
			Element mbeanElement = new Element("mbean");
			mbeanElement.setAttribute("name", objInfo.getObjectName().toString());
			mbeanElement.setAttribute("description", StringUtils.noNull(objInfo
					.getDescription()));
			/* attributes */
			for (ObjectAttributeInfo attrInfo : objInfo.getAttributes()) {
				Element attrElement = new Element("attribute");
				attrElement.setAttribute("name", attrInfo.getName());
				attrElement.setAttribute("description", StringUtils.noNull(attrInfo
						.getDescription()));
				mbeanElement.addContent(attrElement);
			}
			/* operations */
			for (ObjectOperationInfo oprInfo : objInfo.getOperations()) {
				Element oprElement = new Element("operation");
				oprElement.setAttribute("name", oprInfo.getName());
				oprElement.setAttribute("description", StringUtils.noNull(oprInfo
						.getDescription()));
				/* parameters */
				for (ObjectParameterInfo paramInfo : oprInfo.getSignature()) {
					Element paramElement = new Element("parameter");
					paramElement.setAttribute("name", paramInfo.getName());
					paramElement.setAttribute("type", paramInfo.getType());
					paramElement.setAttribute("description", StringUtils.noNull(paramInfo
							.getDescription()));
					oprElement.addContent(paramElement);
				}
				mbeanElement.addContent(oprElement);
			}
			rootElement.addContent(mbeanElement);
		}
		return doc;
	}

}
