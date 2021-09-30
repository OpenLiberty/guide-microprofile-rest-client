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

import java.util.List;
import java.util.Properties;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;

@RequestScoped
@Path("/properties")
public class SystemResource {

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Properties getProperties(@QueryParam("properties") String listproperties) {

    if (listproperties != null) {
      String[] params = listproperties.split(",");
      Properties properties = new Properties();
      for (String param: params) {
        String property = System.getProperty(param);
        if (property == null) {
          Properties error = new Properties();
          error.put("Unknown property:", param);
          return error;
        }
        properties.put(param, property);
      }
      return properties;
    }
    return System.getProperties();
  }

}
