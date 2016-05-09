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

import com.alee.laf.scroll.ScrollBarPainter;
import com.alee.laf.scroll.WebScrollBarUI;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.FocusTracker;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import java.awt.*;

/**
 * Base 9-patch painter for JScrollBar component.
 *
 * @author Mikle Garin
 */

@Deprecated
public class NPScrollBarPainter<E extends JScrollBar, U extends WebScrollBarUI> extends ScrollBarPainter<E, U>
{
    /**
     * Used 9-patch icons.
     */
    protected NinePatchIcon hBackgroundIcon = null;
    protected NinePatchIcon hFocusedBackgroundIcon = null;
    protected NinePatchIcon hTrackIcon = null;
    protected NinePatchIcon hFocusedTrackIcon = null;
    protected NinePatchIcon hThumbIcon = null;
    protected NinePatchIcon hFocusedThumbIcon = null;
    protected NinePatchIcon hPressedThumbIcon = null;
    protected NinePatchIcon vBackgroundIcon = null;
    protected NinePatchIcon vFocusedBackgroundIcon = null;
    protected NinePatchIcon vTrackIcon = null;
    protected NinePatchIcon vFocusedTrackIcon = null;
    protected NinePatchIcon vThumbIcon = null;
    protected NinePatchIcon vFocusedThumbIcon = null;
    protected NinePatchIcon vPressedThumbIcon = null;

    /**
     * Runtime variables.
     */
    protected FocusTracker focusTracker;
    protected boolean focused = false;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Disable animation for this painter
        animated = false;

        // Installing FocusTracker to keep an eye on focused state
        focusTracker = new DefaultFocusTracker ()
        {
            @Override
            public void focusChanged ( final boolean focused )
            {
                NPScrollBarPainter.this.focused = focused;
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
     * Returns horizontal scroll bar background icon.
     * It is painted within scroll bar bounds.
     *
     * @return horizontal scroll bar background icon
     */
    public NinePatchIcon getHBackgroundIcon ()
    {
        return hBackgroundIcon;
    }

    /**
     * Sets horizontal scroll bar background icon.
     * It is painted within scroll bar bounds.
     *
     * @param icon horizontal scroll bar background icon
     */
    public void setHBackgroundIcon ( final NinePatchIcon icon )
    {
        this.hBackgroundIcon = icon;
    }

    /**
     * Returns horizontal scroll bar focused background icon.
     * It is painted within scroll bar bounds when it is focused.
     * If set to {@code null} hBackgroundIcon is painted instead.
     *
     * @return horizontal scroll bar focused background icon
     */
    public NinePatchIcon getHFocusedBackgroundIcon ()
    {
        return hFocusedBackgroundIcon;
    }

    /**
     * Sets horizontal scroll bar focused background icon.
     * It is painted within scroll bar bounds when it is focused.
     * If set to {@code null} hBackgroundIcon is painted instead.
     *
     * @param icon horizontal scroll bar focused background icon
     */
    public void setHFocusedBackgroundIcon ( final NinePatchIcon icon )
    {
        this.hFocusedBackgroundIcon = icon;
    }

    /**
     * Returns horizontal scroll bar track background icon.
     * It is painted within scroll bar track bounds provided by the UI.
     *
     * @return horizontal scroll bar track background icon
     */
    public NinePatchIcon getHTrackIcon ()
    {
        return hTrackIcon;
    }

    /**
     * Sets horizontal scroll bar track background icon.
     * It is painted within scroll bar track bounds provided by the UI.
     *
     * @param icon horizontal scroll bar track background icon
     */
    public void setHTrackIcon ( final NinePatchIcon icon )
    {
        this.hTrackIcon = icon;
    }

    /**
     * Returns horizontal scroll bar focused track background icon.
     * It is painted within scroll bar track bounds provided by the UI.
     * If set to {@code null} hTrackIcon is painted instead.
     *
     * @return horizontal scroll bar focused track background icon
     */
    public NinePatchIcon getHFocusedTrackIcon ()
    {
        return hFocusedTrackIcon;
    }

    /**
     * Sets horizontal scroll bar focused track background icon.
     * It is painted within scroll bar track bounds provided by the UI.
     * If set to {@code null} hTrackIcon is painted instead.
     *
     * @param icon horizontal scroll bar focused track background icon
     */
    public void setHFocusedTrackIcon ( final NinePatchIcon icon )
    {
        this.hFocusedTrackIcon = icon;
    }

    /**
     * Returns horizontal scroll bar thumb icon.
     * It is painted within scroll bar thumb bounds provided by the UI.
     *
     * @return horizontal scroll bar thumb icon
     */
    public NinePatchIcon getHThumbIcon ()
    {
        return hThumbIcon;
    }

    /**
     * Sets horizontal scroll bar thumb icon.
     * It is painted within scroll bar thumb bounds provided by the UI.
     *
     * @param icon horizontal scroll bar thumb icon
     */
    public void setHThumbIcon ( final NinePatchIcon icon )
    {
        this.hThumbIcon = icon;
    }

    /**
     * Returns horizontal scroll bar focused thumb icon.
     * It is painted within scroll bar thumb bounds provided by the UI.
     * If set to {@code null} hThumbIcon is painted instead.
     *
     * @return horizontal scroll bar focused thumb icon
     */
    public NinePatchIcon getHFocusedThumbIcon ()
    {
        return hFocusedThumbIcon;
    }

    /**
     * Sets horizontal scroll bar focused thumb icon.
     * It is painted within scroll bar thumb bounds provided by the UI.
     * If set to {@code null} hThumbIcon is painted instead.
     *
     * @param icon horizontal scroll bar focused thumb icon
     */
    public void setHFocusedThumbIcon ( final NinePatchIcon icon )
    {
        this.hFocusedThumbIcon = icon;
    }

    /**
     * Returns horizontal scroll bar pressed thumb icon.
     * It is painted within scroll bar thumb bounds provided by the UI.
     * If set to {@code null} hFocusedThumbIcon or hThumbIcon are painted instead.
     *
     * @return horizontal scroll bar pressed thumb icon
     */
    public NinePatchIcon getHPressedThumbIcon ()
    {
        return hPressedThumbIcon;
    }

    /**
     * Sets horizontal scroll bar pressed thumb icon.
     * It is painted within scroll bar thumb bounds provided by the UI.
     * If set to {@code null} hFocusedThumbIcon or hThumbIcon are painted instead.
     *
     * @param icon horizontal scroll bar pressed thumb icon
     */
    public void setHPressedThumbIcon ( final NinePatchIcon icon )
    {
        this.hPressedThumbIcon = icon;
    }

    /**
     * Returns vertical scroll bar background icon.
     * It is painted within scroll bar bounds.
     *
     * @return vertical scroll bar background icon
     */
    public NinePatchIcon getVBackgroundIcon ()
    {
        return vBackgroundIcon;
    }

    /**
     * Sets vertical scroll bar background icon.
     * It is painted within scroll bar bounds.
     *
     * @param icon vertical scroll bar background icon
     */
    public void setVBackgroundIcon ( final NinePatchIcon icon )
    {
        this.vBackgroundIcon = icon;
    }

    /**
     * Returns vertical scroll bar focused background icon.
     * It is painted within scroll bar bounds when it is focused.
     * If set to {@code null} vBackgroundIcon is painted instead.
     *
     * @return vertical scroll bar focused background icon
     */
    public NinePatchIcon getVFocusedBackgroundIcon ()
    {
        return vFocusedBackgroundIcon;
    }

    /**
     * Sets vertical scroll bar focused background icon.
     * It is painted within scroll bar bounds when it is focused.
     * If set to {@code null} vBackgroundIcon is painted instead.
     *
     * @param icon vertical scroll bar focused background icon
     */
    public void setVFocusedBackgroundIcon ( final NinePatchIcon icon )
    {
        this.vFocusedBackgroundIcon = icon;
    }

    /**
     * Returns vertical scroll bar track background icon.
     * It is painted within scroll bar track bounds provided by the UI.
     *
     * @return vertical scroll bar track background icon
     */
    public NinePatchIcon getVTrackIcon ()
    {
        return vTrackIcon;
    }

    /**
     * Sets vertical scroll bar track background icon.
     * It is painted within scroll bar track bounds provided by the UI.
     *
     * @param icon vertical scroll bar track background icon
     */
    public void setVTrackIcon ( final NinePatchIcon icon )
    {
        this.vTrackIcon = icon;
    }

    /**
     * Returns vertical scroll bar focused track background icon.
     * It is painted within scroll bar track bounds provided by the UI.
     * If set to {@code null} vTrackIcon is painted instead.
     *
     * @return vertical scroll bar focused track background icon
     */
    public NinePatchIcon getVFocusedTrackIcon ()
    {
        return vFocusedTrackIcon;
    }

    /**
     * Sets vertical scroll bar focused track background icon.
     * It is painted within scroll bar track bounds provided by the UI.
     * If set to {@code null} vTrackIcon is painted instead.
     *
     * @param icon vertical scroll bar focused track background icon
     */
    public void setVFocusedTrackIcon ( final NinePatchIcon icon )
    {
        this.vFocusedTrackIcon = icon;
    }

    /**
     * Returns vertical scroll bar thumb icon.
     * It is painted within scroll bar thumb bounds provided by the UI.
     *
     * @return vertical scroll bar thumb icon
     */
    public NinePatchIcon getVThumbIcon ()
    {
        return vThumbIcon;
    }

    /**
     * Sets vertical scroll bar thumb icon.
     * It is painted within scroll bar thumb bounds provided by the UI.
     *
     * @param icon vertical scroll bar thumb icon
     */
    public void setVThumbIcon ( final NinePatchIcon icon )
    {
        this.vThumbIcon = icon;
    }

    /**
     * Returns vertical scroll bar focused thumb icon.
     * It is painted within scroll bar thumb bounds provided by the UI.
     * If set to {@code null} vThumbIcon is painted instead.
     *
     * @return vertical scroll bar focused thumb icon
     */
    public NinePatchIcon getVFocusedThumbIcon ()
    {
        return vFocusedThumbIcon;
    }

    /**
     * Sets vertical scroll bar focused thumb icon.
     * It is painted within scroll bar thumb bounds provided by the UI.
     * If set to {@code null} vThumbIcon is painted instead.
     *
     * @param icon vertical scroll bar focused thumb icon
     */
    public void setVFocusedThumbIcon ( final NinePatchIcon icon )
    {
        this.vFocusedThumbIcon = icon;
    }

    /**
     * Returns vertical scroll bar pressed thumb icon.
     * It is painted within scroll bar thumb bounds provided by the UI.
     * If set to {@code null} vFocusedThumbIcon or vThumbIcon are painted instead.
     *
     * @return vertical scroll bar pressed thumb icon
     */
    public NinePatchIcon getVPressedThumbIcon ()
    {
        return vPressedThumbIcon;
    }

    /**
     * Sets vertical scroll bar pressed thumb icon.
     * It is painted within scroll bar thumb bounds provided by the UI.
     * If set to {@code null} vFocusedThumbIcon or vThumbIcon are painted instead.
     *
     * @param icon vertical scroll bar pressed thumb icon
     */
    public void setVPressedThumbIcon ( final NinePatchIcon icon )
    {
        this.vPressedThumbIcon = icon;
    }

    @Override
    protected void paintBackground ( final Graphics2D g2d, final E scrollbar, final Rectangle b )
    {
        if ( ui.isPaintTrack () )
        {
            final NinePatchIcon backgroundIcon = getCurrentBackgroundIcon ( scrollbar );
            if ( backgroundIcon != null )
            {
                backgroundIcon.paintIcon ( g2d, b );
            }
        }
    }

    /**
     * Returns background 9-patch icon that should be painted right now.
     *
     * @param scrollbar painted scrollbar
     * @return background 9-patch icon that should be painted right now
     */
    protected NinePatchIcon getCurrentBackgroundIcon ( final E scrollbar )
    {
        if ( scrollbar.getOrientation () == JScrollBar.HORIZONTAL )
        {
            return focused && hFocusedBackgroundIcon != null ? hFocusedBackgroundIcon : hBackgroundIcon;
        }
        else
        {
            return focused && vFocusedBackgroundIcon != null ? vFocusedBackgroundIcon : vBackgroundIcon;
        }
    }

    @Override
    protected void paintTrack ( final Graphics2D g2d, final E scrollbar, final Rectangle b )
    {
        if ( ui.isPaintTrack () )
        {
            final NinePatchIcon backgroundIcon = getCurrentTrackIcon ( scrollbar );
            if ( backgroundIcon != null )
            {
                backgroundIcon.paintIcon ( g2d, b );
            }
        }
    }

    /**
     * Returns track 9-patch icon that should be painted right now.
     *
     * @param scrollbar painted scrollbar
     * @return track 9-patch icon that should be painted right now
     */
    protected NinePatchIcon getCurrentTrackIcon ( final E scrollbar )
    {
        if ( scrollbar.getOrientation () == JScrollBar.HORIZONTAL )
        {
            return focused && hFocusedTrackIcon != null ? hFocusedTrackIcon : hTrackIcon;
        }
        else
        {
            return focused && vFocusedTrackIcon != null ? vFocusedTrackIcon : vTrackIcon;
        }
    }

    @Override
    protected void paintThumb ( final Graphics2D g2d, final E scrollbar, final Rectangle b )
    {
        final NinePatchIcon thumbIcon = getCurrentThumbIcon ( scrollbar );
        if ( thumbIcon != null )
        {
            final Insets m = getCurrentThumbMargin ( scrollbar );
            thumbIcon.paintIcon ( g2d, b.x + m.left, b.y + m.top, b.width - m.left - m.right, b.height - m.top - m.bottom );
        }
    }

    /**
     * Returns thumb 9-patch icon that should be painted right now.
     *
     * @param scrollbar painted scrollbar
     * @return thumb 9-patch icon that should be painted right now
     */
    protected NinePatchIcon getCurrentThumbIcon ( final E scrollbar )
    {
        if ( scrollbar.getOrientation () == JScrollBar.HORIZONTAL )
        {
            return ( pressed || dragged ) && hPressedThumbIcon != null ? hPressedThumbIcon :
                    focused && hFocusedBackgroundIcon != null ? hFocusedThumbIcon : hThumbIcon;
        }
        else
        {
            return ( pressed || dragged ) && vPressedThumbIcon != null ? vPressedThumbIcon :
                    focused && vFocusedBackgroundIcon != null ? vFocusedThumbIcon : vThumbIcon;
        }
    }
}