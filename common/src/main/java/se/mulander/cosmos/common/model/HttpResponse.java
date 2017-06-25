package se.mulander.cosmos.common.model;

/**
 * Created by marcu on 2017-06-25.
 */
public class HttpResponse
{
	public Object data;
	public int statusCode;

	public HttpResponse(Object data, int statusCode)
	{
		this.data = data;
		this.statusCode = statusCode;
	}

	/**
	 * Checks if the status code is an error code or not.
	 * i.e. if 200 <= statusCode < 300 holds
	 *
	 * @return true iff the status code is an error code or not
	 */
	public boolean isErrorCode()
	{
		return !(statusCode >= 200 && statusCode < 300);
	}
}