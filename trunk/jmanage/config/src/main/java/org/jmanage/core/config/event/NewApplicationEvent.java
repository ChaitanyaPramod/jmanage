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

import org.jmanage.core.config.ApplicationConfig;

/**
 * This event is fired when a new application is added to the system.
 *  
 * @author rkalra
 */
public class NewApplicationEvent extends ApplicationEvent {

  private static final long serialVersionUID = 1L;

  public NewApplicationEvent(ApplicationConfig config){
    super(config);
  }
}
  