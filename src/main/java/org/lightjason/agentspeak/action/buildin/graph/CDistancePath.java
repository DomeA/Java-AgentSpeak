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

package org.lightjason.agentspeak.action.buildin.graph;

import com.google.common.base.Function;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * calculates the distance of a route within a graph.
 * The action calculates the distance based on a cost-edge-map,
 * so the first argument is a map of edges and numeric cost
 * values, the second and third argument are vertices with
 * start- and end-vertex of the route and all other arguments
 * are graph objects, for each graph object a cost value is
 * returned the action fails on wrong input
 *
 * @code [D1|D2] = graph/shortestpath( CostMap, StartVertex, EndVertex, Graph1, Graph2 ); @endcode
 *
 * @note the cost-map does not need an entry for each edge
 * non-existing edges have got on default zero costs
 */
public class CDistancePath extends IBuildinAction
{
    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation )
    {
        final List<ITerm> l_arguments = CCommon.flatcollection( p_argument ).collect( Collectors.toList() );
        if ( l_arguments.size() < 4 )
            return CFuzzyValue.from( false );


        final Map<Object, Number> l_weights = l_arguments.get( 0 ).<Map<Object, Number>>raw();
        final Function<Object, Number> l_weightfunction = ( e) -> l_weights.getOrDefault( e, 0 );

        l_arguments.stream()
                   .skip( 3 )
                   .map( ITerm::<Graph<Object, Object>>raw )
                   .map( i -> new DijkstraShortestPath<Object, Object>( i, l_weightfunction ) )
                   .map( i -> i.getDistance( l_arguments.get( 1 ).raw(), l_arguments.get( 2 ).raw() ) )
                   .mapToDouble( Number::doubleValue )
                   .boxed()
                   .map( CRawTerm::from )
                   .forEach( p_return::add );

        return CFuzzyValue.from( true );
    }
}
