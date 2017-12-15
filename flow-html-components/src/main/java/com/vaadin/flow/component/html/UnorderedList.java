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
package com.vaadin.flow.component.html;

import com.vaadin.ui.common.HtmlContainer;
import com.vaadin.ui.event.ClickNotifier;
import com.vaadin.ui.Tag;

/**
 * Component representing a <code>&lt;ul&gt;</code> element.
 *
 * @author Vaadin Ltd
 */
@Tag(Tag.UL)
public class UnorderedList extends HtmlContainer implements ClickNotifier {

    /**
     * Creates a new empty unordered list.
     */
    public UnorderedList() {
        super();
    }

    /**
     * Creates a new unordered list with the given list items.
     *
     * @param items
     *            the list items
     */
    public UnorderedList(ListItem... items) {
        super(items);
    }
}