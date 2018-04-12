// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.inventory.rest.client;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Properties;
import java.net.URI;
import java.net.URL;

public class SystemClient {

  // Constants for building URI to the system service.
  private final int DEFAULT_PORT = Integer.valueOf(System.getProperty("default.http.port"));
  private final String SYSTEM_PROPERTIES = "/system";
  private final String PROTOCOL = "http";

  private String url;
  
  @Inject
  @RestClient
  private SystemResourceService restClientService;

  // Used by the following guide(s): CDI, MP-METRICS, FAULT-TOLERANCE
  public void init(String hostname) {
    this.initHelper(hostname, DEFAULT_PORT);
  }

  // Used by the following guide(s): MP-CONFIG, MP-HEALTH
  public void init(String hostname, int port) {
    this.initHelper(hostname, port);
  }

  // Helper method to set the attributes.
  private void initHelper(String hostname, int port) {
    this.url = buildUrl(PROTOCOL, hostname, port, SYSTEM_PROPERTIES);
  }

  // Wrapper function that gets properties
  public Properties getProperties() {
    return getPropertiesHelper();
  }

  // tag::doc[]
  /**
   * Builds the URI string to the system service for a particular host.
   * @param protocol
   *          - http or https.
   * @param host
   *          - name of host.
   * @param port
   *          - port number.
   * @param path
   *          - Note that the path needs to start with a slash!!!
   * @return String representation of the URI to the system properties service.
   */
  // end::doc[]
  protected String buildUrl(String protocol, String host, int port, String path) {
    try {
      URI uri = new URI(protocol, null, host, port, path, null, null);
      return uri.toString();
    } catch (Exception e) {
      System.err.println("Exception thrown while building the URL: " + e.getMessage());
      return null;
    }
  }

  // Helper method that processes the request
  protected Properties getPropertiesHelper() {
	Properties properties = null;
    try {
      System.out.println(restClientService);
      properties = restClientService.getProperties();
      System.out.println(restClientService);
      
    } catch (RuntimeException e) {
      System.err.println("Runtime exception: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Exception thrown while invoking the request: " + e.getMessage());
    }
    return properties;
  }

}
