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

import com.alee.managers.style.StyleException;
import com.alee.managers.style.StyleId;
import com.alee.managers.style.StyleManager;
import com.alee.managers.style.StyleableComponent;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CompareUtils;
import com.alee.utils.ReflectUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;
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

@XStreamAlias ( "style" )
@XStreamConverter ( ComponentStyleConverter.class )
public final class ComponentStyle implements Serializable, Cloneable
{
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
        return ( getParent () != null ? getParent ().getCompleteId () + StyleId.separator : "" ) + getId ();
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
     * Performs styles merge.
     *
     * @param style style to merge with this one
     */
    public void merge ( final ComponentStyle style )
    {
        // Applying new parent
        setParent ( style.getParent () );

        // Applying style ID from the merged style
        setId ( style.getId () );

        // Applying extended ID from the merged style
        setExtendsId ( style.getExtendsId () );

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
                    existing.merge ( mergedNestedStyle );
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
            final ArrayList<ComponentStyle> mergedStylesClone = CollectionUtils.clone ( style.getStyles () );
            for ( final ComponentStyle mergedStyleClone : mergedStylesClone )
            {
                mergedStyleClone.setParent ( this );
            }
            setStyles ( mergedStylesClone );
        }
        else if ( nestedCount > 0 )
        {
            // Simply set base styles
            final ArrayList<ComponentStyle> baseStylesClone = CollectionUtils.clone ( getStyles () );
            for ( final ComponentStyle baseStyleClone : baseStylesClone )
            {
                baseStyleClone.setParent ( this );
            }
            setStyles ( baseStylesClone );
        }
    }

    /**
     * Performs settings copy from extended style.
     *
     * @param properties base properties
     * @param merged     merged properties
     */
    protected void mergeProperties ( final Map<String, Object> properties, final Map<String, Object> merged )
    {
        for ( final Map.Entry<String, Object> property : merged.entrySet () )
        {
            properties.put ( property.getKey (), property.getValue () );
        }
    }

    /**
     * Performs painter settings copy from extended style.
     *
     * @param merged style
     * @param style  extended style
     */
    protected void mergePainters ( final ComponentStyle style, final ComponentStyle merged )
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
                PainterStyle painterStyle = mergedPainters.get ( StyleManager.DEFAULT_PAINTER_ID );
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
     * Collects all specified painters into map by their IDs.
     *
     * @param style    component style
     * @param painters painters list
     * @return map of painters by their IDs
     */
    protected Map<String, PainterStyle> collectPainters ( final ComponentStyle style, final List<PainterStyle> painters )
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
        final ComponentStyle clone = ReflectUtils.cloneByFieldsSafely ( this );

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

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        return getType () + ":" + getCompleteId ();
    }
}