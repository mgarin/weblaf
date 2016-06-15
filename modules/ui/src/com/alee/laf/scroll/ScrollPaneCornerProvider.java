package com.alee.laf.scroll;

import com.alee.api.data.Corner;

import javax.swing.*;

/**
 * This interface can be implemented by any component that wants to provide custom scroll pane corner components.
 *
 * @author Alexandr Zernov
 */

public interface ScrollPaneCornerProvider
{
    /**
     * Returns the component at the specified corner.
     *
     * @param corner corner type
     * @return component for the specified scroll pane corner or {@code null} if none provided
     */
    public JComponent getCorner ( Corner corner );
}