package com.alee.extended.button;

import com.alee.api.jdk.Objects;
import com.alee.laf.button.AbstractButtonPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Basic painter for {@link WebSplitButton} component.
 * It is used as {@link WSplitButtonUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public class SplitButtonPainter<C extends WebSplitButton, U extends WSplitButtonUI, D extends IDecoration<C, D>>
        extends AbstractButtonPainter<C, U, D> implements ISplitButtonPainter<C, U>
{
    /**
     * todo 1. Replace custom split button painting with button component
     * todo 2. Replace custom separator painting with separator component
     */

    /**
     * Style settings.
     * todo This should be in separator style
     */
    protected Color splitLineColor;
    protected Color splitLineDisabledColor;
    protected int splitIconGap;
    protected int contentGap;

    /**
     * Listeners.
     */
    protected transient MouseAdapter splitButtonTracker;

    /**
     * Runtime variables.
     */
    protected transient boolean onSplit;

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installSplitButtonListeners ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallSplitButtonListeners ();
        super.uninstallPropertiesAndListeners ();
    }

    /**
     * Installs split button mouseover listener.
     */
    protected void installSplitButtonListeners ()
    {
        onSplit = false;
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
                    repaint ();
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
                    repaint ();
                }
            }
        };
        component.addMouseListener ( splitButtonTracker );
        component.addMouseMotionListener ( splitButtonTracker );
    }

    /**
     * Uninstalls split button mouseover listener.
     */
    protected void uninstallSplitButtonListeners ()
    {
        component.removeMouseMotionListener ( splitButtonTracker );
        component.removeMouseListener ( splitButtonTracker );
        splitButtonTracker = null;
        onSplit = false;
    }

    @Override
    protected void propertyChanged ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating border on split icon change
        if ( Objects.equals ( property, WebSplitButton.SPLIT_ICON_PROPERTY ) )
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
    protected Insets getBorder ()
    {
        final Insets border = super.getBorder ();
        final Icon splitIcon = component.getSplitIcon ();
        final Insets result;
        if ( splitIcon != null )
        {
            final int right = contentGap + 1 + splitIconGap + splitIcon.getIconWidth () + splitIconGap;
            if ( border != null )
            {
                result = new Insets ( border.top, border.left, border.bottom, border.right + right );
            }
            else
            {
                result = new Insets ( 0, 0, 0, right );
            }
        }
        else
        {
            result = border;
        }
        return result;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final C c, final U ui )
    {
        // Painting split button
        paintSplitButton ( g2d, bounds, c );
    }

    /**
     * Paints split button.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param c      split button
     */
    protected void paintSplitButton ( final Graphics2D g2d, final Rectangle bounds, final C c )
    {
        // Painting split button icon
        final Icon splitIcon = component.getSplitIcon ();
        final Rectangle br = getSplitButtonBounds ( bounds, c );
        final int ix = br.x + br.width / 2 - splitIcon.getIconWidth () / 2;
        final int iy = br.y + br.height / 2 - splitIcon.getIconHeight () / 2;
        splitIcon.paintIcon ( component, g2d, ix, iy );

        // Painting split button line
        final Rectangle lr = getSplitLineBounds ( bounds, c );
        g2d.setPaint ( c.isEnabled () ? splitLineColor : splitLineDisabledColor );
        g2d.drawLine ( lr.x, lr.y, lr.x, lr.y + lr.height );
    }

    /**
     * Returns bounds of the split button part.
     *
     * @param b painting bounds
     * @param c split button
     * @return bounds of the split button part
     */
    protected Rectangle getSplitButtonBounds ( final Rectangle b, final C c )
    {
        final Insets i = c.getInsets ();
        final int x = b.x + ( ltr ? b.width - i.right + contentGap + 1 + splitIconGap : i.left - contentGap - 1 - splitIconGap );
        return new Rectangle ( x, b.y + i.top, component.getSplitIcon ().getIconWidth (), b.height - i.top - i.bottom );
    }

    /**
     * Returns bounds of the split line part.
     *
     * @param b painting bounds
     * @param c split button
     * @return bounds of the split line part
     */
    protected Rectangle getSplitLineBounds ( final Rectangle b, final C c )
    {
        final Insets i = c.getInsets ();
        final int x = b.x + ( ltr ? b.width - i.right + contentGap : i.left - contentGap );
        return new Rectangle ( x, b.y + i.top, 1, b.height - i.top - i.bottom - 1 );
    }

    /**
     * Returns split button part hitbox.
     *
     * @param c split button
     * @return split button part hitbox
     */
    protected Rectangle getSplitButtonHitbox ( final C c )
    {
        final Insets i = c.getInsets ();
        return new Rectangle ( ltr ? c.getWidth () - i.right : 0, 0, ltr ? i.right : i.left, c.getHeight () );
    }
}