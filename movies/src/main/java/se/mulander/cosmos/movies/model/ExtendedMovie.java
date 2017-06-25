package se.mulander.cosmos.movies.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcu on 2017-06-25.
 */
public class ExtendedMovie
{
	@ApiModelProperty(value = "A short synopsis describing what the movie is about")
	public String description;
	@ApiModelProperty(value = "A list of actors who starred in the movie")
	public List<String> actors = new ArrayList<>();
	@ApiModelProperty(value = "A list of people who directed the movie")
	public List<String> directors = new ArrayList<>();
	@ApiModelProperty(value = "A list of people who wrote the screenplay for the movie")
	public List<String> writers = new ArrayList<>();
	@ApiModelProperty(name = "poster_url", value = "An absolute link to a poster image")
	@SerializedName("poster_url")
	public String posterURL;
}
