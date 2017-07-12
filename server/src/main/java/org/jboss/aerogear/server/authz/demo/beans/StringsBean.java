/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.server.authz.demo.beans;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author summers
 */
@ApplicationScoped
public class StringsBean {
    
    private final Set<String> strings = new HashSet<>();
    
    public String getStrings() {
        return strings.stream().collect(Collectors.joining(", "));
    }
    
    public void addString(String string) {
        strings.add(string);
    }
    
    public void removeString(String string) {
        strings.remove(string);
    }
    
    public boolean hasString(String string) {
        return strings.contains(string);
    }
    
}
