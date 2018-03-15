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
import stockExchangeApp.market.stockExchange.Company;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Maciej on 30.12.2017.
 */
public class CompaniesTable extends TableView implements Initializable{

    /**
     * Ustawia odpowiednie wartosci pol w tabeli, przy kliknieciu na pole, wyswietla informacje o spolce
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<Company, String>("name"));
        objectsTable.setItems(Economy.DB.getCompaniesObservableList());
        objectsTable.setRowFactory(tv -> {TableRow<Company> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    Company clickedRow = row.getItem();
                    Economy.DB.setIdOfCompanyToDisplay(clickedRow.getId());
                    CompanyInfoController test = new CompanyInfoController();
                    test.display();
                }
            });
            return row;
        })
        ;}

    @FXML private TableColumn nameColumn;
    @FXML private TableView<Company> objectsTable;

    public CompaniesTable() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/stockExchangeApp/views/Objects/CompaniesTable.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

}

