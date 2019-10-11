package com.alee.laf.menu;

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.StyleManager;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.event.HierarchyEvent;
import java.util.List;

/**
 * Basic painter for {@link JMenu} component.
 * It is used as {@link WebMenuUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class MenuPainter<C extends JMenu, U extends WebMenuUI, D extends IDecoration<C, D>> extends AbstractMenuItemPainter<C, U, D>
        implements IMenuPainter<C, U>
{
    @Override
    protected void hierarchyChanged ( @NotNull final HierarchyEvent event )
    {
        // Ensure default actions are performed
        super.hierarchyChanged ( event );

        // Update states on hierarchy changes
        // This is needed due to parent-based decoration states
        if ( event.getID () == HierarchyEvent.HIERARCHY_CHANGED )
        {
            updateDecorationState ();
        }
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();

        // State based on where exactly this menu is placed
        if ( component.getParent () != null )
        {
            final boolean inPopupMenu = component.getParent () instanceof JPopupMenu;
            final Class<? extends JComponent> clazz = inPopupMenu ? JPopupMenu.class : JMenuBar.class;
            final String state = StyleManager.getDescriptor ( clazz ).getId ();
            states.add ( state );
        }

        return states;
    }
}