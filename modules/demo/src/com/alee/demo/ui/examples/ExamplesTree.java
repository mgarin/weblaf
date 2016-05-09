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
import com.alee.laf.tree.TreeNodeEventRunnable;
import com.alee.laf.tree.TreeSelectionStyle;

/**
 * @author Mikle Garin
 */

public final class ExamplesTree extends WebExTree<ExamplesTreeNode>
{
    /**
     * Constructs new examples tree.
     */
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
    }
}