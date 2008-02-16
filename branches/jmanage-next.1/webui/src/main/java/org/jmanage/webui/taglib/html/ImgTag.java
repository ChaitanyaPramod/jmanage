package org.jmanage.webui.taglib.html;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

public class ImgTag extends org.apache.struts.taglib.html.ImgTag {
	
	public int doEndTag() throws JspException {
		if(src != null)
			src = ((HttpServletRequest)pageContext.getRequest()).getContextPath() + src;
		return super.doEndTag();
	}

}
