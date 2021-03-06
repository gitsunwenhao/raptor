/*
 * Copyright 2016 CREATE-NET
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
package org.createnet.raptor.models.objects.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.Iterator;
import org.createnet.raptor.models.objects.Action;
import org.createnet.raptor.models.objects.ServiceObject;
import org.createnet.raptor.models.objects.Stream;

/**
 *
 * @author Luca Capra <luca.capra@gmail.com>
 */
public class ServiceObjectDeserializer extends JsonDeserializer<ServiceObject> {

    protected String getText(String fieldName, JsonNode tree) {
        if (tree.has(fieldName) && !tree.get(fieldName).isNull()) {
            return tree.get(fieldName).asText();
        }
        return null;
    }

    @Override
    public ServiceObject deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        ServiceObject serviceObject = new ServiceObject();

        JsonNode tree = jp.getCodec().readTree(jp);

        if (tree.has("id")) {
            serviceObject.id = getText("id", tree);
        }

        if (tree.has("userId")) {
            serviceObject.userId = getText("userId", tree);
        }

        if (tree.has("name")) {
            serviceObject.name = getText("name", tree);
        }

        if (tree.has("description")) {
            serviceObject.description = getText("description", tree);
        }

        if (tree.has("parentId")) {
            serviceObject.parentId = getText("parentId", tree);
        }

        if (tree.has("path")) {
            serviceObject.path = getText("path", tree);
        }

        if (tree.has("streams")) {

            if (tree.get("streams").isObject()) {

                Iterator<String> fieldNames = tree.get("streams").fieldNames();
                while (fieldNames.hasNext()) {

                    String name = fieldNames.next();
                    JsonNode jsonStream = tree.get("streams").get(name);

                    Stream stream = new Stream(name, jsonStream, serviceObject);
                    serviceObject.streams.put(stream.name, stream);
                }
            }

            if (tree.get("streams").isArray()) {
                for (JsonNode jsonStream : tree.get("streams")) {
                    Stream stream = new Stream(jsonStream);
                    serviceObject.streams.put(stream.name, stream);
                }
            }

        }

        if (tree.has("actions")) {

            if (tree.get("actions").isArray()) {
                for (JsonNode json : tree.get("actions")) {
                    Action actuation = new Action(json, serviceObject);
                    serviceObject.actions.put(actuation.name, actuation);
                }
            }

            if (tree.get("actions").isObject()) {
                Iterator<String> fieldNames = tree.get("actions").fieldNames();
                while (fieldNames.hasNext()) {

                    String name = fieldNames.next();
                    JsonNode json = tree.get("actions").get(name);

                    Action actuation = new Action(name, json, serviceObject);
                    serviceObject.actions.put(actuation.name, actuation);
                }
            }

        }

        return serviceObject;
    }
}
