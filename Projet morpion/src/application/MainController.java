package application;


import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;


public class MainController {
	
	
	public static Stage stage;
	
	public Button bouttonIA;
	public Button bouttonJoueur;
	
	
	public void joueurVSIA(ActionEvent e) throws IOException { // boutton Joueur VS IA
		  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/JoueurVSIA.fxml"));
		  Parent root = fxmlLoader.load();
		  Stage window=(Stage) bouttonIA.getScene().getWindow();
		  window.setTitle("Joueur VS IA");
          window.setScene(new Scene(root));

	}
	
	public void joueurVSJoueur(ActionEvent e) throws IOException { // boutton Joueur VS Joueur
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TerrainJoueurVSJoueur.fxml"));
		  Parent root = fxmlLoader.load();
		  Stage window=(Stage) bouttonJoueur.getScene().getWindow();
		  window.setTitle("Joueur VS Joueur");
          window.setScene(new Scene(root));

	}
	
	
	

}
