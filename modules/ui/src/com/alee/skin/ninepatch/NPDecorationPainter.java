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

import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.FocusTracker;
import com.alee.painter.AbstractPainter;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * 9-patch partial decoration painter for any kind of components.
 * This painter is similar to {@code WebDecorationPainter} but this one is based on 9-patch icons.
 * It uses 9-patch icons for normal and focused component states and its side separators.
 *
 * @author Mikle Garin
 * @see com.alee.managers.style.skin.web.WebDecorationPainter
 */

@Deprecated
public class NPDecorationPainter<E extends JComponent, U extends ComponentUI> extends AbstractPainter<E, U>
{
    /**
     * todo 1. Extend WebDecorationPainter to take over variables part
     */

    /**
     * Style settings.
     */
    protected boolean undecorated = false;
    protected boolean paintFocus = false;
    protected boolean paintTop = true;
    protected boolean paintLeft = true;
    protected boolean paintBottom = true;
    protected boolean paintRight = true;
    protected boolean paintTopLine = true;
    protected boolean paintLeftLine = true;
    protected boolean paintBottomLine = true;
    protected boolean paintRightLine = true;
    protected boolean detectSideByContentPatches = false;
    protected int hiddenSideSpacing = 0;

    /**
     * Used 9-patch icons.
     */
    protected NinePatchIcon backgroundIcon = null;
    protected NinePatchIcon focusedBackgroundIcon = null;
    protected NinePatchIcon separatorIcon = null;
    protected NinePatchIcon topSeparatorIcon = null;
    protected NinePatchIcon leftSeparatorIcon = null;
    protected NinePatchIcon bottomSeparatorIcon = null;
    protected NinePatchIcon rightSeparatorIcon = null;

    /**
     * Runtime variables.
     */
    protected FocusTracker focusTracker;
    protected boolean focused = false;

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
                NPDecorationPainter.this.focused = focused;
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
            repaint ();
        }
    }

    public int getShadeWidth ()
    {
        // todo Implement later?
        return 0;
    }

    public void setShadeWidth ( final int width )
    {
        // todo Implement later?
    }

    /**
     * Returns whether should paint top side or not.
     *
     * @return true if should paint top side, false otherwise
     */
    public boolean isPaintTop ()
    {
        return paintTop;
    }

    public void setPaintTop ( final boolean top )
    {
        if ( this.paintTop != top )
        {
            this.paintTop = top;
            updateAll ();
        }
    }

    /**
     * Returns whether should paint left side or not.
     *
     * @return true if should paint left side, false otherwise
     */
    public boolean isPaintLeft ()
    {
        return paintLeft;
    }

    public void setPaintLeft ( final boolean left )
    {
        if ( this.paintLeft != left )
        {
            this.paintLeft = left;
            updateAll ();
        }
    }

    /**
     * Returns whether should paint bottom side or not.
     *
     * @return true if should paint bottom side, false otherwise
     */
    public boolean isPaintBottom ()
    {
        return paintBottom;
    }

    public void setPaintBottom ( final boolean bottom )
    {
        if ( this.paintBottom != bottom )
        {
            this.paintBottom = bottom;
            updateAll ();
        }
    }

    /**
     * Returns whether should paint right side or not.
     *
     * @return true if should paint right side, false otherwise
     */
    public boolean isPaintRight ()
    {
        return paintRight;
    }

    public void setPaintRight ( final boolean right )
    {
        if ( this.paintRight != right )
        {
            this.paintRight = right;
            updateAll ();
        }
    }

    public void setPaintSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        if ( this.paintTop != top || this.paintLeft != left || this.paintBottom != bottom || this.paintRight != right )
        {
            this.paintTop = top;
            this.paintLeft = left;
            this.paintBottom = bottom;
            this.paintRight = right;
            updateAll ();
        }
    }

    /**
     * Returns whether should paint top side line or not.
     *
     * @return true if should paint top side line, false otherwise
     */
    public boolean isPaintTopLine ()
    {
        return paintTopLine;
    }

    public void setPaintTopLine ( final boolean top )
    {
        if ( this.paintTopLine != top )
        {
            this.paintTopLine = top;
            updateAll ();
        }
    }

    /**
     * Returns whether should paint left side line or not.
     *
     * @return true if should paint left side line, false otherwise
     */
    public boolean isPaintLeftLine ()
    {
        return paintLeftLine;
    }

    public void setPaintLeftLine ( final boolean left )
    {
        if ( this.paintLeftLine != left )
        {
            this.paintLeftLine = left;
            updateAll ();
        }
    }

    /**
     * Returns whether should paint bottom side line or not.
     *
     * @return true if should paint bottom side line, false otherwise
     */
    public boolean isPaintBottomLine ()
    {
        return paintBottomLine;
    }

    public void setPaintBottomLine ( final boolean bottom )
    {
        if ( this.paintBottomLine != bottom )
        {
            this.paintBottomLine = bottom;
            updateAll ();
        }
    }

    /**
     * Returns whether should paint right side line or not.
     *
     * @return true if should paint right side line, false otherwise
     */
    public boolean isPaintRightLine ()
    {
        return paintRightLine;
    }

    public void setPaintRightLine ( final boolean right )
    {
        if ( this.paintRightLine != right )
        {
            this.paintRightLine = right;
            updateAll ();
        }
    }

    public void setPaintSideLines ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        if ( this.paintTopLine != top || this.paintLeftLine != left || this.paintBottomLine != bottom || this.paintRightLine != right )
        {
            this.paintTopLine = top;
            this.paintLeftLine = left;
            this.paintBottomLine = bottom;
            this.paintRightLine = right;
            updateAll ();
        }
    }

    /**
     * Returns whether single decoration side size should be detected by content patches on the image or not.
     * When true - hidden panel sides will cut appropriate 9-patch image sides using its content patches.
     * When false - hidden panel sides will cut appropriate 9-patch image sides using its stretch patches.
     *
     * @return true if single decoration side size should be detected by content patches on the image, false otherwise
     */
    public boolean isDetectSideByContentPatches ()
    {
        return detectSideByContentPatches;
    }

    /**
     * Set whether single decoration side size should be detected by content patches on the image or not.
     * If set to true - hidden panel sides will cut appropriate 9-patch image sides using its content patches.
     * If set to - hidden panel sides will cut appropriate 9-patch image sides using its stretch patches.
     *
     * @param detectSideByContentPatches whether single decoration side size should be detected by content patches on the image or not
     */
    public void setDetectSideByContentPatches ( final boolean detectSideByContentPatches )
    {
        if ( this.detectSideByContentPatches != detectSideByContentPatches )
        {
            this.detectSideByContentPatches = detectSideByContentPatches;
            updateAll ();
        }
    }

    /**
     * Returns additional spacing provided for side with hidden decoration.
     *
     * @return additional spacing provided for side with hidden decoration
     */
    public int getHiddenSideSpacing ()
    {
        return hiddenSideSpacing;
    }

    /**
     * Sets additional spacing provided for side with hidden decoration.
     * This might be useful if you want to keep default component size the same even if some sides are hidden.
     *
     * @param spacing additional spacing provided for side with hidden decoration
     */
    public void setHiddenSideSpacing ( final int spacing )
    {
        if ( this.hiddenSideSpacing != spacing )
        {
            this.hiddenSideSpacing = spacing;
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

    /**
     * Returns default side separator 9-patch icon.
     * This icon is used instead of specific side separator icon if one not specified.
     *
     * @return default side separator 9-patch icon
     */
    public NinePatchIcon getSeparatorIcon ()
    {
        return separatorIcon;
    }

    /**
     * Sets default side separator 9-patch icon.
     * This icon is used instead of specific side separator icon if one not specified.
     *
     * @param icon default side separator 9-patch icon
     */
    public void setSeparatorIcon ( final NinePatchIcon icon )
    {
        this.separatorIcon = icon;
        if ( !undecorated && isAnyLineShouldBePainted () )
        {
            updateAll ();
        }
    }

    /**
     * Sets top side separator 9-patch icon.
     *
     * @return top side separator 9-patch icon
     */
    public NinePatchIcon getTopSeparatorIcon ()
    {
        return topSeparatorIcon;
    }

    /**
     * Sets top side separator 9-patch icon.
     *
     * @param icon top side separator 9-patch icon
     */
    public void setTopSeparatorIcon ( final NinePatchIcon icon )
    {
        this.topSeparatorIcon = icon;
        if ( !undecorated && isTopLineShouldBePainted () )
        {
            updateAll ();
        }
    }

    /**
     * Returns left side separator 9-patch icon.
     *
     * @return left side separator 9-patch icon
     */
    public NinePatchIcon getLeftSeparatorIcon ()
    {
        return leftSeparatorIcon;
    }

    /**
     * Sets left side separator 9-patch icon.
     *
     * @param icon left side separator 9-patch icon
     */
    public void setLeftSeparatorIcon ( final NinePatchIcon icon )
    {
        this.leftSeparatorIcon = icon;
        if ( !undecorated && isLeftLineShouldBePainted () )
        {
            updateAll ();
        }
    }

    /**
     * Returns bottom side separator 9-patch icon.
     *
     * @return bottom side separator 9-patch icon
     */
    public NinePatchIcon getBottomSeparatorIcon ()
    {
        return bottomSeparatorIcon;
    }

    /**
     * Sets bottom side separator 9-patch icon.
     *
     * @param icon bottom side separator 9-patch icon
     */
    public void setBottomSeparatorIcon ( final NinePatchIcon icon )
    {
        this.bottomSeparatorIcon = icon;
        if ( !undecorated && isBottomLineShouldBePainted () )
        {
            updateAll ();
        }
    }

    /**
     * Returns right side separator 9-patch icon.
     *
     * @return right side separator 9-patch icon
     */
    public NinePatchIcon getRightSeparatorIcon ()
    {
        return rightSeparatorIcon;
    }

    /**
     * Sets right side separator 9-patch icon.
     *
     * @param icon right side separator 9-patch icon
     */
    public void setRightSeparatorIcon ( final NinePatchIcon icon )
    {
        this.rightSeparatorIcon = icon;
        if ( !undecorated && isRightLineShouldBePainted () )
        {
            updateAll ();
        }
    }

    @Override
    public Insets getBorders ()
    {
        final NinePatchIcon backgroundIcon = getCurrentBackgroundIcon ();
        if ( !undecorated && backgroundIcon != null )
        {
            final Insets margin = backgroundIcon.getMargin ();
            if ( !paintTop )
            {
                final NinePatchIcon icon = getCurrentTopSeparatorIcon ();
                margin.top = ( paintTopLine && icon != null ? icon.getPreferredSize ().height : 0 ) + hiddenSideSpacing;
            }
            if ( !paintLeft )
            {
                final NinePatchIcon icon = getCurrentLeftSeparatorIcon ();
                margin.left = ( paintLeftLine && icon != null ? icon.getPreferredSize ().width : 0 ) + hiddenSideSpacing;
            }
            if ( !paintBottom )
            {
                final NinePatchIcon icon = getCurrentBottomSeparatorIcon ();
                margin.bottom = ( paintBottomLine && icon != null ? icon.getPreferredSize ().height : 0 ) + hiddenSideSpacing;
            }
            if ( !paintRight )
            {
                final NinePatchIcon icon = getCurrentRightSeparatorIcon ();
                margin.right = ( paintRightLine && icon != null ? icon.getPreferredSize ().width : 0 ) + hiddenSideSpacing;
            }
            return margin;
        }
        else
        {
            return super.getBorders ();
        }
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        if ( !undecorated )
        {
            // Painting background decoration
            final NinePatchIcon backgroundIcon = getCurrentBackgroundIcon ();
            if ( backgroundIcon != null )
            {
                backgroundIcon.paintIcon ( g2d, getBackgroundBounds ( backgroundIcon, bounds, c ) );
            }

            // Painting appropriate separators
            if ( isTopLineShouldBePainted () )
            {
                final NinePatchIcon icon = getCurrentTopSeparatorIcon ();
                if ( icon != null )
                {
                    final Dimension ps = icon.getPreferredSize ();
                    icon.paintIcon ( g2d, bounds.x, bounds.y, bounds.width, ps.height );
                }
            }
            if ( isLeftLineShouldBePainted () )
            {
                final NinePatchIcon icon = getCurrentLeftSeparatorIcon ();
                if ( icon != null )
                {
                    final Dimension ps = icon.getPreferredSize ();
                    icon.paintIcon ( g2d, bounds.x, bounds.y, ps.width, bounds.height );
                }
            }
            if ( isBottomLineShouldBePainted () )
            {
                final NinePatchIcon icon = getCurrentBottomSeparatorIcon ();
                if ( icon != null )
                {
                    final Dimension ps = icon.getPreferredSize ();
                    icon.paintIcon ( g2d, bounds.x, bounds.y + bounds.height - ps.height, bounds.width, ps.height );
                }
            }
            if ( isRightLineShouldBePainted () )
            {
                final NinePatchIcon icon = getCurrentRightSeparatorIcon ();
                if ( icon != null )
                {
                    final Dimension ps = icon.getPreferredSize ();
                    icon.paintIcon ( g2d, bounds.x + bounds.width - ps.width, bounds.y, ps.width, bounds.height );
                }
            }
        }
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

    /**
     * Returns top separator 9-patch icon that should be painted according to provided icons.
     *
     * @return top separator 9-patch icon that should be painted according to provided icons
     */
    protected NinePatchIcon getCurrentTopSeparatorIcon ()
    {
        return topSeparatorIcon != null ? topSeparatorIcon : separatorIcon;
    }

    /**
     * Returns left separator 9-patch icon that should be painted according to provided icons.
     *
     * @return left separator 9-patch icon that should be painted according to provided icons
     */
    protected NinePatchIcon getCurrentLeftSeparatorIcon ()
    {
        return leftSeparatorIcon != null ? leftSeparatorIcon : separatorIcon;
    }

    /**
     * Returns bottom separator 9-patch icon that should be painted according to provided icons.
     *
     * @return bottom separator 9-patch icon that should be painted according to provided icons
     */
    protected NinePatchIcon getCurrentBottomSeparatorIcon ()
    {
        return bottomSeparatorIcon != null ? bottomSeparatorIcon : separatorIcon;
    }

    /**
     * Returns right separator 9-patch icon that should be painted according to provided icons.
     *
     * @return right separator 9-patch icon that should be painted according to provided icons
     */
    protected NinePatchIcon getCurrentRightSeparatorIcon ()
    {
        return rightSeparatorIcon != null ? rightSeparatorIcon : separatorIcon;
    }

    /**
     * Returns whether at least one side line should be painted or not.
     * This method is used to optimize painting performance.
     *
     * @return true if at least one side line should be painted, false otherwise
     */
    protected boolean isAnyLineShouldBePainted ()
    {
        return isTopLineShouldBePainted () || isLeftLineShouldBePainted () ||
                isBottomLineShouldBePainted () || isRightLineShouldBePainted ();
    }

    /**
     * Returns whether top side line should be painted or not.
     *
     * @return true if top side line should be painted, false otherwise
     */
    protected boolean isTopLineShouldBePainted ()
    {
        return !paintTop && paintTopLine;
    }

    /**
     * Returns whether left side line should be painted or not.
     *
     * @return true if left side line should be painted, false otherwise
     */
    protected boolean isLeftLineShouldBePainted ()
    {
        return !paintLeft && paintLeftLine;
    }

    /**
     * Returns whether bottom side line should be painted or not.
     *
     * @return true if bottom side line should be painted, false otherwise
     */
    protected boolean isBottomLineShouldBePainted ()
    {
        return !paintBottom && paintBottomLine;
    }

    /**
     * Returns whether right side line should be painted or not.
     *
     * @return true if right side line should be painted, false otherwise
     */
    protected boolean isRightLineShouldBePainted ()
    {
        return !paintRight && paintRightLine;
    }

    /**
     * Returns bounds within which background 9-patch icon should be painted.
     * These bounds might not always fit component bounds to make a clipped side trick.
     *
     * @param icon background 9-patch icon
     * @param b    component bounds
     * @param c    component instance
     * @return bounds within which background 9-patch icon should be painted
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected Rectangle getBackgroundBounds ( final NinePatchIcon icon, final Rectangle b, final E c )
    {
        if ( !undecorated && icon != null )
        {
            final Insets margin = detectSideByContentPatches ? icon.getMargin () : icon.getStretchMargin ();
            final int ts = paintTop ? 0 : margin.top;
            final int ls = paintLeft ? 0 : margin.left;
            final int bs = paintBottom ? 0 : margin.bottom;
            final int rs = paintRight ? 0 : margin.right;
            return new Rectangle ( b.x - ls, b.y - ts, b.width + ls + rs, b.height + ts + bs );
        }
        else
        {
            return b;
        }
    }
}