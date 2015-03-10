package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;


// test svm code taken from http://stackoverflow.com/questions/10792576/libsvm-java-implementation
public class Modeler {

	// Fields
	static double[][] train;
	static double[][] test;
	static String[] emotions = {"anger", "disgust", "fear", "happiness", "neutral", "sadness", "surprise"};
	
	public static void main(String[] args){
		train = new double[300][14];
		test = new double[10][14];
		
		String fname = "C:\\Users\\coblebj.000\\Documents\\Courses\\Senior Thesis\\Eliza\\src\\corpus\\train2.dat";
		String heart_scale = "C:\\Users\\coblebj.000\\Documents\\Courses\\Senior Thesis\\libsvm-3.20\\libsvm-3.20\\heart_scale";
		
		String[][] li = new String[270][14];
		String[][] ti = new String[10][14];
		
		try {
			int i=0;
			Scanner reader = new Scanner(new File(heart_scale));
			while(reader.hasNextLine()){
				String line = "";
				line = reader.nextLine();
				li[i] = line.split(" ");
				train[i][0] = Double.parseDouble(li[i][0]);
				for (int j=1; j<li[i].length-1; j++){
					String value = li[i][j].split(":")[1];
					train[i][j] = Double.parseDouble(value);
//					System.out.println(value);
				}
				i++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			int i=0;
			Scanner reader = new Scanner(new File(fname));
			while(reader.hasNextLine()){
				String line = "";
				line = reader.nextLine();
				ti[i] = line.split(" ");
				test[i][0] = Double.parseDouble(ti[i][0]);
				for (int j=1; j<ti[i].length-1; j++){
					String value = ti[i][j].split(":")[1];
					test[i][j] = Double.parseDouble(value);
//					System.out.println(value);
				}
				i++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		
		svm_model model = svmTrain();
		
		for(int i=0;i<test.length;i++){
			evaluate(test[i], model);
		}
	}
	
	public static svm_model svmTrain(){
		svm_problem prob = new svm_problem();
		int dataCount = train.length;
		prob.y = new double[dataCount];
		prob.l = dataCount;
		prob.x = new svm_node[dataCount][];
		
		for (int i=0; i<dataCount; i++){
			double[] features = train[i];
			prob.x[i] = new svm_node[features.length-1];
			for (int j=1; j<features.length; j++){
				svm_node node = new svm_node();
				node.index = j;
				node.value = features[j];
				prob.x[i][j-1] = node;
			}
			prob.y[i] = features[0];
		}
		
		svm_parameter param = new svm_parameter();
		param.probability = 1;
		param.gamma = 0.5;
		param.nu = 0.5;
		param.C = 10;
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.LINEAR;
		param.cache_size = 20000;
		param.eps = 0.001;
		
		svm.svm_check_parameter(prob, param);
		svm_model model = svm.svm_train(prob, param);
		
		return model;
		
		
	}
	
	public static double evaluate(double[] features, svm_model model){
		svm_node[] nodes = new svm_node[features.length-1];
		for (int i=1; i<features.length; i++){
			svm_node node = new svm_node();
			node.index = i;
			node.value = features[i];
			nodes[i-1] = node;		
		}
				
		int totalClasses = 3; // could change to 7 for emotion
		int[] labels = {+1,0,-1};//new int[totalClasses];
//		svm.svm_get_labels(model, labels);
		
		double[] prob_estimates = new double[totalClasses];
		double v = svm.svm_predict_probability(model, nodes, prob_estimates);
		
		for (int i=0; i<totalClasses; i++){
			System.out.println("("+ labels[i] + ":" + prob_estimates[i] + ")");
		}
		
		System.out.println("(Actual:" + features[0] + " Prediction:" + v + ")");
		System.out.println("-------");

		return v;
	}
	
	
}
