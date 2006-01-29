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
package org.jmanage.core.management.data;

/**
 * This is the default DataFormat for types that do not have a specific
 * DataFormat defined.
 *
 * <p>
 * Date:  Dec 9, 2005
 * @author	Rakesh Kalra
 */
public class DefaultDataFormat implements DataFormat {

    public String format(Object data) {
        String output = data.toString();
        if(DataFormatUtil.isEscapeHtml()){
            // todo: do we need this? We are now using <pre> tag.
            output = htmlEscape(output);
        }
        return output;
    }

    private static String htmlEscape(String str){
        StringBuffer buff = new StringBuffer(str.length());
        for(int i=0; i<str.length(); i++){
            final char ch = str.charAt(i);
            if(ch == '"'){
                buff.append("&quot;");
            }else if(ch == '<'){
                buff.append("&lt;");
            }else if(ch == '>'){
                buff.append("&gt;");
            }else{
                buff.append(ch);
            }
        }
        return buff.toString();
    }
}
