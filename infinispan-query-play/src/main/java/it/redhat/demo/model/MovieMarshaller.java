package it.redhat.demo.model;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;

public class MovieMarshaller implements MessageMarshaller<Movie> {

	public Movie readFrom(ProtoStreamReader reader) throws IOException {
		String id = reader.readString( "id" );
		Integer genre = reader.readInt( "genre" );
		Long releaseDate = reader.readLong( "releaseDate" );
		String suitableForKids = reader.readString( "suitableForKids" );
		String title = reader.readString( "title" );
		byte[] viewerRatings = reader.readBytes( "viewerRating" );

		return new Movie( id, genre, releaseDate, suitableForKids, title, viewerRatings[0] );
	}

	public void writeTo(ProtoStreamWriter writer, Movie movie) throws IOException {
		byte[] viewerRatingsContainer = new byte[1];
		viewerRatingsContainer[0] = movie.viewerRating;

		writer.writeString( "id", movie.id );
		writer.writeInt( "genre", movie.genre );
		writer.writeLong( "releaseDate", movie.releaseDate );
		writer.writeString( "suitableForKids", movie.suitableForKids );
		writer.writeString( "title", movie.title );
		writer.writeBytes( "viewerRating", viewerRatingsContainer );
	}

	public Class<? extends Movie> getJavaClass() {
		return Movie.class;
	}

	public String getTypeName() {
		return "ProtoModel.Movie";
	}
}
