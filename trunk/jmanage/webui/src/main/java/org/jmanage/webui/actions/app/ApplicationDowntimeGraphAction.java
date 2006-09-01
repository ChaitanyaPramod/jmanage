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
package org.jmanage.webui.actions.app;

import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
import org.jmanage.monitoring.downtime.ApplicationDowntimeService;
import org.jmanage.monitoring.downtime.DowntimeRecorder;
import org.jmanage.webui.actions.BaseAction;
import org.jmanage.webui.util.WebContext;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 
 * @author Rakesh Kalra
 */
public class ApplicationDowntimeGraphAction extends BaseAction {

    public ActionForward execute(WebContext context, ActionMapping mapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        response.setContentType("image/jpeg");
        int width = 300;
        int height = 200;
        if (request.getParameter("width") != null)
            width = Integer.parseInt(request.getParameter("width"));
        if (request.getParameter("height") != null)
            height = Integer.parseInt(request.getParameter("height"));
        
        DowntimeRecorder recorder = ApplicationDowntimeService.getInstance().getDowntimeRecorder();
        double unavailable = recorder.getUnavailablePercentage(context.getApplicationConfig()); 
        
        drawImage(response.getOutputStream(), width, height, unavailable);
        return null;
    }

    private static void drawImage(OutputStream os, int width, int height, double unavailable) 
        throws Exception {
        // create dataset...
        final PieDataset dataset = createDataset(unavailable);
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
    private static PieDataset createDataset(double unavailable) {

        final DefaultPieDataset result = new DefaultPieDataset();
        result.setValue("Unavailable", new Double(unavailable));
        result.setValue("Available", new Double(100.0 - unavailable));
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
        chart.setBorderPaint(new Color(230, 238, 249));

        final PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 10));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelLinkPaint(Color.red);
        plot.setLabelGap(0.02);
        
        // set paint for unavailable/available sections
        plot.setSectionPaint(0, Color.RED);
        plot.setSectionPaint(1, Color.GREEN);
        
        plot.setBackgroundPaint(new Color(230, 238, 249));
        
        //plot.set
        return chart;
    }
    
    public static void main(String[] args) throws Exception{
        FileOutputStream fos = new FileOutputStream("c:/temp/out.jpg");
        drawImage(fos, 300, 200, 10.0);
    }
}
