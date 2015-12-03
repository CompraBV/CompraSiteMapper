package nl.compra.CompraCrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
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
		 "mailto:", "tel:", " "
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
			
			for (String illegalContaining : ILLEGAL_CONTAININGS)
			{
				
				/*
				 *  TODO Improve the way the crawler checks if these conditions are true in a way that we may easily add new ones
				 *  and manage the existing ones from a more accessible point in our code base like I've done with the `ILLEGAL_CONTAININGS` constant.
				 *  Really all there is to it is creating another constant called something like `ILLEGAL_LITERALS` and then loop through it
				 *  to see if any of it matches `collectionIt`
				 */
				if (collectionIt.equals("") || collectionIt.equals (" ") || collectionIt.equals ("#") || collectionIt.equals ("/"))
				{
					
					collectionIterator.remove ();
					continue collectionLoop;
					
				}
				
				// Check if the link contains some illegal wares
				if (collectionIt.contains((illegalContaining)))
				{
					
					Log ("Removing this entry: " + collectionIt);
					collectionIterator.remove();
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
			
			
			
			while ((inputLine = reader.readLine ()) != null)
			{
				
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
					while (lineCursor < inputLine.length () - 6)
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
							
							// Advance towards the " character
							while (inputLine.charAt (lineCursor) != '"' || inputLine.charAt (lineCursor) != 39)
							{
								
								if (inputLine.charAt (lineCursor) == 39)
								{
									
									singleQuotes = true;
									Log ("Detected some silly boy using single quotes, tisk tisk tisk.");
									
								}
								
								if (lineCursor < inputLine.length () - 1) // HERE BE BUGS
									lineCursor++;
								
							}
							
							// Advance beyond the " or ' character
							lineCursor++;
							
							int beginningOfHrefPosition = lineCursor;
							
							if (singleQuotes)
								while (inputLine.charAt (lineCursor) != 39)
									lineCursor++;
							else
								while (inputLine.charAt (lineCursor) != '"')
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
			Log ("I have crawled the requested page and have collected several URLs, I will now filter out the ones that aren't desirable.");
			
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
