package application;

import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class JoueurVSIAController {
	
	
	public
	ChoiceBox<String> difficulte = new ChoiceBox<>();
	
	public void valider(ActionEvent e) throws IOException, InterruptedException {
		if (difficulte.getValue().equals("Choix de la difficulté")) {
			alert();
		}
		else {
			
			String diff = difficulte.getValue();
			System.out.println("Difficulté : "+diff);
			
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Task.fxml"));
			Parent root = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("Apprentissage");
			stage.setScene(new Scene(root, 500, 200));
			stage.setResizable(false);
			stage.show();
			
			
			//TaskController task = new TaskController(difficulte.getValue().toString());
			
			}
	}
	
	private void alert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Alerte");
		alert.setContentText("Veuillez choisir une difficulté !");
		alert.showAndWait();
	}
		
}
		


