package org.example;

public class HelloWorld {
    private String message;
    public void setMessage(String message) {
        this.message = message;
    }
    public void printMessage(){
        System.out.println("Message : " + message);
    }
}
