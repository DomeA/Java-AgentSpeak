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

package org.lightjason.agentspeak.action.buildin.math.blas.vector;

import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.action.buildin.math.blas.EType;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.Arrays;
import java.util.List;


/**
 * creates a dense- or sparse-vector from as string.
 *
 * @note seperator is comma, semicolon or space
 */
public final class CParse extends IBuildinAction
{
    /**
     * ctor
     */
    public CParse()
    {
        super( 4 );
    }

    @Override
    public final int minimalArgumentNumber()
    {
        return 1;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        switch ( p_argument.size() > 1 ? EType.from( p_argument.get( 1 ).<String>raw() ) : EType.DENSE )
        {
            case DENSE:
                p_return.add( CRawTerm.from( new DenseDoubleMatrix1D( CParse.parse( p_argument.get( 0 ).<String>raw() ) ) ) );
                break;

            case SPARSE:
                p_return.add( CRawTerm.from( new SparseDoubleMatrix1D( CParse.parse( p_argument.get( 0 ).<String>raw() ) ) ) );
                break;

            default:
        }
        return CFuzzyValue.from( true );
    }

    /**
     * parses the string
     *
     * @param p_string string
     * @return double array
     */
    private static double[] parse( final String p_string )
    {
        return Arrays.stream( p_string.split( ";|,|\\s" ) )
                     .map( String::trim )
                     .filter( i -> !i.isEmpty() )
                     .mapToDouble( Double::parseDouble )
                     .toArray();
    }

}
