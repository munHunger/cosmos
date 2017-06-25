package se.tfmoney.microservice.tfMCCMicro.model.http;

/**
 * Created by Marcus Münger on 2017-04-28.
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
