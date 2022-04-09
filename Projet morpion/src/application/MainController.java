package application;


import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;


public class MainController {
	
	@FXML
	private BorderPane rootPane; // Cadre principal
	
	
	public static Stage stage;
	
	public Button bouttonIA;
	public Button bouttonJoueur;
	
    public void initialize() {
    	rootPane.setOpacity(0);
    	makeFadeInTransition();
    }
    
    private void makeFadeInTransition() {
  	  FadeTransition fade = new FadeTransition();
    	  fade.setNode(rootPane);
    	  fade.setDuration(Duration.millis(400));
    	  fade.setInterpolator(Interpolator.LINEAR);
    	  fade.setFromValue(0);
    	  fade.setToValue(1);
    	  fade.play();
  }
	
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
