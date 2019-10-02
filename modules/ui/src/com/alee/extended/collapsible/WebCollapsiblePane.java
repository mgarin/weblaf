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

package com.alee.extended.collapsible;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.BoxOrientation;
import com.alee.api.jdk.Objects;
import com.alee.extended.WebContainer;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * This extended components allows you to quickly create and manipulate a collapsible pane.
 * Pane title, content and style can be modified in any way you like.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebCollapsiblePane">How to use WebCollapsiblePane</a>
 * @see CollapsiblePaneListener
 * @see CollapsiblePaneLayout
 * @see WCollapsiblePaneUI
 * @see WebCollapsiblePaneUI
 * @see ICollapsiblePanePainter
 * @see CollapsiblePanePainter
 */
public class WebCollapsiblePane extends WebContainer<WebCollapsiblePane, WCollapsiblePaneUI>
{
    /**
     * Component properties.
     */
    public static final String HEADER_POSITION_PROPERTY = "headerPosition";
    public static final String HEADER_COMPONENT_PROPERTY = "headerComponent";
    public static final String TITLE_PROPERTY = "title";
    public static final String TITLE_COMPONENT_PROPERTY = "titleComponent";
    public static final String CONTROL_COMPONENT_PROPERTY = "controlComponent";
    public static final String CONTENT_PROPERTY = "content";
    public static final String ANIMATED_PROPERTY = "animated";
    public static final String EXPANDED_PROPERTY = "expanded";
    public static final String IN_TRANSITION_PROPERTY = "inTransition";

    /**
     * Header panel position.
     */
    @Nullable
    protected BoxOrientation headerPosition;

    /**
     * Title {@link Component}.
     */
    @Nullable
    protected Component header;

    /**
     * Title {@link Component}.
     */
    @Nullable
    protected Component title;

    /**
     * Control {@link Component}.
     */
    @Nullable
    protected Component control;

    /**
     * Content {@link Component}.
     */
    @Nullable
    protected Component content;

    /**
     * Whether or not {@link WebCollapsiblePane} expansion and collapse should be animated.
     * By default it is {@code null} which is equal to {@code true} value.
     */
    @Nullable
    protected Boolean animated;

    /**
     * Whether or not {@link WebCollapsiblePane} is expanded.
     */
    protected boolean expanded;

    /**
     * Constructs empty {@link WebCollapsiblePane}.
     */
    public WebCollapsiblePane ()
    {
        this ( StyleId.auto, null, null, null, true );
    }

    /**
     * Constructs empty {@link WebCollapsiblePane} with specified expansion state.
     *
     * @param expanded whether or not {@link WebCollapsiblePane} is initially expanded
     */
    public WebCollapsiblePane ( final boolean expanded )
    {
        this ( StyleId.auto, null, null, null, expanded );
    }

    /**
     * Constructs empty {@link WebCollapsiblePane} with specified title text.
     *
     * @param title title text
     */
    public WebCollapsiblePane ( @Nullable final String title )
    {
        this ( StyleId.auto, null, title, null, true );
    }

    /**
     * Constructs empty {@link WebCollapsiblePane} with specified title text and expansion state.
     *
     * @param title    title text
     * @param expanded whether or not {@link WebCollapsiblePane} is initially expanded
     */
    public WebCollapsiblePane ( @Nullable final String title, final boolean expanded )
    {
        this ( StyleId.auto, null, title, null, expanded );
    }

    /**
     * Constructs empty {@link WebCollapsiblePane} with specified title icon and text.
     *
     * @param icon  title icon
     * @param title title text
     */
    public WebCollapsiblePane ( @Nullable final Icon icon, @Nullable final String title )
    {
        this ( StyleId.auto, icon, title, null, true );
    }

    /**
     * Constructs empty {@link WebCollapsiblePane} with specified title icon, text and expansion state.
     *
     * @param icon     title icon
     * @param title    title text
     * @param expanded whether or not {@link WebCollapsiblePane} is initially expanded
     */
    public WebCollapsiblePane ( @Nullable final Icon icon, @Nullable final String title, final boolean expanded )
    {
        this ( StyleId.auto, icon, title, null, expanded );
    }

    /**
     * Constructs {@link WebCollapsiblePane} with specified content.
     *
     * @param content content {@link Component}
     */
    public WebCollapsiblePane ( @Nullable final Component content )
    {
        this ( StyleId.auto, null, null, content, true );
    }

    /**
     * Constructs {@link WebCollapsiblePane} with specified content and expansion state.
     *
     * @param content  content {@link Component}
     * @param expanded whether or not {@link WebCollapsiblePane} is initially expanded
     */
    public WebCollapsiblePane ( @Nullable final Component content, final boolean expanded )
    {
        this ( StyleId.auto, null, null, content, expanded );
    }

    /**
     * Constructs {@link WebCollapsiblePane} with specified title text and content.
     *
     * @param title   title text
     * @param content content {@link Component}
     */
    public WebCollapsiblePane ( @Nullable final String title, @Nullable final Component content )
    {
        this ( StyleId.auto, null, title, content, true );
    }

    /**
     * Constructs {@link WebCollapsiblePane} with specified title text, content and expansion state.
     *
     * @param title    title text
     * @param content  content {@link Component}
     * @param expanded whether or not {@link WebCollapsiblePane} is initially expanded
     */
    public WebCollapsiblePane ( @Nullable final String title, @Nullable final Component content, final boolean expanded )
    {
        this ( StyleId.auto, null, title, content, expanded );
    }

    /**
     * Constructs {@link WebCollapsiblePane} with specified title icon, text and content.
     *
     * @param icon    title icon
     * @param title   title text
     * @param content content {@link Component}
     */
    public WebCollapsiblePane ( @Nullable final Icon icon, @Nullable final String title, @Nullable final Component content )
    {
        this ( StyleId.auto, icon, title, content, true );
    }

    /**
     * Constructs {@link WebCollapsiblePane} with specified title icon, text, content and expansion state.
     *
     * @param icon     title icon
     * @param title    title text
     * @param content  content {@link Component}
     * @param expanded whether or not {@link WebCollapsiblePane} is initially expanded
     */
    public WebCollapsiblePane ( @Nullable final Icon icon, @Nullable final String title, @Nullable final Component content,
                                final boolean expanded )
    {
        this ( StyleId.auto, icon, title, content, expanded );
    }

    /**
     * Constructs empty {@link WebCollapsiblePane}.
     *
     * @param id style ID
     */
    public WebCollapsiblePane ( @NotNull final StyleId id )
    {
        this ( id, null, null, null, true );
    }

    /**
     * Constructs empty {@link WebCollapsiblePane} with specified expansion state.
     *
     * @param id       style ID
     * @param expanded whether or not {@link WebCollapsiblePane} is initially expanded
     */
    public WebCollapsiblePane ( @NotNull final StyleId id, final boolean expanded )
    {
        this ( id, null, null, null, expanded );
    }

    /**
     * Constructs empty {@link WebCollapsiblePane} with specified title text.
     *
     * @param id    style ID
     * @param title title text
     */
    public WebCollapsiblePane ( @NotNull final StyleId id, @Nullable final String title )
    {
        this ( id, null, title, null, true );
    }

    /**
     * Constructs empty {@link WebCollapsiblePane} with specified title text and expansion state.
     *
     * @param id       style ID
     * @param title    title text
     * @param expanded whether or not {@link WebCollapsiblePane} is initially expanded
     */
    public WebCollapsiblePane ( @NotNull final StyleId id, @Nullable final String title, final boolean expanded )
    {
        this ( id, null, title, null, expanded );
    }

    /**
     * Constructs empty {@link WebCollapsiblePane} with specified title icon and text.
     *
     * @param id    style ID
     * @param icon  title icon
     * @param title title text
     */
    public WebCollapsiblePane ( @NotNull final StyleId id, @Nullable final Icon icon, @Nullable final String title )
    {
        this ( id, icon, title, null, true );
    }

    /**
     * Constructs empty {@link WebCollapsiblePane} with specified title icon, text and expansion state.
     *
     * @param id       style ID
     * @param icon     title icon
     * @param title    title text
     * @param expanded whether or not {@link WebCollapsiblePane} is initially expanded
     */
    public WebCollapsiblePane ( @NotNull final StyleId id, @Nullable final Icon icon, @Nullable final String title, final boolean expanded )
    {
        this ( id, icon, title, null, expanded );
    }

    /**
     * Constructs {@link WebCollapsiblePane} with specified content.
     *
     * @param id      style ID
     * @param content content {@link Component}
     */
    public WebCollapsiblePane ( @NotNull final StyleId id, @Nullable final Component content )
    {
        this ( id, null, null, content, true );
    }

    /**
     * Constructs {@link WebCollapsiblePane} with specified content and expansion state.
     *
     * @param id       style ID
     * @param content  content {@link Component}
     * @param expanded whether or not {@link WebCollapsiblePane} is initially expanded
     */
    public WebCollapsiblePane ( @NotNull final StyleId id, @Nullable final Component content, final boolean expanded )
    {
        this ( id, null, null, content, expanded );
    }

    /**
     * Constructs {@link WebCollapsiblePane} with specified title text and content.
     *
     * @param id      style ID
     * @param title   title text
     * @param content content {@link Component}
     */
    public WebCollapsiblePane ( @NotNull final StyleId id, @Nullable final String title, @Nullable final Component content )
    {
        this ( id, null, title, content, true );
    }

    /**
     * Constructs {@link WebCollapsiblePane} with specified title text, content and expansion state.
     *
     * @param id       style ID
     * @param title    title text
     * @param content  content {@link Component}
     * @param expanded whether or not {@link WebCollapsiblePane} is initially expanded
     */
    public WebCollapsiblePane ( @NotNull final StyleId id, @Nullable final String title, @Nullable final Component content,
                                final boolean expanded )
    {
        this ( id, null, title, content, expanded );
    }

    /**
     * Constructs {@link WebCollapsiblePane} with specified title icon, text and content.
     *
     * @param id      style ID
     * @param icon    title icon
     * @param title   title text
     * @param content content {@link Component}
     */
    public WebCollapsiblePane ( @NotNull final StyleId id, @Nullable final Icon icon, @Nullable final String title,
                                @Nullable final Component content )
    {
        this ( id, icon, title, content, true );
    }

    /**
     * Constructs {@link WebCollapsiblePane} with specified title icon, text, content and expansion state.
     *
     * @param id       style ID
     * @param icon     title icon
     * @param title    title text
     * @param content  content {@link Component}
     * @param expanded whether or not {@link WebCollapsiblePane} is initially expanded
     */
    public WebCollapsiblePane ( @NotNull final StyleId id, @Nullable final Icon icon, @Nullable final String title,
                                @Nullable final Component content, final boolean expanded )
    {
        updateUI ();
        setIcon ( icon );
        setTitle ( title );
        setContent ( content );
        setStyleId ( id );
        setExpanded ( expanded );
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.collapsiblepane;
    }

    @Override
    public void setLayout ( @Nullable final LayoutManager layout )
    {
        if ( layout == null || layout instanceof CollapsiblePaneLayout )
        {
            final CollapsiblePaneLayout oldLayout = getLayout ();
            if ( oldLayout != null )
            {
                oldLayout.uninstall ( this );
            }
            final CollapsiblePaneLayout newLayout = ( CollapsiblePaneLayout ) layout;
            if ( layout != null )
            {
                newLayout.install ( this );
            }
            super.setLayout ( layout );
        }
        else
        {
            throw new IllegalArgumentException ( "Only CollapsiblePaneLayout instances are supported" );
        }
    }

    @Override
    public CollapsiblePaneLayout getLayout ()
    {
        return ( CollapsiblePaneLayout ) super.getLayout ();
    }

    /**
     * Returns header panel position.
     *
     * @return header panel position
     */
    @NotNull
    public BoxOrientation getHeaderPosition ()
    {
        return headerPosition != null ? headerPosition : BoxOrientation.top;
    }

    /**
     * Sets header panel position.
     *
     * @param position new header panel position
     */
    public void setHeaderPosition ( @Nullable final BoxOrientation position )
    {
        final BoxOrientation headerPosition = getHeaderPosition ();
        if ( headerPosition != position )
        {
            this.headerPosition = position;
            firePropertyChange ( HEADER_POSITION_PROPERTY, headerPosition, position );
        }
    }

    /**
     * Returns current header {@link Component}.
     *
     * @return current header {@link Component}
     */
    @NotNull
    public Component getHeaderComponent ()
    {
        if ( header == null )
        {
            setHeaderComponent ( createHeaderComponent () );
        }
        return header;
    }

    /**
     * Sets header {@link Component}.
     * Note that modifying header will cause custom icon, title and control to be lost.
     *
     * @param header header {@link Component}
     */
    public void setHeaderComponent ( @NotNull final Component header )
    {
        if ( this.header != header )
        {
            final Component old = this.header;
            this.header = header;
            firePropertyChange ( HEADER_COMPONENT_PROPERTY, old, header );
        }
    }

    /**
     * Returns default header {@link Component}.
     *
     * @return default header {@link Component}
     */
    @NotNull
    protected Component createHeaderComponent ()
    {
        return new AbstractHeaderPanel.UIResource ()
        {
            @NotNull
            @Override
            public BoxOrientation getHeaderPosition ()
            {
                return WebCollapsiblePane.this.getHeaderPosition ();
            }

            @Override
            public boolean isExpanded ()
            {
                return WebCollapsiblePane.this.isExpanded ();
            }

            @Override
            public boolean isInTransition ()
            {
                return WebCollapsiblePane.this.isInTransition ();
            }

            @Override
            protected void onHeaderAction ( @NotNull final InputEvent e )
            {
                expandOrCollapse ( e );
            }
        };
    }

    /**
     * Returns default title component {@link Icon}.
     *
     * @return default title component {@link Icon}
     */
    @Nullable
    public Icon getIcon ()
    {
        return title instanceof AbstractTitleLabel ? ( ( AbstractTitleLabel ) title ).getIcon () : null;
    }

    /**
     * Sets default title component {@link Icon}.
     * Note that this will only work with default {@link AbstractTitleLabel} implementation or it's derivatives.
     *
     * @param icon new default title component {@link Icon}
     */
    public void setIcon ( @Nullable final Icon icon )
    {
        final Component titleComponent = getTitleComponent ();
        if ( titleComponent instanceof AbstractTitleLabel )
        {
            final AbstractTitleLabel titleLabel = ( AbstractTitleLabel ) titleComponent;
            final Icon old = titleLabel.getIcon ();
            titleLabel.setIcon ( icon );
            firePropertyChange ( TITLE_PROPERTY, old, titleComponent );
        }
    }

    /**
     * Returns default title component text.
     *
     * @return default title component text
     */
    @Nullable
    public String getTitle ()
    {
        return title instanceof AbstractTitleLabel ? ( ( AbstractTitleLabel ) title ).getText () : null;
    }

    /**
     * Sets default title component text.
     * Note that this will only work with default {@link AbstractTitleLabel} implementation or it's derivatives.
     *
     * @param title new title text
     */
    public void setTitle ( @Nullable final String title )
    {
        final Component titleComponent = getTitleComponent ();
        if ( titleComponent instanceof AbstractTitleLabel )
        {
            final AbstractTitleLabel titleLabel = ( AbstractTitleLabel ) titleComponent;
            final String oldTitle = titleLabel.getText ();
            titleLabel.setText ( title );
            firePropertyChange ( TITLE_PROPERTY, oldTitle, title );
        }
    }

    /**
     * Returns title {@link Component}.
     *
     * @return title {@link Component}
     */
    @Nullable
    public Component getTitleComponent ()
    {
        if ( title == null )
        {
            setTitleComponent ( createTitleComponent () );
        }
        return title;
    }

    /**
     * Sets title {@link Component}.
     * Note that modifying title component will cause custom icon and title to be lost.
     *
     * @param title new title {@link Component}
     */
    public void setTitleComponent ( @Nullable final Component title )
    {
        if ( this.title != title )
        {
            final Component oldTitle = this.title;
            this.title = title;
            firePropertyChange ( TITLE_COMPONENT_PROPERTY, oldTitle, title );
        }
    }

    /**
     * Returns default title {@link Component}.
     *
     * @return default title {@link Component}
     */
    @Nullable
    protected Component createTitleComponent ()
    {
        return new AbstractTitleLabel.UIResource ()
        {
            @NotNull
            @Override
            public BoxOrientation getHeaderPosition ()
            {
                return WebCollapsiblePane.this.getHeaderPosition ();
            }

            @Override
            public boolean isExpanded ()
            {
                return WebCollapsiblePane.this.isExpanded ();
            }

            @Override
            public boolean isInTransition ()
            {
                return WebCollapsiblePane.this.isInTransition ();
            }
        };
    }

    /**
     * Returns control {@link Component}.
     *
     * @return control {@link Component}
     */
    @Nullable
    public Component getControlComponent ()
    {
        if ( control == null )
        {
            setControlComponent ( createControlComponent () );
        }
        return control;
    }

    /**
     * Sets control {@link Component}.
     *
     * @param control new control {@link Component}
     */
    public void setControlComponent ( @Nullable final Component control )
    {
        if ( this.control != control )
        {
            final Component old = this.control;
            this.control = control;
            firePropertyChange ( CONTROL_COMPONENT_PROPERTY, old, control );
        }
    }

    /**
     * Returns default expansion control {@link Component}.
     *
     * @return default expansion control {@link Component}
     */
    @Nullable
    protected Component createControlComponent ()
    {
        return new AbstractControlButton.UIResource ()
        {
            @NotNull
            @Override
            public BoxOrientation getHeaderPosition ()
            {
                return WebCollapsiblePane.this.getHeaderPosition ();
            }

            @Override
            public boolean isExpanded ()
            {
                return WebCollapsiblePane.this.isExpanded ();
            }

            @Override
            public boolean isInTransition ()
            {
                return WebCollapsiblePane.this.isInTransition ();
            }

            @Override
            protected void onControlAction ( @NotNull final ActionEvent e )
            {
                expandOrCollapse ( e );
            }
        };
    }

    /**
     * Either expands or collapses this {@link WebCollapsiblePane}.
     *
     * @param e {@link AWTEvent}
     */
    protected void expandOrCollapse ( @NotNull final AWTEvent e )
    {
        WebCollapsiblePane.this.setExpanded ( !WebCollapsiblePane.this.isExpanded () );
        if ( e instanceof MouseEvent || e instanceof ActionEvent )
        {
            if ( WebCollapsiblePane.this.isShowing () && WebCollapsiblePane.this.isEnabled () )
            {
                if ( WebCollapsiblePane.this.isFocusable () )
                {
                    WebCollapsiblePane.this.requestFocusInWindow ();
                }
                else
                {
                    WebCollapsiblePane.this.transferFocus ();
                }
            }
        }
    }

    /**
     * Returns content {@link Component}.
     *
     * @return content {@link Component}
     */
    @Nullable
    public Component getContent ()
    {
        return content;
    }

    /**
     * Sets content {@link Component}.
     *
     * @param content new content {@link Component}
     */
    public void setContent ( @Nullable final Component content )
    {
        if ( this.content != content )
        {
            final Component old = this.content;
            this.content = content;
            firePropertyChange ( CONTENT_PROPERTY, old, content );
        }
    }

    /**
     * Returns whether or not {@link WebCollapsiblePane} expansion and collapse is animated.
     *
     * @return {@code true} if {@link WebCollapsiblePane} expansion and collapse is animated, {@code false} otherwise
     */
    public boolean isAnimated ()
    {
        return animated == null || animated;
    }

    /**
     * Sets whether or not {@link WebCollapsiblePane} expansion and collapse should be animated.
     *
     * @param animated whether or not {@link WebCollapsiblePane} expansion and collapse should be animated
     */
    public void setAnimated ( final boolean animated )
    {
        final Boolean wasAnimated = this.animated;
        if ( Objects.notEquals ( animated, wasAnimated ) )
        {
            this.animated = animated;
            firePropertyChange ( ANIMATED_PROPERTY, wasAnimated, ( Boolean ) animated );
        }
    }

    /**
     * Returns whether or not {@link WebCollapsiblePane} is expanded.
     *
     * @return {@code true} if {@link WebCollapsiblePane} is expanded, {@code false} otherwise
     */
    public boolean isExpanded ()
    {
        return expanded;
    }

    /**
     * Asks this {@link WebCollapsiblePane} to either expand or collapse.
     *
     * @param expanded whether {@link WebCollapsiblePane} should be expanded or collapsed
     */
    public void setExpanded ( final boolean expanded )
    {
        if ( this.expanded != expanded )
        {
            final boolean old = this.expanded;
            this.expanded = expanded;
            firePropertyChange ( EXPANDED_PROPERTY, old, expanded );
        }
    }

    /**
     * Asks this {@link WebCollapsiblePane} to expand if possible.
     */
    public void expand ()
    {
        setExpanded ( true );
    }

    /**
     * Asks this {@link WebCollapsiblePane} to collapse if possible.
     */
    public void collapse ()
    {
        setExpanded ( false );
    }

    /**
     * Returns whether or not {@link WebCollapsiblePane} is in transition to either of two expansion states.
     * Note this will only be {@code true} whenever {@link #expanded} state changes and {@link CollapsiblePaneLayout} is configured to
     * perform an animated transition on the {@link #content} of this {@link WebCollapsiblePane}.
     *
     * @return {@code true} if {@link WebCollapsiblePane} is in transition, {@code false} otherwise
     */
    public boolean isInTransition ()
    {
        return getLayout ().isInTransition ();
    }

    /**
     * Adds {@link CollapsiblePaneListener}.
     *
     * @param listener {@link CollapsiblePaneListener} to add
     */
    public void addCollapsiblePaneListener ( @NotNull final CollapsiblePaneListener listener )
    {
        listenerList.add ( CollapsiblePaneListener.class, listener );
    }

    /**
     * Removes {@link CollapsiblePaneListener}.
     *
     * @param listener {@link CollapsiblePaneListener} to remove
     */
    public void removeCollapsiblePaneListener ( @NotNull final CollapsiblePaneListener listener )
    {
        listenerList.remove ( CollapsiblePaneListener.class, listener );
    }

    /**
     * Notifies when {@link WebCollapsiblePane} starts to expand.
     */
    public void fireExpanding ()
    {
        for ( final CollapsiblePaneListener listener : listenerList.getListeners ( CollapsiblePaneListener.class ) )
        {
            listener.expanding ( this );
        }
    }

    /**
     * Notifies when {@link WebCollapsiblePane} finished expanding.
     */
    public void fireExpanded ()
    {
        for ( final CollapsiblePaneListener listener : listenerList.getListeners ( CollapsiblePaneListener.class ) )
        {
            listener.expanded ( this );
        }
    }

    /**
     * Notifies when {@link WebCollapsiblePane} starts to collapse.
     */
    public void fireCollapsing ()
    {
        for ( final CollapsiblePaneListener listener : listenerList.getListeners ( CollapsiblePaneListener.class ) )
        {
            listener.collapsing ( this );
        }
    }

    /**
     * Notifies when {@link WebCollapsiblePane} finished collapsing.
     */
    public void fireCollapsed ()
    {
        for ( final CollapsiblePaneListener listener : listenerList.getListeners ( CollapsiblePaneListener.class ) )
        {
            listener.collapsed ( this );
        }
    }

    /**
     * Returns the look and feel (LaF) object that renders this component.
     *
     * @return the {@link WCollapsiblePaneUI} object that renders this component
     */
    public WCollapsiblePaneUI getUI ()
    {
        return ( WCollapsiblePaneUI ) ui;
    }

    /**
     * Sets the LaF object that renders this component.
     *
     * @param ui {@link WCollapsiblePaneUI}
     */
    public void setUI ( final WCollapsiblePaneUI ui )
    {
        super.setUI ( ui );
    }

    @Override
    public void updateUI ()
    {
        StyleManager.getDescriptor ( this ).updateUI ( this );
    }

    @NotNull
    @Override
    public String getUIClassID ()
    {
        return StyleManager.getDescriptor ( this ).getUIClassId ();
    }
}