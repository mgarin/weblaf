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

import com.alee.managers.style.StyleId;

/**
 * Each {@link AbstractStylePreview} is tied to specific skin style.
 *
 * @author Mikle Garin
 */
public abstract class AbstractStylePreview extends AbstractTitledPreview
{
    /**
     * Preview style ID.
     */
    protected final StyleId styleId;

    /**
     * Constructs new {@link AbstractStylePreview}.
     *
     * @param example example this preview belongs to
     * @param id      preview ID
     * @param state   feature state
     * @param styleId preview style ID
     */
    public AbstractStylePreview ( final Example example, final String id, final FeatureState state, final StyleId styleId )
    {
        super ( example, id, state );
        this.styleId = styleId;
    }

    /**
     * Returns preview style ID.
     *
     * @return preview style ID
     */
    protected StyleId getStyleId ()
    {
        return styleId;
    }
}