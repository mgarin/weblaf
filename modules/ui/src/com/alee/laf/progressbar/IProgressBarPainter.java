package com.alee.laf.progressbar;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JProgressBar} component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IProgressBarPainter<E extends JProgressBar, U extends WProgressBarUI> extends SpecificPainter<E, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}