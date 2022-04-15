package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TerrainJoueurVSJoueurController {
	
    TransitionFade transitionFade = new TransitionFade();
	
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
    private Label turn; // label du tour actuel
    String winner; // defini le gagnant
    private int playerTurn = 1; //defini le tour du joueur

    ArrayList<Button> buttons;
    ArrayList<Line> lines;
    
    public void initialize() {
    	rootPane.setOpacity(0);
    	transitionFade.makeFadeInTransition(rootPane);
    	
        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));
        buttons.forEach(button ->{
            setupButton(button);
            button.setFocusTraversable(false);
        });
        lines = new ArrayList<>(Arrays.asList(line1,line2,line3,line4,line5,line6,line7,line8));
        lines.forEach(line ->{line.setVisible(false);}); // default false
        updateTurn();
    }
	
    public void menu(ActionEvent e) throws IOException { // boutton retour au menu
		transitionFade.makeFadeOutTransition("/view/Main.fxml", "Menu principal", backToMenu, rootPane);
    }
    
    public void restartGame(ActionEvent e) {
        buttons.forEach(this::resetButton);
        lines.forEach(line ->{line.setVisible(false);});
        playerTurn = 1;
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
        });
    }
    
    public void setPlayerSymbol(Button button){
        if(playerTurn % 2 == 0){
            button.setText("X");
            button.setTextFill(Color.BLACK);
            button.setStyle("-fx-opacity: 1");
            button.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            playerTurn = 1;
        }
        else{
        	button.setText("O");
            button.setTextFill(Color.PURPLE);
            button.setStyle("-fx-opacity: 1");
            button.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            playerTurn = 0;
        }
        updateTurn();
    }
    
    public void checkIfGameIsOver(){
        for (int a = 0; a < 8; a++) {
            String line = switch (a) { // test pour chaque cas de win
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

            if (line.equals("XXX")) {
            	winner = "Joueur 2";
            	disableAll();
            	lines.get(a).setVisible(true);
            	linesAnimation(a);
            	alertWin();
            }

            else if (line.equals("OOO")) {
            	winner = "Joueur 1";
            	disableAll();
            	lines.get(a).setVisible(true);
            	linesAnimation(a);
            	alertWin();
            }
        }
        if (winner == "") {
	        for (int i=0;i<9;i++) {
	        	if (buttons.get(i).getText().equals(""))break;
	        	else {
	        		if (i==8) {
	        			System.out.println("egalite");
	        			winner="Egalité, personne";
	        			disableAll();
	        			alertWin();
	        		}
	        	}
	        }
        }
    }
    
    
    public void linesAnimation(int a){
		// disparaitre
		FadeTransition fade = new FadeTransition();
		fade.setNode(lines.get(a));
		fade.setDuration(Duration.millis (1000));
		fade.setCycleCount(TranslateTransition.INDEFINITE);
		fade.setInterpolator(Interpolator.LINEAR);
		fade.setFromValue(0);
		fade.setToValue(1);
		fade.play();
    }
    
    public void updateTurn() {
    	if(playerTurn % 2 == 0)turn.setText("Joueur 2");
    	else turn.setText("Joueur 1");
    }

    private void alertWin() { // alerte qui affiche le gagnant et demande si on recommence
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Fin de la partie");
		alert.setContentText(winner+" a  gagné la partie ! Voulez-vous recommencer ?");
		
		ButtonType oui = new ButtonType("Oui");
		ButtonType non = new ButtonType("Non");

		// Remove default ButtonTypes
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(oui, non); // ajout des bouttons "oui" et "non"
		
		Optional<ButtonType> option = alert.showAndWait();

	      if (option.get() == null) { // si pas de réponse
	    	  System.out.println("null");
	      } else if (option.get() == oui) { // si oui
	    	  System.out.println("restart");
	    	  restartGame(null);
	      }
	}
    
    public void disableAll() { // désactivation de tous les bouttons
    	buttons.forEach(button ->{
            button.setDisable(true);
            button.setStyle("-fx-opacity: 1");
        });
    }
}
