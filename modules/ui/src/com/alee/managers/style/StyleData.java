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

import com.alee.managers.style.skin.AbstractSkin;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * This object contains style data for single component instance.
 * This is basically all {@link com.alee.managers.style.StyleManager} knows about the component styling.
 *
 * @author Mikle Garin
 */

public final class StyleData
{
    /**
     * Applied skin.
     */
    private AbstractSkin skin;

    /**
     * Style ID.
     */
    private StyleId styleId;

    /**
     * Related style children.
     */
    private final List<WeakReference<JComponent>> children;

    /**
     * Constructs new empty style data object.
     */
    public StyleData ()
    {
        super ();
        this.skin = null;
        this.styleId = null;
        this.children = new ArrayList<WeakReference<JComponent>> ( 1 );
    }

    /**
     * Returns currently applied skin.
     *
     * @return currently applied skin
     */
    public AbstractSkin getSkin ()
    {
        return skin;
    }

    /**
     * Replaces component skin and returns previously applied skin.
     *
     * @param component component to change skin for
     * @param skin      new component skin
     * @return previously applied skin
     */
    public AbstractSkin changeSkin ( final JComponent component, final AbstractSkin skin )
    {
        // Removing old skin
        final AbstractSkin oldSkin = removeSkin ( component );

        // Applying new skin
        if ( skin != null )
        {
            // Applying skin to specified component
            skin.applySkin ( component );
            this.skin = skin;

            // Applying skin to component's style children
            for ( final WeakReference<JComponent> reference : children )
            {
                final JComponent child = reference.get ();
                if ( child != null )
                {
                    StyleManager.applySkin ( child, skin );
                }
            }
        }

        return oldSkin;
    }

    /**
     * Reapplies currently used skin to force visual updates.
     *
     * @param component component to reapply skin for
     */
    public void reapplySkin ( final JComponent component )
    {
        changeSkin ( component, skin );
    }

    /**
     * Removes skin currently applied to the specified component.
     *
     * @param component component to remove skin for
     * @return previously applied skin
     */
    public AbstractSkin removeSkin ( final JComponent component )
    {
        final AbstractSkin oldSkin = this.skin;
        if ( this.skin != null )
        {
            this.skin.removeSkin ( component );
            this.skin = null;

            // todo Remove skin from children?
        }
        return oldSkin;
    }

    /**
     * Returns currently used style ID.
     *
     * @return currently used style ID
     */
    public StyleId getStyleId ()
    {
        return styleId;
    }

    /**
     * Sets currently used style ID.
     *
     * @param component component to change style ID for
     * @param styleId   new style ID
     */
    public void setStyleId ( final JComponent component, final StyleId styleId )
    {
        if ( !CompareUtils.equals ( this.styleId, styleId ) )
        {
            this.styleId = styleId;
            reapplySkin ( component );
        }
    }

    /**
     * Returns related style children.
     *
     * @return related style children
     */
    public List<WeakReference<JComponent>> getChildren ()
    {
        return children;
    }

    /**
     * Adds related style child.
     *
     * @param child related style child
     */
    public void addChild ( final JComponent child )
    {
        children.add ( new WeakReference<JComponent> ( child ) );
    }

    /**
     * Removes related style child.
     *
     * @param child related style child
     */
    public void removeChild ( final JComponent child )
    {
        children.add ( new WeakReference<JComponent> ( child ) );
    }
}