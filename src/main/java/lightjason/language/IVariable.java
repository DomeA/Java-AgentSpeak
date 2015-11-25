/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.language;

/**
 * variable defintion
 */
public interface IVariable<T> extends ITerm, Cloneable
{
    /**
     * returns the name of the variable
     *
     * @return name
     */
    public String getName();

    /**
     * sets the value
     *
     * @param p_value
     */
    public void set( final T p_value );

    /**
     * gets the value
     *
     * @return value
     */
    public T get();

    /**
     * returns allocated state
     *
     * @return boolean flag
     */
    public boolean isAllocated();

    /**
     * checkes assinable of the value
     *
     * @param p_class class
     * @return assinable (on null always true)
     */
    public boolean isValueAssignableFrom( final Class<?> p_class );

}
