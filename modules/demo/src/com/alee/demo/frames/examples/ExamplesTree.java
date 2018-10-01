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

package com.alee.demo.frames.examples;

import com.alee.demo.skin.DemoStyles;
import com.alee.extended.tree.WebExTree;
import com.alee.laf.tree.TreeSelectionStyle;
import com.alee.managers.language.LanguageSensitive;

/**
 * @author Mikle Garin
 */
public final class ExamplesTree extends WebExTree<ExamplesTreeNode> implements LanguageSensitive
{
    /**
     * Constructs new examples tree.
     */
    public ExamplesTree ()
    {
        super ( DemoStyles.examplesTree, new ExamplesTreeDataProvider () );
        setEditable ( false );
        setRootVisible ( false );
        setShowsRootHandles ( true );
        setMultipleSelectionAllowed ( false );
        setSelectionStyle ( TreeSelectionStyle.line );
        expandAll ();
    }
}