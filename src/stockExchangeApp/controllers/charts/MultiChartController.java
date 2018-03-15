package stockExchangeApp.controllers.charts;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import stockExchangeApp.Economy;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Maciej on 26.12.2017.
 */
public class MultiChartController implements Initializable{

    @FXML
    private Pane pane;

    @FXML
    private LineChart<Number, Number> lineChart;


    /**
     * Wyswietla wykres w nowym oknie
     */
    public void display(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/stockExchangeApp/views/charts/multiChart.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root, 600, 400);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * Inicializuje dane do wykresu biorąc je z "bazy danych"
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lineChart.setTitle("Multi chart");
        for(int i = 0; i < Economy.DB.getChartsYValues().size(); i++){
            XYChart.Series series = new XYChart.Series();
            series.setName(Economy.DB.getSeriesNames().get(i));

            for(int j = 0; j < Economy.DB.getChartsYValues().get(i).size(); j++){
                series.getData().add(new XYChart.Data(Economy.DB.getChartsXValues().get(i).get(j),
                        Economy.DB.getChartsYValues().get(i).get(j)));
            }
            lineChart.getData().add(series);
        }
    }
}
