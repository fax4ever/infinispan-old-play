package it.redhat.demo.model;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		Movie movie = (Movie) o;
		return viewerRating == movie.viewerRating &&
				Objects.equals( id, movie.id ) &&
				Objects.equals( genre, movie.genre ) &&
				Objects.equals( releaseDate, movie.releaseDate ) &&
				Objects.equals( suitableForKids, movie.suitableForKids ) &&
				Objects.equals( title, movie.title );
	}

	@Override
	public int hashCode() {
		return Objects.hash( id, genre, releaseDate, suitableForKids, title, viewerRating );
	}

	@Override
	public String toString() {
		return "Movie{" +
				"id='" + id + '\'' +
				", genre=" + genre +
				", releaseDate=" + releaseDate +
				", suitableForKids='" + suitableForKids + '\'' +
				", title='" + title + '\'' +
				", viewerRating=" + viewerRating +
				'}';
	}
}
