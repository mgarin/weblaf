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

package com.alee.api.merge.type;

import com.alee.api.Conjunction;
import com.alee.api.merge.Merge;
import com.alee.api.merge.MergeException;
import com.alee.api.merge.MergePolicy;

import java.util.Arrays;
import java.util.List;

/**
 * Restricts merge depending on specified policies.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-Merge">How to use Merge</a>
 * @see Merge
 */

public class CompoundMergePolicy implements MergePolicy
{
    /**
     * Conjunction used to join different policies check results.
     */
    private final Conjunction conjunction;

    /**
     * Combined merge policies.
     */
    private final List<MergePolicy> policies;

    /**
     * Constructs new {@link CompoundMergePolicy}.
     *
     * @param conjunction conjunction used to join different policies check results
     * @param policies    combined merge policies
     */
    public CompoundMergePolicy ( final Conjunction conjunction, final MergePolicy... policies )
    {
        this ( conjunction, Arrays.asList ( policies ) );
    }

    /**
     * Constructs new {@link CompoundMergePolicy}.
     *
     * @param conjunction conjunction used to join different policies check results
     * @param policies    combined merge policies
     */
    public CompoundMergePolicy ( final Conjunction conjunction, final List<MergePolicy> policies )
    {
        super ();
        this.conjunction = conjunction;
        this.policies = policies;
    }

    @Override
    public boolean accept ( final Merge merge, final Object object, final Object merged )
    {
        switch ( conjunction )
        {
            case AND:
            {
                for ( final MergePolicy policy : policies )
                {
                    if ( !policy.accept ( merge, object, merged ) )
                    {
                        return false;
                    }
                }
                return true;
            }
            case OR:
            {
                for ( final MergePolicy policy : policies )
                {
                    if ( policy.accept ( merge, object, merged ) )
                    {
                        return true;
                    }
                }
                return false;
            }
            default:
            {
                throw new MergeException ( "Unknown conjunction specified: " + conjunction );
            }
        }
    }
}