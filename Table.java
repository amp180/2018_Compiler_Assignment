
class Table {

    String id;
    String value;
    SimpleNode scope;
    Table tail;

    Table(String i, SimpleNode scope, String v, Table t) {id=i; this.scope = scope; value=v; tail=t;}

    static Table update(Table t, SimpleNode s, String id, String value) {
        if (t != null && (t.non_recursive_lookup(t, s, id) != null))
            throw new RuntimeException("Variable "+id+" defined multiple times in "+s.toString());
        return new Table(id,s,value,t);
    }

    String non_recursive_lookup(Table t, SimpleNode s, String key) {
        if ((t.id).equals(key) && (t.scope).equals(s))
            return t.value;

        if (t.tail == null)
            return null;

        return non_recursive_lookup(t.tail, s, key);
    }

    String lookup(Table t, SimpleNode s, String key) {
        Node n = s;
        if ((t.id).equals(key)) {
            while (n != null && !t.scope.equals(n)) {
                n = n.jjtGetParent();
            }

            if (t.scope.equals(n))
                return t.value;
        }
        if (t.tail == null)
            return null;
        return lookup(t.tail, s, key);
    }


}