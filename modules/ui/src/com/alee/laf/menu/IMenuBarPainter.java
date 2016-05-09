package com.alee.laf.menu;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JMenuBar component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IMenuBarPainter<E extends JMenuBar, U extends WebMenuBarUI> extends SpecificPainter<E, U>
{
}