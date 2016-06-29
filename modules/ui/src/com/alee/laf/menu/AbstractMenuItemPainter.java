package com.alee.laf.menu;

import com.alee.laf.button.AbstractButtonPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.plaf.MenuItemUI;

/**
 * Abstract painter for menu item components.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public abstract class AbstractMenuItemPainter<E extends JMenuItem, U extends MenuItemUI, D extends IDecoration<E, D>>
        extends AbstractButtonPainter<E, U, D> implements IAbstractMenuItemPainter<E, U>
{
    /**
     * Button model change listener.
     */
    protected MenuItemChangeListener buttonModelChangeListener;

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Installing corner updater
        buttonModelChangeListener = MenuItemChangeListener.install ( component );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Uninstalling corner updater
        buttonModelChangeListener = MenuItemChangeListener.uninstall ( buttonModelChangeListener, component );

        super.uninstall ( c, ui );
    }

    @Override
    protected boolean isSelected ()
    {
        return component.isEnabled () && ( component.getModel ().isArmed () || component.getModel ().isSelected () );
    }
}