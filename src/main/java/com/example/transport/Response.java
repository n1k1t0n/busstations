package com.example.transport;

public class Response {

    private final String from;
    private final String to;
    private final boolean direct;

    public Response(String from, String to, boolean direct) {
        this.from = from;
        this.to = to;
        this.direct = direct;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public boolean isDirect() {
        return direct;
    }
}
