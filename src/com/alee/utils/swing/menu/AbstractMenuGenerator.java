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

package com.alee.utils.swing.menu;

import com.alee.laf.menu.WebCheckBoxMenuItem;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebRadioButtonMenuItem;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.utils.swing.UnselectableButtonGroup;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * This is a base generator class for custom menu generators.
 * Menu generators are made to simplify various Swing menues creation.
 *
 * @author Mikle Garin
 */

public abstract class AbstractMenuGenerator
{
    /**
     * Default menu item icon format.
     */
    protected static final String defaultIconFormat = ".png";

    /**
     * Class near which icon is placed.
     */
    protected Class nearClass;

    /**
     * Path to icons folder relative to class.
     */
    protected String path;

    /**
     * Menu item icon format.
     */
    protected String extension;

    /**
     * Menu item language key prefix.
     */
    protected String languagePrefix;

    /**
     * Buttons grouping.
     */
    protected UnselectableButtonGroup group;

    public Class getNearClass ()
    {
        return nearClass;
    }

    public void setNearClass ( final Class nearClass )
    {
        this.nearClass = nearClass;
    }

    public String getPath ()
    {
        return path;
    }

    public void setPath ( final String path )
    {
        this.path = path;
    }

    public String getExtension ()
    {
        return extension;
    }

    public void setExtension ( final String extension )
    {
        this.extension = extension.startsWith ( "." ) ? extension : "." + extension;
    }

    public void setIconSettings ( final Class nearClass, final String path, final String extension )
    {
        this.nearClass = nearClass;
        this.path = path;
        this.extension = extension;
    }

    public void setLanguagePrefix ( final String languagePrefix )
    {
        this.languagePrefix = languagePrefix;
    }

    public String getLanguagePrefix ()
    {
        return languagePrefix;
    }

    public ImageIcon getIcon ( final String icon )
    {
        if ( icon != null )
        {
            try
            {
                return new ImageIcon ( nearClass.getResource ( path + icon + extension ) );
            }
            catch ( final Throwable e )
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    public String getLanguageKey ( final String text )
    {
        return languagePrefix == null ? text : languagePrefix + "." + text;
    }

    public WebMenuItem createItem ( final String icon, final String text, final HotkeyData hotkey, final boolean enabled,
                                    final ActionListener actionListener )
    {
        final WebMenuItem item = new WebMenuItem ();
        item.setIcon ( getIcon ( icon ) );
        item.setLanguage ( getLanguageKey ( text ) );
        item.setAccelerator ( hotkey );
        item.setEnabled ( enabled );
        item.addActionListener ( actionListener );
        return item;
    }

    public WebCheckBoxMenuItem createCheckBoxItem ( final String icon, final String text, final HotkeyData hotkey, final boolean enabled,
                                                    final boolean selected, final ActionListener actionListener )
    {
        final WebCheckBoxMenuItem item = new WebCheckBoxMenuItem ();
        item.setIcon ( getIcon ( icon ) );
        item.setLanguage ( getLanguageKey ( text ) );
        item.setAccelerator ( hotkey );
        item.setEnabled ( enabled );
        item.setSelected ( selected );
        item.addActionListener ( actionListener );
        group ( item );
        return item;
    }

    public WebRadioButtonMenuItem createRadioButtonItem ( final String icon, final String text, final HotkeyData hotkey,
                                                          final boolean enabled, final boolean selected,
                                                          final ActionListener actionListener )
    {
        final WebRadioButtonMenuItem item = new WebRadioButtonMenuItem ();
        item.setIcon ( getIcon ( icon ) );
        item.setLanguage ( getLanguageKey ( text ) );
        item.setAccelerator ( hotkey );
        item.setEnabled ( enabled );
        item.setSelected ( selected );
        item.addActionListener ( actionListener );
        group ( item );
        return item;
    }

    public WebMenu createMenu ( final String icon, final String text, final boolean enabled, final ActionListener actionListener )
    {
        final WebMenu item = new WebMenu ();
        item.setIcon ( getIcon ( icon ) );
        item.setLanguage ( getLanguageKey ( text ) );
        item.setEnabled ( enabled );
        item.addActionListener ( actionListener );
        return item;
    }

    public UnselectableButtonGroup openGroup ()
    {
        return openGroup ( false );
    }

    public UnselectableButtonGroup openGroup ( final boolean unselectable )
    {
        return group = new UnselectableButtonGroup ( unselectable );
    }

    public UnselectableButtonGroup group ( final AbstractButton button )
    {
        if ( group != null )
        {
            group.add ( button );
        }
        return group;
    }

    public UnselectableButtonGroup closeGroup ()
    {
        final UnselectableButtonGroup group = this.group;
        this.group = null;
        return group;
    }
}