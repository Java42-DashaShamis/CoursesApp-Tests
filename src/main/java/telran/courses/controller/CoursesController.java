package telran.courses.controller;

import java.util.List;

import javax.validation.*;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import telran.courses.dto.Course;
import telran.courses.exceptions.BadRequestException;
import telran.courses.service.CoursesService;
@RestController
@RequestMapping("/courses")
@Validated
@CrossOrigin
public class CoursesController {
	static Logger LOG = LoggerFactory.getLogger(CoursesController.class);
	@Autowired
	private CoursesService coursesService;

	@PostMapping
	Course addCourse(@RequestBody @Valid Course course) {
		Course courseAdded = coursesService.addCourse(course);
		LOG.debug("added course with id {}", courseAdded.id);
		return courseAdded;
	}

	@GetMapping
	List<Course> getCourses() {
		List<Course> courses = coursesService.getAllCourses();
		LOG.trace("getting {} courses", courses.size());
		return courses;
	}
	
	@GetMapping("/{id}")
	Course getCourse(@PathVariable(name = "id") int id) {
		Course course = coursesService.getCourse(id);
		LOG.debug("course with id {} returned to client", course.id);
		return course;
	}
	
	@DeleteMapping("/{id}")
	Course removeCourse(@PathVariable(name = "id") int id) {
		Course course = coursesService.removeCourse(id);
		LOG.debug("course with id {} has been removed", id);
		return course;
	}
	
	@PutMapping("/{id}")
	Course updateCourse(@PathVariable(name = "id") int id, @RequestBody @Valid Course course) {
		if (course.id != id) {
			throw new BadRequestException(String.format("new course has id: %d but updated course should have id %d",
					course.id, id));
		}
		LOG.debug("new course for update {}", course);
		Course courseUpdated = coursesService.updateCourse(id, course);
		LOG.debug("course with id {} has been updated ", course.id);
		return courseUpdated;
	}
}
