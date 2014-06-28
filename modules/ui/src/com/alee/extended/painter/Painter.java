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

import javax.swing.*;
import java.awt.*;

/**
 * This interface provides basic methods for component view painting.
 * Using painters you can quickly and easily change Swing and WebLaF components view.
 * <p/>
 * You might want to use single painter for multiply components, but be aware that whether or not single painter can work with multiply
 * components at once depends only on its own way of implementation. In most cases painters which does some animation won't work well with
 * multiply components unless noted otherwise.
 *
 * @param <E> component type
 * @author Mikle Garin
 * @see AbstractPainter
 */

public interface Painter<E extends JComponent>
{
    /**
     * Called when painter is intalled into some component.
     * You might want to use this method to add custom component listeners or modify component settings.
     *
     * @param c component to process
     */
    public void install ( E c );

    /**
     * Called when painter is intalled into some component.
     * You might want to use this method to add custom component listeners or modify component settings.
     *
     * @param c component to process
     */
    public void uninstall ( E c );

    /**
     * Returns whether visual data provided by this painter is opaque or not.
     * Returned value might affect component opacity depending on painter support inside that component UI.
     * Simply return null if you don't want to change default component opacity.
     *
     * @param c component to process
     * @return true if visual data provided by this painter is opaque, false otherwise
     */
    public Boolean isOpaque ( E c );

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
     * You may pass null instead of zero margin - it will simply be ignored in that case.
     *
     * @param c component to process
     * @return margin required for visual data provided by this painter or null for zero margin
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

    /**
     * Adds new painter listener.
     *
     * @param listener painter listener to add
     */
    public void addPainterListener ( PainterListener listener );

    /**
     * Removes painter listener.
     *
     * @param listener painter listener to remove
     */
    public void removePainterListener ( PainterListener listener );
}