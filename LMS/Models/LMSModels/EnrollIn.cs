using System;
using System.Collections.Generic;

namespace LMS.Models.LMSModels
{
    public partial class EnrollIn
    {
        public string UId { get; set; } = null!;
        public int CatalogId { get; set; }
        public string Semester { get; set; } = null!;
        public string Grade { get; set; } = null!;

        public virtual Class Class { get; set; } = null!;
        public virtual Student UIdNavigation { get; set; } = null!;
    }
}
