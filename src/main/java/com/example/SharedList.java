package com.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma lista compartilhada entre threads
 * com operações thread-safe para adicionar e remover elementos
 */
public class SharedList {
    private final List<Integer> list;
    private final Object lock;
    
    public SharedList() {
        this.list = new ArrayList<>();
        this.lock = new Object();
    }
    
    /**
     * REGIÃO CRÍTICA: Método para adicionar elementos à lista
     * Precisa ser sincronizado para evitar condições de corrida
     */
    public void add(Integer value) {
        synchronized (lock) {  // INÍCIO DA REGIÃO CRÍTICA
            list.add(value);
            System.out.println("T1 (Produtor) adicionou: " + value + " | Tamanho da lista: " + list.size());
            
            // Notifica threads que estão esperando por novos elementos
            lock.notify();
        }  // FIM DA REGIÃO CRÍTICA
    }
    
    /**
     * REGIÃO CRÍTICA: Método para remover e retornar o primeiro elemento da lista
     * Precisa ser sincronizado para evitar condições de corrida
     */
    public Integer remove() throws InterruptedException {
        synchronized (lock) {  // INÍCIO DA REGIÃO CRÍTICA
            // Se a lista estiver vazia, aguarda até que algum elemento seja adicionado
            while (list.isEmpty()) {
                System.out.println("T2 (Consumidor) aguardando elementos...");
                lock.wait();  // Libera o lock e aguarda notificação
            }
            
            Integer value = list.remove(0);
            System.out.println("T2 (Consumidor) removeu: " + value + " | Tamanho da lista: " + list.size());
            return value;
        }  // FIM DA REGIÃO CRÍTICA
    }
    
    /**
     * REGIÃO CRÍTICA: Método para verificar se a lista está vazia
     * Precisa ser sincronizado para garantir leitura consistente
     */
    public boolean isEmpty() {
        synchronized (lock) {  // INÍCIO DA REGIÃO CRÍTICA
            return list.isEmpty();
        }  // FIM DA REGIÃO CRÍTICA
    }
    
    /**
     * REGIÃO CRÍTICA: Método para obter o tamanho da lista
     * Precisa ser sincronizado para garantir leitura consistente
     */
    public int size() {
        synchronized (lock) {  // INÍCIO DA REGIÃO CRÍTICA
            return list.size();
        }  // FIM DA REGIÃO CRÍTICA
    }
}