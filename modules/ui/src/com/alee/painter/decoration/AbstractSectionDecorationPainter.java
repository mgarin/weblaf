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
import com.alee.painter.PainterException;
import com.alee.painter.SectionPainter;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Abstract decoration painter that can be used by any section painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public abstract class AbstractSectionDecorationPainter<C extends JComponent, U extends ComponentUI, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements SectionPainter<C, U>
{
    /**
     * Origin {@link Painter}.
     * It is mainly used to provide origin decoration state duplication behavior.
     * It is kept within {@link WeakReference} to avoid memory leaks as section painters might be easily replaced.
     */
    protected transient WeakReference<Painter<C, U>> origin;

    @Override
    public void install ( final C c, final U ui, final Painter<C, U> origin )
    {
        this.origin = new WeakReference<Painter<C, U>> ( origin );
        super.install ( c, ui );
    }

    @Override
    public void uninstall ( final C c, final U ui, final Painter<C, U> origin )
    {
        super.uninstall ( c, ui );
        this.origin = null;
    }

    @Override
    public Painter<C, U> getOrigin ()
    {
        if ( origin == null )
        {
            throw new PainterException ( "Origin Painter was not specified for painter: " + this );
        }
        final Painter<C, U> originPainter = origin.get ();
        if ( originPainter == null )
        {
            throw new PainterException ( "Origin Painter was destroyed before its SectionPainter: " + this );
        }
        return originPainter;
    }

    /**
     * We do not want {@link SectionPainter} to perform any default tracking as it is already done within {@link #origin}.
     * Maybe in some rare cases in the future this will be enabled but so far there are none.
     *
     * @return {@code false}
     */
    @Override
    protected boolean usesFocusedView ()
    {
        return false;
    }

    /**
     * We do not want {@link SectionPainter} to perform any default tracking as it is already done within {@link #origin}.
     * Maybe in some rare cases in the future this will be enabled but so far there are none.
     *
     * @return {@code false}
     */
    @Override
    protected boolean usesInFocusedParentView ()
    {
        return false;
    }

    /**
     * We do not want {@link SectionPainter} to perform any default tracking as it is already done within {@link #origin}.
     * Maybe in some rare cases in the future this will be enabled but so far there are none.
     *
     * @return {@code false}
     */
    @Override
    protected boolean usesHoverView ()
    {
        return false;
    }

    /**
     * We do not want {@link SectionPainter} to perform any default tracking as it is already done within {@link #origin}.
     * Maybe in some rare cases in the future this will be enabled but so far there are none.
     *
     * @return {@code false}
     */
    @Override
    protected boolean usesHierarchyBasedView ()
    {
        return false;
    }

    /**
     * Returns decoration states from {@link #origin} {@link Painter} if possible.
     * That is quite useful to retain all origin component states from its {@link Painter} and further expand on them for the section.
     *
     * @return current component section decoration states
     */
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states;
        final Painter<C, U> origin = getOrigin ();
        if ( origin != null && origin instanceof IDecorationPainter )
        {
            // Retrieving origin decoration states
            final IDecorationPainter painter = ( IDecorationPainter ) origin;
            states = painter.getDecorationStates ();
        }
        else
        {
            // Retrieving default decoration states
            states = super.getDecorationStates ();
        }
        return states;
    }

    /**
     * Plain background is rarely needed in section painters.
     * It was designed mostly for base component painters that might want to fill background by default.
     *
     * @param c component to paint background for
     * @return always {@code false}
     */
    @Override
    protected boolean isPlainBackgroundRequired ( final C c )
    {
        return false;
    }
}