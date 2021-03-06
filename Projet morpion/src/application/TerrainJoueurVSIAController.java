package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import ai.*;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class TerrainJoueurVSIAController {
	
    TransitionFade transitionFade = new TransitionFade(); // nouvelle instance
	
	@FXML
	private BorderPane notdefined; // Cadre principal
	@FXML
	private AnchorPane rootPane; // Cadre principal sans background

	@FXML
	public Button backToMenu; // boutton retour vers le menu
	@FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;
    @FXML
    private Button button8;
    @FXML
    private Button button9;
    
    @FXML
    private Line line1;
    @FXML
    private Line line2;
    @FXML
    private Line line3;
    @FXML
    private Line line4;
    @FXML
    private Line line5;
    @FXML
    private Line line6;
    @FXML
    private Line line7;
    @FXML
    private Line line8;

    @FXML
    private Label turn;
    
    private int playerTurn = 1; //Joueur commence

    public int h;
	public double lr;
	public int l;
    
    ArrayList<Button> buttons; // liste des bouttons
    ArrayList<Line> lines; // liste des lignes pour mettre en evidence les pions du gagnant
	String winner=""; // "Joueur" ou "IA"
	
    MultiLayerPerceptron net;
    Coup c;
    double[] board = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    
    public void menu(ActionEvent e) throws IOException { // boutton retour au menu
		transitionFade.makeFadeOutTransition("/view/JoueurVSIA.fxml", "Menu principal", backToMenu, rootPane);
    }
    
    public void initialize() {
    	
    	rootPane.setOpacity(0);
    	transitionFade.makeFadeInTransition(rootPane);
    	
    	h = JoueurVSIAController.config.hiddenLayerSize;
    	lr = JoueurVSIAController.config.learningRate;
    	l = JoueurVSIAController.config.numberOfhiddenLayers;
    	
    	net = ai.MultiLayerPerceptron.load("resources/models/Model_"+l+"_"+lr+"_"+h+".srl"); // chargement du modele

		c = new ai.Coup(9, "JoueurVSIA");



        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));
        buttons.forEach(button ->{
            setupButton(button);
            button.setFocusTraversable(false);
        });
        lines = new ArrayList<>(Arrays.asList(line1,line2,line3,line4,line5,line6,line7,line8));
        lines.forEach(line ->{line.setVisible(false);});   
        updateTurn();
    }

    
    public void restartGame(ActionEvent e) {
        buttons.forEach(this::resetButton);
        lines.forEach(line ->{line.setVisible(false);});
        playerTurn = 1;
        winner = "";
        updateTurn();
    }

    public void resetButton(Button button){
        button.setDisable(false);
        button.setText("");
    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
        	button.setDisable(true);
            setPlayerSymbol(button);
            checkIfGameIsOver();
            getBoardAndPlay();
        });
    }
    
    public void setPlayerSymbol(Button button){
        if(playerTurn == 1){
        	button.setText("O"); //joueur
            button.setTextFill(Color.PURPLE);
            button.setStyle("-fx-opacity: 1");
            button.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            playerTurn = 0;
        }
        else{
        	System.out.println("erreur");
        }
        updateTurn();
    }
    
    public void checkIfGameIsOver(){
        for (int a = 0; a < 8; a++) {
            String line = switch (a) {
                case 0 -> button1.getText() + button2.getText() + button3.getText();
                case 1 -> button4.getText() + button5.getText() + button6.getText();
                case 2 -> button7.getText() + button8.getText() + button9.getText();
                case 3 -> button1.getText() + button5.getText() + button9.getText();
                case 4 -> button3.getText() + button5.getText() + button7.getText();
                case 5 -> button1.getText() + button4.getText() + button7.getText();
                case 6 -> button2.getText() + button5.getText() + button8.getText();
                case 7 -> button3.getText() + button6.getText() + button9.getText();
                default -> null;
            };
            
            if (line.equals("XXX")) { //IA gagne (X)
            	disableAll();
            	winner="IA";
            	System.out.println("Winner = "+winner);
            	lines.get(a).setVisible(true);
            	linesAnimation(a);
            	alert();
            }

            else if (line.equals("OOO")) { //Joueur gagne (O)
            	disableAll();
            	winner="Joueur";
            	System.out.println("Winner = "+winner);
            	lines.get(a).setVisible(true);
            	linesAnimation(a);
            	alert();
            }
            
        }
        if (winner == "") {
	        for (int i=0;i<9;i++) {
	        	if (isEmpty(i))break;
	        	else {
	        		if (i==8) {
	        			System.out.println("egalite");
	        			winner="Egalit??, personne";
	        			disableAll();
	        			alert();
	        		}
	        	}
	        }
        }
    }
    
    	public void linesAnimation(int a){
		  FadeTransition fade = new FadeTransition();
		  fade.setNode(lines.get(a));
		  fade.setDuration(Duration.millis (1000));
		  fade.setCycleCount(TranslateTransition.INDEFINITE);
		  fade.setInterpolator(Interpolator.LINEAR);
		  fade.setFromValue(0);
		  fade.setToValue(1);
		  fade.play();
      }
    
    public void disableAll() {
    	buttons.forEach(button ->{
            button.setDisable(true);
            button.setStyle("-fx-opacity: 1");
            button.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        });
    }
    
    public void updateTurn() {
    	if(playerTurn % 2 == 0){
    		turn.setText("IA");
    	}
    	else {
    		turn.setText("Joueur 1");
    	}
    }

    public void getBoardAndPlay() {
    	for (int i=0;i<9;i++) {
    		if (buttons.get(i).getText().equals("X")) board[i] = -1.0;
    		else if(buttons.get(i).getText().equals("O")) board[i] = 1.0;
    		else board[i] = 0.0;
    	}
    	
    	if (playerTurn == 0 && winner == "") { // Si c'est a l'IA de jouer et que la partie est pas finie
    		
    		c.addInBoard(board);
	    	
	    	double[] res = ai.Test.play(net, c);
	    	//System.out.println("Test predicted: "+Arrays.toString(res) + " -> true: "+ Arrays.toString(c.out));
	    	
	    	double max = 0;
	    	int index = 0;
	    	
	    	while (playerTurn == 0) {
	    		
	    		for (int i=0;i<9;i++) { //On recupere le max de res
	        		if(res[i] > max) {
	        			max = res[i];
	        			index = i;
	        		}
	        	}
		        //System.out.println(max);
		        if (isEmpty(index)) { // si la case est libre
		        	buttons.get(index).setDisable(true);
		        	buttons.get(index).setText("X");
		        	buttons.get(index).setTextFill(Color.BLACK);
		            buttons.get(index).setStyle("-fx-opacity: 1");
		            buttons.get(index).setFont(Font.font("Arial", FontWeight.BOLD, 40));
		        	max = 0;
		        	playerTurn = 1;
		        	updateTurn();
		        	checkIfGameIsOver();
		        }
		        else { //case pas libre -> on recommence
		        	max = 0;
		        	res[index] = 0;
		        }
		        
	    	}   
    	}
    }
    
    public Boolean isEmpty(int index) {
    	if (buttons.get(index).getText().equals("")) return true;
    	else return false;
    }
    
    private void alert() { // alerte qui affiche le gagnant
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Fin de la partie");
		alert.setContentText(winner+" a gagn? la partie ! Voulez-vous recommencer ?");
		
		ButtonType oui = new ButtonType("Oui");
		ButtonType non = new ButtonType("Non");

		// Remove default ButtonTypes
		alert.getButtonTypes().clear();

		alert.getButtonTypes().addAll(oui, non);
		
		Optional<ButtonType> option = alert.showAndWait();

	      if (option.get() == null) {
	    	  System.out.println("test");
	      } else if (option.get() == oui) {
	    	  System.out.println("restart");
	    	  restartGame(null);
	      }
	}
}