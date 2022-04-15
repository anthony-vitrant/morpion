package application;

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TransitionFade {
	
	
	public void makeFadeInTransition(AnchorPane anchorPane) { // transition fade in avec comme argument l'élément affecté par cette dernière
  	  FadeTransition fade = new FadeTransition();
    	  fade.setNode(anchorPane);
    	  fade.setDuration(Duration.millis(300)); // durée de 0.3sec pour la transition
    	  fade.setInterpolator(Interpolator.LINEAR);
    	  fade.setFromValue(0);
    	  fade.setToValue(1);
    	  fade.play();
  }
  
  public void makeFadeOutTransition(String path, String title, Button backToMenu, AnchorPane rootPane) { // transition fade out avec comme arguments : le chemin de la nouvelle scene à afficher, le titre de la nouvelle scene, le bouton de retour en arriere et l'élément affecté par la transition
  	  FadeTransition fade = new FadeTransition();
    	  fade.setNode(rootPane);
    	  fade.setDuration(Duration.millis(300)); // durée de 0.3sec pour la transition
    	  fade.setInterpolator(Interpolator.LINEAR);
    	  fade.setFromValue(1);
    	  fade.setToValue(0);
    	  
    	  fade.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				loadScene(path,title,backToMenu); // après avoir fais la transition on charge la nouvelle scene
				
			}
		});
    	  
    	  fade.play();
  }
  
  public void loadScene(String path, String title, Button backToMenu) { // chargement de la nouvelle scene avec comme arguments : le chemin de la nouvelle scene à afficher, le titre de la nouvelle scene, le bouton de retour en arriere
		  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
		  Parent root = null;
		try {
			root = fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  Stage window=(Stage) backToMenu.getScene().getWindow();
		  window.setTitle(title); // on défini le titre de la scene ici
		  window.setScene(new Scene(root));
		  root.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); // ajout du css sur toutes les nouvelles scenes créées
  }
}
