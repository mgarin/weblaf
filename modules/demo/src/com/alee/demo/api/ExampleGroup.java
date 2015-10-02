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
import java.util.List;

/**
 * Interface representing group of feature examples or other groups.
 *
 * @author Mikle Garin
 */

public interface ExampleGroup extends ExampleElement
{
    /**
     * Returns example group icon.
     *
     * @return example group icon
     */
    public Icon getIcon ();

    /**
     * Returns example group title.
     *
     * @return example group title
     */
    public String getTitle ();

    /**
     * Returns example group features state.
     *
     * @return example group features state
     */
    public FeatureState getFeatureState ();

    /**
     * Returns list of example sub groups for this example group.
     *
     * @return list of example sub groups for this example group
     */
    public List<ExampleGroup> getGroups ();

    /**
     * Returns list of examples for this example group.
     *
     * @return list of examples for this example group
     */
    public List<Example> getExamples ();
}