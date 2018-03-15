package stockExchangeApp.serialization;

import stockExchangeApp.DataStorage;
import stockExchangeApp.Economy;

import java.io.*;

/**
 * Created by Maciej on 06.01.2018.
 */
public class Serialization {

    /**
     * Serializuje stan symulacji do podanego pliku
     * @param fileName nazwa pliku
     */
    public void serialize(String fileName){
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(fileName)));
            out.writeObject(Economy.DB);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserializuje swiat z podanego pliku
     * @param fileName nazwa pliku
     */
    public void deserialize(String fileName){
        DataStorage dataStorage;
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream(fileName)));
            try {
               dataStorage = (DataStorage) in.readObject();
               Economy.DB.deserializeData(dataStorage);
               in.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
