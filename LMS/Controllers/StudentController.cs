using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using LMS.Models.LMSModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace LMS.Controllers
{
    [Authorize(Roles = "Student")]
    public class StudentController : Controller
    {
        //If your context is named something else, fix this and the
        //constructor param
        private LMSContext db;
        public StudentController(LMSContext _db)
        {
            db = _db;
        }

        public IActionResult Index()
        {
            return View();
        }

        public IActionResult Catalog()
        {
            return View();
        }

        public IActionResult Class(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            return View();
        }

        public IActionResult Assignment(string subject, string num, string season, string year, string cat, string aname)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            return View();
        }


        public IActionResult ClassListings(string subject, string num)
        {
            System.Diagnostics.Debug.WriteLine(subject + num);
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            return View();
        }


        /*******Begin code to modify********/

        /// <summary>
        /// Returns a JSON array of the classes the given student is enrolled in.
        /// Each object in the array should have the following fields:
        /// "subject" - The subject abbreviation of the class (such as "CS")
        /// "number" - The course number (such as 5530)
        /// "name" - The course name
        /// "season" - The season part of the semester
        /// "year" - The year part of the semester
        /// "grade" - The grade earned in the class, or "--" if one hasn't been assigned
        /// </summary>
        /// <param name="uid">The uid of the student</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetMyClasses(string uid)
        {
            var get_class = from e in db.EnrollIns
                            where uid == e.UId
                            join c in db.Classes
                            on e.CatalogId equals c.CatalogId
                            where e.Semester == c.Semester
                            join cc in db.Courses
                            on c.CatalogId equals cc.CatalogId
                            select new { subject = cc.ListedUnder, number = cc.Number, name = cc.Name, season = c.Semester.Substring(4, c.Semester.Length - 4), year = c.Semester.Substring(0, 4), grade = e.Grade };

            return Json(get_class.ToArray());
        }

        /// <summary>
        /// Returns a JSON array of all the assignments in the given class that the given student is enrolled in.
        /// Each object in the array should have the following fields:
        /// "aname" - The assignment name
        /// "cname" - The category name that the assignment belongs to
        /// "due" - The due Date/Time
        /// "score" - The score earned by the student, or null if the student has not submitted to this assignment.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="uid"></param>
        /// <returns>The JSON array</returns>
        public IActionResult GetAssignmentsInClass(string subject, int num, string season, int year, string uid)
        {
            string semester = year.ToString() + season;

            var get = from e in db.EnrollIns
                      where uid == e.UId
                      join c in db.Classes
                      on e.CatalogId equals c.CatalogId
                      where semester == c.Semester
                      join cc in db.Courses
                      on c.CatalogId equals cc.CatalogId
                      where cc.ListedUnder == subject && cc.Number == num
                      join ac in db.AssignmentCategories
                      on cc.CatalogId equals ac.CatalogId
                      join a in db.Assignments
                      on ac.CatalogId equals a.AccatalogId
                      where ac.Name == a.Acname
                      select new
                      {
                          aname = a.Name,
                          cname = ac.Name,
                          due = a.DueDateTime,
                          score = (from ss in db.Submissions
                                   where ss.AId == a.AssignmentId && ss.UId == uid
                                   select ss.Score
                                   ).FirstOrDefault()
                      };

            return Json(get.ToArray());
        }



        /// <summary>
        /// Adds a submission to the given assignment for the given student
        /// The submission should use the current time as its DateTime
        /// You can get the current time with DateTime.Now
        /// The score of the submission should start as 0 until a Professor grades it
        /// If a Student submits to an assignment again, it should replace the submission contents
        /// and the submission time (the score should remain the same).
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The new assignment name</param>
        /// <param name="uid">The student submitting the assignment</param>
        /// <param name="contents">The text contents of the student's submission</param>
        /// <returns>A JSON object containing {success = true/false}</returns>
        public IActionResult SubmitAssignmentText(string subject, int num, string season, int year,
          string category, string asgname, string uid, string contents)
        {
            string semester = year.ToString() + season;

            var get_replace = from c in db.Courses
                              where subject == c.ListedUnder && num == c.Number
                              join cc in db.Classes
                              on c.CatalogId equals cc.CatalogId
                              where semester == cc.Semester
                              join ac in db.AssignmentCategories
                              on cc.CatalogId equals ac.CatalogId
                              where category == ac.Name
                              join a in db.Assignments
                              on ac.CatalogId equals a.AccatalogId
                              where asgname == a.Name
                              join s in db.Submissions
                              on a.AssignmentId equals s.AId
                              select s;

            if (get_replace.Count() > 0)
            {
                get_replace.ToArray()[0].SubmissionDateTime = DateTime.Now;
                get_replace.ToArray()[0].Contents = contents;
            }
            else
            {
                var get_aid = from c in db.Courses
                              where subject == c.ListedUnder && num == c.Number
                              join cc in db.Classes
                              on c.CatalogId equals cc.CatalogId
                              where semester == cc.Semester
                              join ac in db.AssignmentCategories
                              on cc.CatalogId equals ac.CatalogId
                              where category == ac.Name
                              join a in db.Assignments
                              on ac.CatalogId equals a.AccatalogId
                              where asgname == a.Name
                              select a.AssignmentId;

                Submission newSub = new Submission();
                newSub.UId = uid;
                newSub.SubmissionDateTime = DateTime.Now;
                newSub.Contents = contents;
                newSub.Score = 0;
                newSub.AId = get_aid.ToArray()[0];
                db.Submissions.Add(newSub);
            }

            db.SaveChanges();
            return Json(new { success = true });
        }

        /// <summary>
        /// Enrolls a student in a class.
        /// </summary>
        /// <param name="subject">The department subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester</param>
        /// <param name="year">The year part of the semester</param>
        /// <param name="uid">The uid of the student</param>
        /// <returns>A JSON object containing {success = {true/false}. 
        /// false if the student is already enrolled in the class, true otherwise.</returns>
        public IActionResult Enroll(string subject, int num, string season, int year, string uid)
        {
            string semester = year.ToString() + season;

            var student = from e in db.EnrollIns
                          where uid == e.UId && semester == e.Semester
                          join c in db.Classes
                          on e.CatalogId equals c.CatalogId
                          join cc in db.Courses
                          on c.CatalogId equals cc.CatalogId
                          where subject == cc.ListedUnder && num == cc.Number
                          select e;

            if (student.Count() > 0)
            {
                return Json(new { success = false });
            }

            var cID = from cc in db.Courses
                      where num == cc.Number && subject == cc.ListedUnder
                      select cc.CatalogId;

            EnrollIn enr = new EnrollIn();
            enr.UId = uid;
            enr.CatalogId = cID.ToArray()[0];
            enr.Semester = semester;
            enr.Grade = "--";
            db.EnrollIns.Add(enr);
            db.SaveChanges();

            return Json(new { success = true });
        }

        /// <summary>
        /// Calculates a student's GPA
        /// A student's GPA is determined by the grade-point representation of the average grade in all their classes.
        /// Assume all classes are 4 credit hours.
        /// If a student does not have a grade in a class ("--"), that class is not counted in the average.
        /// If a student is not enrolled in any classes, they have a GPA of 0.0.
        /// Otherwise, the point-value of a letter grade is determined by the table on this page:
        /// https://advising.utah.edu/academic-standards/gpa-calculator-new.php
        /// </summary>
        /// <param name="uid">The uid of the student</param>
        /// <returns>A JSON object containing a single field called "gpa" with the number value</returns>
        public IActionResult GetGPA(string uid)
        {
            var grade = from s in db.Students
                        where uid == s.UId
                        join e in db.EnrollIns
                        on s.UId equals e.UId
                        select e;

            if (grade.Count() == 0)
            {
                return Json(new { gpa = 0.0 });
            }

            double gpa = 0.0;
            int total = 0;

            foreach (var each_grade in grade)
            {
                if (each_grade.Grade == "A")
                {
                    gpa += 4.0;
                    total++;
                }
                else if (each_grade.Grade == "A-")
                {
                    gpa += 3.7;
                    total++;
                }
                else if (each_grade.Grade == "B+")
                {
                    gpa += 3.3;
                    total++;
                }
                else if (each_grade.Grade == "B")
                {
                    gpa += 3.0;
                    total++;
                }
                else if (each_grade.Grade == "B-")
                {
                    gpa += 2.7;
                    total++;
                }
                else if (each_grade.Grade == "C+")
                {
                    gpa += 2.3;
                    total++;
                }
                else if (each_grade.Grade == "C")
                {
                    gpa += 2.0;
                    total++;
                }
                else if (each_grade.Grade == "C-")
                {
                    gpa += 1.7;
                    total++;
                }
                else if (each_grade.Grade == "D+")
                {
                    gpa += 1.3;
                    total++;
                }
                else if (each_grade.Grade == "D")
                {
                    gpa += 1.0;
                    total++;
                }
                else if (each_grade.Grade == "D-")
                {
                    gpa += 0.7;
                    total++;
                }
                else if (each_grade.Grade == "E")
                {
                    gpa += 0.0;
                    total++;
                }
            }

            double result = gpa / total;

            return Json(new { gpa = result });
        }

        /*******End code to modify********/

    }
}

