/*******************************************************************************
 * Copyright (c) 2016, 2018 Oracle and/or its affiliates. All rights reserved.
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

import org.eclipse.yasson.internal.Marshaller;
import org.eclipse.yasson.internal.ReflectionUtils;
import org.eclipse.yasson.internal.model.ClassModel;
import org.eclipse.yasson.internal.model.PropertyModel;

import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Serializes arbitrary object by reading its properties.
 *
 * @author Roman Grigoriadi
 */
public class ObjectSerializer<T> extends AbstractContainerSerializer<T> {

    /**
     * Creates a new instance.
     *
     * @param builder Builder to initialize the instance.
     */
    public ObjectSerializer(SerializerBuilder builder) {
        super(builder);
    }

    /**
     * Creates a new instance.
     *
     * @param wrapper wrapped item
     * @param runtimeType class type
     * @param classModel model of the class
     */
    public ObjectSerializer(CurrentItem<?> wrapper, Type runtimeType, ClassModel classModel) {
        super(wrapper, runtimeType, classModel);
    }

    @Override
    protected void serializeInternal(T object, JsonGenerator generator, SerializationContext ctx) {
        final PropertyModel[] allProperties = ((Marshaller) ctx).getMappingContext().getOrCreateClassModel(object.getClass()).getSortedProperties();
        for (PropertyModel model : allProperties) {
            marshallProperty(object, generator, ctx, model);
        }
    }

    @Override
    protected void writeStart(JsonGenerator generator) {
        generator.writeStartObject();
    }

    @Override
    protected void writeStart(String key, JsonGenerator generator) {
        generator.writeStartObject(key);
    }

    @SuppressWarnings("unchecked")
    private void marshallProperty(T object, JsonGenerator generator, SerializationContext ctx, PropertyModel propertyModel) {
        Marshaller marshaller = (Marshaller) ctx;

        if (propertyModel.isReadable()) {
            final Object propertyValue = propertyModel.getValue(object);
            if (propertyValue == null || isEmptyOptional(propertyValue)) {
                if (propertyModel.getCustomization().isNillable()) {
                    generator.writeNull(propertyModel.getWriteName());
                }
                return;
            }

            generator.writeKey(propertyModel.getWriteName());

            final JsonbSerializer<?> propertyCachedSerializer = propertyModel.getPropertySerializer();
            if (propertyCachedSerializer != null) {
                serializerCaptor(propertyCachedSerializer, propertyValue, generator, ctx);
                return;
            }

            Optional<Type> runtimeTypeOptional = ReflectionUtils.resolveOptionalType(this, propertyModel.getPropertyType());
            Type genericType = runtimeTypeOptional.orElse(null);
            final JsonbSerializer<?> serializer = new SerializerBuilder(marshaller.getJsonbContext())
                    .withWrapper(this)
                    .withObjectClass(propertyValue.getClass())
                    .withCustomization(propertyModel.getCustomization())
                    .withType(genericType).build();
            serializerCaptor(serializer, propertyValue, generator, ctx);
        }
    }

    private boolean isEmptyOptional(Object object) {
        if (object instanceof Optional) {
            return !((Optional) object).isPresent();
        } else if (object instanceof OptionalInt) {
            return !((OptionalInt) object).isPresent();
        } else if (object instanceof OptionalLong) {
            return !((OptionalLong) object).isPresent();
        } else if (object instanceof OptionalDouble) {
            return !((OptionalDouble) object).isPresent();
        }
        return false;
    }

}
