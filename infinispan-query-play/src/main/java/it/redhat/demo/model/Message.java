package it.redhat.demo.model;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;

public class Message {

	private Long id;
	private String body;
	private Long moment;
	private String username;

	public Message() {
	}

	public Message(Long id, String body, String username) {
		this.id = id;
		this.body = body;
		this.moment = System.currentTimeMillis();
		this.username = username;
	}

	public Message(Long id, String body, Long moment, String username) {
		this.id = id;
		this.body = body;
		this.moment = moment;
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getBody() {
		return body;
	}

	/*
	 * package HibernateOGMGenerated;
	 *
	 * message Message {
     *   required int64 id = 1;
     *   optional string body = 2;
     *   optional int64 moment = 3;
     *   optional string username = 4;
	 * }
	 *
	 */
	public static final class Marshaller implements MessageMarshaller<Message> {

		@Override
		public Message readFrom(ProtoStreamReader reader) throws IOException {
			Message message = new Message();
			message.id = reader.readLong( "id" );
			message.body = reader.readString( "body" );
			message.moment = reader.readLong( "moment" );
			message.username = reader.readString( "username" );

			return message;
		}

		@Override
		public void writeTo(ProtoStreamWriter writer, Message messageId) throws IOException {
			writer.writeLong( "id", messageId.id );
			writer.writeString( "body", messageId.body );
			writer.writeLong( "moment", messageId.moment );
			writer.writeString( "username", messageId.username );
		}

		@Override
		public Class<? extends Message> getJavaClass() {
			return Message.class;
		}

		@Override
		public String getTypeName() {
			return "ProtoModel.Message";
		}
	}
}
