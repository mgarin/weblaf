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
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 * @see StyleManager#registerComponentDescriptor(ComponentDescriptor)
 * @see StyleManager#unregisterComponentDescriptor(ComponentDescriptor)
 * @see StyleManager#initializeDescriptors()
 */
public abstract class AbstractComponentDescriptor<C extends JComponent, U extends ComponentUI> implements ComponentDescriptor<C, U>
{
    /**
     * todo 1. Add specific painter class definition
     * todo 2. Add adaptive painter class definition
     * todo 3. Automate painters installation/uninstallation within UIs (in StyleManager.installSkin/uninstallSkin)
     */

    /**
     * Component icons cache.
     * {@link Icon}s are saved per component class.
     */
    protected static final Map<Class, Icon> componentIcons = new HashMap<Class, Icon> ();

    /**
     * Component identifier.
     */
    protected final String id;

    /**
     * Component class.
     */
    protected final Class<C> componentClass;

    /**
     * Component UI class identifier.
     */
    protected final String uiClassId;

    /**
     * Base UI class applicable to this component.
     */
    protected final Class<U> baseUIClass;

    /**
     * UI class applied to the component by default.
     */
    protected final Class<? extends U> uiClass;

    /**
     * Component default style ID.
     */
    protected final StyleId defaultStyleId;

    /**
     * Constructs new {@link AbstractComponentDescriptor}.
     *
     * @param id             component identifier
     * @param componentClass component class
     * @param uiClassId      component UI class ID
     * @param baseUIClass    base UI class applicable to this component
     * @param uiClass        UI class applied to the component by default
     * @param defaultStyleId component default style ID
     */
    public AbstractComponentDescriptor ( final String id, final Class<C> componentClass, final String uiClassId,
                                         final Class<U> baseUIClass, final Class<? extends U> uiClass, final StyleId defaultStyleId )
    {
        super ();
        this.id = id;
        this.componentClass = componentClass;
        this.uiClassId = uiClassId;
        this.baseUIClass = baseUIClass;
        this.uiClass = uiClass;
        this.defaultStyleId = defaultStyleId;
    }

    @Override
    public Class<C> getComponentClass ()
    {
        return componentClass;
    }

    @Override
    public String getUIClassId ()
    {
        return uiClassId;
    }

    @Override
    public Class<U> getBaseUIClass ()
    {
        return baseUIClass;
    }

    @Override
    public Class<? extends U> getUIClass ()
    {
        return uiClass;
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return defaultStyleId;
    }

    @Override
    public StyleId getDefaultStyleId ( final JComponent component )
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

    @Override
    public String getId ()
    {
        return getDefaultStyleId ().getId ();
    }

    @Override
    public Icon getIcon ()
    {
        final Class<C> key = getComponentClass ();
        if ( componentIcons.containsKey ( key ) )
        {
            return componentIcons.get ( key );
        }
        else
        {
            try
            {
                final ImageIcon icon = new ImageIcon ( getIconResource () );
                componentIcons.put ( key, icon );
                return icon;
            }
            catch ( final Exception e )
            {
                throw new StyleException ( "Unable to find component icon: " + key, e );
            }
        }
    }

    /**
     * Returns {@link Icon} resource.
     * We are simply using default style identifier as icon name for convenience.
     * It only works for WebLaF and Swing components as icons for all those components are predefined.
     * You can simply override this method for your custom component icons.
     *
     * @return {@link Icon} resource
     */
    protected URL getIconResource ()
    {
        final String path = "icons/styleable/" + getId () + ".png";
        return AbstractComponentDescriptor.class.getResource ( path );
    }

    @Override
    public String getTitle ()
    {
        return ReflectUtils.getClassName ( getComponentClass () );
    }

    @Override
    public void updateDefaults ( final UIDefaults table )
    {
        // Updating UI class mapping
        table.put ( getUIClassId (), getUIClass ().getName () );
    }

    @Override
    public void updateUI ( final C component )
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
    protected ComponentUI createUI ( final C component, final Class<? extends U> uiClass )
    {
        final ComponentUI ui;
        if ( uiClass != null )
        {
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
        }
        else
        {
            // Appropriate UI class was not provided
            throw new StyleException ( "Unable to instantiate UI instance for empty class" );
        }
        return ui;
    }

    @Override
    public String toString ()
    {
        final String parameters = TextUtils.arrayToString ( ", ", componentClass, uiClassId, baseUIClass, uiClass, defaultStyleId );
        return getId () + "[" + parameters + "]";
    }
}