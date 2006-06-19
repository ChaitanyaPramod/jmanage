/*
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

	function refreshDBComponent(component, timeout, appId, varName, varValue){
		var varURL = '/app/drawDashboardComponent.do?applicationId=' + appId + '&dashBID=jvmThreads&componentID=' + component + '&' + varName + '=' + varValue;
		//alert(varURL);
		dojo.io.bind({
			url: varURL,
	    	load: function(type, data, evt){
	    	   	if(type == "load"){
					var divElement = document.getElementById(component);
					//alert(data);
	        		divElement.innerHTML = data;		           
		       	}else if(type == "error"){
		           alert("error getting data from server");
		       	}else{
		           // other types of events might get passed, handle them here
		       	}
	    	  },
	    	mimetype: "text/plain"
		});
	
		funcName = "refreshDBComponent('"+ component + "', " + timeout + ",'" + appId + "','" + varName + "','" + varValue + "')";
		//alert(funcName);
		self.setTimeout(funcName, timeout);
	}

	var eventHandlers = new Array();
	
	// custom object
	function eventHandler(component, eventName, targetComponent, dataVar, applicationId){
		this.component = component;
		this.eventName = eventName;
		this.targetComponent = targetComponent;
		this.dataVar = dataVar;
		this.applicationId = applicationId;
	}

	function addEventHandler(component, eventName, targetComponent, dataVar, applicationId){
		var handlerObj = new eventHandler(component, eventName, targetComponent, dataVar, applicationId);
		eventHandlers.push(handlerObj);	
	}

	function handleEvent(component, eventName, data){
		for(i=0; i<eventHandlers.length; i++){
			if(eventHandlers[i].component == component && eventHandlers[i].eventName == eventName){
				refreshDBComponent(eventHandlers[i].targetComponent, 10000, eventHandlers[i].applicationId, eventHandlers[i].dataVar, data);
			} 	
		}	
	}




