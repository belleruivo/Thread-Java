package com.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * VERSÃO ALTERNATIVA: Implementação usando BlockingQueue
 * 
 * Esta versão demonstra uma abordagem alternativa usando a classe
 * BlockingQueue do pacote java.util.concurrent, que já possui
 * sincronização interna e métodos thread-safe.
 * 
 * Comparação com a implementação manual:
 * - Menos código boilerplate
 * - Sincronização já implementada
 * - Métodos bloqueantes integrados
 * - Maior abstração das regiões críticas
 */
public class ThreadDemoWithBlockingQueue {
    
    public static void main(String[] args) {
        System.out.println("=== VERSÃO ALTERNATIVA COM BLOCKINGQUEUE ===");
        System.out.println("Demonstração usando java.util.concurrent.BlockingQueue\n");
        
        final int NUMBER_OF_ELEMENTS = 10;
        
        /*
         * RECURSO COMPARTILHADO COM SINCRONIZAÇÃO INTERNA:
         * BlockingQueue já implementa todas as sincronizações necessárias
         * internamente, eliminando a necessidade de gerenciar regiões críticas
         * manualmente
         */
        BlockingQueue<Integer> sharedQueue = new LinkedBlockingQueue<>();
        
        // Thread Produtora usando BlockingQueue
        Thread producer = new Thread(() -> {
            System.out.println("T1 (Produtor) iniciada");
            try {
                for (int i = 1; i <= NUMBER_OF_ELEMENTS; i++) {
                    Thread.sleep(500);
                    
                    /*
                     * REGIÃO CRÍTICA ABSTRATA:
                     * O método put() da BlockingQueue gerencia internamente
                     * toda a sincronização necessária
                     */
                    sharedQueue.put(i);
                    System.out.println("T1 (Produtor) adicionou: " + i + 
                                     " | Tamanho da fila: " + sharedQueue.size());
                }
                System.out.println("T1 (Produtor) finalizou a produção");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "T1-Producer-BlockingQueue");
        
        // Thread Consumidora usando BlockingQueue
        Thread consumer = new Thread(() -> {
            System.out.println("T2 (Consumidor) iniciada");
            try {
                for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
                    /*
                     * REGIÃO CRÍTICA ABSTRATA:
                     * O método take() da BlockingQueue:
                     * - Gerencia sincronização automaticamente
                     * - Bloqueia se a fila estiver vazia
                     * - Não requer wait()/notify() manual
                     */
                    Integer value = sharedQueue.take();
                    System.out.println("T2 (Consumidor) removeu: " + value + 
                                     " | Tamanho da fila: " + sharedQueue.size());
                    
                    System.out.println("T2 (Consumidor) processando valor: " + value);
                    Thread.sleep(300);
                }
                System.out.println("T2 (Consumidor) finalizou o consumo");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "T2-Consumer-BlockingQueue");
        
        long startTime = System.currentTimeMillis();
        
        producer.start();
        consumer.start();
        
        try {
            producer.join();
            consumer.join();
            
            long endTime = System.currentTimeMillis();
            System.out.println("\n=== EXECUÇÃO FINALIZADA ===");
            System.out.println("Tempo total: " + (endTime - startTime) + "ms");
            System.out.println("Fila vazia: " + sharedQueue.isEmpty());
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}