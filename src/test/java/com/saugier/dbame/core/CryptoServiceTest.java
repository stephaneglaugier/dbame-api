package com.saugier.dbame.core;

import com.saugier.dbame.core.model.base.Roll;
import com.saugier.dbame.core.service.ICryptoService;
import org.slf4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CryptoServiceTest {

    @Autowired
    ICryptoService cryptoService;

    @Autowired
    Logger log;

    @Test
    void isRelativelyPrimeTest() {
        java.math.BigInteger a = new java.math.BigInteger("6");
        java.math.BigInteger b = new java.math.BigInteger("35");
        boolean result = a.gcd(b).equals(java.math.BigInteger.ONE);
        assertTrue(result);
    }

    @Test
    void isNotRelativelyPrimeTest() {
        java.math.BigInteger a = new java.math.BigInteger("6");
        java.math.BigInteger b = new java.math.BigInteger("36");
        boolean result = a.gcd(b).equals(java.math.BigInteger.ONE);
        assertFalse(result);
    }

    @Test
    void randomlySelectTest() throws Exception {
        java.math.BigInteger a = new java.math.BigInteger("100");
        java.math.BigInteger b = cryptoService.randomlySelect(a);
        assertTrue(a.compareTo(java.math.BigInteger.ZERO) == 1);
        assertTrue(b.compareTo(a) == -1);
    }

    @Test
    void randomCoprimeTest() {
        java.math.BigInteger a = new java.math.BigInteger("100");
        java.math.BigInteger b = cryptoService.randomCoprime(a);
        assertTrue(a.gcd(b).equals(java.math.BigInteger.ONE));
        assertTrue(b.compareTo(a) == -1);
    }

    @Test
    void verifySignedTest() throws Exception {
        Roll roll = new Roll(
                new BigInteger("2775", 16),
                null);
        roll = cryptoService.sign(roll);
        assertTrue(cryptoService.validate(roll));
    }
}
