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
package org.jmanage.webui;

import org.jmanage.core.crypto.Crypto;

/**
 * JettyStopKey provides a secure key based on the admin password,
 * so that only admin user can shutdown the jManage process.
 * <p>
 * Date:  Dec 3, 2005
 * @author	Rakesh Kalra
 */
class JettyStopKey {

	private static String prefix = "d39dlf931!#$1ad93F1#F40A1#931d1bs!dadbe1#45%@1";
	private static String suffix = "x22D#!30d;3d#foa03#faa01#F04dcbp1E$Gea!2gd1pbie11";

	private final String key;

	public JettyStopKey(String password) {
		key = Crypto.hash(prefix + password + suffix);
	}

	public String toString() {
		return key;
	}
}
