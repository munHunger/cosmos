package se.mulander.cosmos.settings;


import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.models.Swagger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by marcu on 2017-02-14.
 */
public class Main extends HttpServlet
{
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		ServletContext context = config.getServletContext();
		Swagger swagger = new Swagger();
		new SwaggerContextService().withServletConfig(config).updateSwagger(swagger);
	}
}