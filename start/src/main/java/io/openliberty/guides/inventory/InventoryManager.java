// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2017, 2022 IBM Corporation and others.
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

import java.util.Properties;
import jakarta.enterprise.context.ApplicationScoped;

import io.openliberty.guides.inventory.model.InventoryList;

@ApplicationScoped
public class InventoryManager {

  public Properties get(String hostname) {
	return null;
    }

  public void add(String hostname, Properties props) {
    }

  public InventoryList list() {
	  return null;
	}
}
