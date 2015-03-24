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

package com.alee.laf.rootpane;

import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.utils.EventUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.WindowUtils;
import com.alee.utils.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.util.List;

/**
 * This JWindow extenstion class provides some additional methods and options to manipulate window behavior.
 *
 * @author Mikle Garin
 */

public class WebWindow extends JWindow implements WindowEventMethods, LanguageContainerMethods, SettingsMethods, WindowMethods<WebWindow>
{
    /**
     * Whether should close window on focus loss or not.
     */
    protected boolean closeOnFocusLoss = false;

    /**
     * Window focus tracker.
     */
    protected DefaultFocusTracker focusTracker;

    /**
     * Creates a window with no specified owner. This window will not be focusable.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     */
    public WebWindow ()
    {
        super ();
        initialize ();
    }

    /**
     * Creates a window with the specified {@code GraphicsConfiguration} of a screen device. This window will not be focusable.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param gc the {@code GraphicsConfiguration} that is used to construct the new window with; if gc is {@code null},
     *           the system default {@code GraphicsConfiguration} is assumed
     */
    public WebWindow ( final GraphicsConfiguration gc )
    {
        super ( gc );
        initialize ();
    }

    /**
     * Creates a window with the specified owner frame.
     * If {@code owner} is {@code null}, the shared owner will be used and this window will not be focusable.
     * Also, this window will not be focusable unless its owner is showing on the screen.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the frame from which the window is displayed
     */
    public WebWindow ( final Frame owner )
    {
        super ( owner );
        initialize ();
    }

    /**
     * Creates a window with the owner window from the specified component.
     * This window will not be focusable unless its owner is showing on the screen.
     * If {@code owner} is {@code null}, the shared owner will be used and this window will not be focusable.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the componnt from which parent window this window is displayed
     */
    public WebWindow ( final Component owner )
    {
        super ( SwingUtils.getWindowAncestor ( owner ) );
        initialize ();
    }

    /**
     * Creates a window with the specified owner window. This window will not be focusable unless its owner is showing on the screen.
     * If {@code owner} is {@code null}, the shared owner will be used and this window will not be focusable.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the window from which the window is displayed
     */
    public WebWindow ( final Window owner )
    {
        super ( owner );
        initialize ();
    }

    /**
     * Creates a window with the specified owner window and {@code GraphicsConfiguration} of a screen device.
     * If {@code owner} is {@code null}, the shared owner will be used and this window will not be focusable.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the window from which the window is displayed
     * @param gc    the {@code GraphicsConfiguration} that is used to construct the new window with; if gc is {@code null},
     *              the system default {@code GraphicsConfiguration} is assumed, unless {@code owner} is also null, in which
     *              case the {@code GraphicsConfiguration} from the shared owner frame will be used
     */
    public WebWindow ( final Window owner, final GraphicsConfiguration gc )
    {
        super ( owner, gc );
        initialize ();
    }

    /**
     * Additional initializtion of WebWindow settings.
     */
    protected void initialize ()
    {
        setFocusable ( true );
        SwingUtils.setOrientation ( this );

        // Adding focus tracker for this window
        // It is stored into a separate field to avoid its disposal from memory
        focusTracker = new DefaultFocusTracker ( true )
        {
            @Override
            public boolean isTrackingEnabled ()
            {
                return isShowing () && closeOnFocusLoss;
            }

            @Override
            public void focusChanged ( final boolean focused )
            {
                if ( closeOnFocusLoss && isShowing () && !focused )
                {
                    dispose ();
                }
            }
        };
        FocusManager.addFocusTracker ( this, focusTracker );
    }

    /**
     * Returns whether should close window on focus loss or not.
     *
     * @return true if should close window on focus loss, false otherwise
     */
    public boolean isCloseOnFocusLoss ()
    {
        return closeOnFocusLoss;
    }

    /**
     * Sets whether should close window on focus loss or not.
     *
     * @param closeOnFocusLoss whether should close window on focus loss or not
     */
    public void setCloseOnFocusLoss ( final boolean closeOnFocusLoss )
    {
        this.closeOnFocusLoss = closeOnFocusLoss;
    }

    /**
     * Returns focusable childs that don't force window to close even if it set to close on focus loss.
     *
     * @return focusable childs that don't force window to close even if it set to close on focus loss
     */
    public List<Component> getFocusableChilds ()
    {
        return focusTracker.getCustomChildren ();
    }

    /**
     * Adds focusable child that won't force window to close even if it set to close on focus loss.
     *
     * @param child focusable child that won't force window to close even if it set to close on focus loss
     */
    public void addFocusableChild ( final Component child )
    {
        focusTracker.addCustomChild ( child );
    }

    /**
     * Removes focusable child that doesn't force window to close even if it set to close on focus loss.
     *
     * @param child focusable child that doesn't force window to close even if it set to close on focus loss
     */
    public void removeFocusableChild ( final Component child )
    {
        focusTracker.removeCustomChild ( child );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WindowAdapter onClosing ( final WindowEventRunnable runnable )
    {
        return EventUtils.onClosing ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WindowCloseAdapter onClose ( final ComponentEventRunnable runnable )
    {
        return EventUtils.onClose ( this, runnable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLanguageContainerKey ( final String key )
    {
        LanguageManager.registerLanguageContainer ( this, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeLanguageContainerKey ()
    {
        LanguageManager.unregisterLanguageContainer ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLanguageContainerKey ()
    {
        return LanguageManager.getLanguageContainerKey ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key )
    {
        SettingsManager.registerComponent ( getRootPane (), key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( getRootPane (), key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( getRootPane (), key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String group, final String key )
    {
        SettingsManager.registerComponent ( getRootPane (), group, key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( getRootPane (), group, key, defaultValueClass );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( getRootPane (), group, key, defaultValue );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key, final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( getRootPane (), key, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( getRootPane (), key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( getRootPane (), key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( getRootPane (), group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( getRootPane (), group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerSettings ( final SettingsProcessor settingsProcessor )
    {
        SettingsManager.registerComponent ( getRootPane (), settingsProcessor );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterSettings ()
    {
        SettingsManager.unregisterComponent ( getRootPane () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadSettings ()
    {
        SettingsManager.loadComponentSettings ( getRootPane () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSettings ()
    {
        SettingsManager.saveComponentSettings ( getRootPane () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow setWindowOpaque ( final boolean opaque )
    {
        return WindowUtils.setWindowOpaque ( this, opaque );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWindowOpaque ()
    {
        return WindowUtils.isWindowOpaque ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow setWindowOpacity ( final float opacity )
    {
        return WindowUtils.setWindowOpacity ( this, opacity );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getWindowOpacity ()
    {
        return WindowUtils.getWindowOpacity ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow center ()
    {
        return WindowUtils.center ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow center ( final Component relativeTo )
    {
        return WindowUtils.center ( this, relativeTo );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow center ( final int width, final int height )
    {
        return WindowUtils.center ( this, width, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow center ( final Component relativeTo, final int width, final int height )
    {
        return WindowUtils.center ( this, relativeTo, width, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow packToWidth ( final int width )
    {
        return WindowUtils.packToWidth ( this, width );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow packToHeight ( final int height )
    {
        return WindowUtils.packToHeight ( this, height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow packAndCenter ()
    {
        return WindowUtils.packAndCenter ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebWindow packAndCenter ( final boolean animate )
    {
        return WindowUtils.packAndCenter ( this, animate );
    }
}