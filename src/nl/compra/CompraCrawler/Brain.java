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
	
	private void SpawnCrawler (String target)
	{
		
		crawlers.add (new Crawler (target));
		
		
	}
	
	public void Execute ()
	{
		
		// Explore first given page
		SpawnCrawler (target);
		
		
		
	}
