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

package com.alee.laf.scroll;

import com.alee.api.data.Corner;
import com.alee.api.jdk.Consumer;
import com.alee.extended.canvas.WebCanvas;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.awt.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom UI for {@link JScrollPane} component.
 *
 * @author Mikle Garin
 */
public class WebScrollPaneUI extends BasicScrollPaneUI implements ShapeSupport, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( ScrollPanePainter.class )
    protected IScrollPanePainter painter;

    /**
     * Listeners.
     */
    protected transient PropertyChangeListener propertyChangeListener;
    protected transient ContainerAdapter viewListener;

    /**
     * Runtime variables.
     */
    protected transient Map<Corner, JComponent> cornersCache = new HashMap<Corner, JComponent> ( 4 );

    /**
     * Returns an instance of the {@link WebScrollPaneUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebScrollPaneUI}
     */
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebScrollPaneUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Scroll bars styling
        StyleId.scrollpaneViewport.at ( scrollpane ).set ( scrollpane.getViewport () );
        StyleId.scrollpaneVerticalBar.at ( scrollpane ).set ( scrollpane.getVerticalScrollBar () );
        StyleId.scrollpaneHorizontalBar.at ( scrollpane ).set ( scrollpane.getHorizontalScrollBar () );

        // Updating scrollpane corner
        updateCorners ();

        // Viewport listener
        viewListener = new ContainerAdapter ()
        {
            @Override
            public void componentAdded ( final ContainerEvent e )
            {
                removeCorners ();
                updateCorners ();
            }

            @Override
            public void componentRemoved ( final ContainerEvent e )
            {
                removeCorners ();
                updateCorners ();
            }
        };
        final JViewport viewport = scrollpane.getViewport ();
        if ( viewport != null )
        {
            viewport.addContainerListener ( viewListener );
        }

        // Property change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                final String property = evt.getPropertyName ();
                if ( property.equals ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY ) )
                {
                    // Simply updating corners
                    removeCorners ();
                    updateCorners ();
                }
                else if ( property.equals ( WebLookAndFeel.VIEWPORT_PROPERTY ) )
                {
                    // Updating old viewport style and removing listener
                    if ( evt.getOldValue () != null )
                    {
                        final JViewport viewport = ( JViewport ) evt.getOldValue ();
                        viewport.removeContainerListener ( viewListener );
                        StyleId.viewport.set ( viewport );
                    }

                    // Updating new viewport style and adding listener
                    if ( evt.getNewValue () != null )
                    {
                        final JViewport viewport = ( JViewport ) evt.getNewValue ();
                        viewport.addContainerListener ( viewListener );
                        StyleId.scrollpaneViewport.at ( scrollpane ).set ( scrollpane.getViewport () );
                    }

                    // Updating corners
                    removeCorners ();
                    updateCorners ();
                }
                else if ( property.equals ( WebLookAndFeel.VERTICAL_SCROLLBAR_PROPERTY ) )
                {
                    final JScrollBar vsb = scrollpane.getVerticalScrollBar ();
                    if ( vsb != null )
                    {
                        StyleId.scrollpaneVerticalBar.at ( scrollpane ).set ( vsb );
                    }
                }
                else if ( property.equals ( WebLookAndFeel.HORIZONTAL_SCROLLBAR_PROPERTY ) )
                {
                    final JScrollBar hsb = scrollpane.getHorizontalScrollBar ();
                    if ( hsb != null )
                    {
                        StyleId.scrollpaneHorizontalBar.at ( scrollpane ).set ( hsb );
                    }
                }
            }
        };
        scrollpane.addPropertyChangeListener ( propertyChangeListener );

        // Applying skin
        StyleManager.installSkin ( scrollpane );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( scrollpane );

        // Cleaning up listeners
        scrollpane.removePropertyChangeListener ( propertyChangeListener );

        // Removing listener and custom corners
        removeCorners ();

        // Resetting layout to default used within JScrollPane
        // This update will ensure that we properly cleanup custom layout
        scrollpane.setLayout ( new ScrollPaneLayout.UIResource () );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    protected MouseWheelListener createMouseWheelListener ()
    {
        return new MouseWheelListener ()
        {
            @Override
            public void mouseWheelMoved ( final MouseWheelEvent e )
            {
                if ( scrollpane.isWheelScrollingEnabled () && e.getWheelRotation () != 0 )
                {
                    // Determining scroll orientation
                    // This is the only part of the original {@link BasicScrollPaneUI} which is modified
                    // It provides HORIZONTAL instead of VERTICAL in case `SHIFT` modified is available
                    int orientation = SwingConstants.VERTICAL;
                    JScrollBar toScroll = scrollpane.getVerticalScrollBar ();
                    if ( toScroll == null || !toScroll.isVisible () || SwingUtils.isShift ( e ) )
                    {
                        toScroll = scrollpane.getHorizontalScrollBar ();
                        if ( toScroll == null || !toScroll.isVisible () )
                        {
                            return;
                        }
                        orientation = SwingConstants.HORIZONTAL;
                    }

                    // Properly consuming event
                    e.consume ();

                    // Performing scroll
                    final int direction = e.getWheelRotation () < 0 ? -1 : 1;
                    if ( e.getScrollType () == MouseWheelEvent.WHEEL_UNIT_SCROLL )
                    {
                        final JViewport vp = scrollpane.getViewport ();
                        if ( vp == null )
                        {
                            return;
                        }
                        final Component component = vp.getView ();
                        final int units = Math.abs ( e.getUnitsToScroll () );

                        // When the scrolling speed is set to maximum, it's possible for a single wheel click to scroll by more units than
                        // will fit in the visible area. This makes it hard/impossible to get to certain parts of the scrolling
                        // Component with the wheel. To make for more accurate low-speed scrolling, we limit scrolling to the block
                        // increment if the wheel was only rotated one click.
                        final boolean limitScroll = Math.abs ( e.getWheelRotation () ) == 1;

                        // Check if we should use the visibleRect trick
                        final Object fastWheelScroll = toScroll.getClientProperty ( "JScrollBar.fastWheelScrolling" );
                        if ( Boolean.TRUE == fastWheelScroll && component instanceof Scrollable )
                        {
                            // 5078454: Under maximum acceleration, we may scroll by many 100s of units in ~1 second.
                            // BasicScrollBarUI.scrollByUnits() can bog down the EDT with repaints in this situation.
                            // However, the Scrollable interface allows us to pass in an arbitrary visibleRect.
                            // This allows us to accurately calculate the total scroll amount, and then update the GUI once.
                            // This technique provides much faster accelerated wheel scrolling.
                            final Scrollable scrollComp = ( Scrollable ) component;
                            final Rectangle viewRect = vp.getViewRect ();
                            final int startingX = viewRect.x;
                            final boolean leftToRight = component.getComponentOrientation ().isLeftToRight ();
                            int scrollMin = toScroll.getMinimum ();
                            int scrollMax = toScroll.getMaximum () - toScroll.getModel ().getExtent ();

                            if ( limitScroll )
                            {
                                final int blockIncr = scrollComp.getScrollableBlockIncrement ( viewRect, orientation, direction );
                                if ( direction < 0 )
                                {
                                    scrollMin = Math.max ( scrollMin, toScroll.getValue () - blockIncr );
                                }
                                else
                                {
                                    scrollMax = Math.min ( scrollMax, toScroll.getValue () + blockIncr );
                                }
                            }

                            for ( int i = 0; i < units; i++ )
                            {
                                // Modify the visible rect for the next unit, and check to see if we're at the end already
                                final int unitIncr = scrollComp.getScrollableUnitIncrement ( viewRect, orientation, direction );
                                if ( orientation == SwingConstants.VERTICAL )
                                {
                                    if ( direction < 0 )
                                    {
                                        viewRect.y -= unitIncr;
                                        if ( viewRect.y <= scrollMin )
                                        {
                                            viewRect.y = scrollMin;
                                            break;
                                        }
                                    }
                                    else
                                    {
                                        viewRect.y += unitIncr;
                                        if ( viewRect.y >= scrollMax )
                                        {
                                            viewRect.y = scrollMax;
                                            break;
                                        }
                                    }
                                }
                                else
                                {
                                    if ( leftToRight ? direction < 0 : direction > 0 )
                                    {
                                        // Scroll left
                                        viewRect.x -= unitIncr;
                                        if ( leftToRight )
                                        {
                                            if ( viewRect.x < scrollMin )
                                            {
                                                viewRect.x = scrollMin;
                                                break;
                                            }
                                        }
                                    }
                                    else
                                    {
                                        // Scroll right
                                        viewRect.x += unitIncr;
                                        if ( leftToRight )
                                        {
                                            if ( viewRect.x > scrollMax )
                                            {
                                                viewRect.x = scrollMax;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            // Set the final view position on the ScrollBar
                            if ( orientation == SwingConstants.VERTICAL )
                            {
                                toScroll.setValue ( viewRect.y );
                            }
                            else
                            {
                                if ( leftToRight )
                                {
                                    toScroll.setValue ( viewRect.x );
                                }
                                else
                                {
                                    // RTL scrollbars are oriented with minValue on the right and maxValue on the left
                                    int newPos = toScroll.getValue () - ( viewRect.x - startingX );
                                    if ( newPos < scrollMin )
                                    {
                                        newPos = scrollMin;
                                    }
                                    else if ( newPos > scrollMax )
                                    {
                                        newPos = scrollMax;
                                    }
                                    toScroll.setValue ( newPos );
                                }
                            }
                        }
                        else
                        {
                            // Viewport's view is not a Scrollable, or fast wheel scrolling is not enabled
                            scrollByUnits ( toScroll, direction, units, limitScroll );
                        }
                    }
                    else if ( e.getScrollType () == MouseWheelEvent.WHEEL_BLOCK_SCROLL )
                    {
                        scrollByBlock ( toScroll, direction );
                    }
                }
            }
        };
    }

    /**
     * Method for scrolling by a unit increment.
     * Added for mouse wheel scrolling support, RFE 4202656.
     *
     * This method is called from {@link BasicScrollPaneUI} to implement wheel scrolling, as well as from scrollByUnit().
     *
     * If {@code limitByBlock} is set to {@code true}, the scrollbar will scroll at least 1 unit increment,
     * but will not scroll farther than the block increment.
     *
     * This is a full copy of {@link javax.swing.plaf.basic.BasicScrollBarUI#scrollByUnits(javax.swing.JScrollBar, int, int, boolean)}.
     *
     * @param scrollbar    scroll bar
     * @param direction    scroll direction
     * @param units        scrolled untins
     * @param limitToBlock whether or not should limit scroll to block increment
     */
    protected void scrollByUnits ( final JScrollBar scrollbar, final int direction,
                                   final int units, final boolean limitToBlock )
    {
        int delta;
        int limit = -1;

        if ( limitToBlock )
        {
            if ( direction < 0 )
            {
                limit = scrollbar.getValue () - scrollbar.getBlockIncrement ( direction );
            }
            else
            {
                limit = scrollbar.getValue () + scrollbar.getBlockIncrement ( direction );
            }
        }

        for ( int i = 0; i < units; i++ )
        {
            if ( direction > 0 )
            {
                delta = scrollbar.getUnitIncrement ( direction );
            }
            else
            {
                delta = -scrollbar.getUnitIncrement ( direction );
            }

            final int oldValue = scrollbar.getValue ();
            int newValue = oldValue + delta;

            // Check for overflow.
            if ( delta > 0 && newValue < oldValue )
            {
                newValue = scrollbar.getMaximum ();
            }
            else if ( delta < 0 && newValue > oldValue )
            {
                newValue = scrollbar.getMinimum ();
            }
            if ( oldValue == newValue )
            {
                break;
            }

            if ( limitToBlock && i > 0 )
            {
                if ( direction < 0 && newValue < limit || direction > 0 && newValue > limit )
                {
                    break;
                }
            }
            scrollbar.setValue ( newValue );
        }
    }

    /**
     * Method for scrolling by a block increment.
     * Added for mouse wheel scrolling support, RFE 4202656.
     *
     * This method is called from {@link BasicScrollPaneUI} to implement wheel scrolling, and also from scrollByBlock().
     *
     * This is a full copy of {@link javax.swing.plaf.basic.BasicScrollBarUI#scrollByBlock(javax.swing.JScrollBar, int)}.
     *
     * @param scrollbar scroll bar
     * @param direction scroll direction
     */
    protected void scrollByBlock ( final JScrollBar scrollbar, final int direction )
    {
        final int oldValue = scrollbar.getValue ();
        final int blockIncrement = scrollbar.getBlockIncrement ( direction );
        final int delta = blockIncrement * ( direction > 0 ? +1 : -1 );
        int newValue = oldValue + delta;

        // Check for overflow.
        if ( delta > 0 && newValue < oldValue )
        {
            newValue = scrollbar.getMaximum ();
        }
        else if ( delta < 0 && newValue > oldValue )
        {
            newValue = scrollbar.getMinimum ();
        }

        scrollbar.setValue ( newValue );
    }

    @Override
    public Shape getShape ()
    {
        return PainterSupport.getShape ( scrollpane, painter );
    }

    @Override
    public boolean isShapeDetectionEnabled ()
    {
        return PainterSupport.isShapeDetectionEnabled ( scrollpane, painter );
    }

    @Override
    public void setShapeDetectionEnabled ( final boolean enabled )
    {
        PainterSupport.setShapeDetectionEnabled ( scrollpane, painter, enabled );
    }

    @Override
    public Insets getMargin ()
    {
        return PainterSupport.getMargin ( scrollpane );
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        PainterSupport.setMargin ( scrollpane, margin );
    }

    @Override
    public Insets getPadding ()
    {
        return PainterSupport.getPadding ( scrollpane );
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        PainterSupport.setPadding ( scrollpane, padding );
    }

    /**
     * Returns panel painter.
     *
     * @return panel painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getPainter ( painter );
    }

    /**
     * Sets scroll pane painter.
     * Pass null to remove scroll pane painter.
     *
     * @param painter new scroll pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( scrollpane, new Consumer<IScrollPanePainter> ()
        {
            @Override
            public void accept ( final IScrollPanePainter newPainter )
            {
                WebScrollPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, IScrollPanePainter.class, AdaptiveScrollPanePainter.class );
    }

    /**
     * Updates custom scrollpane corners.
     */
    protected void updateCorners ()
    {
        final ScrollPaneCornerProvider provider = getScrollCornerProvider ();
        for ( final Corner type : Corner.values () )
        {
            JComponent corner = cornersCache.get ( type );
            if ( corner == null )
            {
                if ( provider != null )
                {
                    corner = provider.getCorner ( type );
                }
                if ( corner == null )
                {
                    // todo Make this corner optional
                    if ( type == Corner.lowerLeading || type == Corner.lowerTrailing || type == Corner.upperTrailing )
                    {
                        corner = new WebCanvas ( StyleId.scrollpaneCorner.at ( scrollpane ), type.name () );
                    }
                }
            }
            if ( corner != null )
            {
                cornersCache.put ( type, corner );
                scrollpane.setCorner ( type.getScrollPaneConstant (), corner );
            }
        }
    }

    /**
     * Returns scroll corner provider.
     *
     * @return scroll corner provider
     */
    protected ScrollPaneCornerProvider getScrollCornerProvider ()
    {
        ScrollPaneCornerProvider scp = null;
        if ( scrollpane.getViewport () != null && scrollpane.getViewport ().getView () != null )
        {
            final Component view = scrollpane.getViewport ().getView ();
            if ( view instanceof ScrollPaneCornerProvider )
            {
                scp = ( ScrollPaneCornerProvider ) view;
            }
            else if ( view instanceof JComponent )
            {
                final JComponent jView = ( JComponent ) view;
                if ( LafUtils.hasUI ( jView ) )
                {
                    final ComponentUI ui = LafUtils.getUI ( jView );
                    if ( ui instanceof ScrollPaneCornerProvider )
                    {
                        scp = ( ScrollPaneCornerProvider ) ui;
                    }
                }
            }
        }
        return scp;
    }

    /**
     * Removes custom scrollpane corners.
     */
    protected void removeCorners ()
    {
        // We do not remove corners by types here by components directly
        // This is required since internal types will be shifted upon component orientation change
        for ( final JComponent corner : cornersCache.values () )
        {
            scrollpane.remove ( corner );
        }
        cornersCache.clear ();
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
            painter.paint ( ( Graphics2D ) g, c, this, new Bounds ( c ) );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        // return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
        return null;
    }
}