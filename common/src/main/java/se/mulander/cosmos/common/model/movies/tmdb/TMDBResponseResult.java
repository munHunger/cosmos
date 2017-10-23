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

    public TMDBResponseResult()
    {
    }

    public TMDBResponseResult(String posterPath, boolean adult, String overview, String releaseDate,
                              List<Integer> genreIds, int id, String mediaType, String originalTitle,
                              String originalLanguage, String title, String backdropPath, float popularity,
                              int voteCount, boolean video, float voteAverage)
    {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.id = id;
        this.mediaType = mediaType;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
    }
}
