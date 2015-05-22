package com.alee.managers.style.skin.web;

import com.alee.extended.button.SplitButtonPainter;
import com.alee.extended.button.WebSplitButton;
import com.alee.extended.button.WebSplitButtonStyle;
import com.alee.extended.button.WebSplitButtonUI;
import com.alee.global.StyleConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Mikle Garin
 */

public class WebSplitButtonPainter<E extends WebSplitButton, U extends WebSplitButtonUI> extends WebAbstractButtonPainter<E, U>
        implements SplitButtonPainter<E, U>
{
    /**
     * Style settings.
     */
    protected int splitIconGap = WebSplitButtonStyle.splitIconGap;
    protected int contentGap = WebSplitButtonStyle.contentGap;

    /**
     * Listeners.
     */
    protected MouseAdapter splitButtonTracker;

    /**
     * Runtime variables.
     */
    protected boolean onSplit = false;

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing split button mouseover tracker
        component.removeMouseMotionListener ( splitButtonTracker );
        component.removeMouseListener ( splitButtonTracker );
        onSplit = false;

        super.uninstall ( c, ui );
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOnSplit ()
    {
        return onSplit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ()
    {
        // Retrieving default button margin
        final Insets margin = super.getMargin ();

        // Adding split part width to appropriate border side
        final ImageIcon splitIcon = component.getSplitIcon ();
        final int splitPartWidth = splitIcon.getIconWidth () + 1 + splitIconGap * 2 + contentGap;
        if ( ltr )
        {
            margin.right += splitPartWidth;
        }
        else
        {
            margin.left += splitPartWidth;
        }

        return margin;
    }

    /**
     * {@inheritDoc}
     */
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
     * @param g2d
     * @param c
     */
    protected void paintSplitButton ( final Graphics2D g2d, final E c )
    {
        // Retrieving split button bounds
        final Rectangle rect = getSplitButtonBounds ( c );

        // Painting split button icon
        final ImageIcon splitIcon = component.getSplitIcon ();
        final int ix = rect.x + rect.width / 2 - splitIcon.getIconWidth () / 2;
        final int iy = rect.y + rect.height / 2 - splitIcon.getIconHeight () / 2;
        g2d.drawImage ( splitIcon.getImage (), ix, iy, null );

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
        final int styleSide = actualPaintRight ? shadeWidth + 1 : ( paintRightLine ? 1 : 0 );
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
        return ltr ? new Rectangle ( c.getWidth () - i.right + contentGap, 0, i.right - contentGap, c.getHeight () ) :
                new Rectangle ( 0, 0, i.left - contentGap, c.getHeight () );
    }
}