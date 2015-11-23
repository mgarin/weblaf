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

package com.alee.extended.tree;

import com.alee.laf.tree.UniqueNode;
import com.alee.utils.compare.Filter;
import com.alee.utils.text.DefaultTextProvider;
import com.alee.utils.text.TextProvider;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Special smart tree filter that doesn't filter out parent nodes which has children that are accepted by filter.
 * This can be used in any kind of filter fields to provide a proper visual feedback in tree.
 *
 * @author Mikle Garin
 */

public class StructuredTreeNodesFilter<E extends UniqueNode> implements Filter<E>
{
    /**
     * Original tree filter.
     */
    protected Filter<E> originalFilter;

    /**
     * Nodes text provider.
     */
    protected TextProvider<E> textProvider;

    /**
     * Accept states by node IDs cache.
     */
    protected Map<String, Boolean> acceptStatesCache = new HashMap<String, Boolean> ();

    /**
     * Whether should match case or not.
     */
    protected boolean matchCase = false;

    /**
     * Whether should use space character as requests separator or not.
     */
    protected boolean useSpaceAsSeparator = false;

    /**
     * Whether should search from node text beginning or not.
     */
    protected boolean searchFromStart = false;

    /**
     * Search request text.
     */
    protected String searchText = "";

    /**
     * Returns original tree filter.
     *
     * @return original tree filter
     */
    public Filter<E> getOriginalFilter ()
    {
        return originalFilter;
    }

    /**
     * Sets original tree filter.
     *
     * @param filter original tree filter
     */
    public void setOriginalFilter ( final Filter<E> filter )
    {
        this.originalFilter = filter;
    }

    /**
     * Returns nodes text provider.
     *
     * @return nodes text provider
     */
    public TextProvider<E> getTextProvider ()
    {
        return textProvider;
    }

    /**
     * Sets nodes text provider.
     * If set to null DefaultTextProvider will be used instead.
     *
     * @param textProvider new nodes text provider
     */
    public void setTextProvider ( final TextProvider<E> textProvider )
    {
        this.textProvider = textProvider != null ? textProvider : new DefaultTextProvider ();
    }

    /**
     * Returns whether should match case or not.
     *
     * @return true if should match case, false otherwise
     */
    public boolean isMatchCase ()
    {
        return matchCase;
    }

    /**
     * Returns whether should match case or not.
     *
     * @param matchCase whether should match case or not
     */
    public void setMatchCase ( final boolean matchCase )
    {
        this.matchCase = matchCase;
    }

    /**
     * Returns whether should use space character as requests separator or not.
     *
     * @return true if should use space character as requests separator, false otherwise
     */
    public boolean isUseSpaceAsSeparator ()
    {
        return useSpaceAsSeparator;
    }

    /**
     * Sets whether should use space character as requests separator or not.
     *
     * @param useSpaceAsSeparator whether should use space character as requests separator or not
     */
    public void setUseSpaceAsSeparator ( final boolean useSpaceAsSeparator )
    {
        this.useSpaceAsSeparator = useSpaceAsSeparator;
    }

    /**
     * Returns whether should search from node text beginning or not.
     *
     * @return true if should search from node text beginning, false otherwise
     */
    public boolean isSearchFromStart ()
    {
        return searchFromStart;
    }

    /**
     * Sets whether should search from node text beginning or not.
     *
     * @param searchFromStart whether should search from node text beginning or not
     */
    public void setSearchFromStart ( final boolean searchFromStart )
    {
        this.searchFromStart = searchFromStart;
    }

    /**
     * Returns search request text.
     *
     * @return search request text
     */
    public String getSearchText ()
    {
        return searchText;
    }

    /**
     * Sets search request text.
     *
     * @param searchText search request text
     */
    public void setSearchText ( final String searchText )
    {
        this.searchText = searchText;
    }

    /**
     * Clears accept states cache.
     */
    public void clearCache ()
    {
        acceptStatesCache.clear ();
    }

    /**
     * Clears specific node accept state cache.
     *
     * @param node node to clear accept state for
     */
    public void clearCache ( final E node )
    {
        clearCache ( node.getId () );
    }

    /**
     * Clears specific node accept state cache.
     *
     * @param nodeId ID of the node to clear accept state for
     */
    public void clearCache ( final String nodeId )
    {
        acceptStatesCache.remove ( nodeId );
    }

    @Override
    public boolean accept ( final E node )
    {
        if ( originalFilter == null || originalFilter.accept ( node ) )
        {
            // Structured nodes filtering
            final String searchRequest = matchCase ? searchText : searchText.toLowerCase ( Locale.ROOT );
            return searchRequest.equals ( "" ) || acceptIncludingChildren ( node, searchRequest );
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns whether the specified node or any of its children match the filter or not.
     *
     * @param node          node to match
     * @param searchRequest search request text
     * @return true if the specified node or any of its children match the filter, false otherwise
     */
    protected boolean acceptIncludingChildren ( final E node, final String searchRequest )
    {
        if ( acceptNode ( node, searchRequest ) )
        {
            return true;
        }
        for ( int i = 0; i < node.getChildCount (); i++ )
        {
            if ( acceptIncludingChildren ( ( E ) node.getChildAt ( i ), searchRequest ) )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the specified node matches the filter or not.
     * This method might return cached value if it exists, otherwise it will retrieve and cache a new value.
     *
     * @param node          node to match
     * @param searchRequest search request text
     * @return true if the specified node matches the filter, false otherwise
     */
    protected boolean acceptNode ( final E node, final String searchRequest )
    {
        Boolean accept = acceptStatesCache.get ( node.getId () );
        if ( accept == null )
        {
            accept = acceptNodeImpl ( node, searchRequest );
            acceptStatesCache.put ( node.getId (), accept );
        }
        return accept;
    }

    /**
     * Returns whether the specified node matches the filter or not.
     *
     * @param node          node to match
     * @param searchRequest search request text
     * @return true if the specified node matches the filter, false otherwise
     */
    protected boolean acceptNodeImpl ( final E node, final String searchRequest )
    {
        final String nodeText = matchCase ? textProvider.getText ( node ) : textProvider.getText ( node ).toLowerCase ( Locale.ROOT );
        if ( useSpaceAsSeparator )
        {
            final StringTokenizer tokenizer = new StringTokenizer ( searchRequest, " ", false );
            while ( tokenizer.hasMoreTokens () )
            {
                if ( accept ( nodeText, tokenizer.nextToken (), searchFromStart ) )
                {
                    return true;
                }
            }
            return false;
        }
        else
        {
            return accept ( nodeText, searchRequest, searchFromStart );
        }
    }

    /**
     * Returns whether filter accepts specified node text or not.
     *
     * @param nodeText        node text
     * @param searchRequest   single search request
     * @param searchFromStart whether should start searching from the beginning of the node text
     * @return true if filter accepts specified node text, false otherwise
     */
    protected boolean accept ( final String nodeText, final String searchRequest, final boolean searchFromStart )
    {
        return searchFromStart ? nodeText.startsWith ( searchRequest ) : nodeText.contains ( searchRequest );
    }
}