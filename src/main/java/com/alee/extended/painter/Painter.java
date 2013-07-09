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

package com.alee.extended.painter;

import java.awt.*;

/**
 * This interface provides basic methods for component background (or component parts) painting.
 * It is supported in most of the Web-components and WebLaF UIs so that you can quickly and easily change any component view.
 *
 * @param <E> component type
 * @author Mikle Garin
 * @see DefaultPainter
 * @since 1.4
 */

public interface Painter<E extends Component>
{
    /**
     * Returns whether visual data provided by this painter is opaque or not.
     * Returned value might affect component opacity depending on painter support inside that component UI.
     *
     * @param c component to process
     * @return true if visual data provided by this painter is opaque, false otherwise
     */
    public boolean isOpaque ( E c );

    /**
     * Returns preferred size required for proper painting of visual data provided by this painter.
     * This should not take into account any sizes not related to this painter settings (for example text size on button).
     *
     * @param c component to process
     * @return preferred size required for proper painting of visual data provided by this painter
     */
    public Dimension getPreferredSize ( E c );

    /**
     * Returns margin required for visual data provided by this painter.
     * This margin is usually added to component's margin when the final component border is calculated.
     *
     * @param c component to process
     * @return margin required for visual data provided by this painter
     */
    public Insets getMargin ( E c );

    /**
     * Paints visual data onto the component graphics.
     * Provided graphics and component are taken directly from component UI paint method.
     * Provided bounds are usually fake (zero location, component size) but in some cases it might be specified by componentUI.
     *
     * @param g2d    component graphics
     * @param bounds bounds for painter visual data
     * @param c      component to process
     */
    public void paint ( Graphics2D g2d, Rectangle bounds, E c );
}