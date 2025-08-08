package com.example.movie_browser_application;

class ControllerData {
    private final HelloController controller;
    private final double scrollValue;

    public ControllerData(HelloController controller, double scrollValue) {
        this.controller = controller;
        this.scrollValue = scrollValue;
    }

    public HelloController controller() {
        return controller;
    }

    public double scrollValue() {
        return scrollValue;
    }
}
