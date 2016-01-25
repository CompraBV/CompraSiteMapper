package nl.compra.CompraCrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Crawler {

	public String target;
	public Brain brainReference;
	public static final String[] ILLEGAL_CONTAININGS = 
	{
		 ".css", ".js", ".jpg", ".jpeg", ".png", ".bmp", ".gif", ".svg", ".txt", ".ico", 
		 ".zip", ".rar", ".tar.gz", ".tar", ".mp3", ".mp4", ".avi", ".wmv", ".sql", ".psd",
		 "mailto:", "tel:", "#"
	 };
	
	private HttpURLConnection 	connection;
	private int					httpResponseCode;
	private List<String> 		collection;
	
	public List<String> getCollection () { return collection; }

	public Crawler (String target)
	{
		
		Log ("Going to explore the target: " + target);

		collection = new ArrayList<String> ();
		
		ConnectToTarget (target);
		
	}
	
	private void Log (String message)
	{
		
		System.out.println ("[" + Brain.NAME + "]: " + message);
		
	}
	
	public void ConnectToTarget (String target)
	{
		
		this.target = target;
		
		try {

			URL url = new URL (target);	
			connection = (HttpURLConnection) url.openConnection();
			
			httpResponseCode = connection.getResponseCode();
			
			Log ("I've successfully connected to the target.");
		
		} catch (IOException e) {

			Log ("Could not connect to the given url.");
			e.printStackTrace ();
			
		}
		
	}
	
	public void Filter ()
	{
		
		Iterator<String> collectionIterator = collection.iterator ();
		collectionLoop: while (collectionIterator.hasNext ())
		{
			
			String collectionIt = collectionIterator.next ();
			
			// Check if root isn't fooling us
			if (collectionIt.equals("/"))
			{

				collectionIterator.remove (); // Gotcha! :D
				continue collectionLoop;
				
			}
			
			for (String illegalContaining : ILLEGAL_CONTAININGS)
			{
								
				// Check if the link contains some illegal wares
				if (collectionIt.contains((illegalContaining)))
				{
					
					Log ("Removing this entry: " + collectionIt);
					collectionIterator.remove (); // If so, confiscate the Mary Janes.
					continue collectionLoop;
					
				}
				
				if (collectionIt.contains ("http://") || collectionIt.contains ("https://")) // Check if the link doesn't go outside the requested domain
				{
					
					if ( ! collectionIt.contains (target))
					{
						
						Log ("This domain goes outside the requested one (" + target + "), removing: " + collectionIt);
						collectionIterator.remove ();
						continue collectionLoop;
						
					} 
					
				}
				
			}
			
		}
		
	}
	
	public List<String> Crawl ()
	{
		
		if (httpResponseCode != 200) // This means the page does not have the expected content.
		{
			
			Log ("The response code from the requested target remained a HTTP Response code other than 200.");
			return collection;
			
		}
		
		Log ("I will now attempt to crawl the requested target.");
		
		BufferedReader reader;
		
		try {

			reader = new BufferedReader (new InputStreamReader (connection.getInputStream()));
			String inputLine;
			
			Log ("Crawling will now commence.");
			
			int linesRead = 0;
			
			while ((inputLine = reader.readLine ()) != null)
			{
				
				linesRead++;
				
				inputLine = inputLine.toLowerCase ();
				
				/*
				 * Loop through every character of a line and search for href=" and then try to extract everything after the next "
				 * after that, we just keep on searching on that same line until it ends.
				 * 
				 * We should however also mind single quotes (').
				 * You never know with all these people writing HTML and stuffles.
				 */
				
				if (inputLine.contains("href="))
				{
					
					// Remove all tabs
					inputLine = inputLine.replace ("\t", "");
					
					int lineCursor = 0;
					while (lineCursor < inputLine.length () - 6) // this "- 6" works miracles
					{
						
						if (inputLine.substring (lineCursor, (lineCursor + 4)).equals("href"))
						{
							
							boolean singleQuotes = false;
							
							// At this point we are sure that we are currently right at the HREF, right BEFORE the H to be precise.
							// Advance beyond the "href"
							lineCursor += 4;
							
							// Advance towards the = character
							while (inputLine.charAt (lineCursor) != '=')
								lineCursor++;
							
							// lekker coden met een fles op me hoofd hue hue hue hue hue
							
							// Advance towards the " or ' character
							while (inputLine.charAt (lineCursor) != 34 && inputLine.charAt (lineCursor) != 39)
							{

//								System.out.println (inputLine.charAt (lineCursor));
								
//								if (lineCursor < inputLine.length () - 1)
									lineCursor++;
								
							}
							
							if (inputLine.charAt (lineCursor) == 39) // 39 is UNICODE for a single quote (')
							{
								
								// The link is using single quotes
								singleQuotes = true;
								Log ("Detected some silly boy using single quotes, tisk tisk tisk.");
								
							} 
							else if (inputLine.charAt (lineCursor) == 34) // 34 is UNICODE for a double quote (") 
							{
								
								// Else if condition just to be sure because I'm paranoid
								singleQuotes = false;
								
							} 
							
							// Advance beyond the " or ' character
							lineCursor++;
							
							int beginningOfHrefPosition = lineCursor;
							
							if (singleQuotes)
								while (inputLine.charAt (lineCursor) != 39)
									lineCursor++;
							else
								while (inputLine.charAt (lineCursor) != 34)
									lineCursor++;
							
							String hrefLink = inputLine.substring (beginningOfHrefPosition, lineCursor);
						
							collection.add (hrefLink);
							
							// Create a safe distance
							lineCursor++;						
							
						}
						
						lineCursor++;
						
					}
					
				}
				
			}
			
			reader.close ();
						
			// State success
			if ( ! collection.isEmpty ())
				Log ("I have crawled the requested page and have collected several URLs, I will now filter out the ones that aren't desirable.");
			else
				Log ("I have crawled the requested page but it yielded no URLs whatsoever. (Note: THIS DOESN'T MEAN I COULDN'T CONNECT. THERE JUST ARE NO URLS!!");
			
			Filter ();
			
			Log ("My work is done here, thank you for playing with me :)");
			
			return collection;
			
		} catch (IOException e) {
			
			Log ("Could not crawl target.");
			e.printStackTrace();
			
		}
		
		return collection;
		
	}
	
}
