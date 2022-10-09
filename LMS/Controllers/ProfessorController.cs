using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.Json;
using System.Threading.Tasks;
using LMS.Models.LMSModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

// For more information on enabling MVC for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace LMS_CustomIdentity.Controllers
{
    [Authorize(Roles = "Professor")]
    public class ProfessorController : Controller
    {

        //If your context is named something else, fix this
        //and the constructor param
        private readonly LMSContext db;

        public ProfessorController(LMSContext _db)
        {
            db = _db;
        }

        public IActionResult Index()
        {
            return View();
        }

        public IActionResult Students(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
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

        public IActionResult Categories(string subject, string num, string season, string year)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            return View();
        }

        public IActionResult CatAssignments(string subject, string num, string season, string year, string cat)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
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

        public IActionResult Submissions(string subject, string num, string season, string year, string cat, string aname)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            return View();
        }

        public IActionResult Grade(string subject, string num, string season, string year, string cat, string aname, string uid)
        {
            ViewData["subject"] = subject;
            ViewData["num"] = num;
            ViewData["season"] = season;
            ViewData["year"] = year;
            ViewData["cat"] = cat;
            ViewData["aname"] = aname;
            ViewData["uid"] = uid;
            return View();
        }

        /*******Begin code to modify********/


        /// <summary>
        /// Returns a JSON array of all the students in a class.
        /// Each object in the array should have the following fields:
        /// "fname" - first name
        /// "lname" - last name
        /// "uid" - user ID
        /// "dob" - date of birth
        /// "grade" - the student's grade in this class
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetStudentsInClass(string subject, int num, string season, int year)
        {
            string semester = year.ToString() + season;

            var get_student = from c in db.Courses
                              where subject == c.ListedUnder && num == c.Number
                              join cc in db.Classes
                              on c.CatalogId equals cc.CatalogId
                              where semester == cc.Semester
                              join e in db.EnrollIns
                              on cc.CatalogId equals e.CatalogId
                              join u in db.Users
                              on e.UId equals u.UId
                              select new { fname = u.FirstName, lname = u.LastName, uid = u.UId, dob = u.Dob, grade = e.Grade };

            return Json(get_student.ToArray());
        }

        /// <summary>
        /// Returns a JSON array with all the assignments in an assignment category for a class.
        /// If the "category" parameter is null, return all assignments in the class.
        /// Each object in the array should have the following fields:
        /// "aname" - The assignment name
        /// "cname" - The assignment category name.
        /// "due" - The due DateTime
        /// "submissions" - The number of submissions to the assignment
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class, 
        /// or null to return assignments from all categories</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetAssignmentsInCategory(string subject, int num, string season, int year, string category)
        {
            string semester = year.ToString() + season;

            if (category != null)
            {
                var get_assignment = from c in db.Courses
                                     where subject == c.ListedUnder && num == c.Number
                                     join cc in db.Classes
                                     on c.CatalogId equals cc.CatalogId
                                     where semester == cc.Semester
                                     join ac in db.AssignmentCategories
                                     on cc.CatalogId equals ac.CatalogId
                                     where category == ac.Name
                                     join a in db.Assignments
                                     on ac.CatalogId equals a.AccatalogId
                                     where ac.Name == a.Acname
                                     select new
                                     {
                                         aname = a.Name,
                                         cname = ac.Name,
                                         due = a.DueDateTime,
                                         submissions = (from s in db.Submissions
                                                        where s.AId == a.AssignmentId
                                                        select s.AId).Count()
                                     };

                return Json(get_assignment.ToArray());
            }
            else
            {
                var get_assignment = from c in db.Courses
                                     where subject == c.ListedUnder && num == c.Number
                                     join cc in db.Classes
                                     on c.CatalogId equals cc.CatalogId
                                     where semester == cc.Semester
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
                                         submissions = (from s in db.Submissions
                                                        where s.AId == a.AssignmentId
                                                        select s.AId).Count()
                                     };

                return Json(get_assignment.ToArray());
            }
        }

        /// <summary>
        /// Returns a JSON array of the assignment categories for a certain class.
        /// Each object in the array should have the folling fields:
        /// "name" - The category name
        /// "weight" - The category weight
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetAssignmentCategories(string subject, int num, string season, int year)
        {
            string semester = year.ToString() + season;

            var get_assignment_cats = from c in db.Courses
                                      where subject == c.ListedUnder && num == c.Number
                                      join cc in db.Classes
                                      on c.CatalogId equals cc.CatalogId
                                      where semester == cc.Semester
                                      join ac in db.AssignmentCategories
                                      on cc.CatalogId equals ac.CatalogId
                                      select new
                                      {
                                          name = ac.Name,
                                          weight = ac.GradingWeight,
                                      };

            return Json(get_assignment_cats);
        }

        /// <summary>
        /// Creates a new assignment category for the specified class.
        /// If a category of the given class with the given name already exists, return success = false.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The new category name</param>
        /// <param name="catweight">The new category weight</param>
        /// <returns>A JSON object containing {success = true/false} </returns>
        public IActionResult CreateAssignmentCategory(string subject, int num, string season, int year, string category, int catweight)
        {
            string semester = year.ToString() + season;

            var get_assignment_cats = from c in db.Courses
                                      where subject == c.ListedUnder && num == c.Number
                                      join cc in db.Classes
                                      on c.CatalogId equals cc.CatalogId
                                      where semester == cc.Semester
                                      join ac in db.AssignmentCategories
                                      on cc.CatalogId equals ac.CatalogId
                                      where category == ac.Name && catweight == ac.GradingWeight
                                      select ac.Name;

            if (get_assignment_cats.Count() > 0)
            {
                return Json(new { success = false });
            }

            var get_class_id = from c in db.Courses
                               where subject == c.ListedUnder && num == c.Number
                               join cc in db.Classes
                               on c.CatalogId equals cc.CatalogId
                               where semester == cc.Semester
                               select cc.CatalogId;

            int class_id = get_class_id.ToArray()[0];

            AssignmentCategory newCats = new AssignmentCategory();
            newCats.CatalogId = class_id;
            newCats.Name = category;
            newCats.Semester = semester;
            newCats.GradingWeight = catweight;
            db.AssignmentCategories.Add(newCats);
            db.SaveChanges();

            return Json(new { success = true });
        }

        /// <summary>
        /// Creates a new assignment for the given class and category.
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The new assignment name</param>
        /// <param name="asgpoints">The max point value for the new assignment</param>
        /// <param name="asgdue">The due DateTime for the new assignment</param>
        /// <param name="asgcontents">The contents of the new assignment</param>
        /// <returns>A JSON object containing success = true/false</returns>
        public IActionResult CreateAssignment(string subject, int num, string season, int year, string category, string asgname, int asgpoints, DateTime asgdue, string asgcontents)
        {
            string semester = year.ToString() + season;

            var get_assignment = from c in db.Courses
                                 where subject == c.ListedUnder && num == c.Number
                                 join cc in db.Classes
                                 on c.CatalogId equals cc.CatalogId
                                 where semester == cc.Semester
                                 join en in db.EnrollIns
                                 on cc.CatalogId equals en.CatalogId
                                 join ac in db.AssignmentCategories
                                 on cc.CatalogId equals ac.CatalogId
                                 where category == ac.Name
                                 join a in db.Assignments
                                 on ac.CatalogId equals a.AccatalogId
                                 where asgname == a.Name
                                 select new { cID = a.AccatalogId, cName = ac.Name, c_semester = ac.Semester, whichStu = en.UId };

            if (get_assignment.Count() > 0)
            {
                return Json(new { success = false });
            }

            uint a_id = 1;

            while (true)
            {
                var get_aid = from a in db.Assignments
                              where a_id == a.AssignmentId
                              select a.AssignmentId;
                if (get_aid.Count() != 0)
                {
                    a_id++;
                }
                else
                {
                    break;
                }
            }

            var get_cata_info = from c in db.Courses
                                where subject == c.ListedUnder && num == c.Number
                                join cc in db.Classes
                                on c.CatalogId equals cc.CatalogId
                                where semester == cc.Semester
                                join en in db.EnrollIns
                                on cc.CatalogId equals en.CatalogId
                                join ac in db.AssignmentCategories
                                on cc.CatalogId equals ac.CatalogId
                                where category == ac.Name
                                select new
                                {
                                    cID = ac.CatalogId,
                                    cname = ac.Name,
                                    csemester = ac.Semester,
                                };

            int c_id = get_cata_info.ToArray()[0].cID;
            string c_name = get_cata_info.ToArray()[0].cname;
            string c_semester = get_cata_info.ToArray()[0].csemester;

            Assignment newAss = new Assignment();
            newAss.AccatalogId = c_id;
            newAss.Name = asgname;
            newAss.MaxPointValue = asgpoints;
            newAss.Contents = asgcontents;
            newAss.DueDateTime = asgdue;
            newAss.AssignmentId = a_id;
            newAss.Acname = c_name;
            newAss.Acsemester = c_semester;
            db.Assignments.Add(newAss);
            db.SaveChanges();

            var allStud = get_assignment.ToArray();

            for (int i = 0; i < allStud.Length; i++)
            {
                var find_enroll = from co in db.Courses
                                  where subject == co.ListedUnder && num == co.Number
                                  join cl in db.Classes
                                  on co.CatalogId equals cl.CatalogId
                                  where semester == cl.Semester
                                  join e in db.EnrollIns
                                  on cl.CatalogId equals e.CatalogId
                                  join s in db.Students
                                  on e.UId equals s.UId
                                  where allStud[i].whichStu == s.UId
                                  select e;
                find_enroll.ToArray()[0].Grade = AutoGrading(subject, num, season, year, allStud[i].whichStu);
                db.SaveChanges();
            }

            return Json(new { success = true });
        }


        /// <summary>
        /// Gets a JSON array of all the submissions to a certain assignment.
        /// Each object in the array should have the following fields:
        /// "fname" - first name
        /// "lname" - last name
        /// "uid" - user ID
        /// "time" - DateTime of the submission
        /// "score" - The score given to the submission
        /// 
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetSubmissionsToAssignment(string subject, int num, string season, int year, string category, string asgname)
        {
            string semester = year.ToString() + season;

            var get_student_score = from c in db.Courses
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
                                    join u in db.Users
                                    on s.UId equals u.UId
                                    select new
                                    {
                                        fname = u.FirstName,
                                        lname = u.LastName,
                                        uid = u.UId,
                                        time = s.SubmissionDateTime,
                                        score = s.Score,
                                    };

            return Json(get_student_score.ToArray());
        }

        /// <summary>
        /// Set the score of an assignment submission
        /// </summary>
        /// <param name="subject">The course subject abbreviation</param>
        /// <param name="num">The course number</param>
        /// <param name="season">The season part of the semester for the class the assignment belongs to</param>
        /// <param name="year">The year part of the semester for the class the assignment belongs to</param>
        /// <param name="category">The name of the assignment category in the class</param>
        /// <param name="asgname">The name of the assignment</param>
        /// <param name="uid">The uid of the student who's submission is being graded</param>
        /// <param name="score">The new score for the submission</param>
        /// <returns>A JSON object containing success = true/false</returns>
        public IActionResult GradeSubmission(string subject, int num, string season, int year, string category, string asgname, string uid, int score)
        {
            string parm_emester = year.ToString() + season;

            var raw_submission = from c in db.Courses
                                 where subject == c.ListedUnder && num == c.Number
                                 join cl in db.Classes
                                 on c.CatalogId equals cl.CatalogId
                                 where parm_emester == cl.Semester
                                 join ac in db.AssignmentCategories
                                 on cl.CatalogId equals ac.CatalogId
                                 where category == ac.Name
                                 join a in db.Assignments
                                 on ac.CatalogId equals a.AccatalogId
                                 where asgname == a.Name
                                 join sub in db.Submissions
                                 on a.AssignmentId equals sub.AId
                                 where uid == sub.UId
                                 select sub;

            if (raw_submission.Count() > 0)
            {
                raw_submission.ToArray()[0].Score = score;
                db.SaveChanges();

                var find_enroll = from co in db.Courses
                                  where subject == co.ListedUnder && num == co.Number
                                  join cl in db.Classes
                                  on co.CatalogId equals cl.CatalogId
                                  where parm_emester == cl.Semester
                                  join e in db.EnrollIns
                                  on cl.CatalogId equals e.CatalogId
                                  join s in db.Students
                                  on e.UId equals s.UId
                                  where uid == s.UId
                                  select e;
                find_enroll.ToArray()[0].Grade = AutoGrading(subject, num, season, year, uid);
                db.SaveChanges();

                return Json(new { success = true });
            }
            else
            {
                return Json(new { success = false });
            }
        }

        /// <summary>
        /// Returns a JSON array of the classes taught by the specified professor
        /// Each object in the array should have the following fields:
        /// "subject" - The subject abbreviation of the class (such as "CS")
        /// "number" - The course number (such as 5530)
        /// "name" - The course name
        /// "season" - The season part of the semester in which the class is taught
        /// "year" - The year part of the semester in which the class is taught
        /// </summary>
        /// <param name="uid">The professor's uid</param>
        /// <returns>The JSON array</returns>
        public IActionResult GetMyClasses(string uid)
        {
            var get_class = from p in db.Professors
                            where uid == p.UId
                            join c in db.Classes
                            on p.UId equals c.Instructor
                            join cc in db.Courses
                            on c.CatalogId equals cc.CatalogId
                            select new
                            {
                                subject = cc.ListedUnder,
                                number = cc.Number,
                                name = cc.Name,
                                season = c.Semester.Substring(4, c.Semester.Length - 4),
                                year = c.Semester.Substring(0, 4),
                            };

            return Json(get_class.ToArray());
        }

        public string AutoGrading(string subject, int num, string season, int year, string uid)
        {
            string para_semester = year.ToString() + season;

            var effectiveAsCate = from c in db.Courses
                                  where subject == c.ListedUnder && num == c.Number
                                  join cc in db.Classes
                                  on c.CatalogId equals cc.CatalogId
                                  where para_semester == cc.Semester
                                  join ac in db.AssignmentCategories
                                  on cc.CatalogId equals ac.CatalogId
                                  join a in db.Assignments
                                  on ac.CatalogId equals a.AccatalogId
                                  where ac.Name == a.Acname
                                  select new { cat = a.Acname, weig = ac.GradingWeight };
            var distinctAsCate = effectiveAsCate.Distinct();

            double realWeighSum = 0.0;
            foreach (var info in distinctAsCate) {
                realWeighSum += info.weig;
            }

            Dictionary<string, double> cateAndRate = new Dictionary<string, double>();

            foreach (var info in distinctAsCate)
            {
                cateAndRate.Add(info.cat, info.weig / realWeighSum);
            }

            var allAssignments = from e in db.EnrollIns
                      where uid == e.UId
                      join c in db.Classes
                      on e.CatalogId equals c.CatalogId
                      where para_semester == c.Semester
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
                          max = a.MaxPointValue,
                          score = (from ss in db.Submissions
                                   where ss.AId == a.AssignmentId && ss.UId == uid
                                   select ss.Score
                                   ).FirstOrDefault()
                      };

            Dictionary<string, double> cateAndScore = new Dictionary<string, double>();
            Dictionary<string, double> cateAndMax = new Dictionary<string, double>();

            foreach (var info in allAssignments)
            {
                if(cateAndScore.ContainsKey(info.cname))
                {
                    double temp1 = cateAndScore[info.cname] + info.score;
                    cateAndScore[info.cname] = temp1;

                    double temp2 = cateAndMax[info.cname] + info.max;
                    cateAndMax[info.cname] = temp2;
                }
                else
                {
                    cateAndScore[info.cname] = info.score;
                    cateAndMax[info.cname] = info.max;
                }
            }

            double classGrade = 0.0;
            foreach (var calculate in cateAndRate)
            {
                classGrade += cateAndScore[calculate.Key] / cateAndMax[calculate.Key] * cateAndRate[calculate.Key];
            }

            classGrade *= 100;

            Console.WriteLine("Finishing AutoGrading: "+ classGrade);
            if (classGrade >= 92)
            {
                return ("A");
            }
            else if (classGrade >= 90)
            {
                return ("A-");
            }
            else if (classGrade >= 87)
            {
                return ("B+");
            }
            else if (classGrade >= 82)
            {
                return ("B");
            }
            else if (classGrade >= 80)
            {
                return ("B-");
            }
            else if (classGrade >= 77)
            {
                return ("C+");
            }
            else if (classGrade >= 72)
            {
                return ("C");
            }
            else if (classGrade >= 70)
            {
                return ("C-");
            }
            else if (classGrade >= 67)
            {
                return ("D+");
            }
            else if (classGrade >= 62)
            {
                return ("D");
            }
            else if (classGrade >= 60)
            {
                return ("D-");
            }
            else
            {
                return ("E");
            }
        }

        /*******End code to modify********/
    }
}

