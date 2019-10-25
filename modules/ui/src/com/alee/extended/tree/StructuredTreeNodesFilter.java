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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Function;
import com.alee.laf.tree.UniqueNode;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Special smart tree filter that doesn't filter out parent nodes which has children that are accepted by filter.
 * This can be used in any kind of filter fields to provide a proper visual feedback in tree.
 *
 * @param <N> {@link UniqueNode} type
 * @author Mikle Garin
 */
public class StructuredTreeNodesFilter<N extends UniqueNode> implements NodesFilter<N>
{
    /**
     * Accept states by node IDs cache.
     */
    @NotNull
    protected final Map<String, Boolean> acceptStatesCache;

    /**
     * Nodes text provider.
     */
    @NotNull
    protected Function<N, String> textProvider;

    /**
     * Whether should match case or not.
     */
    protected boolean matchCase;

    /**
     * Whether should use space character as requests separator or not.
     */
    protected boolean useSpaceAsSeparator;

    /**
     * Whether should search from node text beginning or not.
     */
    protected boolean searchFromStart;

    /**
     * Search request text.
     */
    @NotNull
    protected String searchText;

    /**
     * Constructs new {@link StructuredTreeNodesFilter}.
     */
    public StructuredTreeNodesFilter ()
    {
        this ( new UniqueNodeTextProvider<N> () );
    }

    /**
     * Constructs new {@link StructuredTreeNodesFilter}.
     *
     * @param textProvider nodes text provider
     */
    public StructuredTreeNodesFilter ( @NotNull final Function<N, String> textProvider )
    {
        this.acceptStatesCache = new HashMap<String, Boolean> ();
        this.textProvider = textProvider;
        this.matchCase = false;
        this.useSpaceAsSeparator = false;
        this.searchFromStart = false;
        this.searchText = "";
    }

    /**
     * Returns nodes text provider.
     *
     * @return nodes text provider
     */
    @NotNull
    public Function<N, String> getTextProvider ()
    {
        return textProvider;
    }

    /**
     * Sets nodes text provider.
     * If set to null DefaultTextProvider will be used instead.
     *
     * @param textProvider new nodes text provider
     */
    public void setTextProvider ( @NotNull final Function<N, String> textProvider )
    {
        this.textProvider = textProvider;
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
    @NotNull
    public String getSearchText ()
    {
        return searchText;
    }

    /**
     * Sets search request text.
     *
     * @param searchText search request text
     */
    public void setSearchText ( @Nullable final String searchText )
    {
        this.searchText = searchText != null ? searchText : "";
    }

    @Override
    public void clearCache ()
    {
        acceptStatesCache.clear ();
    }

    @Override
    public void clearCache ( @NotNull final N node )
    {
        acceptStatesCache.remove ( node.getId () );
    }

    @Override
    public boolean accept ( @NotNull final N node )
    {
        final String searchRequest = matchCase ? searchText : searchText.toLowerCase ( Locale.ROOT );
        return searchRequest.equals ( "" ) || acceptIncludingChildren ( node, searchRequest );
    }

    /**
     * Returns whether the specified node or any of its children match the filter or not.
     *
     * @param node          node to match
     * @param searchRequest search request text
     * @return true if the specified node or any of its children match the filter, false otherwise
     */
    protected boolean acceptIncludingChildren ( @NotNull final N node, @NotNull final String searchRequest )
    {
        boolean accepted;
        if ( acceptNode ( node, searchRequest ) )
        {
            accepted = true;
        }
        else
        {
            accepted = false;
            for ( int i = 0; i < node.getChildCount (); i++ )
            {
                if ( acceptIncludingChildren ( ( N ) node.getChildAt ( i ), searchRequest ) )
                {
                    accepted = true;
                    break;
                }
            }
        }
        return accepted;
    }

    /**
     * Returns whether the specified node matches the filter or not.
     * This method might return cached value if it exists, otherwise it will retrieve and cache a new value.
     *
     * @param node          node to match
     * @param searchRequest search request text
     * @return true if the specified node matches the filter, false otherwise
     */
    protected boolean acceptNode ( @NotNull final N node, @NotNull final String searchRequest )
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
    protected boolean acceptNodeImpl ( @NotNull final N node, @NotNull final String searchRequest )
    {
        boolean accepted;
        final String rawText = textProvider.apply ( node );
        final String nodeText = matchCase ? rawText : rawText.toLowerCase ( Locale.ROOT );
        if ( useSpaceAsSeparator )
        {
            accepted = false;
            final StringTokenizer tokenizer = new StringTokenizer ( searchRequest, " ", false );
            while ( tokenizer.hasMoreTokens () )
            {
                if ( accept ( nodeText, tokenizer.nextToken (), searchFromStart ) )
                {
                    accepted = true;
                    break;
                }
            }
        }
        else
        {
            accepted = accept ( nodeText, searchRequest, searchFromStart );
        }
        return accepted;
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