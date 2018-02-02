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

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.util.List;

/**
 * Interface that defines main methods for any {@link Painter} based on {@link Decorations}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 * @see Decorations
 * @see IDecoration
 * @see Painter
 */

public interface IDecorationPainter<C extends JComponent, U extends ComponentUI, D extends IDecoration<C, D>>
        extends Painter<C, U>
{
    /**
     * Returns current component decoration states.
     * This method shouldn't cache states internally to avoid update issues.
     * That is why it is important to avoid any heavy operations within this method.
     *
     * @return current component decoration states
     */
    public List<String> getDecorationStates ();

    /**
     * Returns whether component has decoration associated with specified state.
     *
     * @param state decoration state
     * @return {@code true} if component has decoration associated with specified state, {@code false} otherwise
     */
    public boolean usesState ( String state );

    /**
     * Returns decoration matching current states.
     * Decorations returned here are cached copies of the data presented in skins.
     * This was made to avoid corrupting inital data and to increase the decoration retrieval speed.
     *
     * @return decoration matching current states
     */
    public D getDecoration ();

    /**
     * Performs current decoration state update.
     */
    public void updateDecorationState ();
}