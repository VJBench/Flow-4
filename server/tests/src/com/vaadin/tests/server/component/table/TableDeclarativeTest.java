/*
 * Copyright 2000-2014 Vaadin Ltd.
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
package com.vaadin.tests.server.component.table;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.Table.RowHeaderMode;
import com.vaadin.ui.Table.TableDragMode;

/**
 * Test declarative support for {@link Table}.
 * 
 * @since
 * @author Vaadin Ltd
 */
public class TableDeclarativeTest extends TableDeclarativeTestBase {

    @Test
    public void testBasicAttributes() {

        String design = "<v-table page-length=30 cache-rate=3 selectable editable sortable=false "
                + "drag-mode=row multi-select-mode=simple column-header-mode=id "
                + "column-reordering-allowed column-collapsing-allowed sort-ascending=false "
                + "row-header-mode=id sort-container-property-id=foo />";

        Table table = new Table();
        table.setPageLength(30);
        table.setCacheRate(3);
        table.setSelectable(true);
        table.setEditable(true);
        table.setSortEnabled(false);
        table.setDragMode(TableDragMode.ROW);
        table.setMultiSelectMode(MultiSelectMode.SIMPLE);
        table.setColumnHeaderMode(ColumnHeaderMode.ID);
        table.setRowHeaderMode(RowHeaderMode.ID);
        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(true);
        table.setSortAscending(false);
        table.setSortContainerPropertyId("foo");

        testRead(design, table);
        // testWrite(design, table);
    }

    @Test
    public void testColumns() {
        String design = "<v-table column-collapsing-allowed=true>" //
                + "  <table>" //
                + "    <colgroup>"
                + "      <col property-id='foo'>"
                + "      <col property-id='bar' center expand=1 collapsible=false>"
                + "      <col property-id='baz' right expand=2 collapsed=true>"
                + "    </colgroup>" //
                + "  </table>" //
                + "</v-table>";

        Table table = new Table();
        table.setColumnCollapsingAllowed(true);

        table.addContainerProperty("foo", String.class, null);
        table.setColumnAlignment("foo", Align.LEFT);
        table.setColumnExpandRatio("foo", 0);

        table.addContainerProperty("bar", String.class, null);
        table.setColumnAlignment("bar", Align.CENTER);
        table.setColumnExpandRatio("bar", 1);
        table.setColumnCollapsible("bar", false);

        table.addContainerProperty("baz", String.class, null);
        table.setColumnAlignment("baz", Align.RIGHT);
        table.setColumnExpandRatio("baz", 2);
        table.setColumnCollapsed("baz", true);

        testRead(design, table);
        // testWrite(design, table);
    }

    @Test
    public void testHeadersFooters() {
        String design = "<v-table>" //
                + "  <table>" //
                + "    <colgroup><col property-id=foo><col property-id=bar></colgroup>" //
                + "    <thead>" //
                + "      <tr><th icon='http://example.com/icon.png'>FOO<th>BAR" //
                + "    </thead>" //
                + "    <tfoot>" //
                + "      <tr><td>foo<td>bar" //
                + "    </tfoot>" //
                + "  </table>" //
                + "</v-table>";

        Table table = new Table();
        table.setFooterVisible(true);

        table.addContainerProperty("foo", String.class, null);
        table.setColumnHeader("foo", "FOO");
        table.setColumnIcon("foo", new ExternalResource(
                "http://example.com/icon.png"));
        table.setColumnFooter("foo", "foo");

        table.addContainerProperty("bar", String.class, null);
        table.setColumnHeader("bar", "BAR");
        table.setColumnFooter("bar", "bar");

        testRead(design, table);
        // testWrite(design, table);
    }

    @Test
    public void testInlineData() throws UnsupportedEncodingException {
        String design = "<v-table footer-visible=true> "//
                + "  <table>" //
                + "    <colgroup>"
                + "      <col property-id='foo' width=150 />"
                + "      <col property-id='bar' />"
                + "      <col property-id='baz' />" //
                + "    </colgroup>"
                + "    <thead>"
                + "      <tr><th>Description<th>Milestone<th>Status</tr>"
                + "    </thead>"
                + "    <tbody>"
                + "      <tr><td>r1c1</td><td>r1c2</td><td>r1c3</td>" //
                + "      <tr><td>r2c1</td><td>r2c2</td><td>r2c3</td>" //
                + "    </tbody>" //
                + "    <tfoot>" //
                + "      <tr><td>F1<td>F2<td>F3</tr>" //
                + "    </tfoot>" //
                + "  </table>" //
                + "</v-table>";
        Table table = new Table();
        table.addContainerProperty("foo", String.class, null);
        table.addContainerProperty("bar", String.class, null);
        table.addContainerProperty("baz", String.class, null);
        table.setColumnHeaders("Description", "Milestone", "Status");
        table.setColumnFooter("foo", "F1");
        table.setColumnFooter("bar", "F2");
        table.setColumnFooter("baz", "F3");
        table.addItem(new Object[] { "r1c1", "r1c2", "r1c3" }, null);
        table.addItem(new Object[] { "r2c1", "r2c2", "r2c3" }, null);
        table.setFooterVisible(true);
        testRead(design, table);
        // testWrite(design, table);

    }
}
