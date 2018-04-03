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

import java.util.*;

import java.util.Properties;
import io.openliberty.guides.inventory.client.SystemClient;
import io.openliberty.guides.inventory.model.InventoryList;
import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.openliberty.guides.service.MusicPlaylistService;

// tag::ApplicationScoped[]
@ApplicationScoped
// end::ApplicationScoped[]
public class InventoryManager {

  private InventoryList invList = new InventoryList();
  private SystemClient systemClient = new SystemClient();

   @Inject
   @RestClient
   public MusicPlaylistService playlistService;

  public Properties get(String hostname) {
    systemClient.init(hostname);

    String names = playlistService.getPlaylistNames();
    System.out.println("hahahaha" + names);


    Properties properties = systemClient.getProperties();
    if (properties != null) {
        invList.addToInventoryList(hostname, properties);
      }
    return properties;

  }

  public InventoryList list() {
    return invList;
  }
}
