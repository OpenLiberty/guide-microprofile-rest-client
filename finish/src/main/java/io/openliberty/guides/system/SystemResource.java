// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
 // end::copyright[]
package io.openliberty.guides.system;

import java.util.Properties;

// CDI
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
// JAX-RS
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.UnknownHostException;

@RequestScoped
@Path("/properties")
public class SystemResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Properties getProperties() {
    // try {
    //   Properties props = System.getProperties();
    //   return Response.ok(props).build();
    // } catch (UnknownHostException e){
    //   return Response.status(Response.Status.NOT_FOUND).build();
    // }
    return System.getProperties();
  }
}
