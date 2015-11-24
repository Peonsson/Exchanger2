package com.example.peonsson.Controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import Models.Currency;

/**
 * Created by Peonsson on 2015-11-19.
 */
public class GetDataFromInternet extends AsyncTask<Object, Void, ArrayList<Currency>> {

    private static final String url = "http://maceo.sth.kth.se/Home/eurofxref";

    private ArrayList<Currency> tempCurrencies = new ArrayList<Currency>(25);
    private OutputStream outputStream;
    private MainActivity activity;

    public GetDataFromInternet(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Currency> doInBackground(Object... params) {
        System.out.println("Executing.. GetDataFromInternet.. doInBackground.. ");

        outputStream = (OutputStream) params[0];

        HttpURLConnection http = null;
        InputStream istream = null;

        try {
            //add time as long to top of document
            outputStream.write((Long.toString(new Date().getTime()) + "\n").getBytes());
            tempCurrencies.add(new Currency("EUR", "1"));

            URL text = new URL(url);
            http = (HttpURLConnection) text.openConnection();
            istream = http.getInputStream();
            System.out.println(istream.toString());
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(istream, null);
            String writeToFile;

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {

                } else if(eventType == XmlPullParser.START_TAG && xpp.getAttributeCount() > 0) {
                    if(xpp.getAttributeName(0).equalsIgnoreCase("time")) {
                        outputStream.write((xpp.getAttributeValue(0) + "\n").getBytes());
                    }
                    else if(xpp.getAttributeCount() == 2 && xpp.getAttributeName(0).equalsIgnoreCase("currency") && xpp.getAttributeName(1).equalsIgnoreCase("rate")) {
                        writeToFile = xpp.getAttributeValue(0) + " " + xpp.getAttributeValue(1) + "\n";
                        tempCurrencies.add(new Currency(xpp.getAttributeValue(0), xpp.getAttributeValue(1)));
                        outputStream.write(writeToFile.getBytes());
                    }
                }
                eventType = xpp.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return tempCurrencies;
    }

    @Override
    protected void onPostExecute(ArrayList<Currency> strings) {
        super.onPostExecute(strings);
        System.out.println("GetDataFromInternet.. onPostExecute..");
        activity.updateUI(strings);

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
