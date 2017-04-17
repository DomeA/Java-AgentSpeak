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

package org.lightjason.agentspeak.action.buildin.collection;

import org.lightjason.agentspeak.action.buildin.IBuildinAction;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.fuzzy.CFuzzyValue;
import org.lightjason.agentspeak.language.execution.fuzzy.IFuzzyValue;

import java.util.List;


/**
 * abstract class to get a single elements of multiple maps
 *
 * @tparam T map instance
 */
public abstract class IMapGetSingle<T> extends IBuildinAction
{

    /**
     * ctor
     */
    protected IMapGetSingle()
    {
        super( 3 );
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
        CCommon.flatcollection( p_argument )
               .skip( 1 )
               .forEach( i ->  this.apply( i.<T>raw(), p_argument.get( 0 ).raw(), p_parallel, p_return ) );

        return CFuzzyValue.from( true );
    }


    /**
     * apply operation
     *
     * @param p_instance object instance
     * @param p_key key
     * @param p_parallel parallel flag
     * @param p_return return list
     */
    protected abstract void apply( final T p_instance, final Object p_key, final boolean p_parallel, final List<ITerm> p_return );

}