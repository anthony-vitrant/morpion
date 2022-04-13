package application;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.IOException;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class MainController {
	
	TransitionFade transitionFade = new TransitionFade(); // nouvelle instance pour les transitions de scene

	
	@FXML
	private BorderPane notdefined;
	@FXML
	private AnchorPane rootPane; // Cadre principal sans le BG
		
	public static Stage stage;
	@FXML
	public Button bouttonIA;
	@FXML
	public Button bouttonJoueur;
	
    public void initialize() {
    	rootPane.setOpacity(0);
    	transitionFade.makeFadeInTransition(rootPane);
    }
    

	
	public void joueurVSIA(ActionEvent e) throws IOException { // boutton Joueur VS IA
		transitionFade.makeFadeOutTransition("/view/JoueurVSIA.fxml", "Joueur VS IA", bouttonIA, rootPane);
	}
	
	public void joueurVSJoueur(ActionEvent e) throws IOException { // boutton Joueur VS Joueur
		transitionFade.makeFadeOutTransition("/view/TerrainJoueurVSJoueur.fxml", "Joueur VS Joueur", bouttonIA, rootPane);
	}
	
	public void settings(ActionEvent e) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Settings.fxml"));
		  Parent root = fxmlLoader.load();
		  stage = new Stage();
		  stage.setTitle("Paramètres");
		  stage.setScene(new Scene(root));
		  stage.setResizable(false);
		  stage.showAndWait();
	}
	
	
}
