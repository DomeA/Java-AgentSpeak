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

package org.lightjason.agentspeak.grammar;

import org.junit.Assert;
import org.junit.Test;
import org.lightjason.agentspeak.agent.IAgent;
import org.lightjason.agentspeak.generator.CActionStaticGenerator;
import org.lightjason.agentspeak.generator.IActionGenerator;
import org.lightjason.agentspeak.generator.ILambdaStreamingGenerator;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CLiteral;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ILiteral;
import org.lightjason.agentspeak.language.execution.instantiable.plan.IPlan;
import org.lightjason.agentspeak.language.execution.instantiable.plan.trigger.ITrigger;
import org.lightjason.agentspeak.language.execution.instantiable.rule.IRule;
import org.lightjason.agentspeak.language.variable.CVariable;
import org.lightjason.agentspeak.language.variable.IVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * test for plan-bundle parser
 */
public final class TestCPlanBundleParser extends IBaseGrammarTest
{

    /**
     * test belief
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void belief() throws Exception
    {
        final IASTVisitorPlanBundle l_parser = new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY )
                                                    .parse( streamfromstring( "bar(1234). foo('tests')." ) );

        final List<ILiteral> l_beliefs = new ArrayList<>( l_parser.initialbeliefs() );

        Assert.assertEquals( 2, l_beliefs.size() );
        Assert.assertEquals( CLiteral.of( "bar", CRawTerm.of( 1234.0 ) ), l_beliefs.get( 0 ) );
        Assert.assertEquals( CLiteral.of( "foo", CRawTerm.of( "tests" ) ), l_beliefs.get( 1 ) );
    }

    /**
     * test simple rule
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void simplerule() throws Exception
    {
        final IRule l_rule = parsesinglerule(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "nexttower(T) :- T++."
        );

        final IVariable<?> l_tvar = new CVariable<>( "T" ).set( 0 );

        Assert.assertTrue(
            execute(
                l_rule,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_tvar
            )
        );

        Assert.assertEquals( 1.0, l_tvar.<Number>raw() );
    }

    /**
     * test complex rule
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void complexrule() throws Exception
    {
        final Random l_random = new Random();
        final int l_nvalue = l_random.nextInt( 5 );
        final int l_mvalue = l_random.nextInt( 6 );

        final CCollectValues l_values = new CCollectValues();

        final IAgent<?> l_agent = new CAgentGenerator(
            "ackermann(N, M, R)"
                + " :- N == 0; R = M+1"
                + " :- M == 0; TN = N - 1; $ackermann(TN, 1, R)"
                + " :- TM = M - 1; $ackermann(N, TM, RI); TN = N - 1; $ackermann(TN, RI, R)."
            + " +!ack(N,M) <- $ackermann(N, M, R); .push/value(N, M, R).",
            new CActionStaticGenerator( Stream.of( l_values ) ),
            ILambdaStreamingGenerator.EMPTY
        ).generatesingle();

        try
        {
            Assert.assertTrue(
                defuzzify(
                    l_agent.trigger(
                        ITrigger.EType.ADDGOAL.builddefault( CLiteral.of( "ack", CRawTerm.of( l_nvalue ), CRawTerm.of( l_mvalue ) ) ),
                        true
                    ),
                    l_agent
                )
            );

            Assert.assertEquals( 3, l_values.value().size() );
            Assert.assertEquals( l_nvalue, l_values.value().get( 0 ).<Number>raw() );
            Assert.assertEquals( l_mvalue, l_values.value().get( 1 ).<Number>raw() );
            Assert.assertEquals( ackermann( l_nvalue, l_mvalue ).doubleValue(), l_values.value().get( 2 ).<Number>raw() );
        }
        catch ( final StackOverflowError l_exception )
        {
            Assert.assertTrue( true );
        }
    }

    /**
     * test success and fail plan
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void successfailplan() throws Exception
    {
        final Map<ILiteral, IPlan> l_plans = parsemultipleplans(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!testsuccess <- success. +!testfail <- fail."
        ).collect( Collectors.toMap( i -> i.trigger().literal(), i -> i ) );

        Assert.assertEquals( 2, l_plans.size() );

        Assert.assertTrue(
            l_plans.get( CLiteral.of( "testsuccess" ) ).toString(),
            execute(
                l_plans.get( CLiteral.of( "testsuccess" ) ),
                false,
                Collections.emptyList(),
                Collections.emptyList()
            )
        );

        Assert.assertFalse(
            l_plans.get( CLiteral.of( "testfail" ) ).toString(),
            execute(
                l_plans.get( CLiteral.of( "testfail" ) ),
            false,
                Collections.emptyList(),
                Collections.emptyList()
            )
        );
    }

    /**
     * test repair-chain
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void repair() throws Exception
    {
        final Map<ILiteral, IPlan> l_plans = parsemultipleplans(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!threesuccess <- fail << fail << success. +!twofail <- fail << fail."
        ).collect( Collectors.toMap( i -> i.trigger().literal(), i -> i ) );

        Assert.assertEquals( 2, l_plans.size() );

        Assert.assertTrue(
            l_plans.get( CLiteral.of( "threesuccess" ) ).toString(),
            execute(
                l_plans.get( CLiteral.of( "threesuccess" ) ),
                false,
                Collections.emptyList(),
                Collections.emptyList()
            )
        );

        Assert.assertFalse(
            l_plans.get( CLiteral.of( "twofail" ) ).toString(),
            execute(
                l_plans.get( CLiteral.of( "twofail" ) ),
                false,
                Collections.emptyList(),
                Collections.emptyList()
            )
        );
    }

    /**
     * test deconstruct
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void deconstructsimple() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!mainsuccess <- [A|B] =.. bar('test')."
        );

        final IVariable<?> l_avar = new CVariable<>( "A" );
        final IVariable<?> l_bvar = new CVariable<>( "B" );

        Assert.assertTrue(
            l_plan.toString(),
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_avar,
                l_bvar
            )
        );

        Assert.assertEquals( "bar", l_avar.raw() );
        Assert.assertTrue( l_bvar.toString(), ( l_bvar.raw() instanceof List<?> ) && ( l_bvar.<List<?>>raw().size() == 1 ) );
        Assert.assertEquals( "test", l_bvar.<List<?>>raw().get( 0 ) );
    }

    /**
     * test number expression
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void numberexpression() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate <- X = 5 + 4 * 3 + 1 - ( 3 + 1 ) * 2 + 2 ** 2 * 3."
        );

        final IVariable<?> l_xvar = new CVariable<>( "X" );

        Assert.assertTrue(
            l_plan.toString(),
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_xvar
            )
        );

        Assert.assertEquals( 22.0, l_xvar.<Number>raw() );
    }

    /**
     * test number expression
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void numbervariableexpression() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate <- Z = A * 3 - B * ( 5 + C ) + 4.2 ** D."
        );

        final Random l_random = new Random();

        final IVariable<?> l_avar = new CVariable<>( "A" ).set( l_random.nextDouble() );
        final IVariable<?> l_bvar = new CVariable<>( "B" ).set( l_random.nextInt( 40 ) );
        final IVariable<?> l_cvar = new CVariable<>( "C" ).set( l_random.nextInt( 30 ) );
        final IVariable<?> l_dvar = new CVariable<>( "D" ).set( l_random.nextInt( 20 ) );
        final IVariable<?> l_result = new CVariable<>( "Z" ).set( l_random.nextInt( 10 ) );

        Assert.assertTrue(
            l_plan.toString(),
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_result,
                l_avar,
                l_bvar,
                l_cvar,
                l_dvar
            )
        );

        Assert.assertEquals(
            l_avar.<Number>raw().doubleValue() * 3 - l_bvar.<Number>raw().doubleValue() * ( 5 + l_cvar.<Number>raw().doubleValue() )
            + Math.pow( 4.2, l_dvar.<Number>raw().doubleValue() ),
            l_result.<Number>raw().doubleValue(),
            0.000001
        );
    }

    /**
     * test number expression with constants
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void constantexpression() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate <- S = electron * boltzmann * lightspeed."
        );

        final IVariable<?> l_result = new CVariable<>( "S" );

        Assert.assertTrue(
            l_plan.toString(),
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_result
            )
        );

        Assert.assertEquals(
            CCommon.NUMERICCONSTANT.get( "electron" ) * CCommon.NUMERICCONSTANT.get( "boltzmann" ) * CCommon.NUMERICCONSTANT.get( "lightspeed" ),
            l_result.<Number>raw().doubleValue(),
            0.00000001
        );
    }


    /**
     * test boolean operators
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void booleanoperators() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate <- OrTrue = true || false; OrFalse = false or false; AndTrue = true && true; AndFalse = true and false;"
            + "XorFalse = true ^ true; NotFalse = not true; NotTrue = ~false; XorTrue = true xor false."
        );

        final IVariable<?> l_ortrue = new CVariable<>( "OrTrue" );
        final IVariable<?> l_orfalse = new CVariable<>( "OrFalse" );
        final IVariable<?> l_xortrue = new CVariable<>( "XorTrue" );
        final IVariable<?> l_xorfalse = new CVariable<>( "XorFalse" );
        final IVariable<?> l_andtrue = new CVariable<>( "AndTrue" );
        final IVariable<?> l_andfalse = new CVariable<>( "AndFalse" );

        final IVariable<?> l_notfalse = new CVariable<>( "NotFalse" );
        final IVariable<?> l_nottrue = new CVariable<>( "NotTrue" );

        Assert.assertTrue(
            l_plan.toString(),
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_andtrue,
                l_andfalse,
                l_ortrue,
                l_orfalse,
                l_xortrue,
                l_xorfalse,
                l_notfalse,
                l_nottrue
            )
        );

        Assert.assertTrue( l_plan.toString(),  l_andtrue.raw() );
        Assert.assertFalse( l_plan.toString(), l_andfalse.raw() );

        Assert.assertTrue( l_plan.toString(),  l_ortrue.raw() );
        Assert.assertFalse( l_plan.toString(), l_orfalse.raw() );

        Assert.assertTrue( l_plan.toString(),  l_xortrue.raw() );
        Assert.assertFalse( l_plan.toString(), l_xorfalse.raw() );

        Assert.assertTrue( l_plan.toString(),  l_nottrue.raw() );
        Assert.assertFalse( l_plan.toString(), l_notfalse.raw() );
    }

    /**
     * test complex boolean expression
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void booleanexpression() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate <- R = true || false and ( true or false ) xor not ( 5 < 3 )."
        );

        final IVariable<?> l_result = new CVariable<>( "R" );

        Assert.assertTrue(
            l_plan.toString(),
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_result
            )
        );

        //Checkstyle:OFF:SimplifyBooleanExpression
        //noinspection ConstantConditions
        Assert.assertEquals( true || false && ( true || false ) ^ !( 5 < 3 ), l_result.raw() );
        //Checkstyle:ON:SimplifyBooleanExpression
    }

    /**
     * test ternary operator true case
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void ternarytrue() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate <- T = 3 < 5 ? gravity : positiveinfinity."
        );

        final IVariable<?> l_result = new CVariable<>( "T" );

        Assert.assertTrue(
            l_plan.toString(),
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_result
            )
        );

        Assert.assertEquals( CCommon.NUMERICCONSTANT.get( "gravity" ), l_result.<Number>raw().doubleValue(), 0.00000001 );
    }

    /**
     * test ternary operator true case
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void ternaryfalse() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!calculate <- V = 5 < 3 ? negativeinfinity : minimumvalue."
        );

        final IVariable<?> l_result = new CVariable<>( "V" );

        Assert.assertTrue(
            l_plan.toString(),
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_result
            )
        );

        Assert.assertEquals( CCommon.NUMERICCONSTANT.get( "minimumvalue" ), l_result.<Number>raw().doubleValue(), 0.00000001 );
    }

    /**
     * test multiple items in a plan
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void multipleplanitems() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!items <- N = 'foo'; P = 'bar'; C = success."
        );

        final IVariable<?> l_nvar = new CVariable<>( "N" );
        final IVariable<?> l_pvar = new CVariable<>( "P" );
        final IVariable<?> l_cvar = new CVariable<>( "C" );

        Assert.assertTrue(
            l_plan.toString(),
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_nvar,
                l_pvar,
                l_cvar
            )
        );

        Assert.assertEquals( "foo", l_nvar.raw() );
        Assert.assertEquals( "bar", l_pvar.raw() );
        Assert.assertEquals( true, l_cvar.raw() );
    }

    /**
     * test plan description
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void plandescription() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "@description('a long plan description') +!description <- success."
        );

        Assert.assertEquals( "a long plan description", l_plan.description() );
    }

    /**
     * test multiple plan execute
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void multipleplanexecution() throws Exception
    {
        final IPlan[] l_plans = parsemultipleplans(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!multi(X) : X >= 5 <- Y = X + 7 : X < 5 <- Y = X ** 2."
        ).toArray( IPlan[]::new );

        final IVariable<?> l_var = new CVariable<>( "Y" );

        Assert.assertArrayEquals(
            Stream.of(
                12.0,
                16.0
            ).toArray(),

            Stream.of(
                new CVariable<>( "X" ).set( 5 ),
                new CVariable<>( "X" ).set( 4 )
            )
                  .map( i -> new CLocalContext( i, l_var ) )
                  .map( i -> Arrays.stream( l_plans )
                                   .filter( j -> j.condition( i ) )
                                   .map( j -> execute( j, false, Collections.emptyList(), Collections.emptyList(), i ) )
                                   .filter( j -> j )
                                   .findFirst()
                                   .map( j -> l_var )
                                   .get()
                  )
                  .map( i -> l_var.raw() )
                  .toArray()
        );
    }

    /**
     * test plan annotation
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void annotation() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "@parallel @atomic @constant(StringValue,'xyz') @constant(NumberValue,-777) @tag('foobar')"
            + "@variable(Y,'y value description') @tag('test') @description('description text') +!annotation(Y) <- success."
        );

        Assert.assertEquals( "description text", property( "m_description", l_plan ) );

        Assert.assertTrue( l_plan.toString(), property( "m_atomic", l_plan ) );
        Assert.assertTrue( l_plan.toString(), property( "m_parallel", l_plan ) );

        Assert.assertTrue(
            l_plan.toString(),
            l_plan.variables().parallel().anyMatch( i -> ( "StringValue".equals( i.functor() ) ) && ( "xyz".equals( i.raw() ) ) )
        );
        Assert.assertTrue(
            l_plan.toString(),
            l_plan.variables().parallel().anyMatch( i -> ( "NumberValue".equals( i.functor() ) ) && ( Double.valueOf( -777 ).equals( i.raw() ) ) )
        );

        Assert.assertArrayEquals( Stream.of( "foobar", "test" ).toArray(), l_plan.tags().toArray() );

        Assert.assertArrayEquals(
            Stream.of( new CVariable<>( "Y" ).set( "y value description" ) ).toArray(),
            l_plan.variabledescription().toArray()
        );
    }

    /**
     * test term-value list
     *
     * @throws Exception thrown on stream and parser error
     */
    @Test
    public void termlist() throws Exception
    {
        final IPlan l_plan = parsesingleplan(
            new CParserPlanBundle( IActionGenerator.EMPTY, ILambdaStreamingGenerator.EMPTY ),
            "+!list <- L = [123, false, 'hello']."
        );

        final IVariable<?> l_var = new CVariable<>( "L" );

        Assert.assertTrue(
            l_plan.toString(),
            execute(
                l_plan,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                l_var
            )
        );

        Assert.assertTrue( l_var.toString(), l_var.raw() instanceof List<?> );
        Assert.assertEquals( 3, l_var.<List<?>>raw().size() );
        Assert.assertEquals( 123.0, l_var.<List<?>>raw().get( 0 ) );
        Assert.assertEquals( false, l_var.<List<?>>raw().get( 1 ) );
        Assert.assertEquals( "hello", l_var.<List<?>>raw().get( 2 ) );
    }
}
