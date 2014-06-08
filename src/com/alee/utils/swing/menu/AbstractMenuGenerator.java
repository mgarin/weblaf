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
import com.alee.managers.language.LM;
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
     * Default menu icons format.
     */
    protected static final String defaultIconFormat = ".png";

    /**
     * Class near which menu icons are placed.
     */
    protected Class nearClass;

    /**
     * Path to menu icons folder relative to class.
     */
    protected String path;

    /**
     * Menu icons format.
     */
    protected String extension;

    /**
     * Menu language key prefix.
     */
    protected String languagePrefix;

    /**
     * Buttons grouping.
     */
    protected UnselectableButtonGroup group;

    /**
     * Returns class near which menu icons are placed.
     *
     * @return class near which menu icons are placed
     */
    public Class getNearClass ()
    {
        return nearClass;
    }

    /**
     * Sets class near which menu icons are placed.
     *
     * @param nearClass class near which menu icons are placed
     */
    public void setNearClass ( final Class nearClass )
    {
        this.nearClass = nearClass;
    }

    /**
     * Returns path to menu icons folder relative to class.
     *
     * @return path to menu icons folder relative to class
     */
    public String getPath ()
    {
        return path;
    }

    /**
     * Sets path to menu icons folder relative to class.
     *
     * @param path path to menu icons folder relative to class
     */
    public void setPath ( final String path )
    {
        this.path = path;
    }

    /**
     * Returns menu icons format.
     *
     * @return menu icons format
     */
    public String getExtension ()
    {
        return extension;
    }

    /**
     * Sets menu icons format.
     *
     * @param extension menu icons format
     */
    public void setExtension ( final String extension )
    {
        this.extension = extension.startsWith ( "." ) ? extension : "." + extension;
    }

    /**
     * Sets menu icons location and format.
     *
     * @param nearClass class near which menu icons are placed
     * @param path      path to menu icons folder relative to class
     * @param extension menu icons format
     */
    public void setIconSettings ( final Class nearClass, final String path, final String extension )
    {
        this.nearClass = nearClass;
        this.path = path;
        this.extension = extension;
    }

    /**
     * Returns menu language key prefix.
     *
     * @return menu language key prefix
     */
    public String getLanguagePrefix ()
    {
        return languagePrefix;
    }

    /**
     * Sets menu language key prefix.
     *
     * @param prefix menu language key prefix
     */
    public void setLanguagePrefix ( final String prefix )
    {
        this.languagePrefix = prefix;
    }

    /**
     * Returns menu item language key for the specified name.
     *
     * @param text menu item name or text
     * @return menu item language key for the specified name
     */
    public String getLanguageKey ( final String text )
    {
        if ( languagePrefix == null )
        {
            return text;
        }
        else
        {
            final String key = languagePrefix + "." + text;
            return LM.contains ( key ) ? key : text;
        }
    }

    /**
     * Returns newly created menu item.
     *
     * @param icon           menu item icon
     * @param text           menu item text
     * @param hotkey         menu item accelerator
     * @param enabled        whether menu item is enabled or not
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
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

    /**
     * Returns newly created checkbox menu item.
     *
     * @param icon           checkbox menu item icon
     * @param text           checkbox menu item text
     * @param hotkey         checkbox menu item accelerator
     * @param enabled        whether checkbox menu item is enabled or not
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
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

    /**
     * Returns newly created radio button menu item.
     *
     * @param icon           radio button menu item icon
     * @param text           radio button menu item text
     * @param hotkey         radio button menu item accelerator
     * @param enabled        whether radio button menu item is enabled or not
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
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

    /**
     * Returns newly created menu.
     *
     * @param icon           menu icon
     * @param text           menu text
     * @param enabled        whether menu is enabled or not
     * @param actionListener menu action listener
     * @return newly created menu
     */
    public WebMenu createMenu ( final String icon, final String text, final boolean enabled, final ActionListener actionListener )
    {
        final WebMenu item = new WebMenu ();
        item.setIcon ( getIcon ( icon ) );
        item.setLanguage ( getLanguageKey ( text ) );
        item.setEnabled ( enabled );
        item.addActionListener ( actionListener );
        return item;
    }

    /**
     * Starts grouping menu items.
     * All items created after this call and before {@code closeGroup()} call will get grouped.
     *
     * @return buttons group used for grouping
     */
    public UnselectableButtonGroup openGroup ()
    {
        return openGroup ( false );
    }

    /**
     * Starts grouping menu items.
     * All items created after this call and before {@code closeGroup()} call will get grouped.
     *
     * @param unselectable whether items should be unselectable or not
     * @return buttons group used for grouping
     */
    public UnselectableButtonGroup openGroup ( final boolean unselectable )
    {
        return group = new UnselectableButtonGroup ( unselectable );
    }

    /**
     * Adds custom button into currently used buttons group.
     *
     * @param button custom button to add into buttons group
     * @return buttons group used for grouping
     */
    public UnselectableButtonGroup group ( final AbstractButton button )
    {
        if ( group != null )
        {
            group.add ( button );
        }
        return group;
    }

    /**
     * Finishes grouping menu items.
     *
     * @return buttons group used for grouping
     */
    public UnselectableButtonGroup closeGroup ()
    {
        final UnselectableButtonGroup group = this.group;
        this.group = null;
        return group;
    }

    /**
     * Returns icon for the specified name.
     *
     * @param icon icon name
     * @return icon for the specified name
     */
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
}