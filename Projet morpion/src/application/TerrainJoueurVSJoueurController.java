package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TerrainJoueurVSJoueurController {

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
    
    private int playerTurn = 1;

    ArrayList<Button> buttons;
    ArrayList<Line> lines;
	
    public void initialize() {
        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));
        buttons.forEach(button ->{
            setupButton(button);
            button.setFocusTraversable(false);
        });
        lines = new ArrayList<>(Arrays.asList(line1,line2,line3,line4,line5,line6,line7,line8));
        lines.forEach(line ->{line.setVisible(true);});   // default false
        updateTurn();
    }
    
    public void restartGame(ActionEvent e) {
        buttons.forEach(this::resetButton);
        lines.forEach(line ->{line.setVisible(false);});
        //winnerText.setText("Tic-Tac-Toe");
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
            button.setTextFill(Color.BLUE);
            button.setStyle("-fx-opacity: 1");
            button.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            playerTurn = 1;
        }
        else{
        	button.setText("O");
            button.setTextFill(Color.RED);
            button.setStyle("-fx-opacity: 1");
            button.setFont(Font.font("Arial", FontWeight.BOLD, 40));
            playerTurn = 0;
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

            //X winner
            if (line.equals("XXX")) {
                //winnerText.setText("X won!");
            	System.out.println("Winner"+a);
            	lines.get(a).setVisible(true);
            }

            //O winner
            else if (line.equals("OOO")) {
                //winnerText.setText("O won!");
            	System.out.println("Winner"+a);
            	lines.get(a).setVisible(true);
            }
        }
    }
    
    public void updateTurn() {
    	if(playerTurn % 2 == 0){
    		turn.setText("Joueur 2");
    	}
    	else {
    		turn.setText("Joueur 1");
    	}
    }

}
