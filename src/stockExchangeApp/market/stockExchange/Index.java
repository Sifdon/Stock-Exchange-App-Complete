package stockExchangeApp.market.stockExchange;


import stockExchangeApp.market.StockExchange;
import stockExchangeApp.market.stockExchange.Company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Index implements Serializable{
    private int id;
    private String name;
    private ArrayList<Company> companies;
    private StockExchange stockExchange;

    public Index(int id, String name, ArrayList<Company> companies, StockExchange stockExchange) {
        this.id = id;
        this.name = name;
        this.companies = companies;
        this.stockExchange = stockExchange;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(ArrayList<Company> companies) {
        this.companies = companies;
    }

    public StockExchange getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }
}
