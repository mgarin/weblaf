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

package com.alee.painter.decoration;

import com.alee.painter.Painter;
import com.alee.painter.SectionPainter;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Abstract decoration painter that can be used by any section painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public abstract class AbstractSectionDecorationPainter<E extends JComponent, U extends ComponentUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements SectionPainter<E, U>
{
    /**
     * Origin painter.
     * It is mainly used to provide origin decoration state duplication behavior.
     * It is kept within weak reference to avoid memory leaks as section painters might be easily replaced.
     */
    protected WeakReference<Painter<E, U>> origin;

    /**
     * Returns origin painter.
     *
     * @return origin painter
     */
    public Painter<E, U> getOrigin ()
    {
        return origin != null ? origin.get () : null;
    }

    /**
     * Sets origin painter.
     *
     * @param origin origin painter
     */
    public void setOrigin ( final Painter<E, U> origin )
    {
        this.origin = new WeakReference<Painter<E, U>> ( origin );
    }

    /**
     * Clears origin painter reference.
     */
    public void clearOrigin ()
    {
        this.origin = null;
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final Painter<E, U> origin = getOrigin ();
        return origin != null && origin instanceof AbstractDecorationPainter ?
                ( ( AbstractDecorationPainter ) origin ).getDecorationStates () : super.getDecorationStates ();
    }
}