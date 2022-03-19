package application;


import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class MainController {
	

	public void joueurVSIA(ActionEvent e) throws IOException {
		  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("JoueurVSIA.fxml"));
		  Parent root = fxmlLoader.load();
		  Stage stage = new Stage();
		  stage.setTitle("Joueur VS IA");
		  stage.setScene(new Scene(root, 700, 450));
		  stage.setResizable(false);
		  stage.showAndWait();  
	}
	
	public void joueurVSJoueur(ActionEvent e) throws IOException {
		System.out.println("Joueur VS Joueur...");
	}
	
	
	

}
