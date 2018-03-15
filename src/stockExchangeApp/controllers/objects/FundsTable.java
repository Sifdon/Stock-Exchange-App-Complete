package stockExchangeApp.controllers.objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import stockExchangeApp.Economy;
import stockExchangeApp.trader.Fund;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Maciej on 30.12.2017.
 */
public class FundsTable extends TableView implements Initializable{

    /**
     * Ustawia odpowiednie wartosci pol w tabli, po kliknieciu wyswietla informacje o funduszu
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<Fund, String>("name"));
        objectsTable.setItems(Economy.DB.getFundsObservableList());
        objectsTable.setRowFactory(tv -> {TableRow<Fund> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    Fund clickedRow = row.getItem();
                    Economy.DB.setIdOfFundToDisplay(clickedRow.getId());
                    FundInfoController test = new FundInfoController();
                    test.display();
                }
            });
            return row;
        })
        ;}

    @FXML private TableColumn nameColumn;
    @FXML private TableView<Fund> objectsTable;

    public FundsTable() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/stockExchangeApp/views/Objects/FundsTable.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

}

