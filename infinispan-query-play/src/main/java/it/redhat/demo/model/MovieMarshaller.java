package it.redhat.demo.model;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;

import it.redhat.demo.helper.ByteArrayHelper;

public class MovieMarshaller implements MessageMarshaller<Movie> {

	public Movie readFrom(ProtoStreamReader reader) throws IOException {
		String id = reader.readString( "id" );
		Integer genre = reader.readInt( "genre" );
		Long releaseDate = reader.readLong( "releaseDate" );
		String suitableForKids = reader.readString( "suitableForKids" );
		String title = reader.readString( "title" );
		byte[] viewerRatings = reader.readBytes( "viewerRating" );

		return new Movie( id, genre, releaseDate, suitableForKids, title, ByteArrayHelper.toSingle( viewerRatings ) );
	}

	public void writeTo(ProtoStreamWriter writer, Movie movie) throws IOException {
		writer.writeString( "id", movie.id );
		writer.writeInt( "genre", movie.genre );
		writer.writeLong( "releaseDate", movie.releaseDate );
		writer.writeString( "suitableForKids", movie.suitableForKids );
		writer.writeString( "title", movie.title );
		writer.writeBytes( "viewerRating", ByteArrayHelper.toArray( movie.viewerRating ) );
	}

	public Class<? extends Movie> getJavaClass() {
		return Movie.class;
	}

	public String getTypeName() {
		return "ProtoModel.Movie";
	}
}
