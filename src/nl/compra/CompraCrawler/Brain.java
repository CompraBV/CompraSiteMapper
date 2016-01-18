package nl.compra.CompraCrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Brain {
	
	public static final String NAME = "Bob's evil twin";
	public static final int MAX_DEPTH = 20;
	
	private static List<Crawler> 	crawlers 		= new ArrayList<Crawler> ();
	private static List<String> 	overCollection 	= new CopyOnWriteArrayList<String> ();;
	private static String 			target;

	public static List<String> 	GetOverCollection () 	{ return overCollection; }
	public static String 		GetTarget ()			{ return target; }
	
	public static void SetTarget (String target) 		{ Brain.target = target; }
	
	private static List<String> SpawnCrawler (String target)
	{
		
		Crawler crawler = new Crawler (target);
		crawlers.add (crawler);
		return (List<String>) crawler.Crawl ();
		
		
	}
	
	public static void Execute ()
	{
		
		// Explore first given page and it's URLS
		ReceiveCollection (SpawnCrawler (target));
		
		List<String> initialCollection = overCollection;
		
		if ( ! initialCollection.isEmpty ())
		{
		
			for (String collected : initialCollection)
			{
				
				if (collected.contains ("http"))
					ReceiveCollection (SpawnCrawler (collected));
				else
					ReceiveCollection (SpawnCrawler (target + collected));
				
			}
		
		}
		
		/*
		 * Several loops should commence here where the system reiteratively goes over all the newly acquired URLs
		 * so it may extend itself beyond the first and second depth.
		 * However this process should be halted once it reaches a certain configurable depth.
		 */
		int depth = 0;
		int newFoundings = 0;
		
		depthLoop: while (depth <= MAX_DEPTH)
		{
			
			for (String collected : overCollection)
			{
				
				// TODO This is not done yet, read the loop, write the logic/magic**
				// **not actual magic
				if (collected.contains ("http"))
					SpawnCrawler (collected);
				else
					SpawnCrawler (target + collected);
				
			}
			
			if (newFoundings <= 0)
				break depthLoop;
			
			depth++;
						
		}
		
	}
	
	private static void Log (String message)
	{
		
		System.out.println ("[BRAIN]: " + message);
		
	}
	
	public static void LogCollection ()
	{
		
		for (String collected : overCollection)
		{
			
			Log (collected);
			
		}
		
	}
	
	public static void ReceiveCollection (List<String> collection) {
		
		for (String collected : collection)
		{
			
			// This doesn't exclude HTTPS because HTTPS does actually contain HTTP.
			if (collected.contains("http"))
			{
			
				if ( ! overCollection.contains (target + collected))
				{
					
					if ( ! collected.equals(target))
						overCollection.add (target + collected);
	
					
				}
				
			} 
			else
			{
				
				if ( ! overCollection.contains (collected))
				{
					
					if ( ! collected.equals(target))
						overCollection.add (collected);
	
					
				}
				
			}
			
		}
		
	}
	
}
