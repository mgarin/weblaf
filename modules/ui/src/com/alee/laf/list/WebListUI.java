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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.list.behavior.ListItemHoverBehavior;
import com.alee.managers.style.*;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for {@link JList} component.
 *
 * @author Mikle Garin
 */
public class WebListUI extends WListUI
{
    /**
     * Static fields from {@link javax.swing.plaf.basic.BasicListUI}.
     */
    public static final int heightChanged = 1 << 8;
    public static final int widthChanged = 1 << 9;

    /**
     * List selection style.
     */
    protected ListSelectionStyle selectionStyle;

    /**
     * Listeners.
     */
    protected transient ListItemHoverBehavior hoverCellTracker;

    /**
     * Runtime variables.
     */
    protected transient int hoverIndex = -1;

    /**
     * Returns an instance of the {@link WebListUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebListUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebListUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Hover behavior
        hoverCellTracker = new ListItemHoverBehavior<JList> ( list, true )
        {
            @Override
            public void hoverChanged ( @NotNull final Integer previous, @NotNull final Integer current )
            {
                // Updating hover row
                final int previousIndex = hoverIndex;
                hoverIndex = current;

                // Repainting nodes according to hover changes
                // This occurs only if hover highlight is enabled
                final Painter painter = PainterSupport.getPainter ( list );
                if ( painter instanceof IListPainter && ( ( IListPainter ) painter ).isItemHoverDecorationSupported () )
                {
                    repaintCell ( previousIndex );
                    repaintCell ( hoverIndex );
                }

                // Updating custom WebLaF tooltip display state
                final ListToolTipProvider provider = getToolTipProvider ();
                if ( provider != null )
                {
                    provider.hoverAreaChanged (
                            list,
                            previousIndex != -1 ? new ListCellArea ( previousIndex ) : null,
                            hoverIndex != -1 ? new ListCellArea ( hoverIndex ) : null
                    );
                }

                // Informing {@link com.alee.laf.list.WebList} about hover index change
                // This is performed here to avoid excessive hover listeners usage
                if ( list instanceof WebList )
                {
                    ( ( WebList ) list ).fireHoverChanged ( previous, current );
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

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( list );

        // Removing custom listeners
        hoverCellTracker.uninstall ();
        hoverCellTracker = null;

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    public int getHoverIndex ()
    {
        return hoverIndex;
    }

    @Override
    public ListSelectionStyle getSelectionStyle ()
    {
        return selectionStyle;
    }

    @Override
    public void setSelectionStyle ( final ListSelectionStyle style )
    {
        this.selectionStyle = style;
    }

    @Override
    public void updateListLayout ()
    {
        updateLayoutStateNeeded = modelChanged;
        redrawList ();
    }

    @Override
    public CellRendererPane getCellRendererPane ()
    {
        return rendererPane;
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, x, y );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        // Invalidating list layout
        validateListLayout ();

        // Painting list
        PainterSupport.paint ( g, c, this, new ListPaintParameters (
                getListWidth (),
                getListHeight (),
                getColumnCount (),
                getRowsPerColumn (),
                getPreferredHeight (),
                cellWidth,
                cellHeight,
                cellHeights
        ) );
    }

    /**
     * Perform list layout validation.
     */
    protected void validateListLayout ()
    {
        switch ( list.getLayoutOrientation () )
        {
            case JList.VERTICAL_WRAP:
                if ( list.getHeight () != getListHeight () )
                {
                    updateLayoutStateNeeded |= heightChanged;
                    redrawList ();
                }
                break;

            case JList.HORIZONTAL_WRAP:
                if ( list.getWidth () != getListWidth () )
                {
                    updateLayoutStateNeeded |= widthChanged;
                    redrawList ();
                }
                break;

            default:
                break;
        }
        maybeUpdateLayoutState ();
    }

    /**
     * Requests full list update.
     */
    public void redrawList ()
    {
        list.revalidate ();
        list.repaint ();
    }

    /**
     * Returns cached list height field value.
     * This is a bridge method to access private basic list UI field.
     *
     * @return cached list height field value
     */
    protected int getListHeight ()
    {
        final Integer listHeight = getBasicListUIValue ( "listHeight" );
        if ( listHeight == null )
        {
            throw new UIException ( "List height value is not available" );
        }
        return listHeight;
    }

    /**
     * Returns cached list width field value.
     * This is a bridge method to access private basic list UI field.
     *
     * @return cached list width field value
     */
    protected int getListWidth ()
    {
        final Integer listWidth = getBasicListUIValue ( "listWidth" );
        if ( listWidth == null )
        {
            throw new UIException ( "List width value is not available" );
        }
        return listWidth;
    }

    /**
     * Returns cached column count field value.
     * This is a bridge method to access private basic list UI field.
     *
     * @return cached column count field value
     */
    protected int getColumnCount ()
    {
        final Integer columnCount = getBasicListUIValue ( "columnCount" );
        if ( columnCount == null )
        {
            throw new UIException ( "List column count value is not available" );
        }
        return columnCount;
    }

    /**
     * Returns cached rows per column amount field value.
     * This is a bridge method to access private basic list UI field.
     *
     * @return cached rows per column amount field value
     */
    protected int getRowsPerColumn ()
    {
        final Integer rowsPerColumn = getBasicListUIValue ( "rowsPerColumn" );
        if ( rowsPerColumn == null )
        {
            throw new UIException ( "List rows per column value is not available" );
        }
        return rowsPerColumn;
    }

    /**
     * Returns cached preferred height field value.
     * This is a bridge method to access private basic list UI field.
     *
     * @return cached preferred height field value
     */
    protected int getPreferredHeight ()
    {
        final Integer preferredHeight = getBasicListUIValue ( "preferredHeight" );
        if ( preferredHeight == null )
        {
            throw new UIException ( "List preferred height value is not available" );
        }
        return preferredHeight;
    }

    /**
     * Returns basic list UI field value.
     * This is a bridge method to access private basic list UI field.
     *
     * @param field field name
     * @param <T>   field type
     * @return basic list UI field value
     */
    @Nullable
    protected <T> T getBasicListUIValue ( @NotNull final String field )
    {
        try
        {
            return ReflectUtils.getFieldValue ( this, field );
        }
        catch ( final Exception e )
        {
            throw new UIException ( "Unable to access BasicListUI field: " + field, e );
        }
    }

    /**
     * Returns {@link ListToolTipProvider} for {@link JList} that uses this {@link WebListUI}.
     *
     * @return {@link ListToolTipProvider} for {@link JList} that uses this {@link WebListUI}
     */
    @Nullable
    protected ListToolTipProvider getToolTipProvider ()
    {
        return list != null ?
                ( ListToolTipProvider ) list.getClientProperty ( WebList.TOOLTIP_PROVIDER_PROPERTY ) :
                null;
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ) );
    }
}