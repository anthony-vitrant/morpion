package application;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class JoueurVSIAController {
	
	TransitionFade transitionFade = new TransitionFade(); // Nouvelle instance de la class transition
	
	@FXML
	private BorderPane notdefined; // Cadre principal
	@FXML
	private AnchorPane rootPane; // Cadre principal sans l'arri�re plan
	
	public Button backToMenu; // boutton retour vers le menu
	
	public static Stage stageTask = new Stage();
	public static Config config = null;
	public static String diff = null;
	public ChoiceBox<String> difficulte = new ChoiceBox<>();
	public Button btn_lancer = new Button();
	

	
	
	public void initialize() {
    	
    	rootPane.setOpacity(0); // on d�finit l'opacit� de la scene � 0 puis on lance la transition pour la faire apparaitre
    	transitionFade.makeFadeInTransition(rootPane); // juste ici
		btn_lancer.setDisable(true); //D�sactivation du boutton "Lancer la partie"
    	
    }
	
    public void menu(ActionEvent e) throws IOException { // boutton retour au menu
		transitionFade.makeFadeOutTransition("/view/Main.fxml", "Menu principal", backToMenu, rootPane);
    }
	
	
	
	public static void close(Stage stage) {stage.close();}
	
	public void enable(Button btn) {
		btn_lancer.setDisable(false);
	}
	
	public void disable(Button btn) {
		btn.setDisable(true);
	}
	

	public void valider(ActionEvent e) throws IOException, InterruptedException {
		if (difficulte.getValue().equals("Choix de la difficult�")) {
			alert();
		}
		else {
			
			btn_lancer.setDisable(false);//Activation du boutton "lancer la partie"
			diff = difficulte.getValue(); //r�cuperation de la difficult�
			System.out.println("Difficult� : "+diff);
			
			ConfigFileLoader cfl = new ConfigFileLoader();
			cfl.loadConfigFile("./resources/config.txt");
			
			
			if (diff.equals("Facile")) config = cfl.get("F"); //Si difficult� = facile
			else if (diff.equals("Difficile"))config = cfl.get("D"); //Si difficult� = difficile
			
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
		
		if (difficulte.getValue().equals("Choix de la difficult�")) {
			alert();
		}
		else {
			transitionFade.makeFadeOutTransition("/view/TerrainJoueurVSIA.fxml", "Joueur VS IA ["+diff+"]", backToMenu, rootPane);
	        
		}
	}
	
	
	private void alert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText(null);
		alert.setTitle("Alerte");
		alert.setContentText("Veuillez choisir une difficult� !");
		alert.showAndWait();
	}
	
	private void alertModele() throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Alerte");
        alert.setContentText("Le modele existe deja, voulez-vous lancer la partie ?");
        
        ButtonType oui = new ButtonType("Oui");
        ButtonType non = new ButtonType("Non");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(oui, non); // ajout des bouttons "oui" et "non"
        
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null) { // si pas de r�ponse
              System.out.println("null");
          } else if (option.get() == oui) { // si oui
              lancerPartie(null);
          }
    }
		
}
		


