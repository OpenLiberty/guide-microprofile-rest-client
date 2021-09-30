// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018, 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
// tag::client[]
package io.openliberty.guides.inventory.client;

import java.util.Properties;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

// tag::RegisterRestClient[]
@RegisterRestClient(configKey = "systemClient", baseUri = "http://localhost:9080/system")
// end::RegisterRestClient[]
// tag::RegisterProvider[]
@RegisterProvider(UnknownUriExceptionMapper.class)
// end::RegisterProvider[]
@Path("/properties")
// tag::SystemClient[]
// tag::AutoCloseable[]
public interface SystemClient extends AutoCloseable {
// end::AutoCloseable[]

  @GET
  // tag::Produces[]
  @Produces(MediaType.APPLICATION_JSON)
  // end::Produces[]
  // tag::getProperties[]
  public Properties getProperties() throws UnknownUriException, ProcessingException;
  // end::getProperties[]

  @GET 
  // tag::getListProperties[]
  @Path("/listproperties")
  @Produces(MediaType.APPLICATION_JSON)
  public Properties getListProperties(@QueryParam("properties") List<String> listproperties) throws UnknownUriException, ProcessingException;
  // end::getListProperties[]
}
// end::SystemClient[]
// end::client[]
