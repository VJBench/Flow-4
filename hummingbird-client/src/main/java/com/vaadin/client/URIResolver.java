/*
 * Copyright 2000-2016 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.client;

import com.vaadin.shared.VaadinUriResolver;

import elemental.client.Browser;

/**
 * Client side URL resolver for vaadin protocols.
 *
 * @author Vaadin Ltd
 * @since
 */
public class URIResolver extends VaadinUriResolver {
    private transient Registry registry;

    /**
     * Creates a new instance connected to the given registry.
     *
     * @param registry
     *            the global registry
     */
    public URIResolver(Registry registry) {
        this.registry = registry;
    }

    @Override
    protected String getContextRootUrl() {
        String root = registry.getApplicationConfiguration()
                .getContextRootUrl();
        assert root.endsWith("/");
        return root;
    }

    /**
     * Returns the current document location as relative to the base uri of the
     * document.
     *
     * @return the document current location as relative to the document base
     *         uri
     */
    public static String getCurrentLocationRelativeToBaseUri() {
        return getBaseRelativeUri(Browser.getDocument().getBaseURI(),
                Browser.getDocument().getLocation().getHref());
    }

    /**
     * Returns the given uri as relative to the given base uri.
     *
     * @param baseURI
     *            the base uri of the document
     * @param uri
     *            an absolute uri to transform
     * @return the uri as relative to the document base uri, or the given uri
     *         unmodified if it is for different context.
     */
    public static String getBaseRelativeUri(String baseURI, String uri) {
        if (uri.startsWith(baseURI)) {
            return uri.substring(baseURI.length());
        }
        return uri;
    }
}
