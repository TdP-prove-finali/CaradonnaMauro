package it.polito.tdp.laurea;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.laurea.model.Buyer;
import it.polito.tdp.laurea.model.CostVarianceRow;
import it.polito.tdp.laurea.model.Invoice;
import it.polito.tdp.laurea.model.Item;
import it.polito.tdp.laurea.model.Model;
import it.polito.tdp.laurea.model.NewOrder;
import it.polito.tdp.laurea.model.Order;
import it.polito.tdp.laurea.model.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class FXMLNewAnalysisController {

	private Model model;
	private Stage stage;
	private ObservableList<NewOrder> newOrders;
	private Set<String> newItemsId;
	
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<CostVarianceRow> CVtable;

    @FXML
    private Button InsertButton;

    @FXML
    private TableColumn<CostVarianceRow, Double> avgCVColumn;

    @FXML
    private TableColumn<CostVarianceRow, Double> avgPercCVColumn;

    @FXML
    private TableColumn<NewOrder, String> buyerColumn;

    @FXML
    private TextField fileNameTxt;

    @FXML
    private TableColumn<NewOrder, String> invoiceColumn;

    @FXML
    private TableColumn<NewOrder, String> itemColumn;
    
    @FXML
    private TableColumn<CostVarianceRow, String> itemCVColumn;

    @FXML
    private TableColumn<CostVarianceRow, Double> lastCVColumn;

    @FXML
    private TableColumn<CostVarianceRow, Double> lastPercCVColumn;

    @FXML
    private TableColumn<NewOrder, String> orderIdColumn;

    @FXML
    private TableView<NewOrder> ordersTable;

    @FXML
    private TableColumn<CostVarianceRow, Double> repetitiveColumn;

    @FXML
    private Button resetButton;

    @FXML
    private TableColumn<NewOrder, String> siteIdColumn;

    @FXML
    private TableColumn<NewOrder, String> supplierColumn;

    @FXML
    private TableColumn<CostVarianceRow, Double> totalColumn;

    @FXML
    private Button uploadButton;
    
    @FXML
    private Button goButton;
    
    @FXML
    void handleInsert(ActionEvent event) {
    	this.newItemsId = new HashSet<String>();
    	model.addNewOrders(newOrders, newItemsId);
    	model.init(LocalDate.of(2022, 1, 1));
    	List<CostVarianceRow> cvRows = new ArrayList<CostVarianceRow>();
    	for(CostVarianceRow cvr : model.getAllCostVarianceByItemRows()) {
    		if(newItemsId.contains(cvr.getFieldId()))
    			cvRows.add(cvr);
    	}
    	CVtable.setItems(FXCollections.observableArrayList(cvRows));
    }

    @FXML
    void handleReset(ActionEvent event) {
    	fileNameTxt.clear();
    	ordersTable.getItems().clear();
    	CVtable.getItems().clear();
    	newOrders.clear();
    }

    @FXML
    void handleUpload(ActionEvent event) {
    	String nomeFile = fileNameTxt.getText();
    	this.newOrders = FXCollections.observableArrayList(model.readFile(nomeFile));
    	ordersTable.setItems(this.newOrders);
    	InsertButton.setDisable(false);
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
		InsertButton.setDisable(true);
		model.init(LocalDate.of(2022, 1, 1));
	}

    @FXML
    void initialize() {
    	 assert CVtable != null : "fx:id=\"CVtable\" was not injected: check your FXML file 'Scene.fxml'.";
         assert InsertButton != null : "fx:id=\"InsertButton\" was not injected: check your FXML file 'Scene.fxml'.";
         assert avgCVColumn != null : "fx:id=\"avgCVColumn\" was not injected: check your FXML file 'Scene.fxml'.";
         assert avgPercCVColumn != null : "fx:id=\"avgPercCVColumn\" was not injected: check your FXML file 'Scene.fxml'.";
         assert buyerColumn != null : "fx:id=\"buyerColumn\" was not injected: check your FXML file 'Scene.fxml'.";
         assert fileNameTxt != null : "fx:id=\"fileNameTxt\" was not injected: check your FXML file 'Scene.fxml'.";
         assert goButton != null : "fx:id=\"goButton\" was not injected: check your FXML file 'Scene.fxml'.";
         assert invoiceColumn != null : "fx:id=\"invoiceColumn\" was not injected: check your FXML file 'Scene.fxml'.";
         assert itemCVColumn != null : "fx:id=\"itemCVColumn\" was not injected: check your FXML file 'Scene.fxml'.";
         assert itemColumn != null : "fx:id=\"itemColumn\" was not injected: check your FXML file 'Scene.fxml'.";
         assert lastCVColumn != null : "fx:id=\"lastCVColumn\" was not injected: check your FXML file 'Scene.fxml'.";
         assert lastPercCVColumn != null : "fx:id=\"lastPercCVColumn\" was not injected: check your FXML file 'Scene.fxml'.";
         assert orderIdColumn != null : "fx:id=\"orderIdColumn\" was not injected: check your FXML file 'Scene.fxml'.";
         assert ordersTable != null : "fx:id=\"ordersTable\" was not injected: check your FXML file 'Scene.fxml'.";
         assert resetButton != null : "fx:id=\"resetButton\" was not injected: check your FXML file 'Scene.fxml'.";
         assert siteIdColumn != null : "fx:id=\"siteIdColumn\" was not injected: check your FXML file 'Scene.fxml'.";
         assert supplierColumn != null : "fx:id=\"supplierColumn\" was not injected: check your FXML file 'Scene.fxml'.";
         assert totalColumn != null : "fx:id=\"totalColumn\" was not injected: check your FXML file 'Scene.fxml'.";
         assert uploadButton != null : "fx:id=\"uploadButton\" was not injected: check your FXML file 'Scene.fxml'.";
    	
        //avgCVColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, Double>("averageCostVariance"));
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<NewOrder, String>("orderId"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<NewOrder, String>("supplierId"));;
        siteIdColumn.setCellValueFactory(new PropertyValueFactory<NewOrder, String>("siteId"));;
        invoiceColumn.setCellValueFactory(new PropertyValueFactory<NewOrder, String>("invoiceId"));
        itemColumn.setCellValueFactory(new PropertyValueFactory<NewOrder, String>("itemId"));
        buyerColumn.setCellValueFactory(new PropertyValueFactory<NewOrder, String>("buyerId"));
        
        avgCVColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, Double>("averageCostVarianceToOutput"));
        avgPercCVColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, Double>("averagePercentageCostVarianceToOutput"));
        lastCVColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, Double>("lastCostVarianceToOutput"));
        lastPercCVColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, Double>("lastPercentageCostVarianceToOutput"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, Double>("totalToOutput"));
//        repetitiveColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, Double>("repetitive"));
        itemCVColumn.setCellValueFactory(new PropertyValueFactory<CostVarianceRow, String>("fieldDescription"));
        
    }

}

