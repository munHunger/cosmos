package se.mulander.cosmos.common.model.movies;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by marcu on 2017-06-25.
 */
@ApiModel(description = "A rating coming from one provider")
@Entity
@Table(name = "rating")
public class Rating implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "movie_id")
    @JsonIgnore
    public Movie parent;

    @ApiModelProperty(value = "A provider whom the rating is coming from. For example imdb or rotten tomato")
    @Column(name = "provider", length = 45)
    @Id
    public String provider;
    @ApiModelProperty(
            value = "The rating from the provider. The scale is not normalized and can not be compared across " +
                    "providers")
    @Column(name = "rating")
    public double rating;
    @ApiModelProperty(value = "The amount of votes from the provider")
    @JsonProperty(value = "vote_count")
    @Column(name = "vote_count")
    public int voteCount;
    @ApiModelProperty(value = "The highest possible rating")
    @Column(name = "max_vote")
    public double max;

    public Rating() {
    }

    public Rating(String provider, double rating, double max) {
        this.provider = provider;
        this.rating = rating;
        this.max = max;
    }

    public Rating(String provider, double rating, double max, int voteCount) {
        this.provider = provider;
        this.rating = rating;
        this.voteCount = voteCount;
        this.max = max;
    }
}
