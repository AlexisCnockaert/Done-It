package com.example.todo.event;

import org.springframework.context.ApplicationEvent;

public class DemoResetEvent extends ApplicationEvent {
    private final String demoEmail;

    public DemoResetEvent(Object source, String demoEmail) {
        super(source);
        this.demoEmail = demoEmail;
    }

    public String getDemoEmail() {
        return demoEmail;
    }
}
