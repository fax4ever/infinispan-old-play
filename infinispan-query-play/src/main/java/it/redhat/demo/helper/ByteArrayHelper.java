package it.redhat.demo.helper;

public class ByteArrayHelper {

	public static byte[] toArray(byte single) {
		byte[] array = new byte[1];
		array[0] = single;
		return array;
	}

	public static byte toSingle(byte[] array) {
		return array[0];
	}

}
