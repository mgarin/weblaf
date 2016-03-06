package com.alee.utils.swing;

import javax.swing.*;
import javax.swing.plaf.ActionMapUIResource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An ActionMap that populates its contents as necessary. The contents are populated by invoking the <code>loadActionMap</code>
 * method on the passed in Object.
 */
public class LazyActionMap extends ActionMapUIResource
{
    /**
     * Object to invoke <code>loadActionMap</code> on. This may be a Class object.
     */
    private transient Object _loader;

    /**
     * Installs an ActionMap that will be populated by invoking the <code>loadActionMap</code> method on the specified Class when
     * necessary.
     * <p>
     * This should be used if the ActionMap can be shared.
     *
     * @param c           JComponent to install the ActionMap on.
     * @param loaderClass Class object that gets loadActionMap invoked on.
     * @param defaultsKey Key to use to defaults table to check for existing map and what resulting Map will be registered on.
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
     * Returns an ActionMap that will be populated by invoking the <code>loadActionMap</code> method on the specified Class when necessary.
     * <p>
     * This should be used if the ActionMap can be shared.
     *
     * @param loaderClass Class object that gets loadActionMap invoked on.
     * @param defaultsKey Key to use to defaults table to check for existing map and what resulting Map will be registered on.
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

    private LazyActionMap ( final Class loader )
    {
        _loader = loader;
    }

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

    private void loadIfNecessary ()
    {
        if ( _loader != null )
        {
            final Object loader = _loader;

            _loader = null;
            final Class klass = ( Class ) loader;
            try
            {
                final Method method = klass.getDeclaredMethod ( "loadActionMap", new Class[]{ LazyActionMap.class } );
                method.setAccessible ( true );
                method.invoke ( klass, this );
            }
            catch ( final NoSuchMethodException nsme )
            {
                assert false : "LazyActionMap unable to load actions " + klass;
            }
            catch ( final IllegalAccessException iae )
            {
                assert false : "LazyActionMap unable to load actions " + iae;
            }
            catch ( final InvocationTargetException ite )
            {
                assert false : "LazyActionMap unable to load actions " + ite;
            }
            catch ( final IllegalArgumentException iae )
            {
                assert false : "LazyActionMap unable to load actions " + iae;
            }
        }
    }
}
