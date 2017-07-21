package se.mulander.cosmos.movies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcu on 2017-06-25.
 */
@Entity
@Table(name = "extended_movie")
public class ExtendedMovie
{
	@Id
	@Column(name = "movie_id")
	@JsonIgnore
	public String movieID;

	@OneToOne(fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	public Movie parent;

	@ApiModelProperty(value = "A short synopsis describing what the movie is about")
	@Column(name = "description")
	public String description;
	@ApiModelProperty(value = "A list of actors who starred in the movie")
	@ElementCollection
	@CollectionTable(name = "actors", joinColumns = @JoinColumn(name = "movie_id"))
	@Column(name = "name")
	public List<String> actors = new ArrayList<>();
	@ApiModelProperty(value = "A list of people who directed the movie")
	@ElementCollection
	@CollectionTable(name = "directors", joinColumns = @JoinColumn(name = "movie_id"))
	@Column(name = "name")
	public List<String> directors = new ArrayList<>();
	@ApiModelProperty(value = "A list of people who wrote the screenplay for the movie")
	@ElementCollection
	@CollectionTable(name = "writers", joinColumns = @JoinColumn(name = "movie_id"))
	@Column(name = "name")
	public List<String> writers = new ArrayList<>();
	@ApiModelProperty(name = "poster_url", value = "An absolute link to a poster image")
	@SerializedName("poster_url")
	@Column(name = "poster_url")
	public String posterURL;

	public ExtendedMovie()
	{
	}

	public ExtendedMovie(String description, String posterURL)
	{
		this.description = description;
		this.posterURL = posterURL;
	}
}
