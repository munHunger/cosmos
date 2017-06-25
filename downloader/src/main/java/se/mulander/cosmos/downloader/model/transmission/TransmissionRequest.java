package se.mulander.cosmos.downloader.utils.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by marcu on 2017-04-23.
 */
public class TransmissionRequest
{
	public String method;
	public Object arguments;
	public int tag;

	public TransmissionRequest(String method, Object arguments, int tag)
	{
		this.method = method;
		this.arguments = arguments;
		this.tag = tag;
	}

	public TransmissionRequest(String method, Object arguments)
	{
		this(method, arguments, 0);
	}

	public static String getAllTorrentsJSON()
	{
		JsonObject object = new JsonObject();
		object.addProperty("method", "torrent-get");
		JsonObject args = new JsonObject();
		JsonElement fields = new Gson().toJsonTree(new String[]{"eta", "name", "status", "rateDownload (B/s)", "rateUpload (B/s)"});
		args.add("fields", fields);
		object.add("arguments", args);
		System.out.println(object.toString());
		return object.toString();
	}
}
