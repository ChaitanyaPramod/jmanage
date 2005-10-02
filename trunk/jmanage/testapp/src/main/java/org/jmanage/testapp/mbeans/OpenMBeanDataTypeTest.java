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

import javax.management.openmbean.*;

/**
 *
 * <p>
 * Date:  Sep 26, 2005
 * @author	Rakesh Kalra
 */
public class OpenMBeanDataTypeTest implements OpenMBeanDataTypeTestMBean {

    private TabularData tabularData;
    private CompositeData compositeData;

    public OpenMBeanDataTypeTest() throws OpenDataException {

        String[] itemNames = {
            "Name",
            "NumberOfFloors",
            "Height",
            "UndergroundParking",
            "NumberOfElevators",
            "OfficeSpage"};

        String[] itemDescriptions = {
            "Name of the building",
            "The number of floors the building has",
            "The height of the building in feet",
            "Whether or not the building has underground parking",
            "The total number of elevators in the building",
            "The amount of office space in square feet"
        };

        OpenType[] itemTypes = {
            SimpleType.STRING,
            SimpleType.SHORT,
            SimpleType.INTEGER,
            SimpleType.BOOLEAN,
            SimpleType.SHORT,
            SimpleType.LONG
        };

        CompositeType buildingType = new CompositeType(
            "BuildingCompositeType",
            "CompositeType that represents a building",
            itemNames,
            itemDescriptions,
            itemTypes);

        Object[] itemValuesA = {
            "Building A",
            new Short((short)3),
            new Integer(45),
            Boolean.FALSE,
            new Short((short)1),
            new Long(10000)
        };

        Object[] itemValuesB = {
            "Building B",
            new Short((short)5),
            new Integer(66),
            Boolean.FALSE,
            new Short((short)2),
            new Long(20000)
        };

        compositeData = new CompositeDataSupport(buildingType,
                itemNames, itemValuesA);

        TabularType tabularType =
                new TabularType(TabularData.class.getName(),
                        "Table containing building info",
                        buildingType,
                        new String[]{"Name"});
        tabularData = new TabularDataSupport(tabularType);
        tabularData.put(compositeData);
        tabularData.put(new CompositeDataSupport(buildingType, itemNames,
                itemValuesB));
    }

    public TabularData getTabularData() {
        return tabularData;
    }

    public CompositeData getCompositeData() {
        return compositeData;
    }

}
