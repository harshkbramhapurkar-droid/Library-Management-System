package app;

import ui.LoginUI;

public class LibraryApp {
    public static void main(String[] args) {
        
        new LibraryApp().start();
    }

    public void start() {
        LoginUI loginUI = new LoginUI();
        loginUI.showLoginLoop(); 
        System.out.println("\nShutting down LibraryApp. Goodbye!");
    }
}
