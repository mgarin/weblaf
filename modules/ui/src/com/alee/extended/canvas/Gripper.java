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

package com.alee.extended.canvas;

import com.alee.api.data.CompassDirection;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractContent;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Gripper content implementation.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "Gripper" )
public class Gripper<C extends JComponent, D extends IDecoration<C, D>, I extends Gripper<C, D, I>>
        extends AbstractContent<C, D, I>
{
    /**
     * todo 1. Make one-directional grippers have cone shape
     */

    /**
     * Gripper direction.
     */
    @XStreamAsAttribute
    protected CompassDirection direction;

    /**
     * Gripper parts size.
     */
    @XStreamAsAttribute
    protected Dimension part;

    /**
     * Spacing between gripper parts.
     */
    @XStreamAsAttribute
    protected Integer spacing;

    /**
     * Gripper parts color.
     */
    @XStreamAsAttribute
    protected Color color;

    /**
     * Gripper parts shadow color.
     */
    @XStreamAsAttribute
    protected Color shadow;

    @Override
    public String getId ()
    {
        return id != null ? id : "gripper";
    }

    @Override
    public boolean isEmpty ( final C c, final D d )
    {
        return false;
    }

    /**
     * Returns gripper direction.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return gripper direction
     */
    public CompassDirection getDirection ( final C c, final D d )
    {
        return direction != null ? direction : CompassDirection.southEast;
    }

    /**
     * Returns gripper parts size.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return gripper parts size
     */
    public Dimension getPart ( final C c, final D d )
    {
        return part != null ? part : new Dimension ( 2, 2 );
    }

    /**
     * Returns spacing between gripper parts.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return spacing between gripper parts
     */
    public Integer getSpacing ( final C c, final D d )
    {
        return spacing != null ? spacing : 1;
    }

    /**
     * Returns gripper parts color.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return gripper parts color
     */
    public Color getColor ( final C c, final D d )
    {
        return color != null ? color : Color.WHITE;
    }

    /**
     * Returns gripper parts shadow color.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return gripper parts shadow color
     */
    public Color getShadow ( final C c, final D d )
    {
        return shadow != null ? shadow : Color.LIGHT_GRAY;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final C c, final D d, final Rectangle bounds )
    {
        final Dimension gs = getGripSize ();
        int x;
        switch ( direction )
        {
            case northWest:
            case west:
            case southWest:
                x = bounds.x + spacing;
                break;

            case northEast:
            case east:
            case southEast:
                x = bounds.x + bounds.width - gs.width + spacing;
                break;

            default:
                x = bounds.x + bounds.width / 2 - gs.width / 2 + spacing;
        }
        for ( int col = 0; col <= 2; col++ )
        {
            int y;
            switch ( direction )
            {
                case northWest:
                case west:
                case southWest:
                    y = bounds.y + spacing;
                    break;

                case northEast:
                case east:
                case southEast:
                    y = bounds.y + bounds.height - gs.height + spacing;
                    break;

                default:
                    y = bounds.y + bounds.height / 2 - gs.height / 2 + spacing;
            }
            for ( int row = 0; row <= 2; row++ )
            {
                paintGrip ( g2d, c, d, col, row, x, y );
                y += part.height * 1.5 + spacing;
            }
            x += part.width * 1.5 + spacing;
        }
    }

    /**
     * Paints grip element at the specified column and row if it is necessary.
     *
     * @param g2d graphics context
     * @param c   painted component
     * @param d   painted decoration state
     * @param col grip element column
     * @param row grip element row
     * @param x   grip element X coordinate
     * @param y   grip element Y coordinate
     */
    protected void paintGrip ( final Graphics2D g2d, final C c, final D d, final int col, final int row, final int x, final int y )
    {
        boolean paint = false;
        switch ( direction )
        {
            case northWest:
            {
                paint = row == 0 || row == 1 && col < 2 || row == 2 && col == 0;
                break;
            }
            case north:
            {
                paint = row == 0;
                break;
            }
            case northEast:
            {
                paint = row == 0 || row == 1 && col > 0 || row == 2 && col == 2;
                break;
            }
            case west:
            {
                paint = col == 0;
                break;
            }
            case center:
            {
                paint = true;
                break;
            }
            case east:
            {
                paint = col == 2;
                break;
            }
            case southWest:
            {
                paint = row == 0 && col == 0 || row == 1 && col < 2 || row == 2;
                break;
            }
            case south:
            {
                paint = row == 2;
                break;
            }
            case southEast:
            {
                paint = row == 0 && col == 2 || row == 1 && col > 0 || row == 2;
                break;
            }
        }
        if ( paint )
        {
            paintGripPart ( g2d, c, d, x, y );
        }
    }

    /**
     * Paints grip element at the specified column and row.
     *
     * @param g2d graphics context
     * @param c   painted component
     * @param d   painted decoration state
     * @param x   grip element X coordinate
     * @param y   grip element Y coordinate
     */
    protected void paintGripPart ( final Graphics2D g2d, final C c, final D d, final int x, final int y )
    {
        final int w = Math.round ( part.width * 1.5f );
        final int h = Math.round ( part.height * 1.5f );
        g2d.setPaint ( getShadow ( c, d ) );
        g2d.fillRect ( x + w - part.width, y + h - part.height, part.width, part.height );
        g2d.setPaint ( getColor ( c, d ) );
        g2d.fillRect ( x, y, part.width, part.height );
    }

    @Override
    protected Dimension getContentPreferredSize ( final C c, final D d, final Dimension available )
    {
        return getGripSize ();
    }

    /**
     * Returns preferred grip size.
     *
     * @return preferred grip size
     */
    protected Dimension getGripSize ()
    {
        final int w = ( int ) Math.round ( part.width * 1.5 * 3 + spacing * 4 );
        final int h = ( int ) Math.round ( part.height * 1.5 * 3 + spacing * 4 );
        return new Dimension ( w, h );
    }
}