package se.mulander.cosmos.movies.util;

import se.mulander.cosmos.common.model.settings.Setting;
import se.mulander.cosmos.common.settings.DatabaseSettings;

/**
 * Created by Marcus Münger on 2017-07-20.
 */
public class Settings extends DatabaseSettings
{
    public static Setting defaultSetting = new Setting("movies", new Setting("movie_db_api_key", ".*",
                                                                             "6559b0b40f39a093b15b3c4213bdb613"),
                                                       new Setting("movie_db_api_uri",
                                                                   "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;" +
                                                                           "]*[-a-zA-Z0-9+&@#/%=~_|]",
                                                                   "https://api.themoviedb.org"));
}
