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

package com.alee.laf.radiobutton;

import com.alee.laf.StyleConstants;
import com.alee.laf.list.WebListElement;
import com.alee.laf.tree.WebTreeElement;
import com.alee.utils.ColorUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: mgarin Date: 28.04.11 Time: 14:55
 */

public class WebRadioButtonUI extends BasicRadioButtonUI implements ShapeProvider
{
    public static final int MAX_DARKNESS = 5;

    public static List<ImageIcon> CHECK_STATES = new ArrayList<ImageIcon> ();
    public static ImageIcon DISABLED_CHECK = null;

    static
    {
        CHECK_STATES.add ( StyleConstants.EMPTY_ICON );
        for ( int i = 1; i <= 3; i++ )
        {
            CHECK_STATES.add ( new ImageIcon ( WebRadioButtonUI.class.getResource ( "icons/c" + i + ".png" ) ) );
        }

        DISABLED_CHECK = ImageUtils.getDisabledCopy ( "WebRadioButton.disabled.check", CHECK_STATES.get ( CHECK_STATES.size () - 1 ) );
    }

    private Color borderColor = WebRadioButtonStyle.borderColor;
    private Color darkBorderColor = WebRadioButtonStyle.darkBorderColor;
    private Color disabledBorderColor = WebRadioButtonStyle.disabledBorderColor;

    private Color topBgColor = WebRadioButtonStyle.topBgColor;
    private Color bottomBgColor = WebRadioButtonStyle.bottomBgColor;
    private Color topSelectedBgColor = WebRadioButtonStyle.topSelectedBgColor;
    private Color bottomSelectedBgColor = WebRadioButtonStyle.bottomSelectedBgColor;

    private int shadeWidth = WebRadioButtonStyle.shadeWidth;
    private Insets margin = WebRadioButtonStyle.margin;

    private boolean animated = WebRadioButtonStyle.animated;
    private boolean rolloverDarkBorderOnly = WebRadioButtonStyle.rolloverDarkBorderOnly;

    public Stroke borderStroke = new BasicStroke ( 1.5f );

    private int iconWidth = 16;
    private int iconHeight = 16;

    private int bgDarkness = 0;
    private boolean rollover;
    private WebTimer bgTimer;

    private int checkIcon;
    private boolean checking;
    private WebTimer checkTimer;

    private Rectangle iconRect;

    private JRadioButton radioButton;

    private MouseAdapter mouseAdapter;
    private ItemListener itemListener;

    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebRadioButtonUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        radioButton = ( JRadioButton ) c;

        // Default settings
        SwingUtils.setOrientation ( c );
        radioButton.setOpaque ( false );

        // Initial check state
        checkIcon = radioButton.isSelected () ? CHECK_STATES.size () - 1 : 0;

        // Updating border and icon
        updateBorder ();
        updateIcon ( radioButton );

        // Background fade animation
        bgTimer = new WebTimer ( "WebRadioButtonUI.bgUpdater", 40, new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( rollover && bgDarkness < MAX_DARKNESS )
                {
                    bgDarkness++;
                    c.repaint ();
                }
                else if ( !rollover && bgDarkness > 0 )
                {
                    bgDarkness--;
                    c.repaint ();
                }
                else
                {
                    bgTimer.stop ();
                }
            }
        } );
        mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseEntered ( MouseEvent e )
            {
                rollover = true;
                if ( isAnimated () )
                {
                    bgTimer.start ();
                }
                else
                {
                    bgDarkness = MAX_DARKNESS;
                    c.repaint ();
                }
            }

            @Override
            public void mouseExited ( MouseEvent e )
            {
                rollover = false;
                if ( isAnimated () )
                {
                    bgTimer.start ();
                }
                else
                {
                    bgDarkness = 0;
                    c.repaint ();
                }
            }
        };
        radioButton.addMouseListener ( mouseAdapter );

        // Selection changes animation
        checkTimer = new WebTimer ( "WebRadioButtonUI.iconUpdater", 40, new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                if ( checking && checkIcon < CHECK_STATES.size () - 1 )
                {
                    checkIcon++;
                    c.repaint ();
                }
                else if ( !checking && checkIcon > 0 )
                {
                    checkIcon--;
                    c.repaint ();
                }
                else
                {
                    checkTimer.stop ();
                }
            }
        } );
        itemListener = new ItemListener ()
        {
            @Override
            public void itemStateChanged ( ItemEvent e )
            {
                if ( animated )
                {
                    if ( radioButton.isSelected () )
                    {
                        checking = true;
                        checkTimer.start ();
                    }
                    else
                    {
                        checking = false;
                        checkTimer.start ();
                    }
                }
                else
                {
                    checkTimer.stop ();
                    checkIcon = radioButton.isSelected () ? CHECK_STATES.size () - 1 : 0;
                    c.repaint ();
                }
            }
        };
        radioButton.addItemListener ( itemListener );
    }

    @Override
    public void uninstallUI ( JComponent c )
    {
        radioButton.removeMouseListener ( mouseAdapter );
        radioButton.removeItemListener ( itemListener );

        radioButton.setIcon ( null );
        radioButton = null;

        super.uninstallUI ( c );
    }

    @Override
    public Shape provideShape ()
    {
        return LafUtils.getWebBorderShape ( radioButton, getShadeWidth (), getRound () );
    }

    private void updateBorder ()
    {
        // Actual margin
        boolean ltr = radioButton.getComponentOrientation ().isLeftToRight ();
        Insets m = new Insets ( margin.top, ltr ? margin.left : margin.right, margin.bottom, ltr ? margin.right : margin.left );

        // Installing border
        radioButton.setBorder ( LafUtils.createWebBorder ( m ) );
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

    public boolean isAnimated ()
    {
        return animated && ( radioButton == null || radioButton.getParent () == null ||
                !( radioButton.getParent () instanceof WebListElement || radioButton.getParent () instanceof WebTreeElement ) );
    }

    public void setAnimated ( boolean animated )
    {
        this.animated = animated;
    }

    public boolean isRolloverDarkBorderOnly ()
    {
        return rolloverDarkBorderOnly;
    }

    public void setRolloverDarkBorderOnly ( boolean rolloverDarkBorderOnly )
    {
        this.rolloverDarkBorderOnly = rolloverDarkBorderOnly;
    }

    public Color getBorderColor ()
    {
        return borderColor;
    }

    public void setBorderColor ( Color borderColor )
    {
        this.borderColor = borderColor;
    }

    public Color getDarkBorderColor ()
    {
        return darkBorderColor;
    }

    public void setDarkBorderColor ( Color darkBorderColor )
    {
        this.darkBorderColor = darkBorderColor;
    }

    public Color getDisabledBorderColor ()
    {
        return disabledBorderColor;
    }

    public void setDisabledBorderColor ( Color disabledBorderColor )
    {
        this.disabledBorderColor = disabledBorderColor;
    }

    public Color getTopBgColor ()
    {
        return topBgColor;
    }

    public void setTopBgColor ( Color topBgColor )
    {
        this.topBgColor = topBgColor;
    }

    public Color getBottomBgColor ()
    {
        return bottomBgColor;
    }

    public void setBottomBgColor ( Color bottomBgColor )
    {
        this.bottomBgColor = bottomBgColor;
    }

    public Color getTopSelectedBgColor ()
    {
        return topSelectedBgColor;
    }

    public void setTopSelectedBgColor ( Color topSelectedBgColor )
    {
        this.topSelectedBgColor = topSelectedBgColor;
    }

    public Color getBottomSelectedBgColor ()
    {
        return bottomSelectedBgColor;
    }

    public void setBottomSelectedBgColor ( Color bottomSelectedBgColor )
    {
        this.bottomSelectedBgColor = bottomSelectedBgColor;
    }

    public int getRound ()
    {
        return 6;
    }

    public int getShadeWidth ()
    {
        return shadeWidth;
    }

    public void setShadeWidth ( int shadeWidth )
    {
        this.shadeWidth = shadeWidth;
    }

    public int getIconWidth ()
    {
        return iconWidth;
    }

    public void setIconWidth ( int iconWidth )
    {
        this.iconWidth = iconWidth;
    }

    public int getIconHeight ()
    {
        return iconHeight;
    }

    public void setIconHeight ( int iconHeight )
    {
        this.iconHeight = iconHeight;
    }

    private void updateIcon ( final JRadioButton radioButton )
    {
        radioButton.setIcon ( new Icon ()
        {
            @Override
            public void paintIcon ( Component c, Graphics g, int x, int y )
            {
                iconRect = new Rectangle ( x, y, iconWidth, iconHeight );

                Graphics2D g2d = ( Graphics2D ) g;
                Object aa = LafUtils.setupAntialias ( g2d );

                // Button size and shape
                int round = iconWidth - shadeWidth * 2 - 2;
                Rectangle iconRect =
                        new Rectangle ( x + shadeWidth, y + shadeWidth, iconWidth - shadeWidth * 2 - 1, iconHeight - shadeWidth * 2 - 1 );
                RoundRectangle2D shape =
                        new RoundRectangle2D.Double ( iconRect.x, iconRect.y, iconRect.width, iconRect.height, round, round );

                // Shade
                if ( c.isEnabled () )
                {
                    LafUtils.drawShade ( g2d, shape,
                            c.isEnabled () && c.isFocusOwner () ? StyleConstants.fieldFocusColor : StyleConstants.shadeColor, shadeWidth );
                }

                // Background
                int radius = Math.round ( ( float ) Math.sqrt ( iconRect.width * iconRect.width / 2 ) );
                g2d.setPaint ( new RadialGradientPaint ( iconRect.x + iconRect.width / 2, iconRect.y + iconRect.height / 2, radius,
                        new float[]{ 0f, 1f }, getBgColors ( radioButton ) ) );
                g2d.fill ( shape );

                // Border
                Stroke os = LafUtils.setupStroke ( g2d, borderStroke );
                g2d.setPaint ( c.isEnabled () ?
                        ( rolloverDarkBorderOnly ? ColorUtils.getIntermediateColor ( borderColor, darkBorderColor, getProgress () ) :
                                darkBorderColor ) : disabledBorderColor );
                g2d.draw ( shape );
                LafUtils.restoreStroke ( g2d, os );

                // Check icon
                if ( checkIcon > 0 )
                {
                    ImageIcon icon = radioButton.isEnabled () ? CHECK_STATES.get ( checkIcon ) : DISABLED_CHECK;
                    g2d.drawImage ( icon.getImage (), x + iconWidth / 2 - icon.getIconWidth () / 2,
                            y + iconHeight / 2 - icon.getIconHeight () / 2, radioButton );
                }

                LafUtils.restoreAntialias ( g2d, aa );
            }

            @Override
            public int getIconWidth ()
            {
                return iconWidth;
            }

            @Override
            public int getIconHeight ()
            {
                return iconHeight;
            }
        } );
    }

    private Color[] getBgColors ( JRadioButton radioButton )
    {
        if ( radioButton.isEnabled () )
        {
            float progress = getProgress ();
            if ( progress < 1f )
            {
                return new Color[]{ ColorUtils.getIntermediateColor ( topBgColor, topSelectedBgColor, progress ),
                        ColorUtils.getIntermediateColor ( bottomBgColor, bottomSelectedBgColor, progress ) };
            }
            else
            {
                return new Color[]{ topSelectedBgColor, bottomSelectedBgColor };
            }
        }
        else
        {
            return new Color[]{ topBgColor, bottomBgColor };
        }
    }

    private float getProgress ()
    {
        return ( float ) bgDarkness / MAX_DARKNESS;
    }

    public Rectangle getIconRect ()
    {
        return iconRect != null ? new Rectangle ( iconRect ) : new Rectangle ();
    }

    @Override
    public synchronized void paint ( Graphics g, JComponent c )
    {
        Map hints = SwingUtils.setupTextAntialias ( g );
        super.paint ( g, c );
        SwingUtils.restoreTextAntialias ( g, hints );
    }

    @Override
    protected void paintText ( Graphics g, JComponent c, Rectangle textRect, String text )
    {
        AbstractButton b = ( AbstractButton ) c;
        ButtonModel model = b.getModel ();
        FontMetrics fm = SwingUtils.getFontMetrics ( c, g );
        int mnemonicIndex = b.getDisplayedMnemonicIndex ();

        // Drawing text
        if ( model.isEnabled () )
        {
            // Normal text
            g.setColor ( b.getForeground () );
            SwingUtils.drawStringUnderlineCharAt ( g, text, mnemonicIndex, textRect.x + getTextShiftOffset (),
                    textRect.y + fm.getAscent () + getTextShiftOffset () );
        }
        else
        {
            // Disabled text
            g.setColor ( b.getBackground ().brighter () );
            SwingUtils.drawStringUnderlineCharAt ( g, text, mnemonicIndex, textRect.x, textRect.y + fm.getAscent () );
            g.setColor ( b.getBackground ().darker () );
            SwingUtils.drawStringUnderlineCharAt ( g, text, mnemonicIndex, textRect.x - 1, textRect.y + fm.getAscent () - 1 );
        }
    }
}
