package com.ntr1x.treasure.web.reflection;

public @interface ManagedProperty {
    
    Type update() default Type.ASSIGN;
    Type create() default Type.ASSIGN;
    
    enum Type {
        
        IGNORE,
        ASSIGN,
        CASCADE,
    }
}
