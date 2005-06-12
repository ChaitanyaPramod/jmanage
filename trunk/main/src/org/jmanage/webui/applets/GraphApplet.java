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
package org.jmanage.webui.applets;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

import java.applet.Applet;
import java.awt.*;
import java.util.Date;
import java.util.Properties;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.net.*;
import java.io.*;

/**
 *
 * Date:  May 27, 2005
 * @author	Rakesh Kalra
 */
public class GraphApplet extends Applet implements GraphAppletParameters {

    private JFreeChart chart;
    private String graphTitle;
    // polling interval in seconds
    private long pollingInterval;
    // attributes to be graphed
    private String attributes;
    // remoteURL to get the data from
    private URL remoteURL;

    public void init() {
        // read parameters
        graphTitle = getParameter(GRAPH_TITLE);
        pollingInterval = Long.parseLong(getParameter(POLLING_INTERVAL));
        attributes = getParameter(ATTRIBUTES);
        try {
            remoteURL = new URL(getParameter(REMOTE_URL));
        } catch (MalformedURLException e) {
            new RuntimeException(e);
        }

        /* get initial set of values for the graph */
        poll();

        /* create the chart */
        chart = ChartFactory.createTimeSeriesChart(
                        graphTitle, // chart title
                        "Time", // domain axis label
                        "Attributes", // range axis label
                        dataset, // data
                        true, // include legend
                        true, // tooltips
                        false // urls. would be a pretty good extension so that you could click on a task line to open the document in notes
                    );

        new MyThread().start();
    }

    //this is called automatically when the applet is loaded and resized
    public void paint(Graphics g) {
        chart.draw((Graphics2D)g, getBounds()); //repaints the whole chart
    }

    private class MyThread extends Thread{
        public void run(){
            while(true){
                try {
                    Thread.sleep(pollingInterval * 1000);
                } catch (InterruptedException e) {
                }
                /* get new data */
                poll();
                /* repaint */
                GraphApplet.this.repaint();
            }
        }
    }

    private TimeSeriesCollection dataset;
    private TimeSeries[] series;

    private void poll() {
        Properties properties = getNewValues();
        long timestamp = Long.parseLong(properties.getProperty("timestamp"));
        String attributes = properties.getProperty("attributes");
        String values = properties.getProperty("values");

        if(dataset == null){
            dataset = new TimeSeriesCollection();
            dataset.setDomainIsPointsInTime(false);
            StringTokenizer tokenizer = new StringTokenizer(attributes, "|");
            series = new TimeSeries[tokenizer.countTokens()];
            for(int i=0; tokenizer.hasMoreTokens(); i++){
                series[i] = new TimeSeries(tokenizer.nextToken(), Second.class);
                dataset.addSeries(series[i]);
            }
        }

        /* set the next set of values */
        StringTokenizer tokenizer = new StringTokenizer(values, "|");
        assert series.length == tokenizer.countTokens();
        Second second = new Second(new Date(timestamp));
        for(int i=0; i<series.length; i++){
            double attrValue = Double.parseDouble(tokenizer.nextToken());
            series[i].add(second, attrValue);
        }
    }

    private Properties getNewValues(){

        String output = doHttpPost();
        Properties properties = new Properties();
        try {
            properties.load(new ByteArrayInputStream(output.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }


    private String doHttpPost(){

        try {
            String queryString="attributes=" +
                    URLEncoder.encode(attributes, "UTF-8");
            HttpURLConnection conn = (HttpURLConnection) remoteURL.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            // Sets the default Content-type and content lenght for POST requests
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-length", Integer.toString(queryString.length()));
            // Gets the output stream and POSTs data
            OutputStream postStream = conn.getOutputStream();
            OutputStreamWriter postWriter = new OutputStreamWriter(postStream);
            postWriter.write(queryString);
            postWriter.flush();
            postWriter.close();

            // check the response code
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                // todo: what should the user see?
                return null;
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuffer output = new StringBuffer();
            String outputLine = reader.readLine();
            while(outputLine != null){
                output.append(outputLine + "\n");
                outputLine = reader.readLine();
            }

            return output.toString();

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
