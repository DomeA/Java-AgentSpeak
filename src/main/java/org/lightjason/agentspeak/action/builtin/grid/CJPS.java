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

package org.lightjason.agentspeak.action.builtin.grid;

import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tobject.ObjectMatrix2D;
import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * calculte route via JPS+ algorithm.
 * Routing algorithm for gird environment, first argument
 * must be the grid and pairs of positions of the intermediate goal or
 * target goal. The action returns alist of numeric tuples which present
 * the landmarks
 *
 * @see https://www.gdcvault.com/play/1022094/JPS-Over-100x-Faster-than
 * @see https://github.com/SteveRabin/JPSPlusWithGoalBounding/tree/master/JPSPlusGoalBounding
 *
 * {@code L = .grid/jps(Grid, [1,2, 8,9, 20,20]);}
 */
public final class CJPS extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -4482586078227767194L;

    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CJPS.class, "grid" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_arguments = CCommon.flatten( p_argument ).collect( Collectors.toList() );

        // https://github.com/SteveRabin/JPSPlusWithGoalBounding/tree/master/JPSPlusGoalBounding
        // https://github.com/kevinsheehan/jps/blob/master/src/org/ksdev/jps/JPS.java
        // https://github.com/unrealgamer/Java-JPS/blob/master/JavaJPS/src/com/unreal/jps/Program.java
        // https://harablog.wordpress.com/2011/09/07/jump-point-search/
        // https://www.aaai.org/ocs/index.php/AAAI/AAAI16/paper/download/12336/11660

        // https://github.com/jonasnick/A-star/blob/master/astar/AStar.java

        return Stream.of();
    }


    private static List<DoubleMatrix1D> route( @Nonnull final ObjectMatrix2D p_grid, @Nonnull final DoubleMatrix1D p_start, @Nonnull final DoubleMatrix1D p_end )
    {
        // distance to start + estimate to end
        final Map<DoubleMatrix1D, Double> l_fscore = new ConcurrentHashMap<>();
        // distance to start (parent's g-score + distance from parent)
        final Map<DoubleMatrix1D, Double> l_gscore = new ConcurrentHashMap<>();
        // estimate to end
        final Map<DoubleMatrix1D, Double> l_hscore = new ConcurrentHashMap<>();

        // we want the nodes with the lowest projected F value to be checked first
        final Queue<DoubleMatrix1D> l_open = new PriorityBlockingQueue<>(
            (int) p_grid.size() / 4,
            Comparator.comparingDouble( i -> l_fscore.getOrDefault( i, 0d ) )
        );

        final Set<DoubleMatrix1D> l_goals = Collections.synchronizedSet( new HashSet<>() );
        final Set<DoubleMatrix1D> l_closed = Collections.synchronizedSet( new HashSet<>() );
        //final Map<INode, INode> l_parent = new ConcurrentHashMap<>();

        // adjaceny stop

        if ( l_goals.isEmpty() )
            return Collections.emptyList();

        l_open.add( p_start );
        while ( !l_goals.isEmpty() )
        {
            final DoubleMatrix1D l_node = l_open.poll();
            l_closed.add( l_node );

            if ( l_goals.contains( l_node ) )
                return backtrace();

            successors( p_grid, l_node, p_end, l_closed, l_open );
        }


        return Collections.emptyList();
    }


    private static void successors( final ObjectMatrix2D p_objects, final DoubleMatrix1D p_current, final DoubleMatrix1D p_end,
                                    final Set<DoubleMatrix1D> p_closed, final Queue<DoubleMatrix1D> p_open )
    {

    }

    private static List<DoubleMatrix1D> backtrace()
    {
        return Collections.emptyList();
    }

}