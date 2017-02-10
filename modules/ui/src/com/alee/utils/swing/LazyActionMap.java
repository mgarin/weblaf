package com.alee.utils.swing;

import com.alee.laf.LookAndFeelException;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import javax.swing.plaf.ActionMapUIResource;

/**
 * An ActionMap that populates its contents as necessary.
 * The contents are populated by invoking the {@code loadActionMap} method on the passed in Object.
 *
 * @author Scott Violet
 */

public final class LazyActionMap extends ActionMapUIResource
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
        ActionMap map = ( ActionMap ) UIManager.get ( defaultsKey );
        if ( map == null )
        {
            map = new LazyActionMap ( loaderClass );
            UIManager.getLookAndFeelDefaults ().put ( defaultsKey, map );
        }
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

    /**
     * Adds specified {@link Action} into map.
     *
     * @param action {@link Action} to add
     */
    public void put ( final Action action )
    {
        put ( action.getValue ( Action.NAME ), action );
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
            catch ( final Throwable e )
            {
                throw new LookAndFeelException ( "LazyActionMap unable to load actions", e );
            }
        }
    }
}