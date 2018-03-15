package stockExchangeApp;

import java.io.Serializable;

import static java.lang.Thread.sleep;

/**
 * Created by Maciej on 30.12.2017.
 */
public class Simulation implements Runnable, Serializable {

    private long dayDuration = 10000;

    /**
     * Watek symulacji, wywoluje dodawanie inwestorow oraz funduszy, zmienia dni
     */
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        int day = Economy.DB.getDay();

        while(Economy.DB.isKeepRunning()){
            Economy.ThreadsMngr.addTraders();

            if(System.currentTimeMillis() - startTime > dayDuration) {
                startTime = System.currentTimeMillis();
                day += 1;
                System.out.println(day);
                try {
                    Economy.DB.dayEnded(day);
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("zaczynam dzien numer " + day);
            }
        }
        Economy.DB.setDay(day);
    }

}
