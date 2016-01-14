package es.poc.bid.session.impl;

import es.poc.bid.session.SessionIdGenerator;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by mcalavera81 on 12/01/16.
 */
public class SessionIdGeneratorImpl implements SessionIdGenerator {

    private SecureRandom random = new SecureRandom();

    public  String generatetSessionId(){
        return new BigInteger(130, random).toString(32);
    }


}
