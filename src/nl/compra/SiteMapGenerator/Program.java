package nl.compra.SiteMapGenerator;

import java.io.File;
import java.io.IOException;

import com.redfin.sitemapgenerator.WebSitemapGenerator;

import nl.compra.CompraCrawler.Brain;

public class Program {

	public Program (String target, String location)
	{
		
		// Establish folder
		File locationFolder = new File (location);
		if ( ! locationFolder.exists ())
		{
			
			boolean folderCreationSuccess = new File (location).mkdirs ();
			if ( ! folderCreationSuccess)
			{
				
				System.out.println ("Could not create folder for sitemaps :( Here's the foldername: " + location);
				
			}
			else
			{
				
				System.out.println ("Folder wasn't found (" + location + ") so it has been created.");
				
			}
			
		}

		try {
		
			Brain.SetTarget (target);

			Brain.Execute ();

			Brain.LogCollection ();
			
			WebSitemapGenerator wsg = new WebSitemapGenerator(Brain.GetTarget (), new File (location));
			
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
		
		new Program (args[0], args[1]);
		
	}
	
}
