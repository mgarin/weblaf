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

/*
 *  $Id: FloatSpring.java,v 1.4 2007/06/25 23:00:10 shingoki Exp $
 *
 *  Copyright (c) 2005-2006 shingoki
 *
 *  This file is part of AirCarrier, see http://aircarrier.dev.java.net/
 *
 *    AirCarrier is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.

 *    AirCarrier is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.

 *    You should have received a copy of the GNU General Public License
 *    along with AirCarrier; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

/**
 * A simple spring simulation in one dimension Useful for getting a property (for example a position) to move from one value to another, in
 * a smooth looking way. To use, create a spring with appropriate constants (e.g. new FloatSpring(100) is a reasonable value) Then set
 * spring position to the initial value, and update each frame with target parameter as your desired value. The position parameter will
 * "snap to" the desired value.
 *
 * @author goki
 */

public class FloatSpring
{
    private float position;
    private float springK;
    private float dampingK;
    private float velocity;

    /**
     * Make a spring with given spring constant and damping constant.
     *
     * @param springK  spring constant, the higher this is the "tighter" the spring, and the more force it will exert for a given extension
     * @param dampingK damping constant, the higher this is the stronger the damping, and the more "soggy" the movement
     */
    public FloatSpring ( float springK, float dampingK )
    {
        super ();
        this.position = 0;
        this.springK = springK;
        this.dampingK = dampingK;
        this.velocity = 0;
    }

    /**
     * Create a critically damped spring (or near to critically damped) This spring will quickly move to its target without overshooting.
     *
     * @param springK spring constant - the higher this is, the more quickly the spring will reach its target. A value of 100 gives a
     *                reasonable response in about a second, a higher value gives a faster response.
     */
    public FloatSpring ( float springK )
    {
        this ( springK, ( float ) ( 2 * Math.sqrt ( springK ) ) );
    }

    /**
     * Update the position of the spring. This updates the "position" as if there were a damped spring stretched between the current
     * position and the target position. That is, the spring will tend to pull the position towards the target, and if the spring is damped
     * the position will eventually settle onto the target.
     *
     * @param target target towards which the spring is pulling the position
     * @param time   elapsed time in seconds
     */
    public void update ( float target, float time )
    {

        // Set v to target - position, this is the required movement
        float v = position - target;

        // Multiply displacement by spring constant to get spring force,
        // then subtract damping force
        v = v * -springK - velocity * dampingK;

        // v is now a force, so assuming unit mass is is also acceleration.
        // multiply by elapsed time to get velocity change
        velocity += v * time;

        // If velocity isn't valid, zero it
        if ( Float.isNaN ( velocity ) || Float.isInfinite ( velocity ) )
        {
            velocity = 0;
        }

        // Change the position at the new velocity, for elapsed time
        position += velocity * time;
    }

    /**
     * @return Damping constant, the higher this is the stronger the damping, and the more "soggy" the movement.
     */
    public float getDampingK ()
    {
        return dampingK;
    }

    /**
     * @param dampingK Damping constant, the higher this is the stronger the damping, and the more "soggy" the movement.
     */
    public void setDampingK ( float dampingK )
    {
        this.dampingK = dampingK;
    }

    /**
     * @return The current position of the simulated spring end point, changes as simulation is updated
     */
    public float getPosition ()
    {
        return position;
    }

    /**
     * @param position A new position for simulated spring end point
     */
    public void setPosition ( float position )
    {
        this.position = position;
    }

    /**
     * @return The spring constant - the higher this is, the more quickly the spring will reach its target
     */
    public float getSpringK ()
    {
        return springK;
    }

    /**
     * @param springK The spring constant - the higher this is, the more quickly the spring will reach its target
     */
    public void setSpringK ( float springK )
    {
        this.springK = springK;
    }

    /**
     * @return The current velocity of the position
     */
    public float getVelocity ()
    {
        return velocity;
    }

    /**
     * @param velocity A new value for the current velocity of the position
     */
    public void setVelocity ( float velocity )
    {
        this.velocity = velocity;
    }
}
