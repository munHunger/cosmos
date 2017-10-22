package se.mulander.cosmos.common.discovery;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.glassfish.jersey.client.ClientProperties;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.QueryExp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.*;

/**
 * Created by Marcus Münger on 2017-07-20.
 */
@Path("/discover")
@Api(value = "Discovery", description = "Endpoints for finding other services")
public class Scanner
{
    public static String getLocalAddress() throws SocketException
    {
        Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
        for (; n.hasMoreElements(); )
        {
            NetworkInterface e = n.nextElement();
            Enumeration<InetAddress> a = e.getInetAddresses();
            for (; a.hasMoreElements(); )
            {
                InetAddress addr = a.nextElement();
                String address = addr.getHostAddress();
                if (!address.contains(":") && !address.startsWith("127")) return address;
            }
        }
        return null;
    }

    /**
     * Scans the network for a specific discovery service.
     * It does this by using it's current IP, removing the last byte and iterating over networks.
     * If anyone responds on the address with an OK and the entity "cosmos" then a service has been found.
     * <p>
     * If the computer is offline and thus doesn't have an IP, then 127.0.0.1 will be used
     *
     * @param path        The path to the discovery of the sought service
     * @param serviceName The name of the current service context
     * @return An address to the service
     */
    public static String find(String path, String serviceName)
    {
        Client client = null;
        try
        {
            int port = findPort(serviceName);
            String localIP = getLocalAddress();
            if (localIP == null) return "127.0.0.1";
            String gate = "http://" + localIP.substring(localIP.indexOf("/") + 1, localIP.lastIndexOf(".") + 1);

            client = ClientBuilder.newClient();
            client.property(ClientProperties.CONNECT_TIMEOUT, 3);
            client.property(ClientProperties.READ_TIMEOUT, 3);

            for (int i = 1; i < 255; i++)
            {
                Response response = null;
                try
                {
                    response = client.target(String.format("%s%d:%d", gate, i, port)).path(path).request().get();
                    int responseStatus = response.getStatus();
                    String data = response.readEntity(String.class);

                    if (responseStatus == 200 && data.equals("cosmos")) return String.format("%s%d:%d", gate, i, port);
                } catch (ProcessingException e)
                {
                } catch (Exception e)
                {
                    e.printStackTrace();
                } finally
                {
                    if (response != null) response.close();
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            client.close();
        }
        return null;
    }

    /**
     * Searches on localhost for itself.
     * This starts on 9999 and going down.
     * If multiple instances are running on the same machine, it will still find itself.
     * <p>
     * It will prefer ssl and search for https, but if nothing is found it will switch to http and try again
     *
     * @param serviceName the name of the current service context
     * @return the port that the current instance is running on
     */
    private static int findPort(String serviceName)
    {
        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.CONNECT_TIMEOUT, 10);
        client.property(ClientProperties.READ_TIMEOUT, 10);
        try
        {
            Response res = client.target("http://127.0.0.1:80/movies/api/discover/self").request().get();
            for (int s = 0; s <= 1; s++)
                for (int i = 9999; i > 0; i--)
                {
                    try (ServerSocket serverSocket = new ServerSocket(i))
                    {
                        continue;
                    } catch (IOException e)
                    {
                    }
                    Response response = null;
                    try
                    {
                        String target = String.format("%s127.0.0.1:%d", (s == 0 ? "https://" : "http://"), i);
                        WebTarget webTarget = client.target(target)
                                                    .path(String.format("/%s/", serviceName))
                                                    .path("/api/discover/self");
                        target = webTarget.getUri().toString();
                        response = webTarget.request().get();
                        int responseStatus = response.getStatus();
                        String data = response.readEntity(String.class);

                        if (responseStatus == 200 && data.equals(id))
                            return i;
                    } catch (ProcessingException e)
                    {
                        e.printStackTrace();
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    } finally
                    {
                        if (response != null) response.close();
                    }
                }
        } catch (Throwable t)
        {
            t.printStackTrace();
        } finally
        {
            client.close();
        }
        return -1;
    }

    private static String id = UUID.randomUUID().toString();

    @Context
    HttpServletRequest httpRequest;

    @GET
    @Path("/self")
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Self discovery", notes = "Static point that returns a unique ID that only this service is aware of. This can be used for the service to scan and find what port it is being hosted on")
    @ApiResponses({@ApiResponse(code = HttpServletResponse.SC_OK, message = "static OK with a unique ID for this service instance")})
    public Response selfDiscover()
    {
        return Response.ok(id).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Discover", notes = "Static point that returns 200 OK, to note that this endpoint is alive. As this is common " + "for all cosmos microservices")
    @ApiResponses({@ApiResponse(code = HttpServletResponse.SC_OK, message = "static OK to note that it is alive")})
    public Response discover()
    {
        return Response.ok("cosmos").build();
    }
}
