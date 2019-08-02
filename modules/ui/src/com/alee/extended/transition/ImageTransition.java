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

import com.alee.extended.transition.effects.TransitionEffect;
import com.alee.utils.CollectionUtils;
import com.alee.utils.MathUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class ImageTransition extends JComponent implements ActionListener
{
    // Transition listeners
    protected List<TransitionListener> transitionListeners = new ArrayList<TransitionListener> ( 1 );

    // Images
    protected BufferedImage currentImage = null;
    protected BufferedImage otherImage = null;

    // Added effects
    protected List<TransitionEffect> transitionEffects = new ArrayList<TransitionEffect> ();

    // Variables
    protected WebTimer animator = null;
    protected boolean animating = false;
    protected boolean blocked = false;

    // Current transition effect
    protected TransitionEffect actualTransitionEffect = null;

    public ImageTransition ()
    {
        this ( null, null );
    }

    public ImageTransition ( final BufferedImage currentImage )
    {
        this ( currentImage, null );
    }

    public ImageTransition ( final BufferedImage currentImage, final BufferedImage otherImage )
    {
        super ();
        this.currentImage = currentImage;
        this.otherImage = otherImage;

        SwingUtils.setOrientation ( this );
        setOpaque ( false );
    }

    public boolean isAnimating ()
    {
        return animator != null && animator.isRunning () && animating;
    }

    public boolean isBlocked ()
    {
        return blocked;
    }

    public void setBlocked ( final boolean blocked )
    {
        this.blocked = blocked;
    }

    public BufferedImage getCurrentImage ()
    {
        return currentImage;
    }

    public void setCurrentImage ( final BufferedImage currentImage )
    {
        this.currentImage = currentImage;
    }

    public BufferedImage getOtherImage ()
    {
        return otherImage;
    }

    public void setOtherImage ( final BufferedImage otherImage )
    {
        this.otherImage = otherImage;
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
            transitionEffects.clear ();
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

    public void destroy ()
    {
        if ( animator != null && animator.isRunning () )
        {
            animator.stop ();
            animator = null;
        }
        if ( transitionListeners.size () > 0 )
        {
            transitionListeners.clear ();
        }
        if ( currentImage != null )
        {
            currentImage.flush ();
            currentImage = null;
        }
        if ( otherImage != null )
        {
            otherImage.flush ();
            otherImage = null;
        }
    }

    public void changeImage ( final BufferedImage otherImage )
    {
        // Start only if there is not transition running
        if ( !isAnimating () )
        {
            setOtherImage ( otherImage );
            performTransition ();
        }
    }

    public void performTransition ()
    {
        // Don't allow new transition before old one is finished or transitions blocked
        if ( isAnimating () || isBlocked () )
        {
            return;
        }

        // Defining actual effect for this transaction
        if ( transitionEffects == null || transitionEffects.size () <= 0 )
        {
            actualTransitionEffect = null;
        }
        else if ( transitionEffects.size () == 1 )
        {
            actualTransitionEffect = transitionEffects.get ( 0 );
        }
        else if ( transitionEffects.size () > 1 )
        {
            actualTransitionEffect = transitionEffects.get ( MathUtils.random ( transitionEffects.size () ) );
        }

        // Starting new transition
        final long animationDelay = actualTransitionEffect != null ? actualTransitionEffect.getAnimationDelay () : 0;
        animator = new WebTimer ( "ImageTransition.animator", animationDelay, this );

        // Starting transition
        fireTransitionStarted ();
        animator.start ();
    }

    @Override
    public void actionPerformed ( final ActionEvent e )
    {
        if ( actualTransitionEffect == null || actualTransitionEffect.performAnimationTick ( ImageTransition.this ) )
        {
            animator.stop ();
            finishTransition ();
        }
    }

    public void cancelTransition ()
    {
        if ( animator != null )
        {
            animator.stop ();
        }
    }

    protected void finishTransition ()
    {
        // Switching images
        final BufferedImage oldCurrent = this.currentImage;
        this.currentImage = this.otherImage;
        this.otherImage = oldCurrent;

        // Nullifying current effect
        actualTransitionEffect = null;

        // Updating view
        repaint ();

        // Notifying about transition end
        fireTransitionFinished ();
    }

    @Override
    protected void paintComponent ( final Graphics g )
    {
        super.paintComponent ( g );

        final Graphics2D g2d = ( Graphics2D ) g;
        if ( actualTransitionEffect != null && actualTransitionEffect.isAnimating () && currentImage != null && otherImage != null )
        {
            // Transition view
            actualTransitionEffect.paint ( g2d, ImageTransition.this );
        }
        else if ( currentImage != null )
        {
            // Static image
            g2d.drawImage ( currentImage, 0, 0, getWidth (), getHeight (), null );
        }
    }

    @Override
    public Dimension getPreferredSize ()
    {
        if ( currentImage != null )
        {
            return new Dimension ( currentImage.getWidth (), currentImage.getHeight () );
        }
        else if ( otherImage != null )
        {
            return new Dimension ( otherImage.getWidth (), otherImage.getHeight () );
        }
        else
        {
            return new Dimension ( 0, 0 );
        }
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