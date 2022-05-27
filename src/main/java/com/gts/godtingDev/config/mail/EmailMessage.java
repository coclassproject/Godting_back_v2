package com.gts.godtingDev.config.mail;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class EmailMessage {

    private String to;

    private String subject;

    private String message;
}
