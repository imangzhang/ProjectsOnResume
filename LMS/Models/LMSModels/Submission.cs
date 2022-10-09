using System;
using System.Collections.Generic;

namespace LMS.Models.LMSModels
{
    public partial class Submission
    {
        public string UId { get; set; } = null!;
        public DateTime SubmissionDateTime { get; set; }
        public string Contents { get; set; } = null!;
        public int Score { get; set; }
        public uint AId { get; set; }

        public virtual Assignment AIdNavigation { get; set; } = null!;
        public virtual Student UIdNavigation { get; set; } = null!;
    }
}
