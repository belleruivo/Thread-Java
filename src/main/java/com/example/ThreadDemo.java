package com.example;

/**
 * Classe principal que demonstra o uso de threads com lista compartilhada
 * 
 * DESCRIÇÃO DO PROBLEMA:
 * - Thread T1 (Produtora): Popula valores inteiros em uma lista compartilhada
 * - Thread T2 (Consumidora): Consome e imprime esses valores
 * - As duas operações ocorrem em paralelo
 * 
 * REGIÕES CRÍTICAS IDENTIFICADAS:
 * 1. Método add() da SharedList - acesso concorrente para inserção
 * 2. Método remove() da SharedList - acesso concorrente para remoção
 * 3. Métodos isEmpty() e size() - leitura consistente do estado da lista
 * 
 * SINCRONIZAÇÃO UTILIZADA:
 * - synchronized blocks com objeto lock dedicado
 * - wait()/notify() para coordenação entre threads
 */
public class ThreadDemo {
    
    public static void main(String[] args) {
        System.out.println("=== DEMONSTRAÇÃO DE THREADS COM LISTA COMPARTILHADA ===");
        System.out.println("T1 (Produtora) irá adicionar elementos");
        System.out.println("T2 (Consumidora) irá consumir elementos");
        System.out.println("Ambas executam em paralelo\n");
        
        // Número de elementos a serem produzidos/consumidos
        final int NUMBER_OF_ELEMENTS = 10;
        
        /*
         * RECURSO COMPARTILHADO:
         * Instância única da SharedList que será acessada por ambas as threads
         * Esta é a fonte das regiões críticas que precisam de sincronização
         */
        SharedList sharedList = new SharedList();
        
        // Criação das threads
        ProducerThread producer = new ProducerThread(sharedList, NUMBER_OF_ELEMENTS);
        ConsumerThread consumer = new ConsumerThread(sharedList, NUMBER_OF_ELEMENTS);
        
        // Configuração de prioridades (opcional)
        producer.setPriority(Thread.NORM_PRIORITY);
        consumer.setPriority(Thread.NORM_PRIORITY);
        
        System.out.println("Iniciando execução paralela das threads...\n");
        
        // Marca o tempo de início
        long startTime = System.currentTimeMillis();
        
        // Inicia as threads em paralelo
        producer.start();
        consumer.start();
        
        try {
            // Aguarda ambas as threads terminarem
            producer.join();
            consumer.join();
            
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            
            System.out.println("\n=== EXECUÇÃO FINALIZADA ===");
            System.out.println("Tempo total de execução: " + executionTime + "ms");
            System.out.println("Estado final da lista - Tamanho: " + sharedList.size());
            System.out.println("Lista vazia: " + sharedList.isEmpty());
            
        } catch (InterruptedException e) {
            System.err.println("Thread principal foi interrompida: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}