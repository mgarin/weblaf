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

import com.alee.api.TitleSupport;
import com.alee.managers.language.LanguageManager;

import java.awt.*;

/**
 * Enumeration that represents feature development state.
 *
 * @author Mikle Garin
 */

public enum FeatureState implements TitleSupport
{
    /**
     * Beta feature.
     * This is an unstable feature available in current version for preview.
     */
    beta ( new Color ( 255, 100, 100 ) ),

    /**
     * Newly released feature.
     * This is a new stable feature available in current version.
     */
    release ( new Color ( 100, 190, 100 ) ),

    /**
     * Previously released feature.
     * This is a stable feature available in current version.
     */
    common ( Color.BLACK ),

    /**
     * Updated feature.
     * This is a stable feature that got updated in current version.
     */
    updated ( new Color ( 140, 140, 255 ) ),

    /**
     * Deprecated feature.
     * This feature will be removed or replaced in future updates.
     */
    deprecated ( new Color ( 180, 180, 180 ) );

    /**
     * Feature state color.
     */
    private final Color color;

    /**
     * Constructs new feature state.
     *
     * @param color feature state color
     */
    private FeatureState ( final Color color )
    {
        this.color = color;
    }

    /**
     * Returns feature state color.
     *
     * @return feature state color
     */
    public Color getColor ()
    {
        return color;
    }

    /**
     * Returns feature state color.
     *
     * @return feature state color
     */
    public Color getForeground ()
    {
        return color.darker ();
    }

    @Override
    public String getTitle ()
    {
        return LanguageManager.get ( "demo.state." + this );
    }

    /**
     * Returns feature state description.
     *
     * @return feature state description
     */
    public String geDescription ()
    {
        return LanguageManager.get ( "demo.state." + this + ".description" );
    }
}