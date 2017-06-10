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

package org.lightjason.agentspeak.beliefbase.view;

import edu.umd.cs.findbugs.annotations.NonNull;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.beliefbase.IBeliefbase;
import org.lightjason.agentspeak.beliefbase.IStructure;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.instantiable.plan.trigger.ITrigger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;


/**
 * view for a beliefbase that creates any access to the underlying
 * data structures
 *
 * @tparam T agent type
 */
public interface IView<T extends IAgent<?>> extends IStructure<T>
{

    /**
     * streams path walking
     *
     * @param p_path path
     * @param p_generator generator for view creating (first argument is used, orther elements will be ignored)
     * @return stream of views
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    Stream<IView<T>> walk( @Nonnull final IPath p_path, @Nullable final IViewGenerator<T>... p_generator );

    /**
     * generates path structure
     *
     * @param p_generator generator for views
     * @param p_paths paths items
     * @return self reference
     */
    @Nonnull
    IView<T> generate( @Nonnull final IViewGenerator<T> p_generator, @Nonnull final IPath... p_paths );

    /**
     * returns a stream to the root node
     *
     * @return stream of views
     */
    @Nonnull
    Stream<IView<T>> root();

    /**
     * returns the beliefbase
     *
     * @return beliefbase
     */
    @Nonnull
    IBeliefbase<T> beliefbase();

    /**
     * returns the full path
     *
     * @return path
     */
    @Nonnull
    IPath path();

    /**
     * returns only the element name
     *
     * @return name
     */
    @Nonnull
    String name();

    /**
     * returns the parent of the view
     *
     * @return parent object or null
     */
    @Nullable
    IView<T> parent();

    /**
     * check if the view has got a parent
     *
     * @return boolean flag of the parent
     */
    boolean hasParent();



    /**
     * retruns all trigger of the beliefbase
     *
     * @return set with trigger events
     */
    @Nonnull
    Stream<ITrigger> trigger();



    /**
     * returns stream of literal
     *
     * @param p_path paths of the literals
     * @return literal stream
     */
    @Nonnull
    Stream<ILiteral> stream( @Nullable final IPath... p_path );

    /**
     * returns stream of literal
     *
     * @param p_negated negated flag
     * @param p_path paths of the literals
     * @return literal stream
     */
    @Nonnull
    Stream<ILiteral> stream( final boolean p_negated, @Nullable final IPath... p_path );



    /**
     * clears all elements
     *
     * @param p_path path values
     * @return self reference
     */
    @Nonnull
    IView<T> clear( @Nullable final IPath... p_path );



    /**
     * adds a literal in the current structure
     *
     * @param p_literal literal stream
     * @return self reference
     */
    @Nonnull
    IView<T> add( @Nonnull final Stream<ILiteral> p_literal );

    /**
     * adds a literal in the current structure
     *
     * @param p_literal literal
     * @return self reference
     */
    @Nonnull
    IView<T> add( @Nonnull final ILiteral... p_literal );

    /**
     * adds view in the current structure
     *
     * @param p_view existing view
     * @return self reference
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    IView<T> add( @Nonnull final IView<T>... p_view );

    /**
     * adds view in the current structure
     *
     * @param p_path path
     * @param p_view existing view
     * @return self reference
     */
    @Nonnull
    @SuppressWarnings( "unchecked" )
    IView<T> add( @Nonnull final IPath p_path, @Nonnull final IView<T>... p_view );



    /**
     * removes a literal in the current structure
     *
     * @param p_literal literal stream
     * @return self reference
     */
    @Nonnull
    IView<T> remove( @Nonnull final Stream<ILiteral> p_literal );

    /**
     * removes a literal in the current structure
     *
     * @param p_literal literal
     * @return self reference
     */
    @Nonnull
    IView<T> remove( @Nonnull final ILiteral... p_literal );

    /**
     * removes a view in the current structure
     *
     * @param p_view view
     * @return self reference
     */
    @Nonnull
    IView<T> remove( @Nonnull final IView<T> p_view );



    /**
     * checks if a literal exists
     *
     * @param p_path path to a literal (suffix is literal name)
     * @return existance boolean
     */
    boolean containsLiteral( @Nonnull final IPath p_path );

    /**
     * view existing check
     *
     * @param p_path path to a view
     * @return existance boolean
     */
    boolean containsView( @Nonnull final IPath p_path );

}
