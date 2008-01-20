package org.jmanage.webui.taglib.html;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

public class ImgTag extends org.apache.struts.taglib.html.ImgTag {
	
	public int doEndTag() throws JspException {
		if(src != null){
			src = (String)ExpressionEvaluatorManager.evaluate("src", src, String.class, this, pageContext);
			src = ((HttpServletRequest)pageContext.getRequest()).getContextPath() + src;
		}
		return super.doEndTag();
	}

}
