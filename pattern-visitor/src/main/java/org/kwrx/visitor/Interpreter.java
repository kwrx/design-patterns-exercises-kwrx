/*
 * MIT License
 *
 * Copyright (c) 2020 Antonino Natale
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package org.kwrx.visitor;

import org.kwrx.visitor.interp.Expression;
import org.kwrx.visitor.interp.Statement;
import org.kwrx.visitor.interp.SymbolCallable;
import org.kwrx.visitor.interp.SymbolClass;
import org.kwrx.visitor.interp.expressions.*;
import org.kwrx.visitor.interp.statements.*;
import org.kwrx.visitor.interp.types.Number;
import org.kwrx.visitor.interp.types.*;
import org.kwrx.visitor.parser.Token;
import org.kwrx.visitor.parser.TokenType;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;


public class Interpreter implements Statement.Visitor, Expression.Visitor {

    private final List<Statement> statements;
    private Context runningContext;

    public Interpreter(List<Statement> statements, Context context) {
        this.statements = statements;
        this.runningContext = context;
    }



    @Override
    public void visitBlockStatement(BlockStatement statement) {
        executeBlock(statement, new Context(runningContext));
    }

    @Override
    public void visitExpressionStatement(ExpressionStatement statement) {
        eval(statement.getExpression());
    }

    @Override
    public void visitVariableStatement(VariableStatement statement) {
        runningContext.define(statement.getName(), eval(statement.getConstructor()));
    }

    @Override
    public void visitIfStatement(IfStatement statement) {

        if(Dynamic.isTrue(eval(statement.getCondition())))
            statement.getThenBlock().accept(this);
        else
            statement.getElseBlock().accept(this);

    }

    @Override
    public void visitWhileStatement(WhileStatement statement) {

        while(Dynamic.isTrue(eval(statement.getCondition())))
            statement.getBody().accept(this);

    }

    @Override
    public void visitForStatement(ForStatement statement) {

        new BlockStatement(Arrays.asList(

                statement.getInitializer(),

                new WhileStatement(statement.getCondition(),
                        new BlockStatement(Arrays.asList(
                                statement.getBody(),
                                statement.getIncrement()
                        ))
                )

        )).accept(this);

    }

    @Override
    public void visitFunctionStatement(FunctionStatement statement) {
        runningContext.define(statement.getName(), new Symbol(createSymbolCallable(statement)));
    }

    @Override
    public void visitReturnStatement(ReturnStatement statement) {
        throw new ReturnTrampoline(eval(statement.getResult()));
    }

    @Override
    public void visitClassStatement(ClassStatement statement) {

        SymbolClass superclass = null;

        if(statement.getSuperclass() != null) {

            var resolved = runningContext.resolve(statement.getSuperclass()).getValue();

            if(!(resolved instanceof SymbolClass))
                throw new RunningException(statement.getSuperclass(), "expected superclass be a class");

            superclass = (SymbolClass) resolved;

        }


        runningContext.define(statement.getName(), new Symbol(new SymbolClass(statement.getName(), superclass,

                new LinkedHashMap<>() {{
                        for (var fun : statement.getMethods())
                            put(fun.getName(), new Symbol(createSymbolCallable(fun)));
                }},

                new LinkedHashMap<>() {{
                        for (var loc : statement.getVariables())
                            put(loc.getName(), eval(loc.getConstructor()));
                }}

        )));

    }



    @Override
    public Dynamic visitBinaryExpression(BinaryExpression e) {

        Dynamic left = eval(e.getLeft());
        Dynamic right = eval(e.getRight());

        return switch (e.getOperator().getType()) {

            case PLUS  -> Number.add(left, right);
            case MINUS -> Number.sub(left, right);
            case STAR  -> Number.mul(left, right);
            case SLASH -> Number.div(left, right);

            case GREATER       -> Number.gr(left, right);
            case LESS          -> Number.ls(left, right);
            case GREATER_EQUAL -> Number.gre(left, right);
            case LESS_EQUAL    -> Number.lse(left, right);

            case EQUAL_EQUAL   -> Logical.from( Dynamic.isEquals(left, right));
            case BANG_EQUAL    -> Logical.from(!Dynamic.isEquals(left, right));

            default -> throw new IllegalStateException();

        };

    }

    @Override
    public Dynamic visitGroupingExpression(GroupingExpression e) {
        return eval(e.getExpression());
    }

    @Override
    public Dynamic visitLiteralExpression(LiteralExpression e) {

        if(e.getLiteral() == null)
            return Nil.value();


        return switch (e.getLiteral().getClass().getSimpleName()) {

            case "Double"  -> new Number(e.getLiteral());
            case "String"  -> new Text(e.getLiteral());
            case "Boolean" -> Logical.from((boolean) e.getLiteral());

            default -> new Dynamic(e.getLiteral());

        };

    }

    @Override
    public Dynamic visitUnaryExpression(UnaryExpression e) {

        if(e.getRight() instanceof VariableExpression && e.getOperator().getType() == TokenType.AND)
            return new Reference(((VariableExpression) e.getRight()).getName());


        return switch (e.getOperator().getType()) {

            case BANG  -> Logical.from(!Dynamic.isTrue(eval(e.getRight())));
            case MINUS -> Number.minus(eval(e.getRight()));

            default -> throw new IllegalStateException();

        };

    }

    @Override
    public Dynamic visitVariableExpression(VariableExpression e) {
        return runningContext.resolve(e.getName());
    }

    @Override
    public Dynamic visitAssignExpression(AssignExpression e) {
        return runningContext.assign(e.getName(), eval(e.getValue()));
    }

    @Override
    public Dynamic visitInvokeExpression(InvokeExpression e) {

        var fun = (Symbol) eval(e.getReference());
        var sym = (SymbolCallable) fun.getSymbol();


        if (sym.arity() != -1) {

            if (e.getParams().size() != sym.arity())
                throw new RunningException(sym.name(), String.format("too many or too few arguments, expected %d, given %d", sym.arity(), e.getParams().size()));

        }

        return sym.call(
                this,
                fun.getInstance(),
                e.getParams()
                        .stream()
                        .map(this::eval)
                        .collect(Collectors.toList())
        );

    }


    @Override
    public Dynamic visitGetFieldExpression(GetFieldExpression e) {

        var instance = eval(e.getInstance());

        if(instance instanceof Instance) {

            var field = ((Instance) instance).getField(e.getField());

            if(field instanceof Symbol)
                ((Symbol) field).bind((Instance) instance);

            return field;

        }

        throw new RunningException(e.getField(), "expected an instance");

    }

    @Override
    public Dynamic visitSetFieldExpression(SetFieldExpression e) {

        var instance = eval(e.getInstance());

        if(instance instanceof Instance)
            return ((Instance) instance).setField(e.getField(), eval(e.getValue()));

        throw new RunningException(e.getField(), String.format("expected an instance, given %s", instance.getType()));

    }


    @Override
    public Dynamic visitThisExpression(ThisExpression e) {
        return runningContext.resolve(e.getThisToken());
    }

    @Override
    public Dynamic visitSuperExpression(SuperExpression e) {
        return runningContext.resolve(e.getSuperToken());
    }

    @Override
    public Dynamic visitNoopExpression(NoopExpression e) {
        return Nil.value();
    }





    private Dynamic eval(Expression e) {
        return e.accept(this);
    }

    private Dynamic executeBlock(BlockStatement statement, Context context) {

        Context parent = runningContext;

        try {

            runningContext = context;

            for (var s : statement.getStatements())
                s.accept(this);


        } catch(ReturnTrampoline returnValue) {
            return returnValue.getValue();

        } finally {
            runningContext = parent;
        }


        return Nil.value();

    }


    private Dynamic executeFunction(FunctionStatement statement, Interpreter interpreter, Instance instance, List<Dynamic> params) {

        Context context = new Context(runningContext);

        if(instance != null) {

            context.define(new Token(TokenType.THIS, "this", null, 0, 0), instance);

            if(instance.getParent() != null)
                context.define(new Token(TokenType.SUPER, "super", null, 0, 0), instance.getParent());

        }


        for (int i = 0; i < params.size(); i++)
            context.define(statement.getParams().get(i), params.get(i));

        return interpreter.executeBlock(statement.getBody(), context);

    }

    private SymbolCallable createSymbolCallable(FunctionStatement statement) {
        return new SymbolCallable() {

            @Override
            public Dynamic call(Interpreter interpreter, Instance instance, List<Dynamic> params) {
                return interpreter.executeFunction(statement, interpreter, instance, params);
            }

            @Override
            public Token name() {
                return statement.getName();
            }

            @Override
            public int arity() {
                return statement.getParams().size();
            }

        };
    }



    public Context execute() {

        for(var statement : statements)
            statement.accept(this);

        return runningContext;

    }

}
