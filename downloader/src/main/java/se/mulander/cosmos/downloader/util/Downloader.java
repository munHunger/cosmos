package se.mulander.cosmos.downloader.util;

import se.mulander.cosmos.common.business.HttpRequest;
import se.mulander.cosmos.common.model.HttpResponse;
import se.mulander.cosmos.downloader.model.transmission.TransmissionResponse;
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
	private String sessionID = "";

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
				if(!t.Name.toUpperCase().contains("CAM") && !t.Name.toUpperCase().contains("TS") && !t.Name.toUpperCase().contains("DVD"))
				{
					Map<String, String> headers = new HashMap<>();
					headers.put("Authorization", "Basic " + authHeader);
					Map<String, Object> body = new HashMap<>();
					body.put("method", "torrent-add");
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("filename", t.Magnet);
					body.put("arguments", parameters);

					HttpResponse response = HttpRequest.postRequest(url, headers, body, TransmissionResponse.class);
					if(response.statusCode == 409)
					{
						sessionID = Arrays.asList(response.headers).stream().filter(h -> h.getName().toUpperCase().equals("X-TRANSMISSION-SESSION-ID")).findFirst().get().getValue();
						headers.put("X-Transmission-Session-Id", sessionID);
						response = HttpRequest.postRequest(url, headers, body, TransmissionResponse.class);
					}
					TransmissionResponse res = (TransmissionResponse) response.data;
					if(res.result.trim().toUpperCase().equals("SUCCESS"))
						return true;
				}
			}
		}
		return false;
	}
}
