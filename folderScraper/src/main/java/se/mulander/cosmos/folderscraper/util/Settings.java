package se.mulander.cosmos.folderscraper.util;

import se.mulander.cosmos.common.model.settings.Setting;
import se.mulander.cosmos.common.settings.DatabaseSettings;

/**
 * Created by Marcus Münger on 2017-07-20.
 */
public class Settings extends DatabaseSettings
{
    public Setting getDefaultSetting()
    {
        return null;
    }

    public Settings()
    {
        DatabaseSettings.singleton = this;
        DatabaseSettings.init();
    }
}
