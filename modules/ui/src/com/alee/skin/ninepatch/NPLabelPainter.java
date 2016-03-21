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

package com.alee.skin.ninepatch;

import com.alee.laf.label.LabelPainter;
import com.alee.laf.label.WebLabelUI;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.FocusTracker;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import java.awt.*;

/**
 * Base 9-patch painter for JLabel component.
 *
 * @author Mikle Garin
 */

@Deprecated
public class NPLabelPainter<E extends JLabel, U extends WebLabelUI, D extends IDecoration<E, D>> extends LabelPainter<E, U, D>
{
    /**
     * Style settings.
     */
    protected boolean undecorated = false;
    protected boolean paintFocus = false;

    /**
     * Used 9-patch icons.
     */
    protected NinePatchIcon backgroundIcon = null;
    protected NinePatchIcon focusedBackgroundIcon = null;


    /**
     * Runtime variables.
     */
    protected FocusTracker focusTracker;
    protected boolean focused = false;

    /**
     * Constructs 9-patch label painter using the specified background icon.
     *
     * @param backgroundIcon background 9-patch icon
     */
    public NPLabelPainter ( final NinePatchIcon backgroundIcon )
    {
        super ();
        setBackgroundIcon ( backgroundIcon );
    }

    /**
     * Constructs 9-patch label painter using the specified background icons.
     *
     * @param backgroundIcon        background 9-patch icon
     * @param focusedBackgroundIcon focused background 9-patch icon
     */
    public NPLabelPainter ( final NinePatchIcon backgroundIcon, final NinePatchIcon focusedBackgroundIcon )
    {
        super ();
        setBackgroundIcon ( backgroundIcon );
        setFocusedBackgroundIcon ( focusedBackgroundIcon );
    }

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Installing FocusTracker to keep an eye on focused state
        focusTracker = new DefaultFocusTracker ()
        {
            @Override
            public boolean isTrackingEnabled ()
            {
                return !undecorated && paintFocus;
            }

            @Override
            public void focusChanged ( final boolean focused )
            {
                NPLabelPainter.this.focused = focused;
                repaint ();
            }
        };
        FocusManager.addFocusTracker ( c, focusTracker );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing FocusTracker
        FocusManager.removeFocusTracker ( focusTracker );
        focusTracker = null;

        super.uninstall ( c, ui );
    }

    /**
     * Returns whether decoration should be painted or not.
     *
     * @return true if decoration should be painted, false otherwise
     */
    public boolean isUndecorated ()
    {
        return undecorated;
    }

    /**
     * Sets whether decoration should be painted or not.
     *
     * @param undecorated whether decoration should be painted or not
     */
    public void setUndecorated ( final boolean undecorated )
    {
        if ( this.undecorated != undecorated )
        {
            this.undecorated = undecorated;
            updateAll ();
        }
    }

    /**
     * Returns whether focus should be painted or not.
     *
     * @return true if focus should be painted, false otherwise
     */
    public boolean isPaintFocus ()
    {
        return paintFocus;
    }

    /**
     * Sets whether focus should be painted or not.
     *
     * @param paint whether focus should be painted or not
     */
    public void setPaintFocus ( final boolean paint )
    {
        if ( this.paintFocus != paint )
        {
            this.paintFocus = paint;
            updateAll ();
        }
    }

    /**
     * Returns background 9-patch icon.
     *
     * @return background 9-patch icon
     */
    public NinePatchIcon getBackgroundIcon ()
    {
        return backgroundIcon;
    }

    /**
     * Sets background 9-patch icon.
     *
     * @param icon background 9-patch icon
     */
    public void setBackgroundIcon ( final NinePatchIcon icon )
    {
        this.backgroundIcon = icon;
        if ( !undecorated && ( !paintFocus || !focused ) )
        {
            updateAll ();
        }
    }

    /**
     * Returns focused background 9-patch icon.
     *
     * @return focused background 9-patch icon
     */
    public NinePatchIcon getFocusedBackgroundIcon ()
    {
        return focusedBackgroundIcon;
    }

    /**
     * Sets focused background 9-patch icon.
     *
     * @param icon focused background 9-patch icon
     */
    public void setFocusedBackgroundIcon ( final NinePatchIcon icon )
    {
        this.focusedBackgroundIcon = icon;
        if ( !undecorated && paintFocus && focused )
        {
            updateAll ();
        }
    }

    @Override
    public Insets getBorders ()
    {
        final NinePatchIcon backgroundIcon = getCurrentBackgroundIcon ();
        return !undecorated && backgroundIcon != null ? backgroundIcon.getMargin () : super.getBorders ();
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Paint simple background if undecorated & opaque
        if ( undecorated && c.isOpaque () )
        {
            g2d.setPaint ( c.getBackground () );
            g2d.fillRect ( bounds.x, bounds.y, bounds.width, bounds.height );
        }

        // Painting background decoration
        if ( !undecorated )
        {
            final NinePatchIcon backgroundIcon = getCurrentBackgroundIcon ();
            if ( backgroundIcon != null )
            {
                backgroundIcon.paintIcon ( g2d, bounds );
            }
        }

        // Painting label
        super.paint ( g2d, bounds, c, ui );
    }

    /**
     * Returns background 9-patch icon that should be painted right now.
     *
     * @return background 9-patch icon that should be painted right now
     */
    protected NinePatchIcon getCurrentBackgroundIcon ()
    {
        return focused && focusedBackgroundIcon != null ? focusedBackgroundIcon : backgroundIcon;
    }
}