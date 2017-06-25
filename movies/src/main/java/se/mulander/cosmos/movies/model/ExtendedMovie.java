package se.mulander.cosmos.movies.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcu on 2017-06-25.
 */
public class ExtendedMovie
{
	public String description;
	public List<String> actors = new ArrayList<>();
	public List<String> directors = new ArrayList<>();
	public List<String> writers = new ArrayList<>();
	public String posterURL;
}
