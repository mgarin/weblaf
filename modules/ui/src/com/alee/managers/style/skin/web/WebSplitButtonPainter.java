package com.alee.managers.style.skin.web;

import com.alee.extended.button.ISplitButtonPainter;
import com.alee.extended.button.WebSplitButton;
import com.alee.extended.button.WebSplitButtonUI;
import com.alee.global.StyleConstants;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Mikle Garin
 */

public class WebSplitButtonPainter<E extends WebSplitButton, U extends WebSplitButtonUI> extends AbstractButtonPainter<E, U>
        implements ISplitButtonPainter<E, U>
{
    /**
     * Style settings.
     */
    protected int splitIconGap;
    protected int contentGap;

    /**
     * Listeners.
     */
    protected MouseAdapter splitButtonTracker;

    /**
     * Runtime variables.
     */
    protected boolean onSplit = false;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Adding split button mouseover tracker
        splitButtonTracker = new MouseAdapter ()
        {
            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                // Saving old split mouseover state
                final boolean wasOnSplit = onSplit;

                // Updating split mouseover state
                onSplit = getSplitButtonHitbox ( component ).contains ( e.getPoint () );

                // Repainting button if state has changed
                if ( wasOnSplit != onSplit )
                {
                    repaint ( getSplitButtonBounds ( component ) );
                }
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                // Saving old split mouseover state
                final boolean wasOnSplit = onSplit;

                // Resetting split mouseover state
                onSplit = false;

                // Repainting button if state has changed
                if ( wasOnSplit != onSplit )
                {
                    repaint ( getSplitButtonBounds ( component ) );
                }
            }
        };
        component.addMouseListener ( splitButtonTracker );
        component.addMouseMotionListener ( splitButtonTracker );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing split button mouseover tracker
        component.removeMouseMotionListener ( splitButtonTracker );
        component.removeMouseListener ( splitButtonTracker );
        splitButtonTracker = null;
        onSplit = false;

        super.uninstall ( c, ui );
    }

    @Override
    protected void propertyChange ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChange ( property, oldValue, newValue );

        // Updating border on split icon change
        if ( CompareUtils.equals ( property, WebSplitButton.SPLIT_ICON_PROPERTY ) )
        {
            updateBorder ();
        }
    }

    /**
     * Returns gap between split icon and split part sides.
     *
     * @return gap between split icon and split part sides
     */
    public int getSplitIconGap ()
    {
        return splitIconGap;
    }

    /**
     * Sets gap between split icon and split part sides
     *
     * @param splitIconGap gap between split icon and split part sides
     */
    public void setSplitIconGap ( final int splitIconGap )
    {
        this.splitIconGap = splitIconGap;
    }

    /**
     * Returns gap between split part and button content.
     *
     * @return gap between split part and button content
     */
    public int getContentGap ()
    {
        return contentGap;
    }

    /**
     * Sets gap between split part and button content.
     *
     * @param contentGap gap between split part and button content
     */
    public void setContentGap ( final int contentGap )
    {
        this.contentGap = contentGap;
    }

    @Override
    public boolean isOnSplit ()
    {
        return onSplit;
    }

    @Override
    public Insets getBorders ()
    {
        final Insets borders = super.getBorders ();
        final Icon splitIcon = component.getSplitIcon ();
        final int splitPartWidth = contentGap + 1 + splitIconGap + ( splitIcon != null ? splitIcon.getIconWidth () : 0 ) + splitIconGap;
        return i ( borders, 0, ltr ? 0 : splitPartWidth, 0, ltr ? splitPartWidth : 0 );
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Painting button
        super.paint ( g2d, bounds, c, ui );

        // Painting split button
        paintSplitButton ( g2d, c );
    }

    /**
     * Paints split button.
     *
     * @param g2d graphics context
     * @param c   split button
     */
    protected void paintSplitButton ( final Graphics2D g2d, final E c )
    {
        // Retrieving split button bounds
        final Rectangle rect = getSplitButtonBounds ( c );

        // Painting split button icon
        final Icon splitIcon = component.getSplitIcon ();
        final int ix = rect.x + rect.width / 2 - splitIcon.getIconWidth () / 2;
        final int iy = rect.y + rect.height / 2 - splitIcon.getIconHeight () / 2;
        splitIcon.paintIcon ( component, g2d, ix, iy );

        // Painting split button line
        final int lineX = ltr ? rect.x : rect.x + rect.width - 1;
        g2d.setColor ( c.isEnabled () ? StyleConstants.borderColor : StyleConstants.disabledBorderColor );
        g2d.drawLine ( lineX, rect.y + 1, lineX, rect.y + rect.height - 2 );
    }

    /**
     * Returns bounds of the split button part.
     *
     * @param c split button
     * @return bounds of the split button part
     */
    protected Rectangle getSplitButtonBounds ( final E c )
    {
        final Insets i = c.getInsets ();
        final int styleSide = actualPaintRight ? shadeWidth + 1 : paintRightLine ? 1 : 0;
        final int height = c.getHeight () - i.top - i.bottom;
        if ( ltr )
        {
            final int width = i.right - contentGap - styleSide;
            return new Rectangle ( c.getWidth () - i.right + contentGap, i.top, width, height );
        }
        else
        {
            final int width = i.left - contentGap - styleSide;
            return new Rectangle ( styleSide, i.top, width, height );
        }
    }

    /**
     * Returns split button part hitbox.
     *
     * @param c split button
     * @return split button part hitbox
     */
    protected Rectangle getSplitButtonHitbox ( final E c )
    {
        final Insets i = c.getInsets ();
        if ( ltr )
        {
            return new Rectangle ( c.getWidth () - i.right + contentGap, 0, i.right - contentGap, c.getHeight () );
        }
        else
        {
            return new Rectangle ( 0, 0, i.left - contentGap, c.getHeight () );
        }
    }
}