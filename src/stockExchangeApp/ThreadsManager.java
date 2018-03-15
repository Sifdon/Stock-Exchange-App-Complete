package stockExchangeApp;

import stockExchangeApp.market.StockExchange;
import stockExchangeApp.market.stockExchange.Company;
import stockExchangeApp.trader.Fund;
import stockExchangeApp.trader.Investor;

/**
 * Created by Maciej on 07.01.2018.
 */
public class ThreadsManager {

    private Thread simulation;

    /**
     * <p>
     *     Uruchamia watek symulacji oraz watki spolek, inwestorow oraz funduszy
     * </p>
     */
    public void startThreads(){
        Economy.DB.setKeepRunning(true);
        simulation = new Thread(new Simulation());
        spawnThreads();
        for(Thread thread : Economy.DB.getThreads()){
            thread.start();
        }
        simulation.start();
    }

    /**
     * <p>
     *     Zatrzymuje watek symulacji oraz watki spolek, inwestorow oraz funduszy
     * </p>
     */
    public void stopThreads(){
        Economy.DB.setKeepRunning(false);
        for(Thread thread : Economy.DB.getThreads()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            simulation.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     *    Tworzy watki dla obiektow spolek, funduszy i inwestorow
     * </p>
     *
     */
    private void spawnThreads(){
        Economy.DB.getThreads().clear();

        for(Investor investor : Economy.DB.getInvestors()){
            Thread thread = new Thread(investor);
            Economy.DB.getThreads().add(thread);
        }

        for(Fund fund : Economy.DB.getFunds()){
            Thread thread = new Thread(fund);
            Economy.DB.getThreads().add(thread);
        }

        for (StockExchange stockExchange : Economy.DB.getStockExchanges()) {
            for (Company company : stockExchange.getCompanies()) {
                Thread thread = new Thread(company);
                Economy.DB.getThreads().add(thread);
            }
        }
    }

    /**
     *<p>
     *     Odpowiada ze generowanie funduszy i inwesytacji tak, by bylo ich 1.5 wiecej niż wszystkich assetow
     *</p>
     */
    public void addTraders(){
        int numberOfAssets = Economy.DB.getCompaniesObservableList().size() + Economy.DB.getCurrenciesObserv().size() + Economy.DB.getMetals().size();
        int numberOfTraders = Economy.DB.getInvestors().size() + Economy.DB.getFunds().size();

        while(numberOfTraders < 1.5 * numberOfAssets){
            Fund fund = Economy.Rndm.randomizeFund();
            Investor investor = Economy.Rndm.randomizeInvestor();
            Economy.DB.addFund(fund);
            Economy.DB.addInvestor(investor);
            Thread thread1 = new Thread(fund);
            Thread thread2 = new Thread(investor);
            Economy.DB.getThreads().add(thread1);
            Economy.DB.getThreads().add(thread2);
            thread1.start();
            thread2.start();

            numberOfTraders += 2;
        }
    }
}
