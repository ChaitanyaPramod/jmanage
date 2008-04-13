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
package org.jmanage.core.auth;

import org.jmanage.core.util.Expression;
import org.jmanage.core.util.Loggers;

import javax.management.ObjectName;
import java.util.Set;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ACLContext is used to create dynamic ACLs. ACLContext is appended to the ACL after an
 * "@" character. It contains the application, mbean and the attribute/operation on which
 * the ACL is being applied.
 * 
 * Date: Apr 8, 2005
 * 
 * @author Rakesh Kalra
 */
public class ACLContext {

	private static final Logger logger = Loggers.getLogger(ACLContext.class);
	
	private static final String WILDCARD = Expression.WILDCARD;

	private String appName = WILDCARD;
	/* object name or configured name */
	private String mbeanName = WILDCARD;
	/* this could be attribute or operation name */
	private String targetName = WILDCARD;

	public ACLContext(String aclContext) {
		Expression expr = new Expression(aclContext);
		if (expr.getAppName() != null) {
			appName = expr.getAppName();
		}
		if (expr.getMBeanName() != null) {
			// todo: we need to convert the MBean name to canonical name
			// for comparision. It will be good to further improve
			// the comparision by allowing wild card comparisions.
			mbeanName = expr.getMBeanName();
		}
		if (expr.getTargetName() != null) {
			targetName = expr.getTargetName();
		}
	}

	public ACLContext(String appName, String mbeanName, String targetName) {
		if (appName != null) {
			this.appName = appName;
		}
		if (mbeanName != null) {
			this.mbeanName = mbeanName;
		}
		if (targetName != null) {
			this.targetName = targetName;
		}
	}

	public boolean equals(Object obj) {
		if (obj instanceof ACLContext) {
			ACLContext context = (ACLContext) obj;
			return compare(context.appName, this.appName)
					&& compareMBeanName(context.mbeanName, this.mbeanName)
					&& compare(context.targetName, this.targetName);
		}
		return false;
	}

	private boolean compare(String a, String b) {
		boolean equals = false;
		if (a == null && b == null) {
			/* both are null */
			equals = true;
		} else if (a == null || b == null) {
			/* one of them is not null */
			equals = false;
		} else if (a.equals(WILDCARD) || b.equals(WILDCARD)) {
			/* one of them is wild card */
			equals = true;
		} else if (a.equals(b)) {
			/* they are the same */
			equals = true;
		}

		return equals;
	}

	public String toString() {
		return new Expression(appName, mbeanName, targetName).toString();
	}

	private boolean compareMBeanName(String a, String b) {
		boolean equals = false;
		if (a == null && b == null) {
			/* both are null */
			equals = true;
		} else if (a == null || b == null) {
			/* one of them is not null */
			equals = false;
		} else if (a.equals(WILDCARD) || b.equals(WILDCARD)) {
			/* one of them is wild card */
			equals = true;
		} else if (a.equals(b)) {
			/* they are the same */
			equals = true;
		} else {
			String aclDomainName = null;
			String inputDomainName = null;
			boolean domainMatch = false;
			boolean nameMatch = false;
			int index = b.indexOf(":");
			if (index != -1) {
				aclDomainName = b.substring(0, index);
				String name = b.substring(index + 1);
				index = a.indexOf(":");
				inputDomainName = a.substring(0, index);
				String inputname = a.substring(index + 1);
				if (aclDomainName.equals(inputDomainName)) {
					domainMatch = true;
				} else if (aclDomainName.equals(WILDCARD) || inputDomainName.equals(WILDCARD)) {
					domainMatch = true;
				}
				if (name.equals(inputname)) {
					nameMatch = true;
				} else if (name.equals(WILDCARD) || inputname.equals(WILDCARD)) {
					nameMatch = true;
				} else {
					try {
						nameMatch = matchNameForKeys(new ObjectName(b), new ObjectName(a));
					} catch (Exception e) {
						logger.log(Level.WARNING, "Error in matchNameForKeys", e);
						equals = false;
					}
				}
				if (domainMatch == true && nameMatch == true) {
					equals = true;
				}
			} else {
				equals = false;
			}
		}
		return equals;
	}

	@SuppressWarnings("unchecked")
	private boolean matchNameForKeys(ObjectName pattern, ObjectName name) {
		Hashtable patternKeyPropertyList = pattern.getKeyPropertyList();
		String patternKeyPropertyListString = pattern.getCanonicalKeyPropertyListString();
		// boolean patternKeyPropertyListHasWildCards =
		// hasWildCards(patternKeyPropertyListString);
		boolean patternKeyPropertyListHasWildCards = (pattern.getCanonicalName().indexOf("*") != -1);
		if (patternKeyPropertyListHasWildCards) {
			Set kpp = getSet(patternKeyPropertyList);
			Hashtable keyPropertyList = name.getKeyPropertyList();
			Set kpl = getSet(keyPropertyList);
			if (kpl.containsAll(kpp))
				return true;
		} else {
			String keyPropertiesListString = name.getCanonicalKeyPropertyListString();
			if (keyPropertiesListString.equals(patternKeyPropertyListString))
				return true;

		}
		return false;
	}

	private Set<String> getSet(Hashtable hashtable) {
		HashSet<String> hashtableToSet = new HashSet<String>();
		Set hashtableEntrySet = hashtable.entrySet();
		for (Iterator hashtableEntrySetIterator = hashtableEntrySet.iterator(); hashtableEntrySetIterator
				.hasNext();) {
			java.util.Map.Entry entrySetElement = (java.util.Map.Entry) hashtableEntrySetIterator
					.next();
			String key = entrySetElement.getKey().toString();
			if (!key.equals("*")) {
				String value = entrySetElement.getValue().toString();
				String keyValuePair = key + "=" + value;
				hashtableToSet.add(keyValuePair);
			}
		}
		return hashtableToSet;
	}
}
