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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.rootpane.WRootPaneUI;
import com.alee.laf.rootpane.WebRootPane;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.language.*;
import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsMethods;
import com.alee.managers.settings.SettingsProcessor;
import com.alee.managers.settings.UISettingsManager;
import com.alee.managers.style.*;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.ProprietaryUtils;
import com.alee.utils.swing.extensions.ComponentEventRunnable;
import com.alee.utils.swing.extensions.WindowCloseAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.util.List;

/**
 * {@link JWindow} extension class.
 * It contains various useful methods to simplify core component usage.
 *
 * This component should never be used with a non-Web UIs as it might cause an unexpected behavior.
 * You could still use that component even if WebLaF is not your application LaF as this component will use Web-UI in any case.
 *
 * @param <T> {@link WebWindow} type
 * @author Mikle Garin
 * @see JWindow
 * @see WebRootPaneUI
 * @see com.alee.laf.rootpane.RootPanePainter
 */
public class WebWindow<T extends WebWindow<T>> extends JWindow implements Styleable, WindowEventMethods, LanguageMethods,
        LanguageEventMethods, SettingsMethods, WindowMethods<T>
{
    /**
     * Component properties.
     */
    public static final String FOCUSABLE_WINDOW_STATE_PROPERTY = "focusableWindowState";
    public static final String WINDOW_DECORATION_STYLE_PROPERTY = "windowDecorationStyle";
    public static final String RESIZABLE_PROPERTY = "resizable";
    public static final String ICON_IMAGE_PROPERTY = "iconImage";
    public static final String TITLE_PROPERTY = "title";

    /**
     * Whether should close window on focus loss or not.
     */
    protected boolean closeOnFocusLoss = false;

    /**
     * Window focus tracker.
     */
    protected transient DefaultFocusTracker focusTracker;

    /**
     * Creates a window with no specified owner. This window will not be focusable.
     * <p>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     */
    public WebWindow ()
    {
        this ( StyleId.auto );
    }

    /**
     * Creates a window with the specified {@code GraphicsConfiguration} of a screen device. This window will not be focusable.
     * <p>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param gc the {@code GraphicsConfiguration} that is used to construct the new window with; if gc is {@code null},
     *           the system default {@code GraphicsConfiguration} is assumed
     */
    public WebWindow ( @Nullable final GraphicsConfiguration gc )
    {
        this ( StyleId.auto, gc );
    }

    /**
     * Creates a window with the owner window from the specified component.
     * This window will not be focusable unless its owner is showing on the screen.
     * If {@code owner} is {@code null}, the shared owner will be used and this window will not be focusable.
     * <p>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the componnt from which parent window this window is displayed
     */
    public WebWindow ( @Nullable final Component owner )
    {
        this ( StyleId.auto, owner );
    }

    /**
     * Creates a window with the specified owner window. This window will not be focusable unless its owner is showing on the screen.
     * If {@code owner} is {@code null}, the shared owner will be used and this window will not be focusable.
     * <p>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the window from which the window is displayed
     */
    public WebWindow ( @Nullable final Window owner )
    {
        this ( StyleId.auto, owner );
    }

    /**
     * Creates a window with the specified owner window and {@code GraphicsConfiguration} of a screen device.
     * If {@code owner} is {@code null}, the shared owner will be used and this window will not be focusable.
     * <p>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the window from which the window is displayed
     * @param gc    the {@code GraphicsConfiguration} that is used to construct the new window with; if gc is {@code null},
     *              the system default {@code GraphicsConfiguration} is assumed, unless {@code owner} is also null, in which
     *              case the {@code GraphicsConfiguration} from the shared owner frame will be used
     */
    public WebWindow ( @Nullable final Window owner, @Nullable final GraphicsConfiguration gc )
    {
        this ( StyleId.auto, owner, gc );
    }

    /**
     * Creates a window with the specified owner frame.
     * If {@code owner} is {@code null}, the shared owner will be used and this window will not be focusable.
     * Also, this window will not be focusable unless its owner is showing on the screen.
     * <p>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param owner the frame from which the window is displayed
     */
    public WebWindow ( @Nullable final Frame owner )
    {
        this ( StyleId.auto, owner );
    }

    /**
     * Creates a window with no specified owner. This window will not be focusable.
     * <p>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param id {@link StyleId}
     */
    public WebWindow ( @NotNull final StyleId id )
    {
        this ( id, ( Frame ) null );
    }

    /**
     * Creates a window with the specified {@code GraphicsConfiguration} of a screen device. This window will not be focusable.
     * <p>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param id {@link StyleId}
     * @param gc the {@code GraphicsConfiguration} that is used to construct the new window with; if gc is {@code null},
     *           the system default {@code GraphicsConfiguration} is assumed
     */
    public WebWindow ( @NotNull final StyleId id, @Nullable final GraphicsConfiguration gc )
    {
        this ( id, null, gc );
    }

    /**
     * Creates a window with the owner window from the specified component.
     * This window will not be focusable unless its owner is showing on the screen.
     * If {@code owner} is {@code null}, the shared owner will be used and this window will not be focusable.
     * <p>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param id    {@link StyleId}
     * @param owner the componnt from which parent window this window is displayed
     */
    public WebWindow ( @NotNull final StyleId id, @Nullable final Component owner )
    {
        this ( id, CoreSwingUtils.getWindowAncestor ( owner ) );
    }

    /**
     * Creates a window with the specified owner window. This window will not be focusable unless its owner is showing on the screen.
     * If {@code owner} is {@code null}, the shared owner will be used and this window will not be focusable.
     * <p>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param id    {@link StyleId}
     * @param owner the window from which the window is displayed
     */
    public WebWindow ( @NotNull final StyleId id, @Nullable final Window owner )
    {
        this ( id, owner, owner != null ? owner.getGraphicsConfiguration () : null );
    }

    /**
     * Creates a window with the specified owner window and {@code GraphicsConfiguration} of a screen device.
     * If {@code owner} is {@code null}, the shared owner will be used and this window will not be focusable.
     * <p>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param id    {@link StyleId}
     * @param owner the window from which the window is displayed
     * @param gc    the {@code GraphicsConfiguration} that is used to construct the new window with; if gc is {@code null},
     *              the system default {@code GraphicsConfiguration} is assumed, unless {@code owner} is also null, in which
     *              case the {@code GraphicsConfiguration} from the shared owner frame will be used
     */
    public WebWindow ( @NotNull final StyleId id, @Nullable final Window owner, @Nullable final GraphicsConfiguration gc )
    {
        super ( owner, gc );
        initialize ( id );
    }

    /**
     * Creates a window with the specified owner frame.
     * If {@code owner} is {@code null}, the shared owner will be used and this window will not be focusable.
     * Also, this window will not be focusable unless its owner is showing on the screen.
     * <p>
     * This constructor sets the component's locale property to the value returned by {@code JComponent.getDefaultLocale}.
     *
     * @param id    {@link StyleId}
     * @param owner the frame from which the window is displayed
     */
    public WebWindow ( @NotNull final StyleId id, @Nullable final Frame owner )
    {
        super ( owner );
        initialize ( id );
    }

    @Override
    protected void windowInit ()
    {
        // Disabling default initialization to optimize startup performance
    }

    /**
     * Additional initializtion of WebWindow settings.
     *
     * @param id initial {@link StyleId}
     */
    protected void initialize ( @NotNull final StyleId id )
    {
        // Default window initialization
        setLocale ( JComponent.getDefaultLocale () );
        setRootPane ( createRootPane () );
        setRootPaneCheckingEnabled ( true );
        ProprietaryUtils.checkAndSetPolicy ( this );

        // Updating base settings
        setFocusable ( true );
        setFocusableWindowState ( true );
        WebLookAndFeel.setOrientation ( this );

        // Applying style
        setStyleId ( id );

        // Adding focus tracker for this window
        // It is stored into a separate field to avoid its disposal from memory
        focusTracker = new DefaultFocusTracker ( getRootPane (), true )
        {
            @Override
            public boolean isEnabled ()
            {
                return closeOnFocusLoss && super.isEnabled ();
            }

            @Override
            public void focusChanged ( final boolean focused )
            {
                if ( isEnabled () && !focused )
                {
                    // Simply disposing window
                    dispose ();
                }
            }
        };
        FocusManager.addFocusTracker ( getRootPane (), focusTracker );
    }

    @Override
    protected JRootPane createRootPane ()
    {
        return new WebWindowRootPane ();
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
     * Returns focusable children that don't force window to close even if it set to close on focus loss.
     *
     * @return focusable children that don't force window to close even if it set to close on focus loss
     */
    public List<Component> getFocusableChildren ()
    {
        return focusTracker.getFocusableChildren ();
    }

    /**
     * Adds focusable child that won't force window to close even if it set to close on focus loss.
     *
     * @param child focusable child that won't force window to close even if it set to close on focus loss
     */
    public void addFocusableChild ( final Component child )
    {
        focusTracker.addFocusableChild ( child );
    }

    /**
     * Removes focusable child that doesn't force window to close even if it set to close on focus loss.
     *
     * @param child focusable child that doesn't force window to close even if it set to close on focus loss
     */
    public void removeFocusableChild ( final Component child )
    {
        focusTracker.removeFocusableChild ( child );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.window;
    }

    @NotNull
    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( getRootPane () );
    }

    @NotNull
    @Override
    public StyleId setStyleId ( @NotNull final StyleId id )
    {
        return StyleManager.setStyleId ( getRootPane (), id );
    }

    @NotNull
    @Override
    public StyleId resetStyleId ()
    {
        return StyleManager.resetStyleId ( getRootPane () );
    }

    @NotNull
    @Override
    public Skin getSkin ()
    {
        return StyleManager.getSkin ( getRootPane () );
    }

    @Nullable
    @Override
    public Skin setSkin ( @NotNull final Skin skin )
    {
        return StyleManager.setSkin ( getRootPane (), skin );
    }

    @Nullable
    @Override
    public Skin setSkin ( @NotNull final Skin skin, final boolean recursively )
    {
        return StyleManager.setSkin ( getRootPane (), skin, recursively );
    }

    @Nullable
    @Override
    public Skin resetSkin ()
    {
        return StyleManager.resetSkin ( getRootPane () );
    }

    @Override
    public void addStyleListener ( @NotNull final StyleListener listener )
    {
        StyleManager.addStyleListener ( getRootPane (), listener );
    }

    @Override
    public void removeStyleListener ( @NotNull final StyleListener listener )
    {
        StyleManager.removeStyleListener ( getRootPane (), listener );
    }

    @Nullable
    @Override
    public Painter getCustomPainter ()
    {
        return StyleManager.getCustomPainter ( getRootPane () );
    }

    @Nullable
    @Override
    public Painter setCustomPainter ( @NotNull final Painter painter )
    {
        return StyleManager.setCustomPainter ( getRootPane (), painter );
    }

    @Override
    public boolean resetCustomPainter ()
    {
        return StyleManager.resetCustomPainter ( getRootPane () );
    }

    @NotNull
    @Override
    public Shape getPainterShape ()
    {
        return PainterSupport.getShape ( getRootPane () );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( getRootPane () );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( getRootPane (), enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( getRootPane () );
    }

    @Override
    public void setMargin ( final int margin )
    {
        PainterSupport.setMargin ( getRootPane (), margin );
    }

    @Override
    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        PainterSupport.setMargin ( getRootPane (), top, left, bottom, right );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( getRootPane (), margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( getRootPane () );
    }

    @Override
    public void setPadding ( final int padding )
    {
        PainterSupport.setPadding ( getRootPane (), padding );
    }

    @Override
    public void setPadding ( final int top, final int left, final int bottom, final int right )
    {
        PainterSupport.setPadding ( getRootPane (), top, left, bottom, right );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( getRootPane (), padding );
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link com.alee.laf.rootpane.WRootPaneUI} object that renders this component
     */
    public WRootPaneUI getUI ()
    {
        return ( WRootPaneUI ) getRootPane ().getUI ();
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WRootPaneUI}
     */
    public void setUI ( final WRootPaneUI ui )
    {
        getRootPane ().setUI ( ui );
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

    @Nullable
    @Override
    public String getLanguage ()
    {
        return UILanguageManager.getComponentKey ( getRootPane () );
    }

    @Override
    public void setLanguage ( @NotNull final String key, @Nullable final Object... data )
    {
        UILanguageManager.registerComponent ( getRootPane (), key, data );
    }

    @Override
    public void updateLanguage ( @Nullable final Object... data )
    {
        UILanguageManager.updateComponent ( getRootPane (), data );
    }

    @Override
    public void updateLanguage ( @NotNull final String key, @Nullable final Object... data )
    {
        UILanguageManager.updateComponent ( getRootPane (), key, data );
    }

    @Override
    public void removeLanguage ()
    {
        UILanguageManager.unregisterComponent ( getRootPane () );
    }

    @Override
    public boolean isLanguageSet ()
    {
        return UILanguageManager.isRegisteredComponent ( getRootPane () );
    }

    @Override
    public void setLanguageUpdater ( @NotNull final LanguageUpdater updater )
    {
        UILanguageManager.registerLanguageUpdater ( getRootPane (), updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        UILanguageManager.unregisterLanguageUpdater ( getRootPane () );
    }

    @Override
    public void addLanguageListener ( @NotNull final LanguageListener listener )
    {
        UILanguageManager.addLanguageListener ( getRootPane (), listener );
    }

    @Override
    public void removeLanguageListener ( @NotNull final LanguageListener listener )
    {
        UILanguageManager.removeLanguageListener ( getRootPane (), listener );
    }

    @Override
    public void removeLanguageListeners ()
    {
        UILanguageManager.removeLanguageListeners ( getRootPane () );
    }

    @Override
    public void addDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        UILanguageManager.addDictionaryListener ( getRootPane (), listener );
    }

    @Override
    public void removeDictionaryListener ( @NotNull final DictionaryListener listener )
    {
        UILanguageManager.removeDictionaryListener ( getRootPane (), listener );
    }

    @Override
    public void removeDictionaryListeners ()
    {
        UILanguageManager.removeDictionaryListeners ( getRootPane () );
    }

    @Override
    public void registerSettings ( final Configuration configuration )
    {
        UISettingsManager.registerComponent ( getRootPane (), configuration );
    }

    @Override
    public void registerSettings ( final SettingsProcessor processor )
    {
        UISettingsManager.registerComponent ( getRootPane (), processor );
    }

    @Override
    public void unregisterSettings ()
    {
        UISettingsManager.unregisterComponent ( getRootPane () );
    }

    @Override
    public void loadSettings ()
    {
        UISettingsManager.loadSettings ( getRootPane () );
    }

    @Override
    public void saveSettings ()
    {
        UISettingsManager.saveSettings ( getRootPane () );
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
     * Custom root pane for this {@link WebWindow}.
     * It is required to create {@link WebRootPane} with undecorated style.
     * It is necessary to apply actual {@link StyleId} in {@link #initialize(StyleId)}.
     * It also provides default {@link WebWindow} {@link StyleId} instead of default {@link WebRootPane} {@link StyleId}.
     */
    public class WebWindowRootPane extends WebRootPane
    {
        /**
         * Constructs new {@link WebWindowRootPane} for this {@link WebWindow}.
         */
        public WebWindowRootPane ()
        {
            super ( StyleManager.getDescriptor ( WebWindowRootPane.class ).getDefaultStyleId () );
        }

        @NotNull
        @Override
        public StyleId getDefaultStyleId ()
        {
            return WebWindow.this.getDefaultStyleId ();
        }
    }
}