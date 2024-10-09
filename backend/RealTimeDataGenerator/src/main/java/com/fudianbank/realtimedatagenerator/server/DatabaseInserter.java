package com.fudianbank.realtimedatagenerator.server;

/**
 * @author Charles
 * @Date 2024-09-30 14:32
 * Belongs to Fudian Bank
 */
public interface DatabaseInserter extends Runnable{
    void stopInsertion();
}
