package se.mulander.cosmos.common.model.movies.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import se.mulander.cosmos.common.model.movies.Cast;

import java.util.List;

/**
 * Created by Marcus Münger on 2017-07-21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TMDBCastResponse
{
    public int id;
    public List<Cast> cast;
    public List<Cast> crew;
}
