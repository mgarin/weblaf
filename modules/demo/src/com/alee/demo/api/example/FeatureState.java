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

import com.alee.managers.language.LM;

/**
 * Enumeration that represents feature development state.
 *
 * @author Mikle Garin
 */
public enum FeatureState
{
    /**
     * Beta feature.
     * This is an unstable feature available in current version for preview.
     */
    beta,

    /**
     * Newly released feature.
     * This is a new stable feature available in current version.
     */
    release,

    /**
     * Previously released feature.
     * This is a stable feature available in current version.
     */
    common,

    /**
     * Updated feature.
     * This is a stable feature that got updated in current version.
     */
    updated,

    /**
     * Deprecated feature.
     * This feature will be removed or replaced in future updates.
     */
    deprecated;

    /**
     * Returns {@link FeatureState} title.
     *
     * @return {@link FeatureState} title
     */
    public String getTitle ()
    {
        return LM.get ( "demo.state." + this );
    }

    /**
     * Returns {@link FeatureState} description.
     *
     * @return {@link FeatureState} description
     */
    public String geDescription ()
    {
        return LM.get ( "demo.state." + this + ".description" );
    }
}