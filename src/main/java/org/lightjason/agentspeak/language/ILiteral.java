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

package org.lightjason.agentspeak.language;

import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.execution.IContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;


/**
 * literal interface
 *
 * @note closed world assumption, no negation marker needed
 */
public interface ILiteral extends ITerm, IStructureHash, IShallowCopy<ILiteral>, Comparable<ILiteral>
{
    /**
     * empty literal
     */
    ILiteral EMPTY = new ILiteral()
    {
        @Nonnull
        @Override
        public final Stream<ITerm> values( final IPath... p_path )
        {
            return Stream.empty();
        }

        @Nonnull
        @Override
        public final Stream<ITerm> orderedvalues( final IPath... p_path )
        {
            return Stream.empty();
        }

        @Override
        public final boolean emptyValues()
        {
            return true;
        }

        @Override
        public boolean negated()
        {
            return false;
        }

        @Override
        public final boolean hasAt()
        {
            return false;
        }

        @Nonnull
        @Override
        public final ILiteral unify( @Nonnull final IContext p_context )
        {
            return this;
        }

        @Nonnull
        @Override
        public final ILiteral allocate( @Nonnull final IContext p_context )
        {
            return this;
        }

        @Override
        public final int compareTo( @Nonnull final ILiteral p_literal )
        {
            return Integer.compare( p_literal.hashCode(), this.hashCode() );
        }

        @Nonnull
        @Override
        public final String functor()
        {
            return "";
        }

        @Nonnull
        @Override
        public IPath functorpath()
        {
            return IPath.EMPTY;
        }

        @Nonnull
        @Override
        public IPath fqnfunctor()
        {
            return IPath.EMPTY;
        }

        @Override
        public boolean hasVariable()
        {
            return false;
        }

        @Nullable
        @Override
        @SuppressWarnings( "unchecked" )
        public final <T> T raw()
        {
            return (T) this;
        }

        @Nonnull
        @Override
        public final ITerm deepcopy( final IPath... p_prefix )
        {
            return this;
        }

        @Nonnull
        @Override
        public final ITerm deepcopysuffix()
        {
            return this;
        }

        @Nonnull
        @Override
        public final ILiteral shallowcopy( final IPath... p_prefix )
        {
            return this;
        }

        @Nonnull
        @Override
        public final ILiteral shallowcopysuffix()
        {
            return this;
        }

        @Override
        public final int structurehash()
        {
            return 0;
        }
    };

    /**
     * returns a stream over value items
     *
     * @param p_path optional filtering value names
     * (filtering values within values)
     * @return stream
     */
    @Nonnull
    Stream<ITerm> values( final IPath... p_path );

    /**
     * returns a stream over the ordered values
     * in sequential ordering
     */
    @Nonnull
    Stream<ITerm> orderedvalues( final IPath... p_path );

    /**
     * check for empty values
     *
     * @return empty flag
     */
    boolean emptyValues();

    /**
     * getter of the literal for the negation
     *
     * @return negated flag
     */
    boolean negated();

    /**
     * returns if the literal has an @ prefix
     *
     * @return prefix is set
     */
    boolean hasAt();

    /**
     * unifies variables if exists
     *
     * @param p_context current execution context
     * @return new literal instance with unified variables
     *
     * @note un-unifyable variables passwd into the result literal
     */
    @Nonnull
    ILiteral unify( @Nonnull final IContext p_context );

    /**
     * allocate all variables with the current context
     *
     * @param p_context current execution context
     * @return literal with replaced variable
     */
    @Nonnull
    ILiteral allocate( @Nonnull final IContext p_context );

}
