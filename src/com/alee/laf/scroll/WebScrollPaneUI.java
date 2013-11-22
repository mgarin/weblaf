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

import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.focus.DefaultFocusTracker;
import com.alee.managers.focus.FocusManager;
import com.alee.managers.focus.FocusTracker;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * User: mgarin Date: 29.04.11 Time: 15:34
 */

public class WebScrollPaneUI extends BasicScrollPaneUI implements ShapeProvider
{
    private boolean drawBorder = WebScrollPaneStyle.drawBorder;
    private Color borderColor = WebScrollPaneStyle.borderColor;
    private Color darkBorder = WebScrollPaneStyle.darkBorder;

    private int round = WebScrollPaneStyle.round;
    private int shadeWidth = WebScrollPaneStyle.shadeWidth;
    private Insets margin = WebScrollPaneStyle.margin;

    private boolean drawFocus = WebScrollPaneStyle.drawFocus;
    private boolean drawBackground = WebScrollPaneStyle.drawBackground;

    private WebScrollPaneCorner corner;
    private PropertyChangeListener propertyChangeListener;

    /**
     * Scroll pane focus tracker.
     */
    protected FocusTracker focusTracker;

    private boolean focused = false;

    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebScrollPaneUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( scrollpane );
        scrollpane.setOpaque ( false );
        scrollpane.setBackground ( StyleConstants.backgroundColor );

        // Border
        updateBorder ();

        // Styled scroll pane corner
        scrollpane.setCorner ( JScrollPane.LOWER_TRAILING_CORNER, getCornerComponent () );
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                scrollpane.setCorner ( JScrollPane.LOWER_TRAILING_CORNER, getCornerComponent () );
            }
        };
        scrollpane.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );

        // Focus tracker for the scroll pane content
        focusTracker = new DefaultFocusTracker ()
        {
            @Override
            public boolean isTrackingEnabled ()
            {
                return drawBorder && drawFocus;
            }

            @Override
            public void focusChanged ( final boolean focused )
            {
                WebScrollPaneUI.this.focused = focused;
                scrollpane.repaint ();
            }
        };
        FocusManager.addFocusTracker ( scrollpane, focusTracker );
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        scrollpane.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );
        scrollpane.remove ( getCornerComponent () );

        FocusManager.removeFocusTracker ( focusTracker );

        super.uninstallUI ( c );
    }

    private WebScrollPaneCorner getCornerComponent ()
    {
        if ( corner == null )
        {
            corner = new WebScrollPaneCorner ();
        }
        return corner;
    }

    @Override
    public Shape provideShape ()
    {
        return LafUtils.getWebBorderShape ( scrollpane, getShadeWidth (), getRound () );
    }

    private void updateBorder ()
    {
        if ( scrollpane != null )
        {
            // Preserve old borders
            if ( SwingUtils.isPreserveBorders ( scrollpane ) )
            {
                return;
            }

            final Insets insets;
            if ( drawBorder )
            {
                insets = new Insets ( shadeWidth + 1 + margin.top, shadeWidth + 1 + margin.left, shadeWidth + 1 + margin.bottom,
                        shadeWidth + 1 + margin.right );
            }
            else
            {
                insets = new Insets ( margin.top, margin.left, margin.bottom, margin.right );
            }
            scrollpane.setBorder ( LafUtils.createWebBorder ( insets ) );
        }
    }

    public boolean isDrawBorder ()
    {
        return drawBorder;
    }

    public void setDrawBorder ( final boolean drawBorder )
    {
        this.drawBorder = drawBorder;
        updateBorder ();
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( final int round )
    {
        this.round = round;
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( final int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateBorder ();
    }

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    public boolean isDrawFocus ()
    {
        return drawFocus;
    }

    public void setDrawFocus ( final boolean drawFocus )
    {
        this.drawFocus = drawFocus;
    }

    public boolean isDrawBackground ()
    {
        return drawBackground;
    }

    public void setDrawBackground ( final boolean drawBackground )
    {
        this.drawBackground = drawBackground;
    }

    public Color getBorderColor ()
    {
        return borderColor;
    }

    public void setBorderColor ( final Color borderColor )
    {
        this.borderColor = borderColor;
    }

    public Color getDarkBorder ()
    {
        return darkBorder;
    }

    public void setDarkBorder ( final Color darkBorder )
    {
        this.darkBorder = darkBorder;
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( drawBorder )
        {
            // Border, background and shade
            LafUtils.drawWebStyle ( ( Graphics2D ) g, c, drawFocus && focused ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor,
                    shadeWidth, round, drawBackground, false );
        }

        super.paint ( g, c );
    }
}
