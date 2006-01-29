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
 * <p>
 * Date:  Aug 31, 2005
 * @author	Rakesh Kalra
 */
public interface DataFormatMBean {

    /* XML */

    // test get and set
    public String getXML();
    public void setXML(String xml);
    // test read only attribute
    public String getXMLData();
    // test operation
    public String retrieveXMLData();

    /* HTML */

    // test get and set
    public String getHTML();
    public void setHTML(String html);
    // test read only attribute
    public String getHTMLData();
    // test operation
    public String retrieveHTMLData();
}
