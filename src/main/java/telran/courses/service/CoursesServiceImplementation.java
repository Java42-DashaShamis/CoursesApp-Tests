package telran.courses.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telran.courses.dto.Course;
import telran.courses.entities.CourseEntity;
import telran.courses.exceptions.BadRequestException;
import telran.courses.exceptions.ResourceNotFoundException;
import telran.courses.repo.CourseRepository;
@Service
public class CoursesServiceImplementation extends AbstractCoursesService {
	
	private static final long serialVersionUID = 1L;
	
	static Logger LOG = LoggerFactory.getLogger(CoursesServiceImpl.class);
	
	@Autowired
	CourseRepository courseRepository;

	@Override
	@Transactional
	public Course addCourse(Course course) {
		course.id = getId();
		CourseEntity courseEntity = CourseEntity.build(course);
		courseRepository.save(courseEntity);
		return course;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Course> getAllCourses() {
		return courseRepository.findAll().stream().map(CourseEntity::getCourseDTO).toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Course getCourse(int id) {
		CourseEntity courseEntity = courseRepository.findById(id).orElse(null);
		if(courseEntity == null) {
			throw new ResourceNotFoundException(String.format("course with id %d is not found", id));
		}
		return courseEntity.getCourseDTO();
	}

	@Override
	@Transactional
	public Course removeCourse(int id) {
		Course res = getOldCourse(id);
		courseRepository.deleteById(id);
		return res;
	}
	/* V.R.
	 *  It is very bad practice to change the names of files, methods
	 *  and so on without reasons. 
	 */
	private Course getOldCourse(int id) {
		if(!exists(id)) {
			throw new ResourceNotFoundException(String.format("course with id %d is not found", id));
		}
		CourseEntity courseEntity = courseRepository.getById(id);
		Course res = courseEntity.getCourseDTO();
		return res;
	}

	@Override
	@Transactional
	public Course updateCourse(int id, Course course) {
		if(!exists(id)) {
			throw new ResourceNotFoundException(String.format("course with id %d is not found", id));
		}
		if(id!=course.id) {
			throw new BadRequestException(String.format("id mesmatching: recieved id - %d, course id - %d", id, course.id));
		}
		CourseEntity courseEntity = courseRepository.getById(id);
		Course res = courseEntity.getCourseDTO();
		courseEntity.fillEntity(course);
		LOG.debug("courseEntity for update {}", courseEntity);
		
		return res;
	}

	@Override
	protected boolean exists(int id) {
		return courseRepository.existsById(id); //spring does
	}

}
