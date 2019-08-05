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

import com.alee.utils.ReflectUtils;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * {@link ActionMap} that populates its contents as necessary.
 * The contents are populated by invoking the {@code loadActionMap} method on the passed in Object.
 *
 * Note that this implementation is not used anymore as there are issues with using {@link com.alee.laf.UIAction} globally as replacement
 * for {@code sun.swing.UIAction} which is proprietary API. The problem is that {@code sun.swing.UIAction#isEnabled(Object)} is being
 * explicitely called by {@link javax.swing.SwingUtilities#notifyAction(Action, KeyStroke, KeyEvent, Object, int)} and there can be no
 * workaround for that specific case except making all actions non-global (per component/UI instance).
 *
 * @author Scott Violet
 * @author Mikle Garin
 * @see UIActionMap
 * @see javax.swing.plaf.basic.LazyActionMap
 * @see com.alee.laf.UIAction
 * @see sun.swing.UIAction
 */
@Deprecated
public final class LazyActionMap extends UIActionMap
{
    /**
     * {@link Class} providing actions in static {@code loadActionMap} method return.
     */
    private transient Class loader;

    /**
     * Installs an ActionMap that will be populated by invoking the {@code loadActionMap} method on the specified class when necessary.
     * This method should be used if {@link ActionMap} can be shared.
     *
     * @param c           {@link JComponent} to install the {@link ActionMap} on
     * @param loaderClass {@link Class} object that gets loadActionMap invoked on
     * @param defaultsKey Key to use to defaults table to check for existing map and what resulting Map will be registered on
     */
    public static void installLazyActionMap ( final JComponent c, final Class loaderClass, final String defaultsKey )
    {
        final ActionMap map = getActionMap ( loaderClass, defaultsKey );
        SwingUtilities.replaceUIActionMap ( c, map );
    }

    /**
     * Returns {@link ActionMap} that will be populated by invoking the {@code loadActionMap} method on the specified class when necessary.
     * This method should be used if {@link ActionMap} can be shared.
     *
     * @param loaderClass {@link Class} object that gets {@code loadActionMap} invoked on
     * @param defaultsKey key to use to defaults table to check for existing map and what resulting Map will be registered on
     * @return {@link ActionMap} that will be populated by invoking the {@code loadActionMap} method on the specified class when necessary
     */
    public static ActionMap getActionMap ( final Class loaderClass, final String defaultsKey )
    {
        ActionMap map = ( ActionMap ) UIManager.get ( defaultsKey );
        if ( map == null )
        {
            map = new LazyActionMap ( loaderClass );
            UIManager.getLookAndFeelDefaults ().put ( defaultsKey, map );
        }
        return map;
    }

    /**
     * Constructs new {@link LazyActionMap} using the specified {@link Class} as actions provider.
     *
     * @param loader {@link Class} providing actions in static {@code loadActionMap} method return
     */
    private LazyActionMap ( final Class loader )
    {
        super ();
        this.loader = loader;
    }

    @Override
    public void put ( final Object key, final Action action )
    {
        loadIfNecessary ();
        super.put ( key, action );
    }

    @Override
    public Action get ( final Object key )
    {
        loadIfNecessary ();
        return super.get ( key );
    }

    @Override
    public void remove ( final Object key )
    {
        loadIfNecessary ();
        super.remove ( key );
    }

    @Override
    public void clear ()
    {
        loadIfNecessary ();
        super.clear ();
    }

    @Override
    public Object[] keys ()
    {
        loadIfNecessary ();
        return super.keys ();
    }

    @Override
    public int size ()
    {
        loadIfNecessary ();
        return super.size ();
    }

    @Override
    public Object[] allKeys ()
    {
        loadIfNecessary ();
        return super.allKeys ();
    }

    @Override
    public void setParent ( final ActionMap map )
    {
        loadIfNecessary ();
        super.setParent ( map );
    }

    /**
     * Loads actions into this map if necessary.
     */
    private void loadIfNecessary ()
    {
        if ( loader != null )
        {
            try
            {
                // Storing loader locally
                final Class clazz = loader;
                loader = null;

                // Trying to load actions
                ReflectUtils.callStaticMethod ( clazz, "loadActionMap", LazyActionMap.this );
            }
            catch ( final Exception e )
            {
                throw new LookAndFeelException ( "LazyActionMap unable to load actions", e );
            }
        }
    }
}