//package com.ntr1x.treasure.web.reflection;
//
//import static java.lang.annotation.ElementType.FIELD;
//import static java.lang.annotation.ElementType.METHOD;
//
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//import org.glassfish.hk2.api.AnnotationLiteral;
//
//@Retention(RetentionPolicy.RUNTIME)
//@Target({ METHOD, FIELD })
//public @interface ResourceProperty {
//    
//    Type update() default Type.ASSIGN;
//    Type create() default Type.ASSIGN;
//    String locator() default "";
//    
//    enum Type {
//        
//        IGNORE,
//        ASSIGN,
//        CASCADE,
//    }
//    
//    public static class Default extends AnnotationLiteral<ResourceProperty> implements ResourceProperty {
//        
//        private static final long serialVersionUID = 7421101557333337604L;
//
//        @Override
//        public Type update() { return Type.ASSIGN; }
//
//        @Override
//        public Type create() { return Type.ASSIGN; }
//
//        @Override
//        public String locator() { return ""; }
//        
//        public static ResourceProperty get() {
//            
//            return new Default();
//        }
//    }
//}
