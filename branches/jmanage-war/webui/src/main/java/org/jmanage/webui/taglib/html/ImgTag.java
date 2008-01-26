/**
 * jManage - Open Source Application Management
 * Copyright (C) 2004-2008 jManage.org
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License version 2 for more details.
 */

package org.jmanage.webui.taglib.html;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 * @author Shashank Bellary
 * Date: Jan 26, 2008
 */
public class ImgTag extends org.apache.struts.taglib.html.ImgTag {
	
	public int doEndTag() throws JspException {
		if(src != null){
			src = (String)ExpressionEvaluatorManager.evaluate("src", src, String.class, this, pageContext);
			src = ((HttpServletRequest)pageContext.getRequest()).getContextPath() + src;
		}
		return super.doEndTag();
	}

}
