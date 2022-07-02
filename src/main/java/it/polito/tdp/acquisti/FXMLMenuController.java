package it.polito.tdp.acquisti;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.acquisti.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLMenuController {

	private Model model;
	private Stage stage;
	
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    void handleGoCVAnalysis(ActionEvent event) {
    	try {
	    	System.out.println("Apri CV Analysis");
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CVAnalysisScene.fxml"));
			Parent root = loader.load();
			
			Scene scene = new Scene(root);
	        scene.getStylesheets().add("/styles/Styles.css");
	        scene.getRoot().setStyle("-fx-font-family: 'serif'");
			
			FXMLCVAnalysisController controller = loader.getController();
 			controller.setModel(model, stage);
			
 			stage.setScene(scene);
			stage.show();
	    	
	    	} catch (Exception e) {
	    		e.printStackTrace();
			}
    }

    @FXML
    void handleGoNewAnalysis(ActionEvent event) {
    	try {
	    	System.out.println("Apri Scene");
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NewAnalysisScene.fxml"));
			Parent root = loader.load();
			
			Scene scene = new Scene(root);
	        scene.getStylesheets().add("/styles/Styles.css");
	        scene.getRoot().setStyle("-fx-font-family: 'serif'");
			
			FXMLNewAnalysisController controller = loader.getController();
 			controller.setModel(model, stage);
			
 			stage.setScene(scene);
			stage.show();
	    	
	    	} catch (Exception e) {
	    		e.printStackTrace();
			}
    }

    @FXML
    void handleGoSimulator(ActionEvent event) {
    	try {
	    	System.out.println("Apri Simulator Scene");
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SimulatorScene.fxml"));
			Parent root = loader.load();
			
			Scene scene = new Scene(root);
	        scene.getStylesheets().add("/styles/Styles.css");
	        scene.getRoot().setStyle("-fx-font-family: 'serif'");
			
			FXMLSimulatorController controller = loader.getController();
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
	}

    @FXML
    void initialize() {

    }

}

