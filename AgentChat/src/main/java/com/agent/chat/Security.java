package com.agent.chat;

/*
 Classe utilisée pour vérifier un mot de passe hashé
 */
public class Security {
    public boolean verifyPassword(String password) {
        FileOperation filework = new FileOperation();
        String password_hash=  filework.getPassword();
        String password_hash_user_input = Integer.toString(password.hashCode());        
        return (password_hash_user_input.equals(password_hash));
    }
}