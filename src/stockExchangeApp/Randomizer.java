package stockExchangeApp;

import stockExchangeApp.market.MetalExchange;
import stockExchangeApp.market.StockExchange;

import stockExchangeApp.trader.Fund;
import stockExchangeApp.trader.Investor;
import stockExchangeApp.trader.Wallet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Randomizer implements Serializable{

    public Random generator = new Random();



    public String randomizeName(String toRandomize) {
//        Path path = Paths.get("data/" + toRandomize + ".txt");
//        String random = "";
//        try (Stream<String> lines = Files.lines(path)) {
//            random = lines.collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
//                Collections.shuffle(collected);
//                return collected.get(0);
//            }));
//        } catch (IOException ex) {
//            System.out.print(ex);
//        }
//        return random;
//
        ArrayList<String> lines = new ArrayList<>();
        String path = "data/" + toRandomize + ".txt";
        String random = "";
        InputStream is = getClass().getResourceAsStream(path);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        try {
            while ((line = br.readLine()) != null)
            {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        random = lines.stream().collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                Collections.shuffle(collected);
                return collected.get(0);
            }));
        return random;
    }

    /**
     * <p>
     *     Generuje losowy pesel
     * </p>
     * @return Pesel w postaci Stringa
     */
    public String randomizePesel(){
        String numbers = "1234567890";
        StringBuilder pesel = new StringBuilder();
        for(int i = 0; i < numbers.length(); i++){
            int index = generator.nextInt(10);
            pesel.append(numbers.charAt(index));
        }
        return pesel.toString();
    }

    /**
     * <p>
     *     Generuje losowego inwestora
     * </p>
     * @return Losowy inwestor
     */
    public Investor randomizeInvestor(){
        return new Investor(Economy.DB.getId(),
                randomizeName("firstName"),
                randomizeName("lastName"),
                randomizePesel(),
                randomizeNumber(100000));
    }

    /**
     * <p>
     *     Generuje losowy fundusz
     * </p>
     * @return Losowy fundusz
     */
    public Fund randomizeFund(){
        return new Fund(Economy.DB.getId(),
                randomizeNumber(100000),
                randomizeName("fundName"),
                randomizeName("firstName"),
       randomizeName("lastName"));
    }

    /**
     * <p>
     *     Generuje losową liczbe typu float
     * </p>
     * @param multiplier liczba calkowita przez ktora ma byc przemnozona uzyskana liczba z zakresu 0 do 1
     * @return losowa liczba typu float
     */
    public float randomizeNumber(int multiplier){
        return generator.nextFloat() * multiplier;
    }

    /**
     * <p>
     *     Generuje tablice losowych cen - obecnej, maksymalnej, minmialnej
     * </p>
     * @param multiplier liczba calkowita przez ktora maja byc przemnozone uzyskane liczby z zakresu 0 do 1
     * @return tablica 3 cen typu float
     */
    public float[] randomizePrices(int multiplier){
        float[] prices = new float[3];
        prices[2] = generator.nextFloat() * multiplier;
        prices[1] = generator.nextFloat() * prices[2];
        prices[0] = generator.nextFloat() * prices[1];
        return prices;
    }

    /**
     * <p>
     *     Generuje losowo wybrany index jednego z niepusych rynkow z listy rynkow
     * </p>
     * @return index rynku z list rynków
     */
    public int getRandomMarketId(){
        Random generator = new Random();
        int marketIndex = generator.nextInt(Economy.DB.getMarkets().size());
        while(Economy.DB.getMarkets().get(marketIndex).isEmptyMarket()){
            marketIndex = generator.nextInt(Economy.DB.getMarkets().size());
        }
        return marketIndex;
    }

    /**
     * <p>
     *     Wybiera losową spolkę z gieldy
     * </p>
     * @param stockExchange gielda
     * @return index spolki z gieldy
     */
    public int getRandomCompany(StockExchange stockExchange){
        Random generator = new Random();
        return generator.nextInt(stockExchange.getCompanies().size());
    }

    /**
     * <p>
     *     Wybiera losowy surowiec z rynku surowcow
     * </p>
     * @param metalExchange rynek surowcow
     * @return index surowca z rynku surowcow
     */
    public int getRandomMetal(MetalExchange metalExchange){
        Random generator = new Random();
        return  generator.nextInt(metalExchange.getMetalsList().size());
    }

    /**
     * <p>
     *     Wybiera losowy asset z portfela
     * </p>
     * @param wallet portfel inwestycyjny
     * @return index losowego assetu
     */
    public int getRandomAsset(Wallet wallet){
        Random generator = new Random();
        return generator.nextInt(wallet.getAssets().size());
    }

    /**
     * <p>
     *     Wybiera losowa walutę z listy walut
     * </p>
     * @param currencies lista walut
     * @return index losowej waluty
     */
    public int getRandomCurrency(ArrayList<stockExchangeApp.asset.Currency> currencies){
        Random random = new Random();
        return random.nextInt(currencies.size());
    }

    /**
     * <p>
     *     Generuje losowe kursy sprzedazy walut w stosunku do zlotego
     * </p>
     * @param size ilosc walut
     * @return lista kursow
     */
    public ArrayList<Float> getRandomZlotyPrices(int size){
        ArrayList<Float> prices = new ArrayList<>();
        for(int i = 0; i < size; i++){
            prices.add(generator.nextFloat() * 10);
        }
        return prices;
    }

    /**<p>
     *      Generuje tablice tablic kursow wymiany walut
     * </p>
     * @param size ilosc walut
     * @return tablica kursow wymiany walut
     */
    public ArrayList<ArrayList<Float>> getRandomCurrencyPrices(int size){
        ArrayList<ArrayList<Float>> prices = new ArrayList<>();
        for(int i = 0; i < size; i++){
            ArrayList<Float> row = new ArrayList<>();
            prices.add(row);
            for(int j = 0; j < size; j++){
                float x = generator.nextFloat() * 10;
                if(j > i){
                    prices.get(i).add(x);
                }
                else if (j == i){
                    prices.get(i).add(1.f);
                }
                else if(j < i){
                    prices.get(i).add(1.f/x);
                }
            }
        }
        return prices;
    }

    /**
     * <p>
     *     Dodaje kursy dla nowo powstalej waluty, jezeli zostaje dodana do swiata po swtorzeniu rynku walut
     * </p>
     * @param currenciesPrices poprzednia tablica kursow wymiany
     * @return nowa tablica kursow wymiany
     */
    public ArrayList<ArrayList<Float>> addNewCurrencyToForex(ArrayList<ArrayList<Float>> currenciesPrices){
        currenciesPrices.add(getRandomZlotyPrices(currenciesPrices.size()));
        for(ArrayList<Float> currency : currenciesPrices){
            currency.add(generator.nextFloat() * 10);
        }
        currenciesPrices.get(currenciesPrices.size() - 1).set(currenciesPrices.size() - 1, 1.f);
        return currenciesPrices;
    }

    public int getRandomFund(){
        Random random = new Random();
        return random.nextInt(Economy.DB.getFunds().size());
    }
}