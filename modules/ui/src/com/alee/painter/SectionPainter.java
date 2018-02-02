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

/**
 * This interface is implemented by painters which are designed to draw a small section for component-specific painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public interface SectionPainter<C extends JComponent, U extends ComponentUI> extends Painter<C, U>
{
    /**
     * Returns section identifier that is be unique within its parent {@link Painter}.
     * It could be anything, but shorter identifiers are recommended for performance reasons.
     *
     * @return section identifier that is be unique within its parent {@link Painter}
     */
    public String getSectionId ();

    /**
     * Returns origin {@link Painter}.
     *
     * @return origin {@link Painter}
     */
    public Painter<C, U> getOrigin ();

    /**
     * Installs this {@link SectionPainter} into specified {@link Painter}.
     * Use this method over {@link #install(JComponent, ComponentUI)} whenever you want to install this as {@link SectionPainter}.
     *
     * @param c      component this painter is being installed onto
     * @param ui     component UI
     * @param origin origin {@link Painter}
     */
    public void install ( C c, U ui, Painter<C, U> origin );

    /**
     * Uninstalls this {@link SectionPainter} from specified {@link Painter}.
     * Use this method over {@link #uninstall(JComponent, ComponentUI)} whenever you want to uninstall this as {@link SectionPainter}.
     *
     * @param c      component this painter is being uninstalled from
     * @param ui     component UI
     * @param origin origin {@link Painter}
     */
    public void uninstall ( C c, U ui, Painter<C, U> origin );
}