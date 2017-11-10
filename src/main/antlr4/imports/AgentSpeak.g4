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

/**
 * base grammar rules of an additional version of AgentSpeak(L) without any terminal symbols,
 * the rules are restricted to the AgentSpeak elements e.g. beliefs, plan, ...
 **/
grammar AgentSpeak;
import ComplexType;


// --- agent-behaviour structure ---------------------------------------------------------

/**
 * belief rule
 **/
belief :
    literal DOT
    ;

/**
 * agent plans rule
 * @note one plan must exists
 **/
plans :
    plan+
    ;

/**
 * optional (prolog) rules
 **/
logicrules :
    logicrule+
    ;

/**
 * plan modified against the original Jason grammar,
 * so a context is optional (on default true) and the
 * plan body is also optional. The definition is
 * trigger name [ plancontent ]* .
 */
plan :
    annotations?
    plan_trigger
    literal
    plandefinition*
    DOT
    ;

/**
 * plan body & context definition
 * The definition is [ : condition ] [ <- body ]
 **/
plandefinition :
    ( COLON expression )?
    LEFTARROW body
    ;

/**
 * rules are similar to plans
 * but without context and trigger event
 **/
logicrule :
    annotations?
    literal
    logicalruledefinition+
    DOT
    ;

/**
 * rule definition similar to plan
 **/
logicalruledefinition :
    RULEOPERATOR
    body
    ;

/**
 * annotation for rules and plans
 **/
annotations :
    ( annotation_atom | annotation_literal )+
    ;

/**
 * atomic annotations (without parameter)
 **/
annotation_atom :
    AT (ATOMIC | PARALLEL)
    ;

/**
 * annotation (with parameter)
 **/
annotation_literal :
    AT annotation_value_literal
    ;

/**
 * annotations (with any parameter)
 **/
annotation_value_literal :
    CONSTANT
    LEFTROUNDBRACKET
    variableatom
    COMMA
    ( number | STRING )
    RIGHTROUNDBRACKET
    ;

/**
 * plan trigger which can match a goal or belief
 **/
plan_trigger :
    (plan_belief_trigger | plan_goal_trigger)
    ;

/**
 * plan trigger for a goal
 **/
plan_goal_trigger :
    (PLUS | MINUS)
    EXCLAMATIONMARK
    ;

/**
 * plan trigger for a belief
 **/
plan_belief_trigger :
    PLUS | MINUS
    ;

/**
 * plan or block body
 **/
body :
    body_formula
    ( SEMICOLON body_formula )*
    ;

// ---------------------------------------------------------------------------------------



// --- agent-execution-context -----------------------------------------------------------

/**
 * basic executable formula
 **/
body_formula :
    repair_formula
    | belief_action

    | deconstruct_expression
    | assignment_expression
    | unary_expression
    | binary_expression

    | lambda
    ;

/**
 * repairable formula
 **/
repair_formula :
    ( executable_term | test_action | achievement_goal_action )
    ( LEFTSHIFT repair_formula )?
    ;

/**
 * belief-action operator
 **/
belief_action :
    ( PLUS | MINUS ) literal
    ;

/**
 * test-goal / -rule action
 **/
test_action :
    QUESTIONMARK DOLLAR? atom
    ;

/**
 * achivement-goal action
 **/
achievement_goal_action :
    ( EXCLAMATIONMARK | DOUBLEEXCLAMATIONMARK ) ( literal | variable_evaluate )
    ;

/**
 * deconstruct expression (splitting clauses)
 **/
deconstruct_expression :
    variablelist
    DECONSTRUCT
    ( literal | variable )
    ;

/**
 * assignment expression (for assignin a variable)
 **/
assignment_expression :
    assignment_expression_singlevariable
    | assignment_expression_multivariable
    ;

/**
 * assignment of a single variable
 **/
assignment_expression_singlevariable :
    variable
    ASSIGN
    executable_term
    ;

/**
 * assignment of a variable list
 **/
assignment_expression_multivariable :
    variablelist
    ASSIGN
    executable_term
    ;

/**
 * unary expression
 **/
unary_expression :
    variable
    unaryoperator
    ;

/**
 * binary expression
 **/
binary_expression :
    variable
    binaryoperator
    ( variable | number )
    ;

/**
 * block-formula of subsection
 **/
block_formula :
    LEFTCURVEDBRACKET body RIGHTCURVEDBRACKET
    | body_formula
    ;

/**
 * lambda expression for iteration
 **/
lambda :
    AT? lambda_initialization
    RIGHTARROW variable
    lambda_return?
    COLON block_formula
    ;

/**
 * initialization of lambda expression
 **/
lambda_initialization :
    LEFTROUNDBRACKET
    ( variable | literal )
    RIGHTROUNDBRACKET
    ;

/**
 * return argument lambda expression
 **/
lambda_return :
    VLINE variable
    ;

// ---------------------------------------------------------------------------------------
