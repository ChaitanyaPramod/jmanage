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
package org.jmanage.core.remote;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.IOException;

/**
 *
 * date:  Feb 11, 2005
 * @author	Rakesh Kalra
 */
public class DataMapping {

    private static final String MAPPING =
        "<?xml version=\"1.0\"?>\n"+
        "\n"+
        "<!DOCTYPE mapping PUBLIC \"-//EXOLAB/Castor Object Mapping DTD Version 1.0//EN\"\n"+
                                     "\"http://castor.exolab.org/mapping.dtd\">\n"+
            "<mapping>\n"+
                    "<description>Description of the mapping</description>\n"+
                    "<class name=\"org.jmanage.core.data.TestBean\" auto-complete=\"true\" />\n"+
                    "<class name=\"org.jmanage.core.data.ApplicationConfigData\" auto-complete=\"true\" />\n"+
                    "<class name=\"org.jmanage.core.data.OperationResultData\" auto-complete=\"true\" />\n"+
            "</mapping>\n";

    public static Mapping getMapping() throws IOException, MappingException {
        Mapping mapping = new Mapping();
        mapping.loadMapping(new InputSource(new StringReader(MAPPING)));
        return mapping;
    }
}
