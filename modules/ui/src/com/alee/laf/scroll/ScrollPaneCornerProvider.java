package com.alee.laf.scroll;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
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
     * Returns {@link JComponent} for the specified scroll pane corner or {@code null} if none provided.
     *
     * @param corner {@link Corner} type
     * @return {@link JComponent} for the specified scroll pane corner or {@code null} if none provided
     */
    @Nullable
    public JComponent getCorner ( @NotNull Corner corner );
}