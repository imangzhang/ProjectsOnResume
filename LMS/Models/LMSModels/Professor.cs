using System;
using System.Collections.Generic;

namespace LMS.Models.LMSModels
{
    public partial class Professor
    {
        public Professor()
        {
            Classes = new HashSet<Class>();
        }

        public string UId { get; set; } = null!;
        public string WorkIn { get; set; } = null!;

        public virtual User UIdNavigation { get; set; } = null!;
        public virtual Department WorkInNavigation { get; set; } = null!;
        public virtual ICollection<Class> Classes { get; set; }
    }
}
