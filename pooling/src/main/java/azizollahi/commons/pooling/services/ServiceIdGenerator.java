package azizollahi.commons.pooling.services;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceIdGenerator {
	private static long id = 0;
	private static Lock lock = new ReentrantLock();

	public static String generateNewId() {
		var newId = "";
		try {
			lock.lock();
			if (id == 999999999)
				id = 0;
			newId = Long.toHexString(id++);
		} finally {
			lock.unlock();
		}
		return newId;
	}
}
