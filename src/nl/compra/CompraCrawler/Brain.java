package nl.compra.CompraCrawler;

import java.util.ArrayList;
import java.util.List;

public class Brain {
	
	public static final String NAME = "Bob's evil twin";
	
	private List<Crawler> 	crawlers;
	private List<String> 	overCollection;
	private String 			target;

	public void SetTarget (String target) 		{ this.target = target; }
	public List<String> getOverCollection () 	{ return overCollection; }
	
	public Brain ()
	{
		
		overCollection 	= new ArrayList<String> ();
		crawlers		= new ArrayList<Crawler> ();
		
	}
	
	private List<String> SpawnCrawler (String target)
	{
		
		Crawler crawler = new Crawler (target);
		crawlers.add (crawler);
		return crawler.Crawl ();
		
		
	}
	
	public void Execute ()
	{
		
		// Explore first given page
		ReceiveCollection (SpawnCrawler (target));
		
	}
	
	private void Log (String message)
	{
		
		System.out.println ("[BRAIN]: " + message);
		
	}
	
	public void LogCollection ()
	{
		
		for (String collected : overCollection)
		{
			
			Log (collected);
			
		}
		
	}
	
	public void ReceiveCollection (List<String> collection) {
		
		for (String collected : collection)
		{
			
			if ( ! overCollection.contains (collected))
			{
				
				overCollection.add (target + "/" + collected);
				
			}
			
		}
		
	}
	
}
