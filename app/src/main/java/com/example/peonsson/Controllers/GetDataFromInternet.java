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
public class GetDataFromInternet extends AsyncTask<Object, Void, ArrayList<String>> {

    private static final String url = "http://maceo.sth.kth.se/Home/eurofxref";
    private final Context context;
    private final boolean isSortedAZ;

    private ArrayList<Currency> currencies;
    private OutputStream outputStream;
    private ArrayList<String> listViewData;
    private ArrayList<String> tempListViewData = new ArrayList<String>(25);
    private ArrayAdapter<String> fromListViewAdapter;
    private ArrayAdapter<String> toListViewAdapter;

    public GetDataFromInternet(Context context, boolean isSortedAZ) {
        this.context = context;
        this.isSortedAZ = isSortedAZ;
    }

    @Override
    protected ArrayList<String> doInBackground(Object... params) {
        System.out.println("Executing.. GetDataFromInternet.. doInBackground.. ");

        currencies =(ArrayList<Currency>) params[0];
        listViewData =(ArrayList<String>) params[1];
        outputStream = (OutputStream) params[2];
        fromListViewAdapter = (ArrayAdapter<String>) params[3];
        toListViewAdapter = (ArrayAdapter<String>) params[4];
        currencies.clear();
        tempListViewData.add("EUR");

        HttpURLConnection http = null;
        InputStream istream = null;

        try {
            //add time as long to top of document
            outputStream.write((Long.toString(new Date().getTime()) + "\n").getBytes());
            currencies.add(new Currency("EUR", "1"));

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
                        currencies.add(new Currency(xpp.getAttributeValue(0), xpp.getAttributeValue(1)));
                        tempListViewData.add(xpp.getAttributeValue(0));
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
        return tempListViewData;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        System.out.println("GetDataFromInternet.. onPostExecute..");

        if(isSortedAZ) {
            Collections.sort(strings);
        } else {
            Collections.sort(strings);
            Collections.reverse(strings);
        }

        System.out.println(strings);
//        listViewData = tempListViewData;

//        fromListViewAdapter = new CustomAdapter(context, strings);
        fromListViewAdapter.clear();
        fromListViewAdapter.addAll(strings);
        fromListViewAdapter.notifyDataSetChanged();
//        toListViewAdapter = new CustomAdapter(context, strings);

        toListViewAdapter.clear();
        toListViewAdapter.addAll(strings);
        toListViewAdapter.notifyDataSetChanged();

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
