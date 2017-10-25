package se.mulander.cosmos.common.model.movies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import se.mulander.cosmos.common.model.movies.movie.Movie;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcu on 2017-06-25.
 */
@Entity
@Table(name = "extended_movie")
public class ExtendedMovie {
    @Id
    @Column(name = "movie_id", length = 64)
    @JsonIgnore
    public String movieID;
    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    @JsonIgnore
    public Movie parent;

    @ApiModelProperty(value = "A short synopsis describing what the movie is about")
    @Column(name = "description", length = 2048)
    public String description;
    @ApiModelProperty(value = "A list of all people who contributed to the movie")
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "movie_id", referencedColumnName = "movie_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<Cast> cast = new ArrayList<>();
    @ApiModelProperty(name = "poster_url", value = "An absolute link to a poster image")
    @JsonProperty("poster_url")
    @Column(name = "poster_url")
    public String posterURL;

    @ApiModelProperty(value = "Describing the status of the movie Can be SEARCHED, WANTED, RELEASED, IN LIBRARY")
    @JsonProperty("status")
    @Column(name= "status", length = 16)
    public String status;
    @JsonIgnore
    @Column(name= "tmdb_ID")
    public int tmdbID;

    public ExtendedMovie() {
    }

    public ExtendedMovie(String description, String posterURL, Status status, int tmbdID) {
        this.description = description;
        this.posterURL = posterURL;
        this.status = status.name();
        this.tmdbID = tmbdID;
    }

    public ExtendedMovie addCastMember(Cast c) {
        c.movieID = this.movieID;
        this.cast.add(c);
        return this;
    }
}
