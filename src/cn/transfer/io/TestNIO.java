package cn.transfer.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestNIO {

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		testRead();
	}

	static void testRead() throws FileNotFoundException, IOException {
		RandomAccessFile aFile = new RandomAccessFile("D:\\fromFile.txt", "rw");
		FileChannel inChannel = aFile.getChannel();

		// create buffer with capacity of 48 bytes
		ByteBuffer buf = ByteBuffer.allocate(48);

		int bytesRead = inChannel.read(buf); // read into buffer.
		while (bytesRead != -1) {

			buf.flip(); // make buffer ready for read

			while (buf.hasRemaining()) {
				System.out.print((char) buf.get()); // read 1 byte at a time
			}

			buf.clear(); // make buffer ready for writing
			bytesRead = inChannel.read(buf);
		}
		aFile.close();
	}

	static void testWrite() throws FileNotFoundException, IOException {
		RandomAccessFile aFile = new RandomAccessFile("D:\\fromFile.txt", "rw");
		RandomAccessFile bFile = new RandomAccessFile("D:\\toFile.txt", "rw");
		FileChannel inChannel = aFile.getChannel();
		FileChannel outChannel = bFile.getChannel();

		// create buffer with capacity of 48 bytes
		ByteBuffer buf = ByteBuffer.allocate(48);

		int bytesRead = inChannel.read(buf); // read into buffer.
		while (bytesRead != -1) {

			buf.flip(); // make buffer ready for read

			while (buf.hasRemaining()) {
				// System.out.print((char) buf.get()); // read 1 byte at a time
				outChannel.write(buf);
			}

			buf.clear(); // make buffer ready for writing
			bytesRead = inChannel.read(buf);
		}
		aFile.close();
	}

	static void transferFrom() throws FileNotFoundException, IOException {
		RandomAccessFile fromFile = new RandomAccessFile("D:\\fromFile.txt",
				"rw");
		FileChannel fromChannel = fromFile.getChannel();

		RandomAccessFile toFile = new RandomAccessFile("D:\\toFile.txt", "rw");
		FileChannel toChannel = toFile.getChannel();

		long position = 0;
		long count = fromChannel.size();

		toChannel.transferFrom(fromChannel, position, count);
	}

	static void transferTo() throws FileNotFoundException, IOException {
		RandomAccessFile fromFile = new RandomAccessFile("D:\\fromFile.txt",
				"rw");
		FileChannel fromChannel = fromFile.getChannel();

		RandomAccessFile toFile = new RandomAccessFile("D:\\toFile.txt", "rw");
		FileChannel toChannel = toFile.getChannel();

		long position = 0;
		long count = fromChannel.size();

		fromChannel.transferTo(position, count, toChannel);
	}
}
