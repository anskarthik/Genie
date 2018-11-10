package com.coacle.genie.event;

import com.coacle.genie.model.User;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Data
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private String appUrl;
    private Locale locale;
    private User user;

    public OnRegistrationCompleteEvent(
            User user, Locale locale, String appUrl) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

}
