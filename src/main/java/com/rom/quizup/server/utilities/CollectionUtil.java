package com.rom.quizup.server.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility to handle collections
 * 
 * @author rom
 *
 */
public class CollectionUtil {
	/**
	 * Prevent construction of this class
	 */
	private CollectionUtil(){}
	
	/**
	 * A generic create and return a @see {@link List} from an @see {@link Iterable}.
	 *  
	 * @param iter
	 * @return
	 */
	public static <E> List<E> makeList(Iterable<E> iter) {
    List<E> list = new ArrayList<E>();
    for (E item : iter) {
        list.add(item);
    }
    return list;
	}
}