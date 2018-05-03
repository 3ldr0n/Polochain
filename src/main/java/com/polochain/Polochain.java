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

import java.security.Security;
import java.util.HashMap;
import java.util.ArrayList;

public class Polochain
{

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
    public static int difficulty = 5;
    public static Wallet firstWallet;
    public static Wallet secondWallet;
    public static double minimumTransaction;

    public static void main( String[] args )
    {
        // Setup Bouncy Castle as a Security Provider.
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        // Create the wallets.
        firstWallet = new Wallet();
        secondWallet = new Wallet();

        // Test public/private keys.
        System.out.println("Private and public keys: ");
        System.out.println(StringUtil.getStringFromKey(firstWallet.getPrivateKey()));
        System.out.println(StringUtil.getStringFromKey(firstWallet.getPublicKey()));

        // Test transaction
        Transaction transaction = new Transaction(firstWallet.getPublicKey(), secondWallet.getPublicKey(), 5, null);
        transaction.generateSignature(firstWallet.getPrivateKey());

        // Verify if the signature works and verify it from public key.
        System.out.println("Is signature verified: ");
        System.out.println(transaction.verifySignature());
    }

    public static boolean isChainValid()
    {
        Block currentBlock;
        Block previousBlock;

        // Loop through blockchain to check the hashes.
        for (int i = 1;i < blockchain.size(); i++)
        {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            // Compare registered hash and calculated hash
            if (!currentBlock.getHash().equals(currentBlock.calculateHash()))
            {
                System.out.println("Current hashes are different");
                return false;
            }

            // Compare previous hash with registered previous hash.
            if (!previousBlock.getHash().equals(previousBlock.calculateHash()))
            {
                System.out.println("Previous hashes are different");
                return false;
            }
        }

        return true;

    }
}
