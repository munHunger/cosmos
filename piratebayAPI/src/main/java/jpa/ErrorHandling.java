package jpa;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

public class ErrorHandling {

	public static void throwIOException(IOException e) throws IOException {
		throw new IOException();
	}
	
	public static void throwMalformedURLException(MalformedURLException e) throws MalformedURLException {
		throw new MalformedURLException();
	}
	
	public static void throwProtocolException(ProtocolException e) throws ProtocolException {
		throw new ProtocolException();
	}
}
