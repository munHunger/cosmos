package se.mulander.cosmos.movies.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import se.mulander.cosmos.common.business.HttpRequest;
import se.mulander.cosmos.common.discovery.Scanner;
import se.mulander.cosmos.common.model.Setting;

import javax.servlet.http.HttpServletResponse;
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

            RequestConfig.Builder requestBuilder = RequestConfig.custom();
            HttpClientBuilder builder = HttpClientBuilder.create();
            builder.setDefaultRequestConfig(requestBuilder.build());
            HttpClient client = builder.build();

            org.apache.http.HttpResponse response = client.execute(new HttpGet(url));
            if(response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK)
            {
                ObjectMapper mapper = new ObjectMapper();
                List<Setting> settings = mapper.readValue(response.getEntity().getContent(), mapper.getTypeFactory().constructCollectionType(List.class, Setting.class));

                Optional<Setting> movieSetting = settings.stream()
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
                    Setting movies = new Setting("movies", dbKey, dbURL);

                    HttpRequest.postRequest(settingsURL + "/settings/register", movies, null);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    };

    public static void init()
    {
        settingsURL = Scanner.find(8080, "/settings/api/discover") + "/settings/api";
        settingsUpdater.accept(settingsURL + "/settings/structure");
        new Thread(() -> {
            while(true)
            {
                settingsUpdater.accept(settingsURL + "/settings/structure/poll");
            }
        }).start();
    }
}
