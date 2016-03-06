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

package com.alee.utils.swing;

import java.awt.*;

/**
 * This interface provides a set of methods that should be added into components that support custom size methods.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see SwingMethods
 * @see com.alee.utils.SizeUtils
 */

public interface SizeMethods<C extends Component> extends SwingMethods
{
    /**
     * Undefined size value constant.
     */
    public static final int UNDEFINED = -1;

    /**
     * Returns component preferred width.
     *
     * @return component preferred width
     */
    public int getPreferredWidth ();

    /**
     * Sets component preferred width.
     * Pass {@link #UNDEFINED} to let component choose preferred width on its own.
     *
     * @param preferredWidth new component preferred width
     * @return modified component
     */
    public C setPreferredWidth ( int preferredWidth );

    /**
     * Returns component preferred height.
     *
     * @return component preferred height
     */
    public int getPreferredHeight ();

    /**
     * Sets component preferred height.
     * Pass {@link #UNDEFINED} to let component choose preferred height on its own.
     *
     * @param preferredHeight new component preferred height
     * @return modified component
     */
    public C setPreferredHeight ( int preferredHeight );

    /**
     * Returns component minimum width.
     *
     * @return component minimum width
     */
    public int getMinimumWidth ();

    /**
     * Sets component minimum width.
     * Pass {@link #UNDEFINED} to let component choose minimum width on its own.
     *
     * @param minimumWidth new component minimum width
     * @return modified component
     */
    public C setMinimumWidth ( int minimumWidth );

    /**
     * Returns component minimum height.
     *
     * @return component minimum height
     */
    public int getMinimumHeight ();

    /**
     * Sets component minimum height.
     * Pass {@link #UNDEFINED} to let component choose minimum height on its own.
     *
     * @param minimumHeight new component minimum height
     * @return modified component
     */
    public C setMinimumHeight ( int minimumHeight );

    /**
     * Returns component maximum width.
     *
     * @return component maximum width
     */
    public int getMaximumWidth ();

    /**
     * Sets component maximum width.
     * Pass {@link #UNDEFINED} to let component choose maximum width on its own.
     *
     * @param maximumWidth new component maximum width
     * @return modified component
     */
    public C setMaximumWidth ( int maximumWidth );

    /**
     * Returns component maximum height.
     *
     * @return component maximum height
     */
    public int getMaximumHeight ();

    /**
     * Sets component maximum height.
     * Pass {@link #UNDEFINED} to let component choose maximum height on its own.
     *
     * @param maximumHeight new component maximum height
     * @return modified component
     */
    public C setMaximumHeight ( int maximumHeight );

    /**
     * Returns component preferred size.
     *
     * @return component preferred size
     */
    public Dimension getPreferredSize ();

    /**
     * Sets component preferred size.
     * This method is a simple bridge for JComponent#setPreferredSize method.
     *
     * @param width  component preferred width
     * @param height component preferred height
     * @return modified component
     */
    public C setPreferredSize ( int width, int height );
}