package com.alee.laf.scroll;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.lang.ref.WeakReference;

/**
 * By default {@code WebScrollPane} creates scrollbars that are instances of this class. {@code WebScrollPaneBar} overrides the
 * {@code getUnitIncrement} and {@code getBlockIncrement} methods so that, if the viewport's view is a {@code Scrollable}, the view is
 * asked to compute these values. Unless the unit/block increment have been explicitly set.
 *
 * @author Mikle Garin
 * @see javax.swing.Scrollable
 * @see com.alee.laf.scroll.WebScrollPane#createVerticalScrollBar
 * @see com.alee.laf.scroll.WebScrollPane#createHorizontalScrollBar
 */

public class WebScrollPaneBar extends WebScrollBar implements UIResource
{
    /**
     * Enclosing scroll pane reference.
     */
    private final WeakReference<JScrollPane> scrollPane;

    /**
     * Set to true when the unit increment has been explicitly set.
     * If this is false the viewport's view is obtained and if it is an instance of {@code Scrollable} the unit increment from it is used.
     */
    private boolean unitIncrementSet;
    /**
     * Set to true when the block increment has been explicitly set.
     * If this is false the viewport's view is obtained and if it is an instance of {@code Scrollable} the block increment from it is used.
     */
    private boolean blockIncrementSet;

    /**
     * Creates a scrollbar with the specified orientation.
     * The options are:
     * <ul>
     * <li>{@code ScrollPaneConstants.VERTICAL}
     * <li>{@code ScrollPaneConstants.HORIZONTAL}
     * </ul>
     *
     * @param scrollPane  scrollpane this bar will be attached to
     * @param orientation an integer specifying one of the legal orientation values shown above
     */
    public WebScrollPaneBar ( final JScrollPane scrollPane, final int orientation )
    {
        super ( orientation );
        this.scrollPane = new WeakReference<JScrollPane> ( scrollPane );
        this.putClientProperty ( "JScrollBar.fastWheelScrolling", Boolean.TRUE );
    }

    /**
     * Messages super to set the value, and resets the {@code unitIncrementSet} instance variable to true.
     *
     * @param unitIncrement the new unit increment value, in pixels
     */
    @Override
    public void setUnitIncrement ( final int unitIncrement )
    {
        unitIncrementSet = true;
        this.putClientProperty ( "JScrollBar.fastWheelScrolling", null );
        super.setUnitIncrement ( unitIncrement );
    }

    /**
     * Computes the unit increment for scrolling if the viewport's view is a {@code Scrollable} object.
     * Otherwise return {@code super.getUnitIncrement}.
     *
     * @param direction less than zero to scroll up/left, greater than zero for down/right
     * @return an integer, in pixels, containing the unit increment
     * @see javax.swing.Scrollable#getScrollableUnitIncrement
     */
    @Override
    public int getUnitIncrement ( final int direction )
    {
        final JViewport vp = scrollPane.get ().getViewport ();
        if ( !unitIncrementSet && ( vp != null ) && ( vp.getView () instanceof Scrollable ) )
        {
            final Scrollable view = ( Scrollable ) vp.getView ();
            final Rectangle vr = vp.getViewRect ();
            return view.getScrollableUnitIncrement ( vr, getOrientation (), direction );
        }
        else
        {
            return super.getUnitIncrement ( direction );
        }
    }

    /**
     * Messages super to set the value, and resets the {@code blockIncrementSet} instance variable to true.
     *
     * @param blockIncrement the new block increment value, in pixels
     */
    @Override
    public void setBlockIncrement ( final int blockIncrement )
    {
        blockIncrementSet = true;
        this.putClientProperty ( "JScrollBar.fastWheelScrolling", null );
        super.setBlockIncrement ( blockIncrement );
    }

    /**
     * Computes the block increment for scrolling if the viewport's view is a {@code Scrollable} object.
     * Otherwise the {@code blockIncrement} equals the viewport's width or height.
     * If there's no viewport return {@code super.getBlockIncrement}.
     *
     * @param direction less than zero to scroll up/left, greater than zero for down/right
     * @return an integer, in pixels, containing the block increment
     * @see javax.swing.Scrollable#getScrollableBlockIncrement
     */
    @Override
    public int getBlockIncrement ( final int direction )
    {
        final JViewport vp = scrollPane.get ().getViewport ();
        if ( blockIncrementSet || vp == null )
        {
            return super.getBlockIncrement ( direction );
        }
        else if ( vp.getView () instanceof Scrollable )
        {
            final Scrollable view = ( Scrollable ) vp.getView ();
            final Rectangle vr = vp.getViewRect ();
            return view.getScrollableBlockIncrement ( vr, getOrientation (), direction );
        }
        else if ( getOrientation () == VERTICAL )
        {
            return vp.getExtentSize ().height;
        }
        else
        {
            return vp.getExtentSize ().width;
        }
    }
}