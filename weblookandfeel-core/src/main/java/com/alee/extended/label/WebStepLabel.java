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

package com.alee.extended.label;

import com.alee.laf.StyleConstants;
import com.alee.laf.label.WebLabel;
import com.alee.managers.language.LanguageMethods;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * User: mgarin Date: 15.08.11 Time: 13:49
 * <p/>
 * This class provides a quick access to step-styled label which can be used to visualize some process steps.
 */

public class WebStepLabel extends WebLabel implements ShapeProvider, LanguageMethods
{
    private Color topBgColor = StyleConstants.topBgColor;
    private Color bottomBgColor = StyleConstants.bottomBgColor;
    private Color selectedBgColor = StyleConstants.selectedBgColor;
    private Color borderColor = StyleConstants.darkBorderColor;
    private Color disabledBorderColor = StyleConstants.disabledBorderColor;

    private final Stroke stroke = new BasicStroke ( 2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f );

    private boolean selected = false;

    public WebStepLabel ()
    {
        super ();
        setupSettings ();
    }

    public WebStepLabel ( Icon image )
    {
        super ( image );
        setupSettings ();
    }

    public WebStepLabel ( Icon image, int horizontalAlignment )
    {
        super ( image, horizontalAlignment );
        setupSettings ();
    }

    public WebStepLabel ( String text )
    {
        super ( text );
        setupSettings ();
    }

    public WebStepLabel ( String text, int horizontalAlignment )
    {
        super ( text, horizontalAlignment );
        setupSettings ();
    }

    public WebStepLabel ( String text, Icon icon, int horizontalAlignment )
    {
        super ( text, icon, horizontalAlignment );
        setupSettings ();
    }

    protected void setupSettings ()
    {
        setOpaque ( false );
        setDrawShade ( true );
        setForeground ( Color.DARK_GRAY );
        setShadeColor ( Color.LIGHT_GRAY );
        setMargin ( 8 );
        setHorizontalAlignment ( WebStepLabel.CENTER );

        SwingUtils.setBoldFont ( this );
        SwingUtils.setFontSize ( this, 20 );
    }

    public Color getBorderColor ()
    {
        return borderColor;
    }

    public void setBorderColor ( Color borderColor )
    {
        this.borderColor = borderColor;
        this.repaint ();
    }

    public Color getDisabledBorderColor ()
    {
        return disabledBorderColor;
    }

    public void setDisabledBorderColor ( Color disabledBorderColor )
    {
        this.disabledBorderColor = disabledBorderColor;
        this.repaint ();
    }

    public boolean isSelected ()
    {
        return selected;
    }

    public void setSelected ( boolean selected )
    {
        this.selected = selected;
        this.repaint ();
    }

    public Shape provideShape ()
    {
        int width = getWidth ();
        int height = getHeight ();
        int length = Math.min ( width, height );
        return new RoundRectangle2D.Double ( width / 2 - length / 2 + 1, height / 2 - length / 2 + 1, length - 3, length - 3,
                getWidth () - 4, getHeight () - 4 );
    }

    protected void paintComponent ( Graphics g )
    {
        Graphics2D g2d = ( Graphics2D ) g;
        Object aa = LafUtils.setupAntialias ( g2d );

        int width = getWidth ();
        int height = getHeight ();
        int length = Math.min ( width, height );

        // Background
        if ( getBackground () != null )
        {
            if ( selected )
            {
                g2d.setPaint ( selectedBgColor );
            }
            else
            {
                g2d.setPaint ( new GradientPaint ( 0, 0, topBgColor, 0, getHeight (), bottomBgColor ) );
            }
            g2d.fillRoundRect ( width / 2 - length / 2 + 1, height / 2 - length / 2 + 1, length - 2, length - 2, getWidth () - 4,
                    getHeight () - 4 );
        }

        // Border
        if ( getBorderColor () != null )
        {
            g2d.setStroke ( stroke );
            g2d.setPaint ( isEnabled () ? borderColor : disabledBorderColor );
            g2d.drawRoundRect ( width / 2 - length / 2 + 1, height / 2 - length / 2 + 1, length - 3, length - 3, getWidth () - 4,
                    getHeight () - 4 );
        }

        LafUtils.restoreAntialias ( g2d, aa );

        super.paintComponent ( g );
    }

    public Dimension getPreferredSize ()
    {
        Dimension ps = super.getPreferredSize ();
        int max = Math.max ( ps.width, ps.height );
        ps.width = max;
        ps.height = max;
        return ps;
    }
}
