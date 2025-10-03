# 🚀 Guia de Execução - Threads Java

Este documento fornece instruções detalhadas para compilar e executar todas as classes do projeto.

## 📁 Estrutura do Projeto

```
thread-java/
├── src/main/java/com/example/
│   ├── SharedList.java                    # Lista compartilhada thread-safe
│   ├── ProducerThread.java               # Thread produtora (T1)
│   ├── ConsumerThread.java               # Thread consumidora (T2)
│   ├── ThreadDemo.java                   # Demonstração principal
│   ├── ThreadDemoWithBlockingQueue.java  # Versão alternativa
│   └── ConcurrencyTest.java              # Testes de concorrência
├── README.md                             # Documentação principal
├── EXECUTION_GUIDE.md                    # Este arquivo
└── .gitignore                           # Configuração Git
```

## 🔧 Pré-requisitos

- **Java 8+** instalado
- **Git** (para clonagem do repositório)
- Terminal/Prompt de comando

## 📥 Clonagem do Repositório

```bash
# Clone o repositório (substitua pela URL real do seu GitHub)
git clone https://github.com/SEU_USUARIO/thread-java.git

# Entre no diretório
cd thread-java
```

## ⚙️ Compilação

### Compilar todas as classes:
```bash
javac -d . src/main/java/com/example/*.java
```

### Verificar compilação:
```bash
# No Windows
dir com\example\*.class

# No Linux/Mac
ls com/example/*.class
```

## ▶️ Execução

### 1. **Demonstração Principal**
Executa o exemplo básico com T1 (produtora) e T2 (consumidora):

```bash
java com.example.ThreadDemo
```

**Saída esperada:**
```
=== DEMONSTRAÇÃO DE THREADS COM LISTA COMPARTILHADA ===
T1 (Produtora) irá adicionar elementos
T2 (Consumidora) irá consumir elementos
Ambas executam em paralelo

T1 (Produtor) adicionou: 1 | Tamanho da lista: 1
T2 (Consumidor) removeu: 1 | Tamanho da lista: 0
...
```

### 2. **Versão com BlockingQueue**
Demonstra implementação alternativa usando java.util.concurrent:

```bash
java com.example.ThreadDemoWithBlockingQueue
```

**Saída esperada:**
```
=== VERSÃO ALTERNATIVA COM BLOCKINGQUEUE ===
Demonstração usando java.util.concurrent.BlockingQueue

T1 (Produtor) adicionou: 1 | Tamanho da fila: 1
T2 (Consumidor) removeu: 1 | Tamanho da fila: 0
...
```

### 3. **Testes de Concorrência**
Executa bateria de testes para validar thread safety:

```bash
java com.example.ConcurrencyTest
```

**Saída esperada:**
```
=== TESTE DE CONCORRÊNCIA ===
Testando comportamento com múltiplas execuções

--- TESTE 1 ---
...
=== TESTE DE STRESS ===
Produtores: 2 | Consumidores: 2 | Total elementos: 40
...
✓ SUCESSO: Todos os elementos foram produzidos e consumidos corretamente
```

## 🔍 Análise dos Resultados

### **Comportamento Esperado:**

1. **Sincronização Correta:**
   - Threads executam em paralelo
   - Não há condições de corrida
   - Lista sempre termina vazia

2. **Coordenação Adequada:**
   - Consumidor aguarda quando lista está vazia
   - Produtor notifica disponibilidade de elementos
   - Sincronização com wait()/notify()

3. **Thread Safety:**
   - Operações atômicas na lista compartilhada
   - Proteção de regiões críticas
   - Estado consistente em todas as operações

### **Sinais de Problemas:**

❌ **Se a lista não estiver vazia no final**
❌ **Se houver exceções de concorrência**
❌ **Se threads ficarem bloqueadas indefinidamente**

## 🐛 Troubleshooting

### **Problema: Classes não encontradas**
```bash
# Solução: Verificar CLASSPATH
java -cp . com.example.ThreadDemo
```

### **Problema: Versão Java incompatível**
```bash
# Verificar versão
java -version

# Deve ser Java 8 ou superior
```

### **Problema: Compilação falhando**
```bash
# Limpar e recompilar
rm -rf com/
javac -d . src/main/java/com/example/*.java
```

## 📊 Executando Múltiplas Vezes

Para detectar problemas intermitentes de concorrência:

### **Windows (PowerShell):**
```powershell
for ($i=1; $i -le 5; $i++) {
    Write-Host "=== Execução $i ==="
    java com.example.ThreadDemo
    Start-Sleep -Seconds 2
}
```

### **Linux/Mac (Bash):**
```bash
for i in {1..5}; do
    echo "=== Execução $i ==="
    java com.example.ThreadDemo
    sleep 2
done
```

## 📈 Monitoramento de Performance

### **Medindo tempo de execução:**

**Windows:**
```powershell
Measure-Command { java com.example.ThreadDemo }
```

**Linux/Mac:**
```bash
time java com.example.ThreadDemo
```

### **Monitorando uso de CPU:**

**Windows:**
```powershell
# Durante execução, verificar Task Manager
# Ou usar:
Get-Process java
```

**Linux/Mac:**
```bash
# Em terminal separado durante execução:
top -p $(pgrep java)
```

## 🎯 Customização

### **Alterando número de elementos:**

Edite a constante em cada classe:
```java
final int NUMBER_OF_ELEMENTS = 20; // Padrão: 10
```

### **Modificando tempos de sleep:**

```java
Thread.sleep(500);  // Produtor - padrão: 500ms
Thread.sleep(300);  // Consumidor - padrão: 300ms
```

### **Teste com mais threads:**

Modifique `ConcurrencyTest.java`:
```java
final int NUM_PRODUCERS = 3;  // Padrão: 2
final int NUM_CONSUMERS = 3;  // Padrão: 2
```

## 📚 Próximos Passos

1. **Experimente** diferentes configurações de timing
2. **Analise** o comportamento com mais produtores/consumidores
3. **Compare** as duas implementações (manual vs BlockingQueue)
4. **Identifique** os pontos de sincronização em cada versão
5. **Teste** em diferentes sistemas operacionais

---

**Dúvidas?** Consulte os comentários detalhados no código-fonte! 🤓