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
    public class CommonController : Controller
    {
        //If your context class is named differently, fix this
        //and the constructor parameter
        private readonly LMSContext db;

        public CommonController(LMSContext _db)
        {
            db = _db;
        }

        /*******Begin code to modify********/

        /// <summary>
        /// Retreive a JSON array of all departments from the database.
        /// Each object in the array should have a field called "name" and "subject",
        /// where "name" is the department name and "subject" is the subject abbreviation.
        /// </summary>
        /// <returns>The JSON array</returns>
        public IActionResult GetDepartments()
        {
            var get_department = from department in db.Departments
                                 select new { name = department.Name, subject = department.SubjectAbbr };

            return Json(get_department.ToArray());
        }

        /// <summary>
        /// Returns a JSON array representing the course catalog.
        /// Each object in the array should have the following fields:
        /// "subject": The subject abbreviation, (e.g. "CS")
        /// "dname": The department name, as in "Computer Science"
        /// "courses": An array of JSON objects representing the courses in the department.
        ///            Each field in this inner-array should have the following fields:
        ///            "number": The course number (e.g. 5530)
        ///            "cname": The course name (e.g. "Database Systems")
        /// </summary>
        /// <returns>The JSON array</returns>
        public IActionResult GetCatalog()
        {
            var get_department = from d in db.Departments
                                 select new
                                 {
                                     subject = d.SubjectAbbr,
                                     dname = d.Name,
                                     courses = (from c in db.Courses
                                                where c.ListedUnder == d.SubjectAbbr
                                                select new { number = c.Number, cname = c.Name }).AsEnumerable()
                                 };

            return Json(get_department.ToArray());
        }

        /// <summary>
        /// Returns a JSON array of all class offerings of a specific course.
        /// Each object in the array should have the following fields:
        /// "season": the season part of the semester, such as "Fall"
        /// "year": the year part of the semester
        /// "location": the location of the class
        /// "start": the start time in format "hh:mm:ss"
        /// "end": the end time in format "hh:mm:ss"
        /// "fname": the first name of the professor
        /// "lname": the last name of the professor
        /// </summary>
        /// <param name="subject">The subject abbreviation, as in "CS"</param>
        /// <param name="number">The course number, as in 5530</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetClassOfferings(string subject, int number)
        {
            var get_class_offering = from c in db.Courses
                                     where subject == c.ListedUnder && number == c.Number
                                     join c1 in db.Classes
                                     on c.CatalogId equals c1.CatalogId
                                     join p in db.Professors
                                     on c1.Instructor equals p.UId
                                     join u in db.Users
                                     on p.UId equals u.UId
                                     select new
                                     {
                                         season = c1.Semester.Substring(4, c1.Semester.Length - 4),
                                         year = c1.Semester.Substring(0, 4),
                                         location = c1.Location,
                                         start = c1.StartTime,
                                         end = c1.EndTime,
                                         fname = u.FirstName,
                                         lname = u.LastName
                                     };

            return Json(get_class_offering.ToArray());
        }

        /// <summary>
        /// This method does NOT return JSON. It returns plain text (containing html).
        /// Use "return Content(...)" to return plain text.
        /// Returns the contents of an assignment.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment in the category</param>
        /// <returns>The assignment contents</returns>
        public IActionResult GetAssignmentContents(string subject, int num, string season, int year, string category, string asgname)
        {
            string sem = year.ToString() + season;

            var content = from c in db.Courses
                          where subject == c.ListedUnder && num == c.Number
                          join cc in db.Classes
                          on c.CatalogId equals cc.CatalogId
                          where sem == cc.Semester
                          join ac in db.AssignmentCategories
                          on cc.CatalogId equals ac.CatalogId
                          where category == ac.Name
                          join a in db.Assignments
                          on ac.CatalogId equals a.AccatalogId
                          where asgname == a.Name
                          select new { a.Contents };

            return Content(content.ToArray()[0].Contents);
        }

        /// <summary>
        /// This method does NOT return JSON. It returns plain text (containing html).
        /// Use "return Content(...)" to return plain text.
        /// Returns the contents of an assignment submission.
        /// Returns the empty string ("") if there is no submission.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment in the category</param>
        /// <param name="uid">The uid of the student who submitted it</param>
        /// <returns>The submission text</returns>
        public IActionResult GetSubmissionText(string subject, int num, string season, int year, string category, string asgname, string uid)
        {
            var content = from c in db.Courses
                          where subject == c.ListedUnder && num == c.Number
                          join cc in db.Classes
                          on c.CatalogId equals cc.CatalogId
                          where (year.ToString() + season) == cc.Semester
                          join ac in db.AssignmentCategories
                          on cc.CatalogId equals ac.CatalogId
                          where category == ac.Name
                          join a in db.Assignments
                          on ac.CatalogId equals a.AccatalogId
                          where asgname == a.Name
                          join s in db.Submissions
                          on a.AssignmentId equals s.AId
                          where uid == s.UId
                          select new { s.Contents };

            if (content.Count() == 0)
            {
                return Content("");
            }
            else
            {
                return Content(content.ToArray()[0].Contents);
            }
        }

        /// <summary>
        /// Gets information about a user as a single JSON object.
        /// The object should have the following fields:
        /// "fname": the user's first name
        /// "lname": the user's last name
        /// "uid": the user's uid
        /// "department": (professors and students only) the name (such as "Computer Science") of the department for the user. 
        ///               If the user is a Professor, this is the department they work in.
        ///               If the user is a Student, this is the department they major in.    
        ///               If the user is an Administrator, this field is not present in the returned JSON
        /// </summary>
        /// <param name="uid">The ID of the user</param>
        /// <returns>
        /// The user JSON object 
        /// or an object containing {success: false} if the user doesn't exist
        /// </returns>
        public IActionResult GetUser(string uid)
        {
            var ifStudent = from u in db.Users
                            where uid == u.UId
                            join s in db.Students
                            on u.UId equals s.UId
                            select new { fname = u.FirstName, lname = u.LastName, uid = u.UId, department = s.MajorIn };

            if (ifStudent.ToArray().Length > 0 && ifStudent.ToArray()[0].uid == uid)
            {
                return Json(ifStudent.ToArray()[0]);
            }

            var ifPro = from u in db.Users
                        where uid == u.UId
                        join p in db.Professors
                        on u.UId equals p.UId
                        select new { fname = u.FirstName, lname = u.LastName, uid = u.UId, department = p.WorkIn };
            if (ifPro.ToArray().Length > 0 && ifPro.ToArray()[0].uid == uid)
            {
                return Json(ifPro.ToArray()[0]);
            }

            var ifAdmin = from u in db.Users
                          where uid == u.UId
                          join a in db.Administrators
                          on u.UId equals a.UId
                          select new { fname = u.FirstName, lname = u.LastName, uid = u.UId };

            if (ifAdmin.ToArray().Length > 0 && ifAdmin.ToArray()[0].uid == uid)
            {
                return Json(ifAdmin.ToArray()[0]);
            }

            return Json(new { success = false });
        }

        /*******End code to modify********/
    }
}

