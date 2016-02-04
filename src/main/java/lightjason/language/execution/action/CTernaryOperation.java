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
import lightjason.language.IVariable;
import lightjason.language.execution.IContext;
import lightjason.language.execution.IExecution;
import lightjason.language.execution.expression.IExpression;
import lightjason.language.execution.fuzzy.CBoolean;
import lightjason.language.execution.fuzzy.IFuzzyValue;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * if-else structure
 */
public final class CTernaryOperation extends IBaseExecution<IExpression>
{

    /**
     * true execution block
     */
    private final IExecution m_true;
    /**
     * false execution block
     */
    private final IExecution m_false;


    /**
     * ctor
     *
     * @param p_expression expression
     * @param p_true true execution block
     * @param p_false false execution block
     */
    public CTernaryOperation( final IExpression p_expression, final IExecution p_true, final IExecution p_false )
    {
        super( p_expression );
        m_true = p_true;
        m_false = p_false;
    }


    @Override
    public final IFuzzyValue<Boolean> execute( final IContext<?> p_context, final Boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return,
                                               final List<ITerm> p_annotation
    )
    {
        final List<ITerm> l_argument = new LinkedList<>();
        if ( ( !m_value.execute( p_context, p_parallel, Collections.<ITerm>emptyList(), l_argument, Collections.<ITerm>emptyList() ).getValue() ) ||
             ( l_argument.size() != 1 ) )
            return CBoolean.from( false );

        return CCommon.getRawValue( l_argument.get( 0 ) )
               ? m_true.execute( p_context, p_parallel, Collections.<ITerm>emptyList(), p_return, Collections.<ITerm>emptyList() )
               : m_false.execute( p_context, p_parallel, Collections.<ITerm>emptyList(), p_return, Collections.<ITerm>emptyList() );
    }

    @Override
    public final int hashCode()
    {
        return m_value.hashCode() + m_true.hashCode() + m_false.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return this.hashCode() == p_object.hashCode();
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "[ {0} ] ? [ {1} ] : [ {2} ]", m_value, m_true, m_false );
    }

    @Override
    public final Set<IVariable<?>> getVariables()
    {
        return new HashSet<IVariable<?>>()
        {{
            addAll( m_true.getVariables() );
            addAll( m_false.getVariables() );
        }};
    }
}