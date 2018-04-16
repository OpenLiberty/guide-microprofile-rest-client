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
// tag::manager[]
package io.openliberty.guides.inventory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import io.openliberty.guides.inventory.model.InventoryList;
import io.openliberty.guides.inventory.client.SystemClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.openliberty.guides.inventory.model.InventoryList;
import javax.enterprise.context.ApplicationScoped;
import io.openliberty.guides.inventory.client.UnknownUrlException;


// tag::ApplicationScoped[]
@ApplicationScoped
// end::ApplicationScoped[]
public class InventoryManager {

  private InventoryList invList = new InventoryList();

  @Inject
  @RestClient
  private SystemClient localRestClientService;


  public Properties get(String hostname) {

	  Properties properties = null;
	  // If the request matches the System Service running on the localhost, use the Injected localRestClientService
	  // For all other host names, use RestCLientBuilder to request properties from the remoteSystemService
	  if(hostname.equals("localhost")) {
      try {
        properties = localRestClientService.getProperties();
        // Response resp = localRestClientService.getProperties();
        // properties = .getEntity(Properties.class);
      } catch (UnknownUrlException e) {
        System.err.println("The given URL is unreachable.");
			  e.printStackTrace();
      }
	  }
	  else {
       String remoteURL = "http://" + hostname + ":9080/system";
		   URL apiURL = null;
		   try {
			  apiURL = new URL(remoteURL);
			  SystemClient remoteSystemService =
					  RestClientBuilder.newBuilder()
					  					.baseUrl(apiURL)
						                .build(SystemClient.class);

			  properties = remoteSystemService.getProperties();
        // properties = remoteSystemService.getProperties().getEntity(Properties.class);
		  } catch (UnknownUrlException e) {
        System.err.println("The given URL is unreachable.");
			  e.printStackTrace();
      } catch (MalformedURLException e) {
			  System.err.println("The given URL is not formatted correctly.");
			  e.printStackTrace();
		  };
	  }

	  if (properties != null) {
		  invList.addToInventoryList(hostname, properties);
	  }
	  return properties;

  }

  public InventoryList list() {
    return invList;
  }
}
// end::manager[]

//private void urlHelper() {
  // String url = null;
  // Map<String, String> configProps = null;
  // Config config = ConfigProvider.getConfig();
  // for(ConfigSource cs :config.getConfigSources()) {
  //     configProps = cs.getProperties();
  //     if(configProps.containsKey(SystemClient.class.getName() + "/mp-rest/url")) {
  //        url = configProps.remove(SystemClient.class.getName() + "/mp-rest/url");
  //        System.out.println("old url = " + url);
  //     }
  //
  //   }
  //  String remoteURL = url.replaceAll("localhost", hostname);
  //  configProps.put(SystemClient.class.getName() + "/mp-rest/url", url);
//}
