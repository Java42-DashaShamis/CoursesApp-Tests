package telran.courses.service;
import static telran.courses.api.ApiConstants.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import telran.courses.dto.Course;
import telran.courses.exceptions.ResourceNotFoundException;


public class CoursesServiceImpl implements CoursesService{
	
	private static final long serialVersionUID = 1L;

	private static final int MILLIS_IN_MINUTES = 5000;
	
	private boolean flUpdate = false; 

	@Value("${app.file.name}")
	private transient String fileName; 
	
	@Value("${app.interval.minutes: 1}")
	private transient int interval;
	
	//private transient SavingServiceThread savingThread;
	
	static Logger LOG = LoggerFactory.getLogger(CoursesService.class);
		
	private Map<Integer, Course> courses = new HashMap<>();
	
	@Override
	public  Course addCourse(Course course) {
	    course.id = generateId();
	    Course res = add(course);
	    flUpdate = true;
	    return res;
	}

	private Course add(Course course) {
		courses.put(course.id, course);
		return course;
	}

	private Integer generateId() {
	    ThreadLocalRandom random = ThreadLocalRandom.current();
	    int randomId;

	    do {
	        randomId = random.nextInt(MIN_ID, MAX_ID);
	    } while (exists(randomId));
	    return randomId;
	}

	private boolean exists(int id) {
		return courses.containsKey(id);
	}

	@Override
	public List<Course> getAllCourses() {
	    return new ArrayList<>(courses.values());
	}

	@Override
	public Course getCourse(int id) {
		Course course = courses.get(id);
		if (course == null) {
			throw new ResourceNotFoundException(String.format("course with id %d not found", id));
		}
		return course;
	}


	@Override
	public Course removeCourse(int id) {
		Course course = courses.remove(id);
		if(course == null) {
			throw new ResourceNotFoundException(String.format("course with id %d not found", id));
		}
		flUpdate = true;
		return course;
	}

	@Override
	public Course updateCourse(int id, Course course) {
		Course courseUpdated = courses.replace(id,course);
		if(courseUpdated == null) {
			throw new ResourceNotFoundException(String.format("course with id %d not found", id));
		}
		flUpdate = true;
		return courseUpdated;
	}
	
	
	
	private void restore() {
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName))) {
				Map<Integer,Course> coursesRestored =  (Map<Integer,Course>) input.readObject();
				this.courses = coursesRestored;
				LOG.debug("service has been restored from file {}", fileName);
			} catch (FileNotFoundException e) {
				LOG.warn("service has not been restored - no file {} found", fileName);;
			} catch (Exception e) {
				LOG.warn("service has not been restored by nested exception {} ", e.toString());
			} 
		
	}
	
	private boolean save() {
		if(flUpdate) {
			try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName))) {
				output.writeObject(courses);
				flUpdate=false;
				return true;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		
		return false;
	}
	
	@PostConstruct
	void restoreInvocation() {
		LOG.debug("interval: {}", interval);
		restore();
		Thread thread = new Thread(() -> {
			LOG.debug("Thread {} created ", Thread.currentThread().getName());
			boolean isSaved = false;
			while(true) {
				try {
					Thread.sleep(interval*MILLIS_IN_MINUTES);
					isSaved = save();
					LOG.debug("Saving");
				} catch (InterruptedException e) {
					e.printStackTrace();
					LOG.debug("Saving is interrapted");
				}
				if (isSaved) {
					LOG.debug("courses data saved into file {}", fileName);
				} else {
					LOG.debug("courses have not been updated or saved");
				}
			}
			
		});
		thread.setDaemon(true);
		thread.start();
	}
	
	@PreDestroy
	void saveInvocation() {
		boolean isSaved = save();
		if (isSaved) {
			LOG.debug("courses data saved into file {}", fileName);
		} else {
			LOG.debug("courses have not been updated or saved");
		}
		
	}
	

}
