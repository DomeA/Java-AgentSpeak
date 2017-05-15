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

package org.lightjason.agentspeak.language.instantiable.rule;

import com.google.common.collect.Multimap;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;


/**
 * placeholder rule to define correct rule referencing
 *
 * @note rules are the first executable elements which are parsed,
 * so if a rule calls itself (recursive) the reference does not exists,
 * with this class a placeholder is used first and after that we replace
 * the placeholder with the correct rule object
 */
public final class CRulePlaceholder implements IRule
{
    /**
     * identifier of the rule
     */
    private final ILiteral m_id;

    /**
     * ctor
     *
     * @param p_id rule literal
     */
    public CRulePlaceholder( final ILiteral p_id )
    {
        m_id = p_id;
    }


    @Override
    public final ILiteral identifier()
    {
        return m_id;
    }

    @Override
    public final IRule replaceplaceholder( final Multimap<IPath, IRule> p_rules )
    {
        return this;
    }

    @Override
    public final IContext instantiate( final IAgent<?> p_agent,
                                       final Stream<IVariable<?>> p_variable
    )
    {
        return CCommon.instantiate( this, p_agent, p_variable );
    }

    @Override
    public IFuzzyValue<Boolean> execute( final IContext p_context, final boolean p_parallel, final List<ITerm> p_argument, final List<ITerm> p_return
    )
    {
        return CFuzzyValue.from( false );
    }

    @Override
    public final double score( final IAgent<?> p_agent )
    {
        return 0;
    }

    @Override
    public final Stream<IVariable<?>> variables()
    {
        return Stream.<IVariable<?>>empty();
    }

    @Override
    public final int hashCode()
    {
        return m_id.hashCode();
    }

    @Override
    public final boolean equals( final Object p_object )
    {
        return ( p_object != null ) && ( p_object instanceof IRule ) && ( this.hashCode() == p_object.hashCode() );
    }

    @Override
    public final String toString()
    {
        return MessageFormat.format(
            "{0} ({1} ==>> ?)",
            super.toString(),
            m_id
        );
    }

}
