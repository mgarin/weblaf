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

package com.alee.extended.accordion;

import com.alee.api.Identifiable;
import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.BoxOrientation;
import com.alee.extended.collapsible.AbstractControlButton;
import com.alee.extended.collapsible.AbstractHeaderPanel;
import com.alee.extended.collapsible.AbstractTitleLabel;
import com.alee.extended.collapsible.HeaderLayout;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.Stateful;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link AccordionPane} for a single collapsible content {@link Component} within {@link WebAccordion}.
 * This is basically a collapsible panel made and optimized specifically for {@link WebAccordion} needs.
 * It doesn't have it's own UI but uses some abstract UI elements from {@link com.alee.extended.collapsible.WebCollapsiblePane}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebAccordion">How to use WebAccordion</a>
 * @see WebAccordion
 * @see AccordionModel
 * @see WebAccordionModel
 */
public class AccordionPane extends WebPanel implements Stateful, Identifiable
{
    /**
     * {@link AccordionPane} unique identifier.
     */
    @NotNull
    protected final String id;

    /**
     * Header {@link JComponent}.
     */
    @NotNull
    protected JComponent header;

    /**
     * Content {@link Component}.
     */
    @Nullable
    protected Component content;

    /**
     * Constructs new {@link AccordionPane}.
     *
     * @param accordion {@link WebAccordion} this {@link AccordionPane} is created for
     * @param id        unique {@link AccordionPane} identifier
     * @param title     {@link AccordionPane} title
     */
    public AccordionPane ( @NotNull final WebAccordion accordion, @NotNull final String id, @Nullable final String title )
    {
        this ( StyleId.accordionPane.at ( accordion ), accordion, id, null, title, null );
    }

    /**
     * Constructs new {@link AccordionPane}.
     *
     * @param accordion {@link WebAccordion} this {@link AccordionPane} is created for
     * @param id        unique {@link AccordionPane} identifier
     * @param icon      {@link AccordionPane} title icon
     * @param title     {@link AccordionPane} title
     */
    public AccordionPane ( @NotNull final WebAccordion accordion, @NotNull final String id, @Nullable final Icon icon,
                           @Nullable final String title )
    {
        this ( StyleId.accordionPane.at ( accordion ), accordion, id, icon, title, null );
    }

    /**
     * Constructs new {@link AccordionPane}.
     *
     * @param accordion {@link WebAccordion} this {@link AccordionPane} is created for
     * @param id        unique {@link AccordionPane} identifier
     * @param title     {@link AccordionPane} title
     * @param content   content {@link Component}
     */
    public AccordionPane ( @NotNull final WebAccordion accordion, @NotNull final String id, @Nullable final String title,
                           @Nullable final Component content )
    {
        this ( StyleId.accordionPane.at ( accordion ), accordion, id, null, title, content );
    }

    /**
     * Constructs new {@link AccordionPane}.
     *
     * @param accordion {@link WebAccordion} this {@link AccordionPane} is created for
     * @param id        unique {@link AccordionPane} identifier
     * @param icon      {@link AccordionPane} title icon
     * @param title     {@link AccordionPane} title
     * @param content   content {@link Component}
     */
    public AccordionPane ( @NotNull final WebAccordion accordion, @NotNull final String id, @Nullable final Icon icon,
                           @Nullable final String title, @Nullable final Component content )
    {
        this ( StyleId.accordionPane.at ( accordion ), accordion, id, icon, title, content );
    }

    /**
     * Constructs new {@link AccordionPane}.
     *
     * @param styleId   {@link StyleId}
     * @param accordion {@link WebAccordion} this {@link AccordionPane} is created for
     * @param id        {@link AccordionPane} unique identifier
     * @param title     {@link AccordionPane} title
     */
    public AccordionPane ( @NotNull final StyleId styleId, @NotNull final WebAccordion accordion, @NotNull final String id,
                           @Nullable final String title )
    {
        this ( styleId, accordion, id, null, title, null );
    }

    /**
     * Constructs new {@link AccordionPane}.
     *
     * @param styleId   {@link StyleId}
     * @param accordion {@link WebAccordion} this {@link AccordionPane} is created for
     * @param id        {@link AccordionPane} unique identifier
     * @param icon      {@link AccordionPane} title icon
     * @param title     {@link AccordionPane} title
     */
    public AccordionPane ( @NotNull final StyleId styleId, @NotNull final WebAccordion accordion, @NotNull final String id,
                           @Nullable final Icon icon, @Nullable final String title )
    {
        this ( styleId, accordion, id, icon, title, null );
    }

    /**
     * Constructs new {@link AccordionPane}.
     *
     * @param styleId   {@link StyleId}
     * @param accordion {@link WebAccordion} this {@link AccordionPane} is created for
     * @param id        {@link AccordionPane} unique identifier
     * @param title     {@link AccordionPane} title
     * @param content   content {@link Component}
     */
    public AccordionPane ( @NotNull final StyleId styleId, @NotNull final WebAccordion accordion, @NotNull final String id,
                           @Nullable final String title, @Nullable final Component content )
    {
        this ( styleId, accordion, id, null, title, content );
    }

    /**
     * Constructs new {@link AccordionPane}.
     *
     * @param styleId   {@link StyleId}
     * @param accordion {@link WebAccordion} this {@link AccordionPane} is created for
     * @param id        {@link AccordionPane} unique identifier
     * @param icon      {@link AccordionPane} title icon
     * @param title     {@link AccordionPane} title
     * @param content   content {@link Component}
     */
    public AccordionPane ( @NotNull final StyleId styleId, @NotNull final WebAccordion accordion, @NotNull final String id,
                           @Nullable final Icon icon, @Nullable final String title, @Nullable final Component content )
    {
        super ( styleId, ( LayoutManager ) null );
        this.id = id;

        this.header = createHeaderComponent ( icon, title );
        add ( this.header );

        this.content = content;
        if ( content != null )
        {
            add ( this.content );
        }
    }

    @NotNull
    @Override
    public String getId ()
    {
        return id;
    }

    @Nullable
    @Override
    public List<String> getStates ()
    {
        final List<String> states = new ArrayList<String> ( 3 );

        // Header position state
        states.add ( getHeaderPosition ().name () );

        // Expansion state
        states.add ( isExpanded () || isInTransition () ? DecorationState.expanded : DecorationState.collapsed );
        if ( isInTransition () )
        {
            states.add ( isExpanded () ? DecorationState.expanding : DecorationState.collapsing );
        }

        return states;
    }

    /**
     * Returns header {@link JComponent}.
     *
     * @return header {@link JComponent}
     */
    @NotNull
    public JComponent getHeader ()
    {
        return header;
    }

    /**
     * Replaces header {@link JComponent}.
     *
     * @param icon  {@link AccordionPane} title icon
     * @param title {@link AccordionPane} title
     */
    public void setHeader ( @Nullable final Icon icon, @Nullable final String title )
    {
        setHeader ( createHeaderComponent ( icon, title ) );
    }

    /**
     * Replaces header {@link JComponent}.
     *
     * @param header new header {@link JComponent}
     */
    public void setHeader ( @NotNull final JComponent header )
    {
        if ( this.header != header )
        {
            remove ( this.header );
            this.header = header;
            add ( this.header );
            SwingUtils.update ( this );
        }
    }

    /**
     * Returns newly created header {@link JComponent}.
     *
     * @param icon  header {@link Icon}
     * @param title header title
     * @return newly created header {@link JComponent}
     */
    @NotNull
    protected JComponent createHeaderComponent ( @Nullable final Icon icon, @Nullable final String title )
    {
        final AbstractHeaderPanel headerPanel = new AbstractHeaderPanel ( StyleId.accordionPaneHeaderPanel.at ( this ) )
        {
            @Override
            @NotNull
            public BoxOrientation getHeaderPosition ()
            {
                return AccordionPane.this.getHeaderPosition ();
            }

            @Override
            public boolean isExpanded ()
            {
                return AccordionPane.this.isExpanded ();
            }

            @Override
            public boolean isInTransition ()
            {
                return AccordionPane.this.isInTransition ();
            }

            @Override
            protected void onHeaderAction ( @NotNull final InputEvent e )
            {
                expandOrCollapse ( e );
            }
        };
        headerPanel.add ( createTitleComponent ( headerPanel, icon, title ), HeaderLayout.TITLE );
        headerPanel.add ( createControlComponent ( headerPanel ), HeaderLayout.CONTROL );
        return headerPanel;
    }

    /**
     * Returns newly created title {@link JComponent}.
     *
     * @param header header {@link JComponent}
     * @param icon   header {@link Icon}
     * @param title  header title
     * @return newly created title {@link JComponent}
     */
    @NotNull
    protected JComponent createTitleComponent ( @NotNull final JComponent header, @Nullable final Icon icon, @Nullable final String title )
    {
        final AbstractTitleLabel titleLabel = new AbstractTitleLabel ( StyleId.accordionPaneTitleLabel.at ( header ) )
        {
            @Override
            @NotNull
            public BoxOrientation getHeaderPosition ()
            {
                return AccordionPane.this.getHeaderPosition ();
            }

            @Override
            public boolean isExpanded ()
            {
                return AccordionPane.this.isExpanded ();
            }

            @Override
            public boolean isInTransition ()
            {
                return AccordionPane.this.isInTransition ();
            }
        };
        titleLabel.setIcon ( icon );
        titleLabel.setText ( title );
        return titleLabel;
    }

    /**
     * Returns newly created control {@link JComponent}.
     *
     * @param header header {@link JComponent}
     * @return newly created control {@link JComponent}
     */
    @NotNull
    protected JComponent createControlComponent ( @NotNull final JComponent header )
    {
        return new AbstractControlButton ( StyleId.accordionPaneControlButton.at ( header ) )
        {
            @Override
            @NotNull
            public BoxOrientation getHeaderPosition ()
            {
                return AccordionPane.this.getHeaderPosition ();
            }

            @Override
            public boolean isExpanded ()
            {
                return AccordionPane.this.isExpanded ();
            }

            @Override
            public boolean isInTransition ()
            {
                return AccordionPane.this.isInTransition ();
            }

            @Override
            protected void onControlAction ( @NotNull final ActionEvent e )
            {
                expandOrCollapse ( e );
            }
        };
    }

    /**
     * Either expands or collapses this {@link AccordionPane}.
     *
     * @param e {@link AWTEvent}
     */
    protected void expandOrCollapse ( @NotNull final AWTEvent e )
    {
        final Container parent = AccordionPane.this.getParent ();
        if ( parent instanceof WebAccordion )
        {
            final WebAccordion accordion = ( WebAccordion ) parent;
            if ( isExpanded () )
            {
                accordion.collapsePane ( getId () );
            }
            else
            {
                accordion.expandPane ( getId () );
            }
            if ( e instanceof MouseEvent || e instanceof ActionEvent )
            {
                if ( accordion.isShowing () )
                {
                    if ( accordion.isFocusable () )
                    {
                        accordion.requestFocusInWindow ();
                    }
                    else
                    {
                        AccordionPane.this.transferFocus ();
                    }
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
     * Replaces content {@link Component}.
     *
     * @param content new content {@link Component}
     */
    public void setContent ( @Nullable final Component content )
    {
        if ( this.content != content )
        {
            if ( this.content != null )
            {
                remove ( this.content );
            }
            this.content = content;
            if ( this.content != null )
            {
                add ( this.content );
            }
            SwingUtils.update ( this );
        }
    }

    /**
     * Returns header panel position.
     *
     * @return header panel position
     */
    @NotNull
    public BoxOrientation getHeaderPosition ()
    {
        final Container parent = getParent ();
        return parent instanceof WebAccordion ? ( ( WebAccordion ) parent ).getHeaderPosition () : BoxOrientation.top;
    }

    /**
     * Returns whether or not this {@link AccordionPane} is expanded.
     *
     * @return {@code true} if this {@link AccordionPane} is expanded, {@code false} otherwise
     */
    public boolean isExpanded ()
    {
        final Container parent = getParent ();
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).isPaneExpanded ( getId () );
    }

    /**
     * Changes state of this {@link AccordionPane} to either expanded or collapsed.
     *
     * @param expanded whether or not this {@link AccordionPane} needs to be expanded or collapsed
     * @return {@code true} if state of this {@link AccordionPane} was changed
     */
    public boolean setExpanded ( final boolean expanded )
    {
        final Container parent = getParent ();
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).setPaneExpanded ( getId (), expanded );
    }

    /**
     * Asks this {@link AccordionPane} to expand if possible.
     *
     * @return {@code true} if this {@link AccordionPane} was successfully expanded, {@code false} otherwise
     */
    public boolean expand ()
    {
        final Container parent = getParent ();
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).expandPane ( getId () );
    }

    /**
     * Returns whether or not this {@link AccordionPane} is collapsed.
     *
     * @return {@code true} if this {@link AccordionPane} is collapsed, {@code false} otherwise
     */
    public boolean isCollapsed ()
    {
        final Container parent = getParent ();
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).isPaneCollapsed ( getId () );
    }

    /**
     * Changes state of this {@link AccordionPane} to either collapsed or expanded.
     *
     * @param collapsed whether or not this {@link AccordionPane} needs to be collapsed or expanded
     * @return {@code true} if state of this {@link AccordionPane} was changed
     */
    public boolean setCollapsed ( final boolean collapsed )
    {
        final Container parent = getParent ();
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).setPaneCollapsed ( getId (), collapsed );
    }

    /**
     * Asks this {@link AccordionPane} to collapse if possible.
     *
     * @return {@code true} if this {@link AccordionPane} was successfully collapsed, {@code false} otherwise
     */
    public boolean collapse ()
    {
        final Container parent = getParent ();
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).collapsePane ( getId () );
    }

    /**
     * Returns whether or not {@link AccordionPane} is in transition to either of two expansion states.
     *
     * @return {@code true} if {@link AccordionPane} is in transition, {@code false} otherwise
     */
    public boolean isInTransition ()
    {
        final Container parent = getParent ();
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).isPaneInTransition ( getId () );
    }

    /**
     * Returns minimum size in pixels of a single {@link AccordionPane}'s content.
     * Can be any value from {@code 0} to any reasonable amount of pixels you want for each {@link AccordionPane}'s content.
     * It is used instead of the content's preferred size to ensure that {@link WebAccordion} preserves it's preferred size at all times.
     * Otherwise whenever you expand/collapse something {@link WebAccordion} preferred size would vary wildly and negatively affect layout.
     *
     * @return minimum size in pixels of a single {@link AccordionPane}'s content
     */
    public int getMinimumPaneContentSize ()
    {
        final Container parent = getParent ();
        return parent instanceof WebAccordion ? ( ( WebAccordion ) parent ).getMinimumPaneContentSize () : 0;
    }

    /**
     * Adds {@link AccordionPaneListener}.
     *
     * @param listener {@link AccordionPaneListener} to add
     */
    public void addAccordionPaneListener ( @NotNull final AccordionPaneListener listener )
    {
        listenerList.add ( AccordionPaneListener.class, listener );
    }

    /**
     * Removes {@link AccordionPaneListener}.
     *
     * @param listener {@link AccordionPaneListener} to remove
     */
    public void removeAccordionPaneListener ( @NotNull final AccordionPaneListener listener )
    {
        listenerList.remove ( AccordionPaneListener.class, listener );
    }

    /**
     * Notifies that specified {@link AccordionPane} started expanding.
     *
     * @param accordion {@link WebAccordion}
     */
    public void fireExpanding ( @NotNull final WebAccordion accordion )
    {
        for ( final AccordionPaneListener listener : listenerList.getListeners ( AccordionPaneListener.class ) )
        {
            listener.expanding ( accordion, this );
        }
    }

    /**
     * Notifies when {@link WebAccordion} finished expanding.
     *
     * @param accordion {@link WebAccordion}
     */
    public void fireExpanded ( @NotNull final WebAccordion accordion )
    {
        for ( final AccordionPaneListener listener : listenerList.getListeners ( AccordionPaneListener.class ) )
        {
            listener.expanded ( accordion, this );
        }
    }

    /**
     * Notifies when {@link WebAccordion} starts to collapse.
     *
     * @param accordion {@link WebAccordion}
     */
    public void fireCollapsing ( @NotNull final WebAccordion accordion )
    {
        for ( final AccordionPaneListener listener : listenerList.getListeners ( AccordionPaneListener.class ) )
        {
            listener.collapsing ( accordion, this );
        }
    }

    /**
     * Notifies when {@link WebAccordion} finished collapsing.
     *
     * @param accordion {@link WebAccordion}
     */
    public void fireCollapsed ( @NotNull final WebAccordion accordion )
    {
        for ( final AccordionPaneListener listener : listenerList.getListeners ( AccordionPaneListener.class ) )
        {
            listener.collapsed ( accordion, this );
        }
    }
}