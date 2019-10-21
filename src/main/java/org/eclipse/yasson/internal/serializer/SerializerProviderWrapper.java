/*******************************************************************************
 * Copyright (c) 2015, 2017 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Roman Grigoriadi
 ******************************************************************************/
package org.eclipse.yasson.internal.serializer;

/**
 * Wraps serializer and deserializer providers.
 */
public class SerializerProviderWrapper {

    private ISerializerProvider serializerProvider;
    private IDeserializerProvider deserializerProvider;

    /**
     * Creates a new instance.
     *
     * @param serializerProvider   Serializer provider.
     * @param deserializerProvider Deserializer provider.
     */
    public SerializerProviderWrapper(ISerializerProvider serializerProvider, IDeserializerProvider deserializerProvider) {
        this.serializerProvider = serializerProvider;
        this.deserializerProvider = deserializerProvider;
    }

    /**
     * Gets serializer provider.
     *
     * @return Serializer provider.
     */
    public ISerializerProvider getSerializerProvider() {
        return serializerProvider;
    }

    /**
     * Gets deserializer provider.
     *
     * @return Deserializer provider.
     */
    public IDeserializerProvider getDeserializerProvider() {
        return deserializerProvider;
    }
}
