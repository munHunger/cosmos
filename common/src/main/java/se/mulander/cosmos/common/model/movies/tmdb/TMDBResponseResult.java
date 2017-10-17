package se.mulander.cosmos.common.model.movies.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDBResponseResult
{
    @JsonProperty("poster_path")
    public String posterPath;
    public boolean adult;
    public String overview;
    @JsonProperty("release_date")
    public String releaseDate;
    @JsonProperty("genre_ids")
    public List<Integer> genreIds;
    public int id;
    @JsonProperty("media_type")
    public String mediaType;
    @JsonProperty("original_title")
    public String originalTitle;
    @JsonProperty("original_language")
    public String originalLanguage;
    public String title;
    @JsonProperty("backdrop_path")
    public String backdropPath;
    public float popularity;
    @JsonProperty("vote_count")
    public int voteCount;
    public boolean video;
    @JsonProperty("vote_average")
    public float voteAverage;
}
