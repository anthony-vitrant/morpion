package application;


import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class MainController {
	
	
	public static Stage stage;
	

	public void joueurVSIA(ActionEvent e) throws IOException { // boutton Joueur VS IA
		  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("JoueurVSIA.fxml"));
		  Parent root = fxmlLoader.load();
		  stage = new Stage();
		  stage.setTitle("Joueur VS IA");
		  stage.setScene(new Scene(root, 700, 450));
		  stage.setResizable(false);
		  stage.showAndWait();  
	}
	
	public void joueurVSJoueur(ActionEvent e) throws IOException { // boutton Joueur VS Joueur
		System.out.println("Joueur VS Joueur...");
	}
	
	
	

}
