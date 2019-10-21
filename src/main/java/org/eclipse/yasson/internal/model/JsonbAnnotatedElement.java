/*******************************************************************************
 * Copyright (c) 2016, 2019 Oracle and/or its affiliates. All rights reserved.
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

import java.lang.reflect.AnnotatedElement;

/**
 * Annotation holder for fields, getters and setters.
 *
 * @param <T> annotated element
 */
public class JsonbAnnotatedElement<T extends AnnotatedElement> extends JsonbAnnotated {

    private final T element;

    /**
     * Creates a new instance.
     *
     * @param element Element.
     */
    public JsonbAnnotatedElement(T element) {
        super(element.getAnnotations());
        this.element = element;
    }

    /**
     * Gets element.
     *
     * @return Element.
     */
    public T getElement() {
        return element;
    }
}
