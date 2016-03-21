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

import com.alee.managers.style.data.ComponentStyle;
import com.alee.managers.style.data.SkinInfo;
import com.alee.utils.XmlUtils;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * This skin is used by StyleManager for cases when skin settings are stored within XML.
 * To use it simply specify XML location relative to your skin class or provide SkinInfo object.
 * All settings and painters will be loaded and applied by this skin automatically when it is used.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see AbstractSkin
 * @see com.alee.managers.style.data.SkinInfo
 */

public class CustomSkin extends AbstractSkin
{
    /**
     * Theme information.
     * Contains complete information about this skin.
     */
    protected SkinInfo skinInfo;

    /**
     * Constructs new custom skin.
     *
     * @param location skin info XML location relative to this class
     */
    public CustomSkin ( final String location )
    {
        super ();
        this.skinInfo = XmlUtils.fromXML ( this.getClass ().getResource ( location ) );
    }

    /**
     * Constructs new custom skin.
     *
     * @param nearClass class to find skin info XML near
     * @param location  skin info XML location relative to the specified class
     */
    public CustomSkin ( final Class nearClass, final String location )
    {
        super ();
        this.skinInfo = XmlUtils.fromXML ( nearClass.getResource ( location ) );
    }

    /**
     * Constructs new custom skin.
     *
     * @param location skin info XML file location
     */
    public CustomSkin ( final File location )
    {
        super ();
        this.skinInfo = XmlUtils.fromXML ( location );
    }

    /**
     * Constructs new custom skin.
     *
     * @param skinInfo skin information
     */
    public CustomSkin ( final SkinInfo skinInfo )
    {
        super ();
        this.skinInfo = skinInfo;
    }

    /**
     * Returns skin information.
     *
     * @return skin information
     */
    public SkinInfo getSkinInfo ()
    {
        return skinInfo;
    }

    @Override
    public String getId ()
    {
        return skinInfo.getId ();
    }

    @Override
    public Icon getIcon ()
    {
        return skinInfo.getIcon ();
    }

    @Override
    public String getTitle ()
    {
        return skinInfo.getTitle ();
    }

    @Override
    public String getDescription ()
    {
        return skinInfo.getDescription ();
    }

    @Override
    public String getAuthor ()
    {
        return skinInfo.getAuthor ();
    }

    @Override
    public List<String> getSupportedSystems ()
    {
        return skinInfo.getSupportedSystemsList ();
    }

    @Override
    public String getSkinClass ()
    {
        return skinInfo.getSkinClass ();
    }

    @Override
    public ComponentStyle getComponentStyle ( final JComponent component )
    {
        final StyleableComponent type = StyleableComponent.get ( component );
        return skinInfo.getStyle ( component, type );
    }
}