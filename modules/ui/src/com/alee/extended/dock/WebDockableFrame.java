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

package com.alee.extended.dock;

import com.alee.extended.WebContainer;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.LanguageMethods;
import com.alee.managers.language.updaters.LanguageUpdater;
import com.alee.managers.log.Log;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleableComponent;
import com.alee.painter.decoration.states.CompassDirection;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Frame element for {@link com.alee.extended.dock.WebDockablePane}.
 * It can have its own content which can be easily moved within dockable pane or even outside of its bounds.
 * Frame usually take side space within dockable pane and its center area is taken by some custom content.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 * @see com.alee.extended.dock.WebDockablePane
 */

public class WebDockableFrame extends WebContainer<WebDockableFrameUI, WebDockableFrame> implements LanguageMethods
{
    /**
     * Component properties.
     */
    public static final String STATE_PROPERTY = "state";
    public static final String FLOATABLE_PROPERTY = "floatable";
    public static final String ICON_PROPERTY = "icon";
    public static final String TITLE_PROPERTY = "title";
    public static final String POSITION_PROPERTY = "position";
    public static final String DOCKABLE_PANE_PROPERTY = "dockablePane";

    /**
     * Unique frame ID.
     * It is used to store frame position data.
     */
    protected String frameId;

    /**
     * Current frame state.
     */
    protected DockableFrameState state;

    /**
     * Whether or not frame can be separated into floating window.
     */
    protected boolean floatable;

    /**
     * Frame icon.
     */
    protected Icon icon;

    /**
     * Frame title.
     */
    protected String title;

    /**
     * Position of this frame on dockable pane.
     * This value will only be used if dockable pane didn't save any position for this frame yet.
     */
    protected CompassDirection position;

    /**
     * Dockable pane this frame is added into.
     */
    protected WebDockablePane dockablePane;

    /**
     * Constructs new {@link com.alee.extended.dock.WebDockableFrame}.
     *
     * @param frameId unique frame ID
     * @param title   frame title
     */
    public WebDockableFrame ( final String frameId, final String title )
    {
        this ( frameId, null, title );
    }

    /**
     * Constructs new {@link com.alee.extended.dock.WebDockableFrame}.
     *
     * @param frameId unique frame ID
     * @param icon    frame icon
     */
    public WebDockableFrame ( final String frameId, final Icon icon )
    {
        this ( frameId, icon, "" );
    }

    /**
     * Constructs new {@link com.alee.extended.dock.WebDockableFrame}.
     *
     * @param frameId unique frame ID
     * @param icon    frame icon
     * @param title   frame title
     */
    public WebDockableFrame ( final String frameId, final Icon icon, final String title )
    {
        super ();
        this.frameId = frameId;
        this.state = DockableFrameState.closed;
        this.floatable = true;
        this.icon = icon;
        this.title = title;
        this.position = CompassDirection.west;
        setLayout ( new BorderLayout () );
        updateUI ();
        setStyleId ( StyleId.dockableframe );
    }

    /**
     * Returns unique frame ID.
     *
     * @return unique frame ID
     */
    public String getFrameId ()
    {
        return frameId;
    }

    /**
     * Returns current frame state.
     *
     * @return current frame state
     */
    public DockableFrameState getState ()
    {
        return state;
    }

    /**
     * Sets frame state.
     *
     * @param state frame state
     * @return this frame
     */
    public WebDockableFrame setState ( final DockableFrameState state )
    {
        if ( this.state != state )
        {
            final DockableFrameState old = this.state;
            this.state = state;
            firePropertyChange ( STATE_PROPERTY, old, state );
        }
        return this;
    }

    /**
     * Returns whether or not frame can be separated into floating window.
     *
     * @return true if frame can be separated into floating window, false otherwise
     */
    public boolean isFloatable ()
    {
        return floatable;
    }

    /**
     * Sets whether or not frame can be separated into floating window.
     *
     * @param floatable whether or not frame can be separated into floating window
     * @return this frame
     */
    public WebDockableFrame setFloatable ( final boolean floatable )
    {
        if ( this.floatable != floatable )
        {
            final boolean old = this.floatable;
            this.floatable = floatable;
            firePropertyChange ( FLOATABLE_PROPERTY, old, floatable );
        }
        return this;
    }

    /**
     * Returns frame icon.
     *
     * @return frame icon
     */
    public Icon getIcon ()
    {
        return icon;
    }

    /**
     * Sets frame icon.
     *
     * @param icon frame icon
     * @return this frame
     */
    public WebDockableFrame setIcon ( final Icon icon )
    {
        if ( this.icon != icon )
        {
            final Icon old = this.icon;
            this.icon = icon;
            firePropertyChange ( ICON_PROPERTY, old, icon );
        }
        return this;
    }

    /**
     * Returns frame title.
     *
     * @return frame title
     */
    public String getTitle ()
    {
        return title;
    }

    /**
     * Sets frame title.
     *
     * @param title frame title
     * @return this frame
     */
    public WebDockableFrame setTitle ( final String title )
    {
        if ( !CompareUtils.equals ( this.title, title ) )
        {
            final String old = this.title;
            this.title = title;
            firePropertyChange ( TITLE_PROPERTY, old, title );
        }
        return this;
    }

    /**
     * Returns position of this frame on dockable pane.
     *
     * @return position of this frame on dockable pane
     */
    public CompassDirection getPosition ()
    {
        return position;
    }

    /**
     * Sets position of this frame on dockable pane.
     *
     * @param position position of this frame on dockable pane
     * @return this frame
     */
    public WebDockableFrame setPosition ( final CompassDirection position )
    {
        if ( this.position != position )
        {
            final CompassDirection old = this.position;
            this.position = position;
            firePropertyChange ( POSITION_PROPERTY, old, position );
        }
        return this;
    }

    /**
     * Returns dockable pane this frame is added into.
     *
     * @return dockable pane this frame is added into
     */
    public WebDockablePane getDockablePane ()
    {
        return dockablePane;
    }

    /**
     * Sets dockable pane this frame is added into.
     *
     * @param dockablePane dockable pane this frame is added into
     */
    protected void setDockablePane ( final WebDockablePane dockablePane )
    {
        if ( this.dockablePane != dockablePane )
        {
            final WebDockablePane old = this.dockablePane;
            this.dockablePane = dockablePane;
            firePropertyChange ( DOCKABLE_PANE_PROPERTY, old, dockablePane );
        }
    }

    /**
     * Returns sidebar button for this frame.
     *
     * @return sidebar button for this frame
     */
    public JComponent getSidebarButton ()
    {
        return getUI ().getSidebarButton ();
    }

    /**
     * Returns whether or not sidebar button is currently visible.
     *
     * @return true if sidebar button is currently visible, false otherwise
     */
    public boolean isSidebarButtonVisible ()
    {
        final WebDockablePane dockablePane = getDockablePane ();
        if ( dockablePane != null )
        {
            switch ( dockablePane.getSidebarVisibility () )
            {
                case none:
                    return false;

                case hidden:
                    return CompareUtils.equals ( state, DockableFrameState.hidden, DockableFrameState.preview );

                case all:
                    return true;
            }
        }
        return false;
    }

    /**
     * Returns whether or not sidebar button is currently visible.
     *
     * @return true if sidebar button is currently visible, false otherwise
     */
    public boolean isOnDockablePane ()
    {
        final WebDockablePane dockablePane = getDockablePane ();
        return dockablePane != null && CompareUtils.equals ( state, DockableFrameState.docked, DockableFrameState.preview );
    }

    /**
     * Docks this dockable frame to its last position on the pane.
     *
     * @return this frame
     */
    public WebDockableFrame dockFrame ()
    {
        setState ( DockableFrameState.docked );
        return this;
    }

    /**
     * Hides this dockable frame, only its sidebar button will be visible.
     *
     * @return this frame
     */
    public WebDockableFrame hideFrame ()
    {
        setState ( DockableFrameState.hidden );
        return this;
    }

    /**
     * Displays frame preview on top of other pane elements, sidebar button will still be kept visible.
     *
     * @return this frame
     */
    public WebDockableFrame previewFrame ()
    {
        setState ( DockableFrameState.preview );
        return this;
    }

    /**
     * Displays frame on a separate non-modal dialog window attached to dockable frame window.
     *
     * @return this frame
     */
    public WebDockableFrame detachFrame ()
    {
        setState ( DockableFrameState.floating );
        return this;
    }

    /**
     * Closes this dockable frame.
     *
     * @return this frame
     */
    public WebDockableFrame closeFrame ()
    {
        final WebDockablePane dockablePane = getDockablePane ();
        return dockablePane != null ? dockablePane.removeFrame ( this ) : this;
    }

    @Override
    public void setLanguage ( final String key, final Object... data )
    {
        LanguageManager.registerComponent ( this, key, data );
    }

    @Override
    public void updateLanguage ( final Object... data )
    {
        LanguageManager.updateComponent ( this, data );
    }

    @Override
    public void updateLanguage ( final String key, final Object... data )
    {
        LanguageManager.updateComponent ( this, key, data );
    }

    @Override
    public void removeLanguage ()
    {
        LanguageManager.unregisterComponent ( this );
    }

    @Override
    public boolean isLanguageSet ()
    {
        return LanguageManager.isRegisteredComponent ( this );
    }

    @Override
    public void setLanguageUpdater ( final LanguageUpdater updater )
    {
        LanguageManager.registerLanguageUpdater ( this, updater );
    }

    @Override
    public void removeLanguageUpdater ()
    {
        LanguageManager.unregisterLanguageUpdater ( this );
    }

    /**
     * Returns the look and feel (L&amp;F) object that renders this component.
     *
     * @return the {@link com.alee.extended.dock.DockableFrameUI} object that renders this component
     */
    public DockableFrameUI getUI ()
    {
        return ( DockableFrameUI ) ui;
    }

    /**
     * Sets the L&amp;F object that renders this component.
     *
     * @param ui {@link com.alee.extended.dock.DockableFrameUI}
     */
    public void setUI ( final DockableFrameUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public WebDockableFrameUI getWebUI ()
    {
        return ( WebDockableFrameUI ) getUI ();
    }

    @Override
    public void updateUI ()
    {
        if ( getUI () == null || !( getUI () instanceof WebDockableFrameUI ) )
        {
            try
            {
                setUI ( ( WebDockableFrameUI ) UIManager.getUI ( this ) );
            }
            catch ( final Throwable e )
            {
                Log.error ( this, e );
                setUI ( new WebDockableFrameUI () );
            }
        }
        else
        {
            setUI ( getUI () );
        }
    }

    @Override
    public String getUIClassID ()
    {
        return StyleableComponent.dockableframe.getUIClassID ();
    }
}