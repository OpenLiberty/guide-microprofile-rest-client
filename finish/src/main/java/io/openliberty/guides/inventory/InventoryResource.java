// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.inventory;

import java.util.List;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import io.openliberty.guides.inventory.model.InventoryList;
import javax.ws.rs.QueryParam;

// tag::RequestScoped[]
@RequestScoped
// end::RequestScoped[]
@Path("/systems")
public class InventoryResource {

  // tag::Inject[]
  @Inject
  InventoryManager manager;
  // end::Inject[]

  @GET
  @Path("/{hostname}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPropertiesForHost(@PathParam("hostname") String hostname,
                    @QueryParam("listproperties") List<String> listproperties) {
    // Get properties
    Properties props = manager.get(hostname, listproperties);
    if (props == null) {
      return Response.status(Response.Status.NOT_FOUND)
                     .entity("{ \"error\" : \"Unknown hostname or the system service "
                     + "may not be running on " + hostname + "\" }")
                     .build();
    }

    // Add to inventory
    manager.add(hostname, listproperties, props);
    return Response.ok(props).build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public InventoryList listContents() {
    return manager.list();
  }
}
