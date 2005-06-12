/**
 * Copyright 2004-2005 jManage.org
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
package org.jmanage.webui.applets;

/**
 *
 * Date:  Jun 11, 2005
 * @author	Rakesh Kalra
 */
public interface GraphAppletParameters {
    /* Parameters for this applet */
    // title of the graph
    String GRAPH_TITLE = "graphTitle";
    // polling interval in seconds
    String POLLING_INTERVAL = "pollingInterval";
    // attributes to be graphed
    String ATTRIBUTES = "attributes";
    // the jmanage server url
    String REMOTE_URL = "remoteURL";
}
