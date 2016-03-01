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

package com.alee.extended.tree.sample;

import com.alee.api.IconSupport;
import com.alee.api.TitleSupport;
import com.alee.extended.tree.AsyncUniqueNode;
import com.alee.laf.tree.WebTreeUI;

import javax.swing.*;

/**
 * Sample node.
 *
 * @author Mikle Garin
 */

public class SampleNode extends AsyncUniqueNode implements Cloneable, IconSupport, TitleSupport
{
    /**
     * Node type.
     */
    protected SampleNodeType type;

    /**
     * Node title to display.
     */
    protected String title;

    /**
     * Time spent to load node children.
     */
    protected long time;

    /**
     * Constructs sample node.
     *
     * @param type  node type
     * @param title node name
     */
    public SampleNode ( final SampleNodeType type, final String title )
    {
        super ();
        this.type = type;
        this.title = title;
        this.time = 0;
    }

    /**
     * Returns node type.
     *
     * @return node type
     */
    public SampleNodeType getType ()
    {
        return type;
    }

    /**
     * Changes node type.
     *
     * @param type new node type
     */
    public void setType ( final SampleNodeType type )
    {
        this.type = type;
    }

    @Override
    public Icon getNodeIcon ()
    {
        switch ( getType () )
        {
            case root:
            {
                return WebTreeUI.ROOT_ICON;
            }
            case folder:
            {
                // todo expanded ? WebTreeUI.OPEN_ICON : WebTreeUI.CLOSED_ICON;
                return WebTreeUI.CLOSED_ICON;
            }
            case leaf:
            default:
            {
                return WebTreeUI.LEAF_ICON;
            }
        }
    }

    /**
     * Returns node name.
     *
     * @return node name
     */
    @Override
    public String getTitle ()
    {
        return title;
    }

    /**
     * Changes node name.
     *
     * @param title new node name
     */
    public void setTitle ( final String title )
    {
        this.title = title;
    }

    /**
     * Returns time spent to load node children.
     *
     * @return time spent to load node children
     */
    public long getTime ()
    {
        return time;
    }

    /**
     * Sets time spent to load node children.
     *
     * @param time new time spent to load node children
     */
    public void setTime ( final long time )
    {
        this.time = time;
    }

    @Override
    public String toString ()
    {
        return getTitle ();
    }

    @Override
    public SampleNode clone ()
    {
        // Cannot use this yet as it will enforce full nodes structure cloning
        // It will also get stuck and eventually throw stackoverflow due to current clone issues
        // return MergeUtils.cloneByFieldsSafely ( this );

        final SampleNode clone = new SampleNode ( getType (), getTitle () );
        clone.setId ( getId () );
        clone.setTime ( getTime () );
        return clone;
    }
}