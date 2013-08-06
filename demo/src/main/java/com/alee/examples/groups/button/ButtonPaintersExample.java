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

package com.alee.examples.groups.button;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.label.WebLinkLabel;
import com.alee.extended.painter.DefaultPainter;
import com.alee.extended.painter.Painter;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.utils.LafUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * User: mgarin Date: 06.06.12 Time: 17:59
 */

public class ButtonPaintersExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Button painter";
    }

    public String getDescription ()
    {
        return "Custom Sea-glass button painter";
    }

    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Custom button painter
        // This painter only changes view on button focus and disabled state changes
        Painter painter = new SeaGlassButtonPainter ();

        // Simple button to demostrate painter in action
        WebButton button1 = new WebButton ( "Simple" );
        button1.setPainter ( painter );
        button1.setMoveIconOnPress ( false );
        button1.setLeftRightSpacing ( 10 );

        // Iconed button to demostrate painter in action
        WebButton button2 = new WebButton ( "Iconed", loadIcon ( "icon.png" ) );
        button2.setPainter ( painter );
        button2.setMoveIconOnPress ( false );

        // Iconed toggle button to demostrate painter in action
        WebToggleButton button3 = new WebToggleButton ( "Toggle", loadIcon ( "icon.png" ) );
        button3.setPainter ( painter );
        button3.setMoveIconOnPress ( false );

        // Sea glass L&F link
        WebLinkLabel seaGlassLink = new WebLinkLabel ();
        seaGlassLink.setLink ( "See the original Sea Glass L&F here", "https://code.google.com/p/seaglass/" );
        seaGlassLink.setMargin ( 4 );

        return new GroupPanel ( false, new GroupPanel ( 4, button1, button2, button3 ), seaGlassLink );
    }

    public static class SeaGlassButtonPainter extends DefaultPainter<AbstractButton>
    {
        // Border colors
        private Color topBorder = new Color ( 140, 176, 220 );
        private Color bottomBorder = new Color ( 94, 138, 188 );
        private Color topPressedBorder = new Color ( 83, 129, 186 );
        private Color bottomPressedBorder = new Color ( 69, 124, 186 );
        private Color topDisabledBorder = new Color ( 190, 207, 230 );
        private Color bottomDisabledBorder = new Color ( 171, 192, 217 );
        private float[] borderFractions = { 0f, 1f };

        // Background colors
        private Color sideBg = Color.WHITE;
        private Color middleBg = new Color ( 216, 233, 247 );
        private Color sidePressedBg = new Color ( 174, 190, 206 );
        private Color middlePressedBg = new Color ( 112, 146, 180 );
        private Color sideSelectedBg = new Color ( 190, 206, 221 );
        private Color middleSelectedBg = new Color ( 135, 174, 207 );
        private Color sideDisabledBg = Color.WHITE;
        private Color middleDisabledBg = new Color ( 242, 248, 252 );
        private float[] bgFractions = { 0f, 0.55f, 1f };

        // Focus color
        private Color focus = new Color ( 122, 166, 205, 200 );

        // Margin
        private Insets margin = new Insets ( 5, 5, 5, 5 );

        public SeaGlassButtonPainter ()
        {
            super ();
        }

        public Insets getMargin ( AbstractButton c )
        {
            return margin;
        }

        public void paint ( Graphics2D g2d, Rectangle bounds, AbstractButton c )
        {
            ButtonModel buttonModel = c.getModel ();

            // Focus
            if ( c.isFocusOwner () )
            {
                LafUtils.drawShade ( g2d, new RoundRectangle2D.Double ( 2, 2, c.getWidth () - 5, c.getHeight () - 5, 6, 6 ), focus, 2 );
            }

            // Background
            g2d.setPaint ( getBackgroundPaint ( c, buttonModel ) );
            g2d.fillRoundRect ( 2, 2, c.getWidth () - 4, c.getHeight () - 4, 7, 7 );

            // Border
            g2d.setPaint ( getBorderPaint ( c, buttonModel ) );
            g2d.drawRoundRect ( 2, 2, c.getWidth () - 5, c.getHeight () - 5, 6, 6 );
        }

        private Paint getBackgroundPaint ( AbstractButton c, ButtonModel buttonModel )
        {
            Color[] colors;
            if ( !c.isEnabled () )
            {
                colors = new Color[]{ sideDisabledBg, middleDisabledBg, sideDisabledBg };
            }
            else if ( buttonModel.isPressed () )
            {
                colors = new Color[]{ sidePressedBg, middlePressedBg, sidePressedBg };
            }
            else if ( buttonModel.isSelected () )
            {
                colors = new Color[]{ sideSelectedBg, middleSelectedBg, sideSelectedBg };
            }
            else
            {
                colors = new Color[]{ sideBg, middleBg, sideBg };
            }
            return new LinearGradientPaint ( 0, 2, 0, c.getHeight () - 2, bgFractions, colors );
        }

        private Paint getBorderPaint ( AbstractButton c, ButtonModel buttonModel )
        {
            Color[] colors;
            if ( !c.isEnabled () )
            {
                colors = new Color[]{ topDisabledBorder, bottomDisabledBorder };
            }
            else if ( buttonModel.isPressed () || buttonModel.isSelected () )
            {
                colors = new Color[]{ topPressedBorder, bottomPressedBorder };
            }
            else
            {
                colors = new Color[]{ topBorder, bottomBorder };
            }
            return new LinearGradientPaint ( 0, 2, 0, c.getHeight () - 2, borderFractions, colors );
        }
    }
}