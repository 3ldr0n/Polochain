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

/**
 * This class will be used  to reference TransactionOutputs that have not yet been spent.
 * The transactionOutputId will be used to find the relevant TransactionOutput, allowing
 * miners to check your ownership.
 */
public class TransactionInput
{
    // Reference to TransactionOutputs.transactionId.
    public String transactionOutputId;
    // Contains unspent transaction output.
    public TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId)
    {
        this.transactionOutputId = transactionOutputId;
    }
}
