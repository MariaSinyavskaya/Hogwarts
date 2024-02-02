select student.name, student.age, faculty.name
from student
full join faculty on student.faculty_id = faculty.id;

select student.name, student.age, avatar.id
from student
inner join avatar on avatar.student_id  = student.id;

