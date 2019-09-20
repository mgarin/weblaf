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
import com.alee.api.jdk.Consumer;
import com.alee.laf.list.behavior.ListItemHoverBehavior;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
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
public class WebListUI extends WListUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Static fields from {@link javax.swing.plaf.basic.BasicListUI}.
     */
    public final static int heightChanged = 1 << 8;
    public final static int widthChanged = 1 << 9;

    /**
     * List selection style.
     */
    protected ListSelectionStyle selectionStyle;

    /**
     * Component painter.
     */
    @DefaultPainter ( ListPainter.class )
    protected IListPainter painter;

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
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebListUI ();
    }

    @Override
    public void installUI ( final JComponent c )
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
                if ( painter != null && painter.isItemHoverDecorationSupported () )
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
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( list );

        // Removing custom listeners
        hoverCellTracker.uninstall ();
        hoverCellTracker = null;

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @NotNull
    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( list, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( list, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( list, painter, enabled );
    }

    @Nullable
    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( list );
    }

    @Override
    public void setMargin ( @Nullable final Insets margin )
    {
        PainterSupport.setMargin ( list, margin );
    }

    @Nullable
    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( list );
    }

    @Override
    public void setPadding ( @Nullable final Insets padding )
    {
        PainterSupport.setPadding ( list, padding );
    }

    /**
     * Returns list painter.
     *
     * @return list painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets list painter.
     * Pass null to remove list painter.
     *
     * @param painter new list painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( list, this, new Consumer<IListPainter> ()
        {
            @Override
            public void accept ( final IListPainter newPainter )
            {
                WebListUI.this.painter = newPainter;
            }
        }, this.painter, painter, IListPainter.class, AdaptiveListPainter.class );
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
    public boolean contains ( final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, painter, x, y );
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            // Invalidating list layout
            validateListLayout ();

            // Preparing list painter
            painter.prepareToPaint ( getLayoutOrientation (), getListHeight (), getListWidth (), getColumnCount (), getRowsPerColumn (),
                    getPreferredHeight (), cellWidth, cellHeight, cellHeights );

            // Painting list
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    /**
     * Perform list layout validation.
     */
    protected void validateListLayout ()
    {
        switch ( getLayoutOrientation () )
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
     * Returns layout orientation field value.
     * This is a bridge method to access private basic list UI field.
     *
     * @return layout orientation field value
     */
    protected Integer getLayoutOrientation ()
    {
        return getBasicListUIValue ( "layoutOrientation" );
    }

    /**
     * Returns cached list height field value.
     * This is a bridge method to access private basic list UI field.
     *
     * @return cached list height field value
     */
    protected Integer getListHeight ()
    {
        return getBasicListUIValue ( "listHeight" );
    }

    /**
     * Returns cached list width field value.
     * This is a bridge method to access private basic list UI field.
     *
     * @return cached list width field value
     */
    protected Integer getListWidth ()
    {
        return getBasicListUIValue ( "listWidth" );
    }

    /**
     * Returns cached column count field value.
     * This is a bridge method to access private basic list UI field.
     *
     * @return cached column count field value
     */
    protected Integer getColumnCount ()
    {
        return getBasicListUIValue ( "columnCount" );
    }

    /**
     * Returns cached rows per column amount field value.
     * This is a bridge method to access private basic list UI field.
     *
     * @return cached rows per column amount field value
     */
    protected Integer getRowsPerColumn ()
    {
        return getBasicListUIValue ( "rowsPerColumn" );
    }

    /**
     * Returns cached preferred height field value.
     * This is a bridge method to access private basic list UI field.
     *
     * @return cached preferred height field value
     */
    protected Integer getPreferredHeight ()
    {
        return getBasicListUIValue ( "preferredHeight" );
    }

    /**
     * Returns basic list UI field value.
     * This is a bridge method to access private basic list UI field.
     *
     * @param field field name
     * @param <T>   field type
     * @return basic list UI field value
     */
    protected <T> T getBasicListUIValue ( final String field )
    {
        return ReflectUtils.getFieldValueSafely ( this, field );
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

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}