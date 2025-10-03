# Demonstração de Threads Java com Lista Compartilhada

Este projeto demonstra o uso de threads em Java com uma lista compartilhada, onde uma thread produtora (T1) adiciona elementos e uma thread consumidora (T2) remove e processa esses elementos em paralelo.

## 📋 Descrição do Problema

- **Thread T1 (Produtora)**: Popula valores inteiros em uma lista compartilhada
- **Thread T2 (Consumidora)**: Consome e imprime esses valores  
- **Execução**: As duas operações ocorrem em paralelo

## 🎯 Objetivos

- Demonstrar programação concorrente com threads
- Identificar e proteger regiões críticas
- Implementar sincronização thread-safe
- Coordenar produção e consumo de dados

## 🏗️ Estrutura do Projeto

```
src/main/java/com/example/
├── SharedList.java      # Lista compartilhada thread-safe
├── ProducerThread.java  # Thread produtora (T1)
├── ConsumerThread.java  # Thread consumidora (T2)
└── ThreadDemo.java      # Classe principal
```

## 🔒 Regiões Críticas Identificadas

### 1. **Método `add()` na SharedList**
- **Problema**: Múltiplas threads podem tentar adicionar elementos simultaneamente
- **Solução**: Sincronização com `synchronized(lock)`
- **Localização**: `SharedList.java`, linhas 21-28

### 2. **Método `remove()` na SharedList**  
- **Problema**: Acesso concorrente para remoção e verificação de lista vazia
- **Solução**: Sincronização com `synchronized(lock)` + `wait()/notify()`
- **Localização**: `SharedList.java`, linhas 35-49

### 3. **Métodos `isEmpty()` e `size()` na SharedList**
- **Problema**: Leitura inconsistente do estado da lista
- **Solução**: Sincronização para garantir leitura atômica
- **Localização**: `SharedList.java`, linhas 55-67

## 🔧 Mecanismos de Sincronização Utilizados

### **synchronized blocks**
```java
synchronized (lock) {
    // Região crítica protegida
}
```

### **wait() / notify()**
```java
// Thread aguarda condição
while (list.isEmpty()) {
    lock.wait();
}

// Thread notifica mudança de estado  
lock.notify();
```

### **volatile keyword**
```java
private volatile boolean stopRequested = false;
```

## 🚀 Como Executar

### **Pré-requisitos**
- Java 8 ou superior
- IDE Java (IntelliJ, Eclipse, VS Code) ou terminal

### **Compilação e Execução**

#### **Via Terminal:**
```bash
# Navegar para o diretório do projeto
cd thread-java

# Compilar
javac -d . src/main/java/com/example/*.java

# Executar
java com.example.ThreadDemo
```

#### **Via IDE:**
1. Abrir o projeto na IDE
2. Executar a classe `ThreadDemo.java`

## 📊 Saída Esperada

```
=== DEMONSTRAÇÃO DE THREADS COM LISTA COMPARTILHADA ===
T1 (Produtora) irá adicionar elementos
T2 (Consumidora) irá consumir elementos
Ambas executam em paralelo

Iniciando execução paralela das threads...

T1 (Produtor) iniciada
T2 (Consumidor) iniciada
T2 (Consumidor) aguardando elementos...
T1 (Produtor) adicionou: 1 | Tamanho da lista: 1
T2 (Consumidor) removeu: 1 | Tamanho da lista: 0
T2 (Consumidor) processando valor: 1
...
```

## 🎓 Conceitos Demonstrados

- **Programação Concorrente**: Execução paralela de threads
- **Sincronização**: Proteção de regiões críticas
- **Producer-Consumer Pattern**: Padrão produtor-consumidor
- **Thread Safety**: Operações seguras entre threads
- **Coordenação de Threads**: wait()/notify() para comunicação

## ⚠️ Pontos de Atenção

1. **Deadlock Prevention**: Uso de um único lock para evitar deadlocks
2. **Starvation Avoidance**: notify() garante que threads esperando sejam notificadas
3. **Interruption Handling**: Tratamento adequado de `InterruptedException`
4. **Volatile Variables**: Uso de `volatile` para flags de controle

## 🔍 Análise de Concorrência

### **Problemas Evitados:**
- **Race Conditions**: Sincronização previne condições de corrida
- **Data Corruption**: Acesso mutuamente exclusivo aos dados
- **Lost Updates**: Operações atômicas protegem atualizações
- **Inconsistent Reads**: Leituras sincronizadas garantem consistência

### **Performance:**
- Threads executam em paralelo quando possível
- Bloqueio mínimo necessário para segurança
- Coordenação eficiente com wait()/notify()

## 📚 Referências

- [Java Concurrency Tutorial](https://docs.oracle.com/javase/tutorial/essential/concurrency/)
- [Java Memory Model](https://docs.oracle.com/javase/specs/jls/se8/html/jls-17.html)
- [Effective Java - Item 78: Synchronize access to shared mutable data](https://www.oreilly.com/library/view/effective-java-3rd/9780134686097/)

---
