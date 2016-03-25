package com.lqipr;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Created by lqipr on 2015/7/23.
 */
public class TempTest {

    public static void main(String[] args) {
        Hehe<String> hehe = new Hehe<String>();
        Class c = hehe.getClass();




        TypeVariable[] typeParameters = c.getTypeParameters();
        for (TypeVariable typeParameter : typeParameters) {
            System.out.println(typeParameter.getClass());
        }


    }

    public static Class<?> getSuperClassGenricType(Class<?> clazz,int index){
        ParameterizedType type=null;
        Type temp=clazz.getGenericSuperclass();
        do{
            if(temp instanceof ParameterizedType){
                type=(ParameterizedType) temp;
                break;
            }
            if(!(temp instanceof Class<?>)) break;
            temp=((Class<?>)temp).getGenericSuperclass();
            if(temp==Object.class) break;

        }while(temp!=null);
        if(type==null) return null;
        Type[] params = ((ParameterizedType) type).getActualTypeArguments();
        if (index >= params.length || index < 0)
            throw new RuntimeException("the index must be between 0 and "+(params.length-1));
        if(!(params[index] instanceof Class)) return null;
        return (Class<?>) params[index];

    }
}
