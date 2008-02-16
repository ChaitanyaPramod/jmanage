/**
 * jManage - Open Source Application Management
 * Copyright (C) 2004-2006 jManage.org
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License version 2 for more details.
 */

package org.jmanage.webui.actions.app;

import java.awt.Color;
import java.awt.Font;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.DefaultPieDataset;
import org.jfree.data.PieDataset;
import org.jmanage.core.management.ObjectAttribute;
import org.jmanage.core.services.MBeanService;
import org.jmanage.core.services.ServiceContext;
import org.jmanage.core.services.ServiceFactory;
import org.jmanage.core.util.Expression;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.Utils;
import org.jmanage.webui.util.WebContext;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
/**
 * 
 * @author Shashank Bellary
 * date Jan 05, 2007
 */
public class DrawPieChartDashboardComponentAction extends BaseAction {
	private String attribRepWhole;

	/**
	 * 
	 */
	public ActionForward execute(WebContext context, ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

        response.setContentType("image/jpeg");
        int width = 400;
        int height = 300;
        if (request.getParameter("width") != null)
            width = Integer.parseInt(request.getParameter("width"));
        if (request.getParameter("height") != null)
            height = Integer.parseInt(request.getParameter("height"));
        String displayNames = request.getParameter("displayNames");
        String attributes = request.getParameter("attributes");
        assert attributes != null;
        List exprList = parse(attributes);
        Map<String, ObjectAttribute> attrMap = new HashMap<String, ObjectAttribute>();
        Map<String, ObjectAttribute> wholeAttrMap = new HashMap<String, ObjectAttribute>();
        StringTokenizer st = new StringTokenizer(displayNames, "|");
        MBeanService mbeanService = ServiceFactory.getMBeanService();
        for (Object anExpression : exprList) {
            Expression expression = (Expression) anExpression;
            ServiceContext srvcContext = null;
            ObjectAttribute objAttribute = null;
            try {
                srvcContext = Utils.getServiceContext(context, expression);
                objAttribute = mbeanService.getObjectAttribute(srvcContext,
                        expression.getTargetName());
            } finally {
                if (srvcContext != null)
                    srvcContext.getServerConnection().close();
            }
            assert objAttribute != null;
            assert objAttribute.getStatus() == ObjectAttribute.STATUS_OK;
            if(attribRepWhole.equals(expression.toString()))
            	wholeAttrMap.put(st.nextToken(), objAttribute);
            else
            	attrMap.put(st.nextToken(), objAttribute);
        }

        drawImage(response.getOutputStream(), width, height, wholeAttrMap, attrMap);
        return null;
    }

    /**
    *
    * @param attributes
    * @return list of Expression objects
    */
   private List parse(String attributes) throws Exception{
       attributes = URLDecoder.decode(attributes, "UTF-8");
       List<Expression> exprList = new LinkedList<Expression>();
       StringTokenizer tokenizer = new StringTokenizer(attributes, ",");
       while(tokenizer.hasMoreTokens()){
           String expression = tokenizer.nextToken();
           assert expression.startsWith("[");
           while(!expression.endsWith("]")){
               assert tokenizer.hasMoreTokens();
               expression += "," + tokenizer.nextToken();
           }
           expression = expression.substring(1, expression.length()-1);
           if(expression.endsWith("|true")){
        	   expression = expression.substring(0, expression.indexOf("|true"));
        	   attribRepWhole = expression;
           }
           exprList.add(new Expression(expression));
       }
       return exprList;
   }

    private static void drawImage(OutputStream os, int width, int height, 
    		Map<String, ObjectAttribute> whole, Map<String, ObjectAttribute> others) throws Exception {
        // create dataset...
        final PieDataset dataset = createDataset(whole, others);
        // create the chart...
        final JFreeChart chart = createChart(dataset);
        // return as image
        // Encode as a JPEG
        JPEGImageEncoder jpeg = JPEGCodec.createJPEGEncoder(os);
        jpeg.encode(chart.createBufferedImage(width, height));
    }
    
    /**
     * Creates a sample dataset for the demo.
     * 
     * @return A sample dataset.
     */
    private static PieDataset createDataset(Map<String, ObjectAttribute> whole, Map<String, ObjectAttribute> others) {

        final DefaultPieDataset result = new DefaultPieDataset();
        if(whole.isEmpty()){
        	double total = 0;
        	for(String attribDispText : others.keySet()){
        		ObjectAttribute otherAttrib = others.get(attribDispText);
        		double segment = ((Long)otherAttrib.getValue()).doubleValue();
        		total += segment;
        	}
        	for(String attribDispText : others.keySet()){
        		ObjectAttribute otherAttrib = others.get(attribDispText);
        		attribDispText += " in %";
        		double segment = ((Long)otherAttrib.getValue()).doubleValue();
        		segment = (100 * segment)/total;
        		result.setValue(attribDispText, new Double(segment));
        	}
        }else{
        	String displayText = whole.keySet().iterator().next();
        	ObjectAttribute attrib = whole.get(displayText);
        	double wholeValue = ((Long)attrib.getValue()).doubleValue();
        	double sumOthers = 0;
        	for(String attribDispText : others.keySet()){
        		ObjectAttribute otherAttrib = others.get(attribDispText);
        		attribDispText += " in %";
        		double segment = ((Long)otherAttrib.getValue()).doubleValue();
        		segment = (100 * segment)/wholeValue;
        		result.setValue(attribDispText, new Double(segment));
        		sumOthers += segment;
        	}
        	displayText += " in %";
	        result.setValue(displayText, new Double(100.0 - sumOthers));
        }
        return result;
    }

    /**
     * Creates a sample chart.
     * 
     * @param dataset
     *            the dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(final PieDataset dataset) {

        final JFreeChart chart = ChartFactory.createPieChart(
                null, // chart title
                dataset, // data
                false, // include legend
                false, 
                false);
        // E6EEF9
        chart.setBackgroundPaint(new Color(230, 238, 249));
        chart.setBorderVisible(false);
        
        final PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelLinkPaint(Color.red);
        plot.setLabelGap(0.02);
        plot.setOutlinePaint(new Color(230, 238, 249));
        
        // set paint for unavailable/available sections
        int itemCount = dataset.getItemCount();
        for(int ctr=0; ctr < itemCount; ctr++){
        	if(ctr % 2 == 0)
        		plot.setSectionPaint(ctr, Color.BLUE);
        	else
                plot.setSectionPaint(ctr, Color.RED);
        }
        plot.setBackgroundPaint(new Color(230, 238, 249));
        //plot.set
        return chart;
    }
}
