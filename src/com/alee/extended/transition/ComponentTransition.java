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
import com.alee.utils.SwingUtils;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 26.10.11 Time: 12:57
 */

public class ComponentTransition extends WebPanel
{
    // Transition listeners
    private List<TransitionListener> transitionListeners = new ArrayList<TransitionListener> ( 1 );

    // Variables
    private boolean animating = false;

    // Current transition
    private ImageTransition transition = null;

    // Added effects
    private List<TransitionEffect> transitionEffects = null;

    // Last component
    private Component lastContent;

    public ComponentTransition ()
    {
        this ( ( Component ) null );
    }

    public ComponentTransition ( Component content )
    {
        this ( content, null );
    }

    public ComponentTransition ( TransitionEffect transitionEffect )
    {
        this ( null, transitionEffect );
    }

    public ComponentTransition ( Component content, TransitionEffect transitionEffect )
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

    /**
     * Operations with content
     */

    public Component getContent ()
    {
        return getComponentCount () > 0 ? getComponent ( 0 ) : null;
    }

    public void setContent ( Component content )
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

    public synchronized void performTransition ( final Component content )
    {
        SwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                performThreadedTransition ( content );
            }
        } );
    }

    /**
     * Delayed transition
     */

    public void delayTransition ( long delay, final Component content )
    {
        SwingUtils.delayInvokeLater ( delay, new Runnable ()
        {
            @Override
            public void run ()
            {
                performThreadedTransition ( content );
            }
        } );
    }

    /**
     * Transition call
     */

    private void performThreadedTransition ( final Component content )
    {
        // Ignore repeated change or change to same content
        if ( lastContent == content )
        {
            return;
        }

        // When animation disabled or component is not shown performing transition instantly
        if ( !isShowing () )
        {
            finishTransition ( content );
            return;
        }

        // Remember new content to which area is switched
        lastContent = content;

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
            Component currentContent = getComponentCount () > 0 ? getComponent ( 0 ) : null;
            currentSnapshot = SwingUtils.createComponentSnapshot ( currentContent, width, height );
        }

        // Enabling focus for transition time so you can focus the panel
        // It will transfer focus onto inner components (or next, if no focusable inner components available) when transition ends
        setFocusable ( true );

        // Passing the focus from subcomponents onto the transition panel
        if ( SwingUtils.hasFocusOwner ( ComponentTransition.this ) )
        {
            // Focus handler required to wait for focus change
            FocusAdapter focusHandle = new FocusAdapter ()
            {
                @Override
                public void focusGained ( FocusEvent e )
                {
                    removeFocusListener ( this );
                    continueThreadedTransition ( content, width, height, currentSnapshot );
                }
            };
            addFocusListener ( focusHandle );

            // Requesting focus into transition panel
            if ( !ComponentTransition.this.requestFocusInWindow () )
            {
                // Request focus failed, continue without changing focus
                removeFocusListener ( focusHandle );
                continueThreadedTransition ( content, width, height, currentSnapshot );
            }
        }
        else
        {
            continueThreadedTransition ( content, width, height, currentSnapshot );
        }
    }

    private void continueThreadedTransition ( final Component content, int width, int height, BufferedImage currentSnapshot )
    {
        // New content image
        removeAll ();
        if ( content != null )
        {
            add ( content, StackLayout.HIDDEN );
        }

        // Creating snapshot before removing all components
        BufferedImage otherSnapshot = SwingUtils.createComponentSnapshot ( content, width, height );

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
                finishTransition ( content );
            }
        } );

        // Start transition
        transition.performTransition ();
    }

    private void finishTransition ( Component content )
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

    private boolean isAnimating ()
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

    public void addTransitionEffect ( TransitionEffect transitionEffect )
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
            transitionEffects.clear ();
            transitionEffects = null;
        }
    }

    public void removeTransitionEffect ( TransitionEffect transitionEffect )
    {
        if ( transitionEffects != null )
        {
            transitionEffects.remove ( transitionEffect );
        }
    }

    public void setTransitionEffect ( TransitionEffect transitionEffect )
    {
        transitionEffects = transitionEffect != null ? CollectionUtils.copy ( transitionEffect ) : null;
    }

    public void setTransitionEffects ( List<TransitionEffect> transitionEffects )
    {
        this.transitionEffects = transitionEffects;
    }

    public void setTransitionEffects ( TransitionEffect... transitionEffects )
    {
        this.transitionEffects = transitionEffects != null ? CollectionUtils.copy ( transitionEffects ) : null;
    }

    public void addTransitionListener ( TransitionListener listener )
    {
        transitionListeners.add ( listener );
    }

    public void removeTransitionListener ( TransitionListener listener )
    {
        transitionListeners.remove ( listener );
    }

    private void fireTransitionStarted ()
    {
        for ( TransitionListener listener : CollectionUtils.copy ( transitionListeners ) )
        {
            listener.transitionStarted ();
        }
    }

    private void fireTransitionFinished ()
    {
        for ( TransitionListener listener : CollectionUtils.copy ( transitionListeners ) )
        {
            listener.transitionFinished ();
        }
    }
}
