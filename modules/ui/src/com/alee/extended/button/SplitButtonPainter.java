package com.alee.extended.button;

import com.alee.api.jdk.Objects;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.AbstractButtonPainter;
import com.alee.managers.icon.Icons;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Basic painter for {@link WebSplitButton} component.
 * It is used as {@link WSplitButtonUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public class SplitButtonPainter<C extends WebSplitButton, U extends WSplitButtonUI<C>, D extends IDecoration<C, D>>
        extends AbstractButtonPainter<C, U, D> implements ISplitButtonPainter<C, U>
{
    /**
     * todo 1. Replace custom split button painting with button component
     * todo 2. Replace custom separator painting with separator component
     * todo 3. Add appropriate border to incorporate two extra components
     */

    /**
     * Style settings.
     * todo This should be in separator & button styles
     */
    protected Color splitLineColor;
    protected Color splitLineDisabledColor;
    protected int menuIconGap;
    protected int contentGap;

    /**
     * Listeners.
     */
    protected transient MouseAdapter menuButtonTracker;
    protected transient PropertyChangeListener popupMenuPropertyChangeListener;

    /**
     * Runtime variables.
     */
    protected transient boolean onMenu;

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installMenuButtonListeners ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallMenuButtonListeners ();
        super.uninstallPropertiesAndListeners ();
    }

    /**
     * Installs menu button mouseover listener.
     */
    protected void installMenuButtonListeners ()
    {
        // todo Should be replaced with state-based settings
        onMenu = false;
        menuButtonTracker = new MouseAdapter ()
        {
            @Override
            public void mouseMoved ( final MouseEvent e )
            {
                // Saving old menu button mouseover state
                final boolean wasOnMenu = onMenu;

                // Updating menu button mouseover state
                onMenu = getMenuButtonHitbox ( component ).contains ( e.getPoint () );

                // Repainting button if state has changed
                if ( wasOnMenu != onMenu )
                {
                    repaint ();
                }
            }

            @Override
            public void mouseExited ( final MouseEvent e )
            {
                // Saving old menu button mouseover state
                final boolean wasOnMenu = onMenu;

                // Resetting menu button mouseover state
                onMenu = false;

                // Repainting button if state has changed
                if ( wasOnMenu != onMenu )
                {
                    repaint ();
                }
            }
        };
        component.addMouseListener ( menuButtonTracker );
        component.addMouseMotionListener ( menuButtonTracker );

        popupMenuPropertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent event )
            {
                if ( Objects.equals ( event.getPropertyName (), WebLookAndFeel.VISIBLE_PROPERTY ) )
                {
                    updateDecorationState ();
                }
            }
        };
        installPopupMenuPropertyChangeListener ( component.getPopupMenu () );
    }

    /**
     * Uninstalls menu button mouseover listener.
     */
    protected void uninstallMenuButtonListeners ()
    {
        uninstallPopupMenuPropertyChangeListener ( component.getPopupMenu () );
        popupMenuPropertyChangeListener = null;
        component.removeMouseMotionListener ( menuButtonTracker );
        component.removeMouseListener ( menuButtonTracker );
        menuButtonTracker = null;
        onMenu = false;
    }

    @Override
    protected void propertyChanged ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating border on menu icon change
        if ( Objects.equals ( property, WebSplitButton.MENU_ICON_PROPERTY ) )
        {
            updateBorder ();
        }

        // Updating border on popup menu change
        if ( Objects.equals ( property, WebSplitButton.POPUP_MENU_PROPERTY ) )
        {
            uninstallPopupMenuPropertyChangeListener ( ( JPopupMenu ) oldValue );
            installPopupMenuPropertyChangeListener ( ( JPopupMenu ) newValue );
        }
    }

    /**
     * Installs {@link PropertyChangeListener} into {@link JPopupMenu} of the {@link WebSplitButton}.
     *
     * @param popupMenu {@link JPopupMenu} to install {@link PropertyChangeListener} into
     */
    protected void installPopupMenuPropertyChangeListener ( final JPopupMenu popupMenu )
    {
        if ( popupMenu != null )
        {
            popupMenu.addPropertyChangeListener ( popupMenuPropertyChangeListener );
        }
    }

    /**
     * Uninstalls {@link PropertyChangeListener} from {@link JPopupMenu} of the {@link WebSplitButton}.
     *
     * @param popupMenu {@link JPopupMenu} to uninstall {@link PropertyChangeListener} from
     */
    protected void uninstallPopupMenuPropertyChangeListener ( final JPopupMenu popupMenu )
    {
        if ( popupMenu != null )
        {
            popupMenu.removePropertyChangeListener ( popupMenuPropertyChangeListener );
        }
    }

    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        states.add ( component.isPopupMenuVisible () ? DecorationState.popupVisible : DecorationState.popupHidden );
        return states;
    }

    /**
     * Returns gap between menu icon and menu button part sides.
     *
     * @return gap between menu icon and menu button part sides
     */
    public int getMenuIconGap ()
    {
        return menuIconGap;
    }

    /**
     * Sets gap between menu icon and menu button part sides
     *
     * @param menuIconGap gap between menu icon and menu button part sides
     */
    public void setMenuIconGap ( final int menuIconGap )
    {
        this.menuIconGap = menuIconGap;
    }

    /**
     * Returns gap between menu button part and button content.
     *
     * @return gap between menu button part and button content
     */
    public int getContentGap ()
    {
        return contentGap;
    }

    /**
     * Sets gap between menu button part and button content.
     *
     * @param contentGap gap between menu button part and button content
     */
    public void setContentGap ( final int contentGap )
    {
        this.contentGap = contentGap;
    }

    @Override
    public boolean isOnMenu ()
    {
        return onMenu;
    }

    @Override
    protected Insets getBorder ()
    {
        final Insets result;
        final Insets border = super.getBorder ();
        final Icon menuIcon = getMenuIcon ();
        final int extra = contentGap + 1 + menuIconGap + menuIcon.getIconWidth () + menuIconGap;
        if ( border != null )
        {
            result = new Insets ( border.top, border.left, border.bottom, border.right + extra );
        }
        else
        {
            result = new Insets ( 0, 0, 0, extra );
        }
        return result;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final C c, final U ui )
    {
        // Painting menu button
        paintMenuButton ( g2d, bounds, c );
    }

    /**
     * Paints menu button.
     *
     * @param g2d    {@link Graphics2D}
     * @param bounds painting bounds
     * @param c      {@link WebSplitButton}
     */
    protected void paintMenuButton ( final Graphics2D g2d, final Rectangle bounds, final C c )
    {
        // Painting menu button icon
        final Icon menuIcon = getMenuIcon ();
        final Rectangle br = getMenuButtonBounds ( bounds, c );
        final int ix = br.x + br.width / 2 - menuIcon.getIconWidth () / 2;
        final int iy = br.y + br.height / 2 - menuIcon.getIconHeight () / 2;
        menuIcon.paintIcon ( component, g2d, ix, iy );

        // Painting menu button side line
        final Rectangle lr = getMenuButtonLineBounds ( bounds, c );
        g2d.setPaint ( c.isEnabled () ? splitLineColor : splitLineDisabledColor );
        g2d.drawLine ( lr.x, lr.y, lr.x, lr.y + lr.height );
    }

    /**
     * Returns bounds of the menu button part.
     *
     * @param b painting bounds
     * @param c {@link WebSplitButton}
     * @return bounds of the menu button part
     */
    protected Rectangle getMenuButtonBounds ( final Rectangle b, final C c )
    {
        final Insets i = c.getInsets ();
        final int iconWidth = getMenuIcon ().getIconWidth ();
        final int x = b.x + ( ltr ? b.width - i.right + contentGap + 1 + menuIconGap :
                i.left - contentGap - 1 - menuIconGap - iconWidth );
        return new Rectangle ( x, b.y + i.top, iconWidth, b.height - i.top - i.bottom );
    }

    /**
     * Returns bounds of the menu line part.
     *
     * @param b painting bounds
     * @param c {@link WebSplitButton}
     * @return bounds of the menu line part
     */
    protected Rectangle getMenuButtonLineBounds ( final Rectangle b, final C c )
    {
        final Insets i = c.getInsets ();
        final int x = b.x + ( ltr ? b.width - i.right + contentGap : i.left - contentGap );
        return new Rectangle ( x, b.y + i.top, 1, b.height - i.top - i.bottom - 1 );
    }

    /**
     * Returns menu button part hitbox.
     *
     * @param c {@link WebSplitButton}
     * @return menu button part hitbox
     */
    protected Rectangle getMenuButtonHitbox ( final C c )
    {
        final Insets i = c.getInsets ();
        return new Rectangle ( ltr ? c.getWidth () - i.right : 0, 0, ltr ? i.right : i.left, c.getHeight () );
    }

    /**
     * Returns menu {@link Icon}.
     *
     * @return menu {@link Icon}
     */
    protected Icon getMenuIcon ()
    {
        final Icon customIcon = component.getMenuIcon ();
        return customIcon != null ? customIcon : Icons.downSmall;
    }
}