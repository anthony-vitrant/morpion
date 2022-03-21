package application;

import java.io.IOException;
import java.util.HashMap;
import ai.Coup;
import ai.MultiLayerPerceptron;
import ai.SigmoidalTransferFunction;
import ai.Test;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class TaskController {

	
	@FXML
	public
	ProgressBar progressBar = new ProgressBar();
	
	@FXML
	public
	TextField textField = new TextField();
	
	@FXML
	public
	Label label = new Label();
	
	public String difficulte;
	public int h;
	public double lr;
	public int l;
	int epochs;
	MultiLayerPerceptron net;
	private Task<Double> task;
    public  Thread thread;
	
	public TaskController(String diff) throws InterruptedException, IOException {
		System.out.println("Contructeur avec difficulté");
		difficulte = diff;
	}
	
	public TaskController() {
		this.h = JoueurVSIAController.config.hiddenLayerSize;
		this.lr = JoueurVSIAController.config.learningRate;
		this.l = JoueurVSIAController.config.numberOfhiddenLayers;
		this.difficulte = JoueurVSIAController.diff;
		
	}
	
	public void initialize() throws InterruptedException, IOException {
			
		try {
			
			label.setText("Difficulté : "+difficulte);
			label.setTextAlignment(TextAlignment.CENTER);
			System.out.println();
			System.out.println("START TRAINING ...");
			System.out.println();
			int size=9;

			int[] layers = new int[l+2];
			layers[0] = size;
			for (int i = 0; i < l; i++) {
				layers[i+1] = h;
			}
			layers[layers.length-1] = size ;
			
			double error = 0.0 ;
			this.net = new MultiLayerPerceptron(layers, lr, new SigmoidalTransferFunction());
			this.epochs = 10000;
		
			System.out.println("---");
			System.out.println("Load data ...");
			HashMap<Integer, Coup> mapTrain = Test.loadCoupsFromFile("./resources/train_dev_test/train.txt");
			HashMap<Integer, Coup> mapDev = Test.loadCoupsFromFile("./resources/train_dev_test/dev.txt");
			HashMap<Integer, Coup> mapTest = Test.loadCoupsFromFile("./resources/train_dev_test/test.txt");
			System.out.println("---");
			
            task = getTask(mapTrain);
            
            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);
            progressBar.progressProperty().bind(task.progressProperty());
            
            
            //Ecouteurs
            task.messageProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    textField.setText(t1);
                }
            });
   
			thread = new Thread(this.task);
            thread.start();
           

			} 
			catch (Exception e1) {
				
				System.out.println("Test.learn()");
				e1.printStackTrace();
				System.exit(-1);
			}
	}
	
	private Task<Double> getTask(HashMap<Integer, Coup> mapTrain) {
        return new Task<Double>() {
            @Override
            protected Double call() throws Exception {
                double error = 0.0;
                System.out.println("" + (epochs / 100));
                for (int i = 0; i <= epochs; i++) {
                    Coup c = null;
                    while (c == null)
                        c = mapTrain.get((int) (Math.round(Math.random() * mapTrain.size())));
                    error += net.backPropagate(c.in, c.out);
                    if (i % (epochs / 100) == 0) {
                        System.out.println("Error at step " + i + " is " + (error / (double) i));
                        updateMessage("Error at step " + i + " is " + (error / (double) i));
                    }
                updateProgress(i,epochs);
                }
                
                net.save("resources/models/Model_"+l+"_"+lr+"_"+h+".srl");
                System.out.println("Apprenstissage terminé !");
                
                error /= epochs;
                if (epochs < 0){
                    updateMessage("Error is " + error);
                    }
                return error;
            }
        };
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
