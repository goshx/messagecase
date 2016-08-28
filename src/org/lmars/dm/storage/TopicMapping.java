package org.lmars.dm.storage;

import java.util.HashSet;
import java.util.Set;

public class TopicMapping {
	public String topic;
	public String table;
	public String pkey;
	public Set<TopicField> fieldMapping = new HashSet<TopicField>();
	public boolean indexflag = false;
}
