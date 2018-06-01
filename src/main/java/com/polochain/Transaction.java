/*
  This software is just a simple clone of the blockchain.
  Copyright (C) 2018 Edison Neto
  
  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.
  
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

package com.polochain;

import java.security.*;
import java.util.ArrayList;

public class Transaction
{
    // Hash of the transaction.
    public String transactionId;
    // Sender's adress(public key).
    public PublicKey sender;
    // Reciepient's adress(public key).
    public PublicKey reciepient;
    public double value;
    // Prevents anybody else from spending funds in other user's wallet.
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    // A rough count of how many transactions have been created.
    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, double value, ArrayList<TransactionInput> inputs)
    {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    } 

    /**
     *
     * @return 
     */
    private String calculateHash()
    {
        // Increase the sequence to avoid identical transactions.
        sequence++;
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                StringUtil.getStringFromKey(reciepient) +
                Double.toString(value) +
                sequence
        );
    }

    /**
     * Signs all the data we don't wish to be tampered with.
     *
     * @param privateKey
     */
    public void generateSignature(PrivateKey privateKey)
    {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) 
            + Double.toString(value);

        signature = StringUtil.applyECDSASignature(privateKey, data);
    }

    /**
     * Verifies the data the user signed hasn't been tampered with.
     *
     * @return
     */
    public Boolean verifySignature()
    {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient)
            + Double.toString(value);

        return StringUtil.verifyECDSASignature(sender, data, signature);
    }

    /**
     * Returns true if the new transaction was created.
     *
     * @return
     */
    public boolean processTransaction()
    {
        if (verifySignature() == false)
        {
            System.out.println("Transaction signature failed to verify!");
            return false;
        }

        // Gather transaction inputs, make sure they are unspent.
        for (TransactionInput input : inputs)
        {
            System.out.println("Input: " + input);
            input.UTXO = Polochain.UTXOs.get(input.transactionOutputId);
        }

        if (getInputsValue() < Polochain.minimumTransaction)
        {
            System.out.println("Transaction inputs to small: " + getInputsValue());
            return false;
        }

        // Generates transaction outputs.
        // Get value of inputs then the left over change.
        double leftOver = getInputsValue() - value;
        transactionId = calculateHash();
        // Send value to reciepient.
        outputs.add(new TransactionOutput(this.reciepient, value, transactionId));
        // Send the left over change back to sender.
        outputs.add(new TransactionOutput(this.sender, value, transactionId));

        // Add outputs to unspent list.
        for (TransactionOutput output : outputs)
        {
            Polochain.UTXOs.put(output.id, output);
        }

        for (TransactionInput input : inputs)
        {
            if (input.UTXO == null)
            {
                continue;
            }
            Polochain.UTXOs.remove(input.UTXO.id);
        }

        return true;
    }

    /**
     * Returns the sum of all inputs(UTXOs) values.
     *
     * @return
     */
    public double getInputsValue()
    {
        double total = 0;
        for (TransactionInput input : inputs)
        {
            // If transaction is nor found, just skip it.
            if (input.UTXO == null) 
            {
                continue;
            }
            total += input.UTXO.value;
        }
        return total;
    }

    // return the sum of all outputs.
    public double getOutputsValue()
    {
        double total = 0;
        for (TransactionOutput output : outputs)
        {
            total += output.value;
        }
        return total;
    }

}
