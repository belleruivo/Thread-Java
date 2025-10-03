package com.example;

/**
 * Classe de teste para validar o comportamento das threads
 * e identificar possíveis problemas de concorrência
 */
public class ConcurrencyTest {
    
    public static void main(String[] args) {
        System.out.println("=== TESTE DE CONCORRÊNCIA ===");
        System.out.println("Testando comportamento com múltiplas execuções\n");
        
        // Executa o teste múltiplas vezes para detectar problemas de concorrência
        for (int test = 1; test <= 3; test++) {
            System.out.println("--- TESTE " + test + " ---");
            runSingleTest();
            System.out.println();
            
            // Pausa entre testes
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("=== TESTE DE STRESS ===");
        runStressTest();
    }
    
    /**
     * Executa um teste individual com poucos elementos
     */
    private static void runSingleTest() {
        SharedList sharedList = new SharedList();
        final int ELEMENTS = 5;
        
        ProducerThread producer = new ProducerThread(sharedList, ELEMENTS);
        ConsumerThread consumer = new ConsumerThread(sharedList, ELEMENTS);
        
        long startTime = System.currentTimeMillis();
        
        producer.start();
        consumer.start();
        
        try {
            producer.join();
            consumer.join();
            
            long endTime = System.currentTimeMillis();
            System.out.println("Tempo: " + (endTime - startTime) + "ms | " +
                             "Lista final: " + (sharedList.isEmpty() ? "VAZIA ✓" : "COM ELEMENTOS ✗"));
            
        } catch (InterruptedException e) {
            System.err.println("Teste interrompido");
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Teste de stress com mais elementos e múltiplas threads
     */
    private static void runStressTest() {
        SharedList sharedList = new SharedList();
        final int ELEMENTS_PER_PRODUCER = 20;
        final int NUM_PRODUCERS = 2;
        final int NUM_CONSUMERS = 2;
        final int TOTAL_ELEMENTS = ELEMENTS_PER_PRODUCER * NUM_PRODUCERS;
        
        System.out.println("Produtores: " + NUM_PRODUCERS + 
                          " | Consumidores: " + NUM_CONSUMERS + 
                          " | Total elementos: " + TOTAL_ELEMENTS);
        
        Thread[] producers = new Thread[NUM_PRODUCERS];
        Thread[] consumers = new Thread[NUM_CONSUMERS];
        
        // Cria produtores
        for (int i = 0; i < NUM_PRODUCERS; i++) {
            final int producerId = i + 1;
            producers[i] = new Thread(() -> {
                try {
                    for (int j = 1; j <= ELEMENTS_PER_PRODUCER; j++) {
                        Thread.sleep(100); // Produção mais rápida
                        int value = (producerId * 100) + j; // Valor único por produtor
                        sharedList.add(value);
                    }
                    System.out.println("Produtor " + producerId + " finalizou");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Producer-" + producerId);
        }
        
        // Cria consumidores
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            final int consumerId = i + 1;
            final int elementsPerConsumer = TOTAL_ELEMENTS / NUM_CONSUMERS;
            
            consumers[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < elementsPerConsumer; j++) {
                        Integer value = sharedList.remove();
                        Thread.sleep(50); // Consumo mais rápido
                    }
                    System.out.println("Consumidor " + consumerId + " finalizou");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Consumer-" + consumerId);
        }
        
        long startTime = System.currentTimeMillis();
        
        // Inicia todos os produtores
        for (Thread producer : producers) {
            producer.start();
        }
        
        // Inicia todos os consumidores
        for (Thread consumer : consumers) {
            consumer.start();
        }
        
        try {
            // Aguarda todos os produtores
            for (Thread producer : producers) {
                producer.join();
            }
            
            // Aguarda todos os consumidores
            for (Thread consumer : consumers) {
                consumer.join();
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("Teste de stress concluído em " + (endTime - startTime) + "ms");
            System.out.println("Estado final - Tamanho: " + sharedList.size() + 
                             " | Vazia: " + sharedList.isEmpty());
            
            if (sharedList.isEmpty()) {
                System.out.println("✓ SUCESSO: Todos os elementos foram produzidos e consumidos corretamente");
            } else {
                System.out.println("✗ ERRO: Elementos restantes na lista - possível problema de sincronização");
            }
            
        } catch (InterruptedException e) {
            System.err.println("Teste de stress interrompido");
            Thread.currentThread().interrupt();
        }
    }
}