package stockExchangeApp.controllers.mainPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import stockExchangeApp.Economy;
import stockExchangeApp.asset.Metal;
import stockExchangeApp.controllers.charts.SingleChartController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class MetalTable extends TableView implements Initializable {

    /**
     * Ustawia odpowiednie wartosci pol w tabeli, przy kliknieciu na pole, wyswietla jego wykres
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<Metal, String>("name"));
        priceColumn.setCellValueFactory(
                new PropertyValueFactory<Metal, Float>("currentPrice"));

        assetTable.setItems(Economy.DB.getMetals());

        assetTable.setRowFactory(tv -> {
            TableRow<Metal> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    Metal clickedRow = row.getItem();
                    if(Economy.DB.isMultiChartModeOn()){
                        Economy.DB.getChartsXValues().add(clickedRow.getDates());
                        Economy.DB.getChartsYValues().add(clickedRow.getPercentageChanges());
                        Economy.DB.getSeriesNames().add(clickedRow.getName());
                    }
                    else {
                        Economy.DB.setChartName(clickedRow.getName());
                        Economy.DB.setChartXValues(clickedRow.getDates());
                        Economy.DB.setChartYValues(clickedRow.getPrices());
                        SingleChartController test = new SingleChartController();
                        test.display();
                    }
                }
            });
            return row;
        });
    }

    @FXML
    private TableColumn nameColumn;

    @FXML
    private TableColumn priceColumn;

    @FXML
    private TableView<Metal> assetTable;


    public MetalTable() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/stockExchangeApp/views/mainPage/assetsTables/MetalTable.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }


    }

}


