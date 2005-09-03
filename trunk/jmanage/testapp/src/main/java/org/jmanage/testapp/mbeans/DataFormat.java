/**
 * Copyright (c) 2004-2005 jManage.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.jmanage.testapp.mbeans;

/**
 *
 *
 * <p>
 * Date:  Aug 31, 2005
 * @author	Rakesh Kalra
 */
public class DataFormat implements DataFormatMBean{

    String xml1 = "<?xml version=\"1.0\"?>\n\n<root><a>1</a><b>2</b></root>";
    String xml2 = "<root><a>1</a><b>2</b></root>";
    String html = "<b>This is a test</b>";

    // test get and set
    public String getXML() {
        return xml1;
    }

    public void setXML(String xml) {
        this.xml1 = xml;
    }

    // test read only attribute
    public String getXMLData() {
        return xml2;
    }

    // test operation
    public String retrieveXMLData() {
        return xml1;
    }

    // test get and set
    public String getHTML() {
        return html;
    }

    public void setHTML(String html) {
        this.html = html;
    }

    // test read only attribute
    public String getHTMLData() {
        return html;
    }

    // test operation
    public String retrieveHTMLData() {
        return html;
    }
}
