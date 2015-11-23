package com.example.peonsson.Controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import Models.Currency;

/**
 * Created by Peonsson on 2015-11-19.
 */
public class GetDataFromLocalFile extends AsyncTask<Object,Void,ArrayList<String>> {

    private final Context context;
    private final boolean isSortedAZ;
    private ArrayList<Currency> currencies;
    private FileInputStream fileInputStream;
    private ArrayList<String> listViewData;
    private ArrayAdapter<String> fromListViewAdapter;
    private ArrayAdapter<String> toListViewAdapter;
    private ArrayList<String> tempListViewData = new ArrayList<String>(25);

    public GetDataFromLocalFile(Context context, FileInputStream fileInputStream, boolean isSortedAZ) {
        this.context = context;
        this.fileInputStream = fileInputStream;
        this.isSortedAZ = isSortedAZ;
    }

    @Override
    protected ArrayList<String> doInBackground(Object... params) {
        System.out.println("Executing.. GetDataFromLocalFile.. doInBackground..");

        currencies = (ArrayList<Currency>) params[0];
        listViewData = (ArrayList<String>) params[1];
        fromListViewAdapter = (ArrayAdapter<String>) params[2];
        toListViewAdapter = (ArrayAdapter<String>) params[3];
        currencies.clear();
        currencies.add(new Currency("EUR", "1"));
        tempListViewData.add("EUR");

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" ");
                if(parts.length == 2) {
                    String name = parts[0];
                    String rate = parts[1];
                    currencies.add(new Currency(name, rate));
                    tempListViewData.add(name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempListViewData;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
        System.out.println("GetDataFromLocalFile.. onPostExecute..");

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
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
