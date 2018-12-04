import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.*;
import java.io.*;

public class TACPrintVisitor implements CCALCheckerVisitor {

    int temp_var_num = 0;
    int label_number = 0;
    PrintWriter out;

    public TACPrintVisitor(){
        try {
            this.out = new PrintWriter(new BufferedWriter(new FileWriter("./out.ir")), true);
        } catch (Exception e) {;};
    }

    public Object visit(SimpleNode var1, Object var2) {
        return null;
    };

    public Object visit(ASTprogram var1, Object var2){
        return var1.childrenAccept(this, var2);
    };

    public Object visit(ASTdecl_list var1, Object var2){
        var1.childrenAccept(this, var2);
        return null;
    };

    public Object visit(ASTvar_decl var1, Object var2){
        return null;
    };

    public Object visit(ASTconst_decl var1, Object var2){
        String name = (String) ((SimpleNode)var1.jjtGetChild(0)).jjtGetValue();

        int tempvar = (Integer) var1.jjtGetChild(2).jjtAccept(this, var2);

        this.out.println (name+" = _var"+tempvar);
        return null;
    };

    public Object visit(ASTidentifier var1, Object var2){
        return null;
    };

    public Object visit(ASTfunction_list var1, Object var2){
        var1.childrenAccept(this, var2);
        return null;
    };

    public Object visit(ASTfunction var1, Object var2){
        this.out.println(((String)((SimpleNode)var1.jjtGetChild(1)).jjtGetValue())+":");

        var1.childrenAccept(this, var2);

        return null;
    };

    public Object visit(ASTretval var1, Object var2){
        int val = (Integer) var1.jjtGetChild(0).jjtAccept(this, var2);
        this.out.println("return _var" + val);
        return null;
    };;

    public Object visit(ASTparameter_list var1, Object var2){
        return null;
    };

    public Object visit(ASTparameter var1, Object var2){
        return null;
    };

    public Object visit(ASTtype var1, Object var2){
        return null;
    };

    public Object visit(ASTmain_func var1, Object var2){
        this.out.println("main:");
        var1.childrenAccept(this, var2);
        return null;
    };

    public Object visit(ASTstatement_block var1, Object var2){
        var1.childrenAccept(this, var2);
        return null;
    };

    public Object visit(ASTassignment_statement var1, Object var2){
        String varName = (String) ((SimpleNode) var1.jjtGetChild(0)).jjtGetValue();
        int temp = (Integer) ((SimpleNode) var1.jjtGetChild(1)).jjtAccept(this, var2);

        this.out.println(varName+" = _var"+temp);

        return temp;
    };

    public Object visit(ASTfunction_call var1, Object var2){
        String name = (String) ((SimpleNode) var1.jjtGetChild(0)).jjtGetValue();
        var1.jjtGetChild(1).jjtAccept(this, var2);
        this.out.println("_var"+this.temp_var_num+" = call "+name+" ");
        return this.temp_var_num++;
    };

    public Object visit(ASTbegin_end_statement var1, Object var2){
        var1.childrenAccept(this, var2);
        return null;
    };

    public Object visit(ASTif_else_statement var1, Object var2){
        int lable_postfix = label_number++;
        int condition_tempvar = (Integer) var1.jjtGetChild(0).jjtAccept(this, var2);

        this.out.println("conditionalJump _var"+condition_tempvar+" "+"_if_"+lable_postfix);
        this.out.println("jmp _else_"+lable_postfix);
        this.out.println("_if_"+lable_postfix+":");
        var1.jjtGetChild(1).jjtGetChild(0).jjtAccept(this, var2);
        this.out.println("jmp _fi_"+lable_postfix);
        this.out.println("_else_"+lable_postfix+":");
        var1.jjtGetChild(2).jjtAccept(this, var2);
        this.out.println("_fi_"+lable_postfix+":");

        return null;
    };

    public Object visit(ASTwhile_statement var1, Object var2){
        int lable_postfix = label_number++;
        int condition_tempvar = (Integer) var1.jjtGetChild(0).jjtAccept(this, var2);
        this.out.println("conditionalJmp _var"+condition_tempvar+" "+"_while_body_"+lable_postfix);
        this.out.println("jmp _endwhile_"+lable_postfix);
        this.out.println("_while_body_"+lable_postfix+":");
        var1.jjtGetChild(1).jjtAccept(this, var2);
        this.out.println("conditionalJmp _var"+condition_tempvar+" "+"_while_body_"+lable_postfix);
        this.out.println("_endwhile_"+lable_postfix+":");
        return null;
    };;

    public Object visit(ASTskip_statement var1, Object var2){
        this.out.println("nop");
        return null;
    };

    public Object visit(ASTexpression var1, Object var2){
        return var1.jjtGetChild(0).jjtAccept(this, var2);
    };

    public Object visit(ASTcondition var1, Object var2){
        return var1.jjtGetChild(0).jjtAccept(this, var2);
    };

    public Object visit(ASTnot var1, Object var2){
        Integer contents = (Integer) var1.jjtGetChild(0).jjtAccept(this, var2);
        int var_num = temp_var_num++;

        this.out.println("_var"+var_num+" = !_var"+contents);
        return var_num;
    };

    public Object visit(ASTbool_expression var1, Object var2){
        Integer item1 = (Integer) var1.jjtGetChild(0).jjtAccept(this, var2);
        String op = ((Token)((SimpleNode) var1.jjtGetChild(1)).jjtGetValue()).image;
        Integer item2 = (Integer) var1.jjtGetChild(2).jjtAccept(this, var2);
        int var_num = temp_var_num++;

        this.out.println("_var"+var_num+" = _var"+item1+" "+op+" _var"+item2);
        return var_num;
    };

    public Object visit(ASTcomp_expression var1, Object var2){
        Integer item1 = (Integer) var1.jjtGetChild(0).jjtAccept(this, var2);
        String op = ((Token)((SimpleNode) var1.jjtGetChild(1)).jjtGetValue()).image;
        Integer item2 = (Integer) var1.jjtGetChild(2).jjtAccept(this, var2);
        int var_num = temp_var_num++;

        this.out.println("_var"+var_num+" = _var"+item1+" "+op+" _var"+item2);
        return var_num;
    };

    public Object visit(ASTarith_expression var1, Object var2){
        Integer item1 = (Integer) var1.jjtGetChild(0).jjtAccept(this, var2);
        String op = ((Token)((SimpleNode) var1.jjtGetChild(1)).jjtGetValue()).image;
        Integer item2 = (Integer) var1.jjtGetChild(2).jjtAccept(this, var2);
        int var_num = temp_var_num++;

        this.out.println("_var"+var_num+" = _var"+item1+" "+op+" _var"+item2);
        return var_num;
    };

    public Object visit(ASTfragment var1, Object var2){
        if (var1.jjtGetValue() instanceof Boolean || var1.jjtGetValue() instanceof Integer) {
            this.out.println("_var" + temp_var_num + " = " + var1.jjtGetValue().toString());
            return temp_var_num++;
        }
        return var1.jjtGetChild(0).jjtAccept(this, var2);
    };

    public Object visit(ASTvar_lookup var1, Object var2){
        String name = (String)(((SimpleNode)var1.jjtGetChild(0)).jjtGetValue());
        int var_num = temp_var_num++;

        this.out.println("_var"+var_num+" = "+name);
        return var_num;
    };

    public Object visit(ASTnegate var1, Object var2){
        Integer contents = (Integer) var1.jjtGetChild(0).jjtAccept(this, var2);
        int var_num = temp_var_num++;

        this.out.println("_var"+var_num+" = - _var"+contents);
        return var_num;
    };

    public Object visit(ASTbinary_arith_op var1, Object var2){
        return null;
    };

    public Object visit(ASTbin_bool_op var1, Object var2){
        return null;
    };

    public Object visit(ASTcomp_op var1, Object var2){
        return null;
    };

    public Object visit(ASTarg_list var1, Object var2){
        for(int i = var1.jjtGetNumChildren(); i>0 ; i--){
            String param = (String) ((SimpleNode) var1.jjtGetChild(i-1)).jjtGetValue();
            this.out.println("push_arg "+ param);
        }

        return null;
    };
}