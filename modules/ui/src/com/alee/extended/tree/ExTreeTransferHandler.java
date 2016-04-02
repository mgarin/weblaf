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

package com.alee.extended.tree;

import com.alee.laf.tree.UniqueNode;

/**
 * Custom TransferHandler for WebAsyncTree that provides a quick and convenient way to implement nodes DnD.
 *
 * @param <N> nodes type
 * @param <T> tree type
 * @author Mikle Garin
 */

public abstract class ExTreeTransferHandler<N extends UniqueNode, T extends WebExTree<N>>
        extends AbstractTreeTransferHandler<N, T, ExTreeModel<N>>
{
    /**
     * This handler uses the same methods as {@link com.alee.extended.tree.AbstractTreeTransferHandler}.
     */
}