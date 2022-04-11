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
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

public class MainController {
	
	@FXML
	private BorderPane rootPane; // Cadre principal
		
	public static Stage stage;
	@FXML
	public Button bouttonIA;
	@FXML
	public Button bouttonJoueur;
	
    public void initialize() {
    	rootPane.setOpacity(0);
    	makeFadeInTransition();
    }
    
    private void makeFadeInTransition() {
  	  FadeTransition fade = new FadeTransition();
    	  fade.setNode(rootPane);
    	  fade.setDuration(Duration.millis(300));
    	  fade.setInterpolator(Interpolator.LINEAR);
    	  fade.setFromValue(0);
    	  fade.setToValue(1);
    	  fade.play();
  }
    
    private void makeFadeOutTransition(String path) {
  	  FadeTransition fade = new FadeTransition();
    	  fade.setNode(rootPane);
    	  fade.setDuration(Duration.millis(300));
    	  fade.setInterpolator(Interpolator.LINEAR);
    	  fade.setFromValue(1);
    	  fade.setToValue(0);
    	  
    	  fade.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				loadScene(path);
				
			}
		});
    	  
    	  fade.play();
  }
    
    public void loadScene(String path) {
		  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
		  Parent root = null;
		try {
			root = fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  Stage window=(Stage) bouttonIA.getScene().getWindow();
		  window.setTitle("Jouer");
		  window.setScene(new Scene(root));
    }
	
	public void joueurVSIA(ActionEvent e) throws IOException { // boutton Joueur VS IA
		makeFadeOutTransition("/view/JoueurVSIA.fxml");
	}
	
	public void joueurVSJoueur(ActionEvent e) throws IOException { // boutton Joueur VS Joueur
		makeFadeOutTransition("/view/TerrainJoueurVSJoueur.fxml");
	}
	
	public void settings(ActionEvent e) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Settings.fxml"));
		  Parent root = fxmlLoader.load();
		  stage = new Stage();
		  stage.setTitle("Paramètres");
		  stage.setScene(new Scene(root, 720, 500));
		  stage.setResizable(false);
		  stage.showAndWait();
	}
	
	
}
