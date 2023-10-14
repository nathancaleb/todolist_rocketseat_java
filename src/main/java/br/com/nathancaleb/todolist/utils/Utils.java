package br.com.nathancaleb.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import ch.qos.logback.core.joran.util.beans.BeanUtil;


//AO USUARIO ATUALIZAR A TASK, IRA VERIFICAR SE ELE NÃO DEIXO NENHUMA INFORMAÇÃO SEM ATUALIZAÇÃO.
public class Utils {
    
    //Vai pegar os valores antigos e substituir pelo que estiver nulo e depois mesclar as informações
    public static void copyNonNullProperties(Object source, Object target){
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    //METODO PARA PEGAR TODA PROPRIEDADE QUE ESTIVER COMO NULA
    public static String[] getNullPropertyNames(Object source){
        //BeanWrapper: interface do Java que permiti uma forma de acessar as propriedades de um objeto .. com IMPL no final é a implementação dessa interface.
        final BeanWrapper src = new BeanWrapperImpl(source);

        PropertyDescriptor[] pds = src.getPropertyDescriptors(); //Pegamos as propriedades do objeto e atribuimos cada uma em um array

        Set<String> emptyNames = new HashSet<>();

        for(PropertyDescriptor pd: pds){
            Object srcValue = src.getPropertyValue(pd.getName());
            if(srcValue == null){
                emptyNames.add(pd.getName()); //Vai adicionar ao array, todas as propriedades que estiverem nulo.
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
