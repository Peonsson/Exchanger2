package Models;

/**
 * Created by Peonsson on 2015-11-20.
 */
public class Currency {

    private String name;
    private String rate;

    public Currency(String name, String rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return getName();
    }
}
