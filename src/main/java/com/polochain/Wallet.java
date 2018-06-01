/**
 * This software is just a simple clone of the blockchain.
 * Copyright (C) 2018 Edison Neto
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package com.polochain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet
{
    private PrivateKey privateKey;
    private PublicKey publicKey;
    // Only UTXOs owned by this wallet.
    public HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

    public Wallet()
    {
        generateKeyPair();
    }

    /**
     *
     *
     * @return
     */
    public PrivateKey getPrivateKey()
    {
        return this.privateKey;
    }

    /**
     *
     *
     * @return
     */
    public PublicKey getPublicKey()
    {
        return this.publicKey;
    }

    /**
     *
     *
     * @return
     * @throw
     */
    public void generateKeyPair()
    {
        try
        {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec esSpec = new ECGenParameterSpec("prime192v1");

            // Initialize the key generator and generate a KeyPair
            keyGen.initialize(esSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            // Set the public and private keys from KeyPair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     *
     * @return
     */
    public double getBalance()
    {
        double total = 0;
        for (Map.Entry<String, TransactionOutput> item : Polochain.UTXOs.entrySet())
        {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(publicKey))
            {
                UTXOs.put(UTXO.id, UTXO);
                total += UTXO.value;
            }
        }
        return total;
    }

    /**
     *
     *
     * @param _reciepient
     * @param value
     * @return
     */
    public Transaction sendFunds(PublicKey _reciepient, double value)
    {
        // Gather balance and funds.
        if (getBalance() < value)
        {
            System.out.println("Not enough funds for transaction.");
            return null;
        }

        ArrayList<TransactionInput> inputs =  new ArrayList<TransactionInput>();

        double total = 0;
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet())
        {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if (total > value)
            {
                break;
            }
        }

        Transaction newTransaction = new Transaction(publicKey, _reciepient, value, inputs);
        newTransaction.generateSignature(privateKey);

        for (TransactionInput input : inputs)
        {
            UTXOs.remove(input.transactionOutputId);
        }

        return newTransaction;

    }

}
