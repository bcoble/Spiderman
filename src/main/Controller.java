/**
 * 
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.jgrapht.graph.DefaultEdge;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.*;
import edu.stanford.nlp.util.*;

/**
 * @author coblebj
 *
 */
public class Controller {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		testIdea();
//
//		ProximityWeb web = buildPWeb();
//
//		List<DefaultEdge> l = web.findPath("manofsteel", "notip");
//		System.out.println(l.size());
//		
//		String corpus = "I really like eating good pizza. But I don't like bad anchovies.";
//		String text = "I really hated the superman actor! He looks so stupid! #manofsteel #mad";//"I like pizza. Chicago pizza is better than Italian.";
//		corpus = text;
//
//		Properties props = new Properties();
//		props.put("annotators",
//				"tokenize, ssplit, pos, lemma, ner, parse, sentiment");
//
//		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//		Annotation document = new Annotation(corpus);
//		pipeline.annotate(document);
//
//		// Write sentiment scorer DONE
//		sentimentScorer(document);
//
//		// Write ner extractor - DONE
//		extractNER(document);
////
////		// Write classifier - TODO
////		classifyEmotion();
	}

	public static void sentimentScorer(Annotation document) {

		String[] sentimentText = { "Very Negative", "Negative", "Neutral",
				"Positive", "Very Positive" };

		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			Tree scoredTree = sentence
					.get(SentimentCoreAnnotations.AnnotatedTree.class);
			int score = RNNCoreAnnotations.getPredictedClass(scoredTree);
			System.out.println("Sentiment: " + sentimentText[score]);
			System.out.println(scoredTree.toString());
		}

	}

	public static void extractNER(Annotation document) {
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				String ner = token.get(NamedEntityTagAnnotation.class);
				if (ner.equals("O") == false) {
					System.out.println(token + " - " + ner);
				}
			}
		}
	}

	public static void classifyEmotion() {
		// anger, disgust, fear, happiness, sadness, surprise

		// have to map sentiment

	}

	public static void example() {
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization,
		// NER, parsing, and coreference resolution
		Properties props = new Properties();
		props.put("annotators",
				"tokenize, ssplit, pos, lemma, ner, parse, sentiment");

		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// read some text in the text variable
		String text = "I really like pizza. Chicago pizza is better than Italian."; // Add
																					// your
																					// text
																					// here!

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and
		// has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for (CoreMap sentence : sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				// this is the NER label of the token
				String ne = token.get(NamedEntityTagAnnotation.class);
			}

			// this is the parse tree of the current sentence
			Tree tree = sentence.get(TreeAnnotation.class);

			// this is the Stanford dependency graph of the current sentence
			SemanticGraph dependencies = sentence
					.get(CollapsedCCProcessedDependenciesAnnotation.class);
		}
		//
		// This is the coreference link graph
		// Each chain stores a set of mentions that link to each other,
		// along with a method for getting the most representative mention
		// Both sentence and token offsets start at 1!
		Map<Integer, CorefChain> graph = document
				.get(CorefChainAnnotation.class);
	}

	public static ProximityWeb buildPWeb() {

		ProximityWeb pweb = new ProximityWeb();
		ArrayList<ArrayList<String>> hashtags = new ArrayList<ArrayList<String>>();

		// open file
		String filename = "C:\\Users\\coblebj.000\\Documents\\Courses\\Senior Thesis\\Eliza\\src\\corpus\\tweet.txt";
		try {
			Scanner reader = new Scanner(new File(filename));
			while (reader.hasNextLine()) {
				String line = "";
				String[] li = null;
				line = reader.nextLine();
				li = line.split(" ");
				ArrayList<String> tweetHashtags = new ArrayList<String>();
				for (int i = 0; i < li.length; i++) {
					String word = li[i];
					if (word.charAt(0) == '#'){
//						System.out.println("FOUND HASHTAG " + word);
						tweetHashtags.add(word.substring(1));
					} else { //TODO - Add NERS
//						System.out.println(li[i]);
					}
				}
				hashtags.add(tweetHashtags);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// extract hashtags per tweet
		System.out.println(hashtags);
		
		for(int i=0;i<hashtags.size();i++){
			for(int j=0;j<hashtags.get(i).size()-1;j++){
				for(int k=1;k<hashtags.get(i).size();k++){
					pweb.addRelationship(hashtags.get(i).get(j), hashtags.get(i).get(k));
				}
			}
		}
		
		pweb.prettyPrinter();
		
		// add to pweb

		return pweb;
	}

	
	public static void testIdea(){
		String filename = "E:\\data\\dataset_dt\\dataset_dt_joy.txt";
		
		String[] tweets = new String[1000000];
		try {
			int hashCount = 0;
			int i = 0;
			BufferedReader br = null;
			br = new BufferedReader(new FileReader(filename));
			String line;
			
			while((line = br.readLine()) != null){
//				System.out.println(line);
				tweets[i] = line;
				if(line.contains("#")){
					hashCount++;
				}
				i++;
			}
			System.out.println(hashCount + "/" + i + " lines have hashtags.");
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	
	
}
