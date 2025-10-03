package com.example;

/**
 * Thread T2 - Consumidora
 * Responsável por consumir e imprimir os valores da lista compartilhada
 */
public class ConsumerThread extends Thread {
    private final SharedList sharedList;
    private final int numberOfElements;
    private volatile boolean stopRequested = false;
    
    public ConsumerThread(SharedList sharedList, int numberOfElements) {
        this.sharedList = sharedList;
        this.numberOfElements = numberOfElements;
        this.setName("T2-Consumer");
    }
    
    @Override
    public void run() {
        System.out.println("T2 (Consumidor) iniciada");
        
        int consumedElements = 0;
        
        try {
            while (consumedElements < numberOfElements && !stopRequested) {
                /*
                 * ACESSO À REGIÃO CRÍTICA:
                 * Chamada ao método remove() da SharedList
                 * Esta operação precisa ser thread-safe e pode bloquear
                 * se não houver elementos disponíveis
                 */
                Integer value = sharedList.remove();
                
                // Simula algum processamento do elemento consumido
                System.out.println("T2 (Consumidor) processando valor: " + value);
                Thread.sleep(300); // 300ms
                
                consumedElements++;
            }
            
            System.out.println("T2 (Consumidor) finalizou o consumo de " + consumedElements + " elementos");
            
        } catch (InterruptedException e) {
            System.err.println("T2 (Consumidor) foi interrompida: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Método para solicitar parada da thread consumidora
     */
    public void requestStop() {
        this.stopRequested = true;
        this.interrupt();
    }
}