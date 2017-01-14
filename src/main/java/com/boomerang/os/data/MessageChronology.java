package com.boomerang.os.data;

import java.util.Comparator;

public class MessageChronology implements Comparator<Message> {
	
	@Override
	public int compare(Message a, Message b) {
		return Long.compare(a.getTimestamp(), b.getTimestamp());
	}

}
