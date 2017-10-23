package se.mulander.cosmos.folderscraper.util;

import se.mulander.cosmos.common.model.settings.Setting;
import se.mulander.cosmos.common.settings.DatabaseSettings;

/**
 * Created by Marcus Münger on 2017-07-20.
 */
public class Settings extends DatabaseSettings
{
    public Settings()
    {
        DatabaseSettings.singleton = this;
        DatabaseSettings.init();
    }

    public Setting getDefaultSetting()
    {
        return new Setting("scraper",
                           new Setting("update_delay", "\\d+", "10"),
                           new Setting("folders",
                                       new Setting("downloads",
                                                   ".*",
                                                   "\\\\192.168.1.181\\munhunger\\Glory\\transmission"),
                                       new Setting("movies", ".*", "\\\\192.168.1.181\\munhunger\\Glory\\Movies"),
                                       new Setting("tv", ".*", "\\\\192.168.1.181\\munhunger\\Holy\\TV-Series")));
    }
}
