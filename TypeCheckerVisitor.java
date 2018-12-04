import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TypeCheckerVisitor implements CCALCheckerVisitor {

    class Data {
        SimpleNode scope;
        FuncTableAndTable t;

        Data(SimpleNode scope, FuncTableAndTable t){
            this.scope = scope;
            this.t = t;
        }
    };

    private Boolean all(ArrayList<Object> bs){
        for(Object b : bs) {
            if (b instanceof Boolean && !((Boolean)b)) return false;
        }
        return true;
    }

    private ArrayList<Object> visitAllChildren(SimpleNode var1, Object var2) {
        ArrayList<Object> arr = new ArrayList<>();
        for (int i = 0; i<var1.jjtGetNumChildren(); i++){
            Object o = var1.jjtGetChild(i).jjtAccept(this, var2);
            arr.add(o);
        }
        return arr;
    }

    public Object visit(SimpleNode var1, Object var2) {
        return (Boolean) true;
    };

    public Object visit(ASTprogram var1, Object var2){
        return all( visitAllChildren(var1, ((Object) new Data(var1, (FuncTableAndTable)(var1.jjtGetValue())))));
    };

    public Object visit(ASTdecl_list var1, Object var2){
        return all( visitAllChildren(var1, var2) );
    };

    public Object visit(ASTvar_decl var1, Object var2){
        return (Boolean) true;
    };

    public Object visit(ASTconst_decl var1, Object var2){
        return all(visitAllChildren(var1, var2));
    };

    public Object visit(ASTidentifier var1, Object var2){
        return (Boolean) true;
    };

    public Object visit(ASTfunction_list var1, Object var2){
        return all( visitAllChildren(var1, var2));
    };

    public Object visit(ASTfunction var1, Object var2){
        Data d = ((Data)(var2));
        Table t = d.t.t;
        HashMap<String, ASTfunction> function_table = d.t.function_table;
        SimpleNode scope = d.scope;
        Data new_data =  new Data(var1, ((Data)(var2)).t);

        String ret_type = (String) ((SimpleNode) var1.jjtGetChild(0)).jjtGetValue();
        Boolean retval_correct = ret_type.equals("VOID") || ret_type.equals((String) ((SimpleNode) var1.jjtGetChild(5)).jjtAccept(this, new_data));

        if (!(retval_correct)) {
            throw new RuntimeException("Incorrect return value");
        }

        return all( visitAllChildren(var1, new_data));
    };

    public Object visit(ASTretval var1, Object var2){

        if(var1.jjtGetNumChildren() == 1){
            return var1.jjtGetChild(0).jjtAccept(this, var2);
        }

        return "VOID";
    };;

    public Object visit(ASTparameter_list var1, Object var2){
        return (Boolean) true;
    };

    public Object visit(ASTparameter var1, Object var2){
        return (Boolean) true;
    };

    public Object visit(ASTtype var1, Object var2){
        return (Boolean) true;
    };

    public Object visit(ASTmain_func var1, Object var2){
        return all( visitAllChildren(var1, ((Object) new Data(var1, ((Data)(var2)).t))));
    };

    public Object visit(ASTstatement_block var1, Object var2){
        return all( visitAllChildren(var1, var2));
    };

    public Object visit(ASTassignment_statement var1, Object var2){
        Data d = ((Data)(var2));
        Table t = d.t.t;
        HashMap<String, ASTfunction> function_table = d.t.function_table;
        SimpleNode scope = d.scope;

        String name = (String) ((SimpleNode) var1.jjtGetChild(0)).jjtGetValue();

        String type1 = t.lookup(t, scope, name);

        String type2 = (String) ((SimpleNode) var1.jjtGetChild(1)).jjtAccept(this, var2);


        if (!type1.equals(type2)) throw new RuntimeException("Incorrect type assigned to variable "+name);

        return true;
    };

    public Object visit(ASTfunction_call var1, Object var2){

        Data d = ((Data)(var2));
        Table t = d.t.t;
        HashMap<String, ASTfunction> function_table = d.t.function_table;
        SimpleNode scope = d.scope;

        String name = (String)(((SimpleNode) var1.jjtGetChild(0)).jjtGetValue());

        if(!function_table.containsKey(name))
            throw new RuntimeException("Function "+name+" not defined.");

        ASTfunction func = function_table.get(name);
        String type = (String)(((SimpleNode) func.jjtGetChild(0)).jjtGetValue());

        ASTparameter_list params = (ASTparameter_list) func.jjtGetChild(2);
        ASTarg_list args = (ASTarg_list) var1.jjtGetChild(1);

        if (params.jjtGetNumChildren() != args.jjtGetNumChildren())
            throw new RuntimeException("Wrong number of arguments supplied to function "+name);

        for(int i = 0; i<params.jjtGetNumChildren(); i++){
            String paramType = (String) ((SimpleNode) params.jjtGetChild(i).jjtGetChild(1)).jjtGetValue();
            String argType = t.lookup(t, scope, (String) ((SimpleNode) args.jjtGetChild(i)).jjtGetValue());
            if (!paramType.equals(argType))
                throw new RuntimeException("Incorrect arguments supplied to function "+name);
        }

        return type;
    };

    public Object visit(ASTbegin_end_statement var1, Object var2){
        return all( visitAllChildren(var1, var2));
    };

    public Object visit(ASTif_else_statement var1, Object var2){
        return all( visitAllChildren(var1, var2));
    };;

    public Object visit(ASTwhile_statement var1, Object var2){
        return all( visitAllChildren(var1, var2));
    };;

    public Object visit(ASTskip_statement var1, Object var2){
        return (Boolean) true;
    };

    public Object visit(ASTexpression var1, Object var2){
        return var1.jjtGetChild(0).jjtAccept(this, var2);
    };

    public Object visit(ASTcondition var1, Object var2){
        if (var1.jjtGetChild(0).jjtAccept(this, var2) != "BOOLEAN")
            throw new RuntimeException("Condition does not contain boolean.");

        return true;
    };

    public Object visit(ASTnot var1, Object var2){
        return var1.jjtGetChild(0).jjtAccept(this, var2) ;
    };

    public Object visit(ASTbool_expression var1, Object var2){
        if (var1.jjtGetNumChildren() == 3) {
            SimpleNode item1 = (SimpleNode) var1.jjtGetChild(0);
            SimpleNode item2 = (SimpleNode) var1.jjtGetChild(2);

            if (!(item1.jjtAccept(this, var2).equals("BOOLEAN") && item2.jjtAccept(this, var2).equals("BOOLEAN")))
                throw new RuntimeException("Invalid item in bool_expression");

            return "BOOLEAN";
        }

        return var1.jjtGetChild(0).jjtAccept(this, var2);
    };

    public Object visit(ASTcomp_expression var1, Object var2){
        if (var1.jjtGetNumChildren() == 3) {
            SimpleNode item1 = (SimpleNode) var1.jjtGetChild(0);
            SimpleNode item2 = (SimpleNode) var1.jjtGetChild(2);

            if (!(item1.jjtAccept(this, var2).equals( item2.jjtAccept(this, var2))))
                throw new RuntimeException("Invalid item in comp_expression");

            return "BOOLEAN";
        }

        return var1.jjtGetChild(0).jjtAccept(this, var2);
    };

    public Object visit(ASTarith_expression var1, Object var2){

        if (var1.jjtGetNumChildren() == 3) {
            SimpleNode item1 = (SimpleNode) var1.jjtGetChild(0);
            SimpleNode item2 = (SimpleNode) var1.jjtGetChild(2);

            if (!(item1.jjtAccept(this, var2).equals("INTEGER") && item2.jjtAccept(this, var2).equals("INTEGER")))
                    throw new RuntimeException("Invalid fragment in arith_expression");

            return "INTEGER";
        }

        return var1.jjtGetChild(0).jjtAccept(this, var2);
    };

    public Object visit(ASTfragment var1, Object var2){
        if(var1.jjtGetValue() instanceof Integer){
            return "INTEGER";
        }
        if(var1.jjtGetValue() instanceof Boolean){
            return "BOOLEAN";
        }
        return var1.jjtGetChild(0).jjtAccept(this, var2);
    };

    public Object visit(ASTvar_lookup var1, Object var2){
        Data d = ((Data)(var2));
        Table t = d.t.t;
        HashMap<String, ASTfunction> function_table = d.t.function_table;
        SimpleNode scope = d.scope;

        String name = (String) (((SimpleNode) var1.jjtGetChild(0)).jjtGetValue());

        return t.lookup(t, scope, name);
    };

    public Object visit(ASTnegate var1, Object var2){
        return var1.jjtGetChild(0).jjtAccept(this, var2);
    };

    public Object visit(ASTbinary_arith_op var1, Object var2){
        return true;
    };

    public Object visit(ASTbin_bool_op var1, Object var2){
        return true;
    };

    public Object visit(ASTcomp_op var1, Object var2){
        return true;
    };

    public Object visit(ASTarg_list var1, Object var2){
        return true;
    };
}