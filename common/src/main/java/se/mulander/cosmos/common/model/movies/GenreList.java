package se.mulander.cosmos.common.model.movies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by marcu on 2017-06-30.
 */
public class GenreList
{
	public List<Genre> genres;

	public GenreList()
	{}

	public GenreList(Genre... genres)
	{
		this.genres = new ArrayList<>();
		Collections.addAll(this.genres, genres);
	}
}
