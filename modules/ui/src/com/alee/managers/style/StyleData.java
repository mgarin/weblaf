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

import com.alee.extended.painter.Painter;
import com.alee.managers.style.skin.Skin;
import com.alee.managers.style.skin.SkinListener;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private Skin skin;

    /**
     * Style ID.
     */
    private StyleId styleId;

    /**
     * Custom painters.
     */
    private Map<String, Painter> painters;

    /**
     * Related style children.
     */
    private List<WeakReference<JComponent>> children;

    /**
     * Skin change listeners.
     */
    private List<SkinListener> listeners;

    /**
     * Constructs new empty style data object.
     */
    public StyleData ()
    {
        super ();
        this.skin = null;
        this.styleId = null;
        this.painters = null;
        this.children = null;
        this.listeners = null;
    }

    /**
     * Returns currently applied skin.
     *
     * @return currently applied skin
     */
    public Skin getSkin ()
    {
        return skin;
    }

    /**
     * Replaces component skin and returns previously applied skin.
     *
     * @param component      component to change skin for
     * @param skin           new component skin
     * @param updateChildren whether or not should update children styles
     * @return previously applied skin
     */
    public Skin changeSkin ( final JComponent component, final Skin skin, final boolean updateChildren )
    {
        // Removing old skin
        final Skin oldSkin = removeSkin ( component );

        // Applying new skin
        if ( skin != null )
        {
            // Applying skin to specified component
            skin.applySkin ( component );
            this.skin = skin;

            // Applying skin to component's style children
            if ( updateChildren && children != null )
            {
                for ( final WeakReference<JComponent> reference : children )
                {
                    final JComponent child = reference.get ();
                    if ( child != null )
                    {
                        StyleManager.setSkin ( child, skin );
                    }
                }
            }
        }

        // Informing about skin changes
        fireSkinChanged ( oldSkin, skin );

        return oldSkin;
    }

    /**
     * Reapplies currently used skin to force visual updates.
     *
     * @param component component to reapply skin for
     */
    public void reapplySkin ( final JComponent component )
    {
        changeSkin ( component, skin, true );
    }

    /**
     * Removes skin currently applied to the specified component.
     *
     * @param component component to remove skin for
     * @return previously applied skin
     */
    public Skin removeSkin ( final JComponent component )
    {
        final Skin oldSkin = this.skin;
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
     * Returns custom painters.
     *
     * @return custom painters
     */
    public Map<String, Painter> getPainters ()
    {
        return painters;
    }

    /**
     * Sets custom painters.
     *
     * @param painters custom painters
     */
    public void setPainters ( final Map<String, Painter> painters )
    {
        this.painters = painters;
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
        if ( children == null )
        {
            children = new ArrayList<WeakReference<JComponent>> ( 1 );
        }
        children.add ( new WeakReference<JComponent> ( child ) );
    }

    /**
     * Removes related style child.
     *
     * @param child related style child
     */
    public void removeChild ( final JComponent child )
    {
        final Iterator<WeakReference<JComponent>> iterator = children.iterator ();
        while ( iterator.hasNext () )
        {
            final WeakReference<JComponent> next = iterator.next ();
            if ( next.get () == child )
            {
                iterator.remove ();
            }
        }
    }

    /**
     * Adds skin change listener.
     *
     * @param listener skin change listener to add
     */
    public void addListener ( final SkinListener listener )
    {
        if ( listeners == null )
        {
            listeners = new ArrayList<SkinListener> ( 1 );
        }
        listeners.add ( listener );
    }

    /**
     * Removes skin change listener.
     *
     * @param listener skin change listener to remove
     */
    public void removeListener ( final SkinListener listener )
    {
        if ( listeners != null )
        {
            listeners.remove ( listener );
        }
    }

    /**
     * Informs about component skin changes.
     *
     * @param oldSkin previously used skin
     * @param newSkin currently used skin
     */
    public void fireSkinChanged ( final Skin oldSkin, final Skin newSkin )
    {
        if ( listeners != null )
        {
            for ( final SkinListener listener : listeners )
            {
                listener.skinChanged ( oldSkin, newSkin );
            }
        }
    }
}