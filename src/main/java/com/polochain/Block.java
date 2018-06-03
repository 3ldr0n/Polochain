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

import java.util.Date;

public class Block
{

    private String hash;
    public String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;

    public Block(String data, String previousHash)
    {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        // This has to be set after the other values.
        this.hash = calculateHash();
    }

    /**
     * Returns the object's hash.
     *
     * @return The object's hash.
     */
    public String getHash()
    {
        return this.hash;
    }

    /**
     * Calculates hash based on content.
     *
     * @return calculated hash.
     */
    public String calculateHash()
    {
        String calculatedHash = StringUtil.applySha256(
                previousHash +
                Long.toString(timeStamp) +
                Integer.toString(nonce) +
                data
        );
        return calculatedHash;
    }

    /**
     * 
     *
     * @param difficulty Represents the difficulty to mine a block.
     */
    public void mineBlock(int difficulty)
    {
        // Creates a string with diffuculty to mine equals to difficulty * "0".
        String target = new String(new char[difficulty]).replace('\0', '0');
        System.out.println("Target: " + target);
        while (!hash.substring(0, difficulty).equals(target))
        {
            nonce++;
            hash = calculateHash();
            System.out.println("Hash: " + hash);
            System.out.println("Substring: " + hash.substring(0, difficulty));
        }

        System.out.println("Block mined! : " + hash);
    }
}
