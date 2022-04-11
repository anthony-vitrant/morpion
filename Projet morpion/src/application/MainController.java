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
	
	TransitionFade transitionFade = new TransitionFade();

	
	@FXML
	private BorderPane rootPane; // Cadre principal
	
	
	public static Stage stage;
	
	public Button bouttonIA;
	public Button bouttonJoueur;
	
    public void initialize() {
    	rootPane.setOpacity(0);
    	transitionFade.makeFadeInTransition(rootPane);
    }
    

	
	public void joueurVSIA(ActionEvent e) throws IOException { // boutton Joueur VS IA
		transitionFade.makeFadeOutTransition("/view/JoueurVSIA.fxml", "Jouer vs IA", bouttonIA, rootPane);
	}
	
	public void joueurVSJoueur(ActionEvent e) throws IOException { // boutton Joueur VS Joueur
		transitionFade.makeFadeOutTransition("/view/TerrainJoueurVSJoueur.fxml", "Jouer", bouttonIA, rootPane);
	}
	
	
	

}
