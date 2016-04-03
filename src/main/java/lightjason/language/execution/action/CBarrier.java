/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
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

package lightjason.language.execution.action;

import lightjason.language.CCommon;
import lightjason.language.ITerm;
import lightjason.language.execution.IContext;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.fuzzy.CFuzzyValue;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * barrier for synchronization
 *
 * @bug incomplete
 */
public final class CBarrier extends IBaseExecution<IExpression>
{
    /**
     * timeout
     */
    private final long m_timeout;


    /**
     * ctor
     *
     * @param p_expression expression
     */
    public CBarrier( final IExpression p_expression )
    {
        this( p_expression, Long.MAX_VALUE );
    }

    /**
     * ctor
     *
     * @param p_expression expression
     * @param p_timeout timeout
     */
    public CBarrier( final IExpression p_expression, final long p_timeout )
    {
        super( p_expression );
        m_timeout = p_timeout;
    }

    @Override
    public final IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final List<ITerm> l_argument = new LinkedList<>();
        if ( ( !m_value.execute( p_context, p_parallel, Collections.<ITerm>emptyList(), l_argument, Collections.<ITerm>emptyList() ).getValue() ) ||
             ( l_argument.size() != 1 ) )
            return CFuzzyValue.from( false );

        return CFuzzyValue.from( CCommon.getRawValue( l_argument.get( 0 ) ) );
    }

    @Override
    public final int hashCode()
    {
        return m_value.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "< {0}{1} >", m_value, m_timeout == Long.MAX_VALUE ? "" : MessageFormat.format( ", {0}", m_timeout ) );
    }
}
