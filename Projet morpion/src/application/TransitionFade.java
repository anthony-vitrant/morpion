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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TransitionFade {
	
	
	public void makeFadeInTransition(BorderPane rootPane) {
  	  FadeTransition fade = new FadeTransition();
    	  fade.setNode(rootPane);
    	  fade.setDuration(Duration.millis(300));
    	  fade.setInterpolator(Interpolator.LINEAR);
    	  fade.setFromValue(0);
    	  fade.setToValue(1);
    	  fade.play();
  }
  
  public void makeFadeOutTransition(String path, String title, Button backToMenu, BorderPane rootPane) {
  	  FadeTransition fade = new FadeTransition();
    	  fade.setNode(rootPane);
    	  fade.setDuration(Duration.millis(400));
    	  fade.setInterpolator(Interpolator.LINEAR);
    	  fade.setFromValue(1);
    	  fade.setToValue(0);
    	  
    	  fade.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				loadScene(path,title,backToMenu);
				
			}
		});
    	  
    	  fade.play();
  }
  
  public void loadScene(String path, String title, Button backToMenu) {
		  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
		  Parent root = null;
		try {
			root = fxmlLoader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  Stage window=(Stage) backToMenu.getScene().getWindow();
		  window.setTitle(title);
		  window.setScene(new Scene(root));
  }
}
