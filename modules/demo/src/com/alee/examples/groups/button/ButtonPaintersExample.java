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
import com.alee.extended.painter.AbstractPainter;
import com.alee.extended.painter.Painter;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebToggleButton;
import com.alee.utils.GraphicsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Button painters example.
 *
 * @author Mikle Garin
 */

public class ButtonPaintersExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Button painter";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Custom Sea-glass button painter";
    }

    /**
     * {@inheritDoc}
     */
    @Override
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

    /**
     * Custom SeaGlass button painter.
     */
    public static class SeaGlassButtonPainter extends AbstractPainter<AbstractButton>
    {
        /**
         * Border colors.
         */
        protected Color topBorder = new Color ( 140, 176, 220 );
        protected Color bottomBorder = new Color ( 94, 138, 188 );
        protected Color topPressedBorder = new Color ( 83, 129, 186 );
        protected Color bottomPressedBorder = new Color ( 69, 124, 186 );
        protected Color topDisabledBorder = new Color ( 190, 207, 230 );
        protected Color bottomDisabledBorder = new Color ( 171, 192, 217 );
        protected float[] borderFractions = { 0f, 1f };

        /**
         * Background colors.
         */
        protected Color sideBg = Color.WHITE;
        protected Color middleBg = new Color ( 216, 233, 247 );
        protected Color sidePressedBg = new Color ( 174, 190, 206 );
        protected Color middlePressedBg = new Color ( 112, 146, 180 );
        protected Color sideSelectedBg = new Color ( 190, 206, 221 );
        protected Color middleSelectedBg = new Color ( 135, 174, 207 );
        protected Color sideDisabledBg = Color.WHITE;
        protected Color middleDisabledBg = new Color ( 242, 248, 252 );
        protected float[] bgFractions = { 0f, 0.55f, 1f };

        /**
         * Focus color.
         */
        protected Color focus = new Color ( 122, 166, 205, 200 );

        /**
         * Constructs new SeaGlass button painter.
         */
        public SeaGlassButtonPainter ()
        {
            super ();
            margin = new Insets ( 5, 5, 5, 5 );
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void paint ( final Graphics2D g2d, final Rectangle bounds, final AbstractButton c )
        {
            final ButtonModel buttonModel = c.getModel ();

            // Focus
            if ( c.isFocusOwner () )
            {
                GraphicsUtils
                        .drawShade ( g2d, new RoundRectangle2D.Double ( 2, 2, c.getWidth () - 5, c.getHeight () - 5, 6, 6 ), focus, 2 );
            }

            // Background
            g2d.setPaint ( getBackgroundPaint ( c, buttonModel ) );
            g2d.fillRoundRect ( 2, 2, c.getWidth () - 4, c.getHeight () - 4, 7, 7 );

            // Border
            g2d.setPaint ( getBorderPaint ( c, buttonModel ) );
            g2d.drawRoundRect ( 2, 2, c.getWidth () - 5, c.getHeight () - 5, 6, 6 );
        }

        /**
         * Returns background paint.
         *
         * @param button button
         * @param model  button model
         * @return background paint
         */
        protected Paint getBackgroundPaint ( final AbstractButton button, final ButtonModel model )
        {
            final Color[] colors;
            if ( !button.isEnabled () )
            {
                colors = new Color[]{ sideDisabledBg, middleDisabledBg, sideDisabledBg };
            }
            else if ( model.isPressed () )
            {
                colors = new Color[]{ sidePressedBg, middlePressedBg, sidePressedBg };
            }
            else if ( model.isSelected () )
            {
                colors = new Color[]{ sideSelectedBg, middleSelectedBg, sideSelectedBg };
            }
            else
            {
                colors = new Color[]{ sideBg, middleBg, sideBg };
            }
            return new LinearGradientPaint ( 0, 2, 0, button.getHeight () - 2, bgFractions, colors );
        }

        /**
         * Returns border paint.
         *
         * @param button button
         * @param model  button model
         * @return border paint
         */
        protected Paint getBorderPaint ( final AbstractButton button, final ButtonModel model )
        {
            final Color[] colors;
            if ( !button.isEnabled () )
            {
                colors = new Color[]{ topDisabledBorder, bottomDisabledBorder };
            }
            else if ( model.isPressed () || model.isSelected () )
            {
                colors = new Color[]{ topPressedBorder, bottomPressedBorder };
            }
            else
            {
                colors = new Color[]{ topBorder, bottomBorder };
            }
            return new LinearGradientPaint ( 0, 2, 0, button.getHeight () - 2, borderFractions, colors );
        }
    }
}