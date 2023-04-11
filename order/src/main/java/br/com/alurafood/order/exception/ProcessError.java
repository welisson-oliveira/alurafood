package br.com.alurafood.order.exception;

public class ProcessError extends RuntimeException {
    public ProcessError(final String message) {
        super(message);
    }
}
