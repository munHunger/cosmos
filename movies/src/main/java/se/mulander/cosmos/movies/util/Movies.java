package se.mulander.cosmos.movies.util;

import se.mulander.cosmos.common.business.HttpRequest;
import se.mulander.cosmos.common.database.jpa.Database;
import se.mulander.cosmos.movies.model.GenreList;
import se.mulander.cosmos.movies.model.Movie;
import se.mulander.cosmos.movies.model.Rating;
import se.mulander.cosmos.movies.model.TMDBResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static se.mulander.cosmos.movies.util.Settings.apiKey;
import static se.mulander.cosmos.movies.util.Settings.theMovieDbURL;

/**
 * Created by marcu on 2017-03-17.
 */
public class Movies
{

	public static Object getRecomendations() throws Exception
	{
		GenreList genreList = getGenres();
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(theMovieDbURL).append("/3/discover/movie");
		urlBuilder.append("?api_key=").append(apiKey);
		urlBuilder.append("&sort_by=popularity.desc");
		urlBuilder.append("&include_adult=false");
		urlBuilder.append("&include_video=false");
		urlBuilder.append("&page=1");
		urlBuilder.append("&primary_release_year=2017");
		TMDBResponse response = (TMDBResponse) HttpRequest.getRequest(urlBuilder.toString(), TMDBResponse.class).data;

		List result =Arrays.asList(response.results).stream().map(tmdb ->
																  {
																	  String year = tmdb.release_date.trim().substring(0, 4);
																	  Movie m = new Movie("https://image.tmdb.org/t/p/w185/" + tmdb.poster_path, tmdb.title, year.matches("\\d+") ? Integer.parseInt(year) : -1).addRating(new Rating("The Movie Database", tmdb.vote_average, tmdb.vote_count));
																	  tmdb.genre_ids.stream().map(id ->
																										  genreList.genres.stream().filter(genre -> genre.id == id).findFirst().get().name
																	  ).forEach(name -> m.addGenre(name));
																	  return m;
																  }).sorted((m1, m2) ->
																					new Double(m2.rating.get(0).rating).compareTo(m1.rating.get(0).rating)
		).collect(Collectors.toList());
		Database.saveObjects(result);
		return result;
	}

	private static GenreList getGenres() throws Exception
	{
		return (GenreList) HttpRequest.getRequest(theMovieDbURL + "/3/genre/movie/list?api_key=" + apiKey, GenreList.class).data;
	}
}
