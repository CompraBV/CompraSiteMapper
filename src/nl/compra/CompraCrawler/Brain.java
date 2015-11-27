package nl.compra.CompraCrawler;

import java.util.ArrayList;
import java.util.List;

public class Brain {
	
	public static final String NAME = "Bob's evil twin";
	
	private static List<Crawler> 	crawlers 		= new ArrayList<Crawler> ();
	private static List<String> 	overCollection 	= new ArrayList<String> ();;
	private static String 			target;

	public static List<String> getOverCollection () 	{ return overCollection; }
	public static String GetTarget ()					{ return target; }
	
	public static void SetTarget (String target) 		{ Brain.target = target; }
	
	private static List<String> SpawnCrawler (String target)
	{
		
		Crawler crawler = new Crawler (target);
		crawlers.add (crawler);
		return crawler.Crawl ();
		
		
	}
	
	public static void Execute ()
	{
		
		// Explore first given page
		ReceiveCollection (SpawnCrawler (target));
		
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
			
			if ( ! overCollection.contains (collected))
			{
				
				overCollection.add (target + "/" + collected);
				
			}
			
		}
		
	}
	
}
