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

package com.alee.demo.skin.decoration;

import com.alee.demo.api.example.FeatureState;
import com.alee.demo.api.example.PreviewPanel;
import com.alee.painter.decoration.WebDecoration;
import com.alee.painter.decoration.background.AbstractBackground;
import com.alee.painter.decoration.shape.WebShape;
import com.alee.utils.GraphicsUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * Demo application feature state background.
 *
 * @param <D> decoration type
 * @author Mikle Garin
 */
@XStreamAlias ( "FeatureStateBackground" )
public class FeatureStateBackground<D extends WebDecoration<PreviewPanel, D>>
        extends AbstractBackground<PreviewPanel, D, FeatureStateBackground<D>>
{
    /**
     * State mark color.
     */
    @XStreamAsAttribute
    protected Color color;

    @Override
    public String getId ()
    {
        return id != null ? id : "feature-state";
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final PreviewPanel previewPanel, final D decoration,
                        final Shape shape )
    {
        final FeatureState featureState = previewPanel.getState ();
        if ( featureState != FeatureState.common )
        {
            final float opacity = getOpacity ();
            if ( opacity > 0 )
            {
                final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );
                final Object aa = GraphicsUtils.setupAntialias ( g2d );

                final int shift = ( ( WebShape ) decoration.getShape () ).getRound ().topLeft * 2;
                final Rectangle bb = shape.getBounds ();
                final GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
                if ( previewPanel.getComponentOrientation ().isLeftToRight () )
                {
                    gp.moveTo ( bb.x, bb.y + shift );
                    gp.lineTo ( bb.x + shift, bb.y );
                    gp.lineTo ( bb.x + shift * 2, bb.y );
                    gp.lineTo ( bb.x, bb.y + shift * 2 );
                }
                else
                {
                    final int cornerX = bb.x + bb.width;
                    gp.moveTo ( cornerX - shift * 2, bb.y );
                    gp.lineTo ( cornerX - shift, bb.y );
                    gp.lineTo ( cornerX, bb.y + shift );
                    gp.lineTo ( cornerX, bb.y + shift * 2 );
                }
                gp.closePath ();

                g2d.setPaint ( color );
                g2d.fill ( gp );

                GraphicsUtils.restoreAntialias ( g2d, aa );
                GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
            }
        }
    }
}