package se.mulander.cosmos.common.business;

import com.google.gson.Gson;

import java.io.*;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;

/**
 * Created by marcu on 2017-03-30.
 */
public class HttpRequest
{
	public static Object putRequest(String url, Object data, Class returnType) throws Exception
	{
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setDoOutput(true);
		con.setRequestMethod("PUT");
		con.setRequestProperty("Content-Type", "application/json");
		String json = new Gson().toJson(data);
		OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
		out.write(json);
		out.close();
		if(con.getResponseCode() == 200)
		{
			if(returnType != null)
				return parseInput(con, returnType);
		}
		return null;
	}

	public static Object getRequest(String url, Class returnType) throws Exception
	{
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestMethod("GET");
		if(con.getResponseCode() == 200)
		{
			if(returnType != null)
				return parseInput(con, returnType);
		}
		return null;
	}

	private static Object parseInput(HttpURLConnection con, Class returnType) throws Exception
	{
		try(BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream())))
		{
			String inputLine;
			StringBuffer response = new StringBuffer();

			while((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			return new Gson().fromJson(response.toString(), returnType);
		}
	}

	/**
	 * Checks to see if a specific port is available.
	 *
	 * @param port the port to check for availability
	 */
	public static boolean portIsAvailable(int port) {

		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
		} finally {
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
                /* should not be thrown */
				}
			}
		}
		return false;
	}
}
