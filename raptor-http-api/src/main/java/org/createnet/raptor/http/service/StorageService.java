/*
 * Copyright 2016 Luca Capra <lcapra@create-net.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.createnet.raptor.http.service;


import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.createnet.raptor.auth.authentication.Authentication;
import org.createnet.raptor.models.objects.RaptorComponent;
import org.createnet.raptor.models.objects.ServiceObject;
import org.jvnet.hk2.annotations.Service;
import org.createnet.raptor.db.Storage;
import org.createnet.raptor.db.StorageProvider;
import org.createnet.raptor.http.configuration.StorageConfiguration;
import org.createnet.raptor.http.exception.ConfigurationException;
import org.createnet.raptor.models.objects.serializer.ServiceObjectView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Luca Capra <lcapra@create-net.org>
 */
@Service
public class StorageService {
  
  private final Logger logger = LoggerFactory.getLogger(StorageService.class);
  
  @Inject
  ConfigurationService configuration;
  
  @Inject
  AuthService auth;
  
  private Storage storage;

  private enum ConnectionId {
    objects, data, subscriptions, actuations
  }

  protected Storage getStorage() throws Storage.StorageException, ConfigurationException {

    if (storage == null) {
      logger.debug("Initializing storage instance");
      storage = new StorageProvider();
      StorageConfiguration conf = configuration.getStorage();
      storage.initialize(conf);
      storage.setup(false);
      storage.connect();
    }

    return storage;
  }

  protected Storage.Connection getObjectConnection() throws ConfigurationException, Storage.StorageException {
    return getStorage().getConnection(ConnectionId.objects.name());
  }

  public ServiceObject getObject(String id) throws Storage.StorageException, RaptorComponent.ParserException, ConfigurationException {
    String json = getObjectConnection().get(id);
    if (json == null) {
      return null;
    }
    ServiceObject obj = new ServiceObject();
    obj.parse(json);
    return obj;
  }

  public List<ServiceObject> listObjects(String userId) throws Storage.StorageException, RaptorComponent.ParserException, ConfigurationException {
    return new ArrayList();
  }

  public String saveObject(ServiceObject obj) throws ConfigurationException, Storage.StorageException, RaptorComponent.ParserException, RaptorComponent.ValidationException, Authentication.AutenticationException {
    
    obj.validate();
    
    obj.id = ServiceObject.generateUUID();
    obj.userId = auth.getUser().getUserId();
    
    String json = obj.toJSON(ServiceObjectView.Internal);
    getObjectConnection().set(obj.id, json, 0);
    return obj.id;
  }

  public void deleteObject(String id) throws ConfigurationException, Storage.StorageException {
    getObjectConnection().delete(id);
  }  
  
}
