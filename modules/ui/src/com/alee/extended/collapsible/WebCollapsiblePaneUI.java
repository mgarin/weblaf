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
import com.alee.api.jdk.Consumer;
import com.alee.api.jdk.Objects;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link WebCollapsiblePane} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebCollapsiblePane">How to use WebCollapsiblePane</a>
 * @see WebCollapsiblePane
 */
public class WebCollapsiblePaneUI<C extends WebCollapsiblePane> extends WCollapsiblePaneUI<C>
        implements PropertyChangeListener, CollapsiblePaneListener, ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( CollapsiblePanePainter.class )
    protected ICollapsiblePanePainter painter;

    /**
     * Returns an instance of the {@link WebCollapsiblePaneUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebCollapsiblePaneUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebCollapsiblePaneUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( pane );

        // Installing settings
        installComponents ();
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling settings
        uninstallComponents ();

        // Uninstalling applied skin
        StyleManager.uninstallSkin ( pane );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    protected void installListeners ()
    {
        pane.addPropertyChangeListener ( this );
        pane.addCollapsiblePaneListener ( this );
    }

    @Override
    protected void uninstallListeners ()
    {
        pane.removeCollapsiblePaneListener ( this );
        pane.removePropertyChangeListener ( this );
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent event )
    {
        final String property = event.getPropertyName ();
        if ( Objects.equals ( property, WebCollapsiblePane.HEADER_POSITION_PROPERTY ) )
        {
            updateDecorationStates ();
        }
        else if ( Objects.equals ( property, WebCollapsiblePane.HEADER_COMPONENT_PROPERTY ) )
        {
            final Component oldHeader = ( Component ) event.getOldValue ();
            if ( oldHeader != null )
            {
                pane.remove ( oldHeader );
            }
            final Component newHeader = ( Component ) event.getNewValue ();
            if ( newHeader != null )
            {
                pane.add ( newHeader );
            }
            SwingUtils.update ( pane );
        }
        else if ( Objects.equals ( property, WebCollapsiblePane.TITLE_PROPERTY ) )
        {
            SwingUtils.update ( pane.getHeaderComponent () );
        }
        else if ( Objects.equals ( property, WebCollapsiblePane.TITLE_COMPONENT_PROPERTY ) )
        {
            final Component headerComponent = pane.getHeaderComponent ();
            if ( headerComponent instanceof AbstractHeaderPanel )
            {
                final AbstractHeaderPanel headerPanel = ( AbstractHeaderPanel ) headerComponent;
                final Component oldTitle = ( Component ) event.getOldValue ();
                if ( oldTitle != null )
                {
                    headerPanel.remove ( oldTitle );
                }
                final Component newTitle = ( Component ) event.getNewValue ();
                if ( newTitle != null )
                {
                    headerPanel.add ( newTitle, HeaderLayout.TITLE );
                }
                SwingUtils.update ( headerPanel );
            }
        }
        else if ( Objects.equals ( property, WebCollapsiblePane.CONTROL_COMPONENT_PROPERTY ) )
        {
            final Component headerComponent = pane.getHeaderComponent ();
            if ( headerComponent instanceof AbstractHeaderPanel )
            {
                final AbstractHeaderPanel headerPanel = ( AbstractHeaderPanel ) headerComponent;

                final Component oldControl = ( Component ) event.getOldValue ();
                if ( oldControl != null )
                {
                    headerPanel.remove ( oldControl );
                }
                final Component newControl = ( Component ) event.getNewValue ();
                if ( newControl != null )
                {
                    headerPanel.add ( newControl, HeaderLayout.CONTROL );
                }
                SwingUtils.update ( headerPanel );
            }
        }
        else if ( Objects.equals ( property, WebCollapsiblePane.CONTENT_PROPERTY ) )
        {
            final Component oldContent = ( Component ) event.getOldValue ();
            if ( oldContent != null )
            {
                pane.remove ( oldContent );
            }
            final Component newContent = ( Component ) event.getNewValue ();
            if ( newContent != null )
            {
                pane.add ( newContent );
            }
            SwingUtils.update ( pane );
        }
    }

    @Override
    public void expanding ( @NotNull final WebCollapsiblePane pane )
    {
        updateDecorationStates ();
    }

    @Override
    public void expanded ( @NotNull final WebCollapsiblePane pane )
    {
        updateDecorationStates ();
    }

    @Override
    public void collapsing ( @NotNull final WebCollapsiblePane pane )
    {
        updateDecorationStates ();
    }

    @Override
    public void collapsed ( @NotNull final WebCollapsiblePane pane )
    {
        updateDecorationStates ();
    }

    /**
     * Updates decoration states for UI elements.
     */
    protected void updateDecorationStates ()
    {
        if ( pane.getHeaderComponent () instanceof AbstractHeaderPanel )
        {
            DecorationUtils.fireStatesChanged ( pane.getTitleComponent () );
            DecorationUtils.fireStatesChanged ( pane.getControlComponent () );
        }
        DecorationUtils.fireStatesChanged ( pane.getHeaderComponent () );
    }

    /**
     * Installs {@link WebCollapsiblePane} decoration elements.
     * We only request particular UI elements here because actual installation will happen on property change events.
     */
    protected void installComponents ()
    {
        final Component headerComponent = pane.getHeaderComponent ();
        if ( headerComponent instanceof AbstractHeaderPanel )
        {
            pane.getTitleComponent ();
            pane.getControlComponent ();
        }
    }

    /**
     * Uninstalls {@link WebCollapsiblePane} decoration elements.
     * We have to manually remove UI elements from the structure since we don't want to remove them from collapsible itself.
     */
    protected void uninstallComponents ()
    {
        final Component headerComponent = pane.getHeaderComponent ();
        if ( headerComponent instanceof AbstractHeaderPanel )
        {
            final AbstractHeaderPanel headerPanel = ( AbstractHeaderPanel ) headerComponent;
            final Component titleComponent = pane.getTitleComponent ();
            if ( titleComponent != null )
            {
                headerPanel.remove ( titleComponent );
            }
            final Component controlComponent = pane.getControlComponent ();
            if ( controlComponent != null )
            {
                headerPanel.remove ( controlComponent );
            }
        }
        pane.removeAll ();
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( pane, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( pane, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( pane, painter, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( pane );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( pane, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( pane );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( pane, padding );
    }

    /**
     * Returns collapsible pane painter.
     *
     * @return collapsible pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets collapsible pane painter.
     * Pass null to remove collapsible pane painter.
     *
     * @param painter new collapsible pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( pane, this, new Consumer<ICollapsiblePanePainter> ()
        {
            @Override
            public void accept ( final ICollapsiblePanePainter newPainter )
            {
                WebCollapsiblePaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, ICollapsiblePanePainter.class, AdaptiveCollapsiblePanePainter.class );
    }

    @Override
    public int getBaseline ( final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, painter, width, height );
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this, painter );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, painter );
    }
}