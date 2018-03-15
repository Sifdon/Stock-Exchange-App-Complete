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
import stockExchangeApp.trader.Investor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Maciej on 30.12.2017.
 */
public class InvestorsTable extends TableView implements Initializable{

    /**
     * Ustawia odpowiednie wartosci pol w tabeli, przy kilknieciu otwiera informacje o inwestorze
     */
    @Override public void initialize(URL location, ResourceBundle resources) {
        firstNameColumn.setCellValueFactory(
            new PropertyValueFactory<Investor, String>("firstName"));
        lastNameColumn.setCellValueFactory(
            new PropertyValueFactory<Investor, String>("lastName"));
        objectsTable.setItems(Economy.DB.getInvestorObservableList());
        objectsTable.setRowFactory(tv -> {TableRow<Investor> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                    && event.getClickCount() == 2) {

                Investor clickedRow = row.getItem();
                Economy.DB.setIdOfInvestorToDisplay(clickedRow.getId());
                InvestorInfoController test = new InvestorInfoController();
                test.display();
            }
        });
        return row;
        })
        ;}

    @FXML private TableColumn firstNameColumn;
    @FXML private TableColumn lastNameColumn;
    @FXML private TableView<Investor> objectsTable;

    public InvestorsTable() {
        FXMLLoader fxmlLoader = new FXMLLoader(
            getClass().getResource("/stockExchangeApp/views/Objects/InvestorsTable.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);
    try {
        fxmlLoader.load();
    } catch (IOException exception) {
        throw new RuntimeException(exception);
    }

    }

}

