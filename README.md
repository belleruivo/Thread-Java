# Demonstração Simples de Threads Java

Este projeto demonstra o uso básico de threads em Java com uma lista compartilhada, onde uma thread produtora (T1) adiciona elementos e uma thread consumidora (T2) remove e processa esses elementos em paralelo.

## 📋 Descrição do Problema

- **Thread T1 (Producer)**: Popula valores inteiros em uma lista compartilhada
- **Thread T2 (Consumer)**: Consome e imprime esses valores  
- **Execução**: As duas operações ocorrem em paralelo

## 🎯 Objetivos

- Demonstrar programação concorrente básica com threads
- Identificar e proteger regiões críticas
- Implementar sincronização thread-safe simples
- Coordenar produção e consumo de dados

## 🏗️ Estrutura do Projeto

```
Thread-Java/
├── SimpleThreadDemo.java    # Implementação completa em um arquivo
└── README.md               # Este arquivo
```

## 🔒 Regiões Críticas Identificadas

### 1. **Lista Compartilhada `sharedList`**
- **Problema**: Múltiplas threads acessam a mesma lista simultaneamente
- **Solução**: Sincronização com `synchronized(sharedList)`
- **Localização**: Linha 13 em `SimpleThreadDemo.java`

### 2. **Variável `finished`**  
- **Problema**: Controle de estado compartilhado entre threads
- **Solução**: Acesso dentro do bloco `synchronized`
- **Localização**: Linha 14 em `SimpleThreadDemo.java`

### 3. **Bloco Producer (linhas 35-41)**
- **Problema**: Adição de elementos na lista de forma concorrente
- **Solução**: `synchronized(sharedList)` + `notify()`

### 4. **Bloco Consumer (linhas 67-85)**
- **Problema**: Remoção de elementos e verificação de lista vazia
- **Solução**: `synchronized(sharedList)` + `wait()`

## 🔧 Mecanismos de Sincronização Utilizados

### **synchronized blocks**
```java
synchronized (sharedList) {
    // Região crítica protegida
}
```

### **wait() / notify()**
```java
// Consumer aguarda novos itens
sharedList.wait();

// Producer notifica novo item disponível
sharedList.notify();
```

## 🚀 Como Executar

### Pré-requisitos
- Java 8 ou superior instalado

### Compilar e Executar
```bash
# Compilar
javac SimpleThreadDemo.java

# Executar
java SimpleThreadDemo
```

## 📊 Exemplo de Saída

```
=== DEMONSTRAÇÃO SIMPLES DE THREADS ===

Producer iniciado - adicionando valores...
Consumer iniciado - consumindo valores...
Producer adicionou: 1
Consumer consumiu: 1
Producer adicionou: 2
Consumer consumiu: 2
...
Producer finalizado!
Consumer finalizado!

Execução finalizada!
```

## 🧠 Conceitos Demonstrados

- **Threads**: Execução paralela com `implements Runnable`
- **Sincronização**: Proteção de regiões críticas com `synchronized`
- **Coordenação**: Comunicação entre threads com `wait()` e `notify()`
- **Lista Compartilhada**: Recurso acessado por múltiplas threads
- **Producer-Consumer**: Padrão clássico de programação concorrente

## � Observações

- O código inclui comentários detalhados sobre as regiões críticas
- Utiliza `Thread.sleep()` para simular tempo de processamento
- Implementa controle de finalização thread-safe
- Demonstra coordenação entre threads produtora e consumidora


