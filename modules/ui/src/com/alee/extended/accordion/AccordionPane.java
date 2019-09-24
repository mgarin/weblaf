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
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

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
public class AccordionPane extends WebPanel implements Identifiable
{
    /**
     * {@link AccordionPane} unique identifier.
     */
    @NotNull
    protected final String id;

    /**
     * Header {@link Component}.
     */
    @NotNull
    protected Component header;

    /**
     * Content {@link Component}.
     */
    @NotNull
    protected Component content;

    /**
     * Constructs new {@link AccordionPane}.
     *
     * @param accordion {@link WebAccordion} this {@link AccordionPane} is created for
     * @param id        unique {@link AccordionPane} identifier
     * @param icon      {@link AccordionPane} title icon
     * @param title     {@link AccordionPane} title
     * @param content   {@link Component} which this {@link AccordionPane} describes
     */
    public AccordionPane ( @NotNull final WebAccordion accordion, @NotNull final String id,
                           @Nullable final Icon icon, @Nullable final String title, @NotNull final Component content )
    {
        this ( StyleId.accordionPane.at ( accordion ), accordion, id, icon, title, content );
    }

    /**
     * Constructs new {@link AccordionPane}.
     *
     * @param styleId   {@link StyleId}
     * @param accordion {@link WebAccordion} this {@link AccordionPane} is created for
     * @param id        {@link AccordionPane} unique identifier
     * @param icon      {@link AccordionPane} title icon
     * @param title     {@link AccordionPane} title
     * @param content   {@link Component} which this {@link AccordionPane} describes
     */
    public AccordionPane ( @NotNull final StyleId styleId, @NotNull final WebAccordion accordion, @NotNull final String id,
                           @Nullable final Icon icon, @Nullable final String title, @NotNull final Component content )
    {
        super ( styleId );

        this.id = id;

        this.header = createHeaderComponent ( icon, title );
        add ( this.header );

        this.content = content;
        add ( this.content );
    }

    @NotNull
    @Override
    public String getId ()
    {
        return id;
    }

    /**
     * Returns header {@link Component}.
     *
     * @return header {@link Component}
     */
    @NotNull
    public Component getHeader ()
    {
        return header;
    }

    /**
     * Replaces header {@link Component}.
     *
     * @param header new header {@link Component}
     */
    public void setHeader ( @NotNull final Component header )
    {
        remove ( this.header );
        this.header = header;
        add ( this.header );
        SwingUtils.update ( this );
    }

    /**
     * Returns content {@link Component}.
     *
     * @return content {@link Component}
     */
    @NotNull
    public Component getContent ()
    {
        return content;
    }

    /**
     * Replaces content {@link Component}.
     *
     * @param content new content {@link Component}
     */
    public void setContent ( @NotNull final Component content )
    {
        remove ( this.content );
        this.content = content;
        add ( this.content );
        SwingUtils.update ( this );
    }

    /**
     * Returns newly created header {@link Component}.
     *
     * @param icon  header {@link Icon}
     * @param title header title
     * @return newly created header {@link Component}
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
     * Returns newly created title {@link Component}.
     *
     * @param header header {@link JComponent}
     * @param icon   header {@link Icon}
     * @param title  header title
     * @return newly created title {@link Component}
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
     * Returns newly created control {@link Component}.
     *
     * @param header header {@link JComponent}
     * @return newly created control {@link Component}
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
    protected void expandOrCollapse ( final AWTEvent e )
    {
        final Container parent = AccordionPane.this.getParent ();
        if ( parent instanceof WebAccordion )
        {
            final WebAccordion accordion = ( WebAccordion ) parent;
            if ( isExpanded () )
            {
                accordion.collapse ( getId () );
            }
            else
            {
                accordion.expand ( getId () );
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
     * Returns unique view identifier.
     *
     * @return unique view identifier
     */
    @NotNull
    public String id ()
    {
        return id;
    }

    /**
     * Returns {@link Component} which this {@link AccordionPane} describes.
     *
     * @return {@link Component} which this {@link AccordionPane} describes
     */
    @NotNull
    public Component component ()
    {
        return content;
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
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).isExpanded ( getId () );
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
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).setExpanded ( getId (), expanded );
    }

    /**
     * Asks this {@link AccordionPane} to expand if possible.
     *
     * @return {@code true} if this {@link AccordionPane} was successfully expanded, {@code false} otherwise
     */
    public boolean expand ()
    {
        final Container parent = getParent ();
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).expand ( getId () );
    }

    /**
     * Returns whether or not this {@link AccordionPane} is collapsed.
     *
     * @return {@code true} if this {@link AccordionPane} is collapsed, {@code false} otherwise
     */
    public boolean isCollapsed ()
    {
        final Container parent = getParent ();
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).isCollapsed ( getId () );
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
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).setCollapsed ( getId (), collapsed );
    }

    /**
     * Asks this {@link AccordionPane} to collapse if possible.
     *
     * @return {@code true} if this {@link AccordionPane} was successfully collapsed, {@code false} otherwise
     */
    public boolean collapse ()
    {
        final Container parent = getParent ();
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).collapse ( getId () );
    }

    /**
     * Returns whether or not {@link AccordionPane} is in transition to either of two expansion states.
     *
     * @return {@code true} if {@link AccordionPane} is in transition, {@code false} otherwise
     */
    public boolean isInTransition ()
    {
        final Container parent = getParent ();
        return parent instanceof WebAccordion && ( ( WebAccordion ) parent ).isInTransition ( getId () );
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
            listener.expanding ( accordion,this );
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
            listener.expanded ( accordion,this );
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
            listener.collapsing ( accordion,this );
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
            listener.collapsed ( accordion,this );
        }
    }
}