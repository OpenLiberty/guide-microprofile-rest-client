// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
// tag::manager[]
package io.openliberty.guides.inventory;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.openliberty.guides.inventory.client.SystemClient;
import io.openliberty.guides.inventory.client.UnknownUrlExceptionMapper;
import io.openliberty.guides.inventory.model.InventoryList;
import io.openliberty.guides.inventory.model.SystemData;

// tag::applicationScoped[]
@ApplicationScoped
// end::applicationScoped[]
public class InventoryManager {

  private List<SystemData> systems = Collections.synchronizedList(new ArrayList<>());
  private final String DEFAULT_PORT = System.getProperty("default.http.port");

  // tag::inject[]
  @Inject
  // end::inject[]
  // tag::restClient[]
  @RestClient
  // end::restClient[]
  // tag::defaultrestClient[]
  private SystemClient defaultRestClient;
  // end::defaultrestClient[]

  public Properties get(String hostname) {
    Properties properties = null;
    if (hostname.equals("localhost")) {
      properties = getPropertiesWithDefaultHostName();
    } else {
      properties = getPropertiesWithGivenHostName(hostname);
    }

    return properties;
  }

  public void add(String hostname, Properties systemProps) {
    Properties props = new Properties();
    props.setProperty("os.name", systemProps.getProperty("os.name"));
    props.setProperty("user.name", systemProps.getProperty("user.name"));

    SystemData host = new SystemData(hostname, props);
    if (!systems.contains(host))
      systems.add(host);
  }

  public InventoryList list() {
    return new InventoryList(systems);
  }

  // tag::getPropertiesWithDefault[]
  private Properties getPropertiesWithDefaultHostName() {
    try {
      // tag::defaultGetProperties[]
      return defaultRestClient.getProperties();
      // end::defaultGetProperties[]
    } catch (URISyntaxException e) {
        System.err.println("The given URI is not formatted correctly.");
    } catch (ProcessingException ex) {
      handleProcessingException(ex);
    }
    return null;
  }
  // end::getPropertiesWithDefault[]

  // tag::builder[]
  private Properties getPropertiesWithGivenHostName(String hostname) {
    String customURIString = "http://" + hostname + ":" + DEFAULT_PORT + "/system";
    URI customURI = null;
    try {
      customURI = new URI(customURIString);
      // tag::restClientBuilder[]
      SystemClient customRestClient = RestClientBuilder.newBuilder()
                                         .baseUri(customURI)
                                         .register(UnknownUrlExceptionMapper.class)
                                         .build(SystemClient.class);
      // end::restClientBuilder[]
      // tag::customGetProperties[]
      return customRestClient.getProperties();
      // end::customGetProperties[]
    } catch (ProcessingException ex) {
      handleProcessingException(ex);
    } catch (URISyntaxException e) {
      System.err.println("The given URI is not formatted correctly.");
    }
    return null;
  }
  // end::builder[]

  private void handleProcessingException(ProcessingException ex) {
    Throwable rootEx = ExceptionUtils.getRootCause(ex);
    if (rootEx != null && (rootEx instanceof UnknownHostException || 
        rootEx instanceof ConnectException)) {
      System.err.println("The specified host is unknown.");
    } else {
      throw ex;
    }
  }

}
// end::manager[]
