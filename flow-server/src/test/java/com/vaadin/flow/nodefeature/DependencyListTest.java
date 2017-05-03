/*
 * Copyright 2000-2017 Vaadin Ltd.
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
package com.vaadin.flow.nodefeature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.flow.util.JsonUtils;
import com.vaadin.tests.util.MockUI;
import com.vaadin.ui.Dependency;
import com.vaadin.ui.Dependency.Type;
import com.vaadin.ui.DependencyList;

import elemental.json.Json;
import elemental.json.JsonObject;

public class DependencyListTest {
    private static final String URL = "https://example.net/";

    private MockUI ui;
    private DependencyList deps;

    @Before
    public void before() {
        ui = new MockUI();
        deps = ui.getInternals().getDependencyList();

        assertEquals(0, deps.getPendingSendToClient().length());
    }

    @Test
    public void addStyleSheetDependency_blocking1() {
        ui.getPage().addStyleSheet(URL);
        validateDependency(URL, DependencyList.TYPE_STYLESHEET, true);
    }

    @Test
    public void addStyleSheetDependency_blocking2() {
        ui.getPage().addStyleSheet(URL, true);
        validateDependency(URL, DependencyList.TYPE_STYLESHEET, true);
    }

    @Test
    public void addStyleSheetDependency_nonBlocking() {
        ui.getPage().addStyleSheet(URL, false);
        validateDependency(URL, DependencyList.TYPE_STYLESHEET, false);
    }

    @Test
    public void addJavaScriptDependency_blocking1() {
        ui.getPage().addJavaScript(URL);
        validateDependency(URL, DependencyList.TYPE_JAVASCRIPT, true);
    }

    @Test
    public void addJavaScriptDependency_blocking2() {
        ui.getPage().addJavaScript(URL, true);
        validateDependency(URL, DependencyList.TYPE_JAVASCRIPT, true);
    }

    @Test
    public void addJavaScriptDependency_nonBlocking() {
        ui.getPage().addJavaScript(URL, false);
        validateDependency(URL, DependencyList.TYPE_JAVASCRIPT, false);
    }

    @Test
    public void addHtmlDependency_blocking1() {
        ui.getPage().addHtmlImport(URL);
        validateDependency(URL, DependencyList.TYPE_HTML_IMPORT, true);
    }

    @Test
    public void addHtmlDependency_blocking2() {
        ui.getPage().addHtmlImport(URL, true);
        validateDependency(URL, DependencyList.TYPE_HTML_IMPORT, true);
    }

    @Test
    public void addHtmlDependency_nonBlocking() {
        ui.getPage().addHtmlImport(URL, false);
        validateDependency(URL, DependencyList.TYPE_HTML_IMPORT, false);
    }

    private void validateDependency(String url, String dependencyType,
            boolean blocking) {
        JsonObject expectedJson = Json.createObject();
        expectedJson.put(DependencyList.KEY_URL, url);
        expectedJson.put(DependencyList.KEY_TYPE, dependencyType);
        expectedJson.put(DependencyList.KEY_BLOCKING, blocking);

        assertEquals("Expected to receive exactly one dependency", 1,
                deps.getPendingSendToClient().length());
        assertTrue(
                String.format(
                        "Dependencies' json representations are different, expected = \n'%s'\n, actual = \n'%s'",
                        expectedJson.toJson(),
                        deps.getPendingSendToClient().get(0).toJson()),
                JsonUtils.jsonEquals(expectedJson,
                        deps.getPendingSendToClient().get(0)));
    }

    @Test
    public void specialUrls() {
        assertUrlUnchanged("/foo?bar");
        assertUrlUnchanged("/foo/baz?bar=http://some.thing");
        assertUrlUnchanged("/foo/baz?bar=http://some.thing&ftp://bar");
        assertUrlUnchanged("http://foo?bar");
        assertUrlUnchanged("http://foo/baz");
        assertUrlUnchanged("http://foo/baz?bar");
        assertUrlUnchanged("http://foo/baz?bar=http://some.thing");
        assertUrlUnchanged("ftp://some.host/some/where");
        assertUrlUnchanged("https://some.host/some/where");
        assertUrlUnchanged("//same.protocol.some.host/some/where");
        assertUrlUnchanged("foo?bar");
        assertUrlUnchanged("foo?bar=http://yah");
        assertUrlUnchanged("foo/baz?bar=http://some.thing");
        assertUrlUnchanged("foo/baz?bar=http://some.thing&ftp://bar");
    }

    private void assertUrlUnchanged(String url) {
        deps.add(new Dependency(Type.JAVASCRIPT, url, true));
        assertEquals(url, ((JsonObject) deps.getPendingSendToClient().get(0))
                .getString(DependencyList.KEY_URL));
        deps.clearPendingSendToClient();
    }

    @Test
    public void urlAddedOnlyOnce() {
        deps.add(new Dependency(Type.JAVASCRIPT, "foo/bar.js", true));
        deps.add(new Dependency(Type.JAVASCRIPT, "foo/bar.js", true));
        assertEquals(1, deps.getPendingSendToClient().length());
        deps.clearPendingSendToClient();

        deps.add(new Dependency(Type.JAVASCRIPT, "foo/bar.js", true));
        assertEquals(0, deps.getPendingSendToClient().length());
    }

    @Test
    public void addDependencyPerformance() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            deps.add(new Dependency(Type.JAVASCRIPT, "foo" + i + "/bar.js",
                    true));
        }
        long time = System.currentTimeMillis() - start;

        assertTrue("Adding 10K dependencies should take about 50ms. Took "
                + time + "ms", time < 500);
    }
}