package com.dalbong.cafein.handler.exception;

public class StickerExcessException extends RuntimeException{
    private static final long serialVersionUID=1L;

    public StickerExcessException(String message) {
        super(message);
    }
}
