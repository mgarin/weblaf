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

package com.alee.laf;

import com.alee.api.annotations.NotNull;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/**
 * Abstract {@link UIInputListener} implementation that resolves and keeps component and UI references.
 *
 * @param <C> {@link JComponent} type
 * @param <U> {@link ComponentUI} type
 * @author Mikle Garin
 */
public abstract class AbstractUIInputListener<C extends JComponent, U extends ComponentUI> implements UIInputListener<C>
{
    /**
     * {@link JComponent} this listener is used for.
     */
    protected C component;

    /**
     * {@link ComponentUI} of the {@link JComponent} this listener is used for.
     */
    protected U componentUI;

    @Override
    public void install ( @NotNull final C component )
    {
        // Saving references to component and UI
        this.component = component;
        this.componentUI = LafUtils.getUI ( component );
    }

    @Override
    public void uninstall ( @NotNull final C component )
    {
        // Cleaning up references to component and UI
        this.componentUI = null;
        this.component = null;
    }
}