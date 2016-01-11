package nl.compra.CompraCrawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Brain {
	
	public static final String NAME = "Bob's evil twin";
	
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
		
		// Explore first given page
		ReceiveCollection (SpawnCrawler (target));
		
		List<String> initialCollection = overCollection;
		
		if ( ! initialCollection.isEmpty ())
		{
		
			for (String collected : initialCollection)
			{
				
				ReceiveCollection (SpawnCrawler (collected));
				
			}
		
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
			
			if ( ! overCollection.contains (target + collected))
			{
				
				// At some point this caused bugs with too many slashes, if shit hits the fan and you think it has to do with slashes..
				// Uncomment dis and comment the other condition :D
				
//				if ( ! collected.equals(target + "/"))
//					overCollection.add (target + "/" + collected);
				
				if ( ! collected.equals(target))
					overCollection.add (target + collected);

				
			}
			
		}
		
	}
	
}
