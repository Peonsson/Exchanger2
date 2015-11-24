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
public class GetDataFromLocalFile extends AsyncTask<Object,Void,ArrayList<Currency>> {

    private final MainActivity activity;
    private FileInputStream fileInputStream;
    private ArrayList<Currency> tempCurrencices = new ArrayList<Currency>(25);

    public GetDataFromLocalFile(FileInputStream fileInputStream, MainActivity activity) {
        this.fileInputStream = fileInputStream;
        this.activity = activity;
    }

    @Override
    protected ArrayList<Currency> doInBackground(Object... params) {
        System.out.println("Executing.. GetDataFromLocalFile.. doInBackground..");

        tempCurrencices.add(new Currency("USD", "1"));

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(" ");
                if(parts.length == 2) {
                    String name = parts[0];
                    String rate = parts[1];
                    tempCurrencices.add(new Currency(name, rate));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempCurrencices;
    }

    @Override
    protected void onPostExecute(ArrayList<Currency> strings) {
        super.onPostExecute(strings);
        System.out.println("GetDataFromLocalFile.. onPostExecute..");
        activity.updateUILocal(strings);

        try {
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
