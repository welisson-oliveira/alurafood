package br.com.alurafood.order.exception;

public class ProcessingError extends RuntimeException {
    public ProcessingError(final String message) {
        super(message);
    }
}
