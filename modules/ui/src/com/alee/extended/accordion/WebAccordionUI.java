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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.extended.collapsible.AbstractHeaderPanel;
import com.alee.managers.style.StyleManager;
import com.alee.painter.PainterSupport;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link WebAccordion} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebAccordion">How to use WebAccordion</a>
 * @see WebAccordion
 * @see AccordionPane
 * @see AccordionModel
 * @see WebAccordionModel
 */
public class WebAccordionUI<C extends WebAccordion> extends WAccordionUI<C> implements PropertyChangeListener, AccordionListener
{
    /**
     * Returns an instance of the {@link WebAccordionUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebAccordionUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebAccordionUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( accordion );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( accordion );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    protected void installListeners ()
    {
        accordion.addPropertyChangeListener ( this );
        accordion.addAccordionListener ( this );
    }

    @Override
    protected void uninstallListeners ()
    {
        accordion.removeAccordionListener ( this );
        accordion.removePropertyChangeListener ( this );
    }

    @Override
    public void propertyChange ( @NotNull final PropertyChangeEvent event )
    {
        final String property = event.getPropertyName ();
        if ( Objects.equals ( property, WebAccordion.ANIMATED_PROPERTY ) )
        {
            updateDecorationStates ();
        }
        else if ( Objects.equals ( property, WebAccordion.HEADER_POSITION_PROPERTY ) )
        {
            updateDecorationStates ();
        }
        else if ( Objects.equals ( property, WebAccordion.MINIMUM_PANE_CONTENT_SIZE_PROPERTY ) )
        {
            SwingUtils.update ( accordion );
        }
        else if ( Objects.equals ( property, WebAccordion.PANES_PROPERTY ) )
        {
            updateDecorationStates ();
        }
    }

    @Override
    public void expanding ( @NotNull final WebAccordion accordion, @NotNull final AccordionPane pane )
    {
        updateDecorationStates ( pane );
    }

    @Override
    public void expanded ( @NotNull final WebAccordion accordion, @NotNull final AccordionPane pane )
    {
        updateDecorationStates ( pane );
    }

    @Override
    public void collapsing ( @NotNull final WebAccordion accordion, @NotNull final AccordionPane pane )
    {
        updateDecorationStates ( pane );
    }

    @Override
    public void collapsed ( @NotNull final WebAccordion accordion, @NotNull final AccordionPane pane )
    {
        updateDecorationStates ( pane );
    }

    /**
     * Updates decoration states for all UI elements.
     */
    protected void updateDecorationStates ()
    {
        for ( final AccordionPane pane : accordion.getPanes () )
        {
            updateDecorationStates ( pane );
        }
    }

    /**
     * Updates decoration states for the specified {@link AccordionPane}.
     *
     * @param pane {@link AccordionPane} to update decoration states for
     */
    protected void updateDecorationStates ( @NotNull final AccordionPane pane )
    {
        final Component header = pane.getHeader ();
        if ( header instanceof AbstractHeaderPanel )
        {
            final AbstractHeaderPanel headerPanel = ( AbstractHeaderPanel ) header;
            DecorationUtils.fireStatesChanged ( headerPanel.getTitle () );
            DecorationUtils.fireStatesChanged ( headerPanel.getControl () );
        }
        DecorationUtils.fireStatesChanged ( header );
        DecorationUtils.fireStatesChanged ( pane );
    }

    @Override
    public int getBaseline ( @NotNull final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, width, height );
    }

    @NotNull
    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        PainterSupport.paint ( g, c, this );
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c );
    }
}