/**
 * 
 */
package org.jmanage.monitoring.data.collector;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jmanage.core.management.ObjectName;
import org.jmanage.monitoring.data.model.ObservedMBeanAttribute;

/**
 * An object that assists in caching and use of ObservedMBeanAttribute objects.
 * 
 * @author rkalra
 */
public class ObservedMBean {

	private ObjectName objectName;
	private String[] attributeNames;
	private ObservedMBeanAttribute[] observedMBeanAttributes;

	public ObservedMBean(ObjectName objectName){
		this(objectName, new String[0], new ObservedMBeanAttribute[0]);
	}

	public ObservedMBean(ObjectName objectName, String[] attributeNames, 
			ObservedMBeanAttribute[] observedMBeanAttributes){
		assert objectName != null;
		assert attributeNames != null;
		assert observedMBeanAttributes != null;
		assert attributeNames.length == observedMBeanAttributes.length;
		this.objectName = objectName;
		this.attributeNames = attributeNames;
		this.observedMBeanAttributes = observedMBeanAttributes;
	}
	
	public String[] getAttributeNames() {
		return attributeNames;
	}
	
	public ObjectName getObjectName() {
		return objectName;
	}
	
	public ObservedMBeanAttribute[] getObservedMBeanAttributes() {
		return observedMBeanAttributes;
	}
	
	/* TODO: this method could use some optimization with creation of temp lists */
	public void addObservedMBeanAttribute(ObservedMBeanAttribute attribute){
		List<ObservedMBeanAttribute> observedMBeanAttributesList =
			new LinkedList<ObservedMBeanAttribute>(Arrays.asList(observedMBeanAttributes));
		observedMBeanAttributesList.add(attribute);
		observedMBeanAttributes = observedMBeanAttributesList.toArray(observedMBeanAttributes);
		List<String> attributeNamesList = new LinkedList<String>(Arrays.asList(attributeNames));
		attributeNamesList.add(attribute.getAttributeName());
		attributeNames = attributeNamesList.toArray(attributeNames);
	}
	
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((objectName == null) ? 0 : objectName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ObservedMBean other = (ObservedMBean) obj;
		if (objectName == null) {
			if (other.objectName != null)
				return false;
		} else if (!objectName.equals(other.objectName))
			return false;
		return true;
	}

}
