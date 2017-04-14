package jpa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;

public class ImageSearch implements Runnable {

	private final Torrent torrent;
	private final Pattern pattern;
	
	public ImageSearch(Torrent torrent) {
		this.torrent = torrent;
		this.pattern = Pattern.compile(Constants.COVER_IMAGE_REGEX);
	}
	
	public void run() {
		String searchTerm = "";
		String[] searchCriteria;
		URL url = null;
		
		if (torrent.Category.equals("TV shows") || torrent.Category.equals("HD - TV shows")) {
			searchCriteria = parseShow();
			searchTerm += searchCriteria[0];
			searchTerm += "&type=series";
		}
		else {
			searchCriteria = parseMovie();
			searchTerm += searchCriteria[0];
			
			if (searchCriteria[1] != null) {
				searchTerm += "&y=" + searchCriteria[1];
			}
		}
		
		if (!searchTerm.equals("null")) {
			try {
				url = new URL(Constants.OMDB_URL + searchTerm);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
			// setup HttpURLConnection
			HttpURLConnection connection = null;
			try {
				connection = (HttpURLConnection) url.openConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				connection.setRequestMethod("GET");
			} catch (ProtocolException e) {
				e.printStackTrace();
			}
			
			String response = null;
			
			try {
				 // ensure the response code is 200
				if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));	 
					 response = bufferedReader.readLine();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
	
			if (response != null) {
				try {
					JSONObject object = new JSONObject(response);
					 
					// get the imageurl and imdbid from the request
					torrent.CoverImage = object.getString("Poster");
					torrent.ImdbID = object.getString("imdbID");
				}
				catch (JSONException e) {
					// either log or ignore any time a cover image isn't found
				}
			}
		}
	 }
			
	private String[] parseMovie() {
		
		final String regex = "(.*?)\\ \\(?(\\d{4}).*";
		Pattern pattern = Pattern.compile(regex);
		final String torrentName = torrent.Name.replace('.', ' ');
		String[] results = new String[2];
		
		Matcher matcher = pattern.matcher(torrentName);
		
		if (matcher.find()) {
			try {
				// title
				results[0] = matcher.group(1);
				// year
				results[1] = matcher.group(2);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// if the search query contains spaces replace the spaces with +'s to provide a proper URL later
		if (results[0] != null) {
			results[0] = results[0].replace(' ', '+');
		}
		
		return results;
	}
	
	private String[] parseShow() {
		final String regex = "(.*?)\\ S(\\d{2})E?(\\d{2})?.*";
		Pattern pattern = Pattern.compile(regex);
		final String torrentName = torrent.Name.replace('.', ' ');
		String[] results = new String[3];
		
		Matcher matcher = pattern.matcher(torrentName);
		
		if (matcher.find()) {
			try {
				// title
				results[0] = matcher.group(1);
				// season
				results[1] = matcher.group(2);
				// episode
				results[2] = matcher.group(3);
			}
			catch (Exception e) {
				// handle exception
			}
		}
		else {
			// handle error
		}
		
		// replace spaces with +'s to provide a proper URL later
		if (results[0] != null) {
			results[0] = results[0].replace(' ', '+');
		}
		
		return results;
	}

	private String parseTitle() {
		String coverString = torrent.Name.replace(" ", ".");
		
		Matcher matcher = pattern.matcher(coverString);
		while (matcher.find()) {
			coverString = matcher.group(1);
		}
		
		return coverString;
	}
}
