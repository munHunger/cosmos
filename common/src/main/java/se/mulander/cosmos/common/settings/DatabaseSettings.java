package se.mulander.cosmos.common.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import se.mulander.cosmos.common.business.HttpRequest;
import se.mulander.cosmos.common.discovery.Scanner;
import se.mulander.cosmos.common.model.settings.Setting;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Marcus Münger on 2017-07-20.
 */
public abstract class DatabaseSettings
{
    public static String settingsURL = null;

    public static Setting settingValue;

    protected static DatabaseSettings singleton;

    protected abstract Setting getDefaultSetting();

    public static String getSettingsValue(String path)
    {
        if (settingValue == null)
            return null;
        return getSettingsValue(path, settingValue);
    }

    private static String getSettingsValue(String path, Setting setting)
    {
        String[] pathSplit = path.split(".");
        if (pathSplit[0].equals(setting.name))
        {
            if (pathSplit.length == 1)
                return setting.value;
            for (Setting child : setting.children)
                if (pathSplit[1].equals(child.name))
                    return getSettingsValue(path.substring(path.indexOf(".") + 1), child);
        }
        return null;
    }

    private static Consumer<String> settingsUpdater = (url) ->
    {
        try
        {

            RequestConfig.Builder requestBuilder = RequestConfig.custom();
            HttpClientBuilder builder = HttpClientBuilder.create();
            builder.setDefaultRequestConfig(requestBuilder.build());
            HttpClient client = builder.build();

            org.apache.http.HttpResponse response = client.execute(new HttpGet(url));
            if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK)
            {
                ObjectMapper mapper = new ObjectMapper();
                List<Setting> settings = mapper.readValue(response.getEntity().getContent(), mapper.getTypeFactory()
                                                                                                   .constructCollectionType(
                                                                                                           List.class,
                                                                                                           Setting
                                                                                                                   .class));

                Optional<Setting> movieSetting = settings.stream()
                                                         .filter(s -> s.name.equals("movies"))
                                                         .findFirst();
                if (movieSetting.isPresent())
                    settingValue = movieSetting.get();
                else
                    HttpRequest.postRequest(settingsURL + "/settings/register", singleton.getDefaultSetting(), null);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    };

    public static void init()
    {
        new Thread(() ->
                   {
                       while (settingsURL == null)
                       {
                           settingsURL = Scanner.find(80, "/settings/api/discover");
                       }
                       settingsURL += "/settings/api";
                       settingsUpdater.accept(settingsURL + "/settings/structure");
                       while (true)
                       {
                           settingsUpdater.accept(settingsURL + "/settings/structure/poll");
                       }
                   }).start();
    }
}
