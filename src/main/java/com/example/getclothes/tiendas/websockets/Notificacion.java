package com.example.getclothes.tiendas.websockets;

public record Notificacion<T>(
        String entity,
        Tipo type,
        T data,
        String createdAt
) {
    public enum Tipo {
        CREATE, UPDATE, DELETE
    }
}
