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

package lightjason.agent.generator;

import lightjason.agent.action.IAction;
import lightjason.grammar.CASTVisitorPlanBundle;
import lightjason.grammar.CErrorListener;
import lightjason.grammar.IASTVisitorPlanBundle;
import lightjason.grammar.IGenericParser;
import lightjason.grammar.PlanBundleLexer;
import lightjason.grammar.PlanBundleParser;
import lightjason.language.instantiable.rule.IRule;

import java.io.InputStream;
import java.util.Collections;
import java.util.Set;


/**
 * default planbundle parser
 */
public final class CDefaultPlanBundleParser extends IGenericParser<IASTVisitorPlanBundle, PlanBundleLexer, PlanBundleParser>
{
    /**
     * set with actions
     */
    final Set<IAction> m_actions;

    /**
     * ctor
     *
     * @param p_actions agent actions
     */
    public CDefaultPlanBundleParser( final Set<IAction> p_actions )
    {
        super( new CErrorListener() );
        m_actions = p_actions;
    }

    @Override
    public final IASTVisitorPlanBundle parse( final InputStream p_stream ) throws Exception
    {
        final IASTVisitorPlanBundle l_visitor = new CASTVisitorPlanBundle( m_actions, Collections.<IRule>emptySet() );
        l_visitor.visit( this.getParser( p_stream ).planbundle() );
        return l_visitor;
    }

    @Override
    protected final Class<PlanBundleLexer> getLexerClass()
    {
        return PlanBundleLexer.class;
    }

    @Override
    protected final Class<PlanBundleParser> getParserClass()
    {
        return PlanBundleParser.class;
    }
}