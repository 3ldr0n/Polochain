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
import java.util.Base64;

public class StringUtil
{

    /**
     * Applies sha256 to a string and returns the result.
     *
     * @param input
     * @return
     */
    public static String applySha256(String input)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Applies sha256 to our input.
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0;i < hash.length;i++)
            {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Applies ECDSA Signature and returns the result.
     *
     * @param privateKey
     * @param input
     * @return
     */
    public static byte[] applyECDSASignature(PrivateKey privateKey, String input)
    {
        Signature dsa;
        byte[] output = new byte[0];

        try
        {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSignature = dsa.sign();
            output = realSignature;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return output;
    }

    /**
     * Verifies a signature.
     *
     * @param publicKey
     * @param data
     * @param Signature
     * @return
     */
    public static Boolean verifyECDSASignature(PublicKey publicKey, String data, byte[] signature)
    {
        try
        {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     *
     * @param ley
     * @return
     */
    public static String getStringFromKey(Key key)
    {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
