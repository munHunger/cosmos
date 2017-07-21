package se.mulander.cosmos.movies.model;

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
    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    public Movie parent;

    @ApiModelProperty(value = "A short synopsis describing what the movie is about")
    @Column(name = "description")
    public String description;
    @ApiModelProperty(value = "A list of all people who contributed to the movie")
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "starredIn", orphanRemoval = true)
    public List<Cast> cast = new ArrayList<>();
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

    public ExtendedMovie addCastMember(Cast c)
    {
        c.starredIn = this.parent;
        this.cast.add(c);
        return this;
    }
}
