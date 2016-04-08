package com.rom.quizup.server.utilities;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {
	
	private CollectionUtil(){}
	
	public static <E> List<E> makeList(Iterable<E> iter) {
    List<E> list = new ArrayList<E>();
    for (E item : iter) {
        list.add(item);
    }
    return list;
	}
}
