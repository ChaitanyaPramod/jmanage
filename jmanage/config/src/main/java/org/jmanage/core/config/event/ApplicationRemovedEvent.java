/**
* Copyright (c) 2004-2006 jManage.org
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
package org.jmanage.core.config.event;

import org.jmanage.core.config.ApplicationConfig;

/**
 * This event gets fired when an application is removed.
 * @author rkalra
 */
public class ApplicationRemovedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    public ApplicationRemovedEvent(ApplicationConfig config){
        super(config);
    }
}
