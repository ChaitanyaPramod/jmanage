package org.jmanage.webui.taglib.html;

import org.jmanage.webui.util.RequestParams;
import org.jmanage.webui.util.Utils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.ServletRequest;
import java.io.IOException;

/**
 *
 * date:  Jun 20, 2004
 * @author	Rakesh Kalra
 */
public class LinkTag extends org.apache.struts.taglib.html.LinkTag {

    protected String calculateURL() throws JspException {
        String url = super.calculateURL();
        url = appendQueryStringParams(url);
        return url;
    }

    private String appendQueryStringParams(String url) {

        ServletRequest request = pageContext.getRequest();
        String applicationId =
                request.getParameter(RequestParams.APPLICATION_ID);
        if (applicationId != null) {
            url = Utils.appendURLParam(url, RequestParams.APPLICATION_ID,
                    applicationId);
        }
        String objectName =
                request.getParameter(RequestParams.OBJECT_NAME);
        if (applicationId != null) {
            url = Utils.appendURLParam(url, RequestParams.OBJECT_NAME,
                    objectName);
        }
        return url;
    }
}
