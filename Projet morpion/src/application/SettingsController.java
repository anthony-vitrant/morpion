package application;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import ai.Config;
import ai.ConfigFileLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class SettingsController {
	
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
	
	String fichier; //fichier config.txt
	String newFile = ""; //nouvelle ligne a inserer dans le fichier
	Config config;
	
	
	public void difficultChange(ActionEvent e) {
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
	
	public void save(ActionEvent e) throws IOException {
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
	
	public void reset(ActionEvent e) throws IOException {
		newFile = "F:256:0.1:2\nD:1024:0.001:3";
		File file = new File("./resources/config.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(newFile);
		writer.close();
		alert("Réinitialisation réussie !");
	}
	
	private void alert(String msg) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setTitle("Alerte");
		alert.setContentText(msg);
		alert.showAndWait();
	}
	
}
		


