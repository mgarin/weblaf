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

package com.alee.painter;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
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
 * @param <U> component UI type
 * @author Mikle Garin
 * @see AbstractPainter
 */

public interface Painter<E extends JComponent, U extends ComponentUI>
{
    /**
     * Called when painter is installed into some component.
     * You might want to use this method to add custom component listeners or modify component settings.
     *
     * @param c  component to process
     * @param ui component UI
     */
    public void install ( E c, U ui );

    /**
     * Called when painter is installed into some component.
     * You might want to use this method to add custom component listeners or modify component settings.
     *
     * @param c  component to process
     * @param ui component UI
     */
    public void uninstall ( E c, U ui );

    /**
     * Returns whether visual data provided by this painter is opaque or not.
     * Returned value might affect component opacity depending on painter support inside that component UI.
     * Simply return null if you don't want to change default component opacity.
     *
     * @return true if visual data provided by this painter is opaque, false otherwise
     */
    public Boolean isOpaque ();

    /**
     * Returns borders required for visual data provided by this painter.
     * These borders are added to component's margin and padding when the final component border is calculated.
     * You may pass {@code null} in case additional borders aren't needed for this painter.
     * These borders do not include possible component margin and padding, only borders provided by painter.
     *
     * @return borders required for visual data provided by this painter or {@code null} in case those aren't needed
     */
    public Insets getBorders ();

    /**
     * Paints visual data onto the component graphics.
     * Provided graphics and component are taken directly from component UI paint method.
     * Provided bounds are usually fake (zero location, component size) but in some cases it might be specified by componentUI.
     *
     * @param g2d    component graphics
     * @param bounds bounds for painter visual data
     * @param c      painted component
     * @param ui     painted component UI
     */
    public void paint ( Graphics2D g2d, Rectangle bounds, E c, U ui );

    /**
     * Returns preferred size required for proper painting of visual data provided by this painter.
     * This should not take into account any sizes not related to this painter settings (for example text size on button).
     *
     * @return preferred size required for proper painting of visual data provided by this painter
     */
    public Dimension getPreferredSize ();

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