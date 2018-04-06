package io.openliberty.guides.inventory.rest.client;

import java.util.Properties;

import javax.enterprise.context.Dependent;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@Path("/properties")
@Consumes("application/json")
@Dependent
@RegisterRestClient
public interface SystemResourceService {

	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public Properties getProperties();
}