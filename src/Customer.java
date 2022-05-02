

import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class Customer {
    private long id;
    private String name;
    private int tier;

    public Customer(String[] customerInfo) {
        this(Long.parseLong(customerInfo[1]), customerInfo[3], Integer.parseInt(customerInfo[5])); }

    public Customer(long id,String name, int tier)
    {
        setId(id);
        setName(name);
        setTier(tier);
    }

    public String toString()
    {
        return "customer: "+ getId() + " name: "+ getName() +" tier: "+ getTier()+"\n";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }
}