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

package org.lightjason.agentspeak.action.buildin;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.IBaseTest;

import org.lightjason.agentspeak.action.buildin.math.blas.CElementWise;
import org.lightjason.agentspeak.action.buildin.math.blas.CSize;
import org.lightjason.agentspeak.action.buildin.math.blas.CMultiply;

import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * test math blas functions
 */
public class TestCActionBlas extends IBaseTest
{
    /**
     * testing matrix
     */
    private DoubleMatrix2D m_matrix = new DenseDoubleMatrix2D( new double[][]{{2, 6}, {3, 8}} );

    /**
     * testing matrix
     */
    private DoubleMatrix2D m_matrix1 = new DenseDoubleMatrix2D( new double[][]{{2, 2}, {3, 1}} );

    /**
     * test size
     */
    @Test
    public final void size()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSize().execute(
                null,
                false,
                Stream.of( m_matrix ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertEquals( l_return.get( 0 ).<Number>raw(), 4L );
    }

    /**
     * test multiply
     */
    @Test
    public final void multiply()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMultiply().execute(
                null,
                false,
                Stream.of( m_matrix, m_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertTrue( l_return.get( 0 ).raw() instanceof DoubleMatrix2D );

        Assert.assertArrayEquals( l_return.get( 0 ).<DoubleMatrix2D>raw().toArray(), new double[][]{{22.0, 10.0}, {30.0, 14.0}} );

    }

    /**
     * test elementwise
     */
    @Test
    public final void elementWise()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CElementWise().execute(
                null,
                false,
                Stream.of( m_matrix, "+", m_matrix1 ).map( CRawTerm::from ).collect( Collectors.toList() ),
                l_return,
                Collections.emptyList()
        );

        Assert.assertEquals( l_return.size(), 1 );
        Assert.assertArrayEquals( l_return.get( 0 ).<DoubleMatrix2D>raw().toArray(), new double[][]{{4.0, 8.0}, {6.0, 9.0}} );
    }


    /**
     * test call
     *
     * @param p_args command-line arguments
     */
    public static void main( final String[] p_args )
    {
        new TestCActionBlas().invoketest();
    }
}