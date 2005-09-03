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
package org.jmanage.core.alert.source;

import org.jmanage.core.alert.AlertSource;
import org.jmanage.core.alert.AlertHandler;
import org.jmanage.core.config.AlertSourceConfig;

/**
 * Date: Aug 31, 2005 11:41:59 AM
 * @author Bhavana
 */
public class GuageAlertSource extends AlertSource{

    public GuageAlertSource(AlertSourceConfig sourceConfig){
        super(sourceConfig);
    }
    public void register(AlertHandler handler){

    }
    public void unregister() {

    }
}
