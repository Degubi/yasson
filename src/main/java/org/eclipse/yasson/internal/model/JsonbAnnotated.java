/*******************************************************************************
 * Copyright (c) 2016, 2017 Oracle and/or its affiliates. All rights reserved.
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

package org.eclipse.yasson.internal.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.json.bind.JsonbException;

import org.eclipse.yasson.internal.properties.MessageKeys;
import org.eclipse.yasson.internal.properties.Messages;

/**
 * Element wrapper containing merged annotation from class, superclasses and interfaces.
 */
public class JsonbAnnotated implements AnnotatedElement {

    private final Map<Class<? extends Annotation>, Annotation> annotations;

    /**
     * Creates a new instance.
     *
     * @param initialAnnotations Annotations to initialize from.
     */
    public JsonbAnnotated(Annotation[] initialAnnotations) {
        this.annotations = new HashMap<>();
        addInitialAnnotations(initialAnnotations);
    }

    private void addInitialAnnotations(Annotation[] initialAnnotations) {
        for (Annotation ann : initialAnnotations) {
            annotations.put(ann.annotationType(), ann);
        }
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return annotationClass.cast(annotations.get(annotationClass));
    }

    @Override
    public Annotation[] getAnnotations() {
        final Collection<Annotation> values = annotations.values();
        return values.toArray(new Annotation[values.size()]);
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        throw new UnsupportedOperationException("Jsonb elements don't track declared annotations");
    }

    /**
     * Adds annotation.
     *
     * @param annotation Annotation to add.
     */
    public void putAnnotation(Annotation annotation) {
        if (annotations.containsKey(annotation.annotationType())) {
            throw new JsonbException(Messages.getMessage(MessageKeys.INTERNAL_ERROR,
                                                         "Annotation already present: " + annotation));
        }
        annotations.put(annotation.annotationType(), annotation);
    }
}
