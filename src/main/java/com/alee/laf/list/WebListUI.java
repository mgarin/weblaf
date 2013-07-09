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

import com.alee.laf.StyleConstants;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
 * @since 1.4
 */

public class WebListUI extends BasicListUI
{
    /**
     * Style settings.
     */
    private boolean highlightRolloverCell = WebListStyle.highlightRolloverCell;
    private int selectionRound = WebListStyle.selectionRound;
    private int selectionShadeWidth = WebListStyle.selectionShadeWidth;
    private boolean autoScrollToSelection = WebListStyle.autoScrollToSelection;

    /**
     * List listeners.
     */
    private MouseAdapter mouseoverAdapter;
    private ListSelectionListener selectionListener;

    /**
     * Runtime variables.
     */
    private int rolloverIndex = -1;

    /**
     * Returns an instance of the WebListUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebListUI
     */
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebListUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( c );
        list.setOpaque ( true );
        list.setBackground ( WebListStyle.background );
        list.setForeground ( WebListStyle.foreground );

        // Rollover listener
        mouseoverAdapter = new MouseAdapter ()
        {
            public void mouseEntered ( MouseEvent e )
            {
                updateMouseover ( e );
            }

            public void mouseExited ( MouseEvent e )
            {
                updateMouseover ( e );
            }

            public void mouseMoved ( MouseEvent e )
            {
                updateMouseover ( e );
            }

            public void mouseDragged ( MouseEvent e )
            {
                updateMouseover ( e );
            }

            private void updateMouseover ( MouseEvent e )
            {
                // Ignore events if rollover highlight is disabled
                if ( !highlightRolloverCell )
                {
                    return;
                }

                // Determining highlighted cell index
                int index = list.locationToIndex ( e.getPoint () );
                Rectangle bounds = list.getCellBounds ( index, index );
                if ( bounds == null || !bounds.contains ( e.getPoint () ) || !list.isEnabled () )
                {
                    index = -1;
                }

                // Updating rollover cell
                if ( rolloverIndex != index )
                {
                    int oldIndex = rolloverIndex;
                    rolloverIndex = index;
                    if ( rolloverIndex != -1 )
                    {
                        Rectangle cellBounds = list.getCellBounds ( rolloverIndex, rolloverIndex );
                        if ( cellBounds != null )
                        {
                            list.repaint ( cellBounds );
                        }
                        else
                        {
                            list.repaint ();
                        }
                    }
                    if ( oldIndex != -1 )
                    {
                        Rectangle cellBounds = list.getCellBounds ( oldIndex, oldIndex );
                        if ( cellBounds != null )
                        {
                            list.repaint ( cellBounds );
                        }
                        else
                        {
                            list.repaint ();
                        }
                    }
                }
            }
        };
        list.addMouseListener ( mouseoverAdapter );
        list.addMouseMotionListener ( mouseoverAdapter );

        // Selection listener
        selectionListener = new ListSelectionListener ()
        {
            public void valueChanged ( ListSelectionEvent e )
            {
                if ( autoScrollToSelection )
                {
                    if ( list.getSelectedIndex () != -1 )
                    {
                        final int index = list.getLeadSelectionIndex ();
                        Rectangle selection = getCellBounds ( list, index, index );
                        if ( !selection.intersects ( list.getVisibleRect () ) )
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
    public void uninstallUI ( JComponent c )
    {
        list.removeMouseListener ( mouseoverAdapter );
        list.removeMouseMotionListener ( mouseoverAdapter );
        list.removeListSelectionListener ( selectionListener );

        super.uninstallUI ( c );
    }

    /**
     * Returns whether to highlight rollover cell or not.
     *
     * @return true if rollover cell is being highlighted, false otherwise
     */
    public boolean isHighlightRolloverCell ()
    {
        return highlightRolloverCell;
    }

    /**
     * Sets whether to highlight rollover cell or not.
     *
     * @param highlightRolloverCell whether to highlight rollover cell or not
     */
    public void setHighlightRolloverCell ( boolean highlightRolloverCell )
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
    public void setSelectionRound ( int selectionRound )
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
    public void setSelectionShadeWidth ( int selectionShadeWidth )
    {
        this.selectionShadeWidth = selectionShadeWidth;
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
    public void setAutoScrollToSelection ( boolean autoScrollToSelection )
    {
        this.autoScrollToSelection = autoScrollToSelection;
    }

    /**
     * Paints list content.
     *
     * @param g graphics context
     * @param c painted component
     */
    public void paint ( Graphics g, JComponent c )
    {
        super.paint ( g, c );

        // todo Visual shade effect
        //        Rectangle vr = c.getVisibleRect ();
        //        if ( vr.y > 0 )
        //        {
        //            final int shadeWidth = 10;
        //            float opacity = shadeWidth > vr.y ? 1f - ( float ) ( shadeWidth - vr.y ) / shadeWidth : 1f;
        //            NinePatchIcon npi = NinePatchUtils.getShadeIcon ( shadeWidth, 0, opacity );
        //            Dimension ps = npi.getPreferredSize ();
        //            npi.paintIcon ( ( Graphics2D ) g, vr.x - shadeWidth, vr.y + shadeWidth - ps.height, vr.width + shadeWidth * 2, ps.height );
        //        }
    }

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
    protected void paintCell ( Graphics g, int index, Rectangle rowBounds, ListCellRenderer cellRenderer, ListModel dataModel,
                               ListSelectionModel selModel, int leadIndex )
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
        final boolean cellHasFocus = list.hasFocus () && ( index == leadIndex );
        final boolean isSelected = selModel.isSelectedIndex ( index );

        if ( isSelected || index == rolloverIndex )
        {
            final Graphics2D g2d = ( Graphics2D ) g;
            final Composite oc = LafUtils.setupAlphaComposite ( g2d, 0.35f, !isSelected );

            LafUtils.drawCustomWebBorder ( g2d, list,
                    new RoundRectangle2D.Double ( rowBounds.x + selectionShadeWidth, rowBounds.y + selectionShadeWidth,
                            rowBounds.width - selectionShadeWidth * 2 - 1, rowBounds.height - selectionShadeWidth * 2 - 1,
                            selectionRound * 2, selectionRound * 2 ), StyleConstants.shadeColor, selectionShadeWidth, true, true );

            LafUtils.restoreComposite ( g2d, oc, !isSelected );
        }

        final Component rendererComponent = cellRenderer.getListCellRendererComponent ( list, value, index, isSelected, cellHasFocus );

        final int cx = rowBounds.x;
        final int cy = rowBounds.y;
        final int cw = rowBounds.width;
        final int ch = rowBounds.height;
        rendererPane.paintComponent ( g, rendererComponent, list, cx, cy, cw, ch, true );
    }
}