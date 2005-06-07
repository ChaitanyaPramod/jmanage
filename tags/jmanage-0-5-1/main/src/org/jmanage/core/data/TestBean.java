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
package org.jmanage.core.data;

/**
 * Used for unit testing Marshalling/Unmarshalling.
 *
 * date:  Feb 4, 2005
 * @author	Rakesh Kalra
 */
public class TestBean{

    private String x;
    private Integer y;

    public TestBean(){}

    public TestBean(String x, Integer y){
        this.x = x;
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public boolean equals(Object obj){
        if(obj instanceof TestBean){
            TestBean bean = (TestBean)obj;
            return bean.x.equals(this.x) && bean.y.equals(this.y);
        }
        return false;
    }
}
