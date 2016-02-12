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

import com.alee.managers.style.*;
import com.alee.managers.style.skin.web.WebListPainter;
import com.alee.managers.tooltip.ToolTipProvider;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.CompareUtils;
import com.alee.utils.ReflectUtils;
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
    /*
     * Static fields from BasicListUI
     */
    public final static int heightChanged = 1 << 8;
    public final static int widthChanged = 1 << 9;
    /**
     * Style settings.
     */
    protected ListSelectionStyle selectionStyle;
    protected boolean selectOnHover;
    protected boolean scrollToSelection;

    /**
     * Listeners.
     */
    protected ListSelectionListener selectionTracker;
    protected ListItemHoverBehavior hoverCellTracker;

    /**
     * Component painter.
     */
    protected IListPainter painter;

    /**
     * Runtime variables.
     */
    protected Insets margin = null;
    protected Insets padding = null;
    protected int hoverIndex = -1;

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
        selectionTracker = new ListSelectionListener ()
        {
            @Override
            public void valueChanged ( final ListSelectionEvent e )
            {
                if ( isScrollToSelection () && list.getSelectedIndex () != -1 )
                {
                    final int index = list.getLeadSelectionIndex ();
                    final Rectangle selection = getCellBounds ( list, index, index );
                    if ( selection != null && !selection.intersects ( list.getVisibleRect () ) )
                    {
                        list.scrollRectToVisible ( selection );
                    }
                }
            }
        };
        list.addListSelectionListener ( selectionTracker );

        // Hover behavior
        hoverCellTracker = new ListItemHoverBehavior ( list, true )
        {
            @Override
            public void hoverChanged ( final Object previous, final Object current )
            {
                // Updating hover row
                final int previousIndex = hoverIndex;
                hoverIndex = indexOf ( current );

                // Updating selection
                if ( selectOnHover )
                {
                    if ( current != null )
                    {
                        list.setSelectedIndex ( hoverIndex );
                    }
                    else
                    {
                        list.clearSelection ();
                    }
                }

                // Repainting nodes according to hover changes
                // This occurs only if hover highlight is enabled
                if ( painter != null && painter.isHoverDecorationSupported () )
                {
                    repaintCell ( previousIndex );
                    repaintCell ( hoverIndex );
                }

                // Updating custom WebLaF tooltip display state
                final ToolTipProvider provider = getToolTipProvider ();
                if ( provider != null )
                {
                    provider.hoverCellChanged ( list, previousIndex, 0, hoverIndex, 0 );
                }

                // Informing {@link com.alee.laf.list.WebList} about hover object change
                // This is performed here to avoid excessive listeners usage for the same purpose
                if ( list instanceof WebList )
                {
                    ( ( WebList ) list ).fireHoverChanged ( previous, current );
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
        hoverCellTracker.install ();

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
        hoverCellTracker.uninstall ();
        hoverCellTracker = null;
        list.removeListSelectionListener ( selectionTracker );
        selectionTracker = null;

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
        PainterSupport.setPainter ( list, new DataRunnable<IListPainter> ()
        {
            @Override
            public void run ( final IListPainter newPainter )
            {
                WebListUI.this.painter = newPainter;
            }
        }, this.painter, painter, IListPainter.class, AdaptiveListPainter.class );
    }

    /**
     * Returns current mousover index.
     *
     * @return current mousover index
     */
    public int getHoverIndex ()
    {
        return hoverIndex;
    }

    /**
     * Returns list selection style.
     *
     * @return list selection style
     */
    public ListSelectionStyle getSelectionStyle ()
    {
        return selectionStyle;
    }

    /**
     * Sets list selection style.
     *
     * @param style list selection style
     */
    public void setSelectionStyle ( final ListSelectionStyle style )
    {
        this.selectionStyle = style;
    }

    /**
     * Returns whether or not cells should be selected on hover.
     *
     * @return true if cells should be selected on hover, false otherwise
     */
    public boolean isSelectOnHover ()
    {
        return selectOnHover;
    }

    /**
     * Sets whether or not cells should be selected on hover.
     *
     * @param select whether or not cells should be selected on hover
     */
    public void setSelectOnHover ( final boolean select )
    {
        this.selectOnHover = select;
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
    public void requestLayoutStateUpdate ()
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
            switch ( getLayoutOrientation () )
            {
                case JList.VERTICAL_WRAP:
                    if ( list.getHeight () != getListHeight () )
                    {
                        updateLayoutStateNeeded |= heightChanged;
                        ( ( WebListPainter ) painter ).redrawList ();
                    }
                    break;
                case JList.HORIZONTAL_WRAP:
                    if ( list.getWidth () != getListWidth () )
                    {
                        updateLayoutStateNeeded |= widthChanged;
                        ( ( WebListPainter ) painter ).redrawList ();
                    }
                    break;
                default:
                    break;
            }

            maybeUpdateLayoutState ();

            ( ( WebListPainter ) painter )
                    .prepareToPaint ( getLayoutOrientation (), getListHeight (), getListWidth (), getColumnCount (), getRowsPerColumn (),
                            getPreferredHeight (), cellWidth, cellHeight, cellHeights );
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c, this );
        }
    }

    /**
     * Recompute the value of cellHeight or cellHeights based
     * and cellWidth, based on the current font and the current
     * values of fixedCellWidth, fixedCellHeight, and prototypeCellValue.
     */
    @Override
    public void updateLayoutState ()
    {
        /* If both JList fixedCellWidth and fixedCellHeight have been
         * set, then initialize cellWidth and cellHeight, and set
         * cellHeights to null.
         */

        final int fixedCellHeight = list.getFixedCellHeight ();
        final int fixedCellWidth = list.getFixedCellWidth ();

        cellWidth = ( fixedCellWidth != -1 ) ? fixedCellWidth : -1;

        if ( fixedCellHeight != -1 )
        {
            cellHeight = fixedCellHeight;
            cellHeights = null;
        }
        else
        {
            cellHeight = -1;
            cellHeights = new int[ list.getModel ().getSize () ];
        }

        /* If either of  JList fixedCellWidth and fixedCellHeight haven't
         * been set, then initialize cellWidth and cellHeights by
         * scanning through the entire model.  Note: if the renderer is
         * null, we just set cellWidth and cellHeights[*] to zero,
         * if they're not set already.
         */

        if ( ( fixedCellWidth == -1 ) || ( fixedCellHeight == -1 ) )
        {

            final ListModel dataModel = list.getModel ();
            final int dataModelSize = dataModel.getSize ();
            final ListCellRenderer renderer = list.getCellRenderer ();

            if ( renderer != null )
            {
                for ( int index = 0; index < dataModelSize; index++ )
                {
                    final Object value = dataModel.getElementAt ( index );
                    final Component c = renderer.getListCellRendererComponent ( list, value, index, false, false );
                    rendererPane.add ( c );
                    final Dimension cellSize = c.getPreferredSize ();
                    if ( fixedCellWidth == -1 )
                    {
                        cellWidth = Math.max ( cellSize.width, cellWidth );
                    }
                    if ( fixedCellHeight == -1 )
                    {
                        cellHeights[ index ] = cellSize.height;
                    }
                }
            }
            else
            {
                if ( cellWidth == -1 )
                {
                    cellWidth = 0;
                }
                if ( cellHeights == null )
                {
                    cellHeights = new int[ dataModelSize ];
                }
                for ( int index = 0; index < dataModelSize; index++ )
                {
                    cellHeights[ index ] = 0;
                }
            }
        }

        setColumnCount ( 1 );
        if ( getLayoutOrientation () != JList.VERTICAL )
        {
            updateHorizontalLayoutState ( fixedCellWidth, fixedCellHeight );
        }
    }


    /**
     * Invoked when the list is layed out horizontally to determine how many columns to create. This updates the {@code rowsPerColumn},
     * {@code columnCount}, {@code preferredHeight} and potentially {@code cellHeight} instance variables.
     *
     * @param fixedCellWidth  fixed list item width
     * @param fixedCellHeight fixed list item height
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected void updateHorizontalLayoutState ( final int fixedCellWidth, final int fixedCellHeight )
    {
        final int visRows = list.getVisibleRowCount ();
        final int dataModelSize = list.getModel ().getSize ();
        final Insets insets = list.getInsets ();

        setListHeight ( list.getHeight () );
        setListWidth ( list.getWidth () );

        if ( dataModelSize == 0 )
        {
            setColumnCount ( 0 );
            setRowsPerColumn ( 0 );
            setPreferredHeight ( insets.top + insets.bottom );
            return;
        }

        final int height;

        if ( fixedCellHeight != -1 )
        {
            height = fixedCellHeight;
        }
        else
        {
            // Determine the max of the renderer heights.
            int maxHeight = 0;
            if ( cellHeights.length > 0 )
            {
                maxHeight = cellHeights[ cellHeights.length - 1 ];
                for ( int counter = cellHeights.length - 2; counter >= 0; counter-- )
                {
                    maxHeight = Math.max ( maxHeight, cellHeights[ counter ] );
                }
            }
            height = cellHeight = maxHeight;
            cellHeights = null;
        }
        // The number of rows is either determined by the visible row
        // count, or by the height of the list.
        setRowsPerColumn ( dataModelSize );
        if ( visRows > 0 )
        {
            setRowsPerColumn ( visRows );
            setColumnCount ( Math.max ( 1, dataModelSize / getRowsPerColumn () ) );
            if ( dataModelSize > 0 && dataModelSize > getRowsPerColumn () &&
                    dataModelSize % getRowsPerColumn () != 0 )
            {
                setColumnCount ( getColumnCount () + 1 );
            }
            if ( getLayoutOrientation () == JList.HORIZONTAL_WRAP )
            {
                // Because HORIZONTAL_WRAP flows differently, the
                // rowsPerColumn needs to be adjusted.
                setRowsPerColumn ( dataModelSize / getColumnCount () );
                if ( dataModelSize % getColumnCount () > 0 )
                {
                    setRowsPerColumn ( getRowsPerColumn () + 1 );
                }
            }
        }
        else if ( getLayoutOrientation () == JList.VERTICAL_WRAP && height != 0 )
        {
            setRowsPerColumn ( Math.max ( 1, ( getListHeight () - insets.top -
                    insets.bottom ) / height ) );
            setColumnCount ( Math.max ( 1, dataModelSize / getRowsPerColumn () ) );
            if ( dataModelSize > 0 && dataModelSize > getRowsPerColumn () &&
                    dataModelSize % getRowsPerColumn () != 0 )
            {
                setColumnCount ( getColumnCount () + 1 );
            }
        }
        else if ( getLayoutOrientation () == JList.HORIZONTAL_WRAP && cellWidth > 0 &&
                getListWidth () > 0 )
        {
            setColumnCount ( Math.max ( 1, ( getListWidth () - insets.left -
                    insets.right ) / cellWidth ) );
            setRowsPerColumn ( dataModelSize / getColumnCount () );
            if ( dataModelSize % getColumnCount () > 0 )
            {
                setRowsPerColumn ( getRowsPerColumn () + 1 );
            }
        }
        setPreferredHeight ( getRowsPerColumn () * cellHeight + insets.top +
                insets.bottom );
    }

    protected Integer getLayoutOrientation ()
    {
        return ( Integer ) getBasicListUIValue ( "layoutOrientation" );
    }

    protected Integer getListHeight ()
    {
        return ( Integer ) getBasicListUIValue ( "listHeight" );
    }

    protected void setListHeight ( final Integer value )
    {
        setBasicListUIValue ( "listHeight", value );
    }

    protected Integer getListWidth ()
    {
        return ( Integer ) getBasicListUIValue ( "listWidth" );
    }

    protected void setListWidth ( final Integer value )
    {
        setBasicListUIValue ( "listWidth", value );
    }

    protected Integer getColumnCount ()
    {
        return ( Integer ) getBasicListUIValue ( "columnCount" );
    }

    protected void setColumnCount ( final Integer value )
    {
        setBasicListUIValue ( "columnCount", value );
    }

    protected Integer getRowsPerColumn ()
    {
        return ( Integer ) getBasicListUIValue ( "rowsPerColumn" );
    }

    protected void setRowsPerColumn ( final Integer value )
    {
        setBasicListUIValue ( "rowsPerColumn", value );
    }

    protected Integer getPreferredHeight ()
    {
        return ( Integer ) getBasicListUIValue ( "preferredHeight" );
    }

    protected void setPreferredHeight ( final Integer value )
    {
        setBasicListUIValue ( "preferredHeight", value );
    }

    protected Object getBasicListUIValue ( final String name )
    {
        try
        {
            return ReflectUtils.getFieldValue ( this, name );
        }
        catch ( final Throwable e )
        {
            return null;
        }
    }

    protected void setBasicListUIValue ( final String name, final Object value )
    {
        try
        {
            ReflectUtils.setFieldValue ( this, name, value );
        }
        catch ( final Throwable ignored )
        {

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