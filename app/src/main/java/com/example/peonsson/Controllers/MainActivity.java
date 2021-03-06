package com.example.peonsson.Controllers;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import Models.Currencies;
import Models.Currency;

public class MainActivity extends AppCompatActivity {

    private Currencies myCurrencies = new Currencies();

    private GetDataFromInternet getDataFromInternet;
    private GetDataFromLocalFile getDataFromLocalFile;

    private ListView fromListView;
    private int fromListViewPosition;
    private String fromListViewItem;
    private ListView toListView;
    private String toListViewItem;
    private int toListViewPosition;

    private ArrayAdapter<String> fromListViewAdapter;
    private ArrayAdapter<String> toListViewAdapter;

    private EditText amount;

    private TextView result;
    private TextView date;

    private boolean isSortedAZ = true;

    private long timeBetweenUpdates = TimeUnit.HOURS.toMillis(1);

    private static final int REQUEST_CODE_SETTINGS = 100;

    long timeInLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("MainActivity.. onCreate..");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        date = (TextView) findViewById(R.id.textView5);

        //fromListView
        fromListView = (ListView) findViewById(R.id.listView);
        fromListViewAdapter = new CustomAdapter(this, myCurrencies.getList(isSortedAZ));
        fromListView.setAdapter(fromListViewAdapter);
        fromListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fromListViewItem = (String) parent.getItemAtPosition(position);
                System.out.println(parent.getItemAtPosition(position));
                fromListViewPosition = position;

                //Computing result
                ArrayList<Currency> currencies = myCurrencies.getCurrencies();
                for (int i = 0; i < currencies.size(); i++) {
                    if (currencies.get(i).getName().equalsIgnoreCase(fromListViewItem)) {
                        double rate = Double.parseDouble(currencies.get(i).getRate());
                        for (int j = 0; j < currencies.size(); j++) {
                            if (currencies.get(j).getName().equalsIgnoreCase(toListViewItem)) {
                                if (amount.getText().toString() != null && !amount.getText().toString().equals("")) {
                                    double amountToConvert = Double.parseDouble(amount.getText().toString());
                                    double doubleResult = (amountToConvert / rate) * Double.parseDouble(currencies.get(j).getRate());
                                    String showResult = String.format("%.1f", doubleResult);
                                    result.setText(amountToConvert + " " + currencies.get(i).getName() + " --> " + currencies.get(j).getName() + " = " + showResult);
                                    System.out.println("onItemClick");
                                    break;
                                } else {
                                    result.setText("Result goes here");
                                }
                            }
                        }
                    }
                }
            }
        });

        //toListView
        toListView = (ListView) findViewById(R.id.listView2);
        toListViewAdapter = new CustomAdapter(this, myCurrencies.getList(isSortedAZ));
        toListView.setAdapter(toListViewAdapter);
        toListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toListViewItem = (String) parent.getItemAtPosition(position);
                System.out.println(parent.getItemAtPosition(position));
                toListViewPosition = position;

                //Computing result
                ArrayList<Currency> currencies = myCurrencies.getCurrencies();
                for (int i = 0; i < currencies.size(); i++) {
                    if (currencies.get(i).getName().equalsIgnoreCase(fromListViewItem)) {
                        double rate = Double.parseDouble(currencies.get(i).getRate());
                        for (int j = 0; j < currencies.size(); j++) {
                            if (currencies.get(j).getName().equalsIgnoreCase(toListViewItem)) {
                                if (amount.getText().toString() != null && !amount.getText().toString().equals("")) {
                                    double amountToConvert = Double.parseDouble(amount.getText().toString());
                                    double doubleResult = (amountToConvert / rate) * Double.parseDouble(currencies.get(j).getRate());
                                    String showResult = String.format("%.1f", doubleResult);
                                    result.setText(amountToConvert + " " + currencies.get(i).getName() + " --> " + currencies.get(j).getName() + " = " + showResult);
                                    System.out.println("onItemClick");
                                    break;
                                } else {
                                    result.setText("Result goes here");
                                }
                            }
                        }
                    }
                }
            }
        });

        //result
        result = (TextView) findViewById(R.id.textView4);

        //amount
        amount = (EditText) findViewById(R.id.editText);
        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (hasFocus) {
                    imm.showSoftInput(amount, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    imm.hideSoftInputFromWindow(amount.getWindowToken(), 0);
                }
            }
        });
        amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Computing result
                ArrayList<Currency> currencies = myCurrencies.getCurrencies();
                for (int i = 0; i < currencies.size(); i++) {
                    if (currencies.get(i).getName().equalsIgnoreCase(fromListViewItem)) {
                        double rate = Double.parseDouble(currencies.get(i).getRate());
                        for (int j = 0; j < currencies.size(); j++) {
                            if (currencies.get(j).getName().equalsIgnoreCase(toListViewItem)) {
                                if (amount.getText().toString() != null && !amount.getText().toString().equals("")) {
                                    double amountToConvert = Double.parseDouble(amount.getText().toString());
                                    double doubleResult = (amountToConvert / rate) * Double.parseDouble(currencies.get(j).getRate());
                                    String showResult = String.format("%.1f", doubleResult);
                                    result.setText(amountToConvert + " " + currencies.get(i).getName() + " --> " + currencies.get(j).getName() + " = " + showResult);
                                    System.out.println("afterTextChanged");
                                    break;
                                } else {
                                    result.setText("Result goes here");
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("MainActivity.. onPause..");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("MainActivity.. onResume..");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("MainActivity.. onSaveInstanceState..");

        outState.putString("FROM_LIST_VIEW_ITEM", fromListViewItem);
        outState.putString("TO_LIST_VIEW_ITEM", toListViewItem);

        outState.putString("AMOUNT_TEXT", amount.getText().toString());
        outState.putString("RESULT_TEXT", result.getText().toString());

        outState.putInt("FROM_LIST_POSITION", fromListViewPosition);
        outState.putInt("TO_LIST_POSITION", toListViewPosition);

        outState.putBoolean("IS_SORTED_AZ", isSortedAZ);
        outState.putLong("TIME_BETWEEN_UPDATES", timeBetweenUpdates);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println();
        if (savedInstanceState != null) {
            System.out.println("MainActivity.. onRestoreInstanceState..");

            fromListViewItem = savedInstanceState.getString("FROM_LIST_VIEW_ITEM");
            toListViewItem = savedInstanceState.getString("TO_LIST_VIEW_ITEM");

            String amountText = savedInstanceState.getString("AMOUNT_TEXT");
            amount.setText(amountText);

            String resultText = savedInstanceState.getString("RESULT_TEXT");
            result.setText(resultText);

            fromListViewPosition = savedInstanceState.getInt("FROM_LIST_POSITION");
            toListViewPosition = savedInstanceState.getInt("TO_LIST_POSITION");

            isSortedAZ = savedInstanceState.getBoolean("IS_SORTED_AZ");
            timeBetweenUpdates = savedInstanceState.getLong("TIME_BETWEEN_UPDATES");
        }
    }

    @Override
    protected void onStop() {
        System.out.println("MainActivity.. onStop..");
        super.onStop();
        if (getDataFromInternet != null)
            getDataFromInternet.cancel(true);
        if (getDataFromLocalFile != null)
            getDataFromLocalFile.cancel(true);
    }

    @Override
    protected void onStart() {
        System.out.println("MainActivity.. onStart..");
        super.onStart();
        Start();
    }

    private void Start() {
        System.out.println("MainActivity.. Start..");

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        File file = new File(getFilesDir().getAbsolutePath() + "/Data");
        FileInputStream fileInputStream;
        FileInputStream tempFileInputStream;
        OutputStream outputStream;

        try {
            if (file.exists()) {
                tempFileInputStream = openFileInput("Data");
                InputStreamReader inputStreamReader = new InputStreamReader(tempFileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String timeInLongString = bufferedReader.readLine();
                double timeInDouble = Double.parseDouble(timeInLongString);
                timeInLong = Long.parseLong(timeInLongString);

                System.out.println("Hours before next update: " + TimeUnit.MILLISECONDS.toHours((long) timeInDouble + TimeUnit.DAYS.toMillis(1) - new Date().getTime()));
                System.out.println("Minutes since last update: " + TimeUnit.MILLISECONDS.toMinutes((long) (new Date().getTime() - timeInDouble)));

                //(FILE EXISTS) and (file is up to date) or (we are NOT connected to the Internet)
                if (timeInDouble + timeBetweenUpdates > new Date().getTime() || !isConnected) {
                    System.out.println("(FILE EXISTS) and (file is up to date) or (we are NOT connected to the Internet)\n");
                    fileInputStream = openFileInput("Data");
                    getDataFromLocalFile = new GetDataFromLocalFile(fileInputStream, this);
                    getDataFromLocalFile.execute();
                    Toast.makeText(getApplicationContext(), "Warning! \nData might be out of date!", Toast.LENGTH_SHORT).show();
                }
                //(FILE EXISTS) and (file is NOT up to date) and (we are connected to the Internet)
                else if (isConnected) {
                    System.out.println("(FILE EXISTS) and (file is NOT up to date) and (we are connected to the Internet)");
                    outputStream = openFileOutput("Data", Context.MODE_PRIVATE);
                    getDataFromInternet = new GetDataFromInternet(this);
                    getDataFromInternet.execute(outputStream);
                }
                //(FILE DOESN'T EXIST) and (we are connected to the Internet)
            } else if (isConnected) {
                System.out.println("(FILE DOESN'T EXIST) and (we are connected to the Internet)\n");
                outputStream = openFileOutput("Data", Context.MODE_PRIVATE);
                getDataFromInternet = new GetDataFromInternet(this);
                getDataFromInternet.execute(outputStream);
                //(FILE DOESN'T EXIST) and (we are NOT connected to the Internet)
            } else if (!isConnected) {
                System.out.println("(FILE DOESN'T EXIST) and (we are NOT connected to the Internet)\n");
                Toast.makeText(getApplicationContext(), "No data on file and no Internet connection!", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);

            long timeInHours = TimeUnit.MILLISECONDS.toHours(timeBetweenUpdates);

            intent.putExtra("isSortedAZ", isSortedAZ);
            System.out.println("SENT MainActivity.. ifClickedSettings isSortedAZ: " + isSortedAZ);
            intent.putExtra("timeInHours", timeInHours);
            System.out.println("SENT MainActivity.. ifClickedSettings timeInHours: " + timeInHours);

            startActivityForResult(intent, REQUEST_CODE_SETTINGS);
            System.out.println("YOU CLICKED SETTINGS!");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("MainActivity.. onActivityResult..");

        if (requestCode == REQUEST_CODE_SETTINGS) {
            if (resultCode == RESULT_OK) {

                isSortedAZ = data.getBooleanExtra("isSortedAZ", false);
                System.out.println("GOT MainActivity.. onActivityResult.. isSortedAZ: " + isSortedAZ);

                if (isSortedAZ) {
                    Collections.sort(myCurrencies.getList(isSortedAZ));
                    fromListViewAdapter.notifyDataSetChanged();
                    toListViewAdapter.notifyDataSetChanged();
                } else {
                    Collections.sort(myCurrencies.getList(isSortedAZ));
                    Collections.reverse(myCurrencies.getList(isSortedAZ));
                    fromListViewAdapter.notifyDataSetChanged();
                    toListViewAdapter.notifyDataSetChanged();
                }

                double intentTimeDataInHours = data.getDoubleExtra("timeInHours", -1);
                long intentTimeDataInHoursLong = Math.round(intentTimeDataInHours);
                System.out.println("GOT MainActivity.. onActivityResult.. intentTimeDataInHours: " + intentTimeDataInHours);

                if (timeBetweenUpdates == -1) {
                    timeBetweenUpdates = TimeUnit.HOURS.toMillis(24);
                } else {
                    timeBetweenUpdates = TimeUnit.HOURS.toMillis(intentTimeDataInHoursLong);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateUI(ArrayList<Currency> currencies) {

        myCurrencies = new Currencies(currencies);
        System.out.println(currencies);

        fromListViewAdapter.clear();
        fromListViewAdapter.addAll(myCurrencies.getList(isSortedAZ));
        fromListViewAdapter.notifyDataSetChanged();

        toListViewAdapter.clear();
        toListViewAdapter.addAll(myCurrencies.getList(isSortedAZ));
        toListViewAdapter.notifyDataSetChanged();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String DateToStr = format.format(myCurrencies.getTimestamp());

        this.date.setText(DateToStr);
    }

    public void updateUILocal(ArrayList<Currency> currencies) {

        myCurrencies = new Currencies(new Date(timeInLong), currencies);

        fromListViewAdapter.clear();
        fromListViewAdapter.addAll(myCurrencies.getList(isSortedAZ));
        fromListViewAdapter.notifyDataSetChanged();

        toListViewAdapter.clear();
        toListViewAdapter.addAll(myCurrencies.getList(isSortedAZ));
        toListViewAdapter.notifyDataSetChanged();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String DateToStr = format.format(myCurrencies.getTimestamp());

        this.date.setText(DateToStr);
    }
}