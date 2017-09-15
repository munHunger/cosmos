package se.mulander.cosmos.movies.util;

import se.mulander.cosmos.common.business.HttpRequest;
import se.mulander.cosmos.common.database.jpa.Database;
import se.mulander.cosmos.common.model.movies.ExtendedMovie;
import se.mulander.cosmos.common.model.movies.GenreList;
import se.mulander.cosmos.common.model.movies.Movie;
import se.mulander.cosmos.common.model.movies.Rating;
import se.mulander.cosmos.movies.model.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

		List<Movie> result = Arrays.asList(response.results)
				.stream()
				.map(tmdb ->
				{
					String year = tmdb.release_date.trim().substring(0, 4);
					Movie m = new Movie("https://image.tmdb.org/t/p/w185/" + tmdb.poster_path,
										tmdb.title, year.matches("\\d+") ? Integer.parseInt(
							year) : -1).addRating(
							new Rating("The Movie Database", tmdb.vote_average, 10, tmdb.vote_count));
					tmdb.genre_ids.stream()
							.map(id -> genreList.genres.stream()
									.filter(genre -> genre.id == id)
									.findFirst()
									.get().name)
							.forEach(name -> m.addGenre(name));
					ExtendedMovie exMovie = new ExtendedMovie(tmdb.overview,
															  "https://image.tmdb.org/t/p/w1920" + tmdb.backdrop_path);
					m.setExtended(exMovie);
					StringBuilder detailsURL = new StringBuilder();
					detailsURL.append(theMovieDbURL).append("/3/movie/");
					detailsURL.append(tmdb.id);
					detailsURL.append("/credits?api_key=").append(apiKey);
					try
					{
						TMDBCastResponse castResponse = (TMDBCastResponse) HttpRequest.getRequest(
								detailsURL.toString(), TMDBCastResponse.class).data;
						castResponse.cast.stream().forEach(cast -> exMovie.addCastMember(cast));
						castResponse.crew.stream().forEach(cast -> exMovie.addCastMember(cast));
						exMovie.cast.stream().forEach(c ->
						{
							c.generateID();
							if(c.profileURL != null && !c.profileURL.toUpperCase().equals("NULL"))
								c.profileURL = "https://image.tmdb.org/t/p/w185" + c.profileURL;
						});
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					return m;
				})
				.sorted((m1, m2) -> new Double(m2.rating.get(0).rating).compareTo(
						m1.rating.get(0).rating))
				.collect(Collectors.toList());
		result.stream().forEach(m ->
		{
			Map<String, Object> param = new HashMap<>();
			param.put("title", m.title);
			param.put("year", m.year);
			try
			{
				List dbMovies = Database.getObjects("from Movie WHERE title = :title AND year = :year", param);
				if(dbMovies.isEmpty())
					Database.saveObject(m);
				else
				{
					Movie oldMovie = (Movie) dbMovies.get(0);
					m.setID(oldMovie.internalID);
					Database.updateObject(m);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		});
		result = result.stream().map(m ->
		{
			m.extendedMovie = null;
			return m;
		}).collect(Collectors.toList());
		return result;
	}

	public static Movie getMovie(String id) throws Exception
	{
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);
		List result = Database.getObjects("from Movie WHERE internalID = :id", param);
		if(result.isEmpty())
			return null;
		return (Movie) result.get(0);
	}

	private static GenreList getGenres() throws Exception
	{
		return (GenreList) HttpRequest.getRequest(theMovieDbURL + "/3/genre/movie/list?api_key=" + apiKey,
				GenreList.class).data;
	}
}
