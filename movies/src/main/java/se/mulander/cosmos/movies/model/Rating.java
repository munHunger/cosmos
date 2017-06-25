package se.mulander.cosmos.movies.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by marcu on 2017-06-25.
 */
@ApiModel(description = "A rating coming from one provider")
public class Rating
{
	@ApiModelProperty(value = "A provider whom the rating is coming from. For example imdb or rotten tomato")
	public String provider;
	@ApiModelProperty(value = "The rating from the provider. The scale is not normalized and can not be compared across providers")
	public double rating;

	public Rating()
	{
	}

	public Rating(String provider, double rating)
	{
		this.provider = provider;
		this.rating = rating;
	}
}
