package se.mulander.cosmos.movies.model;

import se.mulander.cosmos.common.database.MaxLength;
import se.mulander.cosmos.common.database.TableName;

/**
 * Created by marcu on 2017-02-18.
 */
@TableName(name = "settings")
@Deprecated
public class SettingValues
{
	public int databaseID = 1;
	public boolean autoDownload = false;
	@MaxLength(length = 50)
	public String apiV3Key = "6559b0b40f39a093b15b3c4213bdb613";
	@MaxLength(length = 250)
	public String apiV4Key = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NTU5YjBiNDBmMzlhMDkzYjE1YjNjNDIxM2JkYjYxMyIsInN1YiI6IjU4ZTEyZDkxOTI1MTQxMjdlODAwMTEyYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.Uo3pxNqSl6MsU-1zxv5ndXiYu7u9ZvaDz6kXMAGgt4c";
	@MaxLength(length = 250)
	public String theMovieDb = "https://api.themoviedb.org";
}
