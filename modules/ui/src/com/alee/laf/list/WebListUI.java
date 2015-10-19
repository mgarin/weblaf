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
import com.alee.managers.style.*;
import com.alee.managers.tooltip.ToolTipProvider;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;
import java.awt.*;

/**
 * Custom UI for JList component.
 *
 * @author Mikle Garin
 */

public class WebListUI extends BasicListUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Style settings.
     */
    protected boolean mouseoverSelection = WebListStyle.mouseoverSelection;
    protected boolean mouseoverHighlight = WebListStyle.mouseoverHighlight;
    protected boolean scrollToSelection = WebListStyle.scrollToSelection;

    /**
     * Listeners.
     */
    protected ListSelectionListener selectionListener;
    protected ListMouseoverBehavior mouseoverBehavior;

    /**
     * Component painter.
     */
    protected ListPainter painter;

    /**
     * Runtime variables.
     */
    protected StyleId styleId = null;
    protected Insets margin = null;
    protected Insets padding = null;
    protected int mouseoverIndex = -1;

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

        // Selection listener
        selectionListener = new ListSelectionListener ()
        {
            @Override
            public void valueChanged ( final ListSelectionEvent e )
            {
                if ( isScrollToSelection () )
                {
                    if ( list.getSelectedIndex () != -1 )
                    {
                        final int index = list.getLeadSelectionIndex ();
                        final Rectangle selection = getCellBounds ( list, index, index );
                        if ( selection != null && !selection.intersects ( list.getVisibleRect () ) )
                        {
                            list.scrollRectToVisible ( selection );
                        }
                    }
                }
            }
        };
        list.addListSelectionListener ( selectionListener );

        // Mouseover behavior
        mouseoverBehavior = new ListMouseoverBehavior ( list, true )
        {
            @Override
            public void mouseoverChanged ( final Object previous, final Object current )
            {
                // Updating mouseover row
                final int previousIndex = mouseoverIndex;
                mouseoverIndex = indexOf ( current );

                // Updating selection
                if ( mouseoverSelection )
                {
                    if ( current != null )
                    {
                        list.setSelectedIndex ( mouseoverIndex );
                    }
                    else
                    {
                        list.clearSelection ();
                    }
                }

                // Repainting nodes according to mouseover changes
                // This occurs only if mouseover highlight is enabled
                if ( mouseoverHighlight )
                {
                    repaintCell ( previousIndex );
                    repaintCell ( mouseoverIndex );
                }

                // Updating custom WebLaF tooltip display state
                final ToolTipProvider provider = getToolTipProvider ();
                if ( provider != null )
                {
                    provider.mouseoverCellChanged ( list, previousIndex, 0, mouseoverIndex, 0 );
                }

                // Informing {@link com.alee.laf.list.WebList} about mouseover object change
                // This is performed here to avoid excessive listeners usage for the same purpose
                if ( list instanceof WebList )
                {
                    ( ( WebList ) list ).fireMouseoverChanged ( previous, current );
                }
            }

            /**
             * Returns index of the specified object inside the list.
             * @param current object to retrieve index for
             * @return index of the specified object inside the list
             */
            protected int indexOf ( final Object current )
            {
                final ListModel model = list.getModel ();
                if ( model instanceof WebListModel )
                {
                    return ( ( WebListModel ) model ).indexOf ( current );
                }
                else
                {
                    for ( int i = 0; i < model.getSize (); i++ )
                    {
                        if ( CompareUtils.equals ( model.getElementAt ( i ), current ) )
                        {
                            return i;
                        }
                    }
                    return -1;
                }
            }

            /**
             * Repaints specified row if it exists and it is visible.
             *
             * @param index index of cell to repaint
             */
            private void repaintCell ( final int index )
            {
                if ( index != -1 )
                {
                    final Rectangle cellBounds = list.getCellBounds ( index, index );
                    if ( cellBounds != null )
                    {
                        list.repaint ( cellBounds );
                    }
                }
            }
        };
        mouseoverBehavior.install ();

        // Applying skin
        StyleManager.installSkin ( list );
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
        StyleManager.uninstallSkin ( list );

        // Removing custom listeners
        mouseoverBehavior.uninstall ();
        mouseoverBehavior = null;
        list.removeListSelectionListener ( selectionListener );
        selectionListener = null;

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( list );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( list, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( list, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
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
     * Returns current mousover index.
     *
     * @return current mousover index
     */
    public int getMouseoverIndex ()
    {
        return mouseoverIndex;
    }

    /**
     * Returns whether or not cells should be selected on mouseover.
     *
     * @return true if cells should be selected on mouseover, false otherwise
     */
    public boolean isMouseoverSelection ()
    {
        return mouseoverSelection;
    }

    /**
     * Sets whether or not cells should be selected on mouseover.
     *
     * @param select whether or not cells should be selected on mouseover
     */
    public void setMouseoverSelection ( final boolean select )
    {
        this.mouseoverSelection = select;
        if ( select )
        {
            setMouseoverHighlight ( false );
        }
    }

    /**
     * Returns whether or not mouseover cells should be highlighted.
     *
     * @return true if mouseover cells should be highlighted, false otherwise
     */
    public boolean isMouseoverHighlight ()
    {
        return mouseoverHighlight;
    }

    /**
     * Sets whether or not mouseover cells should be highlighted.
     *
     * @param highlight whether or not mouseover cells should be highlighted
     */
    public void setMouseoverHighlight ( final boolean highlight )
    {
        this.mouseoverHighlight = highlight;
    }

    /**
     * Returns whether to scroll list down to selection automatically or not.
     *
     * @return true if list is being automatically scrolled to selection, false otherwise
     */
    public boolean isScrollToSelection ()
    {
        return scrollToSelection;
    }

    /**
     * Sets whether to scroll list down to selection automatically or not.
     *
     * @param scroll whether to scroll list down to selection automatically or not
     */
    public void setScrollToSelection ( final boolean scroll )
    {
        this.scrollToSelection = scroll;
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
     * Returns custom WebLaF tooltip provider.
     *
     * @return custom WebLaF tooltip provider
     */
    protected ToolTipProvider<? extends WebList> getToolTipProvider ()
    {
        return list != null && list instanceof WebList ? ( ( WebList ) list ).getToolTipProvider () : null;
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}