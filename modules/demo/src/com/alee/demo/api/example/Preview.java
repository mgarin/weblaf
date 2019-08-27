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
import java.util.List;

/**
 * Interface representing single preview within specific example.
 *
 * @author Mikle Garin
 */
public interface Preview extends Identifiable, PreviewControl
{
    /**
     * Returns example this preview belongs to.
     *
     * @return example this preview belongs to
     */
    @NotNull
    public Example getExample ();

    /**
     * Returns current development state feature displayed in this preview.
     *
     * @return current development state feature displayed in this preview
     */
    @NotNull
    public FeatureState getFeatureState ();

    /**
     * Returns preview title.
     *
     * @return preview title
     */
    @NotNull
    public String getTitle ();

    /**
     * Returns preview component.
     * This can be anything provided by the example.
     *
     * @param previews all previews within example
     * @param index    index of this preview
     * @return preview component
     */
    @NotNull
    public JComponent getPreview ( @NotNull List<Preview> previews, int index );

    /**
     * Returns preview part which width should be equalized with other previews within same example.
     *
     * @return preview part which width should be equalized with other previews within same example
     */
    @Nullable
    public JComponent getEqualizableWidthComponent ();
}