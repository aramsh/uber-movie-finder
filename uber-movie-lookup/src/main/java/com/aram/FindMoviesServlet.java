package com.aram;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.gson.*;


public class FindMoviesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static List<Movie> allMovies;
	private static JsonArray allMoviesJSON;
	private static long lastFetched;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long now = System.currentTimeMillis();
		if(allMoviesJSON == null || allMoviesJSON.size() == 0 || (now - lastFetched > 24*60*60*1000)) {
			getAllMovies();	
			lastFetched = System.currentTimeMillis();
		} 
		PrintWriter out = resp.getWriter();
		out.print(allMoviesJSON);      
		out.flush();
		out.close();
	}

    public void getAllMovies() {
    	allMovies = new ArrayList<Movie>();
    	try {
    		/*
    		 * I did not like the json format of the site, so I am using the xml, but sending a simple json to the client
    		 */
	    	URL url = new URL("https://data.sfgov.org/api/views/yitu-d5am/rows.xml?accessType=DOWNLOAD");
	        XMLReader parser = XMLReaderFactory.createXMLReader();
	        parser.setContentHandler(new MovieHandler());
	        parser.parse(new InputSource(url.openStream()));
    	} catch(Exception e) {
    		e.printStackTrace();
    		return;
    	}
    	JsonArray array = new JsonArray();
    	for(Movie movie : allMovies) {
    		array.add(movie.toJsonObject());
    	}
    	allMoviesJSON = array;
    	return;
    }

     class MovieHandler extends DefaultHandler {
    	boolean isTitle = false;
    	boolean isLocations = false;
    	boolean isDirector = false;
    	boolean isYear = false;
    	
    	Movie newMovie = new Movie();    	
    	boolean firstRow = true;
     
    	public void startElement(String uri, String localName,String qName, 
                    Attributes attributes) throws SAXException {
     
    		if(qName.equalsIgnoreCase("row")) {
    			if(firstRow) {
	    			firstRow = false;
    			} else {
	    			allMovies.add(newMovie);
	    			newMovie = new Movie();
    			}
    		}
     
    		if (qName.equalsIgnoreCase("title")) {
    			isTitle = true;
    		}
     
    		if (qName.equalsIgnoreCase("locations")) {
    			isLocations = true;
    		}
     
    		if (qName.equalsIgnoreCase("director")) {
    			isDirector = true;
    		}
     
    		if (qName.equalsIgnoreCase("release_year")) {
    			isYear = true;
    		}
     
    	}
     
    	public void endElement(String uri, String localName,
    		String qName) throws SAXException {    
    	}
     
    	public void characters(char ch[], int start, int length) throws SAXException {
     
    		if (isTitle) {
    			newMovie.setTitle(new String(ch, start, length));
    			isTitle = false;
    		}
     
    		if (isLocations) {
    			newMovie.setLocation(new String(ch, start, length));
    			isLocations = false;
    		}
     
    		if (isDirector) {
    			newMovie.setDirector(new String(ch, start, length));
    			isDirector = false;
    		}
     
    		if (isYear) {
    			newMovie.setReleaseYear(new String(ch, start, length));
    			isYear = false;
    		}
    	}
    }
}
