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

package com.alee.laf.separator;

import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;
import java.awt.*;

/**
 * Custom UI for JSeparator component.
 *
 * @author Mikle Garin
 */

public class WebSeparatorUI extends BasicSeparatorUI
{
    private static final float[] fractions = new float[]{ 0.0f, 0.5f, 1f };

    private Color separatorLightUpperColor = WebSeparatorStyle.separatorLightUpperColor;
    private Color separatorLightColor = WebSeparatorStyle.separatorLightColor;
    private Color separatorUpperColor = WebSeparatorStyle.separatorUpperColor;
    private Color separatorColor = WebSeparatorStyle.separatorColor;

    private boolean reversedColors = WebSeparatorStyle.reversedColors;
    private boolean drawLeadingLine = WebSeparatorStyle.drawLeadingLine;
    private boolean drawTrailingLine = WebSeparatorStyle.drawTrailingLine;

    private Insets margin = WebSeparatorStyle.margin;

    private JSeparator separator;

    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebSeparatorUI ();
    }

    @Override
    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        this.separator = ( JSeparator ) c;

        // Default settings
        SwingUtils.setOrientation ( separator );
        separator.setOpaque ( false );

        // Updating border
        updateBorder ();
    }

    @Override
    public void uninstallUI ( JComponent c )
    {
        separator.setOpaque ( true );

        this.separator = null;

        super.uninstallUI ( c );
    }

    private void updateBorder ()
    {
        if ( separator != null )
        {
            // Actual margin
            boolean ltr = separator.getComponentOrientation ().isLeftToRight ();
            Insets m = new Insets ( margin.top, ltr ? margin.left : margin.right, margin.bottom, ltr ? margin.right : margin.left );

            // Installing border
            separator.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    public Color getSeparatorColor ()
    {
        return separatorColor;
    }

    public void setSeparatorColor ( Color separatorColor )
    {
        this.separatorColor = separatorColor;
    }

    public Color getSeparatorUpperColor ()
    {
        return separatorUpperColor;
    }

    public void setSeparatorUpperColor ( Color separatorUpperColor )
    {
        this.separatorUpperColor = separatorUpperColor;
    }

    public Color getSeparatorLightColor ()
    {
        return separatorLightColor;
    }

    public void setSeparatorLightColor ( Color separatorLightColor )
    {
        this.separatorLightColor = separatorLightColor;
    }

    public Color getSeparatorLightUpperColor ()
    {
        return separatorLightUpperColor;
    }

    public void setSeparatorLightUpperColor ( Color separatorLightUpperColor )
    {
        this.separatorLightUpperColor = separatorLightUpperColor;
    }

    public boolean isReversedColors ()
    {
        return reversedColors;
    }

    public void setReversedColors ( boolean reversedColors )
    {
        this.reversedColors = reversedColors;
    }

    public boolean isDrawLeadingLine ()
    {
        return drawLeadingLine;
    }

    public void setDrawLeadingLine ( boolean drawLeadingLine )
    {
        this.drawLeadingLine = drawLeadingLine;
    }

    public boolean isDrawTrailingLine ()
    {
        return drawTrailingLine;
    }

    public void setDrawTrailingLine ( boolean drawTrailingLine )
    {
        this.drawTrailingLine = drawTrailingLine;
    }

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( Insets margin )
    {
        this.margin = margin;
        updateBorder ();
    }

    @Override
    public void paint ( Graphics g, JComponent c )
    {
        Graphics2D g2d = ( Graphics2D ) g;
        LafUtils.setupAntialias ( g2d );

        JSeparator separator = ( JSeparator ) c;

        Insets insets = c.getInsets ();
        int x = insets.left;
        int y = insets.top;
        int width = c.getWidth () - insets.left - insets.right;
        int height = c.getHeight () - insets.top - insets.bottom;

        boolean drawSideLines = drawLeadingLine || drawTrailingLine;
        if ( separator.getOrientation () == WebSeparator.VERTICAL )
        {
            if ( height > 0 )
            {
                if ( drawSideLines )
                {
                    g2d.setPaint ( new LinearGradientPaint ( x, y, x, y + height, fractions, getLightColors () ) );
                    if ( drawLeadingLine )
                    {
                        g2d.drawLine ( x, y, x, y + height );
                    }
                    if ( drawTrailingLine )
                    {
                        g2d.drawLine ( x + ( drawLeadingLine ? 2 : 1 ), y, x + ( drawLeadingLine ? 2 : 1 ), y + height );
                    }
                }

                g2d.setPaint ( new LinearGradientPaint ( x, y, x, y + height, fractions, getDarkColors () ) );
                g2d.drawLine ( x + ( drawLeadingLine ? 1 : 0 ), y, x + ( drawLeadingLine ? 1 : 0 ), y + height );
            }
        }
        else
        {
            if ( width > 0 )
            {
                if ( drawSideLines )
                {
                    g2d.setPaint ( new LinearGradientPaint ( x, y, x + width, y, fractions, getLightColors () ) );
                    if ( drawLeadingLine )
                    {
                        g2d.drawLine ( x, y, x + width, y );
                    }
                    if ( drawTrailingLine )
                    {
                        g2d.drawLine ( x, y + ( drawLeadingLine ? 2 : 1 ), x + width, y + ( drawLeadingLine ? 2 : 1 ) );
                    }
                }

                g2d.setPaint ( new LinearGradientPaint ( x, y, x + width, y, fractions, getDarkColors () ) );
                g2d.drawLine ( x, y + ( drawLeadingLine ? 1 : 0 ), x + width, y + ( drawLeadingLine ? 1 : 0 ) );
            }
        }
    }

    private Color[] getLightColors ()
    {
        return reversedColors ? new Color[]{ separatorUpperColor, separatorColor, separatorUpperColor } :
                new Color[]{ separatorLightUpperColor, Color.WHITE, separatorLightUpperColor };
    }

    private Color[] getDarkColors ()
    {
        return reversedColors ? new Color[]{ separatorLightUpperColor, Color.WHITE, separatorLightUpperColor } :
                new Color[]{ separatorUpperColor, separatorColor, separatorUpperColor };
    }

    @Override
    public Dimension getPreferredSize ( JComponent c )
    {
        Insets insets = c.getInsets ();
        int bonus = 1 + ( drawLeadingLine ? 1 : 0 ) + ( drawTrailingLine ? 1 : 0 );
        if ( ( ( JSeparator ) c ).getOrientation () == WebSeparator.VERTICAL )
        {
            return new Dimension ( insets.left + bonus + insets.right, insets.top + insets.bottom );
        }
        else
        {
            return new Dimension ( insets.left + insets.right, insets.top + bonus + insets.bottom );
        }
    }
}