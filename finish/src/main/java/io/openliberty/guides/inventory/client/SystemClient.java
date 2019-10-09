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

import java.net.URISyntaxException;
import java.util.Properties;
import javax.enterprise.context.Dependent;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.lang.AutoCloseable;

// tag::annotations[]
@Dependent
@RegisterRestClient(configKey="systemClient", baseUri="http://localhost:9080/system")
@RegisterProvider(UnknownUrlExceptionMapper.class)
@Path("/properties")
public interface SystemClient extends AutoCloseable {
  // end::annotations[]

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Properties getProperties() throws URISyntaxException, ProcessingException;
}
// end::client[]
