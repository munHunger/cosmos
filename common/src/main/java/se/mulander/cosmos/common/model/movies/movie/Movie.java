package se.mulander.cosmos.common.model.movies.movie;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import se.mulander.cosmos.common.model.movies.ExtendedMovie;
import se.mulander.cosmos.common.model.movies.Rating;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by marcus on 2017-06-25.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Movie", description = "A movie object containing all relevant information about a particular movie")
@Entity
@Table(name = "movie")
public class Movie {
    @ApiModelProperty(name = "internal_id", value = "The unique identifier used by the cosmos system")
    @JsonProperty("internal_id")
    @Column(name = "movie_id", length = 64)
    @Id
    public String internalID;
    @ApiModelProperty(name = "image_url", value = "An absolute link to a thumbnail image")
    @JsonProperty("image_url")
    @Column(name = "image_url")
    public String imageURL;
    @ApiModelProperty(value = "The title of the movie")
    @Column(name = "title")
    public String title;
    @ApiModelProperty(value = "Release year of the movie")
    @Column(name = "year")
    public int year;
    @ApiModelProperty(value = "A list of ratings by different providers such as rotten tomato and imdb")
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "rating")
    public List<Rating> rating = new ArrayList<>();
    @ApiModelProperty(name = "extended_movie",
                      value = "More details about the movie. This can be info such as a description an what actors " +
                              "are in it")
    @JsonProperty("extended_movie")
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "movie_id")
    public ExtendedMovie extendedMovie;
    @ApiModelProperty(value = "A list of strings noting what genre the movie is part of")
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "genre", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genre")
    public List<String> genre = new ArrayList<>();

    public Movie() {
    }

    public Movie setID(String id) {
        this.internalID = id;
        if (this.extendedMovie != null)
            this.extendedMovie.movieID = id;
        return this;
    }

    public Movie setExtended(ExtendedMovie ex) {
        ex.parent = this;
        ex.movieID = this.internalID;
        this.extendedMovie = ex;
        return this;
    }

    public Movie addGenre(String genre) {
        this.genre.add(genre);
        return this;
    }

    public Movie(String imageURL, String title, int year) {
        this.internalID = UUID.randomUUID().toString();
        this.imageURL = imageURL;
        this.title = title;
        this.year = year;
    }

    public Movie addRating(Rating r) {
        r.parent = this;
        this.rating.add(r);
        return this;
    }
}
