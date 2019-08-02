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

import com.alee.utils.CollectionUtils;
import com.alee.utils.SystemUtils;

import javax.swing.*;
import java.util.List;

/**
 * This abstract class represents single WebLaF skin.
 * Each skin combines a group of component painters and settings to provide an unique visual style.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 * @see Skin
 */
public abstract class AbstractSkin implements Skin
{
    /**
     * Constant provided in the skin that supports any kind of systems.
     */
    public static final String ALL_SYSTEMS_SUPPORTED = "all";

    /**
     * Returns list of supported OS short names.
     *
     * @return list of supported OS short names
     * @see com.alee.utils.SystemUtils#getShortOsName()
     */
    public abstract List<String> getSupportedSystems ();

    @Override
    public boolean isSupported ()
    {
        final List<String> systems = getSupportedSystems ();
        if ( CollectionUtils.isEmpty ( systems ) )
        {
            throw new StyleException ( "Skin must support at least one system" );
        }
        return systems.contains ( ALL_SYSTEMS_SUPPORTED ) || systems.contains ( SystemUtils.getShortOsName () );
    }

    @Override
    public boolean applySkin ( final JComponent component )
    {
        if ( getStyle ( component ).apply ( component ) )
        {
            component.repaint ();
            return true;
        }
        return false;
    }

    @Override
    public void updateSkin ( final JComponent component )
    {
        // todo Provide optimized update sequence for cases when painter is not changed?
        removeSkin ( component );
        applySkin ( component );
    }

    @Override
    public boolean removeSkin ( final JComponent component )
    {
        return getStyle ( component ).remove ( component );
    }

    @Override
    public String toString ()
    {
        return getTitle ();
    }
}