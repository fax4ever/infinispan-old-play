package it.redhat.demo.model;

public class Movie {

	String id;
	Integer genre;
	Long releaseDate;
	String suitableForKids;
	String title;
	byte viewerRating;

	public Movie() {
	}

	public Movie(String id, Integer genre, Long releaseDate, String suitableForKids, String title, byte viewerRating) {
		this.id = id;
		this.genre = genre;
		this.releaseDate = releaseDate;
		this.suitableForKids = suitableForKids;
		this.title = title;
		this.viewerRating = viewerRating;
	}
}
