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
package io.openliberty.guides.inventory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import io.openliberty.guides.inventory.model.InventoryList;
import io.openliberty.guides.inventory.rest.client.SystemClient;
import io.openliberty.guides.inventory.rest.client.SystemResourceService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

// tag::ApplicationScoped[]
@ApplicationScoped
// end::ApplicationScoped[]
public class InventoryManager {
	
  // Application data need to build URL for remote hosts running System Service
  private String applicationPath = "/draft-guide-microprofile-rest-client/system";
  private int port = 9080;
  
  private InventoryList invList = new InventoryList();
  @Inject
  @RestClient
  private SystemResourceService localRestClientService;

  
  public Properties get(String hostname) {

	  Properties properties = null;
	  // If the request matches the System Service is running on the localhost use the Injected localRestClientService 
	  // For all other host names use RestCLientBuilder to request properties from the remoteSystemService
	  if(hostname.equals("localhost")) {
		    properties = localRestClientService.getProperties();
	  } 
	  else {
		  String url = null;
		  Map<String, String> configProps = null;
		  Config config = ConfigProvider.getConfig();
		  for(ConfigSource cs :config.getConfigSources()) {
		      configProps = cs.getProperties();
		      if(configProps.containsKey(SystemResourceService.class.getName() + "/mp-rest/url")) {
		         url = configProps.remove(SystemResourceService.class.getName() + "/mp-rest/url");
		         System.out.println("old url =" + url);
		      }
		                
		    }
		   String remoteURL = url.replaceAll("localhost", hostname);
		   configProps.put(SystemResourceService.class.getName() + "/mp-rest/url", url);
		    
		   URL apiURL = null;
		   try {
			  apiURL = new URL(remoteURL);
			  SystemResourceService remoteSystemService =
					  RestClientBuilder.newBuilder()
					  					.baseUrl(apiURL)
						                .build(SystemResourceService.class);
					
			  properties = remoteSystemService.getProperties();
		  } catch (MalformedURLException e) {
			  System.out.println("THe localHost url is invalid");
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
