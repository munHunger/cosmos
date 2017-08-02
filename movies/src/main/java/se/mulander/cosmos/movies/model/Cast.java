package se.mulander.cosmos.movies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by Marcus Münger on 2017-07-21.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "cast")
@ApiModel(description = "A cast member that has been part of making the movie")
public class Cast
{
	@Id
	@Column(name = "id")
	@JsonIgnore
	public String id;

	public Cast()
	{
		generateID();
	}

	public void generateID()
	{
		this.id = UUID.randomUUID().toString();
	}

	@JsonIgnore
	@Column(name = "movie_id")
	public String movieID;

	@Column(name = "department")
	@ApiModelProperty(
			value = "The work area that the member has participated in. This could for example be Sound, Directing or Visual Effects")
	public String department;

	@Column(name = "job")
	@ApiModelProperty(
			value = "The sub genre of what the member has been doing. For example if department is Sound, this could be Orchestrator")
	public String job;

	@Column(name = "movie_character")
	@ApiModelProperty(value = "If the member played a part in the movie, then this is the name of the played character")
	public String character;

	@Column(name = "name")
	@ApiModelProperty(value = "The name of the cast member")
	public String name;

	@JsonProperty("profile_url")
	@Column(name = "profile_url")
	@ApiModelProperty(value = "A url to an image with the members profile")
	public String profileURL;
}
