package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import ai.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;


public class TerrainJoueurVSIAController {

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
    
    ArrayList<Button> buttons;
    ArrayList<Line> lines;
	String winner="";
    MultiLayerPerceptron net;
    String cell;
    Coup c;
    double[] board = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
    
    public void initialize() {
    	h = JoueurVSIAController.config.hiddenLayerSize;
    	lr = JoueurVSIAController.config.learningRate;
    	l = JoueurVSIAController.config.numberOfhiddenLayers;
    	
    	
    	net = MultiLayerPerceptron.load("resources/models/Model_"+l+"_"+lr+"_"+h+".srl");
    	
    	//HashMap<Integer, Coup> mapTest = loadCoupsFromFile("./resources/train_dev_test/test.txt");
		c = new Coup(9,"test");
		//getBoard();
    	c.addInBoard(board);
    	System.out.println(c);
    	double[] res = play(net, c);
    	
    	System.out.println("Test predicted: "+Arrays.toString(res) + " -> true: "+ Arrays.toString(c.out));
    	for (int i=0;i<9;i++) {
    		System.out.println(c.out[i]);
    		
    	}
    	
    	
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
            getBoardAndPlay();
            checkIfGameIsOver();
        });
    }
    
    public void setPlayerSymbol(Button button){
        if(playerTurn % 2 == 0){
            button.setText("X"); //IA
            button.setTextFill(Color.BLUE);
            button.setStyle("-fx-opacity: 1;  -fx-font-size:40");
            playerTurn = 1;
        }
        else{
            button.setText("O"); //joueur
            button.setTextFill(Color.RED);
            button.setStyle("-fx-opacity: 1; -fx-font-size:40");
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

            //IA gagne (X)
            if (line.equals("XXX")) {
            	disableAll();
            	winner="IA";
            	System.out.println("Winner = "+winner);
            	lines.get(a).setVisible(true);
            }

            //Joueur gagne (O)
            else if (line.equals("OOO")) {
            	disableAll();
            	winner="Joueur";
            	System.out.println("Winner = "+winner);
            	lines.get(a).setVisible(true);
            }
            
            for (int i=0;i<9;i++) {
            	if (isEmpty(i)) {
            		break;
            	}
            	else {
            		if (i==8) {
            			System.out.println("égalité");
            			winner="Joueur";
            			disableAll();
            		}
            	}
            }
        }
    }
    
    public void disableAll() {
    	buttons.forEach(button ->{
            button.setDisable(true);
            button.setStyle("-fx-opacity: 1");
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
    		cell = buttons.get(i).getText();
    		if (cell.equals("X")) board[i] = -1.0;
    		else if(cell.equals("O")) board[i] = 1.0;
    		else board[i] = 0.0;
    	}
    	System.out.println("Board = ");
    	for (int i=0;i<9;i++) {
    		System.out.println(board[i]);
    	}
    	
    	if (playerTurn == 0 && winner == "") { // Si c'est a l'IA de jouer
    		c.addInBoard(board);
	    	double[] res = play(net, c);
	    	System.out.println("Test predicted: "+Arrays.toString(res) + " -> true: "+ Arrays.toString(c.out));
	    	System.out.println(c);
	    	
	    	double max = 0;
	    	int index = 0;
	    	
	    	while (playerTurn == 0) {
	    		
	    		for (int i=0;i<9;i++) { //On recupere le max de res
	        		if(res[i] > max) {
	        			max = res[i];
	        			index = i;
	        		}
	        	}
		        System.out.println(max);
		        if (isEmpty(index)) {
		        	
		        	buttons.get(index).setText("X");
		        	buttons.get(index).setTextFill(Color.BLUE);
		            buttons.get(index).setStyle("-fx-opacity: 1;  -fx-font-size:40");
		        	max = 0;
		        	playerTurn = 1;
		        	
		        }
		        else {
		        	max = 0;
		        	res[index] = 0;
		        }
		        
	    	}   
    	}
    }
    
    public Boolean isEmpty(int index) {
    	if (buttons.get(index).getText().equals("")) {
    		return true;
    	}
    	else return false;
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
