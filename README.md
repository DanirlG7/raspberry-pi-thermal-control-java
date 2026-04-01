


# 🌡️ Controlador Térmico Inteligente (Raspberry Pi + Java)

**Projeto Acadêmico**  
**Disciplina:** Sistemas Digitais e Microprocessadores  
**Objetivo:** Automação de ambientes climatizados através de sistemas embarcados e protocolos industriais.

---

## 📸 Documentação Visual

Para este projeto, desenvolvemos duas visões técnicas que detalham o funcionamento do sistema, desde a conexão física até a lógica de software.

| 🖥️ Visão Geral do Sistema | ⚡ Esquema Elétrico (Pinagem) |
| :---: | :---: |
| ![Sistema](./assets/sistema-termico.png) | ![Circuito](./assets/circuito.png) |
| *Fluxo de dados do Sensor ao Ar-Condicionado via Java.* | *Conexões detalhadas dos pinos GPIO e barramento I2C.* |

---

## ⚙️ Funcionamento Técnico

O sistema foi projetado seguindo a arquitetura clássica de sistemas microprocessados: **Entrada, Processamento e Saída.**

### 1. Entrada e Comunicação (Protocolo I2C)
O sensor **BME280** (destacado em roxo) atua como o transdutor de entrada. 
*   **Endereço Hexadecimal:** O processador comunica-se com o sensor através do endereço `0x76`.
*   **Barramento I2C:** Utiliza as linhas **SDA** (Dados) e **SCL** (Clock) para enviar pacotes binários de temperatura para o processador ARM do Raspberry Pi.

### 2. Processamento e Lógica (Java + Pi4J)
O software, desenvolvido em **Java**, utiliza a biblioteca **Pi4J** para interagir com o kernel do sistema e acessar os registradores de hardware.
*   **Leitura de Memória:** O sistema acessa o registrador `0xF7` para extrair os dados brutos de temperatura.
*   **Processamento Digital:** O dado binário é convertido em graus Celsius e comparado com um limite (ex: 25°C).

### 3. Saída e Atuação (GPIO)
*   **Nível Lógico:** Quando a temperatura excede o limite, o Raspberry Pi altera o estado do pino **GPIO 17 (Pino Físico 11)** para **HIGH (3.3V)**.
*   **Interface de Potência:** Este sinal satura a bobina do **Módulo Relé**, que por sua vez fecha o circuito de carga do **Ar-Condicionado**, iniciando a climatização.

---

## 📌 Pinagem Utilizada (Referência)

Conforme ilustrado no diagrama de circuitos:
*   **VCC (3.3V):** Pino 1 (Alimentação do Sensor).
*   **GND:** Pino 6 (Aterramento comum).
*   **SDA/SCL:** Pinos 3 e 5 (Comunicação de dados I2C).
*   **GPIO 17:** Pino 11 (Controle de saída para o Relé).

---

## 💻 Exemplo do Código (Trecho Principal)

```java
// Acesso ao barramento I2C para leitura do registrador 0xF7
byte rawData = (byte) sensor.readRegister(0xF7);

// Lógica de decisão digital
if (temperatura > 25.0) {
    rele.high(); // Nível Lógico 1 -> Liga o AC
} else {
    rele.low();  // Nível Lógico 0 -> Desliga o AC
}
```

---

## 🛠️ Tecnologias Utilizadas
*   **Hardware:** Raspberry Pi 4 (Processador Broadcom BCM2711, Quad-core Cortex-A72).
*   **Sensor:** BME280 (Pressão, Umidade e Temperatura).
*   **Software:** Java 17 com biblioteca Pi4J v2.
*   **Atuador:** Módulo Relé de 5V/10A.

---

### 💡 Como utilizar este repositório
1.  Consulte o diagrama em `/assets/circuito.png` para realizar as conexões físicas.
2.  Clone o repositório no seu Raspberry Pi.
3.  Compile o código fonte localizado em `/src`.
4.  Execute com permissões de administrador para acesso aos registradores de I/O.

---

