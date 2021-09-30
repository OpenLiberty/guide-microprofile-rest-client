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
// tag::manager[]
package io.openliberty.guides.inventory;

import java.net.ConnectException;
import java.net.URI;
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
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.QueryParamStyle;

import io.openliberty.guides.inventory.client.SystemClient;
import io.openliberty.guides.inventory.client.UnknownUriException;
import io.openliberty.guides.inventory.client.UnknownUriExceptionMapper;
import io.openliberty.guides.inventory.model.InventoryList;
import io.openliberty.guides.inventory.model.SystemData;

// tag::ApplicationScoped[]
@ApplicationScoped
// end::ApplicationScoped[]
public class InventoryManager {

  private List<SystemData> systems = Collections.synchronizedList(
                                       new ArrayList<SystemData>());

  @Inject
  @ConfigProperty(name = "default.http.port")
  String DEFAULT_PORT;

  // tag::Inject[]
  @Inject
  // end::Inject[]
  // tag::RestClient[]
  @RestClient
  // end::RestClient[]
  // tag::SystemClient[]
  private SystemClient defaultRestClient;
  // end::SystemClient[]

  public Properties get(String hostname, List<String> listproperties) {
    Properties properties = null;
    if (hostname.equals("localhost")) {
      properties = getPropertiesWithDefaultHostName(listproperties);
    } else {
      properties = getPropertiesWithGivenHostName(hostname, listproperties);
    }

    return properties;
  }

  public void add(String hostname,  List<String> listproperties, Properties systemProps) {
    Properties props = new Properties();

    if (listproperties.size() == 0) {
      props.setProperty("os.name", systemProps.getProperty("os.name"));
      props.setProperty("user.name", systemProps.getProperty("user.name"));
    }

    SystemData host = new SystemData(hostname, props);
      if (!systems.contains(host)) {
        systems.add(host);
      }
  }

  public InventoryList list() {
    return new InventoryList(systems);
  }

  // tag::getPropertiesWithDefaultHostName[]
  private Properties getPropertiesWithDefaultHostName(List<String> listproperties) {
    try {
      if (listproperties.size() == 0) {
        // tag::defaultRCGetProperties[]
        return defaultRestClient.getProperties();
        // end::defaultRCGetProperties[]
      } else {
        // tag::defaultRCGetListProperties[]
        return defaultRestClient.getListProperties(listproperties);
        // end::defaultRCGetListProperties[]
      }
    } catch (UnknownUriException e) {
      System.err.println("The given URI is not formatted correctly.");
    } catch (ProcessingException ex) {
      handleProcessingException(ex);
    }
    return null;
  }
  // end::getPropertiesWithDefaultHostName[]

  // tag::getPropertiesWithGivenHostName[]
  private Properties getPropertiesWithGivenHostName(String hostname, List<String> listproperties) {
    String customURIString = "http://" + hostname + ":" + DEFAULT_PORT + "/system";
    URI customURI = null;
    try {
      customURI = URI.create(customURIString);
      // tag::customRestClientBuilder[]
      SystemClient customRestClient = RestClientBuilder.newBuilder()
                                        .baseUri(customURI)
                                        .register(UnknownUriExceptionMapper.class)
                                        // tag::followRedirects[]
                                        .followRedirects(true)
                                        // end::followRedirects[]
                                        // tag::queryParam[]
                                        .queryParamStyle(QueryParamStyle.COMMA_SEPARATED)
                                        // end::queryParam[]
                                        .build(SystemClient.class);
      // end::customRestClientBuilder[]
      if (listproperties.size() == 0) {
        // tag::customRCGetProperties[]
        return customRestClient.getProperties();
        // end::customRCGetProperties[]
      } else {
        // tag::customRCGetListProperties[]
        return customRestClient.getListProperties(listproperties);
        // end::customRCGetListProperties[]
      }
    } catch (ProcessingException ex) {
      handleProcessingException(ex);
    } catch (UnknownUriException e) {
      System.err.println("The given URI is unreachable.");
    }
    return null;
  }
  // end::getPropertiesWithGivenHostName[]

  private void handleProcessingException(ProcessingException ex) {
    Throwable rootEx = ExceptionUtils.getRootCause(ex);
    if (rootEx != null && (rootEx instanceof UnknownHostException
        || rootEx instanceof ConnectException)) {
      System.err.println("The specified host is unknown.");
    } else {
      throw ex;
    }
  }

}
// end::manager[]
