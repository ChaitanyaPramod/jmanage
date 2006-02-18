/**
 * Copyright 2004-2005 jManage.org. All rights reserved.
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
package org.jmanage.webui.taglib.jm;

import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 *
 * <p>
 * Date:  Feb 4, 2006
 * @author	Rakesh Kalra
 */
public class BaseTag implements BodyTag {

    protected PageContext pageContext;
    protected BodyContent bodyContent;
    private Tag parent;

    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    public void setBodyContent(BodyContent bodyContent) {
        this.bodyContent = bodyContent;
    }

    public void setParent(Tag tag) {
        this.parent = tag;
    }

    public Tag getParent() {
        return parent;
    }

    public void doInitBody() throws JspException {
    }

    public int doAfterBody() throws JspException {
        return 0;
    }

    public int doStartTag() throws JspException {
        return Tag.EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException {
        return Tag.EVAL_PAGE;
    }

    public void release() {
    }
}
