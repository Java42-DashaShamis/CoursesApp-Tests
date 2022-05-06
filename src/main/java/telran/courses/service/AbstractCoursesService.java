package telran.courses.service;
import static telran.courses.api.ApiConstants.*;

import java.util.concurrent.ThreadLocalRandom;

public abstract class AbstractCoursesService implements CoursesService {

	private static final long serialVersionUID = 1L;

	protected int getId() {
		int id = 0;
		var threadLocal = ThreadLocalRandom.current();
		do {
			id = threadLocal.nextInt(MIN_ID,MAX_ID);
		}while(exists(id));
		return id;
	}
	
	abstract protected boolean exists(int id); 

}
