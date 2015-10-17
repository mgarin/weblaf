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

import com.alee.laf.grouping.GroupPane;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.language.LanguageContainerMethods;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.LanguageUtils;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.style.StyleId;
import com.alee.utils.EventUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.WindowUtils;
import com.alee.managers.style.PaddingSupport;
import com.alee.managers.style.Styleable;
import com.alee.utils.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.util.List;

/**
 * This JFrame extension class provides some additional methods and options to manipulate frame behavior.
 *
 * @author Mikle Garin
 */

public class WebFrame extends JFrame
        implements Styleable, PaddingSupport, WindowEventMethods, LanguageMethods, LanguageContainerMethods, SettingsMethods,
        WindowMethods<WebFrame>
{
    /**
     * Whether should close frame on focus loss or not.
     */
    protected boolean closeOnFocusLoss = false;

    /**
     * Window focus tracker.
     */
    protected DefaultFocusTracker focusTracker;

    /**
     * Constructs a new frame that is initially invisible.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     */
    public WebFrame ()
    {
        super ();
        initialize ( null );
    }

    /**
     * Creates a {@code Frame} in the specified {@code GraphicsConfiguration} of a screen device and a blank title.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param gc the {@code GraphicsConfiguration} that is used to construct the new {@code Frame};
     *           if {@code gc} is {@code null}, the system default {@code GraphicsConfiguration} is assumed
     */
    public WebFrame ( final GraphicsConfiguration gc )
    {
        super ( gc );
        initialize ( null );
    }

    /**
     * Creates a new, initially invisible {@code Frame} with the specified title.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param title the title for the frame
     */
    public WebFrame ( final String title )
    {
        super ( LanguageUtils.getInitialText ( title ) );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( null );
    }

    /**
     * Creates a {@code JFrame} with the specified title and the specified {@code GraphicsConfiguration} of a screen device.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param title the title to be displayed in the frame's border. A {@code null} value is treated as an empty string, ""
     * @param gc    the {@code GraphicsConfiguration} that is used to construct the new {@code JFrame} with;
     *              if {@code gc} is {@code null}, the system default {@code GraphicsConfiguration} is assumed
     */
    public WebFrame ( final String title, final GraphicsConfiguration gc )
    {
        super ( LanguageUtils.getInitialText ( title ), gc );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( null );
    }

    /**
     * Constructs a new frame that is initially invisible.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param id style ID
     */
    public WebFrame ( final StyleId id )
    {
        super ();
        initialize ( id );
    }

    /**
     * Creates a {@code Frame} in the specified {@code GraphicsConfiguration} of a screen device and a blank title.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param id style ID
     * @param gc the {@code GraphicsConfiguration} that is used to construct the new {@code Frame};
     *           if {@code gc} is {@code null}, the system default {@code GraphicsConfiguration} is assumed
     */
    public WebFrame ( final StyleId id, final GraphicsConfiguration gc )
    {
        super ( gc );
        initialize ( id );
    }

    /**
     * Creates a new, initially invisible {@code Frame} with the specified title.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param id    style ID
     * @param title the title for the frame
     */
    public WebFrame ( final StyleId id, final String title )
    {
        super ( LanguageUtils.getInitialText ( title ) );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( id );
    }

    /**
     * Creates a {@code JFrame} with the specified title and the specified {@code GraphicsConfiguration} of a screen device.
     * <p/>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param id    style ID
     * @param title the title to be displayed in the frame's border. A {@code null} value is treated as an empty string, ""
     * @param gc    the {@code GraphicsConfiguration} that is used to construct the new {@code JFrame} with;
     *              if {@code gc} is {@code null}, the system default {@code GraphicsConfiguration} is assumed
     */
    public WebFrame ( final StyleId id, final String title, final GraphicsConfiguration gc )
    {
        super ( LanguageUtils.getInitialText ( title ), gc );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( id );
    }

    /**
     * Additional initialization of WebFrame settings.
     *
     * @param id initial style ID
     */
    protected void initialize ( final StyleId id )
    {
        // Updating base settings
        SwingUtils.setOrientation ( this );

        // Installing root pane style
        if ( id != null )
        {
            setStyleId ( id );
        }

        // Adding focus tracker for this frame
        // It is stored into a separate field to avoid its disposal from memory
        focusTracker = new DefaultFocusTracker ( true )
        {
            @Override
            public boolean isTrackingEnabled ()
            {
                return closeOnFocusLoss;
            }

            @Override
            public void focusChanged ( final boolean focused )
            {
                if ( closeOnFocusLoss && WebFrame.this.isShowing () && !focused )
                {
                    setVisible ( false );
                }
            }
        };
        FocusManager.addFocusTracker ( this, focusTracker );
    }

    /**
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    protected WebRootPaneUI getWebUI ()
    {
        return ( WebRootPaneUI ) getRootPane ().getUI ();
    }

    @Override
    public StyleId getStyleId ()
    {
        return ( ( WebRootPaneUI ) getRootPane ().getUI () ).getStyleId ();
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return ( ( WebRootPaneUI ) getRootPane ().getUI () ).setStyleId ( id );
    }

    @Override
    public Insets getPadding ()
    {
        return ( ( WebRootPaneUI ) getRootPane ().getUI () ).getPadding ();
    }

    /**
     * Sets new padding.
     *
     * @param padding new padding
     */
    public void setPadding ( final int padding )
    {
        setPadding ( padding, padding, padding, padding );
    }

    /**
     * Sets new padding.
     *
     * @param top    new top padding
     * @param left   new left padding
     * @param bottom new bottom padding
     * @param right  new right padding
     */
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        setPadding ( new Insets ( top, left, bottom, right ) );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        ( ( WebRootPaneUI ) getRootPane ().getUI () ).setPadding ( padding );
    }

    /**
     * Returns whether should close frame on focus loss or not.
     *
     * @return true if should close frame on focus loss, false otherwise
     */
    public boolean isCloseOnFocusLoss ()
    {
        return closeOnFocusLoss;
    }

    /**
     * Sets whether should close frame on focus loss or not.
     *
     * @param closeOnFocusLoss whether should close frame on focus loss or not
     */
    public void setCloseOnFocusLoss ( final boolean closeOnFocusLoss )
    {
        this.closeOnFocusLoss = closeOnFocusLoss;
    }

    /**
     * Returns focusable children that don't force frame to close even if it set to close on focus loss.
     *
     * @return focusable children that don't force frame to close even if it set to close on focus loss
     */
    public List<Component> getFocusableChildren ()
    {
        return focusTracker.getCustomChildren ();
    }

    /**
     * Adds focusable child that won't force frame to close even if it set to close on focus loss.
     *
     * @param child focusable child that won't force frame to close even if it set to close on focus loss
     */
    public void addFocusableChild ( final Component child )
    {
        focusTracker.addCustomChild ( child );
    }

    /**
     * Removes focusable child that doesn't force frame to close even if it set to close on focus loss.
     *
     * @param child focusable child that doesn't force frame to close even if it set to close on focus loss
     */
    public void removeFocusableChild ( final Component child )
    {
        focusTracker.removeCustomChild ( child );
    }

    public boolean isDrawWatermark ()
    {
        return getWebRootPaneUI ().isDrawWatermark ();
    }

    public void setDrawWatermark ( final boolean drawWatermark )
    {
        getWebRootPaneUI ().setDrawWatermark ( drawWatermark );
    }

    public ImageIcon getWatermark ()
    {
        return getWebRootPaneUI ().getWatermark ();
    }

    public void setWatermark ( final ImageIcon watermark )
    {
        getWebRootPaneUI ().setWatermark ( watermark );
    }

    public int getMaxTitleWidth ()
    {
        return getWebRootPaneUI ().getMaxTitleWidth ();
    }

    public void setMaxTitleWidth ( final int width )
    {
        getWebRootPaneUI ().setMaxTitleWidth ( width );
    }

    public String getEmptyTitleText ()
    {
        return getWebRootPaneUI ().getEmptyTitleText ();
    }

    public void setEmptyTitleText ( final String text )
    {
        getWebRootPaneUI ().setEmptyTitleText ( text );
    }

    public JComponent getTitleComponent ()
    {
        return getWebRootPaneUI ().getTitleComponent ();
    }

    public void setTitleComponent ( final JComponent titleComponent )
    {
        getWebRootPaneUI ().setTitleComponent ( titleComponent );
    }

    public GroupPane getWindowButtons ()
    {
        return getWebRootPaneUI ().getWindowButtons ();
    }

    public WebResizeCorner getResizeCorner ()
    {
        return getWebRootPaneUI ().getResizeCorner ();
    }

    public boolean isShowResizeCorner ()
    {
        return getWebRootPaneUI ().isShowResizeCorner ();
    }

    public void setShowResizeCorner ( final boolean showResizeCorner )
    {
        getWebRootPaneUI ().setShowResizeCorner ( showResizeCorner );
    }

    public boolean isShowTitleComponent ()
    {
        return getWebRootPaneUI ().isShowTitleComponent ();
    }

    public void setShowTitleComponent ( final boolean showTitleComponent )
    {
        getWebRootPaneUI ().setShowTitleComponent ( showTitleComponent );
    }

    public boolean isShowWindowButtons ()
    {
        return getWebRootPaneUI ().isShowWindowButtons ();
    }

    public void setShowWindowButtons ( final boolean showWindowButtons )
    {
        getWebRootPaneUI ().setShowWindowButtons ( showWindowButtons );
    }

    public boolean isShowMinimizeButton ()
    {
        return getWebRootPaneUI ().isShowMinimizeButton ();
    }

    public void setShowMinimizeButton ( final boolean showMinimizeButton )
    {
        getWebRootPaneUI ().setShowMinimizeButton ( showMinimizeButton );
    }

    public boolean isShowMaximizeButton ()
    {
        return getWebRootPaneUI ().isShowMaximizeButton ();
    }

    public void setShowMaximizeButton ( final boolean showMaximizeButton )
    {
        getWebRootPaneUI ().setShowMaximizeButton ( showMaximizeButton );
    }

    public boolean isShowCloseButton ()
    {
        return getWebRootPaneUI ().isShowCloseButton ();
    }

    public void setShowCloseButton ( final boolean showCloseButton )
    {
        getWebRootPaneUI ().setShowCloseButton ( showCloseButton );
    }

    public boolean isGroupButtons ()
    {
        return getWebRootPaneUI ().isGroupButtons ();
    }

    public void setGroupButtons ( final boolean groupButtons )
    {
        getWebRootPaneUI ().setGroupButtons ( groupButtons );
    }

    public boolean isAttachButtons ()
    {
        return getWebRootPaneUI ().isAttachButtons ();
    }

    public void setAttachButtons ( final boolean attachButtons )
    {
        getWebRootPaneUI ().setAttachButtons ( attachButtons );
    }

    public boolean isShowMenuBar ()
    {
        return getWebRootPaneUI ().isShowMenuBar ();
    }

    public void setShowMenuBar ( final boolean showMenuBar )
    {
        getWebRootPaneUI ().setShowMenuBar ( showMenuBar );
    }

    public WebRootPaneUI getWebRootPaneUI ()
    {
        return ( WebRootPaneUI ) super.getRootPane ().getUI ();
    }

    @Override
    public WindowAdapter onClosing ( final WindowEventRunnable runnable )
    {
        return EventUtils.onClosing ( this, runnable );
    }

    @Override
    public WindowCloseAdapter onClose ( final ComponentEventRunnable runnable )
    {
        return EventUtils.onClose ( this, runnable );
    }

    /**
     * Language methods
     */

    @Override
    public void setLanguage ( final String key, final Object... data )
    {
        LanguageManager.registerComponent ( getRootPane (), key, data );
    }

    @Override
    public void updateLanguage ( final Object... data )
    {
        LanguageManager.updateComponent ( getRootPane (), data );
    }

    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        LanguageManager.updateComponent ( getRootPane (), key, data );
    }

    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( getRootPane () );
    }

    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( getRootPane () );
    }

    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( getRootPane (), updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( getRootPane () );
    }

    /**
     * Language container methods
     */

    @Override
    public void setLanguageContainerKey ( final String key )
    {
        LanguageManager.registerLanguageContainer ( this, key );
    }

    @Override
    public void removeLanguageContainerKey ()
    {
        LanguageManager.unregisterLanguageContainer ( this );
    }

    @Override
    public String getLanguageContainerKey ()
    {
        return LanguageManager.getLanguageContainerKey ( this );
    }

    /**
     * Settings methods
     */

    @Override
    public void registerSettings ( final String key )
    {
        SettingsManager.registerComponent ( getRootPane (), key );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( getRootPane (), key, defaultValueClass );
    }

    @Override
    public void registerSettings ( final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( getRootPane (), key, defaultValue );
    }

    @Override
    public void registerSettings ( final String group, final String key )
    {
        SettingsManager.registerComponent ( getRootPane (), group, key );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass )
    {
        SettingsManager.registerComponent ( getRootPane (), group, key, defaultValueClass );
    }

    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue )
    {
        SettingsManager.registerComponent ( getRootPane (), group, key, defaultValue );
    }

    @Override
    public void registerSettings ( final String key, final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( getRootPane (), key, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( getRootPane (), key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( getRootPane (), key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public <T extends DefaultValue> void registerSettings ( final String group, final String key, final Class<T> defaultValueClass,
                                                            final boolean loadInitialSettings, final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( getRootPane (), group, key, defaultValueClass, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final String group, final String key, final Object defaultValue, final boolean loadInitialSettings,
                                   final boolean applySettingsChanges )
    {
        SettingsManager.registerComponent ( getRootPane (), group, key, defaultValue, loadInitialSettings, applySettingsChanges );
    }

    @Override
    public void registerSettings ( final SettingsProcessor settingsProcessor )
    {
        SettingsManager.registerComponent ( getRootPane (), settingsProcessor );
    }

    @Override
    public void unregisterSettings ()
    {
        SettingsManager.unregisterComponent ( getRootPane () );
    }

    @Override
    public void loadSettings ()
    {
        SettingsManager.loadComponentSettings ( getRootPane () );
    }

    @Override
    public void saveSettings ()
    {
        SettingsManager.saveComponentSettings ( getRootPane () );
    }

    /**
     * Window methods.
     */

    @Override
    public WebFrame setWindowOpaque ( final boolean opaque )
    {
        return WindowUtils.setWindowOpaque ( this, opaque );
    }

    @Override
    public boolean isWindowOpaque ()
    {
        return WindowUtils.isWindowOpaque ( this );
    }

    @Override
    public WebFrame setWindowOpacity ( final float opacity )
    {
        return WindowUtils.setWindowOpacity ( this, opacity );
    }

    @Override
    public float getWindowOpacity ()
    {
        return WindowUtils.getWindowOpacity ( this );
    }

    @Override
    public WebFrame center ()
    {
        return WindowUtils.center ( this );
    }

    @Override
    public WebFrame center ( final Component relativeTo )
    {
        return WindowUtils.center ( this, relativeTo );
    }

    @Override
    public WebFrame center ( final int width, final int height )
    {
        return WindowUtils.center ( this, width, height );
    }

    @Override
    public WebFrame center ( final Component relativeTo, final int width, final int height )
    {
        return WindowUtils.center ( this, relativeTo, width, height );
    }

    @Override
    public WebFrame packToWidth ( final int width )
    {
        return WindowUtils.packToWidth ( this, width );
    }

    @Override
    public WebFrame packToHeight ( final int height )
    {
        return WindowUtils.packToHeight ( this, height );
    }

    @Override
    public WebFrame packAndCenter ()
    {
        return WindowUtils.packAndCenter ( this );
    }

    @Override
    public WebFrame packAndCenter ( final boolean animate )
    {
        return WindowUtils.packAndCenter ( this, animate );
    }
}