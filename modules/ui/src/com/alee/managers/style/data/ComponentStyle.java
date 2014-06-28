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

import com.alee.managers.style.SupportedComponent;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;
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
public final class ComponentStyle implements Serializable
{
    /**
     * Style component type.
     * Refers to component type this style belongs to.
     */
    private SupportedComponent type;

    /**
     * Unique component style ID.
     * Default component style always has "default" ID.
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
     * Contains list of painter style infromation objects.
     */
    private List<PainterStyle> painters;

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
    public SupportedComponent getType ()
    {
        return type;
    }

    /**
     * Sets supported component type.
     *
     * @param type new supported component type
     */
    public void setType ( final SupportedComponent type )
    {
        this.type = type;
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
    public Map<String, Object> getComponentProperties ()
    {
        return componentProperties;
    }

    /**
     * Sets component properties.
     *
     * @param componentProperties new component properties
     */
    public void setComponentProperties ( final Map<String, Object> componentProperties )
    {
        this.componentProperties = componentProperties;
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
     */
    public void setUIProperties ( final Map<String, Object> uiProperties )
    {
        this.uiProperties = uiProperties;
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
     */
    public void setPainters ( final List<PainterStyle> painters )
    {
        this.painters = painters;
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
}