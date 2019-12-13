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

package com.alee.managers.style;

import com.alee.api.annotations.NotNull;
import com.alee.painter.SpecificPainter;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract {@link ComponentDescriptor} implementation that stores various common information about the component.
 * It is used by all abstract and basic descriptors within WebLaF.
 * It can also be used for creating descriptors for any custom {@link JComponent} implementations.
 *
 * @param <C> {@link JComponent} type
 * @param <U> base {@link ComponentUI} type
 * @param <P> {@link SpecificPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 * @see StyleManager#registerComponentDescriptor(ComponentDescriptor)
 * @see StyleManager#unregisterComponentDescriptor(ComponentDescriptor)
 */
public abstract class AbstractComponentDescriptor<C extends JComponent, U extends ComponentUI, P extends SpecificPainter>
        implements ComponentDescriptor<C, U, P>
{
    /**
     * todo 1. Add specific painter class definition
     * todo 2. Add adaptive painter class definition
     * todo 3. Automate painters installation/uninstallation within UIs (in StyleManager.installSkin/uninstallSkin)
     */

    /**
     * {@link JComponent} icons cache.
     * {@link Icon}s are saved per component class.
     */
    protected static final Map<Class, Icon> componentIcons = new HashMap<Class, Icon> ();

    /**
     * {@link JComponent} identifier.
     */
    @NotNull
    protected final String id;

    /**
     * {@link JComponent} {@link Class}.
     */
    @NotNull
    protected final Class<C> componentClass;

    /**
     * {@link ComponentUI} {@link Class} identifier.
     */
    @NotNull
    protected final String uiClassId;

    /**
     * Base {@link ComponentUI} {@link Class} applicable to {@link JComponent}.
     * This is not an interface because all Swing {@link JComponent}s are based on {@link ComponentUI} class.
     */
    @NotNull
    protected final Class<U> baseUIClass;

    /**
     * {@link ComponentUI} {@link Class} used for {@link JComponent} by default.
     */
    @NotNull
    protected final Class<? extends U> uiClass;

    /**
     * {@link SpecificPainter} interface {@link Class}.
     */
    @NotNull
    protected final Class<P> painterInterface;

    /**
     * Default {@link SpecificPainter} implementation {@link Class}.
     */
    @NotNull
    protected final Class<? extends P> painterClass;

    /**
     * Adapter for {@link SpecificPainter}.
     */
    @NotNull
    protected final Class<? extends P> painterAdapterClass;

    /**
     * {@link JComponent} default {@link StyleId}.
     */
    @NotNull
    protected final StyleId defaultStyleId;

    /**
     * Constructs new {@link AbstractComponentDescriptor}.
     *
     * @param id                  {@link JComponent} identifier
     * @param componentClass      {@link JComponent} {@link Class}
     * @param uiClassId           {@link ComponentUI} {@link Class} identifier
     * @param baseUIClass         base {@link ComponentUI} {@link Class} applicable to {@link JComponent}
     * @param uiClass             {@link ComponentUI} {@link Class} used for {@link JComponent} by default
     * @param painterInterface    {@link SpecificPainter} interface {@link Class}
     * @param painterClass        default {@link SpecificPainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link SpecificPainter}
     * @param defaultStyleId      {@link JComponent} default {@link StyleId}
     */
    public AbstractComponentDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass, @NotNull final String uiClassId,
                                         @NotNull final Class<U> baseUIClass, @NotNull final Class<? extends U> uiClass,
                                         @NotNull final Class<P> painterInterface, @NotNull final Class<? extends P> painterClass,
                                         @NotNull final Class<? extends P> painterAdapterClass, @NotNull final StyleId defaultStyleId )
    {
        this.id = id;
        this.componentClass = componentClass;
        this.uiClassId = uiClassId;
        this.baseUIClass = baseUIClass;
        this.uiClass = uiClass;
        this.painterInterface = painterInterface;
        this.painterClass = painterClass;
        this.painterAdapterClass = painterAdapterClass;
        this.defaultStyleId = defaultStyleId;
    }

    @NotNull
    @Override
    public String getId ()
    {
        return id;
    }

    @NotNull
    @Override
    public Class<C> getComponentClass ()
    {
        return componentClass;
    }

    @NotNull
    @Override
    public String getUIClassId ()
    {
        return uiClassId;
    }

    @NotNull
    @Override
    public Class<U> getBaseUIClass ()
    {
        return baseUIClass;
    }

    @NotNull
    @Override
    public Class<? extends U> getUIClass ()
    {
        return uiClass;
    }

    @NotNull
    @Override
    public Class<P> getPainterInterface ()
    {
        return painterInterface;
    }

    @NotNull
    @Override
    public Class<? extends P> getPainterClass ()
    {
        return painterClass;
    }

    @NotNull
    @Override
    public Class<? extends P> getPainterAdapterClass ()
    {
        return painterAdapterClass;
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ()
    {
        return defaultStyleId;
    }

    @NotNull
    @Override
    public StyleId getDefaultStyleId ( @NotNull final JComponent component )
    {
        final StyleId styleId;
        if ( component instanceof Styleable )
        {
            final Styleable styleable = ( Styleable ) component;
            styleId = styleable.getDefaultStyleId ();
        }
        else
        {
            styleId = getDefaultStyleId ();
        }
        return styleId;
    }

    @NotNull
    @Override
    public Icon getIcon ()
    {
        final Icon icon;
        final Class<C> key = getComponentClass ();
        if ( componentIcons.containsKey ( key ) )
        {
            icon = componentIcons.get ( key );
        }
        else
        {
            try
            {
                icon = new ImageIcon ( getIconResource () );
                componentIcons.put ( key, icon );
            }
            catch ( final Exception e )
            {
                throw new StyleException ( "Unable to find component icon: " + key, e );
            }
        }
        return icon;
    }

    /**
     * Returns {@link Icon} resource.
     * We are simply using default style identifier as icon name for convenience.
     * It only works for WebLaF and Swing components as icons for all those components are predefined.
     * You can simply override this method for your custom component icons.
     *
     * @return {@link Icon} resource
     */
    @NotNull
    protected URL getIconResource ()
    {
        final String path = "icons/styleable/" + getId () + ".png";
        final URL resource = AbstractComponentDescriptor.class.getResource ( path );
        if ( resource == null )
        {
            throw new StyleException ( "Unable to find component type icon: " + getId () );
        }
        return resource;
    }

    @NotNull
    @Override
    public String getTitle ()
    {
        return ReflectUtils.getClassName ( getComponentClass () );
    }

    @Override
    public void updateDefaults ( @NotNull final UIDefaults table )
    {
        // Updating UI class mapping
        table.put ( getUIClassId (), getUIClass ().getName () );
    }

    @Override
    public void updateUI ( @NotNull final C component )
    {
        // Check whether or not we need to create new UI instance
        // It will be created and applied if component doesn't have its own UI instance yet
        // It will also be created and applied if component UI instance class is not assignable to the base UI class
        final ComponentUI existingUI = LafUtils.getUI ( component );
        if ( existingUI == null || !getBaseUIClass ().isAssignableFrom ( existingUI.getClass () ) )
        {
            try
            {
                // Using default UI class
                // We don't need to get it from LaF as descriptor value considered to be more important
                final Class<? extends U> uiClass = getUIClass ();

                // Creating UI instance using common Swing way
                final ComponentUI uiInstance = createUI ( component, uiClass );

                // Installing UI into component
                LafUtils.setUI ( component, uiInstance );
            }
            catch ( final Exception e )
            {
                // We were unable to install new component UI
                throw new StyleException ( "Unable to setup component UI: " + component, e );
            }
        }
        else
        {
            // Reinstall existing UI into component
            LafUtils.setUI ( component, existingUI );
        }
    }

    /**
     * Returns {@code ComponentUI} implementation instance for the specified component.
     * Note that whether it is a new instance or reused existing one depends on the {@link ComponentUI} implementation.
     * This method simply invokes static {@code ComponentUI.createUI(component)} method to retrieve the instance.
     *
     * @param component {@code JComponent} to return {@code ComponentUI} implementation instance for
     * @param uiClass   {@link ComponentUI} class
     * @return {@code ComponentUI} implementation instance for the specified component
     */
    @NotNull
    protected ComponentUI createUI ( @NotNull final C component, @NotNull final Class<? extends U> uiClass )
    {
        final ComponentUI ui;
        try
        {
            // Creating UI through common static method
            ui = ReflectUtils.callStaticMethod ( uiClass, "createUI", component );
        }
        catch ( final Exception e )
        {
            // We were unable to create new component UI
            throw new StyleException ( "Unable to instantiate UI instance: " + uiClass, e );
        }
        return ui;
    }

    @NotNull
    @Override
    public String toString ()
    {
        final Object[] data = { componentClass, uiClassId, baseUIClass, uiClass, defaultStyleId };
        final String parameters = TextUtils.arrayToString ( ", ", data );
        return getId () + "[" + parameters + "]";
    }
}