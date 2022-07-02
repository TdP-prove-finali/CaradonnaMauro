package it.polito.tdp.acquisti;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.acquisti.model.Buyer;
import it.polito.tdp.acquisti.model.CostVarianceData;
import it.polito.tdp.acquisti.model.CostVarianceRow;
import it.polito.tdp.acquisti.model.Model;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class FXMLCVAnalysisController {

	private Model model;
	private Stage stage;
	
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<CostVarianceRow> CVTables;

    @FXML
    private TableColumn<CostVarianceRow, Double> avgCVColumn;

    @FXML
    private TableColumn<CostVarianceRow, Double> avgPercCVColumn;
    
    @FXML
    private TableColumn<CostVarianceRow, String> descriptionColumn;

    @FXML
    private TableColumn<CostVarianceRow, String> fieldColumn;

    @FXML
    private TableColumn<CostVarianceRow, Double> lastCVColumn;

    @FXML
    private TableColumn<CostVarianceRow, Double> lastPercCVColumn;

    @FXML
    private Button showButton;

    @FXML
    private TableColumn<CostVarianceRow, Double> totalColumn;
    
    @FXML
    private ComboBox<String> fieldCmbBox;

    @FXML
    private TextField txtSearch;
    
   @FXML
    void handleDisable(ActionEvent event) {
    	/*switch (fieldCmbBox.getValue()) {
		case "Item":
			buyerCmb.setDisable(true);
			siteCmb.setDisable(true);
			commodityCmb.setDisable(true);
			break;
		case "Buyer":
			buyerCmb.setDisable(false);
			siteCmb.setDisable(true);
			commodityCmb.setDisable(true);
			break;	
		case "Site":
			buyerCmb.setDisable(true);
			siteCmb.setDisable(false);
			commodityCmb.setDisable(true);
			break;
		case "Commodity":
			buyerCmb.setDisable(true);
			siteCmb.setDisable(true);
			commodityCmb.setDisable(false);
			break;	

		default:
			break;
		}*/
    }
   
   @FXML
   void handleSearch(KeyEvent event) {
	   String id = txtSearch.getText();
	   if(id != null) {
		   List<CostVarianceRow> rows = model.getRowsWith(id);
		   CVTables.getItems().clear();
		   CVTables.setItems(FXCollections.observableArrayList(rows));
	   }
   }
   
   @FXML
   void handleSearchButton(ActionEvent event) {
	   String id = txtSearch.getText();
	   if(id != null) {
		   List<CostVarianceRow> rows = model.getRowsWith(id);
		   CVTables.getItems().clear();
		   CVTables.setItems(FXCollections.observableArrayList(rows));
	   }
   }
    
    @FXML
    void handleShow(ActionEvent event) {
    	switch (fieldCmbBox.getValue()) {
		case "Item":
			this.fieldColumn.setText("Item");
			this.descriptionColumn.setMinWidth(10);
			this.descriptionColumn.setPrefWidth(280);
			CVTables.getItems().clear();
			CVTables.setItems(FXCollections.observableArrayList(model.getAllCostVarianceByItemRows()));
			break;
		case "Buyer":
			
			this.fieldColumn.setText("Buyer");
			this.descriptionColumn.setMinWidth(10);
			this.descriptionColumn.setPrefWidth(280);
			CVTables.getItems().clear();
			CVTables.setItems(FXCollections.observableArrayList(model.getAllCostVarianceByBuyerRows()));
			break;	
		case "Site":
			
			this.fieldColumn.setText("Site");
			this.descriptionColumn.setMinWidth(0);
			this.descriptionColumn.setPrefWidth(0);
			CVTables.getItems().clear();
			CVTables.setItems(FXCollections.observableArrayList(model.getAllCostVarianceBySiteRows()));
			break;
		case "Commodity":
			
			this.fieldColumn.setText("Commodity");
			this.descriptionColumn.setMinWidth(0);
			this.descriptionColumn.setPrefWidth(0);
			CVTables.getItems().clear();
			CVTables.setItems(FXCollections.observableArrayList(model.getAllCostVarianceByCommodityRows()));
			break;	

		default:
			break;
		}
    }
    
    @FXML
    void goMenu(ActionEvent event) {
    	try {
	    	System.out.println("Apri Menu Scene");
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MenuScene.fxml"));
			Parent root = loader.load();
			
			Scene scene = new Scene(root);
	        scene.getStylesheets().add("/styles/Styles.css");
	        scene.getRoot().setStyle("-fx-font-family: 'serif'");
			
			FXMLMenuController controller = loader.getController();
 			controller.setModel(model, stage);
			
 			stage.setScene(scene);
			stage.show();
	    	
	    	} catch (Exception e) {
	    		e.printStackTrace();
			}
    }
    
    public void setModel(Model model, Stage stage) {
		this.model = model;
		this.stage = stage;
		model.init(LocalDate.of(2022, 1, 1));
		CVTables.setItems(FXCollections.observableArrayList(model.getAllCostVarianceByItemRows()));
		
		
	}

    @FXML
    void initialize() {
    	assert CVTables != null : "fx:id=\"CVTables\" was not injected: check your FXML file 'CVAnalysisScene.fxml'.";
        assert avgCVColumn != null : "fx:id=\"avgCVColumn\" was not injected: check your FXML file 'CVAnalysisScene.fxml'.";
        assert avgPercCVColumn != null : "fx:id=\"avgPercCVColumn\" was not injected: check your FXML file 'CVAnalysisScene.fxml'.";
        assert descriptionColumn != null : "fx:id=\"descriptionColumn\" was not injected: check your FXML file 'CVAnalysisScene.fxml'.";
        assert fieldCmbBox != null : "fx:id=\"fieldCmbBox\" was not injected: check your FXML file 'CVAnalysisScene.fxml'.";
        assert fieldColumn != null : "fx:id=\"fieldColumn\" was not injected: check your FXML file 'CVAnalysisScene.fxml'.";
        assert lastCVColumn != null : "fx:id=\"lastCVColumn\" was not injected: check your FXML file 'CVAnalysisScene.fxml'.";
        assert lastPercCVColumn != null : "fx:id=\"lastPercCVColumn\" was not injected: check your FXML file 'CVAnalysisScene.fxml'.";
        assert showButton != null : "fx:id=\"showButton\" was not injected: check your FXML file 'CVAnalysisScene.fxml'.";
        assert totalColumn != null : "fx:id=\"totalColumn\" was not injected: check your FXML file 'CVAnalysisScene.fxml'.";
        assert txtSearch != null : "fx:id=\"txtSearch\" was not injected: check your FXML file 'CVAnalysisScene.fxml'.";

        avgCVColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, Double>("averageCostVarianceToOutput"));
        avgPercCVColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, Double>("averagePercentageCostVarianceToOutput"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, String>("fieldDescription"));
        fieldColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, String>("fieldId"));
        lastCVColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, Double>("lastCostVarianceToOutput"));
        lastPercCVColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, Double>("lastPercentageCostVarianceToOutput"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, Double>("totalToOutput"));
        
        fieldCmbBox.getItems().clear();
        fieldCmbBox.getItems().add("Item");
        fieldCmbBox.getItems().add("Buyer");
        fieldCmbBox.getItems().add("Site");
        fieldCmbBox.getItems().add("Commodity");
    }

}
