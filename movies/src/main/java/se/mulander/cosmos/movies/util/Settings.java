package se.mulander.cosmos.movies.util;

import se.mulander.cosmos.common.business.HttpRequest;
import se.mulander.cosmos.common.model.HttpResponse;
import se.mulander.cosmos.common.model.Setting;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Marcus Münger on 2017-07-20.
 */
public class Settings
{
    public static String theMovieDbURL = "https://api.themoviedb.org";

    public static String apiKey = "6559b0b40f39a093b15b3c4213bdb613";

    public static String settingsURL = null;

    private static Consumer<String> settingsUpdater = (url) -> {
        try
        {
            HttpResponse res = HttpRequest.getRequest(url, ArrayList.class);
            if (res.statusCode == HttpServletResponse.SC_OK)
            {
                List<Object> resultData = (List) res.data;
                Optional<Setting> movieSetting = resultData.stream()
                                                           .map(o -> (Setting) o)
                                                           .filter(s -> s.name.equals("movies"))
                                                           .findFirst();
                if (movieSetting.isPresent())
                {
                    for (Setting s : movieSetting.get().children)
                    {
                        if (s.name.equals("movie_db_api_key"))
                            apiKey = s.value;
                        else if (s.name.equals("movie_db_api_uri"))
                            theMovieDbURL = s.value;
                    }
                }
                else
                {
                    Setting dbURL = new Setting("movie_db_api_uri",
                                                "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",
                                                theMovieDbURL);
                    Setting dbKey = new Setting("movie_db_api_key", ".*", apiKey);
                    Setting movies = new Setting("moves", dbKey, dbURL);

                    HttpRequest.postRequest(settingsURL + "/settings/register", movies, null);
                }
            }
        }
        catch (Exception e)
        {}
    };

    public static void init()
    {
        settingsUpdater.accept(settingsURL + "/settings/structure");
        new Thread(() -> {
            while(true)
            {
                settingsUpdater.accept(settingsURL + "/settings/structure/poll");
            }
        }).start();
    }
}
