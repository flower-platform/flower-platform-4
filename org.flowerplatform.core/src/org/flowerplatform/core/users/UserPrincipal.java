package org.flowerplatform.core.users;

import java.security.Principal;

public class UserPrincipal implements Principal { 
    private String username;
    
    private String email;
    
    public UserPrincipal(){}
    
    public UserPrincipal(String username) { 
        this.username = username; 
    }
    
    public UserPrincipal(String username, String email) { 
        this.username = username; 
        this.email = email;
    }
    
    public String getName() { 
        return username; 
    } 
    
    public String getEmail() { 
        return email; 
    } 
} 