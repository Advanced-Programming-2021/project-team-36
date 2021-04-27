package controller;

abstract public class BaseMenu {
    abstract protected static void showCurrentMenu();
    abstract protected static void navigateToMenu(String menu);
    abstract protected static void exit();
    abstract public static void programControl();
}
