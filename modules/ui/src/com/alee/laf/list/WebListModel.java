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

package com.alee.laf.list;

import com.alee.utils.CollectionUtils;
import com.alee.utils.compare.IntegerComparator;

import javax.swing.*;
import java.util.*;

/**
 * Custom {@link JList} model with generic element type.
 * Unlike {@link DefaultComboBoxModel} it will not reuse any of the provided arrays or {@link Collection}s.
 * Model should have its own data enclosed in itself in the first place, if you want to have control over it - override the model itself.
 *
 * @param <T> element type
 * @author Mikle Garin
 */
public class WebListModel<T> extends AbstractListModel
{
    /**
     * List data vector.
     */
    protected Vector<T> delegate;

    /**
     * Constructs empty model.
     */
    public WebListModel ()
    {
        this ( Collections.<T>emptyList () );
    }

    /**
     * Constructs model with the specified elements.
     *
     * @param data model data
     */
    public WebListModel ( final T... data )
    {
        this ( CollectionUtils.asList ( data ) );
    }

    /**
     * Constructs model with the specified elements.
     *
     * @param data model data
     */
    public WebListModel ( final Collection<T> data )
    {
        delegate = new Vector<T> ();
        addAll ( data );
    }

    @Override
    public int getSize ()
    {
        return delegate.size ();
    }

    @Override
    public T getElementAt ( final int index )
    {
        return get ( index );
    }

    /**
     * Returns all elements available in the model.
     *
     * @return all elements available in the model
     */
    public List<T> getElements ()
    {
        return new ArrayList<T> ( delegate );
    }

    /**
     * Copies all elements from this model into the specified array.
     *
     * @param array array into which all elements from this model will be copied
     * @throws IndexOutOfBoundsException if array is not big enough to hold all model elements
     */
    public void copyInto ( final T[] array )
    {
        delegate.copyInto ( array );
    }

    /**
     * Trims capacity of this model delegate to fit current model size.
     */
    public void trimToSize ()
    {
        delegate.trimToSize ();
    }

    /**
     * Increases capacity of this model delegate, if necessary, to ensure that it can hold specified amount of elements.
     *
     * @param capacity desired minimum capacity
     */
    public void ensureCapacity ( final int capacity )
    {
        delegate.ensureCapacity ( capacity );
    }

    /**
     * Returns current model delegate capacity.
     *
     * @return current model delegate capacity
     */
    public int capacity ()
    {
        return delegate.capacity ();
    }

    /**
     * Returns amount of elements in this model.
     *
     * @return amount of elements in this model
     */
    public int size ()
    {
        return delegate.size ();
    }

    /**
     * Returns whether or not this model has no elements.
     *
     * @return {@code true} if this model has no elements, {@code false} otherwise
     */
    public boolean isEmpty ()
    {
        return delegate.isEmpty ();
    }

    /**
     * Returns {@link Enumeration} for all elements of this model.
     *
     * @return {@link Enumeration} for all elements of this model
     */
    public Enumeration<T> elements ()
    {
        return delegate.elements ();
    }

    /**
     * Returns whether or not this model contains specified element.
     *
     * @param element element to find
     * @return {@code true} if this model contains specified element, {@code false} otherwise
     */
    public boolean contains ( final T element )
    {
        return delegate.contains ( element );
    }

    /**
     * Returns index of the specified element in this model, {@code -1} if it cannot be found.
     *
     * @param element element to find index for
     * @return index of the specified element in this model, {@code -1} if it cannot be found
     */
    public int indexOf ( final T element )
    {
        return delegate.indexOf ( element );
    }

    /**
     * Returns index of the specified element in this model starting from the specified index, {@code -1} if it cannot be found.
     *
     * @param element element to find index for
     * @param index   index to begin search from
     * @return index of the specified element in this model starting from the specified index, {@code -1} if it cannot be found.
     */
    public int indexOf ( final T element, final int index )
    {
        return delegate.indexOf ( element, index );
    }

    /**
     * Returns last index of the specified element in this model, {@code -1} if it cannot be found.
     *
     * @param element element to find index for
     * @return last index of the specified element in this model, {@code -1} if it cannot be found
     */
    public int lastIndexOf ( final T element )
    {
        return delegate.lastIndexOf ( element );
    }

    /**
     * Returns last index of the specified element in this model starting from the specified index, {@code -1} if it cannot be found.
     *
     * @param element element to find index for
     * @param index   index to begin search from
     * @return last index of the specified element in this model starting from the specified index, {@code -1} if it cannot be found
     */
    public int lastIndexOf ( final T element, final int index )
    {
        return delegate.lastIndexOf ( element, index );
    }

    /**
     * Returns first element in this model.
     *
     * @return first element in this model
     * @throws NoSuchElementException if model is empty
     */
    public T first ()
    {
        return delegate.firstElement ();
    }

    /**
     * Returns last element in this model.
     *
     * @return last element in this model
     * @throws NoSuchElementException if model is empty
     */
    public T last ()
    {
        return delegate.lastElement ();
    }

    /**
     * Returns an array containing all of the elements from this model.
     *
     * @return an array containing all of the elements from this model
     */
    public Object[] toArray ()
    {
        final Object[] rv = new Object[ delegate.size () ];
        delegate.copyInto ( rv );
        return rv;
    }

    /**
     * Returns element contained at the specified index in this model.
     *
     * @param index element index
     * @return element contained at the specified index in this model
     * @throws ArrayIndexOutOfBoundsException if specified index is out of model bounds
     */
    public T get ( final int index )
    {
        return delegate.elementAt ( index );
    }

    /**
     * Adds specified element to this model.
     *
     * @param element element to add
     */
    public void add ( final T element )
    {
        add ( getSize (), element );
    }

    /**
     * Inserts the specified element at the specified index into this model.
     *
     * @param index   index to insert element at
     * @param element element to insert
     * @throws ArrayIndexOutOfBoundsException if specified index is out of model bounds
     */
    public void add ( final int index, final T element )
    {
        addAll ( index, element );
    }

    /**
     * Adds all specified elements to the end of this model.
     *
     * @param elements elements to add
     */
    public void addAll ( final T... elements )
    {
        addAll ( CollectionUtils.asList ( elements ) );
    }

    /**
     * Adds all specified elements to the end of this model.
     *
     * @param elements elements to add
     */
    public void addAll ( final Collection<T> elements )
    {
        addAll ( delegate.size (), elements );
    }

    /**
     * Adds all specified elements at the specified index.
     *
     * @param index    index to add elements at
     * @param elements elements to add
     */
    public void addAll ( final int index, final T... elements )
    {
        addAll ( index, CollectionUtils.asList ( elements ) );
    }

    /**
     * Adds all specified elements at the specified index.
     *
     * @param index    index to add elements at
     * @param elements elements to add
     */
    public void addAll ( final int index, final Collection<T> elements )
    {
        if ( elements.size () > 0 )
        {
            delegate.addAll ( index, elements );
            fireIntervalAdded ( this, index, delegate.size () - 1 );
        }
    }

    /**
     * Replaces element at the specified index within this model with new element.
     *
     * @param index   index of element to replace
     * @param element element to store at the specified position
     * @return element previously stored at the specified index in this model
     * @throws ArrayIndexOutOfBoundsException if specified index is out of model bounds
     */
    public T set ( final int index, final T element )
    {
        final T rv = delegate.elementAt ( index );
        delegate.setElementAt ( element, index );
        fireContentsChanged ( this, index, index );
        return rv;
    }

    /**
     * Replaces all elements in this model with the specified ones.
     *
     * @param elements elements to replace all existing ones with
     */
    public void setAll ( final T... elements )
    {
        setAll ( CollectionUtils.asList ( elements ) );
    }

    /**
     * Replaces all elements in this model with the specified ones.
     *
     * @param elements elements to replace all existing ones with
     */
    public void setAll ( final Collection<T> elements )
    {
        removeAll ();
        addAll ( elements );
    }

    /**
     * Removes the specified element from this model and returns it.
     *
     * @param element element to remove
     * @return removed element
     */
    public T remove ( final T element )
    {
        final int index = indexOf ( element );
        return index != -1 ? remove ( index ) : null;
    }

    /**
     * Removes element at the specified index from this model and returns it.
     *
     * @param index the index of the element to removed
     * @return removed element
     * @throws ArrayIndexOutOfBoundsException if the specified index is out of model bounds
     */
    public T remove ( final int index )
    {
        final T element = delegate.elementAt ( index );
        removeInterval ( index, index );
        return element;
    }

    /**
     * Removes all elements from this model which have index lower than the specified one.
     *
     * @param index index to remove all elements before
     */
    public void removeAllBefore ( final int index )
    {
        if ( index > 0 )
        {
            removeInterval ( 0, index - 1 );
        }
        else
        {
            throw new IllegalArgumentException ( "There are no elements below zero index" );
        }
    }

    /**
     * Removes all elements from this model which have index larger than the specified one.
     *
     * @param index index to remove all elements after
     */
    public void removeAllAfter ( final int index )
    {
        if ( index < delegate.size () - 1 )
        {
            removeInterval ( index + 1, delegate.size () - 1 );
        }
        else
        {
            throw new IllegalArgumentException ( "There are no elements after " + index + " index" );
        }
    }

    /**
     * Removes all specified elements from this model.
     * It ensures that minimal amount of {@link #fireIntervalRemoved(Object, int, int)} calls are made.
     *
     * @param elements the components to be removed
     */
    public void removeAll ( final T... elements )
    {
        removeAll ( CollectionUtils.asList ( elements ) );
    }

    /**
     * Removes the specified elements from this model.
     * It ensures that minimal amount of {@link #fireIntervalRemoved(Object, int, int)} calls are made.
     * todo If two+ non-distinct elements are provided - only one will be removed at a time
     *
     * @param elements the components to be removed
     */
    public void removeAll ( final Collection<T> elements )
    {
        // Collecting indices to remove
        final List<Integer> indices = new ArrayList<Integer> ( elements.size () );
        for ( final T element : elements )
        {
            final int index = delegate.indexOf ( element );
            if ( index != -1 )
            {
                indices.add ( index );
            }
        }

        // Making sure there are no duplicate indices
        CollectionUtils.distinct ( indices );

        // Sorting indices
        CollectionUtils.sort ( indices, IntegerComparator.instance () );

        // Collecting ranges
        int rangeStart = -1;
        int rangeEnd = -1;
        for ( int i = indices.size () - 1; i >= 0; i-- )
        {
            final int index = indices.get ( i );

            // Checking range
            if ( rangeStart == -1 || rangeEnd == -1 )
            {
                // We are in the first iteration
                // Updating range with initial values
                rangeStart = index;
                rangeEnd = index;
            }
            else if ( index == rangeStart - 1 )
            {
                // Current index is next to previous range start
                // Simply moving range start to this index
                rangeStart = index;
            }
            else
            {
                // Updating range with new values
                rangeStart = index;
                rangeEnd = index;
            }

            // Removing interval
            if ( i == 0 || rangeStart - 1 != indices.get ( i - 1 ) )
            {
                removeInterval ( rangeStart, rangeEnd );
            }
        }
    }

    /**
     * Removes all elements from the model
     */
    public void removeAll ()
    {
        if ( delegate.size () > 0 )
        {
            removeInterval ( 0, delegate.size () - 1 );
        }
    }

    /**
     * Removes all elements between specified indices including elements at the specified indices.
     *
     * @param start interval start index, inclusive
     * @param end   interval end index, inclusive
     * @throws ArrayIndexOutOfBoundsException if interval is invalid
     * @throws IllegalArgumentException       if {@code from} is larger than {@code to}
     */
    public void removeInterval ( final int start, final int end )
    {
        if ( start > end )
        {
            throw new IllegalArgumentException ( "Interval end index cannot be less than start index" );
        }
        for ( int i = end; i >= start; i-- )
        {
            delegate.removeElementAt ( i );
        }
        fireIntervalRemoved ( this, start, end );
    }

    /**
     * Updates cell for the specified element.
     *
     * @param element element to update
     */
    public void update ( final T element )
    {
        final int index = indexOf ( element );
        if ( index != -1 )
        {
            fireContentsChanged ( this, index, index );
        }
    }

    /**
     * Made public within {@link WebListModel} to allow content updates from outside of the model.
     */
    @Override
    public void fireContentsChanged ( final Object source, final int index0, final int index1 )
    {
        super.fireContentsChanged ( source, index0, index1 );
    }

    /**
     * Made public within {@link WebListModel} to allow content updates from outside of the model.
     */
    @Override
    public void fireIntervalAdded ( final Object source, final int index0, final int index1 )
    {
        super.fireIntervalAdded ( source, index0, index1 );
    }

    /**
     * Made public within {@link WebListModel} to allow content updates from outside of the model.
     */
    @Override
    public void fireIntervalRemoved ( final Object source, final int index0, final int index1 )
    {
        super.fireIntervalRemoved ( source, index0, index1 );
    }

    @Override
    public String toString ()
    {
        return delegate.toString ();
    }
}