package nl.compra.CompraCrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Program {

	public static Brain brain;
	
	public Program ()
	{
		
		System.out.println ("Please enter a target for the webcrawler: ");
//		BufferedReader reader = new BufferedReader (new InputStreamReader (System.in));
		
//		try {
		
//			String requestedTarget = reader.readLine ();
//			crawler.ConnectToTarget (requestedTarget);
			brain = new Brain ();
			brain.SetTarget ("https://compra.nl");
			brain.Execute ();
			
//		} catch (IOException e) {
//		
//			System.out.println ("Couldn't read requested URL by user.");
//			e.printStackTrace();
//			
//		}
		
		System.out.println ("CRAWLER HAS BEEN TERMINATED");
		
	}
	
	public static void main (String[] args)
	{
		
		new Program ();
		
	}
	
}
