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

package com.alee.managers.style.data;

import com.alee.managers.log.Log;
import com.alee.managers.style.*;
import com.alee.painter.Painter;
import com.alee.utils.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Component style information class.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 */

@XStreamAlias ("style")
@XStreamConverter (ComponentStyleConverter.class)
public final class ComponentStyle implements Serializable, Cloneable
{
    /**
     * Base painter ID.
     */
    public static final String BASE_PAINTER_ID = "painter";

    /**
     * Style component type.
     * Refers to component type this style belongs to.
     */
    private StyleableComponent type;

    /**
     * Unique component style ID.
     * Default component style depends on component type.
     * Use {@link com.alee.managers.style.StyleableComponent#getDefaultStyleId()} to retrieve default style ID.
     */
    private String id;

    /**
     * Another component style ID which is extended by this style.
     * You can specify any existing style ID here to extend it.
     */
    private String extendsId;

    /**
     * Component settings.
     * Contains field-value pairs which will be applied to component fields.
     */
    private Map<String, Object> componentProperties;

    /**
     * Component UI settings.
     * Contains field-value pairs which will be applied to component UI fields.
     */
    private Map<String, Object> uiProperties;

    /**
     * Component painters settings.
     * Contains list of painter style information objects.
     */
    private List<PainterStyle> painters;

    /**
     * Nested styles.
     * Contains list of styles usually provided for child elements.
     */
    private List<ComponentStyle> styles;

    /**
     * Parent style.
     * This variable is only set in runtime for style usage convenience.
     */
    private transient ComponentStyle parent;

    /**
     * Constructs new component style information.
     */
    public ComponentStyle ()
    {
        super ();
    }

    /**
     * Returns supported component type.
     *
     * @return supported component type
     */
    public StyleableComponent getType ()
    {
        return type;
    }

    /**
     * Sets supported component type.
     *
     * @param type new supported component type
     * @return this style
     */
    public ComponentStyle setType ( final StyleableComponent type )
    {
        this.type = type;
        return this;
    }

    /**
     * Returns component style ID.
     *
     * @return component style ID
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Returns complete style ID.
     *
     * @return complete style ID
     */
    public String getCompleteId ()
    {
        return getParent () != null ? getParent ().getCompleteId () + StyleId.styleSeparator + getId () : getId ();
    }

    /**
     * Sets component style ID.
     *
     * @param id new component style ID
     * @return this style
     */
    public ComponentStyle setId ( final String id )
    {
        this.id = id;
        return this;
    }

    /**
     * Returns extended component style ID or null if none extended.
     *
     * @return extended component style ID or null if none extended
     */
    public String getExtendsId ()
    {
        return extendsId;
    }

    /**
     * Sets extended component style ID.
     * Set this to null in case you don't want to extend any style.
     *
     * @param id new extended component style ID
     * @return this style
     */
    public ComponentStyle setExtendsId ( final String id )
    {
        this.extendsId = id;
        return this;
    }

    /**
     * Returns component properties.
     *
     * @return component properties
     */
    public Map<String, Object> getComponentProperties ()
    {
        return componentProperties;
    }

    /**
     * Sets component properties.
     *
     * @param componentProperties new component properties
     * @return this style
     */
    public ComponentStyle setComponentProperties ( final Map<String, Object> componentProperties )
    {
        this.componentProperties = componentProperties;
        return this;
    }

    /**
     * Returns component UI properties.
     *
     * @return component UI properties
     */
    public Map<String, Object> getUIProperties ()
    {
        return uiProperties;
    }

    /**
     * Sets component UI properties
     *
     * @param uiProperties new component UI properties
     * @return this style
     */
    public ComponentStyle setUIProperties ( final Map<String, Object> uiProperties )
    {
        this.uiProperties = uiProperties;
        return this;
    }

    /**
     * Returns component painters.
     *
     * @return component painters
     */
    public List<PainterStyle> getPainters ()
    {
        return painters;
    }

    /**
     * Sets component painters.
     *
     * @param painters new component painters
     * @return this style
     */
    public ComponentStyle setPainters ( final List<PainterStyle> painters )
    {
        this.painters = painters;
        return this;
    }

    /**
     * Returns component base painter.
     *
     * @return component base painter
     */
    public PainterStyle getBasePainter ()
    {
        if ( painters.size () == 1 )
        {
            return painters.get ( 0 );
        }
        else
        {
            for ( final PainterStyle painter : painters )
            {
                if ( painter.isBase () )
                {
                    return painter;
                }
            }
            return null;
        }
    }

    /**
     * Returns nested styles list.
     *
     * @return nested styles list
     */
    public List<ComponentStyle> getStyles ()
    {
        return styles;
    }

    /**
     * Returns nested styles count.
     *
     * @return nested styles count
     */
    public int getStylesCount ()
    {
        return getStyles () != null ? getStyles ().size () : 0;
    }

    /**
     * Sets nested styles list.
     *
     * @param styles nested styles list
     * @return this style
     */
    public ComponentStyle setStyles ( final List<ComponentStyle> styles )
    {
        this.styles = styles;
        return this;
    }

    /**
     * Returns parent component style.
     *
     * @return parent component style
     */
    public ComponentStyle getParent ()
    {
        return parent;
    }

    /**
     * Sets parent component style.
     *
     * @param parent parent component style
     * @return this style
     */
    public ComponentStyle setParent ( final ComponentStyle parent )
    {
        this.parent = parent;
        return this;
    }

    /**
     * Applies this style to the specified component.
     * Returns whether style was successfully applied or not.
     *
     * @param component component to apply style to
     * @return true if style was applied, false otherwise
     */
    public boolean apply ( final JComponent component )
    {
        try
        {
            final ComponentUI ui = getComponentUIImpl ( component );

            // Installing painters
            for ( final PainterStyle painterStyle : getPainters () )
            {
                installPainter ( ui, component, true, painterStyle );
            }

            // Applying UI properties
            applyProperties ( ui, appendEmptyUIProperties ( ui, getUIProperties () ) );

            // Applying component properties
            applyProperties ( component, getComponentProperties () );

            return true;
        }
        catch ( final Throwable e )
        {
            Log.error ( this, e );
            return false;
        }
    }

    /**
     * Installs painter into specified object based on provided painter style.
     *
     * @param object       object to install painter into
     * @param component    component painter is installed for
     * @param customizable whether or not this painter customizable through {@link com.alee.managers.style.StyleManager}
     * @param painterStyle painter style
     * @throws NoSuchFieldException      if painter could not be set into object
     * @throws NoSuchMethodException     if painter setter method could not be found
     * @throws InvocationTargetException if painter setter method invocation failed
     * @throws IllegalAccessException    if painter setter method is not accessible
     */
    protected void installPainter ( final Object object, final JComponent component, final boolean customizable,
                                    final PainterStyle painterStyle )
            throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        // Retrieving painter to install into component
        // Custom painter can be null - that will just mean that component should not have painter installed
        final Painter painter;
        final Map<String, Painter> customPainters = customizable ? StyleManager.getCustomPainters ( component ) : null;
        if ( customPainters != null && customPainters.containsKey ( painterStyle.getId () ) )
        {
            // Using custom painter provided in the application code
            // This painter is set through API provided by {@link com.alee.painter.Paintable} interface
            painter = customPainters.get ( painterStyle.getId () );
        }
        else
        {
            // Creating painter instance
            // Be aware that all painters must have default constructor
            // todo Only reapply settings if painter already exists?
            final String painterClass = painterStyle.getPainterClass ();
            painter = ReflectUtils.createInstanceSafely ( painterClass );
            if ( painter == null )
            {
                final String msg = "Unable to create painter \"%s\" for component \"%s\" in style \"%s\"";
                final String componentType = component != null ? component.toString () : "none";
                throw new StyleException ( String.format ( msg, painterClass, componentType, getId () ) );
            }

            // Applying painter properties
            // These properties are applied only for style-provided painters
            applyProperties ( painter, painterStyle.getProperties () );
        }

        // Installing painter into the UI
        // todo Update component instead of reinstalling painter if it is the same?
        setFieldValue ( object, painterStyle.getId (), painter );
    }

    /**
     * Applies properties to specified object fields.
     *
     * @param object         object instance
     * @param skinProperties skin properties to apply, these properties come from the skin
     * @throws NoSuchFieldException      if painter could not be set into object
     * @throws NoSuchMethodException     if painter setter method could not be found
     * @throws InvocationTargetException if painter setter method invocation failed
     * @throws IllegalAccessException    if painter setter method is not accessible
     */
    private void applyProperties ( final Object object, final Map<String, Object> skinProperties )
            throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        // Applying merged properties
        if ( skinProperties != null && skinProperties.size () > 0 )
        {
            for ( final Map.Entry<String, Object> entry : skinProperties.entrySet () )
            {
                final Object value = entry.getValue ();
                if ( value instanceof PainterStyle )
                {
                    // PainterStyle is handled differently
                    final PainterStyle style = ( PainterStyle ) value;
                    style.setId ( entry.getKey () );
                    installPainter ( object, null, false, style );
                }
                else
                {
                    // Other fields are simply set through common means
                    setFieldValue ( object, entry.getKey (), value );
                }
            }
        }
    }

    /**
     * Appends empty property values if required.
     *
     * @param ui           component UI
     * @param uiProperties properties
     * @return modified properties map
     */
    protected Map<String, Object> appendEmptyUIProperties ( final ComponentUI ui, final Map<String, Object> uiProperties )
    {
        if ( ui instanceof MarginSupport && !uiProperties.containsKey ( ComponentStyleConverter.MARGIN_ATTRIBUTE ) )
        {
            uiProperties.put ( ComponentStyleConverter.MARGIN_ATTRIBUTE, new Insets ( 0, 0, 0, 0 ) );
        }
        if ( ui instanceof PaddingSupport && !uiProperties.containsKey ( ComponentStyleConverter.PADDING_ATTRIBUTE ) )
        {
            uiProperties.put ( ComponentStyleConverter.PADDING_ATTRIBUTE, new Insets ( 0, 0, 0, 0 ) );
        }
        return uiProperties;
    }

    /**
     * Removes this style from the specified component.
     *
     * @param component component to remove style from
     * @return true if style was successfully removed, false otherwise
     */
    public boolean remove ( final JComponent component )
    {
        try
        {
            final ComponentUI ui = getComponentUIImpl ( component );

            // Uninstalling skin painters from the UI
            for ( final PainterStyle painterStyle : getPainters () )
            {
                final String setterMethod = ReflectUtils.getSetterMethodName ( painterStyle.getId () );
                ReflectUtils.callMethod ( ui, setterMethod, ( Painter ) null );
            }

            return true;
        }
        catch ( final Throwable e )
        {
            Log.error ( this, e );
            return false;
        }
    }

    /**
     * Applies specified value to object field.
     * This method allows to access and modify even private object fields.
     * Note that this method might also work even if there is no real field with the specified name but there is fitting setter method.
     *
     * @param object object instance
     * @param field  object field
     * @param value  field value
     * @return true if value was applied successfully, false otherwise
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
     */
    private boolean setFieldValue ( final Object object, final String field, final Object value )
            throws InvocationTargetException, IllegalAccessException
    {
        // Skip ignored values
        if ( value == IgnoredValue.VALUE )
        {
            return false;
        }

        // todo Still need to check method here? Throw exceptions?
        // Trying to use setter method to apply the specified value
        try
        {
            final String setterMethod = ReflectUtils.getSetterMethodName ( field );
            ReflectUtils.callMethod ( object, setterMethod, value );
            return true;
        }
        catch ( final NoSuchMethodException e )
        {
            // Ignoring this error and proceeding to direct field access
        }

        // Applying field value directly
        try
        {
            final Field actualField = ReflectUtils.getField ( object.getClass (), field );
            actualField.set ( object, value );
            return true;
        }
        catch ( final NoSuchFieldException e )
        {
            Log.error ( ComponentStyle.class, e );
            return false;
        }
    }

    /**
     * Returns component UI object.
     *
     * @param component component instance
     * @return component UI object
     */
    private ComponentUI getComponentUIImpl ( final JComponent component )
    {
        final ComponentUI ui = LafUtils.getUI ( component );
        if ( ui == null )
        {
            throw new StyleException ( "Unable to retrieve UI from component: " + component );
        }
        return ui;
    }

    /**
     * Returns actual painter used within specified component.
     *
     * @param component component to retrieve painter from
     * @param <T>       painter type
     * @return actual painter used within specified component
     */
    public <T extends Painter> T getPainter ( final JComponent component )
    {
        return getPainter ( component, BASE_PAINTER_ID );
    }

    /**
     * Returns actual painter used within specified component.
     *
     * @param component component to retrieve painter from
     * @param painterId painter ID
     * @param <T>       painter type
     * @return actual painter used within specified component
     */
    public <T extends Painter> T getPainter ( final JComponent component, final String painterId )
    {
        final String pid = painterId != null ? painterId : getBasePainter ().getId ();
        final ComponentUI ui = getComponentUIImpl ( component );
        return getFieldValue ( ui, pid );
    }

    /**
     * Returns object field value.
     * This method allows to access even private object fields.
     * Note that this method might also work even if there is no real field with the specified name but there is fitting getter method.
     *
     * @param object object instance
     * @param field  object field
     * @param <T>    value type
     * @return field value for the specified object or null
     */
    public static <T> T getFieldValue ( final Object object, final String field )
    {
        final Class<?> objectClass = object.getClass ();

        // Trying to use getter method to retrieve value
        // Note that this method might work even if there is no real field with the specified name but there is fitting getter method
        // This was made to improve call speed (no real field check) and avoid accessing field directly (in most of cases)
        try
        {
            final Method getter = ReflectUtils.getFieldGetter ( object, field );
            return ( T ) getter.invoke ( object );
        }
        catch ( final InvocationTargetException e )
        {
            Log.error ( ComponentStyle.class, e );
        }
        catch ( final IllegalAccessException e )
        {
            Log.error ( ComponentStyle.class, e );
        }

        // Retrieving field value directly
        // This one is rarely used and in most of times will be called when inappropriate property is set
        try
        {
            final Field actualField = ReflectUtils.getField ( objectClass, field );
            return ( T ) actualField.get ( object );
        }
        catch ( final NoSuchFieldException e )
        {
            Log.error ( ComponentStyle.class, e );
            return null;
        }
        catch ( final IllegalAccessException e )
        {
            Log.error ( ComponentStyle.class, e );
            return null;
        }
    }

    /**
     * Merges specified style on top of this style.
     *
     * @param style style to merge on top of this one
     * @return merge result
     */
    public ComponentStyle merge ( final ComponentStyle style )
    {
        // Applying new parent
        setParent ( style.getParent () );

        // Applying style ID from the merged style
        setId ( style.getId () );

        // Applying extended ID from the merged style
        setExtendsId ( style.getExtendsId () );

        // Apply style settings as in case it was extended
        // The same mechanism is used because this is basically an extension of existing style
        extend ( style );

        // Returns merge result
        return this;
    }

    /**
     * Applies specified style settings on top of this style settings.
     *
     * @param style style to merge on top of this one
     * @return current style
     */
    private ComponentStyle extend ( final ComponentStyle style )
    {
        // Copying settings from extended style
        mergeProperties ( getComponentProperties (), style.getComponentProperties () );
        mergeProperties ( getUIProperties (), style.getUIProperties () );
        mergePainters ( this, style );

        // Merging nested styles
        // In case there are no merged styles we don't need to do anything
        final int nestedCount = getStylesCount ();
        final int mergedCount = style.getStylesCount ();
        if ( nestedCount > 0 && mergedCount > 0 )
        {
            // Inherits items that have a parent in a new element, but not in the current
            for ( final ComponentStyle child : getStyles () )
            {
                extendChild ( style, child );
            }

            // Merge styles
            final List<ComponentStyle> nestedStyles = getStyles ();
            for ( final ComponentStyle mergedNestedStyle : style.getStyles () )
            {
                // Looking for existing style with the same ID and type
                ComponentStyle existing = null;
                for ( final ComponentStyle nestedStyle : getStyles () )
                {
                    if ( mergedNestedStyle.getType () == nestedStyle.getType () &&
                            CompareUtils.equals ( mergedNestedStyle.getId (), nestedStyle.getId () ) )
                    {
                        existing = nestedStyle;
                        break;
                    }
                }

                // Either merging style with existing one or simply saving clone
                if ( existing != null )
                {
                    // Merging existing nested style and moving it to the end
                    nestedStyles.remove ( existing );
                    existing.extend ( mergedNestedStyle );
                    nestedStyles.add ( existing );
                }
                else
                {
                    // Simply using merged style clone
                    final ComponentStyle mergedNestedStyleClone = mergedNestedStyle.clone ();
                    mergedNestedStyleClone.setParent ( ComponentStyle.this );
                    nestedStyles.add ( mergedNestedStyleClone );
                }
            }
            setStyles ( nestedStyles );
        }
        else if ( mergedCount > 0 )
        {
            // Simply set merged styles
            final List<ComponentStyle> mergedStylesClone = MergeUtils.clone ( style.getStyles () );
            for ( final ComponentStyle mergedStyleClone : mergedStylesClone )
            {
                mergedStyleClone.setParent ( this );
            }
            setStyles ( mergedStylesClone );
        }
        else if ( nestedCount > 0 )
        {
            // Simply set base styles
            final List<ComponentStyle> baseStylesClone = MergeUtils.clone ( getStyles () );
            for ( final ComponentStyle baseStyleClone : baseStylesClone )
            {
                baseStyleClone.setParent ( this );
            }
            setStyles ( baseStylesClone );
        }
        return this;
    }

    /**
     * Inherits items that have a parent in a new element, but not in the current
     *
     * @param style component style that is merged on top of this one
     * @param child this component style child
     */
    private void extendChild ( final ComponentStyle style, final ComponentStyle child )
    {
        // Skipping styles that are already provided in style merged on top of this
        for ( final ComponentStyle newParentChild : style.getStyles () )
        {
            if ( child.getId ().equals ( newParentChild.getId () ) )
            {
                return;
            }
        }

        // Overriding provided style with this style child
        for ( final ComponentStyle mergedChild : style.getStyles () )
        {
            if ( CompareUtils.equals ( child.getExtendsId (), mergedChild.getId () ) )
            {
                style.getStyles ().add ( child.clone ().extend ( mergedChild ) );
                return;
            }
        }
    }

    /**
     * Performs painter settings copy from extended style.
     *
     * @param merged style
     * @param style  extended style
     */
    private void mergePainters ( final ComponentStyle style, final ComponentStyle merged )
    {
        // Converting painters into maps
        final Map<String, PainterStyle> stylePainters = collectPainters ( style, style.getPainters () );
        final Map<String, PainterStyle> mergedPainters = collectPainters ( merged, merged.getPainters () );

        // Copying proper painters data
        for ( final Map.Entry<String, PainterStyle> entry : stylePainters.entrySet () )
        {
            final String painterId = entry.getKey ();
            final PainterStyle stylePainter = entry.getValue ();
            if ( mergedPainters.containsKey ( painterId ) )
            {
                // Copying painter properties if extended painter class type is assignable from current painter class type
                final PainterStyle mergedPainter = mergedPainters.get ( painterId );
                final Class painterClass = ReflectUtils.getClassSafely ( mergedPainter.getPainterClass () );
                final Class extendedPainterClass = ReflectUtils.getClassSafely ( stylePainter.getPainterClass () );
                if ( painterClass == null || extendedPainterClass == null )
                {
                    final String pc = painterClass == null ? mergedPainter.getPainterClass () : stylePainter.getPainterClass ();
                    final String sid = merged.getType () + ":" + merged.getId ();
                    throw new StyleException ( "Component style \"" + sid + "\" points to missing painter class: \"" + pc + "\"" );
                }
                if ( ReflectUtils.isAssignable ( extendedPainterClass, painterClass ) )
                {
                    // Adding painter based on extended one and merged
                    stylePainter.setBase ( mergedPainter.isBase () );
                    stylePainter.setPainterClass ( mergedPainter.getPainterClass () );
                    mergeProperties ( stylePainter.getProperties (), mergedPainter.getProperties () );
                    mergedPainters.put ( painterId, stylePainter );
                }
                else
                {
                    // Adding painter fully based on merged painter
                    mergedPainters.put ( painterId, mergedPainter.clone () );
                }
            }
            else
            {
                // todo Base painters might clash as the result
                // Adding a full copy of base painter style
                mergedPainters.put ( painterId, stylePainter.clone () );
            }
        }

        // Fixing possible base mark issues
        if ( mergedPainters.size () > 0 )
        {
            boolean hasBase = false;
            for ( final Map.Entry<String, PainterStyle> entry : mergedPainters.entrySet () )
            {
                final PainterStyle painterStyle = entry.getValue ();
                if ( painterStyle.isBase () )
                {
                    hasBase = true;
                    break;
                }
            }
            if ( !hasBase )
            {
                PainterStyle painterStyle = mergedPainters.get ( BASE_PAINTER_ID );
                if ( painterStyle == null )
                {
                    painterStyle = mergedPainters.entrySet ().iterator ().next ().getValue ();
                }
                painterStyle.setBase ( true );
            }
        }

        // Updating painters list
        style.setPainters ( new ArrayList<PainterStyle> ( mergedPainters.values () ) );
    }

    /**
     * Performs properties merge.
     *
     * @param properties base properties
     * @param merged     merged properties
     */
    private void mergeProperties ( final Map<String, Object> properties, final Map<String, Object> merged )
    {
        for ( final Map.Entry<String, Object> property : merged.entrySet () )
        {
            final String key = property.getKey ();
            final Object e = properties.get ( key );
            final Object m = property.getValue ();

            try
            {
                // Cloning existing property value properly
                final Object existing = MergeUtils.clone ( e );

                // Merging another one on top of it
                final Object result = MergeUtils.merge ( existing, m );

                // Saving merge result
                properties.put ( key, result );
            }
            catch ( final Throwable ex )
            {
                Log.get ().error ( "Unable to merge property \"" + key + "\" values: " + e + " and " + m, ex );
            }
        }
    }

    /**
     * Collects all specified painters into map by their IDs.
     *
     * @param style    component style
     * @param painters painters list
     * @return map of painters by their IDs
     */
    private Map<String, PainterStyle> collectPainters ( final ComponentStyle style, final List<PainterStyle> painters )
    {
        final Map<String, PainterStyle> paintersMap = new LinkedHashMap<String, PainterStyle> ( painters.size () );
        for ( final PainterStyle painter : painters )
        {
            final String painterId = painter.getId ();
            if ( paintersMap.containsKey ( painterId ) )
            {
                final String sid = style.getType () + ":" + style.getId ();
                throw new StyleException ( "Component style \"" + sid + "\" has duplicate painters for id \"" + painterId + "\"" );
            }
            paintersMap.put ( painterId, painter );
        }
        return paintersMap;
    }

    /**
     * Returns cloned instance of this component style.
     *
     * @return cloned instance of this component style
     */
    @Override
    public ComponentStyle clone ()
    {
        // Creating style clone
        final ComponentStyle clone = MergeUtils.cloneByFieldsSafely ( this );

        // Updating transient parent field
        clone.setParent ( getParent () );

        // Updating transient parent field for children to cloned one
        if ( !CollectionUtils.isEmpty ( clone.getStyles () ) )
        {
            for ( final ComponentStyle style : clone.getStyles () )
            {
                style.setParent ( clone );
            }
        }

        return clone;
    }

    @Override
    public String toString ()
    {
        return getType () + ":" + getCompleteId ();
    }
}