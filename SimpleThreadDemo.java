import java.util.ArrayList;
import java.util.List;

/**
 * Demonstração simples de threads com lista compartilhada
 * Thread T1 (Producer) popula valores inteiros
 * Thread T2 (Consumer) consome e imprime os valores
 * Execução em paralelo
 */
public class SimpleThreadDemo {
    
    // Lista compartilhada entre as threads - REGIÃO CRÍTICA
    private static List<Integer> sharedList = new ArrayList<>();
    private static boolean finished = false;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== DEMONSTRAÇÃO SIMPLES DE THREADS ===\n");
        
        // Criando as threads
        Thread producer = new Thread(new Producer());
        Thread consumer = new Thread(new Consumer());
        
        // Iniciando execução paralela
        producer.start();
        consumer.start();
        
        // Aguarda as threads terminarem
        producer.join();
        consumer.join();
        
        System.out.println("\nExecução finalizada!");
    }
    
    /**
     * Thread T1 - Produtora
     * Popula valores inteiros na lista compartilhada
     */
    static class Producer implements Runnable {
        @Override
        public void run() {
            System.out.println("Producer iniciado - adicionando valores...");
            
            for (int i = 1; i <= 10; i++) {
                // REGIÃO CRÍTICA - acesso à lista compartilhada
                synchronized (sharedList) {
                    sharedList.add(i);
                    System.out.println("Producer adicionou: " + i);
                    
                    // Notifica consumer que há novo item
                    sharedList.notify();
                }
                
                // Simula tempo de processamento
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            // Marca como finalizado
            synchronized (sharedList) {
                finished = true;
                sharedList.notify(); // Acorda consumer para verificar se terminou
            }
            
            System.out.println("Producer finalizado!");
        }
    }
    
    /**
     * Thread T2 - Consumidora  
     * Consome e imprime valores da lista compartilhada
     */
    static class Consumer implements Runnable {
        @Override
        public void run() {
            System.out.println("Consumer iniciado - consumindo valores...");
            
            while (true) {
                // REGIÃO CRÍTICA - acesso à lista compartilhada
                synchronized (sharedList) {
                    // Aguarda enquanto lista estiver vazia e producer não terminou
                    while (sharedList.isEmpty() && !finished) {
                        try {
                            sharedList.wait(); // Aguarda notificação do producer
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    
                    // Se há itens na lista, consome
                    if (!sharedList.isEmpty()) {
                        Integer value = sharedList.remove(0);
                        System.out.println("Consumer consumiu: " + value);
                    }
                    
                    // Se producer terminou e lista está vazia, sai do loop
                    if (finished && sharedList.isEmpty()) {
                        break;
                    }
                }
                
                // Simula tempo de processamento
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            System.out.println("Consumer finalizado!");
        }
    }
}

/*
 * REGIÕES CRÍTICAS IDENTIFICADAS:
 * 
 * 1. Lista compartilhada 'sharedList' (linha 13):
 *    - Acessada por ambas as threads simultaneamente
 *    - Protegida com synchronized(sharedList)
 * 
 * 2. Variável 'finished' (linha 14):
 *    - Indica quando producer terminou
 *    - Acessada dentro do bloco synchronized
 * 
 * 3. Bloco synchronized no Producer (linhas 35-41):
 *    - Adiciona item à lista de forma thread-safe
 *    - Notifica consumer sobre novo item
 * 
 * 4. Bloco synchronized no Consumer (linhas 67-85):
 *    - Remove item da lista de forma thread-safe
 *    - Usa wait() para aguardar novos itens
 *    - Verifica condição de parada
 * 
 * SINCRONIZAÇÃO UTILIZADA:
 * - synchronized: Garante acesso exclusivo à região crítica
 * - wait(): Consumer aguarda quando lista está vazia
 * - notify(): Producer avisa quando adiciona novo item
 */