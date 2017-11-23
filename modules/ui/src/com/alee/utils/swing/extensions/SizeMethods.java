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

package com.alee.utils.swing.extensions;

import javax.swing.*;
import java.awt.*;

/**
 * This interface provides a set of methods that should be added into any {@link JComponent} that supports customizable sizes.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see MethodExtension
 * @see com.alee.utils.swing.extensions.SizeMethodsImpl
 */

public interface SizeMethods<C extends JComponent> extends MethodExtension
{
    /**
     * Undefined size value constant.
     */
    public static final int UNDEFINED = -1;

    /**
     * Returns {@link JComponent} preferred width.
     *
     * @return {@link JComponent} preferred width
     */
    public int getPreferredWidth ();

    /**
     * Sets {@link JComponent} preferred width.
     * Pass {@link #UNDEFINED} to let {@link JComponent} choose preferred width on its own.
     *
     * @param preferredWidth new {@link JComponent} preferred width
     * @return modified {@link JComponent}
     */
    public C setPreferredWidth ( int preferredWidth );

    /**
     * Returns {@link JComponent} preferred height.
     *
     * @return {@link JComponent} preferred height
     */
    public int getPreferredHeight ();

    /**
     * Sets {@link JComponent} preferred height.
     * Pass {@link #UNDEFINED} to let {@link JComponent} choose preferred height on its own.
     *
     * @param preferredHeight new {@link JComponent} preferred height
     * @return modified {@link JComponent}
     */
    public C setPreferredHeight ( int preferredHeight );

    /**
     * Returns {@link JComponent} minimum width.
     *
     * @return {@link JComponent} minimum width
     */
    public int getMinimumWidth ();

    /**
     * Sets {@link JComponent} minimum width.
     * Pass {@link #UNDEFINED} to let {@link JComponent} choose minimum width on its own.
     *
     * @param minimumWidth new {@link JComponent} minimum width
     * @return modified {@link JComponent}
     */
    public C setMinimumWidth ( int minimumWidth );

    /**
     * Returns {@link JComponent} minimum height.
     *
     * @return {@link JComponent} minimum height
     */
    public int getMinimumHeight ();

    /**
     * Sets {@link JComponent} minimum height.
     * Pass {@link #UNDEFINED} to let {@link JComponent} choose minimum height on its own.
     *
     * @param minimumHeight new {@link JComponent} minimum height
     * @return modified {@link JComponent}
     */
    public C setMinimumHeight ( int minimumHeight );

    /**
     * Returns {@link JComponent} maximum width.
     *
     * @return {@link JComponent} maximum width
     */
    public int getMaximumWidth ();

    /**
     * Sets {@link JComponent} maximum width.
     * Pass {@link #UNDEFINED} to let {@link JComponent} choose maximum width on its own.
     *
     * @param maximumWidth new {@link JComponent} maximum width
     * @return modified {@link JComponent}
     */
    public C setMaximumWidth ( int maximumWidth );

    /**
     * Returns {@link JComponent} maximum height.
     *
     * @return {@link JComponent} maximum height
     */
    public int getMaximumHeight ();

    /**
     * Sets {@link JComponent} maximum height.
     * Pass {@link #UNDEFINED} to let {@link JComponent} choose maximum height on its own.
     *
     * @param maximumHeight new {@link JComponent} maximum height
     * @return modified {@link JComponent}
     */
    public C setMaximumHeight ( int maximumHeight );

    /**
     * Returns {@link JComponent} preferred size.
     * This size is already adjusted according to min/max width and height settings.
     * Use {@link #getOriginalPreferredSize()} method to retrieve original {@link JComponent} preferred size.
     *
     * @return {@link JComponent} preferred size
     */
    public Dimension getPreferredSize ();

    /**
     * Returns original {@link JComponent} preferred size before any adjustments.
     * This might be useful for various calculations involving {@link JComponent} size.
     *
     * @return original {@link JComponent} preferred size before any adjustments
     */
    public Dimension getOriginalPreferredSize ();

    /**
     * Sets {@link JComponent} preferred size.
     * This method is a simple bridge for JComponent#setPreferredSize method.
     *
     * @param width  {@link JComponent} preferred width
     * @param height {@link JComponent} preferred height
     * @return modified {@link JComponent}
     */
    public C setPreferredSize ( int width, int height );
}