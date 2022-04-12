package application;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ai.Config;
import ai.ConfigFileLoader;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class SettingsController {
	
	@FXML
	private BorderPane notdefined; // Cadre principal
	@FXML
	private AnchorPane rootPane; // Cadre principal
	
	public static String diff = null;
	@FXML
	public ChoiceBox<String> difficulte = new ChoiceBox<>();
	@FXML
	public Button sauvegarder = new Button();
	@FXML
	public TextField numberOfhiddenLayers;
	@FXML
	public TextField learningRate; 
	@FXML
	public TextField hiddenLayerSize;
	@FXML
	public ListView<String> list = new ListView<String>();

	
	String fichier; //fichier config.txt
	String newFile = ""; //nouvelle ligne a inserer dans le fichier
	Config config;
	List<String> results = new ArrayList<String>();
	
	File[] files;
	
	public void initialize() {
		update();
	}
	
	public void difficultChange(ActionEvent e) { // lors du changement de la difficulté
		ConfigFileLoader cfl = new ConfigFileLoader();
		cfl.loadConfigFile("./resources/config.txt");
		
		if (difficulte.getValue().equals("Facile")) {
			config = cfl.get("F");
		}
		else if (difficulte.getValue().equals("Difficile")) {
			config = cfl.get("D");
		}
		
		numberOfhiddenLayers.setText(String.valueOf(config.numberOfhiddenLayers));
		learningRate.setText(String.valueOf(config.learningRate));
		hiddenLayerSize.setText(String.valueOf(config.hiddenLayerSize));
	}
	
	public void save(ActionEvent e) throws IOException { // sauvegarde des modifications
		fichier = "";
		String newLine = "";
		File file = new File("./resources/config.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();
		String oldLine = "";
		
		while (line != null){ // on recupere tout le fichier config.txt
			fichier = fichier + line + System.lineSeparator();
			line = reader.readLine();
		}
		
		if(difficulte.getValue().equals("Facile")) {
			oldLine = fichier.split(System.lineSeparator())[0];
			newLine = "F:"+String.valueOf(hiddenLayerSize.getText())+":"+String.valueOf(learningRate.getText())+":"+String.valueOf(numberOfhiddenLayers.getText());
			newFile = fichier.replace(oldLine+System.lineSeparator(), newLine+System.lineSeparator());
		}
		else if (difficulte.getValue().equals("Difficile")) {
			oldLine = fichier.split(System.lineSeparator())[1];
			newLine = "D:"+String.valueOf(hiddenLayerSize.getText())+":"+String.valueOf(learningRate.getText())+":"+String.valueOf(numberOfhiddenLayers.getText());
			newFile = fichier.replace(oldLine+System.lineSeparator(), newLine+System.lineSeparator());
		}
		System.out.println("newFile = \n"+newFile);
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(newFile);
		reader.close();
		writer.close();
		alert("Modification réussie !");
	}
	
	public void reset(ActionEvent e) throws IOException { // reinitialisation aux paramètres par défaut
		newFile = "F:256:0.1:2\nD:1024:0.001:3";
		File file = new File("./resources/config.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(newFile);
		writer.close();
		alert("Réinitialisation réussie !");
	}
	
	private void alert(String msg) { // alerte
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("Alerte");
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
	public void update() { // mise a jour de la liste des modeles
		list.getItems().clear();
		results.clear();
		files = new File("./resources/models/").listFiles();
		for (File file : files) {
		    if (file.isFile()) {
		        results.add(file.getName());
		        list.getItems().add(file.getName());
		    }
		}
	}
	
	public void supprimer(ActionEvent e) {
		String nom = list.getSelectionModel().getSelectedItem();// on recupere le nom du fichier selectionné

		if (nom != null) { // si aucun fichier selectionné
			File file = new File("./resources/models/"+nom);
		      if(file.delete()){ // si le fichier est bien supprimé
		    	  alert("Le fichier "+file.getName()+" est supprimé");
		      }
		      else{
		    	  alert("L'opération de suppression a echouée");
		      }
		      update(); // mise a jour de la liste des modeles
		}
	    else {
	    	alert("Veuillez selectionner un fichier à supprimer");
	    }
	}
}
		


