package com.alee.laf.menu;

import com.alee.laf.button.AbstractButtonPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.plaf.MenuItemUI;

/**
 * Abstract painter for {@link JMenuItem} implementations.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */
public abstract class AbstractMenuItemPainter<C extends JMenuItem, U extends MenuItemUI, D extends IDecoration<C, D>>
        extends AbstractButtonPainter<C, U, D> implements IAbstractMenuItemPainter<C, U>
{
    /**
     * Menu item change listener.
     */
    protected transient MenuItemChangeListener menuItemChangeListener;

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installMenuItemChangeListener ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallMenuItemChangeListener ();
        super.uninstallPropertiesAndListeners ();
    }

    @Override
    protected boolean isSelected ()
    {
        return component.isEnabled () && ( component.getModel ().isArmed () || component.getModel ().isSelected () );
    }

    /**
     * Installs {@link MenuItemChangeListener} into {@link JMenuItem}.
     */
    protected void installMenuItemChangeListener ()
    {
        menuItemChangeListener = MenuItemChangeListener.install ( component );
    }

    /**
     * Uninstalls {@link MenuItemChangeListener} from {@link JMenuItem}.
     */
    protected void uninstallMenuItemChangeListener ()
    {
        menuItemChangeListener = MenuItemChangeListener.uninstall ( menuItemChangeListener, component );
    }
}