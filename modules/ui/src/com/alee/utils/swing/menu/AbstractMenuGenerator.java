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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.resource.ClassResource;
import com.alee.api.resource.FileResource;
import com.alee.api.resource.Resource;
import com.alee.api.resource.UrlResource;
import com.alee.laf.menu.*;
import com.alee.managers.hotkey.HotkeyData;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.utils.ImageUtils;
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
    @Nullable
    protected Class nearClass;

    /**
     * Path to menu icons folder.
     * It is used as path relative to class in case nearClass variable is not null.
     */
    @Nullable
    protected String path;

    /**
     * Menu icons format.
     */
    @Nullable
    protected String extension;

    /**
     * Menu language key prefix.
     */
    @Nullable
    protected String languagePrefix;

    /**
     * Buttons grouping.
     */
    @Nullable
    protected UnselectableButtonGroup group;

    /**
     * Menu {@link JComponent}.
     */
    @NotNull
    protected final C menu;

    /**
     * Constructs new menu generator with the specified menu component.
     *
     * @param menu base menu component
     */
    public AbstractMenuGenerator ( @NotNull final C menu )
    {
        this.menu = menu;
    }

    /**
     * Returns class near which menu icons are placed.
     *
     * @return class near which menu icons are placed
     */
    @Nullable
    public Class getNearClass ()
    {
        return nearClass;
    }

    /**
     * Sets class near which menu icons are placed.
     *
     * @param nearClass class near which menu icons are placed
     */
    public void setNearClass ( @Nullable final Class nearClass )
    {
        this.nearClass = nearClass;
    }

    /**
     * Returns path to menu icons folder relative to class.
     *
     * @return path to menu icons folder relative to class
     */
    @Nullable
    public String getPath ()
    {
        return path;
    }

    /**
     * Sets path to menu icons folder relative to class.
     *
     * @param path path to menu icons folder relative to class
     */
    public void setPath ( @Nullable final String path )
    {
        this.path = path;
    }

    /**
     * Returns menu icons format.
     *
     * @return menu icons format
     */
    @Nullable
    public String getExtension ()
    {
        return extension;
    }

    /**
     * Sets menu icons format.
     *
     * @param extension menu icons format
     */
    public void setExtension ( @Nullable final String extension )
    {
        this.extension = extension == null ? null : extension.startsWith ( "." ) ? extension : "." + extension;
    }

    /**
     * Sets menu icons location and format.
     *
     * @param path      path to menu icons folder in file system
     * @param extension menu icons format
     */
    public void setIconSettings ( @Nullable final String path, @Nullable final String extension )
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
    public void setIconSettings ( @Nullable final Class nearClass, @Nullable final String path, @Nullable final String extension )
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
    @Nullable
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
    public void setLanguagePrefix ( @Nullable final String prefix )
    {
        this.languagePrefix = TextUtils.notEmpty ( prefix ) ? prefix : null;
    }

    /**
     * Returns menu item language key for the specified name.
     *
     * @param text menu item name or text
     * @return menu item language key for the specified name
     */
    @Nullable
    public String getLanguageKey ( @Nullable final String text )
    {
        final String languageKey;
        if ( text != null )
        {
            final String prefix = getLanguagePrefix ();
            if ( prefix != null )
            {
                final String key = prefix + "." + text;
                languageKey = LM.containsText ( key ) ? key : text;
            }
            else
            {
                languageKey = text;
            }
        }
        else
        {
            languageKey = null;
        }
        return languageKey;
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param text           {@link WebMenuItem} text
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @Nullable final String text, @Nullable final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, defaultIcon, text, defaultHotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param text           {@link WebMenuItem} text
     * @param hotkey         {@link WebMenuItem} accelerator
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @Nullable final String text, @Nullable final HotkeyData hotkey,
                                 @Nullable final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, defaultIcon, text, hotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param text           {@link WebMenuItem} text
     * @param enabled        whether {@link WebMenuItem} is enabled or not
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @Nullable final String text, final boolean enabled, @Nullable final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, defaultIcon, text, defaultHotkey, enabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param text           {@link WebMenuItem} text
     * @param hotkey         {@link WebMenuItem} accelerator
     * @param enabled        whether {@link WebMenuItem} is enabled or not
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @Nullable final String text, @Nullable final HotkeyData hotkey, final boolean enabled,
                                 @Nullable final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, defaultIcon, text, hotkey, enabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenuItem} text
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @Nullable final Object icon, @Nullable final String text, @Nullable final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, icon, text, defaultHotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenuItem} text
     * @param hotkey         {@link WebMenuItem} accelerator
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @Nullable final Object icon, @Nullable final String text, @Nullable final HotkeyData hotkey,
                                 @Nullable final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, icon, text, hotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenuItem} text
     * @param enabled        whether {@link WebMenuItem} is enabled or not
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @Nullable final Object icon, @Nullable final String text, final boolean enabled,
                                 @Nullable final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, icon, text, defaultHotkey, enabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenuItem} text
     * @param hotkey         {@link WebMenuItem} accelerator
     * @param enabled        whether {@link WebMenuItem} is enabled or not
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @Nullable final Object icon, @Nullable final String text, @Nullable final HotkeyData hotkey,
                                 final boolean enabled, @Nullable final ActionListener actionListener )
    {
        return addItem ( defaultStyleId, icon, text, hotkey, enabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param id             {@link WebMenuItem} {@link StyleId}
     * @param text           {@link WebMenuItem} text
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @NotNull final StyleId id, @Nullable final String text, @Nullable final ActionListener actionListener )
    {
        return addItem ( id, defaultIcon, text, defaultHotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param id             {@link WebMenuItem} {@link StyleId}
     * @param text           {@link WebMenuItem} text
     * @param hotkey         {@link WebMenuItem} accelerator
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @NotNull final StyleId id, @Nullable final String text, @Nullable final HotkeyData hotkey,
                                 @Nullable final ActionListener actionListener )
    {
        return addItem ( id, defaultIcon, text, hotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param id             {@link WebMenuItem} {@link StyleId}
     * @param text           {@link WebMenuItem} text
     * @param enabled        whether {@link WebMenuItem} is enabled or not
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @NotNull final StyleId id, @Nullable final String text, final boolean enabled,
                                 @Nullable final ActionListener actionListener )
    {
        return addItem ( id, defaultIcon, text, defaultHotkey, enabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param id             {@link WebMenuItem} {@link StyleId}
     * @param text           {@link WebMenuItem} text
     * @param hotkey         {@link WebMenuItem} accelerator
     * @param enabled        whether {@link WebMenuItem} is enabled or not
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @NotNull final StyleId id, @Nullable final String text, @Nullable final HotkeyData hotkey,
                                 final boolean enabled, @Nullable final ActionListener actionListener )
    {
        return addItem ( id, defaultIcon, text, hotkey, enabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param id             {@link WebMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenuItem} text
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                 @Nullable final ActionListener actionListener )
    {
        return addItem ( id, icon, text, defaultHotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param id             {@link WebMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenuItem} text
     * @param hotkey         {@link WebMenuItem} accelerator
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                 @Nullable final HotkeyData hotkey, @Nullable final ActionListener actionListener )
    {
        return addItem ( id, icon, text, hotkey, defaultEnabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param id             {@link WebMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenuItem} text
     * @param enabled        whether {@link WebMenuItem} is enabled or not
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text, final boolean enabled,
                                 @Nullable final ActionListener actionListener )
    {
        return addItem ( id, icon, text, defaultHotkey, enabled, actionListener );
    }

    /**
     * Adds {@link WebMenuItem} into menu.
     *
     * @param id             {@link WebMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenuItem} text
     * @param hotkey         {@link WebMenuItem} accelerator
     * @param enabled        whether {@link WebMenuItem} is enabled or not
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    public WebMenuItem addItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                 @Nullable final HotkeyData hotkey, final boolean enabled, @Nullable final ActionListener actionListener )
    {
        final WebMenuItem item = createItem ( id, icon, text, hotkey, enabled, actionListener );
        getMenu ().add ( item );
        return item;
    }

    /**
     * Returns newly created {@link WebMenuItem}.
     *
     * @param id             {@link WebMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenuItem} text
     * @param hotkey         {@link WebMenuItem} accelerator
     * @param enabled        whether {@link WebMenuItem} is enabled or not
     * @param actionListener {@link WebMenuItem} {@link ActionListener}
     * @return newly created {@link WebMenuItem}
     */
    @NotNull
    protected WebMenuItem createItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                       @Nullable final HotkeyData hotkey, final boolean enabled,
                                       @Nullable final ActionListener actionListener )
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
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @Nullable final String text, final boolean selected,
                                              @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, defaultIcon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param hotkey         {@link WebCheckBoxMenuItem} accelerator
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @Nullable final String text, @Nullable final HotkeyData hotkey, final boolean selected,
                                              @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, defaultIcon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param enabled        whether {@link WebCheckBoxMenuItem} is enabled or not
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @Nullable final String text, final boolean enabled, final boolean selected,
                                              @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, defaultIcon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param hotkey         {@link WebCheckBoxMenuItem} accelerator
     * @param enabled        whether {@link WebCheckBoxMenuItem} is enabled or not
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @Nullable final String text, @Nullable final HotkeyData hotkey, final boolean enabled,
                                              final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, defaultIcon, text, hotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @Nullable final Object icon, @Nullable final String text, final boolean selected,
                                              @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, icon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param hotkey         {@link WebCheckBoxMenuItem} accelerator
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @Nullable final Object icon, @Nullable final String text, @Nullable final HotkeyData hotkey,
                                              final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, icon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param enabled        whether {@link WebCheckBoxMenuItem} is enabled or not
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @Nullable final Object icon, @Nullable final String text, final boolean enabled,
                                              final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, icon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param hotkey         {@link WebCheckBoxMenuItem} accelerator
     * @param enabled        whether {@link WebCheckBoxMenuItem} is enabled or not
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @Nullable final Object icon, @Nullable final String text, @Nullable final HotkeyData hotkey,
                                              final boolean enabled, final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( defaultStyleId, icon, text, hotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param id             {@link WebCheckBoxMenuItem} {@link StyleId}
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @NotNull final StyleId id, @Nullable final String text, final boolean selected,
                                              @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( id, defaultIcon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param id             {@link WebCheckBoxMenuItem} {@link StyleId}
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param hotkey         {@link WebCheckBoxMenuItem} accelerator
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @NotNull final StyleId id, @Nullable final String text, @Nullable final HotkeyData hotkey,
                                              final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( id, defaultIcon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param id             {@link WebCheckBoxMenuItem} {@link StyleId}
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param enabled        whether {@link WebCheckBoxMenuItem} is enabled or not
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @NotNull final StyleId id, @Nullable final String text, final boolean enabled,
                                              final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( id, defaultIcon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param id             {@link WebCheckBoxMenuItem} {@link StyleId}
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param hotkey         {@link WebCheckBoxMenuItem} accelerator
     * @param enabled        whether {@link WebCheckBoxMenuItem} is enabled or not
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @NotNull final StyleId id, @Nullable final String text, @Nullable final HotkeyData hotkey,
                                              final boolean enabled, final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( id, defaultIcon, text, hotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param id             {@link WebCheckBoxMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                              final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( id, icon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param id             {@link WebCheckBoxMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param hotkey         {@link WebCheckBoxMenuItem} accelerator
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                              @Nullable final HotkeyData hotkey, final boolean selected,
                                              @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( id, icon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param id             {@link WebCheckBoxMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param enabled        whether {@link WebCheckBoxMenuItem} is enabled or not
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                              final boolean enabled, final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addCheckItem ( id, icon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebCheckBoxMenuItem} into menu.
     *
     * @param id             {@link WebCheckBoxMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param hotkey         {@link WebCheckBoxMenuItem} accelerator
     * @param enabled        whether {@link WebCheckBoxMenuItem} is enabled or not
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    public WebCheckBoxMenuItem addCheckItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                              @Nullable final HotkeyData hotkey, final boolean enabled, final boolean selected,
                                              @Nullable final ActionListener actionListener )
    {
        final WebCheckBoxMenuItem item = createCheckItem ( id, icon, text, hotkey, enabled, selected, actionListener );
        getMenu ().add ( item );
        return item;
    }

    /**
     * Returns newly created {@link WebCheckBoxMenuItem}.
     *
     * @param id             {@link WebCheckBoxMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebCheckBoxMenuItem} text
     * @param hotkey         {@link WebCheckBoxMenuItem} accelerator
     * @param enabled        whether {@link WebCheckBoxMenuItem} is enabled or not
     * @param selected       whether {@link WebCheckBoxMenuItem} is selected or not
     * @param actionListener {@link WebCheckBoxMenuItem} {@link ActionListener}
     * @return newly created {@link WebCheckBoxMenuItem}
     */
    @NotNull
    protected WebCheckBoxMenuItem createCheckItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                                    @Nullable final HotkeyData hotkey, final boolean enabled, final boolean selected,
                                                    @Nullable final ActionListener actionListener )
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
        if ( isGroupOpen () )
        {
            group ( item );
        }
        return item;
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @Nullable final String text, final boolean selected,
                                                 @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, defaultIcon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param hotkey         {@link WebRadioButtonMenuItem} accelerator
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @Nullable final String text, @Nullable final HotkeyData hotkey, final boolean selected,
                                                 @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, defaultIcon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param enabled        whether {@link WebRadioButtonMenuItem} is enabled or not
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @Nullable final String text, final boolean enabled, final boolean selected,
                                                 @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, defaultIcon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param hotkey         {@link WebRadioButtonMenuItem} accelerator
     * @param enabled        whether {@link WebRadioButtonMenuItem} is enabled or not
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @Nullable final String text, @Nullable final HotkeyData hotkey, final boolean enabled,
                                                 final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, defaultIcon, text, hotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @Nullable final Object icon, @Nullable final String text, final boolean selected,
                                                 @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, icon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param hotkey         {@link WebRadioButtonMenuItem} accelerator
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @Nullable final Object icon, @Nullable final String text,
                                                 @Nullable final HotkeyData hotkey, final boolean selected,
                                                 @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, icon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param enabled        whether {@link WebRadioButtonMenuItem} is enabled or not
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @Nullable final Object icon, @Nullable final String text, final boolean enabled,
                                                 final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, icon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param hotkey         {@link WebRadioButtonMenuItem} accelerator
     * @param enabled        whether {@link WebRadioButtonMenuItem} is enabled or not
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @Nullable final Object icon, @Nullable final String text,
                                                 @Nullable final HotkeyData hotkey, final boolean enabled, final boolean selected,
                                                 @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( defaultStyleId, icon, text, hotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param id             {@link WebRadioButtonMenuItem} {@link StyleId}
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @NotNull final StyleId id, @Nullable final String text, final boolean selected,
                                                 @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( id, defaultIcon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param id             {@link WebRadioButtonMenuItem} {@link StyleId}
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param hotkey         {@link WebRadioButtonMenuItem} accelerator
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @NotNull final StyleId id, @Nullable final String text, @Nullable final HotkeyData hotkey,
                                                 final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( id, defaultIcon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param id             {@link WebRadioButtonMenuItem} {@link StyleId}
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param enabled        whether {@link WebRadioButtonMenuItem} is enabled or not
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @NotNull final StyleId id, @Nullable final String text, final boolean enabled,
                                                 final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( id, defaultIcon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param id             {@link WebRadioButtonMenuItem} {@link StyleId}
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param hotkey         {@link WebRadioButtonMenuItem} accelerator
     * @param enabled        whether {@link WebRadioButtonMenuItem} is enabled or not
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @NotNull final StyleId id, @Nullable final String text, @Nullable final HotkeyData hotkey,
                                                 final boolean enabled, final boolean selected,
                                                 @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( id, defaultIcon, text, hotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param id             {@link WebRadioButtonMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                                 final boolean selected, @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( id, icon, text, defaultHotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param id             {@link WebRadioButtonMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param hotkey         {@link WebRadioButtonMenuItem} accelerator
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                                 @Nullable final HotkeyData hotkey, final boolean selected,
                                                 @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( id, icon, text, hotkey, defaultEnabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param id             {@link WebRadioButtonMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param enabled        whether {@link WebRadioButtonMenuItem} is enabled or not
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                                 final boolean enabled, final boolean selected,
                                                 @Nullable final ActionListener actionListener )
    {
        return addRadioItem ( id, icon, text, defaultHotkey, enabled, selected, actionListener );
    }

    /**
     * Adds {@link WebRadioButtonMenuItem} into menu.
     *
     * @param id             {@link WebRadioButtonMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param hotkey         {@link WebRadioButtonMenuItem} accelerator
     * @param enabled        whether {@link WebRadioButtonMenuItem} is enabled or not
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    public WebRadioButtonMenuItem addRadioItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                                 @Nullable final HotkeyData hotkey, final boolean enabled, final boolean selected,
                                                 @Nullable final ActionListener actionListener )
    {
        final WebRadioButtonMenuItem item = createRadioItem ( id, icon, text, hotkey, enabled, selected, actionListener );
        getMenu ().add ( item );
        return item;
    }

    /**
     * Returns newly created {@link WebRadioButtonMenuItem}.
     *
     * @param id             {@link WebRadioButtonMenuItem} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebRadioButtonMenuItem} text
     * @param hotkey         {@link WebRadioButtonMenuItem} accelerator
     * @param enabled        whether {@link WebRadioButtonMenuItem} is enabled or not
     * @param selected       whether {@link WebRadioButtonMenuItem} is selected or not
     * @param actionListener {@link WebRadioButtonMenuItem} {@link ActionListener}
     * @return newly created {@link WebRadioButtonMenuItem}
     */
    @NotNull
    protected WebRadioButtonMenuItem createRadioItem ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                                       @Nullable final HotkeyData hotkey, final boolean enabled, final boolean selected,
                                                       @Nullable final ActionListener actionListener )
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
        if ( isGroupOpen () )
        {
            group ( item );
        }
        return item;
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param text {@link WebMenu} text
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @Nullable final String text )
    {
        return addSubMenu ( defaultStyleId, defaultIcon, text, defaultEnabled, defaultAction );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param text    {@link WebMenu} text
     * @param enabled whether {@link WebMenu} is enabled or not
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @Nullable final String text, final boolean enabled )
    {
        return addSubMenu ( defaultStyleId, defaultIcon, text, enabled, defaultAction );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param text           {@link WebMenu} text
     * @param actionListener {@link WebMenu} {@link ActionListener}
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @Nullable final String text, @Nullable final ActionListener actionListener )
    {
        return addSubMenu ( defaultStyleId, defaultIcon, text, defaultEnabled, actionListener );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param text           {@link WebMenu} text
     * @param enabled        whether {@link WebMenu} is enabled or not
     * @param actionListener {@link WebMenu} {@link ActionListener}
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @Nullable final String text, final boolean enabled, @Nullable final ActionListener actionListener )
    {
        return addSubMenu ( defaultStyleId, defaultIcon, text, enabled, actionListener );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param icon either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text {@link WebMenu} text
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @Nullable final Object icon, @Nullable final String text )
    {
        return addSubMenu ( defaultStyleId, icon, text, defaultEnabled, defaultAction );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param icon    either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text    {@link WebMenu} text
     * @param enabled whether {@link WebMenu} is enabled or not
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @Nullable final Object icon, @Nullable final String text, final boolean enabled )
    {
        return addSubMenu ( defaultStyleId, icon, text, enabled, defaultAction );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenu} text
     * @param actionListener {@link WebMenu} {@link ActionListener}
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @Nullable final Object icon, @Nullable final String text,
                                      @Nullable final ActionListener actionListener )
    {
        return addSubMenu ( defaultStyleId, icon, text, defaultEnabled, actionListener );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenu} text
     * @param enabled        whether {@link WebMenu} is enabled or not
     * @param actionListener {@link WebMenu} {@link ActionListener}
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @Nullable final Object icon, @Nullable final String text, final boolean enabled,
                                      @Nullable final ActionListener actionListener )
    {
        return addSubMenu ( defaultStyleId, icon, text, enabled, actionListener );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param id   {@link WebMenu} {@link StyleId}
     * @param text {@link WebMenu} text
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @NotNull final StyleId id, @Nullable final String text )
    {
        return addSubMenu ( id, defaultIcon, text, defaultEnabled, defaultAction );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param id      {@link WebMenu} {@link StyleId}
     * @param text    {@link WebMenu} text
     * @param enabled whether {@link WebMenu} is enabled or not
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @NotNull final StyleId id, @Nullable final String text, final boolean enabled )
    {
        return addSubMenu ( id, defaultIcon, text, enabled, defaultAction );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param id             {@link WebMenu} {@link StyleId}
     * @param text           {@link WebMenu} text
     * @param actionListener {@link WebMenu} {@link ActionListener}
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @NotNull final StyleId id, @Nullable final String text,
                                      @Nullable final ActionListener actionListener )
    {
        return addSubMenu ( id, defaultIcon, text, defaultEnabled, actionListener );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param id             {@link WebMenu} {@link StyleId}
     * @param text           {@link WebMenu} text
     * @param enabled        whether {@link WebMenu} is enabled or not
     * @param actionListener {@link WebMenu} {@link ActionListener}
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @NotNull final StyleId id, @Nullable final String text, final boolean enabled,
                                      @Nullable final ActionListener actionListener )
    {
        return addSubMenu ( id, defaultIcon, text, enabled, actionListener );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param id   {@link WebMenu} {@link StyleId}
     * @param icon either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text {@link WebMenu} text
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text )
    {
        return addSubMenu ( id, icon, text, defaultEnabled, defaultAction );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param id      {@link WebMenu} {@link StyleId}
     * @param icon    either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text    {@link WebMenu} text
     * @param enabled whether {@link WebMenu} is enabled or not
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                      final boolean enabled )
    {
        return addSubMenu ( id, icon, text, enabled, defaultAction );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param id             {@link WebMenu} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenu} text
     * @param actionListener {@link WebMenu} {@link ActionListener}
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                      @Nullable final ActionListener actionListener )
    {
        return addSubMenu ( id, icon, text, defaultEnabled, actionListener );
    }

    /**
     * Adds {@link WebMenu} into this {@link AbstractMenuGenerator}.
     * Returned {@link MenuGenerator} will have the same settings as current one, but you can modify those later.
     *
     * @param id             {@link WebMenu} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenu} text
     * @param enabled        whether {@link WebMenu} is enabled or not
     * @param actionListener {@link WebMenu} {@link ActionListener}
     * @return {@link MenuGenerator} for newly created {@link WebMenu}
     */
    @NotNull
    public MenuGenerator addSubMenu ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                      final boolean enabled, @Nullable final ActionListener actionListener )
    {
        final WebMenu menu = createSubMenu ( id, icon, text, enabled, actionListener );
        getMenu ().add ( menu );

        // Creating submenu generator
        final MenuGenerator menuGenerator = new MenuGenerator ( menu );
        menuGenerator.setIconSettings ( getNearClass (), getPath (), getExtension () );
        menuGenerator.setLanguagePrefix ( getLanguagePrefix () );
        return menuGenerator;
    }

    /**
     * Returns newly created {@link WebMenu}.
     *
     * @param id             {@link WebMenu} {@link StyleId}
     * @param icon           either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @param text           {@link WebMenu} text
     * @param enabled        whether {@link WebMenu} is enabled or not
     * @param actionListener {@link WebMenu} {@link ActionListener}
     * @return newly created {@link WebMenu}
     */
    @NotNull
    protected WebMenu createSubMenu ( @NotNull final StyleId id, @Nullable final Object icon, @Nullable final String text,
                                      final boolean enabled, @Nullable final ActionListener actionListener )
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
     * Adds separator into this {@link AbstractMenuGenerator}.
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
     * @return {@link UnselectableButtonGroup} used for grouping
     */
    @NotNull
    public UnselectableButtonGroup openGroup ()
    {
        return openGroup ( false );
    }

    /**
     * Starts grouping menu items.
     * All items created after this call and before {@code closeGroup()} call will get grouped.
     *
     * @param unselectable whether items should be unselectable or not
     * @return {@link UnselectableButtonGroup} used for grouping
     */
    @NotNull
    public UnselectableButtonGroup openGroup ( final boolean unselectable )
    {
        group = new UnselectableButtonGroup ( unselectable );
        return group;
    }

    /**
     * Returns whether or not {@link UnselectableButtonGroup} is currently used for grouping.
     *
     * @return {@code true} if {@link UnselectableButtonGroup} is currently used for grouping, {@code false} otherwise
     */
    public boolean isGroupOpen ()
    {
        return group != null;
    }

    /**
     * Adds specified {@link AbstractButton} into currently used {@link UnselectableButtonGroup}.
     *
     * @param button {@link AbstractButton} to add into {@link UnselectableButtonGroup}
     * @return {@link UnselectableButtonGroup} used for grouping
     */
    @NotNull
    public UnselectableButtonGroup group ( @NotNull final AbstractButton button )
    {
        if ( group == null )
        {
            throw new UtilityException ( "Button group must be opened first" );
        }
        group.add ( button );
        return group;
    }

    /**
     * Finishes grouping menu items.
     *
     * @return {@link UnselectableButtonGroup} used for grouping
     */
    @NotNull
    public UnselectableButtonGroup closeGroup ()
    {
        if ( group == null )
        {
            throw new UtilityException ( "Button group must be opened first" );
        }
        final UnselectableButtonGroup group = this.group;
        this.group = null;
        return group;
    }

    /**
     * Returns menu {@link JComponent}.
     *
     * @return menu {@link JComponent}
     */
    @NotNull
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
     * Returns {@link Icon} for the specified source {@link Object}.
     *
     * @param icon either {@link Icon}, {@link Image}, {@link Resource}, path, {@link File} or {@link URL}
     * @return {@link Icon} for the specified source {@link Object}
     */
    @Nullable
    protected Icon getIcon ( @Nullable final Object icon )
    {
        final Icon result;
        if ( icon != null )
        {
            if ( icon instanceof Icon )
            {
                result = ( Icon ) icon;
            }
            else if ( icon instanceof Image )
            {
                result = new ImageIcon ( ( Image ) icon );
            }
            else
            {
                final Resource resource;
                if ( icon instanceof Resource )
                {
                    resource = ( Resource ) icon;
                }
                else if ( icon instanceof String )
                {
                    try
                    {
                        if ( getNearClass () != null )
                        {
                            resource = new ClassResource ( getNearClass (), getPath () + icon + getExtension () );
                        }
                        else
                        {
                            resource = new FileResource ( new File ( getPath (), icon + getExtension () ) );
                        }
                    }
                    catch ( final Exception e )
                    {
                        throw new UtilityException ( "Unable to find menu icon for path: " + getPath () + icon + getExtension (), e );
                    }
                }
                else if ( icon instanceof File )
                {
                    resource = new FileResource ( ( File ) icon );
                }
                else if ( icon instanceof URL )
                {
                    resource = new UrlResource ( ( URL ) icon );
                }
                else
                {
                    throw new UtilityException ( "Unknown icon object type provided: " + icon );
                }
                result = ImageUtils.loadImageIcon ( resource );
            }
        }
        else
        {
            result = null;
        }
        return result;
    }
}