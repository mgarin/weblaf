package com.alee.laf.menu;

import com.alee.managers.style.StyleableComponent;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.event.HierarchyEvent;
import java.util.List;

/**
 * Basic painter for {@link JMenu} component.
 * It is used as {@link WebMenuUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class MenuPainter<E extends JMenu, U extends WebMenuUI, D extends IDecoration<E, D>> extends AbstractMenuItemPainter<E, U, D>
        implements IMenuPainter<E, U>
{
    @Override
    protected void hierarchyChanged ( final HierarchyEvent e )
    {
        // Ensure default actions are performed
        super.hierarchyChanged ( e );

        // Update states on hierarchy changes
        // This is needed due to parent-based decoration states
        if ( e.getID () == HierarchyEvent.HIERARCHY_CHANGED )
        {
            updateDecorationState ();
        }
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();

        // State based on where exactly this menu is placed
        if ( component.getParent () != null )
        {
            final boolean inPopupMenu = component.getParent () instanceof JPopupMenu;
            states.add ( inPopupMenu ? StyleableComponent.popupmenu.name () : StyleableComponent.menubar.name () );
        }

        return states;
    }
}