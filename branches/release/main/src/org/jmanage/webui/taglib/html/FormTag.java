package org.jmanage.webui.taglib.html;

import org.jmanage.webui.util.RequestParams;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.ServletRequest;
import java.io.IOException;

/**
 *
 * date:  Jun 20, 2004
 * @author	Rakesh Kalra
 */
public class FormTag extends org.apache.struts.taglib.html.FormTag {

    private static final String HIDDEN_FIELD_APP_ID_BEGIN =
            "<input type=\"hidden\" name=\"" + RequestParams.APPLICATION_ID + "\" value=\"" ;
    private static final String HIDDEN_FIELD_OBJECT_NAME_BEGIN =
            "<input type=\"hidden\" name=\"" + RequestParams.OBJECT_NAME + "\" value=\"" ;
    private static final String HIDDEN_FIELD_END =
            "\"/>";

    /**
     * Render the beginning of this form.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        int result = super.doStartTag();
        addHiddenFields();
        return result;
    }

    private void addHiddenFields()
        throws JspException{
        try {
            final ServletRequest request = pageContext.getRequest();
            final JspWriter writer = pageContext.getOut();
            String applicationId =
                    request.getParameter(RequestParams.APPLICATION_ID);
            if(applicationId != null){
                writer.println();
                writer.print(HIDDEN_FIELD_APP_ID_BEGIN);
                writer.print(applicationId);
                writer.println(HIDDEN_FIELD_END);
            }
            String objectName =
                    request.getParameter(RequestParams.OBJECT_NAME);
            if(objectName != null){
                writer.print(HIDDEN_FIELD_OBJECT_NAME_BEGIN);
                writer.print(objectName);
                writer.println(HIDDEN_FIELD_END);
            }

        } catch (IOException e) {
            throw new JspException(e);
        }
    }
}
