package se.mulander.cosmos.movies.model;

import com.google.gson.annotations.SerializedName;
import com.wordnik.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcu on 2017-06-25.
 */
@ApiModel(value = "Movie", description = "A movie object containing all relevant information about a particular movie")
public class Movie
{
	@ApiModelProperty(name = "internal_id", value = "The unique identifier used by the cosmos movie system")
	@SerializedName("internal_id")
	public String internalID;
	@ApiModelProperty(name = "image_url", value = "An absolute link to a thumbnail image")
	@SerializedName("image_url")
	public String imageURL;
	@ApiModelProperty(value = "The title of the movie")
	public String title;
	@ApiModelProperty(value = "Release year of the movie")
	public int year;
	@ApiModelProperty(value = "A list of ratings by different providers such as rotten tomato and imdb")
	public List<Rating> rating = new ArrayList<>();
	@ApiModelProperty(name = "extended_movie",
					  value = "More details about the movie. This can be info such as a description an what actors are in it")
	@SerializedName("extended_movie")
	public ExtendedMovie extendedMovie;
}
