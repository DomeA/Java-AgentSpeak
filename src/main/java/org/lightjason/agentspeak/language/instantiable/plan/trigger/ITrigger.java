/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.lightjason.agentspeak.language.instantiable.plan.trigger;

import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.IShallowCopy;


/**
 * event definition
 *
 * @note trigger are equal iif type and literal functor are equal
 * and number of arguments are equal, otherwise unification is used
 * to define the literal variables
 */
public interface ITrigger extends IShallowCopy<ITrigger>
{

    /**
     * returns the type of the event
     *
     * @return type
     */
    EType getType();

    /**
     * returns the literal of the event
     *
     * @return literal
     */
    ILiteral getLiteral();

    /**
     * returns variable number
     *
     * @return number
     */
    int getVariableSize();

    /**
     * returns a hash value over the whole content
     * @return content hash value
     */
    int contenthash();



    /**
     * event types
     */
    enum EType
    {
        ADDBELIEF( "+" ),
        DELETEBELIEF( "-" ),
        ADDGOAL( "+!" ),
        DELETEGOAL( "-!" );

        /**
         * text name of the enum
         */
        private final String m_name;

        /**
         * ctor
         *
         * @param p_name text name
         */
        EType( final String p_name )
        {
            m_name = p_name;
        }

        @Override
        public String toString()
        {
            return m_name;
        }
    }

}
