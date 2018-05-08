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

import com.alee.api.clone.Clone;
import com.alee.api.clone.CloneBehavior;
import com.alee.api.clone.RecursiveClone;
import com.alee.api.clone.behavior.PreserveOnClone;
import com.alee.api.jdk.Objects;
import com.alee.api.merge.Merge;
import com.alee.managers.style.*;
import com.alee.painter.Painter;
import com.alee.utils.CollectionUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.ReflectUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Component style information class.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see StyleManager
 */
@XStreamAlias ( "style" )
@XStreamConverter ( ComponentStyleConverter.class )
public final class ComponentStyle implements CloneBehavior<ComponentStyle>, Serializable
{
    /**
     * Component painter identifier.
     */
    public static final String COMPONENT_PAINTER_ID = "painter";

    /**
     * Style component type.
     * Refers to identifier of a component this style belongs to.
     *
     * @see ComponentDescriptor#getId()
     */
    @XStreamAsAttribute
    private String type;

    /**
     * Unique component style identifier.
     * Default component style depends on component type and might also depend on the instance.
     *
     * @see ComponentDescriptor#getDefaultStyleId()
     * @see ComponentDescriptor#getDefaultStyleId(JComponent)
     * @see StyleId#getDefault(JComponent)
     * @see StyleId#getDefault(Window)
     */
    @XStreamAsAttribute
    private String id;

    /**
     * Different style identifier which is extended by this style.
     * Any existing style identifier of the same component type can be used here, though extended style must be defined first.
     */
    @XStreamAsAttribute
    private String extendsId;

    /**
     * Component's settings.
     * Contains field-value pairs which will be applied to component fields.
     */
    private LinkedHashMap<String, Object> componentProperties;

    /**
     * Component's UI settings.
     * Contains field-value pairs which will be applied to component UI fields.
     */
    private LinkedHashMap<String, Object> uiProperties;

    /**
     * {@link PainterStyle} for component's {@link Painter}.
     *
     * @see PainterStyle
     */
    private PainterStyle painterStyle;

    /**
     * {@link List} of nested {@link ComponentStyle}s.
     */
    private List<ComponentStyle> nestedStyles;

    /**
     * Parent {@link ComponentStyle}.
     * This variable is only set in runtime for style usage convenience.
     */
    @PreserveOnClone
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
    public String getType ()
    {
        return type;
    }

    /**
     * Sets supported component type.
     *
     * @param type new supported component type
     * @return this style
     */
    public ComponentStyle setType ( final String type )
    {
        this.type = type;
        return this;
    }

    /**
     * Returns component style identifier.
     *
     * @return component style identifier
     */
    public String getId ()
    {
        return id;
    }

    /**
     * Returns complete style identifier.
     * Not that it will also include types of each of the parents.
     *
     * @return complete style identifier
     * @see StyleId#getCompleteId()
     */
    public String getCompleteId ()
    {
        final ComponentStyle parentStyle = getParent ();
        return parentStyle != null ? parentStyle.getPathId () + StyleId.styleSeparator + getId () : getId ();
    }

    /**
     * Returns path for complete style ID.
     * Not that it will also include types of each of the parents.
     *
     * @return path for complete style ID
     * @see StyleId#getPathId(JComponent)
     */
    private String getPathId ()
    {
        // Full identifier for this part of the path
        final String fullId = getType () + ":" + getId ();

        // Combined identifiers path
        final ComponentStyle parentStyle = getParent ();
        return parentStyle != null ? parentStyle.getPathId () + StyleId.styleSeparator + fullId : fullId;
    }

    /**
     * Sets component style ID.
     *
     * @param id new component style ID
     */
    public void setId ( final String id )
    {
        this.id = id;
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
     */
    public void setExtendsId ( final String id )
    {
        this.extendsId = id;
    }

    /**
     * Returns component properties.
     *
     * @return component properties
     */
    public LinkedHashMap<String, Object> getComponentProperties ()
    {
        return componentProperties;
    }

    /**
     * Sets component properties.
     *
     * @param componentProperties new component properties
     */
    public void setComponentProperties ( final LinkedHashMap<String, Object> componentProperties )
    {
        this.componentProperties = componentProperties;
    }

    /**
     * Returns component UI properties.
     *
     * @return component UI properties
     */
    public LinkedHashMap<String, Object> getUIProperties ()
    {
        return uiProperties;
    }

    /**
     * Sets component UI properties
     *
     * @param uiProperties new component UI properties
     */
    public void setUIProperties ( final LinkedHashMap<String, Object> uiProperties )
    {
        this.uiProperties = uiProperties;
    }

    /**
     * Returns component's {@link PainterStyle}.
     *
     * @return component's {@link PainterStyle}
     */
    public PainterStyle getPainterStyle ()
    {
        return painterStyle;
    }

    /**
     * Sets component's {@link PainterStyle}.
     *
     * @param painterStyle new component's {@link PainterStyle}
     */
    public void setPainterStyle ( final PainterStyle painterStyle )
    {
        this.painterStyle = painterStyle;
    }

    /**
     * Returns {@link List} of nested {@link ComponentStyle}s.
     *
     * @return {@link List} of nested {@link ComponentStyle}s
     */
    public List<ComponentStyle> getNestedStyles ()
    {
        return nestedStyles;
    }

    /**
     * Sets {@link List} of nested {@link ComponentStyle}s.
     *
     * @param nestedStyles new {@link List} of nested {@link ComponentStyle}s
     */
    public void setNestedStyles ( final List<ComponentStyle> nestedStyles )
    {
        this.nestedStyles = nestedStyles;
    }

    /**
     * Returns nested styles count.
     *
     * @return nested styles count
     */
    public int getStylesCount ()
    {
        return getNestedStyles () != null ? getNestedStyles ().size () : 0;
    }

    /**
     * Returns parent {@link ComponentStyle}.
     *
     * @return parent {@link ComponentStyle}
     */
    public ComponentStyle getParent ()
    {
        return parent;
    }

    /**
     * Sets parent {@link ComponentStyle}.
     *
     * @param parent parent {@link ComponentStyle}
     */
    public void setParent ( final ComponentStyle parent )
    {
        this.parent = parent;
    }

    /**
     * Applies this {@link ComponentStyle} to the specified {@link JComponent}.
     * Returns whether style was successfully applied or not.
     *
     * @param component {@link JComponent} to apply this {@link ComponentStyle} to
     * @return {@code true} if style was applied successfully, {@code false} otherwise
     */
    public boolean apply ( final JComponent component )
    {
        try
        {
            final ComponentUI ui = getComponentUI ( component );

            // Applying component properties
            applyProperties ( component, getComponentProperties () );

            // Applying UI properties
            applyProperties ( ui, appendEmptyUIProperties ( ui, getUIProperties () ) );

            // Installing painter
            installPainter ( ui, component, true, COMPONENT_PAINTER_ID, getPainterStyle () );

            return true;
        }
        catch ( final Exception e )
        {
            LoggerFactory.getLogger ( ComponentStyle.class ).error ( e.toString (), e );
            return false;
        }
    }

    /**
     * Installs {@link Painter} based on {@link PainterStyle} into specified object based on provided painter style.
     *
     * @param object       object to install painter into
     * @param component    component painter is installed for
     * @param customizable whether or not this painter customizable through {@link StyleManager}
     * @param painterId    {@link Painter} identifier
     * @param painterStyle {@link PainterStyle}
     * @throws NoSuchFieldException      if painter could not be set into object
     * @throws NoSuchMethodException     if painter setter method could not be found
     * @throws InvocationTargetException if painter setter method invocation failed
     * @throws IllegalAccessException    if painter setter method is not accessible
     */
    protected void installPainter ( final Object object, final JComponent component, final boolean customizable,
                                    final String painterId, final PainterStyle painterStyle )
            throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException
    {
        // Retrieving painter to install into component
        // There could be a custom painter present which will take over one offered by painter style
        final Painter painter;
        final Painter customPainter = customizable ? StyleManager.getCustomPainter ( component ) : null;
        if ( customPainter != null )
        {
            // Using custom painter provided in the application code
            // This painter is set through API provided by {@link com.alee.painter.Paintable} interface
            painter = customPainter;
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
                final String componentType = component != null ? component.toString () : "none";
                final String msg = "Unable to create painter '%s' for component '%s' in style '%s'";
                throw new StyleException ( String.format ( msg, painterClass, componentType, getId () ) );
            }

            // Applying painter properties
            // These properties are applied only for style-provided painters
            applyProperties ( painter, painterStyle.getProperties () );
        }

        // Installing painter into the UI
        // todo Update component instead of reinstalling painter if it is the same?
        setFieldValue ( object, painterId, painter );
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
                    installPainter ( object, null, false, entry.getKey (), style );
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
     * Removes this {@link ComponentStyle} from the specified {@link JComponent}.
     *
     * @param component {@link JComponent} to remove this {@link ComponentStyle} from
     * @return {@code true} if style was successfully removed, {@code false} otherwise
     */
    public boolean remove ( final JComponent component )
    {
        try
        {
            // Uninstalling skin painters from the UI
            final ComponentUI ui = getComponentUI ( component );
            final String setterMethod = ReflectUtils.getSetterMethodName ( COMPONENT_PAINTER_ID );
            ReflectUtils.callMethod ( ui, setterMethod, ( Painter ) null );
            return true;
        }
        catch ( final Exception e )
        {
            LoggerFactory.getLogger ( ComponentStyle.class ).error ( e.toString (), e );
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
     * @throws java.lang.reflect.InvocationTargetException if method throws an exception
     * @throws java.lang.IllegalAccessException            if method is inaccessible
     */
    private void setFieldValue ( final Object object, final String field, final Object value )
            throws InvocationTargetException, IllegalAccessException
    {
        // Skipping value if it is marked as ignored
        if ( value == IgnoredValue.VALUE )
        {
            return;
        }

        // Creating separate usable value to avoid source object modifications
        // We have limited options here, so for now we simply clone objects which are defined as Cloneable
        final Object usable;
        if ( value instanceof Painter )
        {
            usable = value;
        }
        else
        {
            try
            {
                usable = Clone.deep ().clone ( value );
            }
            catch ( final Exception e )
            {
                final String msg = "Unable to clone value: %s";
                throw new StyleException ( String.format ( msg, value ), e );
            }
        }

        try
        {
            // todo Add more options on the method names here?
            // Trying to use setter method to apply the specified value
            final String setterMethod = ReflectUtils.getSetterMethodName ( field );
            ReflectUtils.callMethod ( object, setterMethod, usable );
        }
        catch ( final NoSuchMethodException e )
        {
            try
            {
                // Applying field value directly
                ReflectUtils.setFieldValue ( object, field, usable );
            }
            catch ( final Exception fe )
            {
                final String msg = "Unable to set `%s` object `%s` field value to: %s";
                throw new StyleException ( String.format ( msg, object, field, usable ), fe );
            }
        }
    }

    /**
     * Returns component UI object.
     *
     * @param component component instance
     * @return component UI object
     */
    private ComponentUI getComponentUI ( final JComponent component )
    {
        final ComponentUI ui = LafUtils.getUI ( component );
        if ( ui == null )
        {
            final String msg = "Unable to retrieve UI from component '%s'";
            throw new StyleException ( String.format ( msg, component ) );
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
        final ComponentUI ui = getComponentUI ( component );
        return getFieldValue ( ui, COMPONENT_PAINTER_ID );
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
    private <T> T getFieldValue ( final Object object, final String field )
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
            LoggerFactory.getLogger ( ComponentStyle.class ).error ( e.toString (), e );
        }
        catch ( final IllegalAccessException e )
        {
            LoggerFactory.getLogger ( ComponentStyle.class ).error ( e.toString (), e );
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
            LoggerFactory.getLogger ( ComponentStyle.class ).error ( e.toString (), e );
            return null;
        }
        catch ( final IllegalAccessException e )
        {
            LoggerFactory.getLogger ( ComponentStyle.class ).error ( e.toString (), e );
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
            for ( final ComponentStyle child : getNestedStyles () )
            {
                extendChild ( style, child );
            }

            // Merge styles
            final List<ComponentStyle> nestedStyles = getNestedStyles ();
            for ( final ComponentStyle mergedNestedStyle : style.getNestedStyles () )
            {
                // Looking for existing style with the same ID and type
                ComponentStyle existing = null;
                for ( final ComponentStyle nestedStyle : getNestedStyles () )
                {
                    if ( Objects.equals ( mergedNestedStyle.getType (), nestedStyle.getType () ) &&
                            Objects.equals ( mergedNestedStyle.getId (), nestedStyle.getId () ) )
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
            setNestedStyles ( nestedStyles );
        }
        else if ( mergedCount > 0 )
        {
            // Simply set merged styles
            final List<ComponentStyle> mergedStylesClone = Clone.deep ().clone ( style.getNestedStyles () );
            for ( final ComponentStyle mergedStyleClone : mergedStylesClone )
            {
                mergedStyleClone.setParent ( this );
            }
            setNestedStyles ( mergedStylesClone );
        }
        else if ( nestedCount > 0 )
        {
            // Simply set base styles
            final List<ComponentStyle> baseStylesClone = Clone.deep ().clone ( getNestedStyles () );
            for ( final ComponentStyle baseStyleClone : baseStylesClone )
            {
                baseStyleClone.setParent ( this );
            }
            setNestedStyles ( baseStylesClone );
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
        for ( final ComponentStyle newParentChild : style.getNestedStyles () )
        {
            if ( child.getId ().equals ( newParentChild.getId () ) )
            {
                return;
            }
        }

        // Overriding provided style with this style child
        for ( final ComponentStyle mergedChild : style.getNestedStyles () )
        {
            if ( Objects.equals ( child.getExtendsId (), mergedChild.getId () ) )
            {
                style.getNestedStyles ().add ( child.clone ().extend ( mergedChild ) );
                return;
            }
        }
    }

    /**
     * Merges two {@link PainterStyle} settings together.
     *
     * @param style       extended {@link ComponentStyle}
     * @param mergedStyle merged {@link ComponentStyle}
     */
    private void mergePainters ( final ComponentStyle style, final ComponentStyle mergedStyle )
    {
        final PainterStyle resultPainterStyle;

        // Resolving resulting style
        final PainterStyle painterStyle = style.getPainterStyle ();
        final PainterStyle mergedPainterStyle = mergedStyle.getPainterStyle ();
        if ( painterStyle != null )
        {
            if ( mergedPainterStyle != null )
            {
                // Copying painter properties if extended painter class type is assignable from current painter class type
                final Class painterClass = ReflectUtils.getClassSafely ( mergedPainterStyle.getPainterClass () );
                final Class extendedPainterClass = ReflectUtils.getClassSafely ( painterStyle.getPainterClass () );
                if ( painterClass == null || extendedPainterClass == null )
                {
                    final String pc = painterClass == null ? mergedPainterStyle.getPainterClass () : painterStyle.getPainterClass ();
                    final String msg = "Component style '%s' points to a missing painter class: %s";
                    throw new StyleException ( String.format ( msg, mergedStyle, pc ) );
                }
                if ( ReflectUtils.isAssignable ( extendedPainterClass, painterClass ) )
                {
                    // Adding painter styles merge result
                    painterStyle.setPainterClass ( mergedPainterStyle.getPainterClass () );
                    mergeProperties ( painterStyle.getProperties (), mergedPainterStyle.getProperties () );
                    resultPainterStyle = painterStyle;
                }
                else
                {
                    // Adding a full copy of merged painter style
                    resultPainterStyle = Clone.deep ().clone ( mergedPainterStyle );
                }
            }
            else
            {
                // Adding a full copy of base painter style
                resultPainterStyle = Clone.deep ().clone ( painterStyle );
            }
        }
        else
        {
            if ( mergedPainterStyle != null )
            {
                // Adding a full copy of merged painter style
                resultPainterStyle = Clone.deep ().clone ( mergedPainterStyle );
            }
            else
            {
                // No painter style
                resultPainterStyle = null;
            }
        }

        // Updating painters list
        style.setPainterStyle ( resultPainterStyle );
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
            final Object existingValue = properties.get ( key );
            final Object mergedValue = property.getValue ();

            try
            {
                // Merging value on top of existing one
                final Object result = Merge.deep ().merge ( existingValue, mergedValue );

                // Saving merge result
                properties.put ( key, result );
            }
            catch ( final Exception ex )
            {
                final String msg = "Unable to merge property '%s' values: '%s' and '%s'";
                LoggerFactory.getLogger ( ComponentStyle.class ).error ( String.format ( msg, key, existingValue, mergedValue ), ex );
            }
        }
    }

    @Override
    public ComponentStyle clone ( final RecursiveClone clone, final int depth )
    {
        final ComponentStyle styleCopy = clone.cloneFields ( this, depth );

        /**
         * Updating transient parent field for child {@link ComponentStyle}s.
         * This is important since their parent have been cloned and needs to be updated.
         * Zero depth cloned object doesn't need parent to be updated, it is preserved upon clone.
         */
        if ( CollectionUtils.notEmpty ( styleCopy.getNestedStyles () ) )
        {
            for ( final ComponentStyle styleCopyChild : styleCopy.getNestedStyles () )
            {
                styleCopyChild.setParent ( styleCopy );
            }
        }

        return styleCopy;
    }

    @Override
    public ComponentStyle clone ()
    {
        return Clone.deep ().clone ( this );
    }

    @Override
    public String toString ()
    {
        return "ComponentStyle [ id: " + getCompleteId () + " ]";
    }
}