package se.mulander.cosmos.movies.model;

import java.util.List;

/**
 * Created by marcu on 2017-04-02.
 */
public class TMDBResponse
{
	public int page;
	public Result[] results;

	public class Result
	{
		public String poster_path;
		public boolean adult;
		public String overview;
		public String release_date;
		public List<Integer> genre_ids;
		public int id;
		public String original_title;
		public String original_language;
		public String title;
		public String backdrop_path;
		public float popularity;
		public int vote_count;
		public boolean video;
		public float vote_average;
	}
}
