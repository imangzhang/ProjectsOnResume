using System;
using System.Collections.Generic;

namespace LMS.Models.LMSModels
{
    public partial class Course
    {
        public Course()
        {
            Classes = new HashSet<Class>();
        }

        public int CatalogId { get; set; }
        public int Number { get; set; }
        public string Name { get; set; } = null!;
        public string ListedUnder { get; set; } = null!;

        public virtual Department ListedUnderNavigation { get; set; } = null!;
        public virtual ICollection<Class> Classes { get; set; }
    }
}
