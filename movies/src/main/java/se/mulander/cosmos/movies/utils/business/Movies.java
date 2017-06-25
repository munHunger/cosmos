package se.mulander.cosmos.movies.utils.business;

import se.mulander.cosmos.common.business.HttpRequest;
import se.mulander.cosmos.movies.Settings;
import se.mulander.cosmos.movies.utils.model.TMDBResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcu on 2017-03-17.
 */
public class Movies
{
	public static List<TMDBResponse.Result> getRecomendations() throws Exception
	{
		List<TMDBResponse.Result> result = new ArrayList<>();
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(Settings.values.theMovieDb).append("/3/discover/movie");
		urlBuilder.append("?api_key=").append(Settings.values.apiV3Key);
		urlBuilder.append("&sort_by=popularity.desc");
		urlBuilder.append("&include_adult=false");
		urlBuilder.append("&include_video=false");
		urlBuilder.append("&page=1");
		urlBuilder.append("&primary_release_year=2017");
		TMDBResponse response = (TMDBResponse) HttpRequest.getRequest(urlBuilder.toString(), TMDBResponse.class).data;
		for(TMDBResponse.Result movie : response.results)
			result.add(movie);
		return result;
	}
}
