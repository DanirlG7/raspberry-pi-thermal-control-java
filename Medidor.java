
```java
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;

/**
 * PROJETO: Controlador Térmico Inteligente
 * DISCIPLINA: Sistemas Digitais e Microprocessadores
 */
public class Main {

    // --- CONFIGURAÇÕES TÉCNICAS (Sistemas Digitais) ---
    private static final int I2C_BUS = 1;              // Barramento I2C 1 do Raspberry
    private static final int SENSOR_ADDR = 0x76;       // Endereço Hexadecimal do BME280
    private static final int TEMP_REG = 0xF7;          // Registrador de Memória da Temperatura
    private static final int PIN_RELE = 17;            // GPIO 17 (Pino Físico 11)
    private static final double LIMITE_TEMP = 25.0;    // Setpoint de ativação do AC

    public static void main(String[] args) {
        System.out.println("Iniciando Sistema de Controle Térmico...");

        // 1. Inicializa o Contexto do Pi4J (Interface com o processador ARM)
        Context pi4j = Pi4J.newAutoContext();

        // 2. Configura a Saída Digital para o Relé (GPIO 17)
        var releConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("rele-ac")
                .address(PIN_RELE)
                .shutdown(DigitalState.LOW)      // Garante desligamento ao encerrar
                .initial(DigitalState.LOW)       // Começa desligado (0V)
                .build();
        DigitalOutput rele = pi4j.create(releConfig);

        // 3. Configura a Comunicação I2C (Protocolo Serial)
        I2CConfig i2cConfig = I2C.newConfigBuilder(pi4j)
                .bus(I2C_BUS)
                .device(SENSOR_ADDR)
                .build();

        try (I2C sensor = pi4j.create(i2cConfig)) {
            
            while (true) {
                // LEITURA: Acessa o registrador de 8 bits do sensor (Mapeamento de Memória)
                int rawData = sensor.readRegister(TEMP_REG);
                
                // PROCESSAMENTO: Converte o dado binário bruto em Celsius
                // (Nota: Em um BME280 real, usa-se uma fórmula de compensação do datasheet)
                double temperatura = converterBinarioParaCelsius(rawData);

                System.out.printf("Log: [ADDR: 0x76] [REG: 0xF7] | Temp: %.2f°C%n", temperatura);

                // LÓGICA DE CONTROLE (Tomada de Decisão Digital)
                if (temperatura > LIMITE_TEMP) {
                    System.out.println(">>> ALERTA: Temperatura Alta! GPIO 17 -> HIGH (1)");
                    rele.high(); // Envia 3.3V para o Relé (Liga o Ar Condicionado)
                } else {
                    System.out.println(">>> Status: Temperatura OK. GPIO 17 -> LOW (0)");
                    rele.low();  // Envia 0V (Desliga o Ar Condicionado)
                }

                // INTERVALO DE AMOSTRAGEM (Ciclo de Varredura)
                Thread.sleep(2000); 
            }

        } catch (Exception e) {
            System.err.println("Erro Crítico no Barramento I2C: " + e.getMessage());
        } finally {
            pi4j.shutdown(); // Libera os recursos do processador
        }
    }

    /**
     * Simula a conversão binário-para-decimal baseada no datasheet.
     * Demonstra o processamento de bits recebidos do sensor.
     */
    private static double converterBinarioParaCelsius(int data) {
        // Exemplo simplificado de conversão de registradores
        return (double) (data & 0xFF) / 4.0 + 10.0; 
    }
}
