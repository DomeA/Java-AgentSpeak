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

package org.lightjason.agentspeak.language.execution.expression;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.error.CIllegalArgumentException;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.stream.Stream;


/**
 * base unary expression
 */
public abstract class IBaseUnary implements IUnaryExpression
{
    /**
     * serial id
     */
    private static final long serialVersionUID = -8992581726613099958L;
    /**
     * expression operator
     */
    protected final EOperator m_operator;
    /**
     * left-hand-side argument
     */
    protected final IExpression m_expression;


    /**
     * ctor
     *
     * @param p_operator operator
     * @param p_expression expression
     */
    protected IBaseUnary( @Nonnull final EOperator p_operator, @Nonnull final IExpression p_expression )
    {
        if ( !p_operator.isUnary() )
            throw new CIllegalArgumentException( CCommon.languagestring( IBaseUnary.class, "operator", p_operator ) );

        m_operator = p_operator;
        m_expression = p_expression;
    }

    @Nonnull
    @Override
    public final IExpression expression()
    {
        return m_expression;
    }

    @Nonnull
    @Override
    public final EOperator operator()
    {
        return m_operator;
    }

    @Override
    public final int hashCode()
    {
        return m_expression.hashCode() ^ m_operator.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IExpression ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format( "{0}({1})", m_operator, m_expression );
    }

    @Nonnull
    @Override
    public final Stream<IVariable<?>> variables()
    {
        return m_expression.variables();
    }

}
