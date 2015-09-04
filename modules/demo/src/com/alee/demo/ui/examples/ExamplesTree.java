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

package com.alee.demo.ui.examples;

import com.alee.demo.DemoApplication;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.tree.WebExTree;
import com.alee.laf.tree.TreeSelectionStyle;
import com.alee.utils.swing.MouseEventRunnable;

import java.awt.event.MouseEvent;

/**
 * @author Mikle Garin
 */

public class ExamplesTree extends WebExTree<ExamplesTreeNode>
{
    public ExamplesTree ()
    {
        super ( DemoStyles.examplesTree );

        // Tree settings
        setEditable ( false );
        setRootVisible ( false );
        setShowsRootHandles ( true );
        setMultiplySelectionAllowed ( false );
        setSelectionStyle ( TreeSelectionStyle.line );

        // Data provider
        setDataProvider ( new ExamplesTreeDataProvider () );

        // Proper renderer
        setCellRenderer ( new ExamplesTreeCellRenderer () );

        // Tree events
        onDoubleClick ( new MouseEventRunnable ()
        {
            @Override
            public void run ( final MouseEvent e )
            {
                final ExamplesTreeNode node = getNodeForLocation ( e.getPoint () );
                if ( node != null && node.getType () == ExamplesTreeNodeType.example )
                {
                    DemoApplication.getInstance ().open ( node.getExample () );
                }
            }
        } );
    }
}