/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.extended.button;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.MouseButton;
import com.alee.utils.swing.WebTimer;
import com.alee.utils.swing.extensions.KeyEventRunnable;
import com.alee.utils.swing.extensions.MouseEventRunnable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This component that allows switching between two states.
 *
 * @author Mikle Garin
 */
public class WebSwitch extends WebPanel implements ItemSelectable
{
    /**
     * todo 1. Refactor component structure, probably perform all elements painting inside a single painter
     */

    /**
     * Style settings.
     */
    protected boolean animate = true;

    /**
     * UI elements.
     */
    protected final WebPanel gripper;
    protected JComponent selectedComponent;
    protected JComponent deselectedComponent;

    /**
     * Runtime variables.
     */
    protected boolean selected = false;
    protected boolean animating = false;
    protected WebTimer animator;
    protected float gripperLocation = 0;

    /**
     * Constructs a deselected switch.
     */
    public WebSwitch ()
    {
        this ( StyleId.wswitch, false );
    }

    /**
     * Constructs either selected or deselected switch.
     *
     * @param selected whether switch is selected or not
     */
    public WebSwitch ( final boolean selected )
    {
        this ( StyleId.wswitch, selected );
    }

    /**
     * Constructs a deselected switch.
     *
     * @param id {@link StyleId}
     */
    public WebSwitch ( final StyleId id )
    {
        this ( id, false );
    }

    /**
     * Constructs either selected or deselected switch.
     *
     * @param id       {@link StyleId}
     * @param selected whether switch is selected or not
     */
    public WebSwitch ( final StyleId id, final boolean selected )
    {
        super ( id, new WebSwitchLayout () );
        putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );

        // Switch gripper
        gripper = new WebPanel ( StyleId.wswitchGripper.at ( this ) );
        add ( gripper, WebSwitchLayout.GRIPPER );

        // Selected and deselected components
        setSwitchComponents ( "weblaf.ex.switch.selected", "weblaf.ex.switch.deselected" );

        // Switch animator
        createAnimator ();

        // Various listeners
        final Runnable manualSelection = new Runnable ()
        {
            @Override
            public void run ()
            {
                if ( isEnabled () )
                {
                    requestFocusInWindow ();
                    setSelected ( !isSelected () );
                    fireActionPerformed ();
                }
            }
        };
        final KeyEventRunnable keyEventRunnable = new KeyEventRunnable ()
        {
            @Override
            public void run ( @NotNull final KeyEvent e )
            {
                manualSelection.run ();
            }
        };
        final MouseEventRunnable mouseEventRunnable = new MouseEventRunnable ()
        {
            @Override
            public void run ( @NotNull final MouseEvent e )
            {
                manualSelection.run ();
            }
        };
        onKeyPress ( Hotkey.ENTER, keyEventRunnable );
        onKeyPress ( Hotkey.SPACE, keyEventRunnable );
        onMousePress ( MouseButton.left, mouseEventRunnable );

        // Initial selection
        setSelected ( selected, false );
    }

    /**
     * Initializes switch animator.
     */
    protected void createAnimator ()
    {
        animator = new WebTimer ( "WebSwitch.animator", SwingUtils.frameRateDelay ( 60 ), new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                // Updating gripper location
                gripperLocation = gripperLocation + ( selected ? 0.1f : -0.1f );

                // Checking what to do
                if ( selected && gripperLocation >= 1f || !selected && gripperLocation <= 0f )
                {
                    // Updating final gripper and view
                    gripperLocation = selected ? 1f : 0f;
                    revalidate ();

                    // Finishing animation
                    animating = false;
                    animator.stop ();
                }
                else
                {
                    // Updating view
                    revalidate ();
                }
            }
        } );
    }

    /**
     * Starts animation.
     */
    protected void startAnimation ()
    {
        if ( !animating )
        {
            animating = true;
            animator.start ();
        }
    }

    /**
     * Returns switch gripper.
     *
     * @return switch gripper
     */
    public WebPanel getGripper ()
    {
        return gripper;
    }

    /**
     * Returns current gripper location.
     *
     * @return current gripper location
     */
    public float getGripperLocation ()
    {
        return gripperLocation;
    }

    /**
     * Returns selected switch component.
     *
     * @return selected switch component
     */
    public JComponent getSelectedComponent ()
    {
        return selectedComponent;
    }

    /**
     * Sets new selected switch component.
     *
     * @param component new selected switch component
     */
    public void setSelectedComponent ( final JComponent component )
    {
        setSelectedComponentImpl ( component );
        revalidate ();
    }

    /**
     * Sets new selected switch component.
     *
     * @param component new selected switch component
     */
    protected void setSelectedComponentImpl ( final JComponent component )
    {
        if ( this.selectedComponent != null )
        {
            remove ( this.selectedComponent );
        }
        this.selectedComponent = component;
        add ( component, WebSwitchLayout.LEFT );
    }

    /**
     * Returns deselected switch component.
     *
     * @return deselected switch component
     */
    public JComponent getDeselectedComponent ()
    {
        return deselectedComponent;
    }

    /**
     * Sets new deselected switch component.
     *
     * @param component new deselected switch component
     */
    public void setDeselectedComponent ( final JComponent component )
    {
        setDeselectedComponentImpl ( component );
        revalidate ();
    }

    /**
     * Sets new deselected switch component.
     *
     * @param component new deselected switch component
     */
    protected void setDeselectedComponentImpl ( final JComponent component )
    {
        if ( this.deselectedComponent != null )
        {
            remove ( this.deselectedComponent );
        }
        this.deselectedComponent = component;
        add ( component, WebSwitchLayout.RIGHT );
    }

    /**
     * Sets new switch components.
     *
     * @param selected   new selected switch component
     * @param deselected new deselected switch component
     */
    public void setSwitchComponents ( final JComponent selected, final JComponent deselected )
    {
        setSelectedComponentImpl ( selected );
        setDeselectedComponentImpl ( deselected );
        SwingUtils.equalizeComponentsWidth ( selectedComponent, deselectedComponent );
        revalidate ();
    }

    /**
     * Sets new switch components based on two icons.
     *
     * @param selected   new selected switch component
     * @param deselected new deselected switch component
     */
    public void setSwitchComponents ( final Icon selected, final Icon deselected )
    {
        final WebLabel sl = new WebLabel ( StyleId.wswitchSelectedIconLabel.at ( this ), selected, WebLabel.CENTER );
        final WebLabel dl = new WebLabel ( StyleId.wswitchDeselectedIconLabel.at ( this ), deselected, WebLabel.CENTER );
        setSwitchComponents ( sl, dl );
    }

    /**
     * Sets new switch components based on two text labels.
     *
     * @param selected   new selected switch component
     * @param deselected new deselected switch component
     */
    public void setSwitchComponents ( final String selected, final String deselected )
    {
        final WebLabel sl = new WebLabel ( StyleId.wswitchSelectedLabel.at ( this ), selected, WebLabel.CENTER ).setBoldFont ();
        final WebLabel dl = new WebLabel ( StyleId.wswitchDeselectedLabel.at ( this ), deselected, WebLabel.CENTER ).setBoldFont ();
        setSwitchComponents ( sl, dl );
    }

    /**
     * Sets whether switch is enabled or not.
     *
     * @param enabled whether switch is enabled or not
     */
    @Override
    public void setEnabled ( final boolean enabled )
    {
        super.setEnabled ( enabled );
        gripper.setEnabled ( enabled );
        selectedComponent.setEnabled ( enabled );
        deselectedComponent.setEnabled ( enabled );
    }

    /**
     * Returns whether switch is selected or not.
     *
     * @return true if switch is selected, false otherwise
     */
    public boolean isSelected ()
    {
        return selected;
    }

    /**
     * Sets whether switch is selected or not.
     *
     * @param selected whether switch is selected or not
     */
    public void setSelected ( final boolean selected )
    {
        setSelected ( selected, animate );
    }

    /**
     * Sets whether switch is selected or not and animates the transition if requested.
     *
     * @param selected whether switch is selected or not
     * @param animate  whether switch should animate the transition or not
     */
    public void setSelected ( final boolean selected, final boolean animate )
    {
        this.selected = selected;
        if ( animate )
        {
            startAnimation ();
        }
        else
        {
            gripperLocation = selected ? 1f : 0f;
            revalidate ();
        }
        fireItemStateChanged ();
    }

    /**
     * Returns whether switch should animate all transition by default or not.
     *
     * @return true if switch should animate all transition by default, false otherwise
     */
    public boolean isAnimate ()
    {
        return animate;
    }

    /**
     * Sets whether switch should animate all transition by default or not.
     *
     * @param animate whether switch should animate all transition by default or not
     */
    public void setAnimate ( final boolean animate )
    {
        this.animate = animate;
    }

    /**
     * Adds specified {@link ActionListener}.
     *
     * @param listener {@link ActionListener} to add
     */
    public void addActionListener ( @NotNull final ActionListener listener )
    {
        listenerList.add ( ActionListener.class, listener );
    }

    /**
     * Removes specified {@link ActionListener}.
     *
     * @param listener {@link ActionListener} to remove
     */
    public void removeActionListener ( @NotNull final ActionListener listener )
    {
        listenerList.remove ( ActionListener.class, listener );
    }

    /**
     * Informs all {@link ActionListener}s about occurred event.
     */
    public void fireActionPerformed ()
    {
        final ActionEvent actionEvent = new ActionEvent ( WebSwitch.this, 0, "Selection changed" );
        for ( final ActionListener listener : listenerList.getListeners ( ActionListener.class ) )
        {
            listener.actionPerformed ( actionEvent );
        }
    }

    @Nullable
    @Override
    public Object[] getSelectedObjects ()
    {
        return isSelected () ? new Object[]{ WebSwitch.this } : null;
    }

    /**
     * Adds specified {@link ItemListener}.
     *
     * @param listener {@link ItemListener} to add
     */
    @Override
    public void addItemListener ( @NotNull final ItemListener listener )
    {
        listenerList.add ( ItemListener.class, listener );
    }

    /**
     * Removes specified {@link ItemListener}.
     *
     * @param listener {@link ItemListener} to remove
     */
    @Override
    public void removeItemListener ( @NotNull final ItemListener listener )
    {
        listenerList.remove ( ItemListener.class, listener );
    }

    /**
     * Informs all {@link ItemListener}s about occurred event.
     */
    public void fireItemStateChanged ()
    {
        final ItemEvent itemEvent = new ItemEvent ( WebSwitch.this, 0, getSelectedObjects (),
                isSelected () ? ItemEvent.SELECTED : ItemEvent.DESELECTED );
        for ( final ItemListener listener : listenerList.getListeners ( ItemListener.class ) )
        {
            listener.itemStateChanged ( itemEvent );
        }
    }
}