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
import java.util.Properties;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.openliberty.guides.inventory.model.InventoryList;
import io.openliberty.guides.inventory.client.SystemClient;
import io.openliberty.guides.inventory.client.UnknownUrlException;

@ApplicationScoped
public class InventoryManager {

	private InventoryList invList = new InventoryList();

	@Inject
	@RestClient
	private SystemClient defaultRestClient;

	public Properties get(String hostname) {

		Properties properties = null;
		if (hostname.equals("localhost")) {
			properties = getPropertiesWithDefaultHostName();
		} else {
			properties = getPropertiesWithGivenHostName(hostname);
		}

		if (properties != null) {
			invList.addToInventoryList(hostname, properties);
		}
		return properties;
	}

	public InventoryList list() {
		return invList;
	}

	private Properties getPropertiesWithDefaultHostName() {
		try {
			return defaultRestClient.getProperties();
		} catch (UnknownUrlException e) {
			System.err.println("The given URL is unreachable.");
			e.printStackTrace();
			return null;
		}
	}
  // tag::builder[]
	private Properties getPropertiesWithGivenHostName(String hostname) {
		String customURLString = "http://" + hostname + ":9080/system";
		URL customURL = null;
		try {
			customURL = new URL(customURLString);
			SystemClient customRestClient = RestClientBuilder.newBuilder().baseUrl(customURL).build(SystemClient.class);
			return customRestClient.getProperties();

		} catch (UnknownUrlException e) {
			System.err.println("The given URL is unreachable.");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.err.println("The given URL is not formatted correctly.");
			e.printStackTrace();
		}
		return null;
	}
  // end::builder[]
}

// end::manager[]

// If the request matches the System Service running on the localhost, use the
// Injected localRestClientService
// For all other host names, use RestCLientBuilder to request properties from
// the remoteSystemService
// private void urlHelper() {
// String url = null;
// Map<String, String> configProps = null;
// Config config = ConfigProvider.getConfig();
// for(ConfigSource cs :config.getConfigSources()) {
// configProps = cs.getProperties();
// if(configProps.containsKey(SystemClient.class.getName() + "/mp-rest/url")) {
// url = configProps.remove(SystemClient.class.getName() + "/mp-rest/url");
// System.out.println("old url = " + url);
// }
//
// }
// String remoteURL = url.replaceAll("localhost", hostname);
// configProps.put(SystemClient.class.getName() + "/mp-rest/url", url);

// Response resp = localRestClientService.getProperties();
// properties = .getEntity(Properties.class);
// properties = remoteSystemService.getProperties().getEntity(Properties.class);
// }
