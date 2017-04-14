package se.mulander.cosmos.downloader.utils.business;

import se.mulander.cosmos.piratebayAPI.Jpa;
import se.mulander.cosmos.piratebayAPI.Query;
import se.mulander.cosmos.piratebayAPI.QueryOrder;
import se.mulander.cosmos.piratebayAPI.Torrent;

import java.util.List;

/**
 * Created by marcu on 2017-04-14.
 */
public class Downloader
{
	public boolean downloadItem(String title) throws Exception
	{
		List<Torrent> torrents = Jpa.Search(new Query(title, 0, QueryOrder.BySeedsDescending));
		if(!torrents.isEmpty())
		{
			return true;
		}
		return false;
	}
}
