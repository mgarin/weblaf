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

package com.alee.extended.menu;

import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 * Standard dynamic menu item data.
 *
 * @author Mikle Garin
 */

public class WebDynamicMenuItem extends JComponent
{
    /**
     * Menu item icon.
     */
    protected ImageIcon icon;

    /**
     * Menu item text.
     */
    protected String text;

    /**
     * Menu item action.
     */
    protected ActionListener action;

    /**
     * Menu item margin.
     */
    protected Insets margin = new Insets ( 4, 4, 4, 8 );

    /**
     * Menu item text margin.
     */
    protected Insets textMargin = new Insets ( 3, 6, 3, 6 );

    /**
     * Whether should draw border around icon or not.
     */
    protected boolean paintBorder = true;

    /**
     * Border color.
     */
    protected Color borderColor = new Color ( 89, 122, 222 );

    /**
     * Border background color.
     */
    protected Color borderBackground = new Color ( 255, 255, 255, 220 );

    /**
     * Disabled border color.
     */
    protected Color disabledBorderColor = new Color ( 149, 151, 170 );

    /**
     * Disabled border background color.
     */
    protected Color disabledBorderBackground = new Color ( 255, 255, 255, 180 );

    /**
     * Rollover background color.
     */
    protected Color rolloverBackground = new Color ( 0, 0, 0, 150 );

    /**
     * Gap between icon and text.
     */
    protected int iconTextGap = 5;

    /**
     * Runtime variables.
     */
    private boolean rollover;

    public WebDynamicMenuItem ()
    {
        super ();
        initialize ();
    }

    public WebDynamicMenuItem ( final ImageIcon icon )
    {
        super ();
        this.icon = icon;
        initialize ();
    }

    public WebDynamicMenuItem ( final ImageIcon icon, final ActionListener action )
    {
        super ();
        this.icon = icon;
        this.action = action;
        initialize ();
    }

    private void initialize ()
    {
        setOpaque ( false );
        setBackground ( new Color ( 0, 0, 0, 200 ) );
        setForeground ( Color.WHITE );
        setFont ( SwingUtils.getDefaultLabelFont () );

        final MouseAdapter mouseAdapter = new MouseAdapter ()
        {
            @Override
            public void mouseEntered ( final MouseEvent e )
            {
                rollover = true;
                repaint ();
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                rollover = false;
                repaint ();
            }
        };
        addMouseListener ( mouseAdapter );
        addMouseMotionListener ( mouseAdapter );
    }

    public ImageIcon getIcon ()
    {
        return icon;
    }

    public void setIcon ( final ImageIcon icon )
    {
        this.icon = icon;
    }

    public String getText ()
    {
        return text;
    }

    public void setText ( final String text )
    {
        this.text = text;
    }

    public ActionListener getAction ()
    {
        return action;
    }

    public void setAction ( final ActionListener action )
    {
        this.action = action;
    }

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
    }

    public void setMargin ( final int margin )
    {
        this.margin = new Insets ( margin, margin, margin, margin );
    }

    public void setMargin ( final int top, final int left, final int bottom, final int right )
    {
        this.margin = new Insets ( top, left, bottom, right );
    }

    public boolean isPaintBorder ()
    {
        return paintBorder;
    }

    public void setPaintBorder ( final boolean paintBorder )
    {
        this.paintBorder = paintBorder;
    }

    public Color getBorderColor ()
    {
        return borderColor;
    }

    public void setBorderColor ( final Color color )
    {
        this.borderColor = color;
    }

    public Color getBorderBackground ()
    {
        return borderBackground;
    }

    public void setBorderBackground ( final Color color )
    {
        this.borderBackground = color;
    }

    public Color getDisabledBorderColor ()
    {
        return disabledBorderColor;
    }

    public void setDisabledBorderColor ( final Color color )
    {
        this.disabledBorderColor = color;
    }

    public Color getDisabledBorderBackground ()
    {
        return disabledBorderBackground;
    }

    public void setDisabledBorderBackground ( final Color color )
    {
        this.disabledBorderBackground = color;
    }

    public Color getRolloverBackground ()
    {
        return rolloverBackground;
    }

    public void setRolloverBackground ( final Color color )
    {
        this.rolloverBackground = color;
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        final Graphics2D g2d = ( Graphics2D ) g;
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        // final int w = getWidth ();
        final int h = getHeight ();
        final int iw = h - margin.top - margin.bottom;

        // Paint icon
        if ( icon != null )
        {
            // Paint icon border
            if ( isPaintBorder () )
            {
                final Area outer = new Area ( new Ellipse2D.Double ( 0, 0, h, h ) );
                final Ellipse2D.Double inner = new Ellipse2D.Double ( 2, 2, h - 4, h - 4 );
                outer.exclusiveOr ( new Area ( inner ) );

                g2d.setPaint ( isEnabled () ? getBorderColor () : getDisabledBorderColor () );
                g2d.fill ( outer );

                g2d.setPaint ( isEnabled () ? getBorderBackground () : getDisabledBorderBackground () );
                g2d.fill ( inner );
            }

            // Paint icon
            g2d.drawImage ( icon.getImage (), margin.left + iw / 2 - icon.getIconWidth () / 2,
                    margin.top + iw / 2 - icon.getIconHeight () / 2, null );
        }

        // Paint text
        if ( text != null )
        {
            g2d.setFont ( getFont () );

            final FontMetrics fm = g2d.getFontMetrics ();
            final int tw = fm.stringWidth ( text );
            final int th = fm.getHeight ();
            final int ts = LafUtils.getTextCenterShiftY ( fm );

            // Calculating full text rectangle
            final int tx = margin.left + iw + iconTextGap;
            final int ty = margin.top + iw / 2 - th / 2 - textMargin.top;
            final Rectangle tr = new Rectangle ( tx, ty, textMargin.left + tw + textMargin.right, textMargin.top + th + textMargin.bottom );

            // Paint text border
            g2d.setPaint ( rollover ? getRolloverBackground () : getBackground () );
            g2d.fillRoundRect ( tr.x, tr.y, tr.width, tr.height, /*tr.height, tr.height*/ 6, 6 );

            final int ath = tr.height - textMargin.top - textMargin.bottom;
            g2d.setPaint ( getForeground () );
            g2d.drawString ( text, tr.x + textMargin.left, tr.y + textMargin.top + ath / 2 + ts );
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        final Dimension size = new Dimension ();

        if ( icon != null )
        {
            size.width += icon.getIconWidth ();
            size.height = icon.getIconHeight ();
        }
        if ( icon != null && text != null )
        {
            size.width += iconTextGap;
        }
        if ( text != null )
        {
            final FontMetrics fm = getFontMetrics ( getFont () );
            final int tw = fm.stringWidth ( text );
            final int th = fm.getHeight ();
            size.width += textMargin.left + tw + textMargin.right;
            size.height = Math.max ( size.height, th );
        }

        size.width += margin.left + margin.right;
        size.height += margin.top + margin.bottom;

        return size;
    }
}