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
 * This interface provides basic methods for drawing components or sections of components.
 * Using painters you can easily change Swing and WebLaF components visual representation.
 * <p>
 * Whether or not single painter can be used for multiply components exclusively depends on its implementation.
 * In most cases painters which does some animation won't work well with multiply components unless stated otherwise in JavaDoc.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 * @see com.alee.painter.AbstractPainter
 * @see com.alee.painter.SpecificPainter
 * @see com.alee.painter.SectionPainter
 */

public interface Painter<E extends JComponent, U extends ComponentUI>
{
    /**
     * Called when painter is installed onto some component.
     * You might want to use this method to add custom component listeners or modify component settings.
     *
     * @param c  component this painter is being installed onto
     * @param ui component UI
     */
    public void install ( E c, U ui );

    /**
     * Called when painter is installed into some component.
     * You might want to use this method to add custom component listeners or modify component settings.
     *
     * @param c  component this painter is being uninstalled from
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
     * These borders should not include possible component margin and padding, only borders provided by painter.
     * These borders are added to component's margin and padding when the final component border is calculated.
     * These borders should not take component orientation into account, painter will take care of it later.
     * <p>
     * You may pass {@code null} in case additional borders aren't needed for this painter.
     *
     * @return borders required for visual data provided by this painter or {@code null} in case those aren't needed
     */
    public Insets getBorders ();

    /**
     * Paints visual data using component graphics context.
     * Provided graphics context and component are taken directly from component UI paint method.
     * <p>
     * It is highly recommended to honor provided painting bounds to avoid painting issues.
     * These bounds might be representing full component size or contain just a small portion of the component.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param c      painted component
     * @param ui     painted component UI
     */
    public void paint ( Graphics2D g2d, Rectangle bounds, E c, U ui );

    /**
     * Returns preferred size required for proper painting of visual data provided by this painter.
     * This should not take into account any sizes not related to the painter itself.
     *
     * @return preferred size required for proper painting of visual data provided by this painter
     */
    public Dimension getPreferredSize ();

    /**
     * Adds new painter listener.
     *
     * @param listener painter listener to add
     * @see com.alee.painter.PainterListener
     */
    public void addPainterListener ( PainterListener listener );

    /**
     * Removes painter listener.
     *
     * @param listener painter listener to remove
     * @see com.alee.painter.PainterListener
     */
    public void removePainterListener ( PainterListener listener );
}