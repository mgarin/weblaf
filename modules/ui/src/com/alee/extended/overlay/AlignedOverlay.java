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

package com.alee.extended.overlay;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.BoxOrientation;

import javax.swing.*;
import java.awt.*;

/**
 * {@link Overlay} implementation that aligns horizontally and vertically within {@link WebOverlay} component bounds.
 * Note that specified margin can offset covered area relative to {@link WebOverlay} component bounds.
 *
 * @author Mikle Garin
 */
public class AlignedOverlay extends AbstractOverlay
{
    /**
     * Horizontal overlay alignment.
     * Can be either {@link BoxOrientation#left}, {@link BoxOrientation#right} or {@link BoxOrientation#center}.
     */
    @NotNull
    protected BoxOrientation halign;

    /**
     * Vertical overlay alignment.
     * Can be either {@link BoxOrientation#top}, {@link BoxOrientation#bottom} or {@link BoxOrientation#center}.
     */
    @NotNull
    protected BoxOrientation valign;

    /**
     * Constructs new {@link AlignedOverlay}.
     *
     * @param component overlay {@link JComponent}
     * @param halign    horizontal overlay alignment
     * @param valign    vertical overlay alignment
     */
    public AlignedOverlay ( @NotNull final JComponent component, @NotNull final BoxOrientation halign,
                            @NotNull final BoxOrientation valign )
    {
        this ( component, halign, valign, new Insets ( 0, 0, 0, 0 ) );
    }

    /**
     * Constructs new {@link AlignedOverlay}.
     *
     * @param component overlay {@link JComponent}
     * @param halign    horizontal overlay alignment
     * @param valign    vertical overlay alignment
     * @param margin    additional margin for the {@link WebOverlay} content {@link JComponent}
     */
    public AlignedOverlay ( @NotNull final JComponent component, @NotNull final BoxOrientation halign,
                            @NotNull final BoxOrientation valign, @NotNull final Insets margin )
    {
        super ( component, margin );
        this.halign = halign;
        this.valign = valign;
    }

    @NotNull
    @Override
    public Rectangle bounds ( @NotNull final WebOverlay container, @Nullable final JComponent component, @NotNull final Rectangle bounds )
    {
        final JComponent overlay = component ();
        final Dimension ps = overlay.getPreferredSize ();

        final int x;
        switch ( halign )
        {
            case left:
                x = bounds.x;
                break;

            default:
            case center:
                x = bounds.x + bounds.width / 2 - ps.width / 2;
                break;

            case right:
                x = bounds.x + bounds.width - ps.width;
                break;
        }

        final int y;
        switch ( valign )
        {
            case top:
                y = bounds.y;
                break;

            default:
            case center:
                y = bounds.y + bounds.height / 2 - ps.height / 2;
                break;

            case bottom:
                y = bounds.y + bounds.height - ps.height;
                break;
        }

        return new Rectangle ( x, y, ps.width, ps.height );
    }
}