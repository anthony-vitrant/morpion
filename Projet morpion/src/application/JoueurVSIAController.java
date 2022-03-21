package application;

import javafx.event.ActionEvent;

import java.io.File;
import java.io.IOException;
import ai.Config;
import ai.ConfigFileLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class JoueurVSIAController {
	
	public static Config config = null;
	
	public static String diff = null;
	
	
	public
	ChoiceBox<String> difficulte = new ChoiceBox<>();
	
	public void valider(ActionEvent e) throws IOException, InterruptedException {
		if (difficulte.getValue().equals("Choix de la difficulté")) {
			alert();
		}
		else {
			
			diff = difficulte.getValue();
			System.out.println("Difficulté : "+diff);
			
			
			ConfigFileLoader cfl = new ConfigFileLoader();
			cfl.loadConfigFile("./resources/config.txt");
			
			
			if (diff.equals("Facile")) {
				config = cfl.get("F");
			}
			else if (diff.equals("Difficile")){
				config = cfl.get("D");
			}
			
			System.out.println("Config : "+config);
			
			File file = new File("resources/models/Model_"+config.numberOfhiddenLayers+"_"+config.learningRate+"_"+config.hiddenLayerSize+".srl");
            
            if (file.exists()) {
                System.out.println("Le modele existe deja !");
                alertModele();
                
            }
            else {
        
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Task.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Apprentissage");
                stage.setScene(new Scene(root, 500, 200));
                stage.setResizable(false);
                stage.show();
            }
			
			}
	}
	
	private void alert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText(null);
		alert.setTitle("Alerte");
		alert.setContentText("Veuillez choisir une difficulté !");
		alert.showAndWait();
	}
	
	private void alertModele() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("Alerte");
		alert.setContentText("Le modele existe deja !");
		alert.showAndWait();
	}
		
}
		


