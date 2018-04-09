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

package com.alee.laf.tree;

import com.alee.managers.settings.Configuration;
import com.alee.managers.settings.SettingsProcessor;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * {@link SettingsProcessor} implementation that handles {@link WebTree} state settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-SettingsManager">How to use SettingsManager</a>
 * @see com.alee.managers.settings.UISettingsManager
 * @see com.alee.managers.settings.SettingsManager
 * @see SettingsProcessor
 */
public class TreeSettingsProcessor extends SettingsProcessor<WebTree<? extends UniqueNode>, TreeState, Configuration<TreeState>>
{
    /**
     * {@link TreeSelectionListener} for tracking tree selection changes.
     */
    protected transient TreeSelectionListener treeSelectionListener;

    /**
     * {@link TreeExpansionListener} for tracking tree expansion changes.
     */
    protected transient TreeExpansionListener treeExpansionListener;

    /**
     * Constructs new {@link TreeSettingsProcessor}.
     *
     * @param tree          {@link WebTree} which settings are being managed
     * @param configuration {@link Configuration}
     */
    public TreeSettingsProcessor ( final WebTree<? extends UniqueNode> tree, final Configuration configuration )
    {
        super ( tree, configuration );
    }

    @Override
    protected void register ( final WebTree<? extends UniqueNode> tree )
    {
        treeSelectionListener = new TreeSelectionListener ()
        {
            @Override
            public void valueChanged ( final TreeSelectionEvent e )
            {
                save ();
            }
        };
        tree.addTreeSelectionListener ( treeSelectionListener );

        treeExpansionListener = new TreeExpansionListener ()
        {
            @Override
            public void treeExpanded ( final TreeExpansionEvent event )
            {
                save ();
            }

            @Override
            public void treeCollapsed ( final TreeExpansionEvent event )
            {
                save ();
            }
        };
        tree.addTreeExpansionListener ( treeExpansionListener );
    }

    @Override
    protected void unregister ( final WebTree<? extends UniqueNode> tree )
    {
        tree.removeTreeExpansionListener ( treeExpansionListener );
        treeExpansionListener = null;

        tree.removeTreeSelectionListener ( treeSelectionListener );
        treeSelectionListener = null;
    }

    @Override
    protected void loadSettings ( final WebTree<? extends UniqueNode> tree )
    {
        tree.setTreeState ( loadSettings () );
    }

    @Override
    protected void saveSettings ( final WebTree<? extends UniqueNode> tree )
    {
        saveSettings ( tree.getTreeState () );
    }
}