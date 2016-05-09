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
import com.alee.managers.log.Log;
import com.alee.utils.swing.UnselectableButtonGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

/**
 * This is a base generator class for custom menu generators.
 * Menu generators are made to simplify various Swing menues creation.
 * Possible menu types: WebMenuBar, WebPopupMenu and WebMenu
 *
 * @author Mikle Garin
 * @see com.alee.utils.swing.menu.MenuBarGenerator
 * @see com.alee.utils.swing.menu.MenuGenerator
 * @see com.alee.utils.swing.menu.PopupMenuGenerator
 */

public abstract class AbstractMenuGenerator<E extends JComponent>
{
    /**
     * Default constants used within generator methods.
     */
    protected static final Object defaultIcon = null;
    protected static final HotkeyData defaultHotkey = null;
    protected static final boolean defaultEnabled = true;
    protected static final boolean defaultSelected = false;
    protected static final ActionListener defaultAction = null;

    /**
     * Default menu icons format.
     */
    protected static final String defaultIconFormat = ".png";

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
    protected E menu;

    /**
     * Constructs new menu generator with the specified menu component.
     *
     * @param menu base menu component
     */
    public AbstractMenuGenerator ( final E menu )
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
        this.nearClass = null;
        this.path = path;
        this.extension = extension;
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
     * Adds separator into menu.
     */
    public void addSeparator ()
    {
        final E menuComponent = getMenu ();
        if ( menuComponent instanceof WebMenu )
        {
            ( ( WebMenu ) menuComponent ).addSeparator ();
        }
        else if ( menuComponent instanceof WebPopupMenu )
        {
            ( ( WebPopupMenu ) menuComponent ).addSeparator ();
        }
    }

    public WebMenuItem addItem ( final String text, final ActionListener actionListener )
    {
        return addItem ( defaultIcon, text, defaultHotkey, defaultEnabled, actionListener );
    }

    public WebMenuItem addItem ( final String text, final HotkeyData hotkey, final ActionListener actionListener )
    {
        return addItem ( defaultIcon, text, hotkey, defaultEnabled, actionListener );
    }

    public WebMenuItem addItem ( final String text, final boolean enabled, final ActionListener actionListener )
    {
        return addItem ( defaultIcon, text, defaultHotkey, enabled, actionListener );
    }

    public WebMenuItem addItem ( final String text, final HotkeyData hotkey, final boolean enabled, final ActionListener actionListener )
    {
        return addItem ( defaultIcon, text, hotkey, enabled, actionListener );
    }

    public WebMenuItem addItem ( final Object icon, final String text, final ActionListener actionListener )
    {
        return addItem ( icon, text, defaultHotkey, defaultEnabled, actionListener );
    }

    public WebMenuItem addItem ( final Object icon, final String text, final HotkeyData hotkey, final ActionListener actionListener )
    {
        return addItem ( icon, text, hotkey, defaultEnabled, actionListener );
    }

    public WebMenuItem addItem ( final Object icon, final String text, final boolean enabled, final ActionListener actionListener )
    {
        return addItem ( icon, text, defaultHotkey, enabled, actionListener );
    }

    /**
     * Adds simple item into menu.
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
        final WebMenuItem item = createItem ( icon, text, hotkey, enabled, actionListener );
        getMenu ().add ( item );
        return item;
    }

    /**
     * Returns newly created menu item.
     *
     * @param icon           menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu item text
     * @param hotkey         menu item accelerator
     * @param enabled        whether menu item is enabled or not
     * @param actionListener menu item action listener
     * @return newly created menu item
     */
    protected WebMenuItem createItem ( final Object icon, final String text, final HotkeyData hotkey, final boolean enabled,
                                       final ActionListener actionListener )
    {
        final WebMenuItem item = new WebMenuItem ();
        item.setIcon ( getIcon ( icon ) );
        item.setLanguage ( getLanguageKey ( text ) );
        item.setAccelerator ( hotkey );
        item.setEnabled ( enabled );
        if ( actionListener != null )
        {
            item.addActionListener ( actionListener );
        }
        return item;
    }

    public WebCheckBoxMenuItem addCheckItem ( final String text, final boolean selected, final ActionListener actionListener )
    {
        return addCheckItem ( defaultIcon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    public WebCheckBoxMenuItem addCheckItem ( final String text, final HotkeyData hotkey, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( defaultIcon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    public WebCheckBoxMenuItem addCheckItem ( final String text, final boolean enabled, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( defaultIcon, text, defaultHotkey, enabled, selected, actionListener );
    }

    public WebCheckBoxMenuItem addCheckItem ( final String text, final HotkeyData hotkey, final boolean enabled, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( defaultIcon, text, hotkey, enabled, selected, actionListener );
    }

    public WebCheckBoxMenuItem addCheckItem ( final Object icon, final String text, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( icon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    public WebCheckBoxMenuItem addCheckItem ( final Object icon, final String text, final HotkeyData hotkey, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( icon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    public WebCheckBoxMenuItem addCheckItem ( final Object icon, final String text, final boolean enabled, final boolean selected,
                                              final ActionListener actionListener )
    {
        return addCheckItem ( icon, text, defaultHotkey, enabled, selected, actionListener );
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
        final WebCheckBoxMenuItem item = createCheckItem ( icon, text, hotkey, enabled, selected, actionListener );
        getMenu ().add ( item );
        return item;
    }

    /**
     * Returns newly created checkbox menu item.
     *
     * @param icon           checkbox menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           checkbox menu item text
     * @param hotkey         checkbox menu item accelerator
     * @param enabled        whether checkbox menu item is enabled or not
     * @param selected       whether checkbox menu item is selected or not
     * @param actionListener checkbox menu item action listener
     * @return newly created checkbox menu item
     */
    protected WebCheckBoxMenuItem createCheckItem ( final Object icon, final String text, final HotkeyData hotkey, final boolean enabled,
                                                    final boolean selected, final ActionListener actionListener )
    {
        final WebCheckBoxMenuItem item = new WebCheckBoxMenuItem ();
        item.setIcon ( getIcon ( icon ) );
        item.setLanguage ( getLanguageKey ( text ) );
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

    public WebRadioButtonMenuItem addRadioItem ( final String text, final boolean selected, final ActionListener actionListener )
    {
        return addRadioItem ( defaultIcon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    public WebRadioButtonMenuItem addRadioItem ( final String text, final HotkeyData hotkey, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( defaultIcon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    public WebRadioButtonMenuItem addRadioItem ( final String text, final boolean enabled, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( defaultIcon, text, defaultHotkey, enabled, selected, actionListener );
    }

    public WebRadioButtonMenuItem addRadioItem ( final String text, final HotkeyData hotkey, final boolean enabled, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( defaultIcon, text, hotkey, enabled, selected, actionListener );
    }

    public WebRadioButtonMenuItem addRadioItem ( final Object icon, final String text, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( icon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    public WebRadioButtonMenuItem addRadioItem ( final Object icon, final String text, final HotkeyData hotkey, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( icon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    public WebRadioButtonMenuItem addRadioItem ( final Object icon, final String text, final boolean enabled, final boolean selected,
                                                 final ActionListener actionListener )
    {
        return addRadioItem ( icon, text, defaultHotkey, enabled, selected, actionListener );
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
        final WebRadioButtonMenuItem item = createRadioItem ( icon, text, hotkey, enabled, selected, actionListener );
        getMenu ().add ( item );
        return item;
    }

    /**
     * Returns newly created radio button menu item.
     *
     * @param icon           radio button menu item icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           radio button menu item text
     * @param hotkey         radio button menu item accelerator
     * @param enabled        whether radio button menu item is enabled or not
     * @param selected       whether radio button menu item is selected or not
     * @param actionListener radio button menu item action listener
     * @return newly created radio button menu item
     */
    protected WebRadioButtonMenuItem createRadioItem ( final Object icon, final String text, final HotkeyData hotkey, final boolean enabled,
                                                       final boolean selected, final ActionListener actionListener )
    {
        final WebRadioButtonMenuItem item = new WebRadioButtonMenuItem ();
        item.setIcon ( getIcon ( icon ) );
        item.setLanguage ( getLanguageKey ( text ) );
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

    public MenuGenerator addSubMenu ( final String text )
    {
        return addSubMenu ( defaultIcon, text, defaultEnabled, defaultAction );
    }

    public MenuGenerator addSubMenu ( final String text, final boolean enabled )
    {
        return addSubMenu ( defaultIcon, text, enabled, defaultAction );
    }

    public MenuGenerator addSubMenu ( final String text, final ActionListener actionListener )
    {
        return addSubMenu ( defaultIcon, text, defaultEnabled, actionListener );
    }

    public MenuGenerator addSubMenu ( final String text, final boolean enabled, final ActionListener actionListener )
    {
        return addSubMenu ( defaultIcon, text, enabled, actionListener );
    }

    public MenuGenerator addSubMenu ( final Object icon, final String text )
    {
        return addSubMenu ( icon, text, defaultEnabled, defaultAction );
    }

    public MenuGenerator addSubMenu ( final Object icon, final String text, final boolean enabled )
    {
        return addSubMenu ( icon, text, enabled, defaultAction );
    }

    public MenuGenerator addSubMenu ( final Object icon, final String text, final ActionListener actionListener )
    {
        return addSubMenu ( icon, text, defaultEnabled, actionListener );
    }

    /**
     * Adds menu item into menu.
     * <p>
     * Returned menu generator will have the same settings as current one.
     * You can modify them if you need though.
     *
     * @param icon           menu icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu text
     * @param enabled        whether menu is enabled or not
     * @param actionListener menu action listener
     * @return menu generator for newly created menu
     */
    public MenuGenerator addSubMenu ( final Object icon, final String text, final boolean enabled, final ActionListener actionListener )
    {
        final WebMenu menu = createSubMenu ( icon, text, enabled, actionListener );
        getMenu ().add ( menu );

        // Creting sub-menu generator
        final MenuGenerator menuGenerator = new MenuGenerator ( menu );
        menuGenerator.setNearClass ( nearClass );
        menuGenerator.setPath ( path );
        menuGenerator.setExtension ( extension );
        menuGenerator.setLanguagePrefix ( languagePrefix );
        return menuGenerator;
    }

    /**
     * Returns newly created menu.
     *
     * @param icon           menu icon, can be either String icon name, ImageIcon, Image, image File or image URL
     * @param text           menu text
     * @param enabled        whether menu is enabled or not
     * @param actionListener menu action listener
     * @return newly created menu
     */
    protected WebMenu createSubMenu ( final Object icon, final String text, final boolean enabled, final ActionListener actionListener )
    {
        final WebMenu menu = new WebMenu ();
        menu.setIcon ( getIcon ( icon ) );
        menu.setLanguage ( getLanguageKey ( text ) );
        menu.setEnabled ( enabled );
        if ( actionListener != null )
        {
            menu.addActionListener ( actionListener );
        }
        return menu;
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
     * @param icon can be either String icon name, ImageIcon, Image, image File or image URL
     * @return icon for the specified name
     */
    public Icon getIcon ( final Object icon )
    {
        if ( icon != null )
        {
            if ( icon instanceof String )
            {
                try
                {
                    if ( nearClass != null )
                    {
                        return new ImageIcon ( nearClass.getResource ( path + icon + extension ) );
                    }
                    else
                    {
                        return new ImageIcon ( new File ( path, icon + extension ).getAbsolutePath () );
                    }
                }
                catch ( final Throwable e )
                {
                    Log.warn ( "Unable to find menu icon for path: " + path + icon + extension, e );
                    return null;
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
                Log.warn ( "Unknown icon object type provided: " + icon );
                return null;
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns menu component.
     *
     * @return menu component
     */
    public E getMenu ()
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
        return menu.getComponentCount () == 0;
    }
}