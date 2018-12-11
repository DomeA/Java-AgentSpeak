/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
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

package org.lightjason.agentspeak.action.builtin.graph;

import edu.uci.ics.jung.graph.Graph;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import javax.annotation.Nonnull;
import java.util.List;


/**
 * checks all vertex tuples if the first part the successor of the second on a single graph instance.
 * The action checks for the first graph argument if the first part of all vertex tuples
 * a successor of the second part
 *
 * {@code [B1|B2|B3] = .graph/issuccessormultiple( Graph, Vertex1, Vertex2, [Vertex3, Vertex4, [Vertex5, Vertex6]] );}
 */
public final class CIsSuccessorMultiple extends IApplyMultiple
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -6052788842956233556L;

    @Override
    protected int windowsize()
    {
        return 2;
    }

    @Override
    protected void apply( final boolean p_parallel, @Nonnull final Graph<Object, Object> p_graph,
                          @Nonnull final List<ITerm> p_window, @Nonnull final List<ITerm> p_return )
    {
        p_return.add(
            CRawTerm.of(
                p_graph.isSuccessor( p_window.get( 0 ).raw(), p_window.get( 1 ).raw() )
            )
        );
    }
}
