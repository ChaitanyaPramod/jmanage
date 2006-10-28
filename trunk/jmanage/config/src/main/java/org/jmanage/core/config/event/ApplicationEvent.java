/**
 * Copyright 2004-2006 jManage.org
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
package org.jmanage.core.config.event;

import java.util.EventObject;

import org.jmanage.core.config.ApplicationConfig;

/**
 * Base class for Application related events.
 * 
 * @author rkalra
 */
public abstract class ApplicationEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    private final long time;
    
    public ApplicationEvent(ApplicationConfig config){
      super(config);
      time = System.currentTimeMillis();
    }
    
    public ApplicationConfig getApplicationConfig(){
      return (ApplicationConfig)super.getSource();
    }
    
    public Long getTime() {
        return time;
    }
}
