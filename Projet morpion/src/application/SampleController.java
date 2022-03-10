package application;

import ai.Test;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;

public class SampleController {
	
	public void start(ActionEvent e) {
		  System.out.println("start");
		 
		  ai.Test.main(null);
		  Task<Integer> task = new Task<Integer>() {
		         @Override protected Integer call() throws Exception {
		             int iterations;
		             for (iterations = 0; iterations < 100000; iterations++) {
		                 if (isCancelled()) {
		                     break;
		                 }
		                 System.out.println("Iteration " + iterations);
		             }
		             return iterations;
		         }
		     };
	}
}
