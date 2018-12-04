import java.util.*;

public class FuncTableAndTable {
    public FuncTableAndTable(HashMap<String, ASTfunction> function_table, Table t){
        this.t = t;
        this.function_table = function_table;
    }

    Table t;
    HashMap<String, ASTfunction>  function_table;
}
