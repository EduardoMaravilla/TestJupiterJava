package org.edumaravil.junit5app.ejemplos.models;

import org.edumaravil.junit5app.ejemplos.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Repeatable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {
    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest() {
        this.cuenta = new Cuenta("Andres", new BigDecimal("1000.12345"));
        System.out.println("Iniciando el método");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Finaliza el método de prueba");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando la clase test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando la clase test");
    }

    @Nested
    @DisplayName("Probando Atributos de la cuenta")
    class CuentaTestNombreSaldo {
        @Test
        @DisplayName("el nombre")
        void testNombreCuenta() {
            //cuenta.setPersona("Andres");
            String esperado = "Andres";
            String real = cuenta.getPersona();
            assertNotNull(real, () -> "la cuenta no puede ser nula");
            assertEquals(esperado, real, () -> "El nombre de la cuenta no es lo que se esperaba");
            assertEquals("Andres", real, () -> "El nombre debe ser igual al real");
        }

        @Test
        @DisplayName("Probando Saldo de la cuenta")
        void testSaldoCuenta() {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }


        @Test
        @DisplayName("Probando Relaciones de las cuentas")
        void testRelacionBancoCuentas() {
            Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

            Banco banco = new Banco();
            banco.addCuenta(cuenta1);
            banco.addCuenta(cuenta2);
            banco.setNombre("Banco del Estado");
            banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));
            assertAll(
                    () -> {
                        assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
                    },
                    () -> {
                        assertEquals("3000", cuenta1.getSaldo().toPlainString());
                    },
                    () -> {
                        assertEquals(2, banco.getCuentas().size());
                    },
                    () -> {
                        assertEquals("Banco del Estado", cuenta1.getBanco().getNombre());
                    },
                    () -> {
                        assertEquals("Andres", banco.getCuentas().stream().filter(c -> c.getPersona().equals("Andres"))
                                .findFirst().get().getPersona());
                    },
                    () -> {
                        assertTrue(banco.getCuentas().stream().anyMatch(c -> c.getPersona().equals("Andres")));
                    }
            );

        }

    }

    @Nested
    class CuentaOperacionesTest {
        @Test
        @DisplayName("Probando Referencias")
        void testReferenciaCuanta() {
            Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("8900.9997"));
            Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));
            //assertNotEquals(cuenta, cuenta2);
            assertEquals(cuenta1, cuenta2);
        }

        @Test
        @DisplayName("Probando debito a la cuenta")
        void testDebitoCuenta() {
            cuenta.debito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900.12345", cuenta.getSaldo().toPlainString());
        }

        @Test
        @DisplayName("Probando crédito a la cuenta")
        void testCreditoCuenta() {
            cuenta.credito(new BigDecimal(100));
            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue());
            assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
        }

        @Test
        @DisplayName("Probando exception de dinero Insuficiente")
        void testDineroInsuficienteException() {
            Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
                cuenta.debito(new BigDecimal("1500.3"));
            });
            String actual = exception.getMessage();
            String esperado = "Dinero Insuficiente";
            assertEquals(esperado, actual);
        }

        @Test
        @DisplayName("Probando transferencia de cuenta a cuenta")
        void testTransferirDineroCuentas() {
            Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));
            Banco banco = new Banco();
            banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));
            assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
            assertEquals("3000", cuenta1.getSaldo().toPlainString());
        }
    }


    @Nested
    class SistemaOperativotest {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
        }

        @Test
        @EnabledOnOs({OS.MAC, OS.LINUX})
        void testSoloLinuxMax() {
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
        }
    }

    @Nested
    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void SoloJDK8() {
        }

        @Test
        @EnabledOnJre(JRE.JAVA_20)
        void soloJdk20() {
        }

        @Test
        @DisabledOnJre(JRE.JAVA_20)
        void testNoJdk20() {
        }
    }

    @Nested
    class SystemPropertiesTest {
        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k, v) -> System.out.println(k + " --- " + v));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*20.*")
        void testJavaVersion() {
        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testSolo64() {
        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testNo64() {
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "eduar")
        void testUsername() {
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {
        }

    }

    @Nested
    class VarieblesAmbienteTest {

        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v) -> System.out.println(k + "----" + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-20.*")
        void testJavaHome() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "16")
        void testProcesadores() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProdDisable() {
        }

    }

    @Test
    @DisplayName("Probando Saldo de la cuenta con assumptions")
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumeTrue(esDev);
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Probando2 Saldo de la cuenta con assumptions")
    void testSaldoCuentaDev2() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        assumingThat(esDev, () -> {
            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        });
    }

    @RepeatedTest(value = 5,name = "{displayName} -- Repetición numero {currentRepetition} de {totalRepetitions}")
    @DisplayName("Repetir debito a la cuenta")
    void testDebitoCuentaRepetir(RepetitionInfo info) {
        if(info.getCurrentRepetition()==3 ){
            System.out.println("Estamos en la repeticion "+ info.getCurrentRepetition());
            int n=0;

        }
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @ParameterizedTest(name = "numero {index} ejecutando con valor {0}-{argumentsWithNames}")
    @ValueSource(strings = {"100","200","300","500","1000"})
    @DisplayName("Probando debito a la cuenta parametrized")
    void testDebitoCuentaParametrized(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0);

    }


}