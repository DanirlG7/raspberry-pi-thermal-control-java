




---

# 🌡️ Controlador Térmico Inteligente (Raspberry Pi + Java)

**Projeto Acadêmico**  
**Disciplina:** Sistemas Digitais e Microprocessadores  
**Objetivo:** Automação de ambientes climatizados através de sistemas embarcados e protocolos industriais de comunicação.

---

## 📸 Documentação Visual

Para este projeto, desenvolvemos duas visões técnicas que detalham o funcionamento do sistema, integrando a conexão física (hardware) à lógica de controle (software).

| 🖥️ Visão Geral do Sistema | ⚡ Esquema Elétrico (Pinagem) |
| :---: | :---: |
| ![Sistema](./assets/sistema-termico.png) | ![Circuito](./assets/circuito.png) |
| *Fluxo de dados do Sensor ao Ar-Condicionado via Java.* | *Conexões detalhadas dos pinos GPIO e barramento I2C.* |

---

## ⚙️ Funcionamento Técnico

O sistema foi projetado seguindo a arquitetura clássica de sistemas microprocessados: **Entrada, Processamento e Saída.**

### 1. Entrada e Comunicação (Protocolo I2C)
O sensor **BME280** (destacado em roxo nos diagramas) atua como o transdutor de entrada, convertendo a grandeza física em dados digitais.
*   **Endereço Hexadecimal:** O processador comunica-se com o sensor através do endereço único **0x76** no barramento.
*   **Barramento I2C:** Utiliza as linhas **SDA** (Dados) e **SCL** (Clock) para enviar pacotes binários de temperatura para o processador ARM do Raspberry Pi de forma síncrona.

### 2. Processamento e Lógica (Java + Pi4J)
O software, desenvolvido em **Java**, utiliza a biblioteca **Pi4J v2** para interagir com o kernel do sistema e acessar os registradores de hardware do processador.
*   **Leitura de Memória:** O sistema acessa o registrador **0xF7** para extrair os dados brutos de temperatura armazenados no sensor.
*   **Processamento Digital:** O dado binário é convertido em graus Celsius e comparado com um limite pré-definido (*Set Point* de 25°C).

### 3. Saída e Atuação (GPIO)
*   **Nível Lógico:** Quando a temperatura excede o limite, o Raspberry Pi altera o estado do pino **GPIO 17 (Pino Físico 11)** para **HIGH (3.3V / Nível Lógico 1)**.
*   **Interface de Potência:** Este sinal digital satura a bobina do **Módulo Relé**, que funciona como uma chave eletromecânica, fechando o circuito de carga de alta tensão do **Ar-Condicionado**.

---

## 📌 Pinagem Utilizada (Referência)

Conforme ilustrado no diagrama de circuitos (`assets/circuito.png`):
*   **VCC (3.3V):** Pino 1 (Alimentação do Sensor).
*   **GND:** Pino 6 (Aterramento comum/Terra).
*   **SDA / SCL:** Pinos 3 e 5 (Comunicação de dados e clock I2C).
*   **GPIO 17:** Pino 11 (Controle de saída digital para o Relé).

---

## 💻 Exemplo do Código (Lógica Principal)

```java
// Acesso ao barramento I2C para leitura do registrador 0xF7 (Temperatura)
byte rawData = (byte) sensor.readRegister(0xF7);

// Lógica de decisão digital (Sistemas Digitais)
if (temperatura > 25.0) {
    rele.high(); // Nível Lógico 1 (3.3V) -> Ativa o Relé / Liga AC
    System.out.println("Status: AC Ligado");
} else {
    rele.low();  // Nível Lógico 0 (0V) -> Desativa o Relé / Desliga AC
}
```

---

## 🛠️ Tecnologias Utilizadas
*   **Hardware:** Raspberry Pi 4 (Processador ARM Broadcom BCM2711).
*   **Sensor:** BME280 (Protocolo Digital I2C).
*   **Software:** Java 17 com biblioteca de interfaceamento Pi4J v2.
*   **Atuador:** Módulo Relé de 5V (Isolamento Galvânico).

---

### 💡 Como utilizar este repositório
1.  Realize as conexões físicas seguindo o diagrama localizado em `assets/circuito.png`.
2.  Certifique-se de que o **I2C** está habilitado nas configurações do Raspberry Pi (`raspi-config`).
3.  Compile o código fonte localizado na pasta `/src`.
4.  Execute a aplicação com permissões de administrador (`sudo`) para permitir o acesso aos registradores de I/O do sistema.

---
