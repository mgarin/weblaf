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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
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
    @Nullable
    @XStreamAsAttribute
    protected CompassDirection direction;

    /**
     * Gripper parts size.
     */
    @Nullable
    @XStreamAsAttribute
    protected Dimension part;

    /**
     * Spacing between gripper parts.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer spacing;

    /**
     * Gripper parts color.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color color;

    /**
     * Gripper parts shadow color.
     */
    @Nullable
    @XStreamAsAttribute
    protected Color shadow;

    @NotNull
    @Override
    public String getId ()
    {
        return id != null ? id : "gripper";
    }

    @Override
    public boolean isEmpty ( @NotNull final C c, @NotNull final D d )
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
    @NotNull
    public CompassDirection getDirection ( @NotNull final C c, @NotNull final D d )
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
    @NotNull
    public Dimension getPart ( @NotNull final C c, @NotNull final D d )
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
    public int getSpacing ( @NotNull final C c, @NotNull final D d )
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
    @NotNull
    public Color getColor ( @NotNull final C c, @NotNull final D d )
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
    @NotNull
    public Color getShadow ( @NotNull final C c, @NotNull final D d )
    {
        return shadow != null ? shadow : Color.LIGHT_GRAY;
    }

    @Override
    protected void paintContent ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final D d, @NotNull final Rectangle bounds )
    {
        final CompassDirection direction = getDirection ( c, d );
        final Dimension part = getPart ( c, d );
        final int spacing = getSpacing ( c, d );
        final Dimension gripSize = getGripSize ( part, spacing );
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
                x = bounds.x + bounds.width - gripSize.width + spacing;
                break;

            default:
                x = bounds.x + bounds.width / 2 - gripSize.width / 2 + spacing;
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
                    y = bounds.y + bounds.height - gripSize.height + spacing;
                    break;

                default:
                    y = bounds.y + bounds.height / 2 - gripSize.height / 2 + spacing;
            }
            for ( int row = 0; row <= 2; row++ )
            {
                paintGrip ( g2d, c, d, col, row, x, y, direction, part );
                y += part.height * 1.5 + spacing;
            }
            x += part.width * 1.5 + spacing;
        }
    }

    /**
     * Paints grip element at the specified column and row if it is necessary.
     *
     * @param g2d       graphics context
     * @param c         painted component
     * @param d         painted decoration state
     * @param col       grip element column
     * @param row       grip element row
     * @param x         grip element X coordinate
     * @param y         grip element Y coordinate
     * @param direction gripper direction
     * @param part      gripper parts size
     */
    protected void paintGrip ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final D d,
                               final int col, final int row, final int x, final int y,
                               @NotNull final CompassDirection direction, @NotNull final Dimension part )
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
            paintGripPart ( g2d, c, d, x, y, part );
        }
    }

    /**
     * Paints grip element at the specified column and row.
     *
     * @param g2d  graphics context
     * @param c    painted component
     * @param d    painted decoration state
     * @param x    grip element X coordinate
     * @param y    grip element Y coordinate
     * @param part gripper parts size
     */
    protected void paintGripPart ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final D d,
                                   final int x, final int y, @NotNull final Dimension part )
    {
        final int w = Math.round ( part.width * 1.5f );
        final int h = Math.round ( part.height * 1.5f );
        g2d.setPaint ( getShadow ( c, d ) );
        g2d.fillRect ( x + w - part.width, y + h - part.height, part.width, part.height );
        g2d.setPaint ( getColor ( c, d ) );
        g2d.fillRect ( x, y, part.width, part.height );
    }

    @NotNull
    @Override
    protected Dimension getContentPreferredSize ( @NotNull final C c, @NotNull final D d, @NotNull final Dimension available )
    {
        return getGripSize ( getPart ( c, d ), getSpacing ( c, d ) );
    }

    /**
     * Returns preferred grip size.
     *
     * @param part    gripper parts size
     * @param spacing spacing between gripper parts
     * @return preferred grip size
     */
    @NotNull
    protected Dimension getGripSize ( @NotNull final Dimension part, final int spacing )
    {
        final int w = ( int ) Math.round ( part.width * 1.5 * 3 + spacing * 4 );
        final int h = ( int ) Math.round ( part.height * 1.5 * 3 + spacing * 4 );
        return new Dimension ( w, h );
    }
}