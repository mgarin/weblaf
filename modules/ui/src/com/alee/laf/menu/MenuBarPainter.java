package com.alee.laf.menu;

import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JMenuBar} component.
 * It is used as {@link WebMenuBarUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */
public class MenuBarPainter<C extends JMenuBar, U extends WebMenuBarUI, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements IMenuBarPainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractDecorationPainter}.
     */
}