using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;
using LMS.Models.LMSModels;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace LMS.Controllers
{
    public class AdministratorController : Controller
    {

        //If your context class is named something different,
        //fix this member var and the constructor param
        private readonly LMSContext db;

        public AdministratorController(LMSContext _db)
        {
            db = _db;
        }

        // GET: /<controller>/
        public IActionResult Index()
        {
            return View();
        }

        public IActionResult Department(string subject)
        {
            ViewData["subject"] = subject;
            return View();
        }

        public IActionResult Course(string subject, string num)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            return View();
        }

        /*******Begin code to modify********/

        /// <summary>
        /// Create a department which is uniquely identified by it's subject code
        /// </summary>
        /// <param name="subject">the subject code</param>
        /// <param name="name">the full name of the department</param>
        /// <returns>A JSON object containing {success = true/false}.
        /// false if the department already exists, true otherwise.</returns>
        public IActionResult CreateDepartment(string subject, string name)
        {
            var get_department_name = from d in db.Departments
                                      select d.Name;
            var get_department_subject = from d in db.Departments
                                         select d.SubjectAbbr;

            if (get_department_name.Contains(name) && get_department_subject.Contains(subject))
            {
                return Json(new { success = false });
            }


            Department newDepartment = new Department();

            newDepartment.SubjectAbbr = subject;
            newDepartment.Name = name;

            db.Departments.Add(newDepartment);
            db.SaveChanges();

            return Json(new { success = true });
        }


        /// <summary>
        /// Returns a JSON array of all the courses in the given department.
        /// Each object in the array should have the following fields:
        /// "number" - The course number (as in 5530)
        /// "name" - The course name (as in "Database Systems")
        /// </summary>
        /// <param name="subjCode">The department subject abbreviation (as in "CS")</param>
        /// <returns>The JSON result</returns>
        public IActionResult GetCourses(string subject)
        {
            var get_Courses = from c in db.Courses
                              where c.ListedUnder == subject
                              select new { number = c.Number, name = c.Name };

            return Json(get_Courses.ToArray());
        }

        /// <summary>
        /// Returns a JSON array of all the professors working in a given department.
        /// Each object in the array should have the following fields:
        /// "lname" - The professor's last name
        /// "fname" - The professor's first name
        /// "uid" - The professor's uid
        /// </summary>
        /// <param name="subject">The department subject abbreviation</param>
        /// <returns>The JSON result</returns>
        public IActionResult GetProfessors(string subject)
        {
            var get_professor = from p in db.Professors
                                where p.WorkIn == subject
                                join u in db.Users
                                on p.UId equals u.UId
                                select new { lname = u.LastName, fname = u.FirstName, uid = u.UId };

            return Json(get_professor.ToArray());
        }

        /// <summary>
        /// Creates a course.
        /// A course is uniquely identified by its number + the subject to which it belongs
        /// </summary>
        /// <param name="subject">The subject abbreviation for the department in which the course will be added</param>
        /// <param name="number">The course number</param>
        /// <param name="name">The course name</param>
        /// <returns>A JSON object containing {success = true/false}.
        /// false if the course already exists, true otherwise.</returns>
        public IActionResult CreateCourse(string subject, int number, string name)
        {
            var get_courseIDAndsub = from c in db.Courses
                                     where number == c.Number && subject == c.ListedUnder
                                     select c;
            if(get_courseIDAndsub.Count() > 0)
            {
                return Json(new { success = false });
            }


            int catalog_id = 1;

            while (true)
            {
                var get_cID = from c in db.Courses
                              where c.CatalogId == catalog_id
                              select c.CatalogId;
                if (get_cID.Count() != 0)
                {
                    catalog_id++;
                }
                else
                {
                    break;
                }
            }

            Course newCourse = new Course();

            newCourse.CatalogId = catalog_id;
            newCourse.ListedUnder = subject;
            newCourse.Number = number;
            newCourse.Name = name;
            db.Courses.Add(newCourse);
            db.SaveChanges();

            return Json(new { success = true });
        }


        /// <summary>
        /// Creates a class offering of a given course.
        /// </summary>
        /// <param name="subject">The department subject abbreviation</param>
        /// <param name="number">The course number</param>
        /// <param name="season">The season part of the semester</param>
        /// <param name="year">The year part of the semester</param>
        /// <param name="start">The start time</param>
        /// <param name="end">The end time</param>
        /// <param name="location">The location</param>
        /// <param name="instructor">The uid of the professor</param>
        /// <returns>A JSON object containing {success = true/false}. 
        /// false if another class occupies the same location during any time 
        /// within the start-end range in the same semester, or if there is already
        /// a Class offering of the same Course in the same Semester,
        /// true otherwise.</returns>
        public IActionResult CreateClass(string subject, int number, string season, int year, DateTime start, DateTime end, string location, string instructor)
        {
            string semester = year.ToString() + season;
            var get_class_loc = from c in db.Courses
                                join c1 in db.Classes
                                on c.CatalogId equals c1.CatalogId
                                select new { location = c1.Location, start_time = c1.StartTime, end_time = c1.EndTime, semester_year = c1.Semester, departm = c.ListedUnder, course_num = c.Number };
            if (get_class_loc.Count() > 0)
            {
                foreach (var eachRow in get_class_loc)
                {
                    if (location == eachRow.location)
                    {
                        if (DateTimeRangeConflict(start, end, eachRow.start_time, eachRow.end_time))
                        {
                            if (semester == eachRow.semester_year)
                            {

                                Console.WriteLine("Case 1");
                                return Json(new { success = false });
                            }
                        }
                    }
                    if (subject == eachRow.departm && number == eachRow.course_num && semester == eachRow.semester_year)
                    {
                        Console.WriteLine("Case 2");
                        return Json(new { success = false });
                    }
                }
            }

            var get_cID = from c in db.Courses
                          where number == c.Number && subject == c.ListedUnder
                          select c.CatalogId;
            int catalogID = get_cID.ToArray()[0];

            Class newClass = new Class();
            newClass.CatalogId = catalogID;
            newClass.Semester = semester;
            newClass.Location = location;
            newClass.StartTime = start;
            newClass.EndTime = end;
            newClass.Instructor = instructor;
            db.Classes.Add(newClass);
            db.SaveChanges();

            return Json(new { success = true });
        }

        public bool DateTime1Earlier2(DateTime dt1, DateTime dt2)
        {
            int result = DateTime.Compare(dt1, dt2);
            if (result < 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public bool DateTime1Later2(DateTime dt1, DateTime dt2)
        {
            int result = DateTime.Compare(dt1, dt2);
            if (result > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public bool DateTimeRangeConflict(DateTime newStart, DateTime newEnd, DateTime oldStart, DateTime oldEnd)
        {
            if (DateTime1Earlier2(newStart, oldStart) && DateTime1Earlier2(oldStart, oldStart))
            {
                return false;
            }
            else if (DateTime1Later2(newEnd, oldStart) && DateTime1Later2(newEnd, oldStart))
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        /*******End code to modify********/

    }
}

