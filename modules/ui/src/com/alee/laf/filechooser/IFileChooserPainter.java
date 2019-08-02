package com.alee.laf.filechooser;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JFileChooser} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IFileChooserPainter<C extends JFileChooser, U extends WFileChooserUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}