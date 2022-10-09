using System;
using System.Collections.Generic;

namespace LMS.Models.LMSModels
{
    public partial class Student
    {
        public Student()
        {
            EnrollIns = new HashSet<EnrollIn>();
            Submissions = new HashSet<Submission>();
        }

        public string UId { get; set; } = null!;
        public string MajorIn { get; set; } = null!;

        public virtual Department MajorInNavigation { get; set; } = null!;
        public virtual User UIdNavigation { get; set; } = null!;
        public virtual ICollection<EnrollIn> EnrollIns { get; set; }
        public virtual ICollection<Submission> Submissions { get; set; }
    }
}
