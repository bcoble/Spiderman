/**
 * 
 */
package main;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * @author coblebj
 * DEPRECATED - UNNEEDED
 *
 */
public class TweetController {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Get auth keys
		authenticate();
		
		// Call the searcher to load 10 tweets
		searchTwitter();
		
	}
	
	public static void authenticate(){
		String[] args = null;
		File file = new File("twitter4j.properties");
	    Properties prop = new Properties();
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        if (file.exists()) {
	            is = new FileInputStream(file);
	            prop.load(is);
	        }
	        if (args.length < 2) {
	            if (null == prop.getProperty("oauth.consumerKey")
	                    && null == prop.getProperty("oauth.consumerSecret")) {
	                // consumer key/secret are not set in twitter4j.properties
	                System.out.println(
	                        "Usage: java twitter4j.examples.oauth.GetAccessToken [consumer key] [consumer secret]");
	                System.exit(-1);
	            }
	        } else {
	            prop.setProperty("oauth.consumerKey", args[0]);
	            prop.setProperty("oauth.consumerSecret", args[1]);
	            os = new FileOutputStream("twitter4j.properties");
	            prop.store(os, "twitter4j.properties");
	        }
	    } catch (IOException ioe) {
	        ioe.printStackTrace();
	        System.exit(-1);
	    } finally {
	        if (is != null) {
	            try {
	                is.close();
	            } catch (IOException ignore) {
	            }
	        }
	        if (os != null) {
	            try {
	                os.close();
	            } catch (IOException ignore) {
	            }
	       }
	        }
	        try {
	            Twitter twitter = new TwitterFactory().getInstance();
	            RequestToken requestToken = twitter.getOAuthRequestToken();
	            System.out.println("Got request token.");
	            System.out.println("Request token: " + requestToken.getToken());
	            System.out.println("Request token secret: " + requestToken.getTokenSecret());
	            AccessToken accessToken = null;
	
	            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	            while (null == accessToken) {
	                System.out.println("Open the following URL and grant access to your account:");
	                System.out.println(requestToken.getAuthorizationURL());
	                try {
	                    Desktop.getDesktop().browse(new URI(requestToken.getAuthorizationURL()));
	                } catch (UnsupportedOperationException ignore) {
	                } catch (IOException ignore) {
	                } catch (URISyntaxException e) {
	                    throw new AssertionError(e);
	                }
	                System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
	                String pin = br.readLine();
	                try {
	                    if (pin.length() > 0) {
	                        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
	                    } else {
	                        accessToken = twitter.getOAuthAccessToken(requestToken);
	                    }
	                } catch (TwitterException te) {
	                    if (401 == te.getStatusCode()) {
	                        System.out.println("Unable to get the access token.");
	                    } else {
	                        te.printStackTrace();
	                    }
	                }
	            }
	            System.out.println("Got access token.");
	            System.out.println("Access token: " + accessToken.getToken());
	            System.out.println("Access token secret: " + accessToken.getTokenSecret());
	
	            try {
	                prop.setProperty("oauth.accessToken", accessToken.getToken());
	                prop.setProperty("oauth.accessTokenSecret", accessToken.getTokenSecret());
	                os = new FileOutputStream(file);
	                prop.store(os, "twitter4j.properties");
	                os.close();
	            } catch (IOException ioe) {
	                ioe.printStackTrace();
	                System.exit(-1);
	            } finally {
	                if (os != null) {
	                    try {
	                        os.close();
	                    } catch (IOException ignore) {
	                    }
	                }
	            }
	            System.out.println("Successfully stored access token to " + file.getAbsolutePath() + ".");
	            System.exit(0);
	        } catch (TwitterException te) {
	            te.printStackTrace();
	            System.out.println("Failed to get accessToken: " + te.getMessage());
	            System.exit(-1);
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	            System.out.println("Failed to read the system input.");
	            System.exit(-1);
	        }
	    }
	
	
	/**
	 * TODO add params
	 */
	public static void searchTwitter(){

		int count = 10;
		
		Twitter twitter = new TwitterFactory().getInstance();
		
		ArrayList<String> params = new ArrayList<String>();
		params.add("mad");
				
		try{
			// build query
			Query query = new Query(params.get(0));
			query.setCount(count);
			query.setLang("en");
			QueryResult result;
			
			do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				for (Status tweet : tweets) {
					System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
				}				
				System.exit(0);
			} while ((query = result.nextQuery())!= null);
		} catch (TwitterException te){
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}
	}
}
