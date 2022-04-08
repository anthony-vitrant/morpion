package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import ai.*;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class TerrainJoueurVSIAController {

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
		  FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Main.fxml"));
		  Parent root = fxmlLoader.load();
		  Stage window=(Stage) backToMenu.getScene().getWindow();
		  window.setTitle("Menu principal");
		  window.setScene(new Scene(root));
    }
    
    public void initialize() {
    	h = JoueurVSIAController.config.hiddenLayerSize;
    	lr = JoueurVSIAController.config.learningRate;
    	l = JoueurVSIAController.config.numberOfhiddenLayers;
    	net = MultiLayerPerceptron.load("resources/models/Model_"+l+"_"+lr+"_"+h+".srl"); // chargement du modele
		c = new Coup(9,"test");
    	//c.addInBoard(board);

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
            button.setTextFill(Color.RED);
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

            //IA gagne (X)
            if (line.equals("XXX")) {
            	disableAll();
            	winner="IA";
            	System.out.println("Winner = "+winner);
            	lines.get(a).setVisible(true);
            	linesAnimation(a);
            	alert();
            }

            //Joueur gagne (O)
            else if (line.equals("OOO")) {
            	disableAll();
            	winner="Joueur";
            	System.out.println("Winner = "+winner);
            	lines.get(a).setVisible(true);
            	linesAnimation(a);
            	alert();
            }
            
            for (int i=0;i<9;i++) {
            	if (isEmpty(i)) {
            		break;
            	}
            	else {
            		if (i==8) {
            			System.out.println("Ã©galitÃ©");
            			winner="Joueur";
            			disableAll();
            		}
            	}
            }
        }
    }
    
    
    public void linesAnimation(int a){
    	
    	  // disparaÃ®tre
    	  FadeTransition fade = new FadeTransition();
    	  fade.setNode(lines.get(a));
    	  fade.setDuration(Duration.millis (1000));
    	  fade.setCycleCount(TranslateTransition.INDEFINITE);
    	  fade.setInterpolator(Interpolator.LINEAR);
    	  fade.setFromValue(0);
    	  fade.setToValue(1);
    	  fade.play();
    	  
    	  
   	 // Traduire
   	 /* TranslateTransition translate = new TranslateTransition ();
   	  translate.setNode (lines.get(a));
   	  translate.setDuration (Duration.millis (1000));
   	  translate.setCycleCount (TranslateTransition.INDEFINITE);
   	  translate.setByX (500);
   	  translate.setByY (-250); 
   	  translate.setAutoReverse (true);
   	  translate.play ();*/

   	  // faire pivoter
   	 /* RotateTransition rotate = new RotateTransition();
   	  rotate.setNode(lines.get(a));
   	  rotate.setDuration(Duration.millis (500));
   	  rotate.setCycleCount(TranslateTransition.INDEFINITE);
   	  rotate.setInterpolator(Interpolator.LINEAR);
   	  rotate.setByAngle(360);
   	  rotate.setAxis(Rotate.Z_AXIS);
   	  rotate.play();*/
   	    

   	 /* // escalader
   	  ScaleTransition scale = new ScaleTransition();
   	  scale.setNode(lines.get(a));
   	  scale.setDuration(Duration.millis (1000));
   	  scale.setCycleCount(TranslateTransition.INDEFINITE);
   	  scale.setInterpolator(Interpolator.LINEAR);
   	  scale.setByX (2.0);
   	  scale.setByY (2.0);
   	  scale.setAutoReverse(true);
   	  scale.play ();*/
      	
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
	    	double[] res = play(net, c);
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
		        	buttons.get(index).setTextFill(Color.BLUE);
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
    
    
    private void alert() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Fin de la partie");
		alert.setContentText(winner+" a  gagné la partie ! Voulez-vous recommencer ?");
		
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
    
    
    public static HashMap<Integer, Coup> loadGames(String fileName) {
		System.out.println("loadGames from "+fileName+ " ...");
		HashMap<Integer, Coup> map = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
			String s = "";
			br.readLine();
			while ((s = br.readLine()) != null) {
				if ( ! s.endsWith("draw") ) {
					//
					String playerX = s.replace(",?", "");
					String playerO = br.readLine().replace(",?", "");
					//0,8,1,3,?,?,?,loss
					//4,7,2,6,?,?,?,win
					//
					HashMap<Integer, double[]> sequenceMoves = getGameSequence(playerX, playerO, 9);
					//
					int startEmptyBoard = 0 ; 
					if ( playerO.endsWith("win") ) {
						startEmptyBoard = 1 ;
					}
					boolean in = true ;
					double[] currentBoard = new double[9];
					
					//
					for (int pos = startEmptyBoard; pos < sequenceMoves.size(); pos ++ ) {
						double[] board = sequenceMoves.get(pos);
						if ( ! in ) {
							Coup c = new Coup(9, playerX+" "+playerO);
							c.in = currentBoard.clone() ;
							c.out = board ;
							map.put(map.size(), c);
						}
						in = !in ;
						for (int i = 0; i < board.length; i++) {
							if ( currentBoard[i] == 0.0 )
								currentBoard[i] = board[i];
						}
					}
				}
			}
			br.close();
		} 
		catch (Exception e) {
			System.out.println("Test.loadGames()");
			e.printStackTrace();
			System.exit(-1);
		}
		return map ;
	}
    
    public static HashMap<Integer, double[]> getGameSequence(String x, String o, int size){
		HashMap<Integer, double[]> sequence = new HashMap<>();
		double[] board = new double[size];
		sequence.put(0, board);

		x = x.replace(",win", "").replace(",loss", "");
		o = o.replace(",win", "").replace(",loss", "");

		String[] tabX = x.split(",");
		String[] tabO = o.split(",");

		int len = tabX.length;
		if ( tabO.length > tabX.length )
			len = tabO.length ;

		for (int i = 0; i < len; i ++ ) {

			//System.out.println("---");
			//System.out.println("\ti: "+i);
			if ( tabX.length > i ) {
				board = new double[size];
				int c = new Integer(tabX[i]);
				//System.out.println("c: "+c);
				board[c] = Coup.X ;
				sequence.put(sequence.size(), board);
			}
			
			if ( tabO.length > i ) {
				board = new double[size];
				int c = new Integer(tabO[i]);
				board[c] = Coup.O ;
				sequence.put(sequence.size(), board);
				//System.out.println("c: "+c);
			}

		}

		//System.out.println("sequence: "+Arrays.asList(sequence));
		return sequence ;
	}
    
    
    public static HashMap<Integer, Coup> loadCoupsFromFile(String file){
		System.out.println("loadCoupsFromFile from "+file+" ...");
		HashMap<Integer, Coup> map = new HashMap<>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file))));
			String s = "";
			while ((s = br.readLine()) != null) {
				String[] sIn = s.split("\t")[0].split(" ");
				String[] sOut = s.split("\t")[1].split(" ");

				double[] in = new double[sIn.length];
				double[] out = new double[sOut.length];

				for (int i = 0; i < sIn.length; i++) {
					in[i] = Double.parseDouble(sIn[i]);
				}

				for (int i = 0; i < sOut.length; i++) {
					out[i] = Double.parseDouble(sOut[i]);
				}

				Coup c = new Coup(9, "");
				c.in = in ;
				c.out = out ;
				map.put(map.size(), c);
			}
			br.close();
		} 
		catch (Exception e) {
			System.out.println("Test.loadCoupsFromFile()");
			e.printStackTrace();
			System.exit(-1);
		}
		return map ;
	}

    public static double[] play(MultiLayerPerceptron net, Coup c){
		try {
			double[] res = net.forwardPropagation(c.in);
			return res ;
		} 
		catch (Exception e) {
			System.out.println("Test.play()");
			e.printStackTrace();
			System.exit(-1);
		}

		return null ;
	}
    
}
