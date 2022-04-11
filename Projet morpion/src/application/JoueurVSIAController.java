package application;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import ai.Config;
import ai.ConfigFileLoader;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;

public class JoueurVSIAController {
	
	@FXML
	private BorderPane rootPane; // Cadre principal
	
	public Button backToMenu; // boutton retour vers le menu
	
	public static Stage stageTask = new Stage();
	public static Config config = null;
	public static String diff = null;
	public ChoiceBox<String> difficulte = new ChoiceBox<>();
	public Button btn_lancer = new Button();
	

	
	
	public void initialize() {
    	
    	rootPane.setOpacity(0);
    	makeFadeInTransition();
		btn_lancer.setDisable(true); //Désactivation du boutton "Lancer la partie"
    	
    }
	
    public void menu(ActionEvent e) throws IOException { // boutton retour au menu
          makeFadeOutTransition("/view/Main.fxml","Menu principal");
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
    
    private void makeFadeOutTransition(String path, String title) {
    	  FadeTransition fade = new FadeTransition();
      	  fade.setNode(rootPane);
      	  fade.setDuration(Duration.millis(400));
      	  fade.setInterpolator(Interpolator.LINEAR);
      	  fade.setFromValue(1);
      	  fade.setToValue(0);
      	  
      	  fade.setOnFinished(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				loadScene(path,title);
				
			}
		});
      	  
      	  fade.play();
    }
    
    public void loadScene(String path, String title) {
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
	
	
	
	public static void close(Stage stage) {stage.close();}
	
	public void enable(Button btn) {
		btn_lancer.setDisable(false);
	}
	
	public void disable(Button btn) {
		btn.setDisable(true);
	}
	

	public void valider(ActionEvent e) throws IOException, InterruptedException {
		if (difficulte.getValue().equals("Choix de la difficulté")) {
			alert();
		}
		else {
			
			btn_lancer.setDisable(false);//Activation du boutton "lancer la partie"
			diff = difficulte.getValue(); //récuperation de la difficulté
			System.out.println("Difficulté : "+diff);
			
			ConfigFileLoader cfl = new ConfigFileLoader();
			cfl.loadConfigFile("./resources/config.txt");
			
			
			if (diff.equals("Facile")) config = cfl.get("F"); //Si difficulté = facile
			else if (diff.equals("Difficile"))config = cfl.get("D"); //Si difficulté = difficile
			
			System.out.println("Config : "+config);
			
			File file = new File("resources/models/Model_"+config.numberOfhiddenLayers+"_"+config.learningRate+"_"+config.hiddenLayerSize+".srl");
            
            if (file.exists()) { // test si le model existe deja
                System.out.println("Le modele existe deja, vous pouvez lancer la partie !");
                alertModele();
                enable(btn_lancer);
                
            }
            else { //Si le model existe pas, lancement de l'apprentissage
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Task.fxml"));
                Parent root = fxmlLoader.load();
                stageTask = new Stage();
                stageTask.setTitle("Apprentissage ["+diff+"]");
                stageTask.setScene(new Scene(root, 500, 160));
                stageTask.setResizable(false);
                stageTask.show();
            }
		}
	}
	
	
	public void lancerPartie(ActionEvent e) throws IOException {
		
		if (difficulte.getValue().equals("Choix de la difficulté")) {
			alert();
		}
		else {
			makeFadeOutTransition("/view/TerrainJoueurVSIA.fxml", "Joueur VS IA");
	        
		}
	}
	
	
	private void alert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText(null);
		alert.setTitle("Alerte");
		alert.setContentText("Veuillez choisir une difficulté !");
		alert.showAndWait();
	}
	
	private void alertModele() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("Alerte");
		alert.setContentText("Le modele existe deja, vous pouvez lancer la partie !");
		alert.showAndWait();
	}
		
}
		


