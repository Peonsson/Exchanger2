package com.example.peonsson.Controllers;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import Models.Currency;

/**
 * Created by Peonsson on 2015-11-19.
 */
public class GetDataFromLocalFile extends AsyncTask<Object,Void,ArrayList<Currency>> {

    private ArrayList<Currency> currencies;
    private FileInputStream fileInputStream;
    private ArrayList<String> listViewData;
    private ArrayAdapter<String> fromListViewAdapter;
    private ArrayAdapter<String> toListViewAdapter;

    public GetDataFromLocalFile(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    @Override
    protected ArrayList<Currency> doInBackground(Object... params) {
        System.out.println("Executing.. GetDataFromLocalFile.. doInBackground..");

        currencies = (ArrayList<Currency>) params[0];
        listViewData = (ArrayList<String>) params[1];
        fromListViewAdapter = (ArrayAdapter<String>) params[2];
        toListViewAdapter = (ArrayAdapter<String>) params[3];

        try {
            currencies.add(new Currency("EUR", "1"));
            listViewData.add("EUR");

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" ");
                if(parts.length == 2) {
                    String name = parts[0];
                    String rate = parts[1];
                    currencies.add(new Currency(name, rate));
                    listViewData.add(name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currencies;
    }

    @Override
    protected void onPostExecute(ArrayList<Currency> strings) {
        System.out.println("GetDataFromLocalFile.. onPostExecute..");
        super.onPostExecute(strings);

        fromListViewAdapter.notifyDataSetChanged();
        toListViewAdapter.notifyDataSetChanged();
    }
}
