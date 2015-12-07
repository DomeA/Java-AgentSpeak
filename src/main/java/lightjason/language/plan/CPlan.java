/**
 * @cond LICENSE
 * ######################################################################################
 * # GPL License                                                                        #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015, Philipp Kraus (philipp.kraus@tu-clausthal.de)                  #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU General Public License as                            #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU General Public License for more details.                                       #
 * #                                                                                    #
 * # You should have received a copy of the GNU General Public License                  #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package lightjason.language.plan;

import lightjason.beliefbase.IBeliefBaseMask;
import lightjason.language.ILiteral;
import lightjason.language.plan.annotation.CNumberAnnotation;
import lightjason.language.plan.annotation.IAnnotation;
import lightjason.language.plan.fuzzy.CBoolean;
import lightjason.language.plan.trigger.ITrigger;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;


/**
 * plan structure
 *
 * @todo annotations are missing
 * @todo scoring is missing
 * @todo hashcode / equals are missing
 */
public class CPlan implements IPlan
{
    /**
     * plan literal
     **/
    protected final ILiteral m_literal;
    /**
     * trigger event
     */
    protected final ITrigger<?> m_triggerevent;
    /**
     * current plan state
     */
    protected EState m_currentstate = EState.SUCCESS;
    /**
     * number of runs
     */
    protected long m_runs = 0;
    /**
     * number of fail runs
     */
    protected long m_failruns = 0;
    /**
     * action list
     */
    protected final List<IBodyAction> m_action;
    /**
     * map with annotation (enum value for getting annotation object)
     */
    protected final Map<IAnnotation.EType, IAnnotation<?>> m_annotation;

    /**
     * ctor
     *
     * @param p_event trigger event
     * @param p_literal head literal
     * @param p_body plan body
     * @param p_annotation annotations
     */
    public CPlan( final ITrigger<?> p_event, final ILiteral p_literal, final List<IBodyAction> p_body, final Set<IAnnotation<?>> p_annotation )
    {
        m_literal = p_literal;
        m_triggerevent = p_event;
        m_action = Collections.unmodifiableList( p_body );
        m_annotation = addDefault( p_annotation );
    }

    @Override
    public ITrigger<?> getTrigger()
    {
        return m_triggerevent;
    }

    @Override
    public EState getState()
    {
        return m_currentstate;
    }

    @Override
    public Collection<IAnnotation<?>> getAnnotations()
    {
        return m_annotation.values();
    }

    @Override
    public List<IBodyAction> getBodyActions()
    {
        return m_action;
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0} ({1} | {2} |- {3} = {4})", super.toString(), m_annotation.values(), m_triggerevent, m_literal, m_action );
    }

    /**
     * @todo annotation handling is missing
     */
    @Override
    public CBoolean execute( final IBeliefBaseMask p_beliefbase, final Map<ILiteral, IPlan> p_runningplan )
    {
        return new CBoolean(
                ( m_annotation.containsKey( IAnnotation.EType.ATOMIC ) ) ||
                ( ( m_annotation.containsKey( IAnnotation.EType.PARALLEL ) )
                        ? m_action.parallelStream()
                        : m_action.stream()
                ).map( i -> i.execute( p_beliefbase, p_runningplan ) ).allMatch( Predicate.isEqual( true ) )
        );
    }

    /**
     * add default values to the annotation map
     *
     * @param p_annotation set with annotation
     * @return unmodifiable map
     */
    protected static Map<IAnnotation.EType, IAnnotation<?>> addDefault( final Set<IAnnotation<?>> p_annotation )
    {
        final Map<IAnnotation.EType, IAnnotation<?>> l_map = p_annotation.stream().collect( HashMap::new, ( m, s ) -> m.put( s.getID(), s ), Map::putAll );

        l_map.putIfAbsent( IAnnotation.EType.FUZZY, new CNumberAnnotation<>( IAnnotation.EType.FUZZY, 1.0 ) );
        return Collections.unmodifiableMap( l_map );
    }
}