package com.aram;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Movie {

	private String fTitle;
	private String fReleaseYear;
	private String fLocation;
	private String fProdCompany;
	private String fDirector;
	private String fWriter;
	private String[] fActors;
	
	public Movie() {
		
	}
	
	public Movie(String title, String releaseYear, String location, String prodCompany, String director, String writer, String[] actors) {
		this.fTitle = title;
		this.fReleaseYear = releaseYear;
		this.fLocation = location;
		this.fProdCompany = prodCompany;
		this.fDirector = director;
		this.fWriter = writer;;
		this.fActors = actors;
	}
	
	public JsonObject toJsonObject() {
		JsonObject obj = new JsonObject();
		if(getTitle() != null && getLocation() != null) { 
			obj.add("title", new JsonPrimitive(getTitle()));
			obj.add("locations", new JsonPrimitive(getLocation()));
		}
		if(getDirector() != null)
			obj.add("director", new JsonPrimitive(getDirector()));
		if(getReleaseYear() != null)
			obj.add("releaseYear", new JsonPrimitive(getReleaseYear()));
		return obj;
	}

	public String getTitle() {
		return fTitle;
	}
	public void setTitle(String title) {
		this.fTitle = title;
	}
	public String getReleaseYear() {
		return fReleaseYear;
	}
	public void setReleaseYear(String releaseYear) {
		this.fReleaseYear = releaseYear;
	}
	public String getLocation() {
		return fLocation;
	}
	public void setLocation(String location) {
		/*
		 * didn't have time to doing something better about the addresses. but most of the data coming for location seems useless: Broadway (North Beach)
		 */
		this.fLocation = location + " San Francisco, CA";
	}
	public String getProdCompany() {
		return fProdCompany;
	}
	public void setProdCompany(String prodCompany) {
		this.fProdCompany = prodCompany;
	}
	public String getDirector() {
		return fDirector;
	}
	public void setDirector(String director) {
		this.fDirector = director;
	}
	public String getWriter() {
		return fWriter;
	}
	public void setWriter(String writer) {
		this.fWriter = writer;
	}
	public String[] getActors() {
		return fActors;
	}
	public void setActors(String[] actors) {
		this.fActors = actors;
	}
}

//this.title = $(movieXML).find("title").text(); 
//this.release_year = $(movieXML).find("release_year").text();
//this.locations = $(movieXML).find("locations").text() + " san francisco, CA";
//this.production_company = $(movieXML).find("production_company").text();
//this.director = $(movieXML).find("director").text();
//this.writer = $(movieXML).find("writer").text();
//this.actor_1 = $(movieXML).find("actor_1").text();
//this.actor_2 = $(movieXML).find("actor_2").text();
//this.actor_3 = $(movieXML).find("actor_3").text();
////console.log(this.title + "   :   "  + this.locations);
//}
//
//Movie.prototype.toString = function() {
//return "Title: " + this.title + 
//	" -- Release Year: " + this.release_year +
//	" -- Director: " + this.director;
//};
