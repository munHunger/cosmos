package se.mulander.cosmos.common.model.movies.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by marcu on 2017-04-02.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDBResponse
{
    public int page;
    @JsonProperty("total_results")
    public int totalResults;
    @JsonProperty("total_pages")
    public int totalPages;
    public TMDBResponseResult[] results;

    public TMDBResponseResult buildResult(String type)
    {
        TMDBResponseResult result = new TMDBResponseResult();
        result.mediaType = type;
        return result;
    }
}
