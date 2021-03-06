/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
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

package org.lightjason.agentspeak.language.execution.lambda;

import com.codepoetics.protonpack.Indexed;
import com.codepoetics.protonpack.StreamUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.lightjason.agentspeak.error.context.CExecutionIllegealArgumentException;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IBaseExecution;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IExecution;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;


/**
 * lambda initialize with a range
 */
public final class CLambdaInitializeRange extends IBaseExecution<IExecution[]>
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 8945262605702580850L;

    /**
     * ctor
     *
     * @param p_value data
     */
    public CLambdaInitializeRange( @Nonnull final Stream<IExecution> p_value )
    {
        super( p_value.toArray( IExecution[]::new ) );
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context, @Nonnull final List<ITerm> p_argument,
                                           @Nonnull final List<ITerm> p_return )
    {
        final List<ITerm> l_range = CCommon.argumentlist();
        final Pair<List<IFuzzyValue<?>>, Boolean> l_result = CCommon.executesequential( p_parallel, p_context, p_argument, l_range, Arrays.stream( m_value ) );
        if ( !l_result.getValue() )
            return p_context.agent().fuzzy().membership().fail();

        if ( l_range.size() == 0 || l_range.size() > 3 )
            throw new CExecutionIllegealArgumentException(
                p_context,
                org.lightjason.agentspeak.common.CCommon.languagestring( this, "wrongargumentnumber", 1, 3 )
            );

        generate( l_range, p_return );
        return l_result.getKey().stream();
    }

    /**
     * generate streams
     *
     * @param p_range range definition
     * @param p_return return arguments
     */
    private static void generate( @Nonnull final List<ITerm> p_range, @Nonnull final List<ITerm> p_return )
    {
        switch ( p_range.size() )
        {
            case 1:
                p_return.add(
                    CRawTerm.of(
                        LongStream.range( 0, p_range.get( 0 ).<Number>raw().longValue() ).boxed()
                    )
                );
                break;

            case 2:
                p_return.add(
                    CRawTerm.of(
                        LongStream.range( p_range.get( 0 ).<Number>raw().longValue(), p_range.get( 1 ).<Number>raw().longValue() ).boxed()
                    )
                );
                break;

            case 3:
                p_return.add(
                    CRawTerm.of(
                        StreamUtils.zipWithIndex(
                            LongStream.range( p_range.get( 0 ).<Number>raw().longValue(), p_range.get( 1 ).<Number>raw().longValue() ).boxed()
                        ).filter( i -> i.getIndex() % p_range.get( 2 ).<Number>raw().longValue() == 0 ).map( Indexed::getValue )
                    )
                );
                break;

            default:
        }
    }

    @Nonnull
    @Override
    public Stream<IVariable<?>> variables()
    {
        return Arrays.stream( m_value ).flatMap( IExecution::variables );
    }

    @Override
    public String toString()
    {
        return "# " + Arrays.stream( m_value ).map( Object::toString ).collect( Collectors.joining( ", " ) );
    }
}
