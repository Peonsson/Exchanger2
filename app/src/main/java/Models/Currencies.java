package Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Peonsson on 24/11/15.
 */
public class Currencies {

    private Date timestamp;
    private ArrayList<Currency> currencies = new ArrayList<Currency>(25);

    public Currencies() {
        this.timestamp = new Date();
    }

    public Currencies(ArrayList<Currency> currencies) {
        this.timestamp = new Date();
        this.currencies = currencies;
    }

    public Currencies(Date timestamp, ArrayList<Currency> currencies) {
        this.timestamp = timestamp;
        this.currencies = currencies;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ArrayList<Currency> currencies) {
        this.currencies = currencies;
    }

    public static double calculate(double fromRate, double toRate, double amount) {
        return (amount / fromRate) * toRate;
    }

    public ArrayList<String> getList(boolean isSortedAZ) {

        ArrayList<String> returnData = new ArrayList<String>(25);

        for (int i = 0; i < currencies.size(); i++) {
            String name = currencies.get(i).toString();
            returnData.add(name);
        }

        if(isSortedAZ) {
            Collections.sort(returnData);
        } else {
            Collections.sort(returnData);
            Collections.reverse(returnData);
        }
        return returnData;
    }
}
