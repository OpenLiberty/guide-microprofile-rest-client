// tag::client[]
package io.openliberty.guides.inventory.client;

import java.util.Properties;

import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Dependent
@RegisterRestClient
// tag::simple[]
@RegisterProvider(UnknownHostNameExceptionMapper.class)
@Path("/properties")
public interface SystemClient {

	  @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public Properties getProperties() throws UnknownHostNameException;
}
// end::simple[]
// end::client[]
