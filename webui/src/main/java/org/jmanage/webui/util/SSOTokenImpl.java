/**
 * jManage Application Management Platform 
 * Copyright 2004-2008 jManage.org
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details. 
 */
package org.jmanage.webui.util;

import javax.servlet.http.HttpServletRequest;
import org.jmanage.core.auth.SSOToken;

/**
 * This is a sample implementation of SSO Token and is instantiated based on the property
 * 'jmanage.sso.token.impl' in jmanage.properties file. If one has to use their own custom
 * token implementation then the property has to be changed accordingly.
 * 
 * TODO: This currently supports HttpServletRequest based SSO only. Not sure if we need 
 * support for CLI as well.
 * 
 * 
 * Date : Apr 20, 2008 09:38:42 AM
 * @author Shashank
 */

public class SSOTokenImpl implements SSOToken {

	/* This token is the key for any SSO integration. This is a unique ID that gets 
	 * passed	between applications having/ trying to establish single sign on	*/ 
	private String ssoToken;
	
	private String ssoError;
	private String ssoStatus;
	
	public SSOTokenImpl(Object tokenObject) {
		HttpServletRequest request = (HttpServletRequest)tokenObject;
		//GUID -> An example token identifier 
		this.ssoToken = request.getHeader("GUID");
	}

	public String getSSOToken() {
		return ssoToken;
	}

	public String getSSOError() {
		return ssoError;
	}

	public void setSSOError(String ssoError) {
		this.ssoError = ssoError;
	}

	public String getSSOStatus() {
		return ssoStatus;
	}

	public void setSSOStatus(String ssoStatus) {
		this.ssoStatus = ssoStatus;
	}

}
