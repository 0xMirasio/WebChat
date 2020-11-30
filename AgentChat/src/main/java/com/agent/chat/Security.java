package com.agent.chat;

public class Security {
    public boolean verifyPassword(String password) {
        FileOperation filework = new FileOperation();
        String password_hash=  filework.getPassword();
        String sig = filework.getSignature(); // a changer 
        String password_hash_user_input = Integer.toString(password.hashCode());
        String sig_user_input = Integer.toString(Integer.toString(password.hashCode()).hashCode());
        
        return (sig.equals(sig_user_input) && password_hash_user_input.equals(password_hash));
    }
}
