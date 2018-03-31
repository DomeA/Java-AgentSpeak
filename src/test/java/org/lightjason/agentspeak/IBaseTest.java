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

package org.lightjason.agentspeak;

import org.apache.commons.io.IOUtils;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.agent.IBaseAgent;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.configuration.IAgentConfiguration;
import org.lightjason.agentspeak.generator.IBaseAgentGenerator;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.execution.IVariableBuilder;
import org.lightjason.agentspeak.language.execution.instantiable.IInstantiable;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.lambda.ILambdaStreaming;
import org.lightjason.agentspeak.language.variable.IVariable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * base test class with helpers
 */
public abstract class IBaseTest
{
    /**
     * enable printing of test-data
     */
    protected static final boolean PRINTENABLE = Files.exists( Paths.get( "agentprinting.conf" ) );

    /**
     * returns a class property
     *
     * @param p_name name
     * @param p_object object
     * @tparam N return type
     * @return value of the field
     *
     * @throws NoSuchFieldException is thrown if field does not exists
     * @throws IllegalAccessException is thrown if field cannot be read
     */
    @Nullable
    @SuppressWarnings( "unchecked" )
    protected static <N> N property( @Nonnull final String p_name, @Nonnull final Object p_object ) throws NoSuchFieldException, IllegalAccessException
    {

        final Field l_field = fields( p_object.getClass() )
                                .filter( i -> i.getName().equals( p_name ) )
                                .findFirst()
                                .orElseThrow( IllegalArgumentException::new );
        l_field.setAccessible( true );
        return (N) l_field.get( p_object );
    }

    /**
     * stream of all class fields
     *
     * @param p_class class or null
     * @return field stream
     */
    @Nonnull
    private static Stream<Field> fields( @Nullable final Class<?> p_class )
    {
        if ( Objects.isNull( p_class ) )
            return Stream.empty();

        return Stream.concat(
            Arrays.stream( p_class.getDeclaredFields() ),
            fields( p_class.getSuperclass() )
        );
    }

    /**
     * creates a stream of a string value
     *
     * @param p_input string input
     * @return input stream
     * @throws IOException thrown on error
     */
    protected static InputStream streamfromstring( @Nonnull final String p_input ) throws IOException
    {
        return IOUtils.toInputStream(  p_input,  "UTF-8" );
    }

    /**
     * generator of empty agents
     */
    protected static final class CAgentGenerator extends IBaseAgentGenerator<IAgent<?>>
    {
        /**
         * ctor
         *
         * @throws Exception is thrown on any error
         */
        public CAgentGenerator() throws Exception
        {
            this( "", Collections.emptySet(), Collections.emptySet() );
        }

        /**
         * ctor
         *
         * @param p_asl asl code
         * @throws Exception is thrown on any error
         */
        public CAgentGenerator( @Nonnull final String p_asl ) throws Exception
        {
            this( p_asl, Collections.emptySet(), Collections.emptySet() );
        }

        /**
         * ctor
         *
         * @param p_asl asl code
         * @throws Exception is thrown on any error
         */
        public CAgentGenerator( @Nonnull final InputStream p_asl ) throws Exception
        {
            super( p_asl, Collections.emptySet(), Collections.emptySet() );
        }

        /**
         * ctor
         *
         * @param p_asl asl code
         * @param p_action actions
         * @param p_lambdastreaming lambda streaming
         * @throws Exception is thrown on any error
         */
        public CAgentGenerator( @Nonnull final String p_asl, @Nonnull final Set<IAction> p_action, @Nonnull final Set<ILambdaStreaming<?>> p_lambdastreaming )
            throws Exception
        {
            super( IOUtils.toInputStream( p_asl, "UTF-8" ), p_action, p_lambdastreaming );
        }

        /**
         * ctor
         *
         * @param p_asl asl code
         * @param p_action actions
         * @throws Exception is thrown on any error
         */
        public CAgentGenerator( @Nonnull final String p_asl, @Nonnull final Set<IAction> p_action,
                                @Nonnull final Set<ILambdaStreaming<?>> p_lambdastreaming, @Nonnull final IVariableBuilder p_variablebuilder ) throws Exception
        {
            super( IOUtils.toInputStream( p_asl, "UTF-8" ), p_action, p_lambdastreaming, p_variablebuilder );
        }

        /**
         * ctor
         *
         * @param p_asl asl code
         * @param p_action actions
         * @throws Exception is thrown on any error
         */
        public CAgentGenerator( @Nonnull final InputStream p_asl, @Nonnull final Set<IAction> p_action,
                                @Nonnull final Set<ILambdaStreaming<?>> p_lambdastreaming, @Nonnull final IVariableBuilder p_variablebuilder ) throws Exception
        {
            super( p_asl, p_action, p_lambdastreaming, p_variablebuilder );
        }

        /**
         * ctor
         *
         * @param p_asl asl code
         * @param p_action actions
         * @throws Exception is thrown on any error
         */
        public CAgentGenerator( @Nonnull final InputStream p_asl, @Nonnull final Set<IAction> p_action, @Nonnull final Set<ILambdaStreaming<?>> p_lambdastreaming )
            throws Exception
        {
            super( p_asl, p_action, p_lambdastreaming );
        }

        @Nullable
        @Override
        public final IAgent<?> generatesingle( @Nullable final Object... p_data )
        {
            return new CAgent( m_configuration );
        }
    }

    /**
     * agent class
     */
    private static final class CAgent extends IBaseAgent<IAgent<?>>
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = 3961697445753327536L;

        /**
         * ctor
         *
         * @param p_configuration agent configuration
         */
        CAgent( @Nonnull final IAgentConfiguration<IAgent<?>> p_configuration )
        {
            super( p_configuration );
        }
    }

    /**
     * local context
     */
    protected static final class CLocalContext implements IContext
    {
        /**
         * serial id
         */
        private static final long serialVersionUID = -1766581015543678832L;
        /**
         * variable map
         */
        private final Map<IPath, IVariable<?>> m_variables;
        /**
         * agent
         */
        private final IAgent<?> m_agent;

        /**
         * ctor
         *
         * @param p_variables variables
         */
        public CLocalContext( @Nullable final IVariable<?>... p_variables )
        {
            this( IAgent.EMPTY, p_variables );
        }

        /**
         * ctor
         *
         * @param p_agent agent
         * @param p_variables variables
         */
        public CLocalContext( @Nonnull final IAgent<?> p_agent, @Nullable final IVariable<?>... p_variables )
        {
            m_agent = p_agent;
            m_variables = Objects.isNull( p_variables )
                          ? Collections.emptyMap()
                          : Arrays.stream( p_variables )
                                  .collect( Collectors.toMap( ITerm::fqnfunctor, i -> i ) );
        }



        @Nonnull
        @Override
        public final IAgent<?> agent()
        {
            return m_agent;
        }

        @Nonnull
        @Override
        public final IInstantiable instance()
        {
            return IPlan.EMPTY;
        }

        @Nonnull
        @Override
        public final Map<IPath, IVariable<?>> instancevariables()
        {
            return m_variables;
        }

        @Nonnull
        @Override
        public final IContext duplicate()
        {
            return this;
        }
    }
}
