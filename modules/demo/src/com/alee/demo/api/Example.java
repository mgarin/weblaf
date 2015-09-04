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

package com.alee.demo.api;

import javax.swing.*;

/**
 * Interface representing single feature example.
 *
 * @author Mikle Garin
 */

public interface Example extends ExampleElement
{
    /**
     * Returns example icon.
     *
     * @return example icon
     */
    public Icon getIcon ();

    /**
     * Returns example title.
     * It might be used in examples tree or list.
     *
     * @return example title
     */
    public String getTitle ();

    /**
     * Returns short example description.
     * It might be used in some additional information fields.
     *
     * @return short example description
     */
    public String getDescription ();

    /**
     * Returns current development state of the feature displayed in this example.
     *
     * @return current development state of the feature displayed in this example
     */
    public FeatureState getFeatureState ();

    /**
     * Returns style code for this example.
     *
     * @return style code for this example
     */
    public String getStyleCode ();

    /**
     * Returns source code for this example.
     *
     * @return source code for this example
     */
    public String getSourceCode ();

    /**
     * Returns example content component.
     *
     * @return example content component
     */
    public JComponent createContent ();
}