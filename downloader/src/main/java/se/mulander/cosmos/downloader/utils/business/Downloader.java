package se.mulander.cosmos.downloader.utils.business;

import se.mulander.cosmos.common.business.HttpRequest;
import se.mulander.cosmos.downloader.utils.model.TransmissionRequest;
import se.mulander.cosmos.downloader.utils.model.TransmissionResponse;
import se.mulander.cosmos.piratebayAPI.Jpa;
import se.mulander.cosmos.piratebayAPI.Query;
import se.mulander.cosmos.piratebayAPI.QueryOrder;
import se.mulander.cosmos.piratebayAPI.Torrent;

import java.util.AbstractMap;
import java.util.List;

/**
 * Created by marcu on 2017-04-14.
 */
public class Downloader
{
	public boolean downloadItem(String title) throws Exception
	{
		List<Torrent> torrents = Jpa.Search(new Query(title, 0, QueryOrder.BySeeds));
		if(!torrents.isEmpty())
		{
			for(Torrent t : torrents)
			{
				if(!t.Name.contains("CAM"))
				{

					String response = (String) HttpRequest.postRequest("http://192.168.1.83:9091/transmission/rpc/", TransmissionRequest.getAllTorrentsJSON(), "munhunger", "warthog", TransmissionResponse.class);
					String xSessionID = response.substring(response.indexOf("X-Transmission-Session-Id: ") + "X-Transmission-Session-Id: ".length(), response.indexOf("</code>"));
					
					TransmissionResponse res = (TransmissionResponse) HttpRequest.postRequest("http://192.168.1.83:9091/transmission/rpc/", TransmissionRequest.getAllTorrentsJSON(), "munhunger", "warthog", TransmissionResponse.class, new AbstractMap.SimpleEntry<>("X-Transmission-Session-Id", xSessionID));
					System.out.println(res.result);
				}
			}
			return true;
		}
		return false;
	}
}
