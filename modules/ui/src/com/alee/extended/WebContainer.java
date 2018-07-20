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

package com.alee.extended;

import com.alee.api.jdk.BiConsumer;
import com.alee.utils.swing.extensions.ContainerMethods;
import com.alee.utils.swing.extensions.ContainerMethodsImpl;

import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.util.List;

/**
 * The base class for fully custom WebLaF container components.
 *
 * @param <U> container UI type
 * @param <C> container type
 * @author Mikle Garin
 */
public abstract class WebContainer<C extends WebContainer<C, U>, U extends ComponentUI> extends WebComponent<C, U>
        implements ContainerMethods<C>
{
    @Override
    public boolean contains ( final Component component )
    {
        return ContainerMethodsImpl.contains ( this, component );
    }

    @Override
    public C add ( final List<? extends Component> components )
    {
        return ContainerMethodsImpl.add ( this, components );
    }

    @Override
    public C add ( final List<? extends Component> components, final int index )
    {
        return ContainerMethodsImpl.add ( this, components, index );
    }

    @Override
    public C add ( final List<? extends Component> components, final Object constraints )
    {
        return ContainerMethodsImpl.add ( this, components, constraints );
    }

    @Override
    public C add ( final Component component1, final Component component2 )
    {
        return ContainerMethodsImpl.add ( this, component1, component2 );
    }

    @Override
    public C add ( final Component... components )
    {
        return ContainerMethodsImpl.add ( this, components );
    }

    @Override
    public C remove ( final List<? extends Component> components )
    {
        return ContainerMethodsImpl.remove ( this, components );
    }

    @Override
    public C remove ( final Component... components )
    {
        return ContainerMethodsImpl.remove ( this, components );
    }

    @Override
    public C removeAll ( final Class<? extends Component> componentClass )
    {
        return ContainerMethodsImpl.removeAll ( this, componentClass );
    }

    @Override
    public Component getFirstComponent ()
    {
        return ContainerMethodsImpl.getFirstComponent ( this );
    }

    @Override
    public Component getLastComponent ()
    {
        return ContainerMethodsImpl.getLastComponent ( this );
    }

    @Override
    public C equalizeComponentsWidth ()
    {
        return ContainerMethodsImpl.equalizeComponentsWidth ( this );
    }

    @Override
    public C equalizeComponentsHeight ()
    {
        return ContainerMethodsImpl.equalizeComponentsHeight ( this );
    }

    @Override
    public C equalizeComponentsSize ()
    {
        return ContainerMethodsImpl.equalizeComponentsSize ( this );
    }

    @Override
    public <T extends Component> C forEach ( final BiConsumer<Integer, T> consumer )
    {
        return ContainerMethodsImpl.forEach ( this, consumer );
    }
}