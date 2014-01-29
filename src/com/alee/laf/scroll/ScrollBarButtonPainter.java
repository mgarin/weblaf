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

import com.alee.extended.painter.AbstractPainter;
import com.alee.laf.button.WebButtonUI;
import com.alee.utils.ShapeCache;
import com.alee.utils.swing.DataProvider;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Base painter for JScrollBar arrow buttons.
 *
 * @author Mikle Garin
 */

public class ScrollBarButtonPainter<E extends AbstractButton> extends AbstractPainter<E>
{
    /**
     * todo 1. On rollover=false make the same animation scroll bar thumb has -> requires WebButtonUI full painter support
     */

    /**
     * Shape cache key.
     */
    protected static final String ARROW_BUTTON_SHAPE = "arrow-button";

    /**
     * Style settings.
     */
    protected Color borderColor = WebScrollBarStyle.thumbBorderColor;
    protected Color backgroundColor = WebScrollBarStyle.thumbBackgroundColor;
    protected Color disabledBorderColor = WebScrollBarStyle.thumbDisabledBorderColor;
    protected Color disabledBackgroundColor = WebScrollBarStyle.thumbDisabledBackgroundColor;
    protected Color rolloverBorderColor = WebScrollBarStyle.thumbRolloverBorderColor;
    protected Color rolloverBackgroundColor = WebScrollBarStyle.thumbRolloverBackgroundColor;
    protected Color pressedBorderColor = WebScrollBarStyle.thumbPressedBorderColor;
    protected Color pressedBackgroundColor = WebScrollBarStyle.thumbPressedBackgroundColor;

    /**
     * Runtime variables.
     */
    protected final ScrollBarButtonType buttonType;
    protected final JScrollBar scrollbar;

    /**
     * Constructs new scroll bar button painter.
     *
     * @param scrollbar  scroll bar which uses this button
     * @param buttonType button type
     */
    public ScrollBarButtonPainter ( final JScrollBar scrollbar, final ScrollBarButtonType buttonType )
    {
        super ();
        this.scrollbar = scrollbar;
        this.buttonType = buttonType;
        setPreferredSize ( new Dimension ( WebScrollBarStyle.buttonsSize ) );
    }

    /**
     * Returns button border color.
     *
     * @return button border color
     */
    public Color getBorderColor ()
    {
        return borderColor;
    }

    /**
     * Sets button border color.
     *
     * @param color new button border color
     */
    public void setBorderColor ( final Color color )
    {
        if ( this.borderColor != color )
        {
            this.borderColor = color;
            repaint ();
        }
    }

    /**
     * Returns button background color.
     *
     * @return button background color
     */
    public Color getBackgroundColor ()
    {
        return backgroundColor;
    }

    /**
     * Sets button background color.
     *
     * @param color new button background color
     */
    public void setBackgroundColor ( final Color color )
    {
        if ( this.backgroundColor != color )
        {
            this.backgroundColor = color;
            repaint ();
        }
    }

    /**
     * Returns disabled button border color.
     *
     * @return disabled button border color
     */
    public Color getDisabledBorderColor ()
    {
        return disabledBorderColor;
    }

    /**
     * Sets disabled button border color.
     *
     * @param color new disabled button border color
     */
    public void setDisabledBorderColor ( final Color color )
    {
        if ( this.disabledBorderColor != color )
        {
            this.disabledBorderColor = color;
            repaint ();
        }
    }

    /**
     * Returns disabled button background color.
     *
     * @return disabled button background color
     */
    public Color getDisabledBackgroundColor ()
    {
        return disabledBackgroundColor;
    }

    /**
     * Sets disabled button background color.
     *
     * @param color new disabled button background color
     */
    public void setDisabledBackgroundColor ( final Color color )
    {
        if ( this.disabledBackgroundColor != color )
        {
            this.disabledBackgroundColor = color;
            repaint ();
        }
    }

    /**
     * Returns rollover button border color.
     *
     * @return rollover button border color
     */
    public Color getRolloverBorderColor ()
    {
        return rolloverBorderColor;
    }

    /**
     * Sets rollover button border color.
     *
     * @param color new rollover button border color
     */
    public void setRolloverBorderColor ( final Color color )
    {
        if ( this.rolloverBorderColor != color )
        {
            this.rolloverBorderColor = color;
            repaint ();
        }
    }

    /**
     * Returns rollover button background color.
     *
     * @return rollover button background color
     */
    public Color getRolloverBackgroundColor ()
    {
        return rolloverBackgroundColor;
    }

    /**
     * Sets rollover button background color.
     *
     * @param color new rollover button background color
     */
    public void setRolloverBackgroundColor ( final Color color )
    {
        if ( this.rolloverBackgroundColor != color )
        {
            this.rolloverBackgroundColor = color;
            repaint ();
        }
    }

    /**
     * Returns pressed button border color.
     *
     * @return pressed button border color
     */
    public Color getPressedBorderColor ()
    {
        return pressedBorderColor;
    }

    /**
     * Sets pressed button border color.
     *
     * @param color new pressed button border color
     */
    public void setPressedBorderColor ( final Color color )
    {
        if ( this.pressedBorderColor != color )
        {
            this.pressedBorderColor = color;
            repaint ();
        }
    }

    /**
     * Returns pressed button background color.
     *
     * @return pressed button background color
     */
    public Color getPressedBackgroundColor ()
    {
        return pressedBackgroundColor;
    }

    /**
     * Sets pressed button background color.
     *
     * @param color new pressed button background color
     */
    public void setPressedBackgroundColor ( final Color color )
    {
        if ( this.pressedBackgroundColor != color )
        {
            this.pressedBackgroundColor = color;
            repaint ();
        }
    }

    /**
     * Returns scroll bar button type.
     *
     * @return scroll bar button type
     */
    public ScrollBarButtonType getButtonType ()
    {
        return buttonType;
    }

    /**
     * Returns scroll bar which uses this button.
     *
     * @return scroll bar which uses this button
     */
    public JScrollBar getScrollbar ()
    {
        return scrollbar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( final E button )
    {
        final boolean ver = scrollbar.getOrientation () == SwingConstants.VERTICAL;
        final boolean decr = buttonType == ScrollBarButtonType.decrease;
        if ( ver )
        {
            return new Insets ( decr ? 1 : 0, 1, decr ? 0 : 1, 1 );
        }
        else
        {
            return new Insets ( 1, decr ? 1 : 0, 1, decr ? 0 : 1 );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E button )
    {
        // Button model state
        final ButtonModel model = button.getModel ();
        final ButtonUI ui = button.getUI ();
        final boolean enabled = button.isEnabled ();
        final boolean pressed = model.isPressed () || model.isSelected ();
        final boolean rollover = ui instanceof WebButtonUI ? ( ( WebButtonUI ) ui ).isRollover () : model.isRollover ();

        // Retrieving button shape
        final Shape shape = getArrowButtonShape ( bounds, button );

        // Painting button
        g2d.setPaint ( enabled ? ( pressed ? pressedBackgroundColor : ( rollover ? rolloverBackgroundColor : backgroundColor ) ) :
                disabledBackgroundColor );
        g2d.fill ( shape );
        g2d.setPaint (
                enabled ? ( pressed ? pressedBorderColor : ( rollover ? rolloverBorderColor : borderColor ) ) : disabledBorderColor );
        g2d.draw ( shape );
    }

    /**
     * Returns popup border shape.
     *
     * @param bounds button bounds
     * @param button button component
     * @return popup border shape
     */
    protected Shape getArrowButtonShape ( final Rectangle bounds, final E button )
    {
        return ShapeCache.getShape ( button, ARROW_BUTTON_SHAPE, new DataProvider<Shape> ()
        {
            @Override
            public Shape provide ()
            {
                return createArrowButtonShape ( bounds, button );
            }
        }, getCachedShapeSettings ( button ) );
    }

    /**
     * Returns an array of shape settings cached along with the shape.
     *
     * @param button button component
     * @return an array of shape settings cached along with the shape
     */
    protected Object[] getCachedShapeSettings ( final E button )
    {
        return new Object[]{ button.getSize (), button.getInsets (), buttonType, button.getComponentOrientation ().isLeftToRight (),
                scrollbar.getOrientation () };
    }

    /**
     * Returns arrow button shape.
     *
     * @param bounds button bounds
     * @param button button component
     * @return arrow button shape
     */
    protected Shape createArrowButtonShape ( final Rectangle bounds, final E button )
    {
        final int orientation = scrollbar.getOrientation ();
        final Insets i = button.getInsets ();
        final int x = bounds.x + i.left;
        final int y = bounds.y + i.top;
        final int w = bounds.width - i.left - i.right - 1;
        final int h = bounds.height - i.top - i.bottom - 1;

        final GeneralPath shape;
        if ( orientation == SwingConstants.VERTICAL )
        {
            if ( buttonType == ScrollBarButtonType.decrease )
            {
                shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
                shape.moveTo ( x, y + h );
                shape.quadTo ( x + w / 2f, y + h * 2 / 3f, x + w, y + h );
                shape.lineTo ( x + w / 2f, y );
                shape.closePath ();
            }
            else
            {
                shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
                shape.moveTo ( x, y );
                shape.quadTo ( x + w / 2f, y + h / 3f, x + w, y );
                shape.lineTo ( x + w / 2f, y + h );
                shape.closePath ();
            }
        }
        else
        {
            final boolean ltr = scrollbar.getComponentOrientation ().isLeftToRight ();
            if ( ltr ? buttonType == ScrollBarButtonType.decrease : buttonType == ScrollBarButtonType.increase )
            {
                shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
                shape.moveTo ( x + w, y );
                shape.quadTo ( x + w * 2 / 3f, y + h / 2f, x + w, y + h );
                shape.lineTo ( x, y + h / 2f );
                shape.closePath ();
            }
            else
            {
                shape = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
                shape.moveTo ( x, y );
                shape.quadTo ( x + w / 3f, y + h / 2f, x, y + h );
                shape.lineTo ( x + w, y + h / 2f );
                shape.closePath ();
            }
        }
        return shape;
    }
}