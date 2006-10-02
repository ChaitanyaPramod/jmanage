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

import javax.management.openmbean.TabularData;
import javax.management.openmbean.CompositeData;

/**
 *
 * <p>
 * Date:  Sep 26, 2005
 * @author	Rakesh Kalra
 */
public interface OpenMBeanDataTypeTestMBean {

    public TabularData getTabularData();
    public void setTabularData(TabularData tabularData);

    public CompositeData getCompositeData();
    public void setCompositeData(CompositeData compositeData);
}
