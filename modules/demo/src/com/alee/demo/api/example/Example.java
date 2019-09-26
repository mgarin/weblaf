/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.demo.api.example;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.managers.style.Skin;

import javax.swing.*;

/**
 * Interface representing single feature example.
 *
 * @author Mikle Garin
 */
public interface Example extends ExampleElement
{
    /**
     * Returns type of the features displayed in this example.
     *
     * @return type of the features displayed in this example
     */
    @NotNull
    public FeatureType getFeatureType ();

    /**
     * Returns wiki page referenced in this example.
     * Simply return {@code null} in case there is no wiki article related.
     * todo Return {@link java.util.List} of {@link WikiPage}s instead to allow multiple articles
     *
     * @return wiki page referenced in this example
     */
    @NotNull
    public WikiPage getWikiPage ();

    /**
     * Returns source code for this example.
     *
     * @return source code for this example
     */
    @NotNull
    public String getSourceCode ();

    /**
     * Returns style code for this example.
     *
     * @param skin skin to retrieve style code for
     * @return style code for this example
     */
    @NotNull
    public String getStyleCode ( @NotNull Skin skin );

    /**
     * Returns example content component.
     *
     * @return example content component
     */
    @NotNull
    public JComponent createContent ();
}