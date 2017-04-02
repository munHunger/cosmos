package se.mulander.scraper.common.business;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by marcu on 2017-03-30.
 */
public class HttpRequest
{
	public static Object getRequest(String url, Class returnType) throws Exception
	{
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestMethod("GET");
		if(con.getResponseCode() == 200)
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
		return null;
	}
}
