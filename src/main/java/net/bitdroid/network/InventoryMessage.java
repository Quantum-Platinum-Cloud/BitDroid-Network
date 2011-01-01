/**
 * Copyright 2011 Christian Decker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file is part the BitDroidNetwork Project.
 */

package net.bitdroid.network;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.bitdroid.network.wire.LittleEndianInputStream;
import net.bitdroid.network.wire.LittleEndianOutputStream;

public class InventoryMessage extends Message {
	public static final int ERROR = 0, MSG_TX = 1, MSG_BLOCK = 2;
	
	private List<InventoryItem> items = new LinkedList<InventoryMessage.InventoryItem>();


	public InventoryMessage(BitcoinClientSocket clientSocket) {
		super(clientSocket);
	}

	@Override
	void read(LittleEndianInputStream in) throws IOException {
		long count = in.readVariableSize();
		for(int i=0; i<count; i++){
			byte[] buffer = new byte[32];
			int t = in.readInt();
			in.read(buffer);
			items.add(new InventoryItem(t, buffer));
		}
	}

	@Override
	void toWire(LittleEndianOutputStream leos) throws IOException {
	}
	
	public class InventoryItem {
		private byte[] hash;
		/**
		 * @return the hash
		 */
		public final byte[] getHash() {
			return hash;
		}
		/**
		 * @return the type
		 */
		public final int getType() {
			return type;
		}
		private int type;
		public InventoryItem(int type, byte hash[]){
			this.hash = hash;
			this.type = type;
		}
	}
	@Override
	public String getCommand() {
		return "inv";
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<InventoryItem> items) {
		this.items = items;
	}

	/**
	 * @return the items
	 */
	public List<InventoryItem> getItems() {
		return items;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("InventoryMessage[count=");
		
		sb.append(items.size()).append(",");
		for(InventoryItem i : items)
			sb.append(i.toString()).append(" ");
		sb.append("]");
		return super.toString();
	}
}