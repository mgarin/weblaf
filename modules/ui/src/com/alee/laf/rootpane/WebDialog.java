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

import com.alee.painter.Paintable;
import com.alee.painter.Painter;
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
import com.alee.managers.style.PaddingSupport;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.managers.style.Styleable;
import com.alee.managers.style.Skin;
import com.alee.managers.style.StyleListener;
import com.alee.managers.style.Skinnable;
import com.alee.utils.EventUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.WindowUtils;
import com.alee.utils.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

/**
 * This JDialog extension class provides some additional methods and options to manipulate dialog behavior.
 *
 * @author Mikle Garin
 */

public class WebDialog extends JDialog
        implements Styleable, Skinnable, Paintable, PaddingSupport, WindowEventMethods, LanguageMethods, LanguageContainerMethods,
        SettingsMethods, WindowMethods<WebDialog>
{
    /**
     * Whether should close dialog on focus loss or not.
     */
    protected boolean closeOnFocusLoss = false;

    /**
     * Window focus tracker.
     */
    protected DefaultFocusTracker focusTracker;

    public WebDialog ()
    {
        this ( StyleId.dialog );
    }

    public WebDialog ( final Frame owner )
    {
        this ( StyleId.dialog, owner );
    }

    public WebDialog ( final Frame owner, final boolean modal )
    {
        this ( StyleId.dialog, owner, modal );
    }

    public WebDialog ( final Frame owner, final String title )
    {
        this ( StyleId.dialog, owner, title );
    }

    public WebDialog ( final Frame owner, final String title, final boolean modal )
    {
        this ( StyleId.dialog, owner, title, modal );
    }

    public WebDialog ( final Frame owner, final String title, final boolean modal, final GraphicsConfiguration gc )
    {
        this ( StyleId.dialog, owner, title, modal, gc );
    }

    public WebDialog ( final Dialog owner )
    {
        this ( StyleId.dialog, owner );
    }

    public WebDialog ( final Dialog owner, final boolean modal )
    {
        this ( StyleId.dialog, owner, modal );
    }

    public WebDialog ( final Dialog owner, final String title )
    {
        this ( StyleId.dialog, owner, title );
    }

    public WebDialog ( final Dialog owner, final String title, final boolean modal )
    {
        this ( StyleId.dialog, owner, title, modal );
    }

    public WebDialog ( final Dialog owner, final String title, final boolean modal, final GraphicsConfiguration gc )
    {
        this ( StyleId.dialog, owner, title, modal, gc );
    }

    public WebDialog ( final Component owner )
    {
        this ( StyleId.dialog, owner );
    }

    public WebDialog ( final Component owner, final String title )
    {
        this ( StyleId.dialog, owner, title );
    }

    public WebDialog ( final Window owner )
    {
        this ( StyleId.dialog, owner );
    }

    public WebDialog ( final Window owner, final ModalityType modalityType )
    {
        this ( StyleId.dialog, owner, modalityType );
    }

    public WebDialog ( final Window owner, final String title )
    {
        this ( StyleId.dialog, owner, title );
    }

    public WebDialog ( final Window owner, final String title, final ModalityType modalityType )
    {
        this ( StyleId.dialog, owner, title, modalityType );
    }

    public WebDialog ( final Window owner, final String title, final ModalityType modalityType, final GraphicsConfiguration gc )
    {
        this ( StyleId.dialog, owner, title, modalityType, gc );
    }

    public WebDialog ( final StyleId id )
    {
        super ();
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Frame owner )
    {
        super ( owner );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Frame owner, final boolean modal )
    {
        super ( owner, modal );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Frame owner, final String title )
    {
        super ( owner, LanguageUtils.getInitialText ( title ) );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Frame owner, final String title, final boolean modal )
    {
        super ( owner, LanguageUtils.getInitialText ( title ), modal );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Frame owner, final String title, final boolean modal, final GraphicsConfiguration gc )
    {
        super ( owner, LanguageUtils.getInitialText ( title ), modal, gc );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Dialog owner )
    {
        super ( owner );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Dialog owner, final boolean modal )
    {
        super ( owner, modal );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Dialog owner, final String title )
    {
        super ( owner, LanguageUtils.getInitialText ( title ) );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Dialog owner, final String title, final boolean modal )
    {
        super ( owner, LanguageUtils.getInitialText ( title ), modal );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Dialog owner, final String title, final boolean modal, final GraphicsConfiguration gc )
    {
        super ( owner, LanguageUtils.getInitialText ( title ), modal, gc );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Component owner )
    {
        super ( SwingUtils.getWindowAncestor ( owner ) );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Component owner, final String title )
    {
        super ( SwingUtils.getWindowAncestor ( owner ), LanguageUtils.getInitialText ( title ) );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Window owner )
    {
        super ( owner );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Window owner, final ModalityType modalityType )
    {
        super ( owner, modalityType );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Window owner, final String title )
    {
        super ( owner, LanguageUtils.getInitialText ( title ) );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Window owner, final String title, final ModalityType modalityType )
    {
        super ( owner, LanguageUtils.getInitialText ( title ), modalityType );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( id );
    }

    public WebDialog ( final StyleId id, final Window owner, final String title, final ModalityType modalityType,
                       final GraphicsConfiguration gc )
    {
        super ( owner, LanguageUtils.getInitialText ( title ), modalityType, gc );
        LanguageUtils.registerInitialLanguage ( this, title );
        initialize ( id );
    }

    /**
     * Additional initialization of WebDialog settings.
     *
     * @param id initial style ID
     */
    protected void initialize ( final StyleId id )
    {
        // Updating base settings
        SwingUtils.setOrientation ( this );
        setDefaultCloseOperation ( DISPOSE_ON_CLOSE );

        // Installing root pane style
        if ( id != null )
        {
            setStyleId ( id );
        }

        // Adding focus tracker for this dialog
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
                    processWindowEvent ( new WindowEvent ( WebDialog.this, WindowEvent.WINDOW_CLOSING ) );
                }
            }
        };
        FocusManager.addFocusTracker ( this, focusTracker );
    }

    /**
     * Called by the constructor methods to create the default {@code rootPane}.
     */
    @Override
    protected JRootPane createRootPane ()
    {
        return new WebRootPane ();
    }

    /**
     * Returns whether should close dialog on focus loss or not.
     *
     * @return true if should close dialog on focus loss, false otherwise
     */
    public boolean isCloseOnFocusLoss ()
    {
        return closeOnFocusLoss;
    }

    /**
     * Sets whether should close dialog on focus loss or not.
     *
     * @param closeOnFocusLoss whether should close dialog on focus loss or not
     */
    public void setCloseOnFocusLoss ( final boolean closeOnFocusLoss )
    {
        this.closeOnFocusLoss = closeOnFocusLoss;
    }

    /**
     * Returns focusable children that don't force dialog to close even if it set to close on focus loss.
     *
     * @return focusable children that don't force dialog to close even if it set to close on focus loss
     */
    public List<Component> getFocusableChildren ()
    {
        return focusTracker.getCustomChildren ();
    }

    /**
     * Adds focusable child that won't force dialog to close even if it set to close on focus loss.
     *
     * @param child focusable child that won't force dialog to close even if it set to close on focus loss
     */
    public void addFocusableChild ( final Component child )
    {
        focusTracker.addCustomChild ( child );
    }

    /**
     * Removes focusable child that doesn't force dialog to close even if it set to close on focus loss.
     *
     * @param child focusable child that doesn't force dialog to close even if it set to close on focus loss
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
    public Skin restoreSkin ()
    {
        return StyleManager.restoreSkin ( getRootPane () );
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
    public boolean restoreDefaultPainters ()
    {
        return StyleManager.restoreDefaultPainters ( getRootPane () );
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
     * Returns Web-UI applied to this class.
     *
     * @return Web-UI applied to this class
     */
    protected WebRootPaneUI getWebUI ()
    {
        return ( WebRootPaneUI ) getRootPane ().getUI ();
    }

    /**
     * Returns Web-UI applied to root pane used by this dialog.
     *
     * @return Web-UI applied to root pane used by this dialog
     */
    protected WebRootPaneUI getRootPaneWebUI ()
    {
        return ( WebRootPaneUI ) getRootPane ().getUI ();
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

    @Override
    public WebDialog setWindowOpaque ( final boolean opaque )
    {
        return WindowUtils.setWindowOpaque ( this, opaque );
    }

    @Override
    public boolean isWindowOpaque ()
    {
        return WindowUtils.isWindowOpaque ( this );
    }

    @Override
    public WebDialog setWindowOpacity ( final float opacity )
    {
        return WindowUtils.setWindowOpacity ( this, opacity );
    }

    @Override
    public float getWindowOpacity ()
    {
        return WindowUtils.getWindowOpacity ( this );
    }

    @Override
    public WebDialog center ()
    {
        return WindowUtils.center ( this );
    }

    @Override
    public WebDialog center ( final Component relativeTo )
    {
        return WindowUtils.center ( this, relativeTo );
    }

    @Override
    public WebDialog center ( final int width, final int height )
    {
        return WindowUtils.center ( this, width, height );
    }

    @Override
    public WebDialog center ( final Component relativeTo, final int width, final int height )
    {
        return WindowUtils.center ( this, relativeTo, width, height );
    }

    @Override
    public WebDialog packToWidth ( final int width )
    {
        return WindowUtils.packToWidth ( this, width );
    }

    @Override
    public WebDialog packToHeight ( final int height )
    {
        return WindowUtils.packToHeight ( this, height );
    }

    @Override
    public WebDialog packAndCenter ()
    {
        return WindowUtils.packAndCenter ( this );
    }

    @Override
    public WebDialog packAndCenter ( final boolean animate )
    {
        return WindowUtils.packAndCenter ( this, animate );
    }
}