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
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebScrollPaneUI ();
    }

    @Override
    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( scrollpane );
        scrollpane.setOpaque ( false );
        scrollpane.setBackground ( StyleConstants.backgroundColor );

        // Border
        updateBorder ( scrollpane );

        // Styled scroll pane corner
        scrollpane.setCorner ( JScrollPane.LOWER_TRAILING_CORNER, getCornerComponent () );
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( PropertyChangeEvent evt )
            {
                scrollpane.setCorner ( JScrollPane.LOWER_TRAILING_CORNER, getCornerComponent () );
            }
        };
        scrollpane.addPropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );

        // Focus tracker for the scroll pane content
        focusTracker = new DefaultFocusTracker ()
        {
            @Override
            public boolean isTrackingEnabled ()
            {
                return drawBorder && drawFocus;
            }

            @Override
            public void focusChanged ( boolean focused )
            {
                WebScrollPaneUI.this.focused = focused;
                scrollpane.repaint ();
            }
        };
        FocusManager.addFocusTracker ( scrollpane, focusTracker );
    }

    @Override
    public void uninstallUI ( JComponent c )
    {
        scrollpane.removePropertyChangeListener ( WebLookAndFeel.COMPONENT_ORIENTATION_PROPERTY, propertyChangeListener );
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

    private void updateBorder ( JComponent scrollPane )
    {
        if ( scrollPane != null )
        {
            if ( drawBorder )
            {
                scrollPane.setBorder ( BorderFactory
                        .createEmptyBorder ( shadeWidth + 1 + margin.top, shadeWidth + 1 + margin.left, shadeWidth + 1 + margin.bottom,
                                shadeWidth + 1 + margin.right ) );
            }
            else
            {
                scrollPane.setBorder ( BorderFactory.createEmptyBorder ( margin.top, margin.left, margin.bottom, margin.right ) );
            }
        }
    }

    public boolean isDrawBorder ()
    {
        return drawBorder;
    }

    public void setDrawBorder ( boolean drawBorder )
    {
        this.drawBorder = drawBorder;
        updateBorder ( scrollpane );
    }

    public int getRound ()
    {
        return round;
    }

    public void setRound ( int round )
    {
        this.round = round;
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
        updateBorder ( scrollpane );
    }

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( Insets margin )
    {
        this.margin = margin;
        updateBorder ( scrollpane );
    }

    public boolean isDrawFocus ()
    {
        return drawFocus;
    }

    public void setDrawFocus ( boolean drawFocus )
    {
        this.drawFocus = drawFocus;
    }

    public boolean isDrawBackground ()
    {
        return drawBackground;
    }

    public void setDrawBackground ( boolean drawBackground )
    {
        this.drawBackground = drawBackground;
    }

    public Color getBorderColor ()
    {
        return borderColor;
    }

    public void setBorderColor ( Color borderColor )
    {
        this.borderColor = borderColor;
    }

    public Color getDarkBorder ()
    {
        return darkBorder;
    }

    public void setDarkBorder ( Color darkBorder )
    {
        this.darkBorder = darkBorder;
    }

    @Override
    public void paint ( Graphics g, JComponent c )
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
