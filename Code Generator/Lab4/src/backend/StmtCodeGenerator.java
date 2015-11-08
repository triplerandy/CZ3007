package backend;

import java.util.HashMap;

import soot.Unit;
import soot.Value;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
import soot.util.Chain;
import ast.Block;
import ast.BreakStmt;
import ast.ExprStmt;
import ast.IfStmt;
import ast.ReturnStmt;
import ast.Stmt;
import ast.TypeDescriptor;
import ast.Visitor;
import ast.WhileStmt;

/**
 * This class is in charge of creating Jimple code for a given statement (and its nested
 * statements, if applicable).
 */
public class StmtCodeGenerator extends Visitor<Void> {
	/** Cache Jimple singleton for convenience. */
	private final Jimple j = Jimple.v();
	
	/** The {@link FunctionCodeGenerator} that created this object. */
	private final FunctionCodeGenerator fcg;
	
	/** The statement list of the enclosing function body. */
	private final Chain<Unit> units;
	
	/** A map from while statements to their break target. */
	private final HashMap<WhileStmt, Unit> breakTargets = new HashMap<WhileStmt, Unit>();
	
	public StmtCodeGenerator(FunctionCodeGenerator fcg) {
		this.fcg = fcg;
		this.units = fcg.getBody().getUnits();
	}
	
	/** Generates code for an expression statement. */
	@Override
	public Void visitExprStmt(ExprStmt nd) {
		/* TODO: generate code for expression statement (hint: use ExprCodeGenerator.generate) */
		
		// generate code for expression statement
		ExprCodeGenerator.generate(nd.getExpr(), this.fcg);
		
		return null; // dummy
	}
	
	/** Generates code for a break statement. */
	@Override
	public Void visitBreakStmt(BreakStmt nd) {
		/* TODO: generate code for break statement (hint: use ASTNode.getEnclosingLoop and breakTargets;
		 *       use units.add() to insert the statement into the surrounding method) */
		
		/* Structure of break statement */
		// goto label0
		return null;
	}

	/** Generates code for a block of statements. */
	@Override
	public Void visitBlock(Block nd) {
		for(Stmt stmt : nd.getStmts())
			stmt.accept(this);
		return null;
	}
	
	/** Generates code for a return statement. */
	@Override
	public Void visitReturnStmt(ReturnStmt nd) {
		Unit stmt;
		if(nd.hasExpr())
			stmt = j.newReturnStmt(ExprCodeGenerator.generate(nd.getExpr(), fcg));
		else
			stmt = j.newReturnVoidStmt();
		units.add(stmt);
		return null;
	}
	
	/** Generates code for an if statement. */
	@Override
	public Void visitIfStmt(IfStmt nd) {
		
	    /*
	     *  Structure of If Statement
	    	// compute value of cond
	      	c = …
	      	if c == 0 goto label0
	      		// code for stmt1
	    	label0:
	    */
	   
	    /* Structure of If-Else Statement
		      // compute value of cond
		      c = …
		      if c == 0 goto label0
		      // code for stmt1
		      goto label1
		      label0:
		      // code for stmt2 (Else Part)
		      label1:
	    */
		Value cond = ExprCodeGenerator.generate(nd.getExpr(), fcg);
		NopStmt join = j.newNopStmt();
		units.add(j.newIfStmt(j.newEqExpr(cond, IntConstant.v(0)), join));
		nd.getThen().accept(this);
		if(nd.hasElse()) {
			NopStmt els = join;
			join = j.newNopStmt();
			units.add(j.newGotoStmt(join));
			units.add(els);
			nd.getElse().accept(this);
		}
		units.add(join);
		return null;
	}
		
	/** Generates code for a while statement. */
	@Override
	public Void visitWhileStmt(WhileStmt nd) {
		/* TODO: generate code for while statement as discussed in lecture; add the NOP statement you
		 *       generate as the break target to the breakTargets map
		 */
		
		/* Structure of while statement */
		// compute the result 
		return null;
	}
	

	
}
