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

package com.alee.extended.transition;

import com.alee.extended.layout.StackLayout;
import com.alee.extended.transition.effects.TransitionEffect;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.CollectionUtils;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WebTimer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class ComponentTransition extends WebPanel
{
    // Transition listeners
    protected List<TransitionListener> transitionListeners = new ArrayList<TransitionListener> ( 1 );

    // Whether should restore focus after transition or not
    protected boolean restoreFocus = true;

    // Variables
    protected boolean animating = false;

    // Current transition
    protected ImageTransition transition = null;

    // Added effects
    protected List<TransitionEffect> transitionEffects = null;

    // Last component
    protected Component lastContent;

    public ComponentTransition ()
    {
        this ( ( Component ) null );
    }

    public ComponentTransition ( final Component content )
    {
        this ( content, null );
    }

    public ComponentTransition ( final TransitionEffect transitionEffect )
    {
        this ( null, transitionEffect );
    }

    public ComponentTransition ( final Component content, final TransitionEffect transitionEffect )
    {
        // Special layout allowing double content
        super ( new StackLayout () );

        setFocusable ( false );

        // Transition effect
        setTransitionEffect ( transitionEffect );

        // Add initial content
        lastContent = content;
        if ( content != null )
        {
            add ( content );
        }
    }

    public boolean isRestoreFocus ()
    {
        return restoreFocus;
    }

    public void setRestoreFocus ( final boolean restoreFocus )
    {
        this.restoreFocus = restoreFocus;
    }

    /**
     * Operations with content
     */

    public Component getContent ()
    {
        return getComponentCount () > 0 ? getComponent ( 0 ) : null;
    }

    public void setContent ( final Component content )
    {
        if ( !isAnimating () )
        {
            lastContent = content;
            removeAll ();
            add ( content );
            revalidate ();
            repaint ();
        }
    }

    /**
     * Instant transition
     */

    public void performTransition ( final Component content )
    {
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                performTransitionImpl ( content );
            }
        } );
    }

    /**
     * Delayed transition
     */

    public void delayTransition ( final long delay, final Component content )
    {
        WebTimer.delay ( "delayTransition", delay, true, new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                performTransitionImpl ( content );
            }
        } );
    }

    /**
     * Transition call
     */

    protected void performTransitionImpl ( final Component content )
    {
        // Ignore repeated change or change to same content
        if ( lastContent == content )
        {
            return;
        }

        // Remember new content to which area is switched
        lastContent = content;

        // When animation disabled or component is not shown performing transition instantly
        if ( !canAnimate () )
        {
            finishTransitionImpl ( content );
            return;
        }

        // Check for old transition
        if ( transition != null )
        {
            if ( isAnimating () )
            {
                // Cancel old transition if it is already running
                transition.cancelTransition ();
            }
            else
            {
                // Block transition if it has not yet started
                transition.setBlocked ( true );
            }
        }

        // Marking new transition start
        animating = true;

        // Width and height
        final int width = getWidth ();
        final int height = getHeight ();

        // Current content image
        final BufferedImage currentSnapshot;
        if ( transition != null )
        {
            currentSnapshot = transition.getOtherImage ();
        }
        else
        {
            final Component currentContent = getComponentCount () > 0 ? getComponent ( 0 ) : null;
            currentSnapshot = SwingUtils.createComponentSnapshot ( currentContent, width, height );
        }

        // Enabling focus for transition time so you can focus the panel
        // It will transfer focus onto inner components (or next, if no focusable inner components available) when transition ends
        setFocusable ( true );

        // Passing the focus from subcomponents onto the transition panel
        if ( restoreFocus && SwingUtils.hasFocusOwner ( ComponentTransition.this ) )
        {
            // Focus handler required to wait for focus change
            // This is required to handle focus properly and have atleast one component focused withing window of calendar
            // Otherwise focus will be lost and window may become "bugged" and may require additiona user actions in some cases
            final FocusAdapter focusHandle = new FocusAdapter ()
            {
                @Override
                public void focusGained ( final FocusEvent e )
                {
                    removeFocusListener ( this );
                    continueTransitionImpl ( content, width, height, currentSnapshot );
                }
            };
            addFocusListener ( focusHandle );

            // Requesting focus into transition panel
            if ( !ComponentTransition.this.requestFocusInWindow () )
            {
                // Request focus failed, continue without changing focus
                removeFocusListener ( focusHandle );
                continueTransitionImpl ( content, width, height, currentSnapshot );
            }
        }
        else
        {
            continueTransitionImpl ( content, width, height, currentSnapshot );
        }
    }

    protected boolean canAnimate ()
    {
        return getTransitionEffect () != null && isShowing ();
    }

    protected void continueTransitionImpl ( final Component content, final int width, final int height,
                                            final BufferedImage currentSnapshot )
    {
        // New content image
        removeAll ();
        if ( content != null )
        {
            add ( content, StackLayout.HIDDEN );
        }

        // Creating snapshot before removing all components
        final BufferedImage otherSnapshot = SwingUtils.createComponentSnapshot ( content, width, height );

        // Transition panel
        removeAll ();
        transition = new ImageTransition ( currentSnapshot, otherSnapshot );
        transition.setTransitionEffects ( transitionEffects );
        add ( transition );
        revalidate ();
        repaint ();

        // After-transition actions
        transition.addTransitionListener ( new TransitionListener ()
        {
            @Override
            public void transitionStarted ()
            {
                fireTransitionStarted ();
            }

            @Override
            public void transitionFinished ()
            {
                finishTransitionImpl ( content );
            }
        } );

        // Start transition
        transition.performTransition ();
    }

    protected void finishTransitionImpl ( final Component content )
    {
        // Destroying all image links
        this.removeAll ();

        // Adding final content to area
        if ( content != null )
        {
            this.add ( content );
        }
        this.revalidate ();
        this.repaint ();
        animating = false;

        // Returning focus back into content and disabling focus
        if ( isFocusOwner () )
        {
            transferFocus ();
        }
        setFocusable ( false );

        // Cleaning collapse animation resourcs
        if ( transition != null )
        {
            transition.destroy ();
            transition = null;
        }

        // Informing listeners
        fireTransitionFinished ();
    }

    public boolean isAnimating ()
    {
        return animating;
    }

    public List<TransitionEffect> getTransitionEffects ()
    {
        return transitionEffects;
    }

    public TransitionEffect getTransitionEffect ()
    {
        return transitionEffects != null && transitionEffects.size () > 0 ? transitionEffects.get ( 0 ) : null;
    }

    public void addTransitionEffect ( final TransitionEffect transitionEffect )
    {
        if ( transitionEffects == null )
        {
            transitionEffects = new ArrayList<TransitionEffect> ();
        }
        transitionEffects.add ( transitionEffect );
    }

    public void clearTransitionEffects ()
    {
        if ( transitionEffects != null )
        {
            transitionEffects = null;
        }
    }

    public void removeTransitionEffect ( final TransitionEffect transitionEffect )
    {
        if ( transitionEffects != null )
        {
            transitionEffects.remove ( transitionEffect );
        }
    }

    public void setTransitionEffect ( final TransitionEffect transitionEffect )
    {
        transitionEffects = transitionEffect != null ? CollectionUtils.asList ( transitionEffect ) : null;
    }

    public void setTransitionEffects ( final List<TransitionEffect> transitionEffects )
    {
        this.transitionEffects = transitionEffects;
    }

    public void setTransitionEffects ( final TransitionEffect... transitionEffects )
    {
        this.transitionEffects = transitionEffects != null ? CollectionUtils.asList ( transitionEffects ) : null;
    }

    public void addTransitionListener ( final TransitionListener listener )
    {
        transitionListeners.add ( listener );
    }

    public void removeTransitionListener ( final TransitionListener listener )
    {
        transitionListeners.remove ( listener );
    }

    public void fireTransitionStarted ()
    {
        for ( final TransitionListener listener : CollectionUtils.copy ( transitionListeners ) )
        {
            listener.transitionStarted ();
        }
    }

    public void fireTransitionFinished ()
    {
        for ( final TransitionListener listener : CollectionUtils.copy ( transitionListeners ) )
        {
            listener.transitionFinished ();
        }
    }
}