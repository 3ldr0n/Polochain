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

import java.security.PublicKey;

public class TransactionOutput
{
    public String id;
    // Aka the new owner of the coins.
    public PublicKey reciepient;
    // Amount of coins they own.
    public double value;
    // The id of the transaction this output was created in.
    public String parentTransactionId;

    public TransactionOutput(PublicKey reciepient, double value, String parentTransactionId)
    {
        this.reciepient = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient) 
                + Double.toString(value) + parentTransactionId);
    }

    // Check if the coin belongs to the user.
    public boolean isMine(PublicKey publicKey)
    {
        return (publicKey == reciepient);
    }

}
