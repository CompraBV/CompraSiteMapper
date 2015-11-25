package nl.compra.CompraCrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class Crawler {

	private static final String NAME = "No Name";
	private static final String COMMANDS = "I'm a webcrawler which crawls an URL looking for URL's leading to other views within this URL to generate a sitemap.";
	private URLConnection 		connection;
	private List<String> 		collection;
	
	public List<String> getCollection () { return collection; }
	
	public Crawler ()
	{
		
		collection = new ArrayList<String> ();
		
		System.out.println ("Hello! My name is " + NAME + " and I'm a happy webcrawler.\nHere is what I do:\n" + COMMANDS);
		
	}
	
	private void Log (char message)
	{
		
		System.out.println ("[" + NAME + "]: " + message);
		
	}
	
	private void Log (String message)
	{
		
		System.out.println ("[" + NAME + "]: " + message);
		
	}
	
	public void ConnectToTarget (String target)
	{
		
		try {

			URL url = new URL (target);	
			connection = url.openConnection();
			
			Log ("I've successfully connected to the target.");
		
		} catch (IOException e) {

			Log ("Could not connect to the given url.");
			e.printStackTrace ();
			
		}
		
	}
	
	public List<String> Crawl ()
	{
		
		Log ("I will now attempt to crawl the requested target.");
		
		BufferedReader reader;
		
		Map<String, List<Integer>> detections = new HashMap<String, List<Integer>> ();
		
		try {

			reader = new BufferedReader (new InputStreamReader (connection.getInputStream()));
			String inputLine;
			
			Log ("Crawling will now commence.");
			
			
			
			while ((inputLine = reader.readLine ()) != null)
			{
				
				inputLine = inputLine.toLowerCase ();
				
				/*
				 * Loop through every character of a line and search for href=" and then try to extract everything after the next "
				 * after that, we just keep on searching on that same line until it ends.
				 */
				
				if (inputLine.contains("href="))
				{
					
					// Remove all tabs
					inputLine = inputLine.replace ("\t", "");
					
					int lineCursor = 0;
					while (lineCursor < inputLine.length () - 6)
					{
						
						if (inputLine.substring (lineCursor, (lineCursor + 4)).equals("href"))
						{
							
							Log (inputLine.substring (lineCursor, lineCursor + 4));
							// At this point we are sure that we are currently right at the HREF, right BEFORE the H to be precise.
							
							//Advance beyond the "href"
							lineCursor += 4;
							
							// Advance towards the = character
							while (inputLine.charAt (lineCursor) != '=')
								lineCursor++;
							
							// Advance towards the " character
							while (inputLine.charAt (lineCursor) != '"')
								lineCursor++;
							
							// Advance beyond the " character
							lineCursor++;
							
							int beginningOfHREFPosition = lineCursor;
							
							while (inputLine.charAt (lineCursor) != '"')
								lineCursor++;
							
							String hrefLink = inputLine.substring(beginningOfHREFPosition, lineCursor);
							
							collection.add (hrefLink);
							
							// Create a safe distance
							lineCursor += 2;
							
						}
						
						lineCursor++;
						
					}
					
					// Debug purpose
//					Log (inputLine);
					
				}
				
			}
			
			reader.close ();
			
			Log ("My work is done here, thank you for playing with me :)");
			
			return collection;
			
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			Log ("Could not crawl target.");
			e.printStackTrace();
			
		}
		
		return collection;
		
	}
	
}
