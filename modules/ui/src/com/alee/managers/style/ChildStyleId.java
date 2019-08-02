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

package com.alee.managers.style;

import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Component style ID container that can be used for convenient {@link StyleId} construction.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 */
public final class ChildStyleId
{
    /**
     * Style ID.
     * Identifies some specific component style.
     */
    private final String id;

    /**
     * Constructs new child style ID container.
     *
     * @param id style ID
     */
    private ChildStyleId ( final String id )
    {
        super ();
        this.id = id;
    }

    /**
     * Returns child style ID.
     *
     * @return child style ID
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Returns completed style ID for the child style.
     *
     * @param parent parent component
     * @return completed style ID for the child style
     */
    public StyleId at ( final JComponent parent )
    {
        return StyleId.of ( getId (), parent );
    }

    /**
     * Returns completed style ID for the child style.
     *
     * @param parent parent component
     * @return completed style ID for the child style
     */
    public StyleId at ( final Window parent )
    {
        return at ( CoreSwingUtils.getRootPane ( parent ) );
    }

    /**
     * Returns new child style ID container.
     *
     * @param id style ID
     * @return new child style ID container
     */
    public static ChildStyleId of ( final String id )
    {
        return new ChildStyleId ( id );
    }
}