// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2018, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
// tag::mapper[]
package io.openliberty.guides.inventory.client;

import java.util.logging.Logger;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

@Provider
public class UnknownUriExceptionMapper
    implements ResponseExceptionMapper<UnknownUriException> {
  Logger LOG = Logger.getLogger(UnknownUriExceptionMapper.class.getName());

  @Override
  // tag::handles[]
  public boolean handles(int status, MultivaluedMap<String, Object> headers) {
    LOG.info("status = " + status);
    return status == 404;
  }
  // end::handles[]

  @Override
  // tag::toThrowable[]
  public UnknownUriException toThrowable(Response response) {
    return new UnknownUriException();
  }
  // end::toThrowable[]
}
// end::mapper[]
