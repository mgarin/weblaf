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

import com.alee.laf.menu.*;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.utils.TextUtils;
import com.alee.utils.UtilityException;
import com.alee.utils.swing.UnselectableButtonGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

/**
 * Base class for custom menu generators.
 * Menu generators shorten various menus creation code and make it much more convenient to read and support.
 *
 * There are implementations for next menu types:
 * {@link WebMenuBar} - {@link MenuBarGenerator}
 * {@link WebMenu} - {@link MenuGenerator}
 * {@link WebPopupMenu} - {@link PopupMenuGenerator}
 *
 * @param <C> menu component type
 * @author Mikle Garin
 * @see MenuBarGenerator
 * @see MenuGenerator
 * @see PopupMenuGenerator
 */
public abstract class AbstractMenuGenerator<C extends JComponent>
{
    /**
     * Default constants used within generator methods.
     */
    protected static final StyleId defaultStyleId = StyleId.auto;
    protected static final Object defaultIcon = null;
    protected static final HotkeyData defaultHotkey = null;
    protected static final boolean defaultEnabled = true;
    protected static final ActionListener defaultAction = null;

    /**
     * Class near which menu icons are placed.
     * In case it is null path is used as file system path.
     */
    protected Class nearClass;

    /**
     * Path to menu icons folder.
     * It is used as path relative to class in case nearClass variable is not null.
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
     * Menu component.
     */
    protected C menu;

    /**
     * Constructs new menu generator with the specified menu component.
     *
     * @param menu base menu component
     */
    public AbstractMenuGenerator ( final C menu )
    {
        super ();
        this.menu = menu;
    }

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
        this.extension = extension == null ? null : extension.startsWith ( "." ) ? extension : "." + extension;
    }

    /**
     * Sets menu icons location and format.
     *
     * @param path      path to menu icons folder in file system
     * @param extension menu icons format
     */
    public void setIconSettings ( final String path, final String extension )
    {
        setIconSettings ( null, path, extension );
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
        setNearClass ( nearClass );
        setPath ( path );
        setExtension ( extension );
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
     * todo Update all existing items?
     *
     * @param prefix menu language key prefix
     */
    public void setLanguagePrefix ( final String prefix )
    {
        this.languagePrefix = !TextUtils.isEmpty ( prefix ) ? prefix : null;
    }

    /**
     * Returns menu item language key for the specified name.
     *
     * @param text menu item name or text
     * @return menu item language key for the specified name
     */
    public String getLanguageKey ( final String text )
    {
        final String prefix = getLanguagePrefix ();
        if ( prefix != null )
        {
            final String key = prefix + "." + text;
            return LM.containsText ( key ) ? key : text;
        }
        else
        {
            return text;
        }
    }

    /**
     * Adds menu item into menu.
     *
     * @param text           menu item text
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final String text, final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, defaultIcon, text, defaultHotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param text           menu item text
     * @param hotkey         menu item accelerator
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final String text, final HotkeyData hotkey, final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, defaultIcon, text, hotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param text           menu item text
     * @param enabled        whether menu item is enabled or not
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final String text, final boolean enabled, final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, defaultIcon, text, defaultHotkey, enabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param text           menu item text
     * @param hotkey         menu item accelerator
     * @param enabled        whether menu item is enabled or not
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final String text, final HotkeyData hotkey, final boolean enabled, final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, defaultIcon, text, hotkey, enabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param icon           menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu item text
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final Object icon, final String text, final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, icon, text, defaultHotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param icon           menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu item text
     * @param hotkey         menu item accelerator
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final Object icon, final String text, final HotkeyData hotkey, final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, icon, text, hotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param icon           menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu item text
     * @param enabled        whether menu item is enabled or not
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final Object icon, final String text, final boolean enabled, final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, icon, text, defaultHotkey, enabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param icon           menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu item text
     * @param hotkey         menu item accelerator
     * @param enabled        whether menu item is enabled or not
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final Object icon, final String text, final HotkeyData hotkey, final boolean enabled,
                                 final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, icon, text, hotkey, enabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param id             menu item style ID
     * @param text           menu item text
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final StyleId id, final String text, final ActionListener actionListener )
    {
        return addItem ( id, defaultIcon, text, defaultHotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param id             menu item style ID
     * @param text           menu item text
     * @param hotkey         menu item accelerator
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final StyleId id, final String text, final HotkeyData hotkey, final ActionListener actionListener )
    {
        return addItem ( id, defaultIcon, text, hotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param id             menu item style ID
     * @param text           menu item text
     * @param enabled        whether menu item is enabled or not
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final StyleId id, final String text, final boolean enabled, final ActionListener actionListener )
    {
        return addItem ( id, defaultIcon, text, defaultHotkey, enabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param id             menu item style ID
     * @param text           menu item text
     * @param hotkey         menu item accelerator
     * @param enabled        whether menu item is enabled or not
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final StyleId id, final String text, final HotkeyData hotkey, final boolean enabled,
                                 final ActionListener actionListener )
    {
        return addItem ( id, defaultIcon, text, hotkey, enabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param id             menu item style ID
     * @param icon           menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu item text
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final StyleId id, final Object icon, final String text, final ActionListener actionListener )
    {
        return addItem ( id, icon, text, defaultHotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param id             menu item style ID
     * @param icon           menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu item text
     * @param hotkey         menu item accelerator
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final StyleId id, final Object icon, final String text, final HotkeyData hotkey,
                                 final ActionListener actionListener )
    {
        return addItem ( id, icon, text, hotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param id             menu item style ID
     * @param icon           menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu item text
     * @param enabled        whether menu item is enabled or not
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final StyleId id, final Object icon, final String text, final boolean enabled,
                                 final ActionListener actionListener )
    {
        return addItem ( id, icon, text, defaultHotkey, enabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     *
     * @param id             menu item style ID
     * @param icon           menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu item text
     * @param hotkey         menu item accelerator
     * @param enabled        whether menu item is enabled or not
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    public WebMenuItem addItem ( final StyleId id, final Object icon, final String text, final HotkeyData hotkey, final boolean enabled,
                                 final ActionListener actionListener )
    {
        final WebMenuItem item = createItem ( id, icon, text, hotkey, enabled, actionListener );
        getMenu ().add ( item );
        return item;
    }

    /**
     * Returns newly created menu item.
     *
     * @param id             menu item style ID
     * @param icon           menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu item text
     * @param hotkey         menu item accelerator
     * @param enabled        whether menu item is enabled or not
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    protected WebMenuItem createItem ( final StyleId id, final Object icon, final String text, final HotkeyData hotkey,
                                       final boolean enabled, final ActionListener actionListener )
    {
        final WebMenuItem item = new WebMenuItem ( id, getLanguageKey ( text ) );
        item.setIcon ( getIcon ( icon ) );
        item.setAccelerator ( hotkey );
        item.setEnabled ( enabled );
        if ( actionListener != null )
        {
            item.addActionListener ( actionListener );
        }
        return item;
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param text           checkbox menu item text
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final String text, final boolean selected, final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, defaultIcon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param text           checkbox menu item text
     * @param hotkey         checkbox menu item accelerator
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final String text, final HotkeyData hotkey, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, defaultIcon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param text           checkbox menu item text
     * @param enabled        whether checkbox menu item is enabled or not
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final String text, final boolean enabled, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, defaultIcon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param text           checkbox menu item text
     * @param hotkey         checkbox menu item accelerator
     * @param enabled        whether checkbox menu item is enabled or not
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final String text, final HotkeyData hotkey, final boolean enabled, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, defaultIcon, text, hotkey, enabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param icon           checkbox menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           checkbox menu item text
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final Object icon, final String text, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, icon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param icon           checkbox menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           checkbox menu item text
     * @param hotkey         checkbox menu item accelerator
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final Object icon, final String text, final HotkeyData hotkey, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, icon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param icon           checkbox menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           checkbox menu item text
     * @param enabled        whether checkbox menu item is enabled or not
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final Object icon, final String text, final boolean enabled, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, icon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param icon           checkbox menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           checkbox menu item text
     * @param hotkey         checkbox menu item accelerator
     * @param enabled        whether checkbox menu item is enabled or not
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final Object icon, final String text, final HotkeyData hotkey, final boolean enabled,
                                              final boolean selected, final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, icon, text, hotkey, enabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param id             checkbox menu item style ID
     * @param text           checkbox menu item text
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final StyleId id, final String text, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( id, defaultIcon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param id             checkbox menu item style ID
     * @param text           checkbox menu item text
     * @param hotkey         checkbox menu item accelerator
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final StyleId id, final String text, final HotkeyData hotkey, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( id, defaultIcon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param id             checkbox menu item style ID
     * @param text           checkbox menu item text
     * @param enabled        whether checkbox menu item is enabled or not
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final StyleId id, final String text, final boolean enabled, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( id, defaultIcon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param id             checkbox menu item style ID
     * @param text           checkbox menu item text
     * @param hotkey         checkbox menu item accelerator
     * @param enabled        whether checkbox menu item is enabled or not
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final StyleId id, final String text, final HotkeyData hotkey, final boolean enabled,
                                              final boolean selected, final ActionListener actionListener )
    {
        return addCheckItem ( id, defaultIcon, text, hotkey, enabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param id             checkbox menu item style ID
     * @param icon           checkbox menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           checkbox menu item text
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final StyleId id, final Object icon, final String text, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( id, icon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param id             checkbox menu item style ID
     * @param icon           checkbox menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           checkbox menu item text
     * @param hotkey         checkbox menu item accelerator
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final StyleId id, final Object icon, final String text, final HotkeyData hotkey,
                                              final boolean selected, final ActionListener actionListener )
    {
        return addCheckItem ( id, icon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param id             checkbox menu item style ID
     * @param icon           checkbox menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           checkbox menu item text
     * @param enabled        whether checkbox menu item is enabled or not
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final StyleId id, final Object icon, final String text, final boolean enabled,
                                              final boolean selected, final ActionListener actionListener )
    {
        return addCheckItem ( id, icon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds checkbox menu item into menu.
     *
     * @param id             checkbox menu item style ID
     * @param icon           checkbox menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           checkbox menu item text
     * @param hotkey         checkbox menu item accelerator
     * @param enabled        whether checkbox menu item is enabled or not
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    public WebCheckBoxMenuItem addCheckItem ( final StyleId id, final Object icon, final String text, final HotkeyData hotkey,
                                              final boolean enabled, final boolean selected, final ActionListener actionListener )
    {
        final WebCheckBoxMenuItem item = createCheckItem ( id, icon, text, hotkey, enabled, selected, actionListener );
        getMenu ().add ( item );
        return item;
    }

    /**
     * Returns newly created checkbox menu item.
     *
     * @param id             checkbox menu item style ID
     * @param icon           checkbox menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           checkbox menu item text
     * @param hotkey         checkbox menu item accelerator
     * @param enabled        whether checkbox menu item is enabled or not
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    protected WebCheckBoxMenuItem createCheckItem ( final StyleId id, final Object icon, final String text, final HotkeyData hotkey,
                                                    final boolean enabled, final boolean selected, final ActionListener actionListener )
    {
        final WebCheckBoxMenuItem item = new WebCheckBoxMenuItem ( id, getLanguageKey ( text ) );
        final Icon resolvedIcon = getIcon ( icon );
        if ( resolvedIcon != null )
        {
            item.setIcon ( resolvedIcon );
        }
        item.setAccelerator ( hotkey );
        item.setEnabled ( enabled );
        item.setSelected ( selected );
        if ( actionListener != null )
        {
            item.addActionListener ( actionListener );
        }
        group ( item );
        return item;
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param text           radio button menu item text
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final String text, final boolean selected, final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, defaultIcon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param text           radio button menu item text
     * @param hotkey         radio button menu item accelerator
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final String text, final HotkeyData hotkey, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, defaultIcon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param text           radio button menu item text
     * @param enabled        whether radio button menu item is enabled or not
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final String text, final boolean enabled, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, defaultIcon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param text           radio button menu item text
     * @param hotkey         radio button menu item accelerator
     * @param enabled        whether radio button menu item is enabled or not
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final String text, final HotkeyData hotkey, final boolean enabled, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, defaultIcon, text, hotkey, enabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param icon           radio button menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           radio button menu item text
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final Object icon, final String text, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, icon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param icon           radio button menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           radio button menu item text
     * @param hotkey         radio button menu item accelerator
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final Object icon, final String text, final HotkeyData hotkey, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, icon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param icon           radio button menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           radio button menu item text
     * @param enabled        whether radio button menu item is enabled or not
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final Object icon, final String text, final boolean enabled, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, icon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param icon           radio button menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           radio button menu item text
     * @param hotkey         radio button menu item accelerator
     * @param enabled        whether radio button menu item is enabled or not
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final Object icon, final String text, final HotkeyData hotkey, final boolean enabled,
                                                 final boolean selected, final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, icon, text, hotkey, enabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param id             radio button menu item style ID
     * @param text           radio button menu item text
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final StyleId id, final String text, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( id, defaultIcon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param id             radio button menu item style ID
     * @param text           radio button menu item text
     * @param hotkey         radio button menu item accelerator
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final StyleId id, final String text, final HotkeyData hotkey, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( id, defaultIcon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param id             radio button menu item style ID
     * @param text           radio button menu item text
     * @param enabled        whether radio button menu item is enabled or not
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final StyleId id, final String text, final boolean enabled, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( id, defaultIcon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param id             radio button menu item style ID
     * @param text           radio button menu item text
     * @param hotkey         radio button menu item accelerator
     * @param enabled        whether radio button menu item is enabled or not
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final StyleId id, final String text, final HotkeyData hotkey, final boolean enabled,
                                                 final boolean selected, final ActionListener actionListener )
    {
        return addRadioItem ( id, defaultIcon, text, hotkey, enabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param id             radio button menu item style ID
     * @param icon           radio button menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           radio button menu item text
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final StyleId id, final Object icon, final String text, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( id, icon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param id             radio button menu item style ID
     * @param icon           radio button menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           radio button menu item text
     * @param hotkey         radio button menu item accelerator
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final StyleId id, final Object icon, final String text, final HotkeyData hotkey,
                                                 final boolean selected, final ActionListener actionListener )
    {
        return addRadioItem ( id, icon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param id             radio button menu item style ID
     * @param icon           radio button menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           radio button menu item text
     * @param enabled        whether radio button menu item is enabled or not
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final StyleId id, final Object icon, final String text, final boolean enabled,
                                                 final boolean selected, final ActionListener actionListener )
    {
        return addRadioItem ( id, icon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds radio button menu item into menu.
     *
     * @param id             radio button menu item style ID
     * @param icon           radio button menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           radio button menu item text
     * @param hotkey         radio button menu item accelerator
     * @param enabled        whether radio button menu item is enabled or not
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    public WebRadioButtonMenuItem addRadioItem ( final StyleId id, final Object icon, final String text, final HotkeyData hotkey,
                                                 final boolean enabled, final boolean selected, final ActionListener actionListener )
    {
        final WebRadioButtonMenuItem item = createRadioItem ( id, icon, text, hotkey, enabled, selected, actionListener );
        getMenu ().add ( item );
        return item;
    }

    /**
     * Returns newly created radio button menu item.
     *
     * @param id             radio button menu item style ID
     * @param icon           radio button menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           radio button menu item text
     * @param hotkey         radio button menu item accelerator
     * @param enabled        whether radio button menu item is enabled or not
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    protected WebRadioButtonMenuItem createRadioItem ( final StyleId id, final Object icon, final String text, final HotkeyData hotkey,
                                                       final boolean enabled, final boolean selected, final ActionListener actionListener )
    {
        final WebRadioButtonMenuItem item = new WebRadioButtonMenuItem ( id, getLanguageKey ( text ) );
        final Icon resolvedIcon = getIcon ( icon );
        if ( resolvedIcon != null )
        {
            item.setIcon ( resolvedIcon );
        }
        item.setAccelerator ( hotkey );
        item.setEnabled ( enabled );
        item.setSelected ( selected );
        if ( actionListener != null )
        {
            item.addActionListener ( actionListener );
        }
        group ( item );
        return item;
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param text menu text
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final String text )
    {
        return addSubMenu ( defaultStyleId, defaultIcon, text, defaultEnabled, defaultAction );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param text    menu text
     * @param enabled whether menu is enabled or not
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final String text, final boolean enabled )
    {
        return addSubMenu ( defaultStyleId, defaultIcon, text, enabled, defaultAction );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param text           menu text
     * @param actionListener menu action listener
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final String text, final ActionListener actionListener )
    {
        return addSubMenu ( defaultStyleId, defaultIcon, text, defaultEnabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param text           menu text
     * @param enabled        whether menu is enabled or not
     * @param actionListener menu action listener
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final String text, final boolean enabled, final ActionListener actionListener )
    {
        return addSubMenu ( defaultStyleId, defaultIcon, text, enabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param icon menu icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text menu text
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final Object icon, final String text )
    {
        return addSubMenu ( defaultStyleId, icon, text, defaultEnabled, defaultAction );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param icon    menu icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text    menu text
     * @param enabled whether menu is enabled or not
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final Object icon, final String text, final boolean enabled )
    {
        return addSubMenu ( defaultStyleId, icon, text, enabled, defaultAction );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param icon           menu icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu text
     * @param actionListener menu action listener
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final Object icon, final String text, final ActionListener actionListener )
    {
        return addSubMenu ( defaultStyleId, icon, text, defaultEnabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param icon           menu icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu text
     * @param enabled        whether menu is enabled or not
     * @param actionListener menu action listener
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final Object icon, final String text, final boolean enabled, final ActionListener actionListener )
    {
        return addSubMenu ( defaultStyleId, icon, text, enabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param id   menu style ID
     * @param text menu text
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final StyleId id, final String text )
    {
        return addSubMenu ( id, defaultIcon, text, defaultEnabled, defaultAction );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param id      menu style ID
     * @param text    menu text
     * @param enabled whether menu is enabled or not
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final StyleId id, final String text, final boolean enabled )
    {
        return addSubMenu ( id, defaultIcon, text, enabled, defaultAction );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param id             menu style ID
     * @param text           menu text
     * @param actionListener menu action listener
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final StyleId id, final String text, final ActionListener actionListener )
    {
        return addSubMenu ( id, defaultIcon, text, defaultEnabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param id             menu style ID
     * @param text           menu text
     * @param enabled        whether menu is enabled or not
     * @param actionListener menu action listener
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final StyleId id, final String text, final boolean enabled, final ActionListener actionListener )
    {
        return addSubMenu ( id, defaultIcon, text, enabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param id   menu style ID
     * @param icon menu icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text menu text
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final StyleId id, final Object icon, final String text )
    {
        return addSubMenu ( id, icon, text, defaultEnabled, defaultAction );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param id      menu style ID
     * @param icon    menu icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text    menu text
     * @param enabled whether menu is enabled or not
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final StyleId id, final Object icon, final String text, final boolean enabled )
    {
        return addSubMenu ( id, icon, text, enabled, defaultAction );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param id             menu style ID
     * @param icon           menu icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu text
     * @param actionListener menu action listener
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final StyleId id, final Object icon, final String text, final ActionListener actionListener )
    {
        return addSubMenu ( id, icon, text, defaultEnabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     * Returned menu generator will have the same settings as current one, but you can modify those later.
     *
     * @param id             menu style ID
     * @param icon           menu icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu text
     * @param enabled        whether menu is enabled or not
     * @param actionListener menu action listener
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final StyleId id, final Object icon, final String text, final boolean enabled,
                                      final ActionListener actionListener )
    {
        final WebMenu menu = createSubMenu ( id, icon, text, enabled, actionListener );
        getMenu ().add ( menu );

        // Creting sub-menu generator
        final MenuGenerator menuGenerator = new MenuGenerator ( menu );
        menuGenerator.setIconSettings ( getNearClass (), getPath (), getExtension () );
        menuGenerator.setLanguagePrefix ( getLanguagePrefix () );
        return menuGenerator;
    }

    /**
     * Returns newly created menu.
     *
     * @param id             menu style ID
     * @param icon           menu icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu text
     * @param enabled        whether menu is enabled or not
     * @param actionListener menu action listener
     * @return newly created menu
     */
    protected WebMenu createSubMenu ( final StyleId id, final Object icon, final String text, final boolean enabled,
                                      final ActionListener actionListener )
    {
        final WebMenu menu = new WebMenu ( id, getLanguageKey ( text ) );
        menu.setIcon ( getIcon ( icon ) );
        menu.setEnabled ( enabled );
        if ( actionListener != null )
        {
            menu.addActionListener ( actionListener );
        }
        return menu;
    }

    /**
     * Adds separator into menu.
     */
    public void addSeparator ()
    {
        final C menuComponent = getMenu ();
        if ( menuComponent instanceof WebMenu )
        {
            ( ( WebMenu ) menuComponent ).addSeparator ();
        }
        else if ( menuComponent instanceof WebPopupMenu )
        {
            ( ( WebPopupMenu ) menuComponent ).addSeparator ();
        }
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
     * Returns menu component.
     *
     * @return menu component
     */
    public C getMenu ()
    {
        return menu;
    }

    /**
     * Returns whether menu is empty or not.
     *
     * @return true if menu is empty, false otherwise
     */
    public boolean isEmpty ()
    {
        return getMenu ().getComponentCount () == 0;
    }

    /**
     * Returns icon for the specified name.
     *
     * @param icon can be either String icon name, ImageIcon, Image, image File or image URL
     * @return icon for the specified name
     */
    protected Icon getIcon ( final Object icon )
    {
        if ( icon != null )
        {
            if ( icon instanceof String )
            {
                try
                {
                    if ( getNearClass () != null )
                    {
                        return new ImageIcon ( getNearClass ().getResource ( getPath () + icon + getExtension () ) );
                    }
                    else
                    {
                        return new ImageIcon ( new File ( getPath (), icon + getExtension () ).getAbsolutePath () );
                    }
                }
                catch ( final Exception e )
                {
                    throw new UtilityException ( "Unable to find menu icon for path: " + getPath () + icon + getExtension (), e );
                }
            }
            else if ( icon instanceof Icon )
            {
                return ( Icon ) icon;
            }
            else if ( icon instanceof Image )
            {
                return new ImageIcon ( ( Image ) icon );
            }
            else if ( icon instanceof File )
            {
                return new ImageIcon ( ( ( File ) icon ).getAbsolutePath () );
            }
            else if ( icon instanceof URL )
            {
                return new ImageIcon ( ( URL ) icon );
            }
            else
            {
                throw new UtilityException ( "Unknown icon object type provided: " + icon );
            }
        }
        else
        {
            return null;
        }
    }
}