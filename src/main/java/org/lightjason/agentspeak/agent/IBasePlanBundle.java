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

package org.lightjason.agentspeak.agent;

import org.lightjason.agentspeak.configuration.IPlanBundleConfiguration;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;

import javax.annotation.Nonnull;
import java.util.Set;


/**
 * plan bundle class
 */
public abstract class IBasePlanBundle implements IPlanBundle
{
    /**
     * map with all existing plans
     */
    protected final Set<IPlan> m_plans;
    /**
     * initial beliefs
     */
    protected final Set<ILiteral> m_initialbeliefs;
    /**
     * rule set
     */
    protected final Set<IRule> m_rules;


    /**
     * ctor
     *
     * @param p_configuration configuration
     */
    public IBasePlanBundle( @Nonnull final IPlanBundleConfiguration p_configuration )
    {
        m_plans = p_configuration.plans();
        m_initialbeliefs = p_configuration.beliefs();
        m_rules = p_configuration.rules();
    }

    @Nonnull
    @Override
    public final Set<ILiteral> initialbeliefs()
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
