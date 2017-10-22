package se.mulander.cosmos.common.settings;

import se.mulander.cosmos.common.discovery.Scanner;
import se.mulander.cosmos.common.model.settings.Setting;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    private static Consumer<String> settingsUpdater = (url) -> {
        Client client = null;
        try
        {
            client = ClientBuilder.newClient();
            Response res = client.target(url).request().buildGet().invoke();

            if (res.getStatus() == Response.Status.OK.getStatusCode())
            {
                List<Setting> settings = res.readEntity(new GenericType<List<Setting>>()
                {
                });

                Optional<Setting> movieSetting = settings.stream().filter(s -> s.name.equals("movies")).findFirst();
                if (movieSetting.isPresent()) settingValue = movieSetting.get();
                else client.target(settingsURL)
                           .path("/settings/register")
                           .request()
                           .buildPost(Entity.entity(singleton.getDefaultSetting(), MediaType.APPLICATION_JSON_TYPE))
                           .invoke();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (client != null) client.close();
        }
    };

    public static Optional<String> getSettingsValue(String path)
    {
        if (settingValue == null) return Optional.empty();
        return getSettingsValue(path, settingValue);
    }

    private static Optional<String> getSettingsValue(String path, Setting setting)
    {
        String[] pathSplit = path.split("\\.");
        if (pathSplit[0].equals(setting.name))
        {
            if (pathSplit.length == 1) return Optional.of(setting.value);
            for (Setting child : setting.children)
                if (pathSplit[1].equals(child.name))
                    return getSettingsValue(path.substring(path.indexOf(".") + 1), child);
        }
        return Optional.empty();
    }

    public static void init()
    {
        new Thread(() -> {
            while (settingsURL == null)
            {
                settingsURL = Scanner.find("/settings/api/discover");
                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            settingsURL += "/settings/api";
            settingsUpdater.accept(settingsURL + "/settings/structure");
            while (true)
            {
                settingsUpdater.accept(settingsURL + "/settings/structure/poll");
                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected abstract Setting getDefaultSetting();
}
