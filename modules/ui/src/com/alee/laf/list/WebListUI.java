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

package com.alee.laf.list;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.MarginSupport;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;
import java.awt.*;

/**
 * Custom UI for JList component.
 *
 * @author Mikle Garin
 */

public class WebListUI extends BasicListUI implements Styleable, ShapeProvider, MarginSupport
{
    /**
     * Style settings.
     */
    protected boolean highlightRolloverCell = WebListStyle.highlightRolloverCell;
    protected boolean autoScrollToSelection = WebListStyle.autoScrollToSelection;

    /**
     * Component painter.
     */
    protected ListPainter painter;

    /**
     * Runtime variables.
     */
    protected StyleId styleId = null;
    protected Insets margin = null;

    /**
     * Returns an instance of the WebListUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebListUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebListUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.applySkin ( list );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.removeSkin ( list );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( list );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final StyleId id )
    {
        StyleManager.setStyleId ( list, id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( list, painter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * Returns list painter.
     *
     * @return list painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets list painter.
     * Pass null to remove list painter.
     *
     * @param painter new list painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( list, new DataRunnable<ListPainter> ()
        {
            @Override
            public void run ( final ListPainter newPainter )
            {
                WebListUI.this.painter = newPainter;
            }
        }, this.painter, painter, ListPainter.class, AdaptiveListPainter.class );
    }

    /**
     * Returns whether should highlight rollover cell or not.
     *
     * @return true if rollover cell is being highlighted, false otherwise
     */
    public boolean isHighlightRolloverCell ()
    {
        return highlightRolloverCell;
    }

    /**
     * Sets whether should highlight rollover cell or not.
     *
     * @param highlightRolloverCell whether should highlight rollover cell or not
     */
    public void setHighlightRolloverCell ( final boolean highlightRolloverCell )
    {
        this.highlightRolloverCell = highlightRolloverCell;
    }

    /**
     * Returns whether to scroll list down to selection automatically or not.
     *
     * @return true if list is being automatically scrolled to selection, false otherwise
     */
    public boolean isAutoScrollToSelection ()
    {
        return autoScrollToSelection;
    }

    /**
     * Sets whether to scroll list down to selection automatically or not.
     *
     * @param autoScrollToSelection whether to scroll list down to selection automatically or not
     */
    public void setAutoScrollToSelection ( final boolean autoScrollToSelection )
    {
        this.autoScrollToSelection = autoScrollToSelection;
    }

    /**
     * Force list to update layout.
     */
    public void setNeedUpdateLayoutState ()
    {
        updateLayoutStateNeeded++;
    }

    /**
     * Returns tree cell renderer pane.
     *
     * @return tree cell renderer pane
     */
    public CellRendererPane getCellRendererPane ()
    {
        return rendererPane;
    }

    /**
     * Paints list content.
     *
     * @param g graphics context
     * @param c painted component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.prepareToPaint ( updateLayoutStateNeeded != 0 );
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c, this );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}