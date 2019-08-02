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

package com.alee.graphics.strokes;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.*;

/**
 * @author Mikle Garin
 */
public class TextStroke implements Stroke
{
    private final String text;
    private final Font font;
    private boolean stretchToFit = false;
    private boolean repeat = false;
    private final AffineTransform t = new AffineTransform ();

    private static final float FLATNESS = 1;

    public TextStroke ( final String text, final Font font )
    {
        this ( text, font, true, false );
    }

    public TextStroke ( final String text, final Font font, final boolean stretchToFit, final boolean repeat )
    {
        this.text = text;
        this.font = font;
        this.stretchToFit = stretchToFit;
        this.repeat = repeat;
    }

    @Override
    public Shape createStrokedShape ( final Shape shape )
    {
        final FontRenderContext frc = new FontRenderContext ( null, true, true );
        final GlyphVector glyphVector = font.createGlyphVector ( frc, text );

        final GeneralPath result = new GeneralPath ();
        final PathIterator it = new FlatteningPathIterator ( shape.getPathIterator ( null ), FLATNESS );
        final float[] points = new float[ 6 ];
        float moveX = 0;
        float moveY = 0;
        float lastX = 0;
        float lastY = 0;
        float thisX;
        float thisY;
        int type;
        float next = 0;
        int currentChar = 0;
        final int length = glyphVector.getNumGlyphs ();

        if ( length == 0 )
        {
            return result;
        }

        final float factor = stretchToFit ? measurePathLength ( shape ) / ( float ) glyphVector.getLogicalBounds ().getWidth () : 1.0f;
        float nextAdvance = 0;

        while ( currentChar < length && !it.isDone () )
        {
            type = it.currentSegment ( points );
            switch ( type )
            {
                case PathIterator.SEG_MOVETO:
                    moveX = lastX = points[ 0 ];
                    moveY = lastY = points[ 1 ];
                    result.moveTo ( moveX, moveY );
                    nextAdvance = glyphVector.getGlyphMetrics ( currentChar ).getAdvance () * 0.5f;
                    next = nextAdvance;
                    break;

                case PathIterator.SEG_CLOSE:
                    points[ 0 ] = moveX;
                    points[ 1 ] = moveY;
                    // Fall into....

                case PathIterator.SEG_LINETO:
                    thisX = points[ 0 ];
                    thisY = points[ 1 ];
                    final float dx = thisX - lastX;
                    final float dy = thisY - lastY;
                    final float distance = ( float ) Math.sqrt ( dx * dx + dy * dy );
                    if ( distance >= next )
                    {
                        final float r = 1.0f / distance;
                        final float angle = ( float ) Math.atan2 ( dy, dx );
                        while ( currentChar < length && distance >= next )
                        {
                            final Shape glyph = glyphVector.getGlyphOutline ( currentChar );
                            final Point2D p = glyphVector.getGlyphPosition ( currentChar );
                            final float px = ( float ) p.getX ();
                            final float py = ( float ) p.getY ();
                            final float x = lastX + next * dx * r;
                            final float y = lastY + next * dy * r;
                            final float advance = nextAdvance;
                            nextAdvance =
                                    currentChar < length - 1 ? glyphVector.getGlyphMetrics ( currentChar + 1 ).getAdvance () * 0.5f : 0;
                            t.setToTranslation ( x, y );
                            t.rotate ( angle );
                            t.translate ( -px - advance, -py );
                            result.append ( t.createTransformedShape ( glyph ), false );
                            next += ( advance + nextAdvance ) * factor;
                            currentChar++;
                            if ( repeat )
                            {
                                currentChar %= length;
                            }
                        }
                    }
                    next -= distance;
                    lastX = thisX;
                    lastY = thisY;
                    break;
            }
            it.next ();
        }

        return result;
    }

    public float measurePathLength ( final Shape shape )
    {
        final PathIterator it = new FlatteningPathIterator ( shape.getPathIterator ( null ), FLATNESS );
        final float[] points = new float[ 6 ];
        float moveX = 0;
        float moveY = 0;
        float lastX = 0;
        float lastY = 0;
        float thisX;
        float thisY;
        int type;
        float total = 0;

        while ( !it.isDone () )
        {
            type = it.currentSegment ( points );
            switch ( type )
            {
                case PathIterator.SEG_MOVETO:
                    moveX = lastX = points[ 0 ];
                    moveY = lastY = points[ 1 ];
                    break;

                case PathIterator.SEG_CLOSE:
                    points[ 0 ] = moveX;
                    points[ 1 ] = moveY;
                    // Fall into....

                case PathIterator.SEG_LINETO:
                    thisX = points[ 0 ];
                    thisY = points[ 1 ];
                    final float dx = thisX - lastX;
                    final float dy = thisY - lastY;
                    total += ( float ) Math.sqrt ( dx * dx + dy * dy );
                    lastX = thisX;
                    lastY = thisY;
                    break;
            }
            it.next ();
        }

        return total;
    }
}