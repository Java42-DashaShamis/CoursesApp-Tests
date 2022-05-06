package telran.courses.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.*;

public class Course implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final long MIN_HOURS = 80;
	private static final long MAX_HOURS = 500;
	private static final long MIN_COST = 5000;
	private static final long MAX_COST = 20000;
	public Integer id;
	@NotEmpty
	public String course;
	@NotEmpty
	public String lecturer;
	@Min(MIN_HOURS) @Max(MAX_HOURS)
	public int hours;
	 @Min(MIN_COST) @Max(MAX_COST)
	public int cost;
	@NotNull
	@Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}.*")
	public String openingDate;
	public Course(Integer id, @NotEmpty String course, @NotEmpty String lecturer, @Min(80) @Max(500) int hours,
			@Min(5000) @Max(20000) int cost, @NotNull @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}.*") String openingDate) {
		super();
		this.id = id;
		this.course = course;
		this.lecturer = lecturer;
		this.hours = hours;
		this.cost = cost;
		this.openingDate = openingDate;
	}
	public Course() {
	}
	public Course(@NotEmpty String course, @NotEmpty String lecturer, @Min(80) @Max(500) int hours,
			@Min(5000) @Max(20000) int cost, @NotNull @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}.*") String openingDate) {
		this.course = course;
		this.lecturer = lecturer;
		this.hours = hours;
		this.cost = cost;
		this.openingDate = openingDate;
	}
	@Override
	public int hashCode() {
		return Objects.hash(cost, course, hours, id, lecturer, openingDate);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		return cost == other.cost && Objects.equals(course, other.course) && hours == other.hours
				&& Objects.equals(id, other.id) && Objects.equals(lecturer, other.lecturer)
				&& Objects.equals(openingDate, other.openingDate);
	}
	@Override
	public String toString() {
		return "Course [id=" + id + ", course=" + course + ", lecturer=" + lecturer + ", hours=" + hours + ", cost="
				+ cost + ", openingDate=" + openingDate + "]";
	}
}
