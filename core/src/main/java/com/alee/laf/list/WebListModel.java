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

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Modified and optimized Swing DefaultListModel.
 * This model contains multiply elements add/remove methods and works with typed elements.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class WebListModel<T> extends AbstractListModel
{
    /**
     * List data vector.
     */
    private Vector<T> delegate = new Vector<T> ();

    /**
     * Constructs empty model.
     */
    public WebListModel ()
    {
        super ();
    }

    /**
     * Constructs model with the specified elements.
     */
    public WebListModel ( T... data )
    {
        super ();
        Collections.addAll ( delegate, data );
    }

    /**
     * Constructs model with the specified elements.
     */
    public WebListModel ( Collection<T> data )
    {
        super ();
        delegate.addAll ( data );
    }

    /**
     * Returns the number of components in this list.
     * <p/>
     * This method is identical to <code>size</code>, which implements the <code>List</code> interface defined in the 1.2 Collections
     * framework. This method exists in conjunction with <code>setSize</code> so that <code>size</code> is identifiable as a JavaBean
     * property.
     *
     * @return the number of components in this list
     * @see #size()
     */
    public int getSize ()
    {
        return delegate.size ();
    }

    /**
     * Returns the component at the specified index. <blockquote> <b>Note:</b> Although this method is not deprecated, the preferred method
     * to use is <code>get(int)</code>, which implements the <code>List</code> interface defined in the 1.2 Collections framework.
     * </blockquote>
     *
     * @param index an index into this list
     * @return the component at the specified index
     * @throws ArrayIndexOutOfBoundsException if the <code>index</code> is negative or greater than the current size of this list
     * @see #get(int)
     */
    public T getElementAt ( int index )
    {
        return delegate.elementAt ( index );
    }

    /**
     * Copies the components of this list into the specified array. The array must be big enough to hold all the objects in this list, else
     * an <code>IndexOutOfBoundsException</code> is thrown.
     *
     * @param array the array into which the components get copied
     */
    public void copyInto ( T[] array )
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
    public void ensureCapacity ( int minCapacity )
    {
        delegate.ensureCapacity ( minCapacity );
    }

    /**
     * Sets the size of this list.
     *
     * @param newSize the new size of this list
     * @see Vector#setSize(int)
     */
    public void setSize ( int newSize )
    {
        int oldSize = delegate.size ();
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
     * @return <code>true</code> if and only if this list has no components, that is, its size is zero; <code>false</code> otherwise
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
     * @return <code>true</code> if the specified object is the same as a component in this list
     * @see Vector#contains(Object)
     */
    public boolean contains ( T elem )
    {
        return delegate.contains ( elem );
    }

    /**
     * Searches for the first occurrence of <code>elem</code>.
     *
     * @param elem an object
     * @return the index of the first occurrence of the argument in this list; returns <code>-1</code> if the object is not found
     * @see Vector#indexOf(Object)
     */
    public int indexOf ( T elem )
    {
        return delegate.indexOf ( elem );
    }

    /**
     * Searches for the first occurrence of <code>elem</code>, beginning the search at <code>index</code>.
     *
     * @param elem  an desired component
     * @param index the index from which to begin searching
     * @return the index where the first occurrence of <code>elem</code> is found after <code>index</code>; returns <code>-1</code> if the
     *         <code>elem</code> is not found in the list
     * @see Vector#indexOf(Object, int)
     */
    public int indexOf ( T elem, int index )
    {
        return delegate.indexOf ( elem, index );
    }

    /**
     * Returns the index of the last occurrence of <code>elem</code>.
     *
     * @param elem the desired component
     * @return the index of the last occurrence of <code>elem</code> in the list; returns <code>-1</code> if the object is not found
     * @see Vector#lastIndexOf(Object)
     */
    public int lastIndexOf ( T elem )
    {
        return delegate.lastIndexOf ( elem );
    }

    /**
     * Searches backwards for <code>elem</code>, starting from the specified index, and returns an index to it.
     *
     * @param elem  the desired component
     * @param index the index to start searching from
     * @return the index of the last occurrence of the <code>elem</code> in this list at position less than <code>index</code>; returns
     *         <code>-1</code> if the object is not found
     * @see Vector#lastIndexOf(Object, int)
     */
    public int lastIndexOf ( T elem, int index )
    {
        return delegate.lastIndexOf ( elem, index );
    }

    /**
     * Returns the component at the specified index. Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is negative or not
     * less than the size of the list. <blockquote> <b>Note:</b> Although this method is not deprecated, the preferred method to use is
     * <code>get(int)</code>, which implements the <code>List</code> interface defined in the 1.2 Collections framework. </blockquote>
     *
     * @param index an index into this list
     * @return the component at the specified index
     * @see #get(int)
     * @see Vector#elementAt(int)
     */
    public Object elementAt ( int index )
    {
        return delegate.elementAt ( index );
    }

    /**
     * Returns the first component of this list. Throws a <code>NoSuchElementException</code> if this vector has no components.
     *
     * @return the first component of this list
     * @see Vector#firstElement()
     */
    public Object firstElement ()
    {
        return delegate.firstElement ();
    }

    /**
     * Returns the last component of the list. Throws a <code>NoSuchElementException</code> if this vector has no components.
     *
     * @return the last component of the list
     * @see Vector#lastElement()
     */
    public Object lastElement ()
    {
        return delegate.lastElement ();
    }

    /**
     * Sets the component at the specified <code>index</code> of this list to be the specified object. The previous component at that
     * position is discarded.
     * <p/>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is invalid. <blockquote> <b>Note:</b> Although this method is not
     * deprecated, the preferred method to use is <code>set(int,Object)</code>, which implements the <code>List</code> interface defined in
     * the 1.2 Collections framework. </blockquote>
     *
     * @param obj   what the component is to be set to
     * @param index the specified index
     * @see #set(int, Object)
     * @see Vector#setElementAt(Object, int)
     */
    public void setElementAt ( T obj, int index )
    {
        delegate.setElementAt ( obj, index );
        fireContentsChanged ( this, index, index );
    }

    /**
     * Deletes the component at the specified index.
     * <p/>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is invalid. <blockquote> <b>Note:</b> Although this method is not
     * deprecated, the preferred method to use is <code>remove(int)</code>, which implements the <code>List</code> interface defined in the
     * 1.2 Collections framework. </blockquote>
     *
     * @param index the index of the object to remove
     * @see #remove(int)
     * @see Vector#removeElementAt(int)
     */
    public void removeElementAt ( int index )
    {
        delegate.removeElementAt ( index );
        fireIntervalRemoved ( this, index, index );
    }

    /**
     * Inserts the specified object as a component in this list at the specified <code>index</code>.
     * <p/>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is invalid. <blockquote> <b>Note:</b> Although this method is not
     * deprecated, the preferred method to use is <code>add(int,Object)</code>, which implements the <code>List</code> interface defined in
     * the 1.2 Collections framework. </blockquote>
     *
     * @param obj   the component to insert
     * @param index where to insert the new component
     * @throws ArrayIndexOutOfBoundsException if the index was invalid
     * @see #add(int, Object)
     * @see Vector#insertElementAt(Object, int)
     */
    public void insertElementAt ( T obj, int index )
    {
        delegate.insertElementAt ( obj, index );
        fireIntervalAdded ( this, index, index );
    }

    /**
     * Adds the specified component to the end of this list.
     *
     * @param obj the component to be added
     * @see Vector#addElement(Object)
     */
    public void addElement ( T obj )
    {
        int index = delegate.size ();
        delegate.addElement ( obj );
        fireIntervalAdded ( this, index, index );
    }

    /**
     * Adds the specified components to the end of this list.
     *
     * @param objects the components to be added
     */
    public void addElements ( T... objects )
    {
        if ( objects.length > 0 )
        {
            int index = delegate.size ();
            Collections.addAll ( delegate, objects );
            fireIntervalAdded ( this, index, delegate.size () - 1 );
        }
    }

    /**
     * Adds the specified components to the end of this list.
     *
     * @param objects the components to be added
     */
    public void addElements ( Collection<T> objects )
    {
        if ( objects.size () > 0 )
        {
            int index = delegate.size ();
            delegate.addAll ( objects );
            fireIntervalAdded ( this, index, delegate.size () - 1 );
        }
    }

    /**
     * Clears list data and adds specified elements.
     *
     * @param objects the components to be added
     */
    public void setElements ( Collection<T> objects )
    {
        clear ();
        if ( objects.size () > 0 )
        {
            delegate.addAll ( objects );
            fireIntervalAdded ( this, 0, delegate.size () - 1 );
        }
    }

    /**
     * Removes the first (lowest-indexed) occurrence of the argument from this list.
     *
     * @param object the component to be removed
     * @return <code>true</code> if the argument was a component of this list; <code>false</code> otherwise
     * @see Vector#removeElement(Object)
     */
    public boolean removeElement ( T object )
    {
        int index = indexOf ( object );
        boolean rv = delegate.removeElement ( object );
        if ( index >= 0 )
        {
            fireIntervalRemoved ( this, index, index );
        }
        return rv;
    }

    /**
     * Removes the specified elements from this list.
     *
     * @param objects the components to be removed
     */
    public void removeElements ( T... objects )
    {
        for ( T object : objects )
        {
            removeElement ( object );
        }
    }

    /**
     * Removes the specified elements from this list.
     *
     * @param objects the components to be removed
     */
    public void removeElements ( Collection<T> objects )
    {
        for ( T object : objects )
        {
            removeElement ( object );
        }
    }

    /**
     * Removes all components from this list and sets its size to zero. <blockquote> <b>Note:</b> Although this method is not deprecated,
     * the preferred method to use is <code>clear</code>, which implements the <code>List</code> interface defined in the 1.2 Collections
     * framework. </blockquote>
     *
     * @see #clear()
     * @see Vector#removeAllElements()
     */
    public void removeAllElements ()
    {
        int index1 = delegate.size () - 1;
        delegate.removeAllElements ();
        if ( index1 >= 0 )
        {
            fireIntervalRemoved ( this, 0, index1 );
        }
    }

    /**
     * Removes all elements from this list which has index lower than the specified one.
     *
     * @param index index to process
     */
    public void removeAllBefore ( int index )
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
    public void removeAllAfter ( int index )
    {
        final int lastIndex = size () - 1;
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
     * Returns a string that displays and identifies this object's properties.
     *
     * @return a String representation of this object
     */
    public String toString ()
    {
        return delegate.toString ();
    }

    /**
     * Returns an array containing all of the elements in this list in the correct order.
     *
     * @return an array containing the elements of the list
     * @see Vector#toArray()
     */
    public Object[] toArray ()
    {
        Object[] rv = new Object[ delegate.size () ];
        delegate.copyInto ( rv );
        return rv;
    }

    /**
     * Returns the element at the specified position in this list.
     * <p/>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out of range (<code>index &lt; 0 || index &gt;=
     * size()</code>).
     *
     * @param index index of element to return
     */
    public T get ( int index )
    {
        return delegate.elementAt ( index );
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     * <p/>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out of range (<code>index &lt; 0 || index &gt;=
     * size()</code>).
     *
     * @param index   index of element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     */
    public T set ( int index, T element )
    {
        T rv = delegate.elementAt ( index );
        delegate.setElementAt ( element, index );
        fireContentsChanged ( this, index, index );
        return rv;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * <p/>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out of range (<code>index &lt; 0 || index &gt; size()</code>).
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    public void add ( int index, T element )
    {
        delegate.insertElementAt ( element, index );
        fireIntervalAdded ( this, index, index );
    }

    /**
     * Removes the element at the specified position in this list. Returns the element that was removed from the list.
     * <p/>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index is out of range (<code>index &lt; 0 || index &gt;=
     * size()</code>).
     *
     * @param index the index of the element to removed
     */
    public T remove ( int index )
    {
        T rv = delegate.elementAt ( index );
        delegate.removeElementAt ( index );
        fireIntervalRemoved ( this, index, index );
        return rv;
    }

    /**
     * Removes all of the elements from this list.  The list will be empty after this call returns (unless it throws an exception).
     */
    public void clear ()
    {
        int index1 = delegate.size () - 1;
        delegate.removeAllElements ();
        if ( index1 >= 0 )
        {
            fireIntervalRemoved ( this, 0, index1 );
        }
    }

    /**
     * Deletes the components at the specified range of indexes. The removal is inclusive, so specifying a range of (1,5) removes the
     * component at index 1 and the component at index 5, as well as all components in between.
     * <p/>
     * Throws an <code>ArrayIndexOutOfBoundsException</code> if the index was invalid. Throws an <code>IllegalArgumentException</code> if
     * <code>fromIndex &gt; toIndex</code>.
     *
     * @param fromIndex the index of the lower end of the range
     * @param toIndex   the index of the upper end of the range
     * @see #remove(int)
     */
    public void removeRange ( int fromIndex, int toIndex )
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
}