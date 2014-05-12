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

    public SupportedComponent getType ()
    {
        return type;
    }

    public void setType ( final SupportedComponent type )
    {
        this.type = type;
    }

    public String getId ()
    {
        return id;
    }

    public void setId ( final String id )
    {
        this.id = id;
    }

    public String getExtendsId ()
    {
        return extendsId;
    }

    public void setExtendsId ( final String id )
    {
        this.extendsId = id;
    }

    public Map<String, Object> getComponentProperties ()
    {
        return componentProperties;
    }

    public void setComponentProperties ( final Map<String, Object> componentProperties )
    {
        this.componentProperties = componentProperties;
    }

    public Map<String, Object> getUIProperties ()
    {
        return uiProperties;
    }

    public void setUIProperties ( final Map<String, Object> uiProperties )
    {
        this.uiProperties = uiProperties;
    }

    public List<PainterStyle> getPainters ()
    {
        return painters;
    }

    public void setPainters ( final List<PainterStyle> painters )
    {
        this.painters = painters;
    }

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