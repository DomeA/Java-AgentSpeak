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

package org.lightjason.agentspeak.configuration;

import org.lightjason.agentspeak.common.CCommon;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;


/**
 * default plan bundle configuration
 */
public class CDefaultPlanBundleConfiguration implements IPlanBundleConfiguration
{
    /**
     * logger
     */
    protected static final Logger LOGGER = CCommon.logger( IPlanBundleConfiguration.class );
    /**
     * instance of plans
     */
    private final Set<IPlan> m_plans;
    /**
     * rule instance
     */
    private final Set<IRule> m_rules;
    /**
     * instance of initial beliefs
     */
    private final Set<ILiteral> m_initialbeliefs;

    /**
     * ctor
     *
     * @param p_plans plans
     * @param p_rules rules
     * @param p_initialbeliefs initial beliefs
     */
    public CDefaultPlanBundleConfiguration( @Nonnull final Set<IPlan> p_plans, @Nonnull final Set<IRule> p_rules,
                                            @Nonnull final Set<ILiteral> p_initialbeliefs
    )
    {
        m_initialbeliefs = Collections.unmodifiableSet( p_initialbeliefs );
        m_plans = Collections.unmodifiableSet( p_plans );
        m_rules = Collections.unmodifiableSet( p_rules );

        LOGGER.info( MessageFormat.format( "create planbundle configuration: {0}", this ) );
    }

    @Nonnull
    @Override
    public final Set<ILiteral> beliefs()
    {
        return m_initialbeliefs;
    }

    @Nonnull
    @Override
    public final Set<IPlan> plans()
    {
        return m_plans;
    }

    @Nonnull
    @Override
    public final Set<IRule> rules()
    {
        return m_rules;
    }

}
