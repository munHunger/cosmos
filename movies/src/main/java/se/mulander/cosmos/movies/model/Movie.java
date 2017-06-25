package se.mulander.cosmos.movies.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcu on 2017-06-25.
 */
public class Movie
{
	public String internalID;
	public String imageURL;
	public String title;
	public int year;
	public List<Rating> rating = new ArrayList<>();
	public ExtendedMovie extendedMovie;
}
