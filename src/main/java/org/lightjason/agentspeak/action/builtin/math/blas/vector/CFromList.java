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

package org.lightjason.agentspeak.action.builtin.math.blas.vector;

import cern.colt.matrix.tdouble.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.tdouble.impl.SparseDoubleMatrix1D;
import org.lightjason.agentspeak.action.builtin.IBuiltinAction;
import org.lightjason.agentspeak.action.builtin.math.blas.EType;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;


/**
 * creates a dense- or sparse-vector of a list.
 * All input arguments will be converted to a
 * dense or sparse vector, so the arguments must be
 * lists of numbers, the last optional argument can be a string
 * with "dense | sparse" to create dense or sparse structures,
 * the action never fails
 *
 * {@code [V1|V2] = .math/blas/vector( [1,2,3], [4,5,6], "dense | sparse" );}
 */
public final class CFromList extends IBuiltinAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -144138778430324185L;

    /**
     * ctor
     */
    public CFromList()
    {
        super( 4 );
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public IFuzzyValue<Boolean> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                         @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return )
    {
        final int l_limit;
        final EType l_type;
        if ( CCommon.isssignableto( p_argument.get( p_argument.size() - 1 ), String.class )
             && EType.exists( p_argument.get( p_argument.size() - 1 ).<String>raw() ) )
        {
            l_type = EType.of( p_argument.get( p_argument.size() - 1 ).<String>raw() );
            l_limit = p_argument.size() - 1;
        }
        else
        {
            l_type = EType.DENSE;
            l_limit = p_argument.size();
        }


        // create vectors of lists
        p_argument.stream()
                  .limit( l_limit )
                  .map( ITerm::<List<Number>>raw )
                  .map( i -> i.stream().mapToDouble( Number::doubleValue ).toArray() )
                  .map( i ->
                  {
                      switch ( l_type )
                      {
                          case SPARSE:
                              return new SparseDoubleMatrix1D( i );
                          default:
                              return new DenseDoubleMatrix1D( i );
                      }
                  } )
                  .map( CRawTerm::of )
                  .forEach( p_return::add );

        return CFuzzyValue.of( true );
    }

}
