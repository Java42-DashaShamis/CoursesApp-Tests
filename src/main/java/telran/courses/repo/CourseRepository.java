package telran.courses.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.courses.entities.CourseEntity;

public interface CourseRepository extends JpaRepository<CourseEntity, Integer> { 
	
}
