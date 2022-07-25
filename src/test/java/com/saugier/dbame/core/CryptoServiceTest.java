package com.saugier.dbame.core;

import com.saugier.dbame.core.model.entity.Roll;
import com.saugier.dbame.core.service.ICryptoService;
import com.sun.org.slf4j.internal.Logger;
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
        BigInteger a = new BigInteger("6");
        BigInteger b = new BigInteger("35");
        boolean result = a.gcd(b).equals(BigInteger.ONE);
        assertTrue(result);
    }

    @Test
    void isNotRelativelyPrimeTest() {
        BigInteger a = new BigInteger("6");
        BigInteger b = new BigInteger("36");
        boolean result = a.gcd(b).equals(BigInteger.ONE);
        assertFalse(result);
    }

    @Test
    void randomlySelectTest() throws Exception {
        BigInteger a = new BigInteger("100");
        BigInteger b = cryptoService.randomlySelect(a);
        log.warn(b.toString());
        assertTrue(a.compareTo(BigInteger.ZERO) == 1);
        assertTrue(b.compareTo(a) == -1);
    }

    @Test
    void randomCoprimeTest() {
        BigInteger a = new BigInteger("100");
        BigInteger b = cryptoService.randomCoprime(a);
        log.warn(b.toString());
        assertTrue(a.gcd(b).equals(BigInteger.ONE));
        assertTrue(b.compareTo(a) == -1);
    }

    @Test
    void verifySignedTest() throws Exception {
        Roll roll = new Roll();
        roll.setY("10f1");
        roll = cryptoService.sign(roll);
        assertTrue(cryptoService.validate(roll));
    }


}
