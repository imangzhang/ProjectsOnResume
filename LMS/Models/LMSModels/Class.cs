using System;
using System.Collections.Generic;

namespace LMS.Models.LMSModels
{
    public partial class Class
    {
        public Class()
        {
            AssignmentCategories = new HashSet<AssignmentCategory>();
            EnrollIns = new HashSet<EnrollIn>();
        }

        public int CatalogId { get; set; }
        public string Semester { get; set; } = null!;
        public string Location { get; set; } = null!;
        public DateTime StartTime { get; set; }
        public DateTime EndTime { get; set; }
        public string Instructor { get; set; } = null!;

        public virtual Course Catalog { get; set; } = null!;
        public virtual Professor InstructorNavigation { get; set; } = null!;
        public virtual ICollection<AssignmentCategory> AssignmentCategories { get; set; }
        public virtual ICollection<EnrollIn> EnrollIns { get; set; }
    }
}
