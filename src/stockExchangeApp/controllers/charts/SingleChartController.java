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
public class SingleChartController implements Initializable{

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
            root = FXMLLoader.load(getClass().getResource("/stockExchangeApp/views/charts/singleChart.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root, 600, 400);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * Inicjalizuje dane do wykresu biorac je z "bazy danych"
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lineChart.setTitle(Economy.DB.getChartName());
        XYChart.Series series = new XYChart.Series();
        series.setName(Economy.DB.getChartName());
        //populating the series with data

        for(int i = 0; i < Economy.DB.getChartYValues().size(); i++){
            series.getData().add(new XYChart.Data(Economy.DB.getChartXValues().get(i), Economy.DB.getChartYValues().get(i)));
        }

        lineChart.getData().add(series);

    }
}
