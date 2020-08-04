package com.kj.jwt.utils.payloads.responses;

import lombok.Getter;

@Getter
public class MessageAndBodyResponse extends MessageResponse {
    private final Object body;

    public MessageAndBodyResponse(String message, Object body) {
        super(message);
        this.body = body;
    }
}
