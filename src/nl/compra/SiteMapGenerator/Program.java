package nl.compra.SiteMapGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.redfin.sitemapgenerator.WebSitemapGenerator;

import nl.compra.CompraCrawler.Brain;

public class Program {

	public Program ()
	{
		
		// Ge-Ge-Ge-Ge-ne-rate
		System.out.println ("Please enter a target for the webcrawler: ");
		BufferedReader reader = new BufferedReader (new InputStreamReader (System.in));
		
		try {
		
			String requestedTarget = reader.readLine ();
			Brain.SetTarget (requestedTarget);
			Brain.Execute ();

			Brain.LogCollection ();
			
			WebSitemapGenerator wsg = new WebSitemapGenerator(Brain.GetTarget (), new File ("sitemaps"));
			
			for (String collected : Brain.GetOverCollection())
				wsg.addUrl (collected);
			
			wsg.write();
			
		} catch (IOException e) {
		
			System.out.println ("Couldn't read requested URL by user.");
			e.printStackTrace();
			
		}
		
		System.out.println ("CRAWLER HAS BEEN TERMINATED");
		
		
	}
	
	public static void main (String[] args)
	{
		
		new Program ();
		
	}
	
}
