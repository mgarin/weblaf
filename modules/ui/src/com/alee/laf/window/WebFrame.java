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

package com.alee.laf.window;

import com.alee.laf.grouping.GroupPane;
import com.alee.laf.rootpane.WebRootPane;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.LanguageUtils;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.settings.DefaultValue;
import com.alee.managers.settings.SettingsManager;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.style.*;
import com.alee.painter.Paintable;
import com.alee.painter.Painter;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.extensions.ComponentEventRunnable;
import com.alee.utils.swing.extensions.WindowCloseAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.util.List;
import java.util.Map;

/**
 * {@link JFrame} extension class.
 * It contains various useful methods to simplify core component usage.
 * <p/>
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application L&amp;F as this component will use Web-UI in any case.
 *
 * @param <T> frame type
 * @author Mikle Garin
 * @see JFrame
 * @see WebRootPaneUI
 * @see com.alee.laf.rootpane.RootPanePainter
 */

public class WebFrame<T extends WebFrame<T>> extends JFrame
        implements Styleable, Paintable, PaddingSupport, WindowEventMethods, LanguageMethods, SettingsMethods, WindowMethods<T>
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
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     */
    public WebFrame ()
    {
        this ( StyleId.auto );
    }

    /**
     * Creates a {@code Frame} in the specified {@code GraphicsConfiguration} of a screen device and a blank title.
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param gc the {@code GraphicsConfiguration} that is used to construct the new {@code Frame};
     *           if {@code gc} is {@code null}, the system default {@code GraphicsConfiguration} is assumed
     */
    public WebFrame ( final GraphicsConfiguration gc )
    {
        this ( StyleId.auto, gc );
    }

    /**
     * Creates a new, initially invisible {@code Frame} with the specified title.
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param title the title for the frame
     */
    public WebFrame ( final String title )
    {
        this ( StyleId.auto, title );
    }

    /**
     * Creates a {@code JFrame} with the specified title and the specified {@code GraphicsConfiguration} of a screen device.
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param title the title to be displayed in the frame's border. A {@code null} value is treated as an empty string, ""
     * @param gc    the {@code GraphicsConfiguration} that is used to construct the new {@code JFrame} with;
     *              if {@code gc} is {@code null}, the system default {@code GraphicsConfiguration} is assumed
     */
    public WebFrame ( final String title, final GraphicsConfiguration gc )
    {
        this ( StyleId.auto, title, gc );
    }

    /**
     * Constructs a new frame that is initially invisible.
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param id style ID
     */
    public WebFrame ( final StyleId id )
    {
        this ( id, "" );
    }

    /**
     * Creates a {@code Frame} in the specified {@code GraphicsConfiguration} of a screen device and a blank title.
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param id style ID
     * @param gc the {@code GraphicsConfiguration} that is used to construct the new {@code Frame};
     *           if {@code gc} is {@code null}, the system default {@code GraphicsConfiguration} is assumed
     */
    public WebFrame ( final StyleId id, final GraphicsConfiguration gc )
    {
        this ( id, "", gc );
    }

    /**
     * Creates a new, initially invisible {@code Frame} with the specified title.
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param id    style ID
     * @param title the title for the frame
     */
    public WebFrame ( final StyleId id, final String title )
    {
        this ( id, title, null );
    }

    /**
     * Creates a {@code JFrame} with the specified title and the specified {@code GraphicsConfiguration} of a screen device.
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
        initialize ( id, title );
    }

    @Override
    protected void frameInit ()
    {
        // Disabling default initialization to optimize startup performance
    }

    /**
     * Additional initialization of WebFrame settings.
     *
     * @param id    initial style ID
     * @param title frame title
     */
    protected void initialize ( final StyleId id, final String title )
    {
        // Default frame initialization
        enableEvents ( AWTEvent.KEY_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK );
        setLocale ( JComponent.getDefaultLocale () );
        setRootPane ( createRootPane () );
        setRootPaneCheckingEnabled ( true );
        ProprietaryUtils.checkAndSetPolicy ( this );

        // Additional settings
        SwingUtils.setOrientation ( this );

        // Installing root pane style
        setStyleId ( id );

        // Language updater
        if ( title != null )
        {
            LanguageUtils.registerInitialLanguage ( this, title );
        }

        // Adding focus tracker for this frame
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
                if ( closeOnFocusLoss && WebFrame.this.isShowing () && !focused )
                {
                    setVisible ( false );
                }
            }
        };
        FocusManager.addFocusTracker ( this, focusTracker );
    }

    @Override
    protected JRootPane createRootPane ()
    {
        return new WebFrameRootPane ();
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

    /**
     * Returns window title component.
     *
     * @return window title component
     */
    public JComponent getTitleComponent ()
    {
        return getRootPaneWebUI ().getTitleComponent ();
    }

    /**
     * Sets window title component.
     *
     * @param title new window title component
     */
    public void setTitleComponent ( final JComponent title )
    {
        getRootPaneWebUI ().setTitleComponent ( title );
    }

    /**
     * Returns window buttons panel.
     *
     * @return window buttons panel
     */
    public GroupPane getButtonsPanel ()
    {
        return getRootPaneWebUI ().getButtonsPanel ();
    }

    /**
     * Returns whether or not window title component should be displayed.
     *
     * @return true if window title component should be displayed, false otherwise
     */
    public boolean isDisplayTitleComponent ()
    {
        return getRootPaneWebUI ().isDisplayTitleComponent ();
    }

    /**
     * Sets whether or not window title component should be displayed.
     *
     * @param display whether or not window title component should be displayed
     */
    public void setDisplayTitleComponent ( final boolean display )
    {
        getRootPaneWebUI ().setDisplayTitleComponent ( display );
    }

    /**
     * Returns whether or not window buttons should be displayed.
     *
     * @return true if window buttons should be displayed, false otherwise
     */
    public boolean isDisplayWindowButtons ()
    {
        return getRootPaneWebUI ().isDisplayWindowButtons ();
    }

    /**
     * Sets whether or not window buttons should be displayed.
     *
     * @param display whether or not window buttons should be displayed
     */
    public void setDisplayWindowButtons ( final boolean display )
    {
        getRootPaneWebUI ().setDisplayWindowButtons ( display );
    }

    /**
     * Returns whether or not window minimize button should be displayed.
     *
     * @return true if window minimize button should be displayed, false otherwise
     */
    public boolean isDisplayMinimizeButton ()
    {
        return getRootPaneWebUI ().isDisplayMinimizeButton ();
    }

    /**
     * Sets whether or not window minimize button should be displayed.
     *
     * @param display whether or not window minimize button should be displayed
     */
    public void setDisplayMinimizeButton ( final boolean display )
    {
        getRootPaneWebUI ().setDisplayMinimizeButton ( display );
    }

    /**
     * Returns whether or not window maximize button should be displayed.
     *
     * @return true if window maximize button should be displayed, false otherwise
     */
    public boolean isDisplayMaximizeButton ()
    {
        return getRootPaneWebUI ().isDisplayMaximizeButton ();
    }

    /**
     * Sets whether or not window maximize button should be displayed.
     *
     * @param display whether or not window maximize button should be displayed
     */
    public void setDisplayMaximizeButton ( final boolean display )
    {
        getRootPaneWebUI ().setDisplayMaximizeButton ( display );
    }

    /**
     * Returns whether or not window close button should be displayed.
     *
     * @return true if window close button should be displayed, false otherwise
     */
    public boolean isDisplayCloseButton ()
    {
        return getRootPaneWebUI ().isDisplayCloseButton ();
    }

    /**
     * Sets whether or not window close button should be displayed.
     *
     * @param display whether or not window close button should be displayed
     */
    public void setDisplayCloseButton ( final boolean display )
    {
        getRootPaneWebUI ().setDisplayCloseButton ( display );
    }

    /**
     * Returns whether or not menu bar should be displayed.
     *
     * @return true if menu bar should be displayed, false otherwise
     */
    public boolean isDisplayMenuBar ()
    {
        return getRootPaneWebUI ().isDisplayMenuBar ();
    }

    /**
     * Sets whether or not menu bar should be displayed.
     *
     * @param display whether or not menu bar should be displayed
     */
    public void setDisplayMenuBar ( final boolean display )
    {
        getRootPaneWebUI ().setDisplayMenuBar ( display );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return JFrame.isDefaultLookAndFeelDecorated () ? StyleId.frameDecorated : StyleId.frame;
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( getRootPane () );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( getRootPane (), id );
    }

    @Override
    public StyleId resetStyleId ()
    {
        return StyleManager.resetStyleId ( getRootPane () );
    }

    @Override
    public Skin getSkin ()
    {
        return StyleManager.getSkin ( getRootPane () );
    }

    @Override
    public Skin setSkin ( final Skin skin )
    {
        return StyleManager.setSkin ( getRootPane (), skin );
    }

    @Override
    public Skin setSkin ( final Skin skin, final boolean recursively )
    {
        return StyleManager.setSkin ( getRootPane (), skin, recursively );
    }

    @Override
    public Skin resetSkin ()
    {
        return StyleManager.resetSkin ( getRootPane () );
    }

    @Override
    public void addStyleListener ( final StyleListener listener )
    {
        StyleManager.addStyleListener ( getRootPane (), listener );
    }

    @Override
    public void removeStyleListener ( final StyleListener listener )
    {
        StyleManager.removeStyleListener ( getRootPane (), listener );
    }

    @Override
    public Map<String, Painter> getCustomPainters ()
    {
        return StyleManager.getCustomPainters ( getRootPane () );
    }

    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( getRootPane () );
    }

    @Override
    public Painter getCustomPainter ( final String id )
    {
        return StyleManager.getCustomPainter ( getRootPane (), id );
    }

    @Override
    public Painter setCustomPainter ( final Painter painter )
    {
        return StyleManager.setCustomPainter ( getRootPane (), painter );
    }

    @Override
    public Painter setCustomPainter ( final String id, final Painter painter )
    {
        return StyleManager.setCustomPainter ( getRootPane (), id, painter );
    }

    @Override
    public boolean resetPainter ()
    {
        return StyleManager.resetPainter ( getRootPane () );
    }

    @Override
    public Insets getPadding ()
    {
        return getRootPaneWebUI ().getPadding ();
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
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    protected WebRootPaneUI getWebUI ()
    {
        return ( WebRootPaneUI ) getRootPane ().getUI ();
    }

    /**
     * Returns Web-UI applied to root pane used by this frame.
     *
     * @return Web-UI applied to root pane used by this frame
     */
    protected WebRootPaneUI getRootPaneWebUI ()
    {
        return ( WebRootPaneUI ) getRootPane ().getUI ();
    }

    @Override
    public WindowAdapter onClosing ( final WindowEventRunnable runnable )
    {
        return WindowEventMethodsImpl.onClosing ( this, runnable );
    }

    @Override
    public WindowCloseAdapter onClose ( final ComponentEventRunnable runnable )
    {
        return WindowEventMethodsImpl.onClose ( this, runnable );
    }

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

    @Override
    public void registerSettings ( final String key )
    {
        SettingsManager.registerComponent ( getRootPane (), key );
    }

    @Override
    public <V extends DefaultValue> void registerSettings ( final String key, final Class<V> defaultValueClass )
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
    public <V extends DefaultValue> void registerSettings ( final String group, final String key, final Class<V> defaultValueClass )
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
    public <V extends DefaultValue> void registerSettings ( final String key, final Class<V> defaultValueClass,
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
    public <V extends DefaultValue> void registerSettings ( final String group, final String key, final Class<V> defaultValueClass,
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

    @Override
    public boolean isWindowOpaque ()
    {
        return WindowMethodsImpl.isWindowOpaque ( this );
    }

    @Override
    public T setWindowOpaque ( final boolean opaque )
    {
        return WindowMethodsImpl.setWindowOpaque ( this, opaque );
    }

    @Override
    public float getWindowOpacity ()
    {
        return WindowMethodsImpl.getWindowOpacity ( this );
    }

    @Override
    public T setWindowOpacity ( final float opacity )
    {
        return WindowMethodsImpl.setWindowOpacity ( this, opacity );
    }

    @Override
    public T center ()
    {
        return WindowMethodsImpl.center ( this );
    }

    @Override
    public T center ( final Component relativeTo )
    {
        return WindowMethodsImpl.center ( this, relativeTo );
    }

    @Override
    public T center ( final int width, final int height )
    {
        return WindowMethodsImpl.center ( this, width, height );
    }

    @Override
    public T center ( final Component relativeTo, final int width, final int height )
    {
        return WindowMethodsImpl.center ( this, relativeTo, width, height );
    }

    @Override
    public T packToWidth ( final int width )
    {
        return WindowMethodsImpl.packToWidth ( this, width );
    }

    @Override
    public T packToHeight ( final int height )
    {
        return WindowMethodsImpl.packToHeight ( this, height );
    }

    /**
     * Custom root pane for this {@link WebFrame}.
     * It is required to provide undecorated root pane style ID to avoid issues with further style updates.
     * It also provides default frame style ID instead of default root pane style ID.
     */
    public class WebFrameRootPane extends WebRootPane
    {
        /**
         * Constructs new root pane for this {@link WebFrame}.
         */
        public WebFrameRootPane ()
        {
            super ( StyleId.rootpane );
        }

        @Override
        public StyleId getDefaultStyleId ()
        {
            return WebFrame.this.getDefaultStyleId ();
        }
    }
}