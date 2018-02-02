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

package com.alee.painter.common;

import com.alee.managers.style.Bounds;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;

/**
 * Titled border painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 * @see com.alee.painter.common.BorderPainter
 * @see com.alee.painter.AbstractPainter
 * @see com.alee.painter.Painter
 */

public class TitledBorderPainter<C extends JComponent, U extends ComponentUI> extends BorderPainter<C, U> implements SwingConstants
{
    /**
     * todo 1. Left/Right title position
     * todo 2. Icon for title text
     * todo 3. Take title position into account when calculating preferred size &amp; border
     */

    /**
     * Title side offset.
     */
    protected int titleOffset = 4;

    /**
     * Gap between title and border line.
     */
    protected int titleBorderGap = 3;

    /**
     * Title position relative to border.
     */
    protected TitlePosition titlePosition = TitlePosition.insideLine;

    /**
     * Title foreground.
     */
    protected Color foreground = null;

    /**
     * Painter background.
     */
    protected Color background = null;

    /**
     * Whether to clip background under the title or not.
     */
    protected boolean clipTitleBackground = true;

    /**
     * Title text alignment.
     */
    protected int titleAlignment;

    /**
     * Title text display side.
     */
    protected int titleSide;

    /**
     * Title text.
     */
    protected String titleText;

    /**
     * Runtime variables.
     */
    protected transient int w;
    protected transient int h;
    protected transient int sw;
    protected transient boolean emptyTitle;
    protected transient FontMetrics fontMetrics;
    protected transient int titleAreaHeight;
    protected transient int titleWidth;
    protected transient int titleX;
    protected transient int titleY;
    protected transient double borderCenter;
    protected transient double borderPosition;
    protected transient Shape borderShape;
    protected transient boolean doClip;

    public TitledBorderPainter ()
    {
        this ( null );
    }

    public TitledBorderPainter ( final String titleText )
    {
        this ( titleText, LEADING );
    }

    public TitledBorderPainter ( final String titleText, final int titleAlignment )
    {
        this ( titleText, titleAlignment, TOP );
    }

    public TitledBorderPainter ( final String titleText, final int titleAlignment, final int titleSide )
    {
        super ();
        this.titleText = titleText;
        this.titleAlignment = titleAlignment;
        this.titleSide = titleSide;
    }

    public int getTitleOffset ()
    {
        return titleOffset;
    }

    public void setTitleOffset ( final int titleOffset )
    {
        this.titleOffset = titleOffset;
        updateAll ();
    }

    public int getTitleBorderGap ()
    {
        return titleBorderGap;
    }

    public void setTitleBorderGap ( final int titleBorderGap )
    {
        this.titleBorderGap = titleBorderGap;
        updateAll ();
    }

    public int getTitleSide ()
    {
        return titleSide;
    }

    public void setTitleSide ( final int titleSide )
    {
        this.titleSide = titleSide;
        updateAll ();
    }

    public int getTitleAlignment ()
    {
        return titleAlignment;
    }

    public void setTitleAlignment ( final int titleAlignment )
    {
        this.titleAlignment = titleAlignment;
        repaint ();
    }

    public TitlePosition getTitlePosition ()
    {
        return titlePosition;
    }

    public void setTitlePosition ( final TitlePosition titlePosition )
    {
        this.titlePosition = titlePosition;
        repaint ();
    }

    public Color getForeground ()
    {
        return foreground;
    }

    public void setForeground ( final Color foreground )
    {
        this.foreground = foreground;
        repaint ();
    }

    public Color getBackground ()
    {
        return background;
    }

    public void setBackground ( final Color background )
    {
        this.background = background;
        repaint ();
    }

    public boolean isClipTitleBackground ()
    {
        return clipTitleBackground;
    }

    public void setClipTitleBackground ( final boolean clipTitleBackground )
    {
        this.clipTitleBackground = clipTitleBackground;
        repaint ();
    }

    public String getTitleText ()
    {
        return titleText;
    }

    public void setTitleText ( final String titleText )
    {
        this.titleText = titleText;
        updateAll ();
    }

    @Override
    protected Insets getBorder ()
    {
        final Insets m = super.getBorder ();
        if ( !isEmptyTitle () )
        {
            switch ( titleSide )
            {
                case TOP:
                {
                    m.top += getTitleAreaHeight ( component );
                    break;
                }
                case LEFT:
                {
                    m.left += getTitleAreaHeight ( component );
                    break;
                }
                case BOTTOM:
                {
                    m.bottom += getTitleAreaHeight ( component );
                    break;
                }
                case RIGHT:
                {
                    m.right += getTitleAreaHeight ( component );
                    break;
                }
            }
        }
        return m;
    }

    @Override
    public void paint ( final Graphics2D g2d, final C c, final U ui, final Bounds bounds )
    {
        // Initializing values
        w = c.getWidth ();
        h = c.getHeight ();
        sw = getStrokeWidth ();
        emptyTitle = isEmptyTitle ();
        fontMetrics = emptyTitle ? null : c.getFontMetrics ( c.getFont () );
        titleWidth = emptyTitle ? 0 : fontMetrics.stringWidth ( titleText );
        titleAreaHeight = getTitleAreaHeight ( c );
        titleX = getTitleX ();
        titleY = getTitleY ();
        borderCenter = ( double ) sw / 2;
        borderPosition = getBorderPosition ();
        borderShape = getBorderShape ();
        doClip = clipTitleBackground && !emptyTitle && titlePosition.equals ( TitlePosition.insideLine );

        // Drawing border and background

        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        final Stroke os = GraphicsUtils.setupStroke ( g2d, stroke, stroke != null );

        // Drawing background when title is not on the border line
        if ( background != null && !doClip )
        {
            g2d.setPaint ( background );
            g2d.fill ( borderShape );
        }

        // Properly clipping border line for text space
        final Shape clipShape = doClip ? getBorderClipShape () : null;
        final Shape oldClip = GraphicsUtils.subtractClip ( g2d, clipShape, doClip );

        // Drawing clipped by text background
        if ( background != null && doClip )
        {
            g2d.setPaint ( background );
            g2d.fill ( borderShape );
        }

        // Drawing clipped border
        g2d.setPaint ( color );
        g2d.draw ( borderShape );

        GraphicsUtils.restoreClip ( g2d, oldClip, doClip );
        GraphicsUtils.restoreStroke ( g2d, os, stroke != null );
        GraphicsUtils.restoreAntialias ( g2d, aa );

        // Drawing text
        if ( !emptyTitle )
        {
            g2d.setPaint ( foreground != null ? foreground : c.getForeground () );
            switch ( titleSide )
            {
                case TOP:
                case BOTTOM:
                {
                    final Map hints = SwingUtils.setupTextAntialias ( g2d );
                    g2d.drawString ( titleText, titleX, titleY );
                    SwingUtils.restoreTextAntialias ( g2d, hints );
                    break;
                }
                case LEFT:
                case RIGHT:
                {
                    // todo
                    break;
                }
            }
        }
    }

    protected int getTitleX ()
    {
        if ( titleAlignment == LEFT || titleAlignment == LEADING && ltr ||
                titleAlignment == TRAILING && !ltr )
        {
            return Math.max ( sw, getRound () ) + titleOffset + titleBorderGap;
        }
        else if ( titleAlignment == RIGHT || titleAlignment == TRAILING && ltr ||
                titleAlignment == LEADING && !ltr )
        {
            return w - Math.max ( sw, getRound () ) - titleOffset - titleBorderGap -
                    titleWidth;
        }
        else
        {
            return w / 2 - titleWidth / 2;
        }
    }

    protected int getTitleY ()
    {
        final int fontDescent = fontMetrics != null ? fontMetrics.getDescent () : 0;
        switch ( titleSide )
        {
            case TOP:
            {
                return titlePosition.equals ( TitlePosition.aboveLive ) ? titleAreaHeight - sw - fontDescent :
                        titleAreaHeight - fontDescent;
            }
            case BOTTOM:
            {
                return titlePosition.equals ( TitlePosition.belowLine ) ? h - sw - fontDescent : h - fontDescent;
            }
            case LEFT:
            {
                // todo
                break;
            }
            case RIGHT:
            {
                // todo
                break;
            }
        }
        return 0;
    }

    protected double getBorderPosition ()
    {
        if ( emptyTitle )
        {
            return borderCenter;
        }
        else
        {
            if ( titlePosition.equals ( TitlePosition.insideLine ) )
            {
                return ( double ) titleAreaHeight / 2;
            }
            else if ( titlePosition.equals ( TitlePosition.aboveLive ) )
            {
                return titleAreaHeight - borderCenter;
            }
            else
            {
                return borderCenter;
            }
        }
    }

    protected int getTitleAreaHeight ( final C c )
    {
        if ( isEmptyTitle () )
        {
            return sw;
        }
        else
        {
            final int height = c.getFontMetrics ( c.getFont () ).getHeight ();
            return titlePosition.equals ( TitlePosition.insideLine ) ? height : height + sw;
        }
    }

    protected boolean isEmptyTitle ()
    {
        return titleText == null;
    }

    protected Shape getBorderShape ()
    {
        Rectangle2D rect = null;
        switch ( titleSide )
        {
            case TOP:
            {
                rect = new Rectangle2D.Double ( borderCenter, borderPosition, w - borderCenter * 2, h - borderPosition - borderCenter );
                break;
            }
            case LEFT:
            {
                rect = new Rectangle2D.Double ( borderPosition, borderCenter, w - borderPosition - borderCenter, h - borderCenter * 2 );
                break;
            }
            case BOTTOM:
            {
                rect = new Rectangle2D.Double ( borderCenter, borderCenter, w - borderCenter * 2, h - borderPosition - borderCenter );
                break;
            }
            case RIGHT:
            {
                rect = new Rectangle2D.Double ( borderCenter, borderCenter, w - borderPosition - borderCenter, h - borderCenter * 2 );
                break;
            }
        }
        final int round = getRound ();
        return round <= 0 ? rect :
                new RoundRectangle2D.Double ( rect.getX (), rect.getY (), rect.getWidth (), rect.getHeight (), round * 2, round * 2 );
    }

    protected Shape getBorderClipShape ()
    {
        if ( emptyTitle )
        {
            return null;
        }
        else
        {
            switch ( titleSide )
            {
                case TOP:
                {
                    return new RoundRectangle2D.Double ( titleX - titleBorderGap, borderPosition - titleAreaHeight / 2,
                            titleWidth + titleBorderGap * 2, titleAreaHeight, 3, 3 );
                }
                case BOTTOM:
                {
                    return new RoundRectangle2D.Double ( titleX - titleBorderGap, h - borderPosition - titleAreaHeight / 2,
                            titleWidth + titleBorderGap * 2, titleAreaHeight, 3, 3 );
                }
                case LEFT:
                {
                    // todo
                    return null;
                }
                case RIGHT:
                {
                    // todo
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public Dimension getPreferredSize ()
    {
        if ( isEmptyTitle () )
        {
            return super.getPreferredSize ();
        }
        else
        {
            final int titleAreaHeight = getTitleAreaHeight ( component );
            final int titleWidth = component.getFontMetrics ( component.getFont () ).stringWidth ( titleText );
            final int border = Math.max ( getStrokeWidth (), getRound () );
            final int title = Math.max ( titleAreaHeight, border );
            switch ( titleSide )
            {
                case TOP:
                case BOTTOM:
                {
                    return new Dimension ( border * 2 + titleWidth + titleOffset * 2 +
                            titleBorderGap * 2, title + border );
                }
                case LEFT:
                case RIGHT:
                {
                    return new Dimension ( title + border, border * 2 + titleWidth + titleOffset * 2 +
                            titleBorderGap * 2 );
                }
            }
            return null;
        }
    }
}