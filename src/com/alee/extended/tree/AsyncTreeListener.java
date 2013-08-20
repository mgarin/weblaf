package com.alee.extended.tree;

import java.util.EventListener;
import java.util.List;

/**
 * This listener interface provide various asynchronous tree events.
 *
 * @param <E> custom node type
 * @author Mikle Garin
 */

public interface AsyncTreeListener<E extends AsyncUniqueNode> extends EventListener
{
    /**
     * Invoked when childs load operation starts.
     *
     * @param parent node which childs are being loaded
     */
    public void childsLoadStarted ( E parent );

    /**
     * Invoked when childs load operation finishes.
     *
     * @param parent node which childs were loaded
     * @param childs loaded child nodes
     */
    public void childsLoadCompleted ( E parent, List<E> childs );
}