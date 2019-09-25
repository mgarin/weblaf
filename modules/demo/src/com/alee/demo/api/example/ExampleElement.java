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

import com.alee.api.Identifiable;
import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

import javax.swing.*;

/**
 * @author Mikle Garin
 */
public interface ExampleElement extends Identifiable
{
    @NotNull
    @Override
    public String getId ();

    /**
     * Returns parent group ID.
     *
     * @return parent group ID
     */
    @Nullable
    public String getGroupId ();

    /**
     * Returns example element icon.
     *
     * @return example element icon
     */
    @NotNull
    public Icon getIcon ();

    /**
     * Returns example element title.
     *
     * @return example title
     */
    @NotNull
    public String getTitle ();

    /**
     * Returns example element features state.
     *
     * @return example element features state
     */
    @NotNull
    public FeatureState getFeatureState ();
}