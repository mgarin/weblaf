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

package com.alee.laf.progressbar;

import com.alee.api.annotations.NotNull;
import com.alee.api.data.Orientation;
import com.alee.painter.decoration.WebDecoration;
import com.alee.painter.decoration.shape.AbstractProgressShape;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;

/**
 * {@link JProgressBar} progress shape.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> shape type
 * @author Mikle Garin
 */
@XStreamAlias ( "ProgressShape" )
public class ProgressShape<C extends JProgressBar, D extends WebDecoration<C, D>, I extends ProgressShape<C, D, I>>
        extends AbstractProgressShape<C, D, I>
{
    @NotNull
    @Override
    protected Orientation getOrientation ( @NotNull final C c, @NotNull final D d )
    {
        return Orientation.get ( c.getOrientation () );
    }

    @Override
    protected boolean isIndeterminate ( @NotNull final C c, @NotNull final D d )
    {
        return c.isIndeterminate ();
    }

    @Override
    protected double getProgress ( @NotNull final C c, @NotNull final D d )
    {
        return ( double ) c.getValue () / ( c.getMaximum () - c.getMinimum () );
    }
}