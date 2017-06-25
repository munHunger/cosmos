package se.mulander.cosmos.downloader.util;

import se.mulander.cosmos.common.business.HttpRequest;
import se.mulander.cosmos.piratebayAPI.Jpa;
import se.mulander.cosmos.piratebayAPI.Query;
import se.mulander.cosmos.piratebayAPI.QueryOrder;
import se.mulander.cosmos.piratebayAPI.Torrent;

import java.util.*;

/**
 * Created by marcu on 2017-04-14.
 */
public class Downloader
{
	private static String username = "munhunger";
	private static String password = "warthog";
	private static String url = "http://192.168.1.83:9091/transmission/rpc";
	private static String authHeader = "";

	static
	{
		authHeader = Base64.getEncoder().encodeToString(String.format("%s:%s", username, password).getBytes());
	}

	public boolean downloadItem(String title) throws Exception
	{
		List<Torrent> torrents = Jpa.Search(new Query(title, 0, QueryOrder.BySeeds));
		if(!torrents.isEmpty())
		{
			for(Torrent t : torrents)
			{
				if(!t.Name.contains("CAM"))
				{
					Map<String, String> headers = new HashMap<>();
					headers.put("Authorization", "Basic " + authHeader);
					Map<String, Object> body = new HashMap<>();
					body.put("method", "torrent-get");
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("fields", Arrays.asList("name"));
					body.put("arguments", parameters);

					String res = HttpRequest.postRequest(url, headers, body, null).data.toString();

					/*
					String response = (String) HttpRequest.postRequest("http://192.168.1.83:9091/transmission/rpc/", TransmissionRequest.getAllTorrentsJSON(), "munhunger", "warthog", TransmissionResponse.class);
					String xSessionID = response.substring(response.indexOf("X-Transmission-Session-Id: ") + "X-Transmission-Session-Id: ".length(), response.indexOf("</code>"));

					TransmissionResponse res = (TransmissionResponse) HttpRequest.postRequest("http://192.168.1.83:9091/transmission/rpc/", TransmissionRequest.getAllTorrentsJSON(), "munhunger", "warthog", TransmissionResponse.class, new AbstractMap.SimpleEntry<>("X-Transmission-Session-Id", xSessionID));
					System.out.println(res.result);
					*/
				}
			}
			return true;
		}
		return false;
	}
}
