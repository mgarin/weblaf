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

package com.alee.extended.dock;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.CompassDirection;
import com.alee.painter.DefaultPainter;
import com.alee.painter.SectionPainter;
import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.IDecoration;

import java.awt.*;
import java.util.List;

/**
 * Basic painter for {@link WebDockablePane} component.
 * It is used as {@link WDockableFrameUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDockablePane">How to use WebDockablePane</a>
 */
public class DockablePanePainter<C extends WebDockablePane, U extends WDockablePaneUI, D extends IDecoration<C, D>>
        extends AbstractContainerPainter<C, U, D> implements IDockablePanePainter<C, U>
{
    /**
     * {@link SectionPainter} that can be used to customize sidebar and content areas backgrounds.
     */
    @DefaultPainter ( DockableAreaPainter.class )
    protected IDockableAreaPainter areaPainter;

    @Nullable
    @Override
    protected List<SectionPainter<C, U>> getSectionPainters ()
    {
        return asList ( areaPainter );
    }

    @Override
    protected void paintContent ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final U ui, @NotNull final Rectangle bounds )
    {
        // Paints sidebar area background
        paintSidebar ( g2d, c, ui, bounds );
    }

    /**
     * Paints {@link WebDockablePane} area background.
     *
     * @param g2d    {@link Graphics2D}
     * @param c      {@link WebDockablePane}
     * @param ui     {@link WDockablePaneUI}
     * @param bounds painting bounds
     */
    protected void paintSidebar ( @NotNull final Graphics2D g2d, @NotNull final C c, @NotNull final U ui, @NotNull final Rectangle bounds )
    {
        // There is no point to paint contnt or sidebar area decorations there is a maximized frame
        if ( areaPainter != null && c.getMaximizedFrame () == null )
        {
            final DockablePaneModel model = c.getModel ();
            final Rectangle outerBounds = model.getOuterBounds ( c );
            final Rectangle innerBounds = model.getInnerBounds ( c );
            for ( final CompassDirection area : CompassDirection.values () )
            {
                final Rectangle areaBounds = getAreaBounds ( area, outerBounds, innerBounds );
                if ( areaBounds.width > 0 && areaBounds.height > 0 && areaPainter.hasDecorationFor ( area ) )
                {
                    areaPainter.prepareToPaint ( area );
                    paintSection ( areaPainter, g2d, areaBounds );
                }
            }
        }
    }

    /**
     * Returns {@link WebDockablePane} area bounds.
     *
     * @param area        {@link WebDockablePane} area
     * @param outerBounds {@link WebDockablePane} outer bounds
     * @param innerBounds {@link WebDockablePane} inner bounds
     * @return {@link WebDockablePane} area bounds
     */
    @NotNull
    protected Rectangle getAreaBounds ( final CompassDirection area, final Rectangle outerBounds, final Rectangle innerBounds )
    {
        final Rectangle areaBounds;
        switch ( area )
        {
            case northWest:
                areaBounds = new Rectangle (
                        outerBounds.x,
                        outerBounds.y,
                        innerBounds.x - outerBounds.x,
                        innerBounds.y - outerBounds.y
                );
                break;

            case north:
                areaBounds = new Rectangle (
                        innerBounds.x,
                        outerBounds.y,
                        innerBounds.width,
                        innerBounds.y - outerBounds.y
                );
                break;

            case northEast:
                areaBounds = new Rectangle (
                        innerBounds.x + innerBounds.width,
                        outerBounds.y,
                        outerBounds.x + outerBounds.width - innerBounds.x - innerBounds.width,
                        innerBounds.y - outerBounds.y
                );
                break;

            case west:
                areaBounds = new Rectangle (
                        outerBounds.x,
                        innerBounds.y,
                        innerBounds.x - outerBounds.x,
                        innerBounds.height
                );
                break;

            default:
            case center:
                areaBounds = new Rectangle (
                        innerBounds.x,
                        innerBounds.y,
                        innerBounds.width,
                        innerBounds.height
                );
                break;

            case east:
                areaBounds = new Rectangle (
                        innerBounds.x + innerBounds.width,
                        innerBounds.y,
                        outerBounds.x + outerBounds.width - innerBounds.x - innerBounds.width,
                        innerBounds.height
                );
                break;

            case southWest:
                areaBounds = new Rectangle (
                        outerBounds.x,
                        innerBounds.y + innerBounds.height,
                        innerBounds.x - outerBounds.x,
                        outerBounds.y + outerBounds.height - innerBounds.y - innerBounds.height
                );
                break;

            case south:
                areaBounds = new Rectangle (
                        innerBounds.x,
                        innerBounds.y + innerBounds.height,
                        innerBounds.width,
                        outerBounds.y + outerBounds.height - innerBounds.y - innerBounds.height
                );
                break;

            case southEast:
                areaBounds = new Rectangle (
                        innerBounds.x + innerBounds.width,
                        innerBounds.y + innerBounds.height,
                        outerBounds.x + outerBounds.width - innerBounds.x - innerBounds.width,
                        outerBounds.y + outerBounds.height - innerBounds.y - innerBounds.height
                );
                break;
        }
        return areaBounds;
    }
}