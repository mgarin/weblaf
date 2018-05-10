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
 * @see com.alee.laf.list.WebList
 */
public class WebListModel<T> extends AbstractListModel
{
    /**
     * List data vector.
     */
    protected Vector<T> delegate = new Vector<T> ();

    /**
     * Constructs empty model.
     */
    public WebListModel ()
    {
        super ();
    }

    /**
     * Constructs model with the specified elements.
     *
     * @param data list data
     */
    public WebListModel ( final T... data )
    {
        Collections.addAll ( delegate, data );
    }

    /**
     * Constructs model with the specified elements.
     *
     * @param data list data
     */
    public WebListModel ( final Collection<T> data )
    {
        delegate.addAll ( data );
    }

    @Override
    public int getSize ()
    {
        return delegate.size ();
    }

    @Override
    public T getElementAt ( final int index )
    {
        return delegate.elementAt ( index );
    }

    /**
     * Returns list of all elements.
     *
     * @return list of all elements
     */
    public List<T> getElements ()
    {
        return new ArrayList<T> ( delegate );
    }

    /**
     * Copies the components of this list into the specified array.
     * The array must be big enough to hold all the objects in this list, else an {@code IndexOutOfBoundsException} is thrown.
     *
     * @param array the array into which the components get copied
     */
    public void copyInto ( final T[] array )
    {
        delegate.copyInto ( array );
    }

    /**
     * Trims the capacity of this list to be the list's current size.
     *
     * @see Vector#trimToSize()
     */
    public void trimToSize ()
    {
        delegate.trimToSize ();
    }

    /**
     * Increases the capacity of this list, if necessary, to ensure that it can hold at least the number of components specified by the
     * minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     * @see Vector#ensureCapacity(int)
     */
    public void ensureCapacity ( final int minCapacity )
    {
        delegate.ensureCapacity ( minCapacity );
    }

    /**
     * Sets the size of this list.
     *
     * @param newSize the new size of this list
     * @see Vector#setSize(int)
     */
    public void setSize ( final int newSize )
    {
        final int oldSize = delegate.size ();
        delegate.setSize ( newSize );
        if ( oldSize > newSize )
        {
            fireIntervalRemoved ( this, newSize, oldSize - 1 );
        }
        else if ( oldSize < newSize )
        {
            fireIntervalAdded ( this, oldSize, newSize - 1 );
        }
    }

    /**
     * Returns the current capacity of this list.
     *
     * @return the current capacity
     * @see Vector#capacity()
     */
    public int capacity ()
    {
        return delegate.capacity ();
    }

    /**
     * Returns the number of components in this list.
     *
     * @return the number of components in this list
     * @see Vector#size()
     */
    public int size ()
    {
        return delegate.size ();
    }

    /**
     * Tests whether this list has any components.
     *
     * @return {@code true} if and only if this list has no components, that is, its size is zero; {@code false} otherwise
     * @see Vector#isEmpty()
     */
    public boolean isEmpty ()
    {
        return delegate.isEmpty ();
    }

    /**
     * Returns an enumeration of the components of this list.
     *
     * @return an enumeration of the components of this list
     * @see Vector#elements()
     */
    public Enumeration<T> elements ()
    {
        return delegate.elements ();
    }

    /**
     * Tests whether the specified object is a component in this list.
     *
     * @param elem an object
     * @return {@code true} if the specified object is the same as a component in this list
     * @see Vector#contains(Object)
     */
    public boolean contains ( final T elem )
    {
        return delegate.contains ( elem );
    }

    /**
     * Searches for the first occurrence of {@code elem}.
     *
     * @param elem an object
     * @return the index of the first occurrence of the argument in this list; returns {@code -1} if the object is not found
     * @see Vector#indexOf(Object)
     */
    public int indexOf ( final T elem )
    {
        return delegate.indexOf ( elem );
    }

    /**
     * Searches for the first occurrence of {@code elem}, beginning the search at {@code index}.
     *
     * @param elem  an desired component
     * @param index the index from which to begin searching
     * @return the index where the first occurrence of {@code elem} is found after {@code index}; returns {@code -1} if the
     * {@code elem} is not found in the list
     * @see Vector#indexOf(Object, int)
     */
    public int indexOf ( final T elem, final int index )
    {
        return delegate.indexOf ( elem, index );
    }

    /**
     * Returns the index of the last occurrence of {@code elem}.
     *
     * @param elem the desired component
     * @return the index of the last occurrence of {@code elem} in the list; returns {@code -1} if the object is not found
     * @see Vector#lastIndexOf(Object)
     */
    public int lastIndexOf ( final T elem )
    {
        return delegate.lastIndexOf ( elem );
    }

    /**
     * Searches backwards for {@code elem}, starting from the specified index, and returns an index to it.
     *
     * @param elem  the desired component
     * @param index the index to start searching from
     * @return the index of the last occurrence of the {@code elem} in this list at position less than {@code index}; returns
     * {@code -1} if the object is not found
     * @see Vector#lastIndexOf(Object, int)
     */
    public int lastIndexOf ( final T elem, final int index )
    {
        return delegate.lastIndexOf ( elem, index );
    }

    /**
     * Returns the first component of this list. Throws a {@code NoSuchElementException} if this vector has no components.
     *
     * @return the first component of this list
     * @see Vector#firstElement()
     */
    public T first ()
    {
        return delegate.firstElement ();
    }

    /**
     * Returns the last component of the list. Throws a {@code NoSuchElementException} if this vector has no components.
     *
     * @return the last component of the list
     * @see Vector#lastElement()
     */
    public T last ()
    {
        return delegate.lastElement ();
    }

    /**
     * Removes all elements from this list which has index lower than the specified one.
     *
     * @param index index to process
     */
    public void removeAllBefore ( final int index )
    {
        for ( int i = 0; i < index; i++ )
        {
            delegate.removeElementAt ( index );
        }
        if ( index > 0 )
        {
            fireIntervalRemoved ( this, 0, index - 1 );
        }
    }

    /**
     * Removes all elements from this list which has index larger than the specified one.
     *
     * @param index index to process
     */
    public void removeAllAfter ( final int index )
    {
        final int lastIndex = getSize () - 1;
        for ( int i = lastIndex; i > index; i-- )
        {
            delegate.removeElementAt ( i );
        }
        if ( lastIndex > index )
        {
            fireIntervalRemoved ( this, index + 1, lastIndex );
        }
    }

    /**
     * Returns an array containing all of the elements in this list in the correct order.
     *
     * @return an array containing the elements of the list
     * @see Vector#toArray()
     */
    public Object[] toArray ()
    {
        final Object[] rv = new Object[ delegate.size () ];
        delegate.copyInto ( rv );
        return rv;
    }

    /**
     * Returns the element at the specified position in this list.
     * Throws an {@code ArrayIndexOutOfBoundsException} if the index is out of range ({@code index &lt; 0 || index &gt;= size()}).
     *
     * @param index index of element to return
     * @return element at the specified position in this list
     */
    public T get ( final int index )
    {
        return delegate.elementAt ( index );
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * Throws an {@code ArrayIndexOutOfBoundsException} if the index is out of range ({@code index &lt; 0 || index &gt;= size()}).
     *
     * @param index   index of element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     */
    public T set ( final int index, final T element )
    {
        final T rv = delegate.elementAt ( index );
        delegate.setElementAt ( element, index );
        fireContentsChanged ( this, index, index );
        return rv;
    }

    /**
     * Clears list data and adds specified elements.
     *
     * @param elements elements to be added
     */
    public void setAll ( final T... elements )
    {
        setAll ( CollectionUtils.asList ( elements ) );
    }

    /**
     * Clears list data and adds specified elements.
     *
     * @param elements elements to be added
     */
    public void setAll ( final Collection<T> elements )
    {
        clear ();
        if ( elements.size () > 0 )
        {
            delegate.addAll ( elements );
            fireIntervalAdded ( this, 0, delegate.size () - 1 );
        }
    }

    /**
     * Adds the specified element to this list.
     *
     * @param element element to be added
     */
    public void add ( final T element )
    {
        add ( getSize (), element );
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Throws an {@code ArrayIndexOutOfBoundsException} if the index is out of range ({@code index &lt; 0 || index &gt; size()}).
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    public void add ( final int index, final T element )
    {
        delegate.insertElementAt ( element, index );
        fireIntervalAdded ( this, index, index );
    }

    /**
     * Adds specified elements to the end of this list.
     *
     * @param elements elements to be added
     */
    public void add ( final T... elements )
    {
        add ( CollectionUtils.asList ( elements ) );
    }

    /**
     * Adds specified elements to the end of this list.
     *
     * @param elements elements to be added
     */
    public void add ( final Collection<T> elements )
    {
        if ( elements.size () > 0 )
        {
            final int index = delegate.size ();
            delegate.addAll ( elements );
            fireIntervalAdded ( this, index, delegate.size () - 1 );
        }
    }

    /**
     * Removes the specified element from this list.
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
     * Removes the element at the specified position in this list.
     * Returns the element that was removed from the list.
     * Throws an {@code ArrayIndexOutOfBoundsException} if the index is out of range ({@code index &lt; 0 || index &gt;= size()}).
     *
     * @param index the index of the element to removed
     * @return removed element
     */
    public T remove ( final int index )
    {
        final T rv = delegate.elementAt ( index );
        delegate.removeElementAt ( index );
        fireIntervalRemoved ( this, index, index );
        return rv;
    }

    /**
     * Removes the specified elements from this list.
     * It ensures that minimal amount of {@link #fireIntervalRemoved(Object, int, int)} calls are made.
     *
     * @param objects the components to be removed
     */
    public void removeAll ( final T... objects )
    {
        removeAll ( CollectionUtils.asList ( objects ) );
    }

    /**
     * Removes the specified elements from this list.
     * todo If two+ non-distinct objects are provided - only one will be removed at a time
     *
     * @param objects the components to be removed
     */
    public void removeAll ( final Collection<T> objects )
    {
        // Collecting indices to remove
        final List<Integer> indices = new ArrayList<Integer> ( objects.size () );
        for ( final T object : objects )
        {
            final int index = delegate.indexOf ( object );
            if ( index != -1 )
            {
                indices.add ( index );
            }
        }

        // Making sure there are no duplicate indices
        CollectionUtils.distinct ( indices );

        // Sorting indices
        CollectionUtils.sort ( indices, new IntegerComparator () );

        // Collecting ranges
        int rangeStart = -1;
        int rangeEnd = -1;
        for ( int i = indices.size () - 1; i >= 0; i-- )
        {
            final int index = indices.get ( i );

            // Removing element
            delegate.remove ( index );

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

            // Firing on range end
            if ( i == 0 || rangeStart - 1 != indices.get ( i - 1 ) )
            {
                // We reached range border
                // Firing interval removal event
                fireIntervalRemoved ( this, rangeStart, rangeEnd );
            }
        }
    }

    /**
     * Removes all of the elements from this list.
     * The list will be empty after this call returns (unless it throws an exception).
     */
    public void clear ()
    {
        final int index1 = delegate.size () - 1;
        delegate.removeAllElements ();
        if ( index1 >= 0 )
        {
            fireIntervalRemoved ( this, 0, index1 );
        }
    }

    /**
     * Deletes the components at the specified range of indexes. The removal is inclusive, so specifying a range of (1,5) removes the
     * component at index 1 and the component at index 5, as well as all components in between.
     *
     * Throws an {@code ArrayIndexOutOfBoundsException} if the index was invalid.
     * Throws an {@code IllegalArgumentException} if {@code fromIndex &gt; toIndex}.
     *
     * @param fromIndex the index of the lower end of the range
     * @param toIndex   the index of the upper end of the range
     * @see #remove(int)
     */
    public void removeRange ( final int fromIndex, final int toIndex )
    {
        if ( fromIndex > toIndex )
        {
            throw new IllegalArgumentException ( "fromIndex must be <= toIndex" );
        }
        for ( int i = toIndex; i >= fromIndex; i-- )
        {
            delegate.removeElementAt ( i );
        }
        fireIntervalRemoved ( this, fromIndex, toIndex );
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