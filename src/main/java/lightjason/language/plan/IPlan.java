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

import lightjason.language.plan.annotation.IAnnotation;
import lightjason.language.plan.trigger.ITrigger;

import java.util.Collection;
import java.util.List;


/**
 * interface of plan
 */
public interface IPlan extends IBodyAction
{

    /**
     * returns the trigger event
     *
     * @return trigger event
     */
    public ITrigger<?> getTrigger();

    /**
     * returns plan state
     *
     * @return state
     */
    public EState getState();

    /**
     * return unmodifieable annotation set
     *
     * @return set with annotation
     */
    public Collection<IAnnotation<?>> getAnnotations();

    /**
     * returns unmodifieable list with plan actions
     *
     * @return action list;
     */
    public List<IBodyAction> getBodyActions();


    /**
     * execution state of a plan
     */
    public enum EState
    {
        SUCCESS,
        FAIL,
        SUSPEND;
    }

}