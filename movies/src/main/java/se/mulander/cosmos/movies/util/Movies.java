package se.mulander.cosmos.movies.util;

import se.mulander.cosmos.common.business.HttpRequest;
import se.mulander.cosmos.movies.model.Movie;
import se.mulander.cosmos.movies.model.Rating;
import se.mulander.cosmos.movies.model.TMDBResponse;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by marcu on 2017-03-17.
 */
public class Movies
{
	private static String theMovieDbURL = "https://api.themoviedb.org";

	public static Object getRecomendations() throws Exception
	{
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(theMovieDbURL).append("/3/discover/movie");
		urlBuilder.append("?api_key=").append("6559b0b40f39a093b15b3c4213bdb613");//Settings.fetchSettingStringValue("moviedb.apiV3Key"));
		urlBuilder.append("&sort_by=popularity.desc");
		urlBuilder.append("&include_adult=false");
		urlBuilder.append("&include_video=false");
		urlBuilder.append("&page=1");
		urlBuilder.append("&primary_release_year=2017");
		TMDBResponse response = (TMDBResponse) HttpRequest.getRequest(urlBuilder.toString(), TMDBResponse.class).data;

		return Arrays.asList(response.results).stream().map(tmdb ->
		{
			String year = tmdb.release_date.trim().substring(0, 4);
			return new Movie("https://image.tmdb.org/t/p/w185/" + tmdb.poster_path, tmdb.title, year.matches("\\d+") ? Integer.parseInt(year) : -1).addRating(new Rating("The Movie Database", tmdb.vote_average));
		}).collect(Collectors.toList());
	}
}