package com.example;

/**
 * Thread T1 - Produtora
 * Responsável por popular a lista compartilhada com valores inteiros
 */
public class ProducerThread extends Thread {
    private final SharedList sharedList;
    private final int numberOfElements;
    
    public ProducerThread(SharedList sharedList, int numberOfElements) {
        this.sharedList = sharedList;
        this.numberOfElements = numberOfElements;
        this.setName("T1-Producer");
    }
    
    @Override
    public void run() {
        System.out.println("T1 (Produtor) iniciada");
        
        try {
            for (int i = 1; i <= numberOfElements; i++) {
                // Simula algum processamento antes de adicionar o elemento
                Thread.sleep(500); // 500ms
                
                /*
                 * ACESSO À REGIÃO CRÍTICA:
                 * Chamada ao método add() da SharedList
                 * Esta operação precisa ser thread-safe
                 */
                sharedList.add(i);
            }
            
            System.out.println("T1 (Produtor) finalizou a produção de " + numberOfElements + " elementos");
            
        } catch (InterruptedException e) {
            System.err.println("T1 (Produtor) foi interrompida: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}