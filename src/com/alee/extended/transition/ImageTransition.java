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
 * User: mgarin Date: 27.10.11 Time: 14:58
 */

public class ImageTransition extends JComponent implements ActionListener
{
    // Transition listeners
    private List<TransitionListener> transitionListeners = new ArrayList<TransitionListener> ( 1 );

    // Images
    private BufferedImage currentImage = null;
    private BufferedImage otherImage = null;

    // Added effects
    private List<TransitionEffect> transitionEffects = new ArrayList<TransitionEffect> ();

    // Variables
    private WebTimer animator = null;
    private boolean animating = false;
    private boolean blocked = false;

    // Current transition effect
    private TransitionEffect actualTransitionEffect = null;

    public ImageTransition ()
    {
        this ( null, null );
    }

    public ImageTransition ( BufferedImage currentImage )
    {
        this ( currentImage, null );
    }

    public ImageTransition ( BufferedImage currentImage, BufferedImage otherImage )
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

    public void setBlocked ( boolean blocked )
    {
        this.blocked = blocked;
    }

    public Image getCurrentImage ()
    {
        return currentImage;
    }

    public void setCurrentImage ( BufferedImage currentImage )
    {
        this.currentImage = currentImage;
    }

    public BufferedImage getOtherImage ()
    {
        return otherImage;
    }

    public void setOtherImage ( BufferedImage otherImage )
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

    public void changeImage ( BufferedImage otherImage )
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

        long animationDelay = actualTransitionEffect != null ? actualTransitionEffect.getAnimationDelay () : 0;

        // Starting new transition
        animator = new WebTimer ( "ImageTransition.animator", animationDelay, this );

        // Starting transition
        fireTransitionStarted ();
        animator.start ();
    }

    @Override
    public void actionPerformed ( ActionEvent e )
    {
        if ( actualTransitionEffect == null || actualTransitionEffect.performAnimationTick ( ImageTransition.this ) )
        {
            animator.stop ();
            finishTransition ();
        }
    }

    public void cancelTransition ()
    {
        animator.stop ();
    }

    private void finishTransition ()
    {
        // Switching images
        BufferedImage oldCurrent = this.currentImage;
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
    protected void paintComponent ( Graphics g )
    {
        super.paintComponent ( g );

        Graphics2D g2d = ( Graphics2D ) g;
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
