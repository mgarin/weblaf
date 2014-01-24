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

package com.alee.extended.button;

import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButtonUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for WebSplitButton component.
 * This UI is based on WebButtonUI and simply adds a few features.
 *
 * @author Mikle Garin
 */

public class WebSplitButtonUI extends WebButtonUI
{
    /**
     * Style settings.
     */
    protected ImageIcon splitIcon = WebSplitButtonStyle.splitIcon;
    protected int splitIconGap = WebSplitButtonStyle.splitIconGap;
    protected int contentGap = WebSplitButtonStyle.contentGap;

    /**
     * Returns an instance of the WebSplitButtonUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebSplitButtonUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebSplitButtonUI ();
    }

    /**
     * Returns split button icon.
     *
     * @return split button icon
     */
    public ImageIcon getSplitIcon ()
    {
        return splitIcon;
    }

    /**
     * Sets split button icon
     *
     * @param splitIcon new split button icon
     */
    public void setSplitIcon ( final ImageIcon splitIcon )
    {
        this.splitIcon = splitIcon;
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
    protected Insets getBorderInsets ()
    {
        final Insets i = super.getBorderInsets ();

        // Adding split part width to appropriate border side
        final boolean ltr = button.getComponentOrientation ().isLeftToRight ();
        final int splitPartWidth = splitIcon.getIconWidth () + 1 + splitIconGap * 2 + contentGap;
        if ( ltr )
        {
            i.right += splitPartWidth;
        }
        else
        {
            i.left += splitPartWidth;
        }

        return i;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        super.paint ( g, c );

        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final Rectangle rect = getSplitButtonBounds ( c );

        // Painting split button icon
        final int ix = rect.x + rect.width / 2 - splitIcon.getIconWidth () / 2;
        final int iy = rect.y + rect.height / 2 - splitIcon.getIconHeight () / 2;
        g.drawImage ( splitIcon.getImage (), ix, iy, null );

        // Painting split button line
        final int lineX = ltr ? rect.x : rect.x + rect.width - 1;
        g.setColor ( c.isEnabled () ? StyleConstants.borderColor : StyleConstants.disabledBorderColor );
        g.drawLine ( lineX, rect.y + 1, lineX, rect.y + rect.height - 2 );
    }

    /**
     * Returns bounds of the split button part.
     *
     * @param c split button
     * @return bounds of the split button part
     */
    public Rectangle getSplitButtonBounds ( final JComponent c )
    {
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final Insets i = c.getInsets ();
        final int styleSide = drawRight ? shadeWidth + 1 : ( drawRightLine ? 1 : 0 );
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
    public Rectangle getSplitButtonHitbox ( final JComponent c )
    {
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final Insets i = c.getInsets ();
        return ltr ? new Rectangle ( c.getWidth () - i.right + contentGap, 0, i.right - contentGap, c.getHeight () ) :
                new Rectangle ( 0, 0, i.left - contentGap, c.getHeight () );
    }
}