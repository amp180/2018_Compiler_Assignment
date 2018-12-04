.PHONEY: clean run

handwritten_files = TypeCheckerVisitor.java TACPrintVisitor.java Table.java FuncTableAndTable.java DeclarationCheckerVisitor.java
javacc_files = CCALChecker.java CCALCheckerConstants.java CCALCheckerTokenManager.java TokenMgrError.java ParseException.java Token.java JavaCharStream.java
jjtree_files = lex.jj Node.java SimpleNode.java ASTprogram.java ASTdecl_list.java ASTvar_decl.java ASTconst_decl.java ASTidentifier.java ASTfunction_list.java ASTfunction.java ASTretval.java ASTparameter_list.java ASTparameter.java ASTtype.java ASTmain_func.java ASTstatement_block.java ASTassignment_statement.java ASTfunction_call.java ASTbegin_end_statement.java ASTif_else_statement.java ASTwhile_statement.java ASTskip_statement.java ASTexpression.java ASTcondition.java ASTnot.java ASTbool_expression.java ASTcomp_expression.java ASTarith_expression.java ASTfragment.java ASTvar_lookup.java ASTnegate.java ASTbinary_arith_op.java ASTbin_bool_op.java ASTcomp_op.java ASTarg_list.java CCALCheckerTreeConstants.java CCALCheckerVisitor.java JJTCCALCheckerState.java


all_files =  ${javacc_files} ${jjtree_files} ${handwritten_files}

run : CCALChecker.class *.class
	java CCALChecker

CCALChecker.class  *.class: ${all_files}
	javac ./*.java

${javacc_files} : lex.jj ${jtree_files} 
	javacc ./lex.jj

${jjtree_files} : lex.jjt ${handwritten_files}
	jjtree ./lex.jjt

clean:
	rm -rf ${javacc_files} ${jjtree_files} *.class
