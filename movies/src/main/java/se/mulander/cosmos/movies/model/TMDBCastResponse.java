package se.mulander.cosmos.movies.model;

import se.mulander.cosmos.common.model.movies.Cast;

import java.util.List;

/**
 * Created by Marcus Münger on 2017-07-21.
 */
public class TMDBCastResponse
{
    public int id;
    public List<Cast> cast;
    public List<Cast> crew;
}
