package com.dh.common.restful.controller;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component  
public class MyHealthChecker implements HealthIndicator {  
  
  
    private boolean up = true;  
  
    @Override  
    public Health health() {  
        if (up) {  
            return new Health.Builder().up().build();  
        } else {  
            return new Health.Builder().down().build();  
        }  
  
    }  
  
    public boolean isUp() {  
        return up;  
    }  
  
    public void setUp(boolean up) {  
        this.up = up;  
    }  
}  