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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.icon.set.IconSet;
import com.alee.managers.style.data.SkinInfo;
import com.alee.managers.style.data.SkinInfoConverter;
import com.alee.utils.XmlUtils;
import com.alee.utils.xml.Resource;
import com.alee.utils.xml.XStreamContext;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * Basic skin extension implementation.
 * Its data is stored in the same XML format used for skins.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 * @see SkinExtension
 */
public class XmlSkinExtension implements SkinExtension
{
    /**
     * Extension data XML location.
     */
    private final Resource location;

    /**
     * Extension meta data.
     * It will never contain actual extension styles or includes.
     */
    private final SkinInfo extensionInfo;

    /**
     * Constructs new skin extension.
     *
     * @param path extension XML file path
     */
    public XmlSkinExtension ( final String path )
    {
        this ( new Resource ( path ) );
    }

    /**
     * Constructs new skin extension.
     *
     * @param file extension XML file
     */
    public XmlSkinExtension ( final File file )
    {
        this ( new Resource ( file ) );
    }

    /**
     * Constructs new skin extension.
     *
     * @param nearClass class to find skin info XML near
     * @param path      extension XML path relative to the specified class
     */
    public XmlSkinExtension ( final Class nearClass, final String path )
    {
        this ( new Resource ( nearClass, path ) );
    }

    /**
     * Constructs new skin extension.
     *
     * @param location extension XML file location
     */
    public XmlSkinExtension ( final Resource location )
    {
        super ();
        this.location = location;
        this.extensionInfo = getMetaData ();
    }

    @Nullable
    @Override
    public String getId ()
    {
        return extensionInfo.getId ();
    }

    @Override
    public Icon getIcon ()
    {
        return extensionInfo.getIcon ();
    }

    @Override
    public String getTitle ()
    {
        return extensionInfo.getTitle ();
    }

    @Override
    public String getDescription ()
    {
        return extensionInfo.getDescription ();
    }

    @Override
    public String getAuthor ()
    {
        return extensionInfo.getAuthor ();
    }

    @Override
    public boolean isSupported ( final String skinId )
    {
        return extensionInfo.isSupported ( skinId );
    }

    @Override
    public List<IconSet> getIconSets ()
    {
        return extensionInfo.getIconSets ();
    }

    /**
     * Returns extension with only meta data read.
     * We cannot read extension outside of some skin context, but we need some extension data before loading it.
     *
     * @return extension with only meta data read
     */
    public SkinInfo getMetaData ()
    {
        return XmlUtils.fromXML ( location, new XStreamContext ( SkinInfoConverter.META_DATA_ONLY_KEY, true ) );
    }

    /**
     * Returns full extension data for the specified skin.
     * It might vary depending on the skin we are reading this extension for, so we do not store it.
     *
     * @param skinClass skin canonical class name
     * @return full extension data for the specified skin
     */
    public SkinInfo getData ( final String skinClass )
    {
        return XmlUtils.fromXML ( location, new XStreamContext ( SkinInfoConverter.SKIN_CLASS, skinClass ) );
    }
}