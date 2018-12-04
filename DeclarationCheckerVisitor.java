import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DeclarationCheckerVisitor implements CCALCheckerVisitor {

    HashMap<SimpleNode, HashSet<String>> assignedVariables = new HashMap<>();

    class Data {
        SimpleNode scope;
        FuncTableAndTable t;

        Data(SimpleNode scope, FuncTableAndTable t){
            this.scope = scope;
            this.t = t;
        }
    };

    private Boolean all(ArrayList<Boolean> bs){
        for(Boolean b : bs) {
            if (!b.booleanValue()) {
                return (Boolean) false;
            }
        }
        return (Boolean)true;
    }

    private ArrayList<Boolean> visitAllChildren(SimpleNode var1, Object var2) {
        ArrayList<Boolean> arr = new ArrayList<>();
        for (int i = 0; i<var1.jjtGetNumChildren(); i++){
            arr.add((Boolean)var1.jjtGetChild(i).jjtAccept(this, var2));
        }
        return arr;
    }

    public Object visit(SimpleNode var1, Object var2) {
        return (Boolean) true;
    };

    public Object visit(ASTprogram var1, Object var2){
        assignedVariables.put(var1, new HashSet<>());
        return all( visitAllChildren(var1, ((Object) new Data(var1, (FuncTableAndTable)(var1.jjtGetValue())))));
    };

    public Object visit(ASTdecl_list var1, Object var2){
        assert(all( visitAllChildren(var1, var2) ));
        return all( visitAllChildren(var1, var2) );
    };

    public Object visit(ASTvar_decl var1, Object var2){
        return (Boolean) true;
    };

    public Object visit(ASTconst_decl var1, Object var2){
        Data d = (Data)(var2);
        SimpleNode scope = d.scope;
        String name = (String) ((SimpleNode)var1.jjtGetChild(0)).jjtGetValue();

        assignedVariables.get(scope).add(name);

        assert(all(visitAllChildren(var1, var2)));

        return all(visitAllChildren(var1, var2));
    };

    public Object visit(ASTidentifier var1, Object var2){
        return (Boolean) true;
    };

    public Object visit(ASTfunction_list var1, Object var2){
        return all( visitAllChildren(var1, var2));

    };

    public Object visit(ASTfunction var1, Object var2){
        assignedVariables.put(var1, new HashSet<>());
        return all( visitAllChildren(var1, ((Object) new Data(var1, ((Data)(var2)).t))));
    };

    public Object visit(ASTretval var1, Object var2){
        return all( visitAllChildren(var1, var2));
    };;

    public Object visit(ASTparameter_list var1, Object var2){
        return all( visitAllChildren(var1, var2));
    };

    public Object visit(ASTparameter var1, Object var2){
        Data d = (Data)(var2);
        SimpleNode scope = d.scope;
        String name = (String) ((SimpleNode)var1.jjtGetChild(0)).jjtGetValue();

        assignedVariables.get(scope).add(name);

        return (Boolean) true;
    };

    public Object visit(ASTtype var1, Object var2){
        return (Boolean) true;
    };

    public Object visit(ASTmain_func var1, Object var2){
        assignedVariables.put(var1, new HashSet<>());
        return all( visitAllChildren(var1, ((Object) new Data(var1, ((Data)(var2)).t))));
    };

    public Object visit(ASTstatement_block var1, Object var2){
        return all( visitAllChildren(var1, var2));
    };

    public Object visit(ASTassignment_statement var1, Object var2){
        Data d = (Data)(var2);
        SimpleNode scope = d.scope;
        String name = (String) ((SimpleNode)var1.jjtGetChild(0)).jjtGetValue();

        assignedVariables.get(scope).add(name);

        return ((d.t.t.lookup(d.t.t, d.scope, name) != null) && all( visitAllChildren(var1, var2)));
    };

    public Object visit(ASTfunction_call var1, Object var2){
        return (Boolean) true;
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
        return all( visitAllChildren(var1, var2) );
    };

    public Object visit(ASTcondition var1, Object var2){
        return all( visitAllChildren(var1, var2) );
    };

    public Object visit(ASTnot var1, Object var2){
        return all( visitAllChildren(var1, var2) );
    };

    public Object visit(ASTbool_expression var1, Object var2){
        return all( visitAllChildren(var1, var2) );
    };

    public Object visit(ASTcomp_expression var1, Object var2){
        return all( visitAllChildren(var1, var2) );
    };

    public Object visit(ASTarith_expression var1, Object var2){
        return all( visitAllChildren(var1, var2) );
    };

    public Object visit(ASTfragment var1, Object var2){
        return all( visitAllChildren(var1, var2) );
    };

    public Object visit(ASTvar_lookup var1, Object var2){

       Data d = (Data)(var2);

        ASTidentifier i = (ASTidentifier) var1.jjtGetChild(0);

        if (!assignedVariables.containsKey(d.scope) || !assignedVariables.get(d.scope).contains(i.jjtGetValue())){
            return false;
        }

        return (d.t.t.lookup(d.t.t, d.scope, (String)i.jjtGetValue()) != null);
    };

    public Object visit(ASTnegate var1, Object var2){
        return all( visitAllChildren(var1, var2) );
    };

    public Object visit(ASTbinary_arith_op var1, Object var2){
        return all( visitAllChildren(var1, var2));
    };

    public Object visit(ASTbin_bool_op var1, Object var2){
        return all( visitAllChildren(var1, var2));
    };

    public Object visit(ASTcomp_op var1, Object var2){
        return (Boolean) true;
    };

    public Object visit(ASTarg_list var1, Object var2){
        return all( visitAllChildren(var1, var2));
    };
}