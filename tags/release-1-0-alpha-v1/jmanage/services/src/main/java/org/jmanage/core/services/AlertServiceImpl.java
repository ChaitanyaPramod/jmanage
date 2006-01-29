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
package org.jmanage.core.services;

import org.jmanage.core.alert.delivery.ConsoleAlerts;
import org.jmanage.core.util.CoreUtils;
import org.jmanage.core.data.AlertData;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 *
 * Date:  Aug 3, 2005
 * @author	Rakesh Kalra
 */
public class AlertServiceImpl implements AlertService {

    public List getConsoleAlerts(ServiceContext context)
            throws ServiceException {

        List alerts = new LinkedList();
        // convert to a list of AlertInfo objects into a list of AlertData objects
        for(Iterator it=ConsoleAlerts.getAll().iterator(); it.hasNext();){
            AlertData alertData = new AlertData();
            CoreUtils.copyProperties(alertData, it.next());

            alerts.add(alertData);
        }
        return alerts;
    }

    public void removeConsoleAlert(ServiceContext context,
                                   String alertId) {
        ConsoleAlerts.remove(alertId);
    }
}
