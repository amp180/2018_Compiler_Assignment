# Parser

This was a parser written in Java-cc as part of an assignment in 2017.
It performs simple type checks and generate a made-up IR from the code it parses.

eg.

```

main
begin
/* a simple comment */
/* a comment /* with /* several */ nested */ comments */
end

```

```
void  func  ()  is
begin
return  ( ) ;
end

main
begin
func  ( ) ;
end
```