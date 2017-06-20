/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-17, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.beliefbase;

import org.lightjason.agentspeak.agent.IAgent;

import javax.annotation.Nonnull;


/**
 * interface for equal method on views and beliefbases
 *
 * @tparam T agent type
 */
public interface IStructure
{

    /**
     * checks if the structure empty
     *
     * @return empty boolean
     */
    boolean empty();

    /**
     * returns the size of literals
     *
     * @return size
     */
    int size();

    /**
     * updates all items
     *
     * @param p_agent agent which runs the update call
     * @return agent
     *
     * @warning call update on a storage and on all storage-view, if exists different views
     * which are point to the same storage, the update is called more than once, so the storage must
     * limit the number of update calls
     */
    @Nonnull
    IAgent<?> update( @Nonnull final IAgent<?> p_agent );

}
