using System;
using System.Collections.Generic;

namespace LMS.Models.LMSModels
{
    public partial class Assignment
    {
        public Assignment()
        {
            Submissions = new HashSet<Submission>();
        }

        public int AccatalogId { get; set; }
        public string Name { get; set; } = null!;
        public int MaxPointValue { get; set; }
        public string Contents { get; set; } = null!;
        public DateTime DueDateTime { get; set; }
        public uint AssignmentId { get; set; }
        public string Acname { get; set; } = null!;
        public string Acsemester { get; set; } = null!;

        public virtual AssignmentCategory Ac { get; set; } = null!;
        public virtual ICollection<Submission> Submissions { get; set; }
    }
}
