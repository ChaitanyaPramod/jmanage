/**
* Copyright (c) 2004-2005 jManage.org
*
* This is a free software; you can redistribute it and/or
* modify it under the terms of the license at
* http://www.jmanage.org.
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
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
