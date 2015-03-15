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

import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.tooltip.ToolTipProvider;
import com.alee.utils.GeometryUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Custom UI for JList component.
 *
 * @author Mikle Garin
 */

public class WebListUI extends BasicListUI
{
    /**
     * todo 1. Visual shade effect on sides when scrollable
     * todo 2. Even-odd cells highlight
     */

    /**
     * Style settings.
     */
    protected boolean decorateSelection = WebListStyle.decorateSelection;
    protected boolean highlightRolloverCell = WebListStyle.highlightRolloverCell;
    protected int selectionRound = WebListStyle.selectionRound;
    protected int selectionShadeWidth = WebListStyle.selectionShadeWidth;
    protected boolean webColoredSelection = WebListStyle.webColoredSelection;
    protected Color selectionBorderColor = WebListStyle.selectionBorderColor;
    protected Color selectionBackgroundColor = WebListStyle.selectionBackgroundColor;
    protected boolean autoScrollToSelection = WebListStyle.autoScrollToSelection;

    /**
     * List listeners.
     */
    protected MouseAdapter mouseAdapter;
    protected ListSelectionListener selectionListener;

    /**
     * Runtime variables.
     */
    protected int rolloverIndex = -1;

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
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( list );
        LookAndFeel.installProperty ( list, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.TRUE );
        list.setBackground ( new ColorUIResource ( WebListStyle.background ) );
        list.setForeground ( new ColorUIResource ( WebListStyle.foreground ) );

        // Rollover listener
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                updateMouseover ( e );
            }

            @Override
            public void mouseDragged ( final MouseEvent e )
            {
                updateMouseover ( e );
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                clearMouseover ();
            }

            private void updateMouseover ( final MouseEvent e )
            {
                final int index = list.locationToIndex ( e.getPoint () );
                final Rectangle bounds = list.getCellBounds ( index, index );
                if ( list.isEnabled () && bounds != null && bounds.contains ( e.getPoint () ) )
                {
                    if ( rolloverIndex != index )
                    {
                        updateRolloverCell ( rolloverIndex, index );
                    }
                }
                else
                {
                    clearMouseover ();
                }
            }

            private void clearMouseover ()
            {
                if ( rolloverIndex != -1 )
                {
                    updateRolloverCell ( rolloverIndex, -1 );
                }
            }

            private void updateRolloverCell ( final int oldIndex, final int newIndex )
            {
                // Updating rollover index
                rolloverIndex = newIndex;

                // Repaint list only if rollover index is used
                if ( decorateSelection && highlightRolloverCell )
                {
                    final Rectangle oldBounds = list.getCellBounds ( oldIndex, oldIndex );
                    final Rectangle newBounds = list.getCellBounds ( newIndex, newIndex );
                    final Rectangle rect = GeometryUtils.getContainingRect ( oldBounds, newBounds );
                    if ( rect != null )
                    {
                        list.repaint ( rect );
                    }
                }

                // Updating custom WebLaF tooltip display state
                final ToolTipProvider provider = getToolTipProvider ();
                if ( provider != null )
                {
                    provider.rolloverCellChanged ( list, oldIndex, 0, newIndex, 0 );
                }
            }
        };
        list.addMouseListener ( mouseAdapter );
        list.addMouseMotionListener ( mouseAdapter );

        // Selection listener
        selectionListener = new ListSelectionListener ()
        {
            @Override
            public void valueChanged ( final ListSelectionEvent e )
            {
                if ( autoScrollToSelection )
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
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        list.removeMouseListener ( mouseAdapter );
        list.removeMouseMotionListener ( mouseAdapter );
        list.removeListSelectionListener ( selectionListener );
        super.uninstallUI ( c );
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

    /**
     * Returns whether should decorate selected and rollover cells or not.
     *
     * @return true if should decorate selected and rollover cells, false otherwise
     */
    public boolean isDecorateSelection ()
    {
        return decorateSelection;
    }

    /**
     * Sets whether should decorate selected and rollover cells or not.
     *
     * @param decorateSelection whether should decorate selected and rollover cells or not
     */
    public void setDecorateSelection ( final boolean decorateSelection )
    {
        this.decorateSelection = decorateSelection;
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
     * Returns cells selection rounding.
     *
     * @return cells selection rounding
     */
    public int getSelectionRound ()
    {
        return selectionRound;
    }

    /**
     * Sets cells selection rounding.
     *
     * @param selectionRound new cells selection rounding
     */
    public void setSelectionRound ( final int selectionRound )
    {
        this.selectionRound = selectionRound;
    }

    /**
     * Returns cells selection shade width.
     *
     * @return cells selection shade width
     */
    public int getSelectionShadeWidth ()
    {
        return selectionShadeWidth;
    }

    /**
     * Sets cells selection shade width.
     *
     * @param selectionShadeWidth new cells selection shade width
     */
    public void setSelectionShadeWidth ( final int selectionShadeWidth )
    {
        this.selectionShadeWidth = selectionShadeWidth;
    }

    /**
     * Returns whether selection should be web-colored or not.
     * In case it is not web-colored selectionBackgroundColor value will be used as background color.
     *
     * @return true if selection should be web-colored, false otherwise
     */
    public boolean isWebColoredSelection ()
    {
        return webColoredSelection;
    }

    /**
     * Sets whether selection should be web-colored or not.
     * In case it is not web-colored selectionBackgroundColor value will be used as background color.
     *
     * @param webColored whether selection should be web-colored or not
     */
    public void setWebColoredSelection ( final boolean webColored )
    {
        this.webColoredSelection = webColored;
    }

    /**
     * Returns selection border color.
     *
     * @return selection border color
     */
    public Color getSelectionBorderColor ()
    {
        return selectionBorderColor;
    }

    /**
     * Sets selection border color.
     *
     * @param color selection border color
     */
    public void setSelectionBorderColor ( final Color color )
    {
        this.selectionBorderColor = color;
    }

    /**
     * Returns selection background color.
     * It is used only when webColoredSelection is set to false.
     *
     * @return selection background color
     */
    public Color getSelectionBackgroundColor ()
    {
        return selectionBackgroundColor;
    }

    /**
     * Sets selection background color.
     * It is used only when webColoredSelection is set to false.
     *
     * @param color selection background color
     */
    public void setSelectionBackgroundColor ( final Color color )
    {
        this.selectionBackgroundColor = color;
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

    //    /**
    //     * Paints list content.
    //     *
    //     * @param g graphics context
    //     * @param c painted component
    //     */
    //    @Override
    //    public void paint ( final Graphics g, final JComponent c )
    //    {
    //        super.paint ( g, c );
    //
    //        Rectangle vr = c.getVisibleRect ();
    //        if ( vr.y > 0 )
    //        {
    //            final int shadeWidth = 10;
    //            float opacity = shadeWidth > vr.y ? 1f - ( float ) ( shadeWidth - vr.y ) / shadeWidth : 1f;
    //            NinePatchIcon npi = NinePatchUtils.getShadeIcon ( shadeWidth, 0, opacity );
    //            Dimension ps = npi.getPreferredSize ();
    //            npi.paintIcon ( ( Graphics2D ) g, vr.x - shadeWidth, vr.y + shadeWidth - ps.height, vr.width + shadeWidth * 2, ps.height );
    //        }
    //    }

    /**
     * Paint one List cell: compute the relevant state, get the "rubber stamp" cell renderer component, and then use the CellRendererPane
     * to paint it. Subclasses may want to override this method rather than paint().
     *
     * @param g            graphics context
     * @param index        cell index
     * @param rowBounds    cell bounds
     * @param cellRenderer cell renderer
     * @param dataModel    list model
     * @param selModel     list selection model
     * @param leadIndex    lead cell index
     * @see #paint
     */
    @Override
    protected void paintCell ( final Graphics g, final int index, final Rectangle rowBounds, final ListCellRenderer cellRenderer,
                               final ListModel dataModel, final ListSelectionModel selModel, final int leadIndex )
    {
        //        if ( list.getLayoutOrientation () == WebList.VERTICAL && ( evenLineColor != null || oddLineColor != null ) )
        //        {
        //            boolean even = index % 2 == 0;
        //            if ( even && evenLineColor != null )
        //            {
        //                g.setColor ( evenLineColor );
        //                g.fillRect ( rowBounds.x, rowBounds.y, rowBounds.width, rowBounds.height );
        //            }
        //            if ( !even && oddLineColor != null )
        //            {
        //                g.setColor ( oddLineColor );
        //                g.fillRect ( rowBounds.x, rowBounds.y, rowBounds.width, rowBounds.height );
        //            }
        //        }

        final Object value = dataModel.getElementAt ( index );
        final boolean isSelected = selModel.isSelectedIndex ( index );

        if ( decorateSelection && ( isSelected || index == rolloverIndex ) )
        {
            final Graphics2D g2d = ( Graphics2D ) g;
            final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, 0.35f, !isSelected );

            final Rectangle rect = new Rectangle ( rowBounds );
            rect.x += selectionShadeWidth;
            rect.y += selectionShadeWidth;
            rect.width -= selectionShadeWidth * 2 + ( selectionBorderColor != null ? 1 : 0 );
            rect.height -= selectionShadeWidth * 2 + ( selectionBorderColor != null ? 1 : 0 );

            LafUtils.drawCustomWebBorder ( g2d, list,
                    new RoundRectangle2D.Double ( rect.x, rect.y, rect.width, rect.height, selectionRound * 2, selectionRound * 2 ),
                    StyleConstants.shadeColor, selectionShadeWidth, true, webColoredSelection, selectionBorderColor, selectionBorderColor,
                    selectionBackgroundColor );

            GraphicsUtils.restoreComposite ( g2d, oc, !isSelected );
        }

        final boolean cellHasFocus = list.hasFocus () && ( index == leadIndex );
        final Component rendererComponent = cellRenderer.getListCellRendererComponent ( list, value, index, isSelected, cellHasFocus );
        rendererPane.paintComponent ( g, rendererComponent, list, rowBounds.x, rowBounds.y, rowBounds.width, rowBounds.height, true );
    }
}