package nl.compra.SiteMapGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.redfin.sitemapgenerator.WebSitemapGenerator;

import nl.compra.CompraCrawler.Brain;

public class Program {

	public Program (String[] args)
	{
		
		String target 	= args[0];
		String location = args[1];

		try {
		
			Brain.SetTarget (target);

			Brain.Execute ();

			Brain.LogCollection ();
			
			WebSitemapGenerator wsg = new WebSitemapGenerator(Brain.GetTarget (), new File ("sitemaps"));
			
			for (String collected : Brain.GetOverCollection())
				if (collected.contains ("http"))
					wsg.addUrl (collected);
				else
					wsg.addUrl (Brain.GetTarget () + collected);
			
			wsg.write();
			
		} catch (IOException e) {
		
			System.out.println ("Couldn't read requested URL by user.");
			e.printStackTrace();
			
		}
		
		System.out.println ("CRAWLER HAS BEEN TERMINATED");
		
		
	}
	
	public static void main (String[] args)
	{
		
		new Program (args);
		
	}
	
}
