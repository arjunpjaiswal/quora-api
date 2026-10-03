package org.example.quoraappapi.exceptions;

public record ErrorResponse(int status, String message) {}